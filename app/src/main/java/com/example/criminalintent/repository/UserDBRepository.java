package com.example.criminalintent.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.criminalintent.database.CrimeDatabase;
import com.example.criminalintent.database.UserDAO;
import com.example.criminalintent.model.User;

import java.util.List;
import java.util.UUID;

public class UserDBRepository implements UserDAO {

    private static UserDBRepository sInstance;
    private UserDAO mUserDAO;
    private Context mContext;

    public static UserDBRepository getInstance(Context contex) {
        if (sInstance == null)
            sInstance = new UserDBRepository(contex);

        return sInstance;
    }

    public UserDBRepository(Context context) {
        mContext = context.getApplicationContext();

        CrimeDatabase crimeDatabase = Room.databaseBuilder(
                mContext, CrimeDatabase.class, "crime.db")
                .allowMainThreadQueries()
                .build();

        mUserDAO = crimeDatabase.mUserDAO();
    }


    @Override
    public List<User> getEntities() {
        return mUserDAO.getEntities();
    }

    @Override
    public User get(UUID userId) {
        return mUserDAO.get(userId);
    }

    @Override
    public void insert(User user) {
        mUserDAO.insert(user);
    }

    public Boolean search(User user) {
        List<User> users = getEntities();
        for (int i = 0; i <users.size() ; i++) {
            if (users.get(i).getUserName().equals(user.getUserName())
            && users.get(i).getPassword().equals(user.getPassword()))
                return true;
        }

        return false;
    }
}
