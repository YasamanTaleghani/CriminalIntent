package com.example.criminalintent.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.criminalintent.model.Crime;
import com.example.criminalintent.model.User;

@Database(entities = {Crime.class, User.class} , version = 1)
@TypeConverters({Converter.class})
public abstract class CrimeDatabase extends RoomDatabase {

    public abstract CrimeDAO mCrimeDAO();
    public abstract UserDAO mUserDAO();
}
