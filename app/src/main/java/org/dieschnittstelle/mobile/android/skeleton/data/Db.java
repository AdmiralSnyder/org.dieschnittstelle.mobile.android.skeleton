package org.dieschnittstelle.mobile.android.skeleton.data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;

import java.util.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class Db
{
    private Db(Context context) { OpenHelper = new TodoItemSqLiteOpenHelper(context); }

    public static void Init(Context context) { Instance = new Db(context); }

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

    public boolean HasDbObjs()
    {
        SQLiteDatabase db = OpenHelper.getReadableDatabase();
        try
        {
            var cursor = db.query(DataContracts.TodoItem.TABLE_NAME, null, null, null, null, null, null);
            try
            {
                return cursor.moveToNext();
            } finally
            {
                cursor.close();
            }
        } finally
        {
            db.close();
        }
    }

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

    public void DeleteDbObj(TodoItem todoItem)
    {
        SQLiteDatabase db = OpenHelper.getWritableDatabase();
        try
        {
            String[] args = {todoItem.getID()};
            db.delete(DataContracts.TodoItem.TABLE_NAME, "ID = ?", args);
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
        result.setName(cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_Name));
        result.setDescription(cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_Description));
        result.setIsDone(cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_IsDone).equals("1"));
        result.setIsFavourite(cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_IsFavourite).equals("1"));

        var contactIDs = result.getContactIDs();
        var contactIDValuess = cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_ContactIDs);
        for (String contactID : cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_ContactIDs).split(";"))
        {
            if(contactID.isEmpty())
            {
                continue;
            }
            contactIDs.add(Long.parseLong(contactID));
        }

        var dueDateStr = cursor.getString(DataContracts.TodoItem.COLUMN_INDEX_DueDate);

        Date dueDate;
        try
        {
            dueDate = Date.from(Instant.parse(dueDateStr));
        } catch (Exception e)
        {
            dueDate = null;
        }
        result.setDueDate(dueDate);
        return result;
    }

    public void SetDbObj(TodoItem todoItem)
    {
        SetDbObj(todoItem, false);
    }
    public void SetDbObj(TodoItem todoItem, boolean insert)
    {
        SQLiteDatabase db = OpenHelper.getWritableDatabase();
        try
        {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();

            var id = todoItem.getID();
            var inserting = insert || (id == null);

            values.put(DataContracts.TodoItem.COLUMN_NAME_Name, todoItem.getName());
            values.put(DataContracts.TodoItem.COLUMN_NAME_Description, todoItem.getDescription());
            values.put(DataContracts.TodoItem.COLUMN_NAME_IsDone, todoItem.getIsDone() ? "1" : "0");
            values.put(DataContracts.TodoItem.COLUMN_NAME_IsFavourite, todoItem.getIsFavourite() ? "1" : "0");
            var dueDate = todoItem.getDueDate();
            values.put(DataContracts.TodoItem.COLUMN_NAME_DueDate, dueDate == null ? null : dueDate.toInstant().toString());
            values.put(DataContracts.TodoItem.COLUMN_NAME_ContactIDs, todoItem.getContactsStr());

            if (inserting)
            {
                if (id == null)
                {
                    id = UUID.randomUUID().toString();
                    todoItem.setID(id);
                }
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
    public void ClearDbObjsAsync()
    {
        SQLiteDatabase db = OpenHelper.getWritableDatabase();
        try
        {
            db.delete(DataContracts.TodoItem.TABLE_NAME, "1 = 1", null);
        }
        finally
        {
            db.close();
        }
    }

    public void DropTable()
    {
        SQLiteDatabase db = OpenHelper.getWritableDatabase();
        try
        {
            db.execSQL(DataContracts.SQLITE_SQL_DELETE_ENTRIES);
            db.execSQL(DataContracts.SQLITE_SQL_CREATE_ENTRIES);
        } finally
        {
            db.close();
        }
    }

    public void SetDbObjsAsync(ArrayList<TodoItem> todos)
    {
        for (var todo : todos)
        {
            SetDbObj(todo, true);
        }
    }
}
