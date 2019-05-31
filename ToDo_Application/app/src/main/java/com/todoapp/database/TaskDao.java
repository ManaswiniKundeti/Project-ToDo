package com.todoapp.database;


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
    List<TaskEntry> loadAllTasks();

    @Insert
    void insertTask(TaskEntry taskEntry);

    @Update
    void updateTask(TaskEntry taskEntry);

    @Delete
    void deleteTask(TaskEntry taskEntry);

}


// the ability to return an object as a return type to an operation*here, query)
// is what makes room an object relational mapping library