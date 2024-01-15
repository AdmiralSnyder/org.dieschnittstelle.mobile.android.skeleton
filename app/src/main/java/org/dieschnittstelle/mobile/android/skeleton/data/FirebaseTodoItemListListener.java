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
                item.setID((String)data.get("id"));
                item.setName((String)data.get("name"));
                item.setDescription((String)data.get("description"));

                var dueDateStr = (Map<String, Object>)data.get("dueDate");

                var year = (Long)dueDateStr.get("year");
                var month = (Long)dueDateStr.get("month");
                var day = (Long)dueDateStr.get("day");
                var hour = (Long)dueDateStr.get("hours");
                var min = (Long)dueDateStr.get("minutes");
                var sec = (Long)dueDateStr.get("seconds");

                var date = new Date(year.intValue() - 1900, month.intValue(), day.intValue(), hour.intValue(), min.intValue(), sec.intValue());
//                date.setYear();
                item.setDueDate(date);
                item.setIsDone((Boolean)data.get("isDone"));
                item.setIsFavourite((Boolean)data.get("isFavourite"));

                list.add(item);
            }
            Continuation.accept(list);
        }

    }
}
