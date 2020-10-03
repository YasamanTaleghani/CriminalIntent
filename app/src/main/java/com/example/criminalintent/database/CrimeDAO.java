package com.example.criminalintent.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.criminalintent.model.Crime;
import com.example.criminalintent.repository.UsRepository;

import java.util.List;
import java.util.UUID;

@Dao
public interface CrimeDAO extends UsRepository<Crime> {

    @Insert
    void insert(Crime input);

    @Query("SELECT * FROM crimeTable")
    List<Crime> getEntities();

    @Query("SELECT * FROM crimeTable WHERE uuid=:userId")
    Crime get(UUID userId);

    @Update
    void updateCrime(Crime crime);

    @Delete
    void deleteCrime(Crime crime);

}
