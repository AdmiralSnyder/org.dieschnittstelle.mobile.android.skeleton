package org.dieschnittstelle.mobile.android.skeleton;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
public class MyOnCompleteListener implements OnCompleteListener<DataSnapshot>
{
    public MyOnCompleteListener(IDataSnapshot dataSnapshotter)
    {
        DataSnapshotter = dataSnapshotter;
    }
    private IDataSnapshot DataSnapshotter;
    @Override
    public void onComplete(@NonNull Task<DataSnapshot> task)
    {
        if (task.isSuccessful())
        {
            for (var snapshot : task.getResult().getChildren())
            {
                DataSnapshotter.setDataSnapshot(snapshot);
                break;
            }
        }
    }
}
