package org.dieschnittstelle.mobile.android.skeleton.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.dieschnittstelle.mobile.android.skeleton.data.DataContracts;
import org.dieschnittstelle.mobile.android.skeleton.data.MyValueEventListener;
import org.dieschnittstelle.mobile.android.skeleton.R;
import org.dieschnittstelle.mobile.android.skeleton.TodoItemListViewArrayAdapter;
import org.dieschnittstelle.mobile.android.skeleton.data.Db;
import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;

import java.util.ArrayList;

public class TodoOverviewActivity extends AppCompatActivity
{
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

        var buttonClearLocalDb = findViewById(R.id.buttonClearLocalDb);
        buttonClearLocalDb.setOnClickListener(view ->
        {
            Db.Instance.DropTable();
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
}
