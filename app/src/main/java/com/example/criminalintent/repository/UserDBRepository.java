package com.example.criminalintent.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.criminalintent.database.CrimeDBHelper;
import com.example.criminalintent.database.UserDBSchema;
import com.example.criminalintent.model.User;
import static com.example.criminalintent.database.UserDBSchema.userTable.Cols;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDBRepository implements UsRepository{

    private static UserDBRepository sInstance;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static UserDBRepository getInstance(Context contex) {
        if (sInstance == null)
            sInstance = new UserDBRepository(contex);

        return sInstance;
    }

    public UserDBRepository(Context context) {
        mContext = context;
        CrimeDBHelper crimeDBHelper = new CrimeDBHelper(mContext);

        //all 4 checks happens on getDataBase
        mDatabase = crimeDBHelper.getWritableDatabase();
    }


    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        Cursor cursor = mDatabase.query(
                UserDBSchema.userTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return users;

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                User user = getUserFromCursor(cursor);

                users.add(user);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return users;
    }

    @Override
    public User getUser(UUID userId) {
        String where = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{userId.toString()};

        Cursor cursor = mDatabase.query(
                UserDBSchema.userTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return new User();

        try {
            cursor.moveToFirst();
            User user = getUserFromCursor(cursor);
            return user;

        } finally {
            cursor.close();
        }
    }

    @Override
    public void insertUser(User user) {
        ContentValues contentValues = getContentValues(user);
        mDatabase.insert(UserDBSchema.userTable.NAME,null,contentValues);
    }

    @Override
    public Boolean searchUser(User user) {
        List<User> users = getUsers();
        String username = user.getUserName();
        String password = user.getPassword();
        for (int i = 0; i < users.size() ; i++) {
            if (users.get(i).getUserName().equals(username) &&
                    users.get(i).getPassword().equals(password)){
                return true;
            }
        }

        return false;
    }

    private User getUserFromCursor(Cursor cursor) {
        UUID uuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(Cols.UUID)));
        String userName = cursor.getString(cursor.getColumnIndex(Cols.USERNAME));
        String passWord = cursor.getString(cursor.getColumnIndex(Cols.PASSWORD));
        return new User(uuid,userName,passWord);
    }

    private ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, user.getId().toString());
        values.put(Cols.USERNAME , user.getUserName());
        values.put(Cols.PASSWORD, user.getPassword());
        return values;
    }
}
