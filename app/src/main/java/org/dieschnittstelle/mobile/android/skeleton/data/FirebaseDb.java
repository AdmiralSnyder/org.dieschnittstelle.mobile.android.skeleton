package org.dieschnittstelle.mobile.android.skeleton.data;
import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.dieschnittstelle.mobile.android.skeleton.R;
import org.dieschnittstelle.mobile.android.skeleton.models.Login;
import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
public class FirebaseDb
{
    private static boolean _IsConnected;
    public static boolean IsConnected()
    {
        return _IsConnected;
    }
    private static Context Context;
    public static void Init(Context context)
    {
        Context = context;
    }

    public static void CheckConnectionAsync(Consumer<Boolean> continuation)
    {
        var instance = FirebaseDatabase.getInstance(Context.getString(R.string.URL_FIREBASE));
        var ref = instance.getReference().child("IsOnline");

        final boolean[] loginNeeded = {false};
        ref.addValueEventListener(new MyValueEventListener(() -> loginNeeded[0] = true));
        ref.get();

        ScheduledExecutorService scs = new ScheduledThreadPoolExecutor(1);
        scs.schedule(() ->
        {
            _IsConnected = loginNeeded[0];
            ((Activity)Context).runOnUiThread(() -> continuation.accept(loginNeeded[0]));
        }, 1000, TimeUnit.MILLISECONDS);
    }

    public static void TryLogin(String loginName, String password, Consumer<Boolean> continuation)
    {
        var instance = FirebaseDatabase.getInstance(Context.getString(R.string.URL_FIREBASE));
        var child = instance.getReference().child("Users");

        SystemClock.sleep(2000);

        child.get().addOnCompleteListener(new FirebaseLoginListListener(allLogins ->
        {
            boolean result = false;
            for (var login : allLogins)
            {
                if (login.getName().equals(loginName) && login.getPassword().equals(password))
                {
                    result = true;
                    break;
                }
            }
            boolean finalResult = result;
            ((Activity)Context).runOnUiThread(() -> continuation.accept(finalResult));
        }));
    }

    public static void GetTodos(Consumer<ArrayList<TodoItem>> continuation)
    {
        var child = GetLoginNode();
        child.get().addOnCompleteListener(new FirebaseTodoItemListListener(allTodos ->
        ((Activity)Context).runOnUiThread(() -> continuation.accept(allTodos))));
    }

    public static void SetDbObj(TodoItem todoItem)
    {
        var child = GetLoginNode().child(todoItem.getID());
        child.setValue(todoItem);
    }

    public static void SetDbObjs(ArrayList<TodoItem> todoItems)
    {
        var parent = GetLoginNode();
        for (var todoItem : todoItems)
        {
            var child = parent.child(todoItem.getID());
            child.setValue(todoItem);
        }
    }

    private static String CleanString(String str)
    {
        return str.replace("_", "__").replace("@", "_AT_").replace(".", "_DOT_");
    }

    private static DatabaseReference GetLoginNode()
    {
        var instance = FirebaseDatabase.getInstance(Context.getString(R.string.URL_FIREBASE));
        var child = instance.getReference()
                .child("TODOs")
                .child(CleanString(Login.CurrentLogin.getName()));
        return child;
    }
    public static void DeleteDbObj(TodoItem todoItem)
    {
        var child = GetLoginNode().child(todoItem.getID());
        child.removeValue();
    }

    public static void DeleteDbObjs()
    {
        var child = GetLoginNode();
        child.removeValue();
    }
}
