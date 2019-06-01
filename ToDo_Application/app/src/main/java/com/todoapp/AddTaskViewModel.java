package com.todoapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.todoapp.database.AppDatabase;
import com.todoapp.database.TaskEntry;

public class AddTaskViewModel extends ViewModel {

    private LiveData<TaskEntry> task;

    public AddTaskViewModel(AppDatabase db, int taskId) {
        task = db.taskDao().loadTaskById(taskId);
    }

    public LiveData<TaskEntry> getTask(){
        return task;
    }
}
