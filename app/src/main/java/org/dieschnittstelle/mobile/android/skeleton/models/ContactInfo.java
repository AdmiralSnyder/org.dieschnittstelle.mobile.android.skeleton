package org.dieschnittstelle.mobile.android.skeleton.models;
import org.dieschnittstelle.mobile.android.skeleton.pages.TodoDetailviewActivity;
public class ContactInfo
{
    private long ContactID;
    public long getContactID() { return ContactID; }
    public void setContactID(long contactID) { ContactID = contactID; }

    private TodoDetailviewActivity Parent;
    public TodoDetailviewActivity getParent() { return Parent; }
    public void setParent(TodoDetailviewActivity parent) { Parent = parent; }

    private TodoItem TodoItem;
    public TodoItem getTodoItem() { return TodoItem; }
    public void setTodoItem(TodoItem todoItem) { TodoItem = todoItem; }

    private String Name;
    public String getName() { return Name; }
    public void setName(String name) { Name = name; }

    private String MobilePhone;
    public String getMobilePhone() { return MobilePhone; }
    public void setMobilePhone(String mobilePhone) { MobilePhone = mobilePhone; }

    private String Email;
    public String getEmail() { return Email; }
    public void setEmail(String email) { Email = email; }
}
