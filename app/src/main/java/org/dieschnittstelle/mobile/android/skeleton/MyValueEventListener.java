package org.dieschnittstelle.mobile.android.skeleton;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
public class MyValueEventListener implements ValueEventListener
{
    public MyValueEventListener(IDataSnapshot dataSnapshotter)
    {
        DataSnapshotter = dataSnapshotter;
    }

    private  IDataSnapshot DataSnapshotter;
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot)
    {
        DataSnapshotter.setDataSnapshot(snapshot);
    }
    @Override
    public void onCancelled(@NonNull DatabaseError error)
    {

    }
}
