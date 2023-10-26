package org.dieschnittstelle.mobile.android.skeleton;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

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

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_overview_todoitem_view, parent, false);

            convertView.setOnClickListener(view ->
            {
                Intent detailViewIntent = new Intent(mContext, DetailviewActivity.class);
                detailViewIntent.putExtra("todoItemID", todoItem.getID());
                mContext.startActivity(detailViewIntent);
            });
        }

        result = convertView;

        EditText nameEdit = result.findViewById(R.id.nameTV);
        EditText descriptionEdit = result.findViewById(R.id.descriptionTV);

        nameEdit.setText(todoItem.getName());
        descriptionEdit.setText(todoItem.getDescription());

        return result;
    }
}
