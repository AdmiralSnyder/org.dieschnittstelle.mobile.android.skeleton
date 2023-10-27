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
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
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

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_overview_todoitem_view, parent, false);

            convertView.setTag(todoItem);
            Button showDetailsButton = convertView.findViewById(R.id.showDetailsButton);
            showDetailsButton.setTag(todoItem);
            showDetailsButton.setOnClickListener(view ->
            {
                TodoItem todoItem2 = (TodoItem)view.getTag();
                Intent detailViewIntent = new Intent(mContext, DetailviewActivity.class);
                detailViewIntent.putExtra("todoItemID", todoItem2.getID());
                mContext.startActivity(detailViewIntent);
            });

            toggleIsFavouriteButton = convertView.findViewById(R.id.toggleIsFavouriteButton);
            toggleIsFavouriteButton.setOnClickListener(view ->
            {
                todoItem.setIsFavourite(!todoItem.getIsFavourite());
                Db.Instance.SetDbObj(todoItem);
                view.setBackgroundColor(todoItem.getIsFavourite()
                        ? Color.argb(255, 255, 0, 0)
                        : Color.argb(255, 0, 255, 0));
            });

            doneCB = convertView.findViewById(R.id.doneCB);
            doneCB.setTag(todoItem);
            doneCB.setOnClickListener(view ->
            {
                TodoItem todoItem2 = (TodoItem)view.getTag();

                if (todoItem2.getIsDone() == doneCB.isChecked()) { return; }

                todoItem2.setIsDone(doneCB.isChecked());
                Db.Instance.SetDbObj(todoItem2);
            });
        }
        else
        {
            toggleIsFavouriteButton = convertView.findViewById(R.id.toggleIsFavouriteButton);
            doneCB = convertView.findViewById(R.id.doneCB);
            convertView.setTag(todoItem);
        }

        result = convertView;

        TextView nameTV = result.findViewById(R.id.nameTV);
        TextView dueDateTV = result.findViewById(R.id.dueDateTV);

        toggleIsFavouriteButton.setBackgroundColor(todoItem.getIsFavourite()
                ? Color.argb(255, 255, 0, 0)
                : Color.argb(255, 0, 255, 0));
        doneCB.setChecked(todoItem.getIsDone());

        nameTV.setText(todoItem.getName());
        dueDateTV.setText(todoItem.getDescription());

        return result;
    }
}
