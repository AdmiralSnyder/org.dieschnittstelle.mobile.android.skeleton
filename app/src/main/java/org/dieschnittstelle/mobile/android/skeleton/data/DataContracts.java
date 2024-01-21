package org.dieschnittstelle.mobile.android.skeleton.data;
public class DataContracts
{
    private DataContracts() {}

    /* Inner class that defines the table contents */
    public static class TodoItem {
        public static final String TABLE_NAME = "TodoItem";

        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_Name = "Name";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsDone = "IsDone";
        public static final String COLUMN_NAME_IsFavourite = "IsFavourite";
        public static final String COLUMN_NAME_DueDate = "DueDate";
        public static final String COLUMN_NAME_ContactIDs = "ContactIDs";

        public static final int COLUMN_INDEX_ID = 0;
        public static final int COLUMN_INDEX_Name = 1;
        public static final int COLUMN_INDEX_Description = 2;
        public static final int COLUMN_INDEX_IsDone = 3;
        public static final int COLUMN_INDEX_IsFavourite = 4;
        public static final int COLUMN_INDEX_DueDate = 5;

        public static final int COLUMN_INDEX_ContactIDs = 6;
    }

    public static final String SQLITE_SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TodoItem.TABLE_NAME + " (" +
                    TodoItem.COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
                    TodoItem.COLUMN_NAME_Name + " TEXT," +
                    TodoItem.COLUMN_NAME_Description + " TEXT," +
                    TodoItem.COLUMN_NAME_IsDone + " TEXT," +
                    TodoItem.COLUMN_NAME_IsFavourite + " TEXT," +
                    TodoItem.COLUMN_NAME_DueDate + " TEXT," +
                    TodoItem.COLUMN_NAME_ContactIDs + " TEXT)";

    public static final String SQLITE_SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TodoItem.TABLE_NAME;
}
