package org.dieschnittstelle.mobile.android.skeleton.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TodoItem
{
    public TodoItem() { }

    private String ID;
    @com.google.firebase.database.PropertyName("ID")
    public String getID() { return ID; }
    @com.google.firebase.database.PropertyName("ID")
    public void setID(String id) { this.ID = id; }

    private String Name;
    @com.google.firebase.database.PropertyName("Name")
    public String getName() { return Name; }
    @com.google.firebase.database.PropertyName("Name")
    public void setName(String name) { Name = name; }

    private String Description;
    @com.google.firebase.database.PropertyName("Description")
    public String getDescription() { return Description; }
    @com.google.firebase.database.PropertyName("Description")
    public void setDescription(String description) { Description = description; }

    private boolean IsDone;
    @com.google.firebase.database.PropertyName("IsDone")

    public boolean getIsDone() { return IsDone; }
    @com.google.firebase.database.PropertyName("IsDone")
    public void setIsDone(boolean isDone) { IsDone = isDone; }

    private boolean IsFavourite;
    @com.google.firebase.database.PropertyName("IsFavourite")
    public boolean getIsFavourite() { return IsFavourite; }
    @com.google.firebase.database.PropertyName("IsFavourite")
    public void setIsFavourite(boolean isFavourite) { IsFavourite = isFavourite; }
    @com.google.firebase.database.Exclude
    private Date DueDate;
    @com.google.firebase.database.PropertyName("DueDate")
    public String getDueDateStr()
    {
        var year = getDueDate().getYear() + 1900;
        return String.format("%1d-%2$tm-%2$tdT%2$tH:%2$tM:%2$tS", year, getDueDate());
    }

    private List<Long> ContactIDs = new ArrayList<>();
    @com.google.firebase.database.Exclude
    public List<Long> getContactIDs() { return ContactIDs; }

    @com.google.firebase.database.PropertyName("Contacts")
    public String getContactsStr()
    {
        return String.join(";", getContactIDs().stream().map(p -> p.toString()).collect(Collectors.toList()));
    }

    @com.google.firebase.database.PropertyName("DueDate")
    public void setDueDateStr(String dueDateStr)
    {
        var year = Integer.parseInt(dueDateStr, 0, 4, 10);
        var month = Integer.parseInt(dueDateStr, 5, 7, 10);
        var day = Integer.parseInt(dueDateStr, 8, 10, 10);
        var hours = Integer.parseInt(dueDateStr, 11, 13, 10);
        var minutes = Integer.parseInt(dueDateStr, 14, 16, 10);
        var seconds = Integer.parseInt(dueDateStr, 17, 19, 10);
        setDueDate(new Date(year - 1900, month, day, hours, minutes, seconds));
    }

    @com.google.firebase.database.Exclude
    public Date getDueDate() { return DueDate; }
    @com.google.firebase.database.Exclude
    public void setDueDate(Date dueDate) { DueDate = dueDate; }
}
