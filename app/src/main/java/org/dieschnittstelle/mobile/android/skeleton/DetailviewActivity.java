package org.dieschnittstelle.mobile.android.skeleton;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        //...

        var buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(view ->
        {
            TodoItem.setName(editName.getText().toString());
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
