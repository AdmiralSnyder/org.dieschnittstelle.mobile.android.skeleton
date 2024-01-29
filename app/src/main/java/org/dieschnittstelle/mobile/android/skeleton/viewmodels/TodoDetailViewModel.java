package org.dieschnittstelle.mobile.android.skeleton.viewmodels;

import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;
public class TodoDetailViewModel extends ViewModelBase
{
    private TodoItem TodoItem;
    public TodoItem getTodoItem() { return TodoItem; }
    public void setTodoItem(TodoItem todoItem)
    {
        TodoItem = todoItem;
    }

    private String TodoItemID;
    public void setTodoItemID(String todoItemID) { TodoItemID = todoItemID; }
    public String getTodoItemID() { return TodoItemID; }
}
