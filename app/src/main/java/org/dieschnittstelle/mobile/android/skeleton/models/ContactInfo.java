package org.dieschnittstelle.mobile.android.skeleton.models;
import org.dieschnittstelle.mobile.android.skeleton.pages.TodoDetailviewActivity;
public class ContactInfo
{
    private long _ContactID;
    public long getContactID() { return _ContactID; }
    public void setContactID(long contactID) { _ContactID = contactID; }

    private TodoDetailviewActivity _Parent;
    public TodoDetailviewActivity getParent() { return _Parent; }
    public void setParent(TodoDetailviewActivity parent) { _Parent = parent; }

    private TodoItem _TodoItem;
    public TodoItem getTodoItem() { return _TodoItem; }
    public void setTodoItem(TodoItem todoItem) { _TodoItem = todoItem; }

    private String _Name;
    public String getName() { return _Name; }
    public void setName(String name) { _Name = name; }

    private String _MobilePhone;
    public String getMobilePhone() { return _MobilePhone; }
    public void setMobilePhone(String mobilePhone) { _MobilePhone = mobilePhone; }

    private String _Email;
    public String getEmail() { return _Email; }
    public void setEmail(String email) { _Email = email; }
}
