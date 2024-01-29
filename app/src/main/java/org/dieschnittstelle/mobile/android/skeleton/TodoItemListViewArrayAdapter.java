package org.dieschnittstelle.mobile.android.skeleton;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import org.dieschnittstelle.mobile.android.skeleton.data.Storage;
import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityOverviewTodoitemViewBinding;
import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;
import org.dieschnittstelle.mobile.android.skeleton.pages.TodoDetailviewActivity;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
public class TodoItemListViewArrayAdapter extends ArrayAdapter<TodoItem>
{
    public TodoItemListViewArrayAdapter(Context context, List<TodoItem> list)
    {
        super(context, R.layout.activity_overview_todoitem_view, list);
        mContext = context;
    }

    private final Context mContext;
    private Comparator<TodoItem> SortDatesComparer = FavouriteFirstComparer;

    public enum CompareModes
    {
        FavouriteDate,
        DateFavourite,
    }

    public void setCompareMode(CompareModes compareMode)
    {
        SortDatesComparer = compareMode == CompareModes.FavouriteDate
                ? FavouriteFirstComparer
                : DateFirstComparer;
        Sort();
    }

    public void Sort()
    {
        sort(SortDatesComparer);
    }


    private static final Comparator<TodoItem> FavouriteFirstComparer = (o1, o2) -> {
        int res = Boolean.compare(o1.getIsDone(), o2.getIsDone());
        if (res == 0)
        {
            res = -Boolean.compare(o1.getIsFavourite(), o2.getIsFavourite());
            if (res == 0)
            {
                res = CompareDates(o1.getDueDate(), o2.getDueDate());
            }
        }
        return res;
    };

    private static final Comparator<TodoItem> DateFirstComparer = (o1, o2) -> {
        int res = Boolean.compare(o1.getIsDone(), o2.getIsDone());
        if (res == 0)
        {
            res = CompareDates(o1.getDueDate(), o2.getDueDate());
            if (res == 0)
            {
                res = -Boolean.compare(o1.getIsFavourite(), o2.getIsFavourite());
            }
        }
        return res;
    };

    private static int CompareDates(Date d1, Date d2)
    {
        var res = Integer.compare(d1.getYear(), d2.getYear());
        if (res == 0)
        {
            res = Integer.compare(d1.getMonth(), d2.getMonth());
            if (res == 0)
            {
                res = Integer.compare(d1.getDate(), d2.getDate());
            }
        }
        return res;
    }



    private void DoWithTodoItem(View view, Consumer<TodoItem> continuation)
    {
        var binding = DataBindingUtil.findBinding(view);
        if (binding instanceof ActivityOverviewTodoitemViewBinding)
        {
            continuation.accept(((ActivityOverviewTodoitemViewBinding) binding).getItem());
        }
    }

    public void SetDbObjAndSort(View view)
    {
        DoWithTodoItem(view, todo ->
        {
            Storage.SetDbObj(todo);

            Sort();
        });
    }

    public void ToggleFavourite(View view)
    {
        DoWithTodoItem(view, todo ->
        {
            todo.setIsFavourite(!todo.getIsFavourite());
        });
        SetDbObjAndSort(view);
    }

    public void ShowTodoItemDetails(View view)
    {
        DoWithTodoItem(view, todo ->
        {
            Intent detailViewIntent = new Intent(mContext, TodoDetailviewActivity.class);
            detailViewIntent.putExtra("todoItemID", todo.getID());
            mContext.startActivity(detailViewIntent);
        });
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        TodoItem todoItem = getItem(position);

        ActivityOverviewTodoitemViewBinding itemBinding;
        final View result;

        var newView = convertView == null;
        if (newView)
        {
            itemBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.activity_overview_todoitem_view, parent, false);
            convertView = itemBinding.getRoot();
        }
        else
        {
            itemBinding = DataBindingUtil.getBinding(convertView);
        }

        assert itemBinding != null;
        itemBinding.setItem(todoItem);
        itemBinding.setVM(this);

        result = convertView;

        return result;
    }
}
