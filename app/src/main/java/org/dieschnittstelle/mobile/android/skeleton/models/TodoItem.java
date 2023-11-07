package org.dieschnittstelle.mobile.android.skeleton.models;

import java.util.Date;

public class TodoItem
{
    public TodoItem() { }

    private String ID;
    public String getID() { return ID; }
    public void setID(String id) { this.ID = id; }

    private String Name;
    public String getName() { return Name; }
    public void setName(String name) { Name = name; }

    private String Description;
    public String getDescription() { return Description; }
    public void setDescription(String description) { Description = description; }

    private boolean IsDone;
    public boolean getIsDone() { return IsDone; }
    public void setIsDone(boolean isDone) { IsDone = isDone; }

    private boolean IsFavourite;
    public boolean getIsFavourite() { return IsFavourite; }
    public void setIsFavourite(boolean isFavourite) { IsFavourite = isFavourite; }

    private Date DueDate;
    public Date getDueDate() { return DueDate; }
    public void setDueDate(Date dueDate) { DueDate = dueDate; }
}
