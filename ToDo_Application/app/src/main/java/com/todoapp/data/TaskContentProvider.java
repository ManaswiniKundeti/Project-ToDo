package com.todoapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class TaskContentProvider extends ContentProvider {

    //It's a convention to use 100,200,300 etc. for directories
    //and their related int's 101,102.. for items in that directory
    public static final int TASKS = 100;
    public static final int TASK_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //match uri for directory
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS, TASKS);
        //match for single item in directory
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS + "/#", TASK_WITH_ID);
        return uriMatcher;
    }

    private TaskDbHelper mTaskDbHelper;

    /* onCreate() is where you should initialize anything you’ll need to setup
    your underlying data source.
    In this case, you’re working with a SQLite database, so you’ll need to
    initialize a DbHelper to gain access to it.
     */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mTaskDbHelper = new TaskDbHelper(context);
        return true;
    }

    //insert handles requests to insert a single new row of data
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri;
        switch(match){
            case TASKS:
                //inserting values into tasks table
                long id = db.insert(TaskContract.TaskEntry.TABLE_NAME,null, values);
                if(id >= 0){
                    //success
                    //creating uri with 1st arg as a base and id added on to the end of the path
                    returnUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " +uri);
                }
                break;
             default:
                throw new UnsupportedOperationException("Unknown Uri "+ uri);
        }
        //notify resolver that change has occurred, so that it can accordingly update db and associated UI
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mTaskDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch(match) {
            case TASKS:
                returnCursor = db.query(TaskContract.TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case TASK_WITH_ID:
                //using selection & selectionArgs
                //URI : content://<authority>/tasks/#
                String id = uri.getPathSegments().get(1); //get(0) is the tasks  and get(1) is the # id
                //selection is _ID column = ?, selectionArgs = row ID from the URI
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};

                returnCursor = db.query(TaskContract.TaskEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" +uri);
        }
        //set notification uri on cursor - if anything changes on the uri, the cursor will know
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        String id = uri.getPathSegments().get(1); //get(0) is the tasks  and get(1) is the # id
        //selection is _ID column = ?, selectionArgs = row ID from the URI
        String mSelection = "_id=?";
        String[] mSelectionArgs = new String[]{id};

        int itemsDeleted;

        itemsDeleted =  db.delete(TaskContract.TaskEntry.TABLE_NAME,
                                    mSelection,
                                    mSelectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return itemsDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksUpdated;

        switch (match){
            case TASK_WITH_ID:
                //update single task by getting the id
                String id = uri.getPathSegments().get(1);
                //selection is _ID column = ?, selectionArgs = row ID from the URI
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                tasksUpdated = db.update(TaskContract.TaskEntry.TABLE_NAME,
                                        values,
                                        mSelection,
                                        mSelectionArgs);
                break;
             default:
                 throw new UnsupportedOperationException("Unknown uri "+ uri);
        }
        if(tasksUpdated != 0){
            //set notifications if a task was updated
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksUpdated;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match){
            case TASKS:
                //directory
                return "vnd.android.cursor.dir" + "/" + TaskContract.AUTHORITY + "/" + TaskContract.PATH_TASKS;
            case TASK_WITH_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" + TaskContract.AUTHORITY + "/" + TaskContract.PATH_TASKS;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

}

