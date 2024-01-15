package org.dieschnittstelle.mobile.android.skeleton.data;
import android.app.Activity;
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
    public static void Sync(Runnable continuation)
    {
        /*
        * Ist die Webanwendung beim Start der Android Anwendung verfügbar, soll der folgende "Abgleich" implementiert werden: - liegen lokale Todos vor, dann werden alle Todos auf Seiten der Web Applikation gelöscht und die lokalen Todos an die Web Applikation übertragen.
    - liegen keine lokalen Todos vor, dann werden alle Todos von der Web Applikation auf die lokale Datenbank übertragen.
        * */
        if (Db.Instance.HasDbObjs())
        {
            SyncDown(continuation);
        }
        else
        {
            SyncUp(continuation);
        }
    }

    public static void SyncUp(Runnable continuation)
    {
        /*
         * - liegen lokale Todos vor, dann werden alle Todos auf Seiten der
         *   Web Applikation gelöscht und die lokalen Todos an die Web
         *   Applikation übertragen.
         */
        FirebaseDb.DeleteDbObjs();
        FirebaseDb.SetDbObjs(Db.Instance.GetDbObjs());

        if (continuation != null)
        {
            ((Activity)Context).runOnUiThread(continuation);
        }
    }

    public static void SyncDown(Runnable continuation)
    {
        /*
         * - liegen keine lokalen Todos vor, dann werden alle Todos von der
         *   Web Applikation auf die lokale Datenbank übertragen
         */
        FirebaseDb.GetTodos(todos ->
        {
            Db.Instance.SetDbObjsAsync(todos);

            if (continuation != null)
            {
                ((Activity)Context).runOnUiThread(continuation);
            }
        });
    }
}
