package org.dieschnittstelle.mobile.android.skeleton.data;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import org.dieschnittstelle.mobile.android.skeleton.models.Login;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
public class FirebaseLoginListListener implements OnCompleteListener<DataSnapshot>
{
    private Consumer<ArrayList<Login>> Continuation;
    private CountDownLatch Latch;

    public FirebaseLoginListListener(Consumer<ArrayList<Login>> continuation)
    {
        Continuation = continuation;
    }

    @Override
    public void onComplete(@NonNull Task<DataSnapshot> task)
    {
        if (task.isSuccessful())
        {
            ArrayList<Login> list = new ArrayList<>();

            for (var snapshot : task.getResult().getChildren())
            {
                list.add(snapshot.getValue(Login.class));
            }
            Continuation.accept(list);
        }

    }
}
