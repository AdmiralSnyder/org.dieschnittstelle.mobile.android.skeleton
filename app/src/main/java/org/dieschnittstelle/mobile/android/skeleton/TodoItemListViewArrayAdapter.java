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

import org.dieschnittstelle.mobile.android.skeleton.data.Storage;
import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;
import org.dieschnittstelle.mobile.android.skeleton.pages.TodoDetailviewActivity;

import java.util.Comparator;
import java.util.Date;
public class TodoItemListViewArrayAdapter extends ArrayAdapter<TodoItem>
{
    Context mContext;

    @Override
    public void sort(@NonNull Comparator<? super TodoItem> comparator)
    {
        super.sort(SortDatesComparer);
    }

    public enum CompareModes
    {
        FavouriteDate,
        DateFavourite,
    }

    private CompareModes _CompareMode;
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

    private static Date getDateOnly(Date date) { return new Date(date.getYear(), date.getMonth(), date.getDate()); }

    private Comparator<TodoItem> SortDatesComparer = FavouriteFirstComparer;

    private static Comparator<TodoItem> FavouriteFirstComparer = (o1, o2) -> {
        int res = -Boolean.compare(o1.getIsDone(), o2.getIsDone());
        if (res == 0)
        {
            res = -Boolean.compare(o1.getIsFavourite(), o2.getIsFavourite());
            if (res == 0)
            {
                res = getDateOnly(o1.getDueDate()).compareTo(getDateOnly(o2.getDueDate()));
            }
        }
        return res;
    };

    private static Comparator<TodoItem> DateFirstComparer = (o1, o2) -> {
        int res = -Boolean.compare(o1.getIsDone(), o2.getIsDone());
        if (res == 0)
        {
            res = getDateOnly(o1.getDueDate()).compareTo(getDateOnly(o2.getDueDate()));
            if (res == 0)
            {
                res = -Boolean.compare(o1.getIsFavourite(), o2.getIsFavourite());
            }
        }
        return res;
    };

    public TodoItemListViewArrayAdapter(Context context)
    {
        super(context, R.layout.activity_overview_todoitem_view);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TodoItem todoItem = getItem(position);
        final View result;

        Button toggleIsFavouriteButton;
        CheckBox doneCB;
        Button showDetailsButton;
        ConstraintLayout constraintLayout_todoItem;

        var newView = convertView == null;
        if (newView)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_overview_todoitem_view, parent, false);
        }

        convertView.setTag(todoItem);
        showDetailsButton = convertView.findViewById(R.id.showDetailsButton);
        showDetailsButton.setTag(todoItem);

        toggleIsFavouriteButton = convertView.findViewById(R.id.toggleIsFavouriteButton);
        toggleIsFavouriteButton.setTag((todoItem));

        constraintLayout_todoItem = convertView.findViewById(R.id.constraintLayout_todoItem);

        doneCB = convertView.findViewById(R.id.doneCB);
        doneCB.setTag(todoItem);

        if (newView)
        {
            showDetailsButton.setOnClickListener(view ->
            {
                TodoItem todoItem2 = (TodoItem) view.getTag();
                Intent detailViewIntent = new Intent(mContext, TodoDetailviewActivity.class);
                detailViewIntent.putExtra("todoItemID", todoItem2.getID());
                mContext.startActivity(detailViewIntent);
            });

            toggleIsFavouriteButton.setOnClickListener(view ->
            {
                TodoItem todoItem2 = (TodoItem) view.getTag();
                todoItem2.setIsFavourite(!todoItem2.getIsFavourite());
                Storage.SetDbObj(todoItem2);
                view.setBackgroundColor(todoItem2.getIsFavourite()
                        ? Color.argb(255, 255, 0, 0)
                        : Color.argb(255, 0, 255, 0));
                Sort();
            });

            doneCB.setOnClickListener(view ->
            {
                TodoItem todoItem2 = (TodoItem) view.getTag();

                if (todoItem2.getIsDone() == doneCB.isChecked()) { return; }

                todoItem2.setIsDone(doneCB.isChecked());
                Storage.SetDbObj(todoItem2);
                Sort();
            });
        }

        result = convertView;

        toggleIsFavouriteButton.setBackgroundColor(todoItem.getIsFavourite()
                ? Color.argb(255, 255, 0, 0)
                : Color.argb(255, 0, 255, 0));
        doneCB.setChecked(todoItem.getIsDone());

        constraintLayout_todoItem.setBackgroundColor(!todoItem.getIsDone() && todoItem.getDueDate().before(new Date())
                ? Color.argb(255, 120, 255, 255)
                : Color.argb(255, 255, 255, 255));

        TextView nameTV = result.findViewById(R.id.nameTV);
        nameTV.setText(todoItem.getName());

        var dueDate = todoItem.getDueDate();
        if (dueDate != null)
        {
            TextView dueDateTV = result.findViewById(R.id.dueDateTV);
            dueDateTV.setText(String.format("%1$td.%1$tm.%1$tY  %1$tH:%1$tM", dueDate));
        }
        return result;
    }
}
