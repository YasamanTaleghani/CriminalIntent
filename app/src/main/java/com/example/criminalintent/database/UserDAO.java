package com.example.criminalintent.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.criminalintent.model.User;
import com.example.criminalintent.repository.UsRepository;

import java.util.List;
import java.util.UUID;

@Dao
public interface UserDAO extends UsRepository<User> {

    @Insert
    void insert(User input);

    @Query("SELECT * FROM userTable")
    List<User> getEntities();

    @Query("SELECT * FROM userTable WHERE uuid=:userId")
    User get(UUID userId);

}
