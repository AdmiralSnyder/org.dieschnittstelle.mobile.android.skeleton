package org.dieschnittstelle.mobile.android.skeleton;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import java.util.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class Db
{
    private Db(Context context) { OpenHelper = new TodoItemSqLiteOpenHelper(context); }

    public static void Init(Context context) { Instance = new Db(context); }

    public ArrayList<TodoItem> GetDbObjs()
    {
        SQLiteDatabase db = OpenHelper.getReadableDatabase();
        try
        {
            var cursor = db.query(DataContracts.TodoItem.TABLE_NAME, null, null, null, null, null, null);
            try
            {
                ArrayList<TodoItem> result = new ArrayList<>();
                while (cursor.moveToNext())
                {
                    result.add(CreateDbObj(cursor));
                }
                return result;
            } finally
            {
                cursor.close();
            }
        } finally
        {
            db.close();
        }
    }

    public void DeleteDbObj(String id)
    {
        SQLiteDatabase db = OpenHelper.getWritableDatabase();
        try
        {
            String[] args = {id};
            db.delete(DataContracts.TodoItem.TABLE_NAME, "ID = ?", args);
        } finally
        {
            db.close();
        }
    }

    public ArrayList<String> GetDbObjIDs()
    {
        SQLiteDatabase db = OpenHelper.getReadableDatabase();
        try
        {
            String[] columns = {DataContracts.TodoItem.COLUMN_NAME_ID};
            var cursor = db.query(DataContracts.TodoItem.TABLE_NAME, columns, null, null, null, null, null);
            try
            {
                ArrayList<String> result = new ArrayList<>();
                while (cursor.moveToNext())
                {
                    result.add(cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_ID));
                }
                return result;
            } finally
            {
                cursor.close();
            }
        } finally
        {
            db.close();
        }
    }

    public TodoItem GetDbObj(String id)
    {
        SQLiteDatabase db = OpenHelper.getReadableDatabase();
        try
        {
            String[] args = {id};
            var cursor = db.query(DataContracts.TodoItem.TABLE_NAME, null, "ID = ?", args, null, null, null);
            try
            {
                if (cursor.moveToNext())
                {
                    return CreateDbObj(cursor);
                }
                return null;
            } finally
            {
                cursor.close();
            }
        } finally
        {
            db.close();
        }
    }
    @NonNull
    private static TodoItem CreateDbObj(Cursor cursor)
    {
        var result = new TodoItem();
        result.setID(cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_ID));
        result.setRemoteID(cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_RemoteID));
        result.setName(cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_Name));
        result.setDescription(cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_Description));
        result.setIsDone(cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_IsDone) == Boolean.toString(true));
        result.setIsFavourite(cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_IsFavourite) == Boolean.toString(true));
        var dueDateStr = cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_DueDate);

        Date dueDate;
        try
        {
            dueDate = Date.from(Instant.parse(dueDateStr));
        }
        catch (Exception e)
        {
            dueDate = null;
        }
        result.setDueDate(dueDate);
        return result;
    }

    public void SetDbObj(TodoItem todoItem)
    {
        SQLiteDatabase db = OpenHelper.getWritableDatabase();
        try
        {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();

            var id = todoItem.getID();
            var inserting = id == null;

            values.put(DataContracts.TodoItem.COLUMN_NAME_RemoteID, todoItem.getRemoteID());
            values.put(DataContracts.TodoItem.COLUMN_NAME_Name, todoItem.getName());
            values.put(DataContracts.TodoItem.COLUMN_NAME_Description, todoItem.getDescription());
            values.put(DataContracts.TodoItem.COLUMN_NAME_IsFavourite, todoItem.getIsFavourite());
            values.put(DataContracts.TodoItem.COLUMN_NAME_IsDone, todoItem.getIsDone());
            var dueDate = todoItem.getDueDate();
            values.put(DataContracts.TodoItem.COLUMN_NAME_DueDate, dueDate == null ? null : dueDate.toInstant().toString());

            if (inserting)
            {

                id = UUID.randomUUID().toString();
                todoItem.setID(id);
                values.put(DataContracts.TodoItem.COLUMN_NAME_ID, todoItem.getID());

                // Insert the new row, returning the primary key value of the new row
                db.insert(DataContracts.TodoItem.TABLE_NAME, null, values);
            }
            else
            {
                String[] args = {id};
                db.update(DataContracts.TodoItem.TABLE_NAME, values, "ID = ?", args);
            }
        } finally
        {
            db.close();
        }
    }

    public static Db Instance;
    private TodoItemSqLiteOpenHelper OpenHelper;
}
