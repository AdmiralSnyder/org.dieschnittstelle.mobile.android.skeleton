package org.dieschnittstelle.mobile.android.skeleton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity
{
    private ListView TodoLV;
    private ArrayList<TodoItem> Todos = new ArrayList<>();
    private TodoItemListViewArrayAdapter TodoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        InitDataBase();

        var buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(view ->
        {
            Intent detailViewIntent = new Intent(this, DetailviewActivity.class);
            startActivity(detailViewIntent);
        });

        TodoLV = findViewById(R.id.todoLV);
        TodoAdapter = new TodoItemListViewArrayAdapter(this);

        TodoLV.setAdapter(TodoAdapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        TodoAdapter.clear();
        TodoAdapter.addAll(Db.Instance.GetDbObjs());
    }

    // TODO im onDestroy die Db schließen oder besser im onStart?
    // siehe https://developer.android.com/guide/components/activities/activity-lifecycle
    private void InitDataBase() { Db.Init(getApplicationContext()); }
}
