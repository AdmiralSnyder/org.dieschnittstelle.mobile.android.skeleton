package org.dieschnittstelle.mobile.android.skeleton.data;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import org.dieschnittstelle.mobile.android.skeleton.models.Login;
import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
public class FirebaseTodoItemListListener implements OnCompleteListener<DataSnapshot>
{
    private Consumer<ArrayList<TodoItem>> Continuation;
    private CountDownLatch Latch;

    public FirebaseTodoItemListListener(Consumer<ArrayList<TodoItem>> continuation)
    {
        Continuation = continuation;
    }

    @Override
    public void onComplete(@NonNull Task<DataSnapshot> task)
    {
        if (task.isSuccessful())
        {
            ArrayList<TodoItem> list = new ArrayList<>();

            for (var snapshot : task.getResult().getChildren())
            {
                TodoItem item = null;
                Map<String, Object> data = (Map<String, Object>) snapshot.getValue(true);

                // doing it myself because date doesn't work
                item = new TodoItem();
                item.setID((String)data.get("ID"));
                item.setName((String)data.get("Name"));
                item.setDescription((String)data.get("Description"));

                var dueDateStr = (String)data.get("DueDate");
                item.setDueDateStr(dueDateStr);
                item.setIsDone((Boolean)data.get("IsDone"));
                item.setIsFavourite((Boolean)data.get("IsFavourite"));

                list.add(item);
            }
            Continuation.accept(list);
        }

    }
}
