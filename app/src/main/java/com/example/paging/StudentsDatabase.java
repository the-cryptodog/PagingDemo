package com.example.paging;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database( entities = {Student.class},version = 1,exportSchema = false)
public abstract class StudentsDatabase extends RoomDatabase {
    static StudentsDatabase instance;
    static synchronized StudentsDatabase getInstance(Context context){
        if(instance == null){
            instance= Room.databaseBuilder(context, StudentsDatabase.class, "students_database").build();
        }
        return instance;
    }
    abstract StudentDao getStudentDao();

}
