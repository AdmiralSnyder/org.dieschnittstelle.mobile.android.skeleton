package org.dieschnittstelle.mobile.android.skeleton.data;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
public class MyValueEventListener implements ValueEventListener
{
    private Runnable Continuation;

    public MyValueEventListener(Runnable continuation)
    {
        Continuation = continuation;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot)
    {
        Continuation.run();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) { }
}
