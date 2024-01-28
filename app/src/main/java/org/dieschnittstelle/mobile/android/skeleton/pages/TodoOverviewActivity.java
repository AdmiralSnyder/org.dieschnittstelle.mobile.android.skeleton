package org.dieschnittstelle.mobile.android.skeleton.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.dieschnittstelle.mobile.android.skeleton.data.FirebaseDb;
import org.dieschnittstelle.mobile.android.skeleton.R;
import org.dieschnittstelle.mobile.android.skeleton.TodoItemListViewArrayAdapter;
import org.dieschnittstelle.mobile.android.skeleton.data.Db;
import org.dieschnittstelle.mobile.android.skeleton.data.Storage;
import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;

import java.util.Comparator;
import java.util.Date;

public class TodoOverviewActivity extends AppCompatActivity
{
    private ListView TodoLV;
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.overview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        var id = item.getItemId();
        if (id == R.id.menuitemSortFavourite)
        {
            TodoAdapter.setCompareMode(TodoItemListViewArrayAdapter.CompareModes.FavouriteDate);
        }
        else if (id == R.id.menuitemSortDate)
        {
            TodoAdapter.setCompareMode(TodoItemListViewArrayAdapter.CompareModes.DateFavourite);
        }
        else if (id == R.id.menuitemDeleteCloud)
        {
            DoAndRefresh(() -> FirebaseDb.DeleteDbObjs());
        }
        else if (id == R.id.menuitemDeleteLocal)
        {
            DoAndRefresh(() -> Db.Instance.DropTable());
        }
        else if (id == R.id.menuitemSyncDown)
        {
            Storage.SyncDown(this::RefreshData);
        }
        else if (id == R.id.menuitemSyncUp)
        {
            Storage.SyncUp(this::RefreshData);
        }
        else if (id == R.id.menuitemSyncUpDown)
        {
            Storage.Sync(this::RefreshData);
        }
        return true;
    }

    private void DoAndRefresh(Runnable action)
    {
        action.run();
        RefreshData();
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
        TodoAdapter.Sort();
    }
}
