package org.dieschnittstelle.mobile.android.skeleton;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class DetailviewActivity extends AppCompatActivity
{
    TodoItem TodoItem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailview);

        var intent = getIntent();
        var todoItemID = intent.getStringExtra("todoItemID");

        if (todoItemID == null)
        {
            TodoItem = new TodoItem();
        }
        else
        {
            TodoItem = Db.Instance.GetDbObj(todoItemID);
        }

        EditText editName = findViewById(R.id.editName);
        editName.setText(TodoItem.getName());
        EditText editDescription = findViewById(R.id.editDescription);
        editDescription.setText(TodoItem.getDescription());

        CheckBox checkboxIsDone = findViewById(R.id.checkboxIsDone);
        checkboxIsDone.setChecked(TodoItem.getIsDone());
        CheckBox checkboxIsFavourite = findViewById(R.id.checkboxIsFavourite);
        checkboxIsFavourite.setChecked(TodoItem.getIsFavourite());

        DatePicker datePickerDueDate = findViewById(R.id.datePickerDueDate);
        TimePicker timePickerDueDate = findViewById(R.id.timePickerDueDate);

        Date dueDate = TodoItem.getDueDate();
        if (dueDate != null)
        {
            datePickerDueDate.updateDate(dueDate.getYear(), dueDate.getMonth(), dueDate.getDay());
            timePickerDueDate.setHour(dueDate.getHours());
            timePickerDueDate.setMinute(dueDate.getMinutes());
        }

        //...

        var buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(view ->
        {
            TodoItem.setName(editName.getText().toString());
            TodoItem.setDescription(editDescription.getText().toString());
            TodoItem.setIsDone(checkboxIsDone.isChecked());
            TodoItem.setIsFavourite(checkboxIsFavourite.isChecked());

            TodoItem.setDueDate(new Date(
                    datePickerDueDate.getYear(),
                    datePickerDueDate.getMonth(),
                    datePickerDueDate.getDayOfMonth(),
                    timePickerDueDate.getHour(),
                    timePickerDueDate.getMinute()));

            //...

            Db.Instance.SetDbObj(TodoItem);
            finish();
        });

        var buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(view ->
        {
            Db.Instance.DeleteDbObj(TodoItem.getID());
            finish();
        });
    }
}
