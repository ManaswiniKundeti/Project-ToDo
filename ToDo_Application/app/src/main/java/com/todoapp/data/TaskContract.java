package com.todoapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

//This class keeps track of all the constants that help us access the data in the db
public class TaskContract {

    //Is the code to access our content provider
    public static final String AUTHORITY = "com.todoapp";
    //uri = <scheme>://<authority>/<path>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    //Define possible paths for accessing data in this contract
    //This is path for the entire "tasks" directory
    public static final String PATH_TASKS = "tasks";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class TaskEntry implements BaseColumns {

        //content URI = base URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "tasks";

        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PRIORITY = "priority";


        /*
        The above table structure looks something like the sample table below.
        With the name of the table and columns on top, and potential contents in rows

        Note: Because this implements BaseColumns, the _id column is generated automatically

        tasks
         - - - - - - - - - - - - - - - - - - - - - -
        | _id  |    description     |    priority   |
         - - - - - - - - - - - - - - - - - - - - - -
        |  1   |  Do Android lesson |       1       |
         - - - - - - - - - - - - - - - - - - - - - -
        |  2   |    Go shopping     |       3       |
         - - - - - - - - - - - - - - - - - - - - - -
        .
        .
        .
         - - - - - - - - - - - - - - - - - - - - - -
        | 93   |     Drink Tea       |       2       |
         - - - - - - - - - - - - - - - - - - - - - -

         */

    }
}

