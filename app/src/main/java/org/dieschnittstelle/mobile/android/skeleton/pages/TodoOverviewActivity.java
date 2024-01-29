package org.dieschnittstelle.mobile.android.skeleton.pages;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.dieschnittstelle.mobile.android.skeleton.data.FirebaseDb;
import org.dieschnittstelle.mobile.android.skeleton.R;
import org.dieschnittstelle.mobile.android.skeleton.TodoItemListViewArrayAdapter;
import org.dieschnittstelle.mobile.android.skeleton.data.Db;
import org.dieschnittstelle.mobile.android.skeleton.data.Storage;
import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityTodoOverviewBinding;
import org.dieschnittstelle.mobile.android.skeleton.viewmodels.TodoOverviewViewModel;

public class TodoOverviewActivity extends ActivityBase<TodoOverviewViewModel>
{
    private ListView TodoLV;
    private TodoItemListViewArrayAdapter TodoAdapter;

    @Override
    public Class<TodoOverviewViewModel> getViewModelClass() { return TodoOverviewViewModel.class; }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        var dataBinding = (ActivityTodoOverviewBinding)DataBindingUtil.setContentView(this, R.layout.activity_todo_overview);

        var vm = getViewModel();
        dataBinding.setVM(vm);

        if (!FirebaseDb.IsConnected())
        {
            TextView welcomeTV = findViewById(R.id.welcomeTV);
            welcomeTV.setText("Übersicht der TODOs - OFFLINE");
        }

        TodoLV = findViewById(R.id.todoLV);
        TodoAdapter = new TodoItemListViewArrayAdapter(this, vm.getTodoItems());
        TodoLV.setAdapter(TodoAdapter);
    }

    public void AddTodoItem(View view) { startActivity(new Intent(this, TodoDetailviewActivity.class)); }

    @Override
    protected void onViewModelInit(TodoOverviewViewModel viewModel)
    {
        super.onViewModelInit(viewModel);
        RefreshData();
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
            DoAndRefresh(FirebaseDb::DeleteDbObjs);
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
    protected void onRestart()
    {
        // kommt immer, wenn wir aus einer anderen activity zurückkehren - in dem Fall, aus der Detail-Acitivity.
        // kommt NICHT, wenn der bildschirm gedreht wurde.
        super.onRestart();
        RefreshData();
    }

    protected void RefreshData()
    {
        // TODO warten oder nicht?
        //SystemClock.sleep(2000);

        TodoAdapter.clear();
        TodoAdapter.addAll(Db.Instance.GetDbObjs());

        TodoAdapter.Sort();
    }
}
