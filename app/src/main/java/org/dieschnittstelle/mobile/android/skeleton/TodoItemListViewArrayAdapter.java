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

import org.dieschnittstelle.mobile.android.skeleton.data.Storage;
import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;
import org.dieschnittstelle.mobile.android.skeleton.pages.TodoDetailviewActivity;
public class TodoItemListViewArrayAdapter extends ArrayAdapter<TodoItem>
{
    Context mContext;

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

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_overview_todoitem_view, parent, false);
        }

        convertView.setTag(todoItem);
        showDetailsButton = convertView.findViewById(R.id.showDetailsButton);
        showDetailsButton.setTag(todoItem);
        showDetailsButton.setOnClickListener(view ->
        {
            TodoItem todoItem2 = (TodoItem) view.getTag();
            Intent detailViewIntent = new Intent(mContext, TodoDetailviewActivity.class);
            detailViewIntent.putExtra("todoItemID", todoItem2.getID());
            mContext.startActivity(detailViewIntent);
        });

        toggleIsFavouriteButton = convertView.findViewById(R.id.toggleIsFavouriteButton);
        toggleIsFavouriteButton.setTag((todoItem));
        toggleIsFavouriteButton.setOnClickListener(view ->
        {
            TodoItem todoItem2 = (TodoItem) view.getTag();
            todoItem2.setIsFavourite(!todoItem2.getIsFavourite());
            Storage.SetDbObj(todoItem2);
            view.setBackgroundColor(todoItem2.getIsFavourite()
                    ? Color.argb(255, 255, 0, 0)
                    : Color.argb(255, 0, 255, 0));
        });

        doneCB = convertView.findViewById(R.id.doneCB);
        doneCB.setTag(todoItem);
        doneCB.setOnClickListener(view ->
        {
            TodoItem todoItem2 = (TodoItem) view.getTag();

            if (todoItem2.getIsDone() == doneCB.isChecked()) { return; }

            todoItem2.setIsDone(doneCB.isChecked());
            Storage.SetDbObj(todoItem2);
        });

        result = convertView;

        TextView nameTV = result.findViewById(R.id.nameTV);
        TextView dueDateTV = result.findViewById(R.id.dueDateTV);

        toggleIsFavouriteButton.setBackgroundColor(todoItem.getIsFavourite()
                ? Color.argb(255, 255, 0, 0)
                : Color.argb(255, 0, 255, 0));
        doneCB.setChecked(todoItem.getIsDone());

        nameTV.setText(todoItem.getName());
        var dueDate = todoItem.getDueDate();
        if (dueDate != null)
        {
            dueDateTV.setText(dueDate.getDate() + "." + (dueDate.getMonth() + 1) + "." + dueDate.getYear() + "  " + dueDate.getHours() + ":" + dueDate.getMinutes());
        }
        return result;
    }
}
