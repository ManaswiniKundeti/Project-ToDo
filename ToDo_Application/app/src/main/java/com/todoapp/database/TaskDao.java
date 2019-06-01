package com.todoapp.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

//Create a DAO(Data Access Object) for each entity in the DB
@Dao
public interface TaskDao {

    @Query("SELECT * FROM task ORDER BY priority")
    LiveData<List<TaskEntry>> loadAllTasks();
    //wrapping the return type in livedata notifies when there is a change in the data

    @Insert
    void insertTask(TaskEntry taskEntry);

    @Update
    void updateTask(TaskEntry taskEntry);

    @Delete
    void deleteTask(TaskEntry taskEntry);

    //as the below func is said to retrieve data from db, we add @query along with appropriate query
    @Query("SELECT * FROM task WHERE id = :id")
    LiveData<TaskEntry> loadTaskById(int id);
}


// the ability to return an object as a return type to an operation*here, query)
// is what makes room an object relational mapping library