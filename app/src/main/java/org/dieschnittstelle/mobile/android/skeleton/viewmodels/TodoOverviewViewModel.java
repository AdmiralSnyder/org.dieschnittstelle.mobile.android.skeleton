package org.dieschnittstelle.mobile.android.skeleton.viewmodels;

import org.dieschnittstelle.mobile.android.skeleton.models.TodoItem;

import java.util.ArrayList;
import java.util.List;

public class TodoOverviewViewModel extends ViewModelBase
{
    private List<TodoItem> TodoItems = new ArrayList<>();

    public List<TodoItem> getTodoItems() {return TodoItems;}
}
