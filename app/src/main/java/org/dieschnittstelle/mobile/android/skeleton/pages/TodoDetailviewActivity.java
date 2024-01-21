package org.dieschnittstelle.mobile.android.skeleton.pages;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.dieschnittstelle.mobile.android.skeleton.ContactInfoListViewArrayAdapter;
import org.dieschnittstelle.mobile.android.skeleton.R;
import org.dieschnittstelle.mobile.android.skeleton.TodoItemListViewArrayAdapter;
import org.dieschnittstelle.mobile.android.skeleton.data.Db;
import org.dieschnittstelle.mobile.android.skeleton.data.Storage;
import org.dieschnittstelle.mobile.android.skeleton.models.ContactInfo;
import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;

import java.util.ArrayList;
import java.util.Date;

public class TodoDetailviewActivity extends AppCompatActivity
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

        ListView listViewContacts = findViewById(R.id.listViewContacts);

        ContactInfoAdapter = new ContactInfoListViewArrayAdapter(this);
        listViewContacts.setAdapter(ContactInfoAdapter);


        for (long contactID : TodoItem.getContactIDs())
        {
            ContactInfoAdapter.add(LoadContactData(contactID));
        }

        DatePicker datePickerDueDate = findViewById(R.id.datePickerDueDate);
        TimePicker timePickerDueDate = findViewById(R.id.timePickerDueDate);

        Date dueDate = TodoItem.getDueDate();
        if (dueDate != null)
        {
            datePickerDueDate.updateDate(dueDate.getYear() + 1900, dueDate.getMonth(), dueDate.getDate());
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
                    datePickerDueDate.getYear() - 1900,
                    datePickerDueDate.getMonth(),
                    datePickerDueDate.getDayOfMonth(),
                    timePickerDueDate.getHour(),
                    timePickerDueDate.getMinute()));
            //...

            Storage.SetDbObj(TodoItem);
            finish();
        });

        findViewById(R.id.buttonAddContact).setOnClickListener(view ->
        {
            SelectContactLauncher.launch(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI));
        });

        var buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(view ->
        {
            new AlertDialog.Builder(this)
                    .setMessage("TODO wirklich löschen?")
                    .setPositiveButton("Yes", (dialog, button) ->
                    {
                        Storage.DeleteDbObj(TodoItem);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    ContactInfoListViewArrayAdapter ContactInfoAdapter;

    private boolean HandlePermission(long tag)
    {
        var hasReadContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if (hasReadContactsPermission == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }

        ContactIDToLoadAfterPermissionGranted = tag;
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUESTCODE_PERMISSION_READ_CONTACTS);
        return false;
    }

    private Long ContactIDToLoadAfterPermissionGranted;

    private static final int REQUESTCODE_PERMISSION_READ_CONTACTS = 10;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTCODE_PERMISSION_READ_CONTACTS)
        {
            // TODO zusätzlich prüfen, ob erfolgreich.
            if (ContactIDToLoadAfterPermissionGranted != null)
            {
                AddContactData(ContactIDToLoadAfterPermissionGranted);
            }
        }
    }

    private void AddContactData(long contactID)
    {
        TodoItem.getContactIDs().add(contactID);
        var contactInfo = LoadContactData(contactID);
        if (contactInfo != null)
        {
            ContactInfoAdapter.add(contactInfo);
        }
    }

    public void DeleteContact(int position)
    {
        TodoItem.getContactIDs().remove(position);
        ContactInfoAdapter.remove(ContactInfoAdapter.getItem(position));
    }

    private ContactInfo LoadContactData(long contactID)
    {
        ContactInfo result = new ContactInfo();
        result.setContactID(contactID);
        result.setParent(this);
        result.setTodoItem(TodoItem);

        var contactIDParam = new String[]{String.valueOf(contactID)};
        var contactCursor = getContentResolver()
                .query(ContactsContract.Contacts.CONTENT_URI, null,  ContactsContract.Contacts._ID + "=?", contactIDParam, null);
        try
        {
            var contactDisplayNameColIdx = contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            if (contactCursor.moveToFirst())
            {
                result.setName(contactCursor.getString(contactDisplayNameColIdx));
            }
            else
            {
                return null;
            }
        } finally
        {
            contactCursor.close();
        }
        var phoneCursor = getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id=?",
                        contactIDParam, null);
        var phoneCursorNumberColIdx = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        var phoneCursorTypeColIdx = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
        while (phoneCursor.moveToNext())
        {
            var number = phoneCursor.getString(phoneCursorNumberColIdx);
            var type = phoneCursor.getInt(phoneCursorTypeColIdx);
            if (type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
            {
                result.setMobilePhone(number);
            }
        }
        phoneCursor.close();

        var emailCursor = getContentResolver()
                .query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?",
                        contactIDParam, null);
        var emailCursorAddressColIdx = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
        var emailCursorTypeColIdx = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE);
        while (emailCursor.moveToNext())
        {
            var address = emailCursor.getString(emailCursorAddressColIdx);
            result.setEmail(address);
        }
        emailCursor.close();

        return result;
    }

    private ActivityResultLauncher<Intent> SelectContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result ->
            {
                if (result.getResultCode() == RESULT_OK)
                {
                    var uri = result.getData().getData();
                    var contactCursor = getContentResolver().query(uri, null, null, null, null);
                    try
                    {
                        var contactIDColIdx = contactCursor.getColumnIndex(ContactsContract.Contacts._ID);
                        while (contactCursor.moveToNext())
                        {
                            var contactID = contactCursor.getLong(contactIDColIdx);

                            if (HandlePermission(contactID))
                            {
                                AddContactData(contactID);
                            }
                        }
                    } finally
                    {
                        contactCursor.close();
                    }
                }
            });

    private static final int REQUESTCODE_PICKCONTACT = 1337;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == REQUESTCODE_PICKCONTACT && resultCode == RESULT_OK)
        {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
