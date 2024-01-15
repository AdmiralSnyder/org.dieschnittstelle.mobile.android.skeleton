package org.dieschnittstelle.mobile.android.skeleton.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.dieschnittstelle.mobile.android.skeleton.data.FirebaseDb;
import org.dieschnittstelle.mobile.android.skeleton.R;
import org.dieschnittstelle.mobile.android.skeleton.TodoItemListViewArrayAdapter;
import org.dieschnittstelle.mobile.android.skeleton.data.Db;
import org.dieschnittstelle.mobile.android.skeleton.data.Storage;
import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;

import java.util.ArrayList;

public class TodoOverviewActivity extends AppCompatActivity
{
    // TODO Sortierung implementieren:
    // Todos sollen immer nach Erledigt/Nichterledigt sortiert sein und wahlweise nach Wichtigkeit+Datum oder nach Datum+Wichtigkeit (d.h. es gibt insgesamt genau 2
    // Sortieralternativen). Nicht erledigte Todos sollen vor erledigten Todos angezeigt werden. Eine der beiden Sortieralternativen soll immer aktiv sein, d.h. nicht erst bei Nutzerinteraktion aktiviert werden.


    private ListView TodoLV;
    private ArrayList<TodoItem> Todos = new ArrayList<>();
    private TodoItemListViewArrayAdapter TodoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        var buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(view ->
        {
            Intent detailViewIntent = new Intent(this, TodoDetailviewActivity.class);
            startActivity(detailViewIntent);
        });

        findViewById(R.id.buttonClearLocalDb).setOnClickListener(view -> Db.Instance.DropTable());
        findViewById(R.id.buttonSyncData).setOnClickListener(view -> Storage.Sync(this::RefreshData));
        findViewById(R.id.buttonUpsyncData).setOnClickListener(view -> Storage.SyncUp(this::RefreshData));
        findViewById(R.id.buttonDownsyncData).setOnClickListener(view -> Storage.SyncDown(this::RefreshData));

        if (!FirebaseDb.IsConnected())
        {
            TextView welcomeTV = findViewById(R.id.welcomeTV);
            welcomeTV.setText(R.string.OverviewHeading + " - OFFLINE");
        }

        TodoLV = findViewById(R.id.todoLV);
        TodoAdapter = new TodoItemListViewArrayAdapter(this);
        TodoLV.setAdapter(TodoAdapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        RefreshData();
    }

    protected void RefreshData()
    {
        TodoAdapter.clear();
        TodoAdapter.addAll(Db.Instance.GetDbObjs());
    }
}
