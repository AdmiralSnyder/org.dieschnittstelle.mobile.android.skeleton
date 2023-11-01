package org.dieschnittstelle.mobile.android.skeleton;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class LoginActivity extends AppCompatActivity implements IDataSnapshot
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        var instance = FirebaseDatabase.getInstance(getString(R.string.URL_FIREBASE));
        var ref = instance.getReference().child(".info/connected");

        ref.addValueEventListener(new MyValueEventListener(this));
        ref.get();

        ScheduledExecutorService scs = new ScheduledThreadPoolExecutor(0);
        var _this = this;
        scs.schedule(new Runnable()
        {
            @Override
            public void run()
            {
                if (LoginNeeded)
                {
                    Button button_login = findViewById(R.id.button_login);
                    button_login.setEnabled(true);
                }
                else
                {
                    startActivity(new Intent(_this, OverviewActivity.class));
                }
            }
        }, 1000, TimeUnit.MILLISECONDS);

    }
    private boolean LoginNeeded;

    @Override
    public DataSnapshot getDataSnapshot()
    {
        return null;
    }
    @Override
    public void setDataSnapshot(DataSnapshot dataSnapshot)
    {
        if (dataSnapshot.getValue(Boolean.class))
        {
            LoginNeeded = true;
        }
    }
}
