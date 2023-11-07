package org.dieschnittstelle.mobile.android.skeleton.data;
import android.content.Context;

import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;
public class Storage
{
    private static Context Context;
    public static void Init(Context context)
    {
        Context = context;
        Db.Init(context);
        FirebaseDb.Init(context);
    }
    public static void SetDbObj(TodoItem todoItem)
    {
        Db.Instance.SetDbObj(todoItem);
        if (FirebaseDb.IsConnected())
        {
            FirebaseDb.SetDbObj(todoItem);
        }
    }
    public static void DeleteDbObj(TodoItem todoItem)
    {
        Db.Instance.DeleteDbObj(todoItem);
        if (FirebaseDb.IsConnected())
        {
            FirebaseDb.DeleteDbObj(todoItem);
        }
    }
}
