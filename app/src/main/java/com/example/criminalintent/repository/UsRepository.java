package com.example.criminalintent.repository;

import com.example.criminalintent.model.User;

import java.util.List;
import java.util.UUID;

public interface UsRepository<T> {
    List<T> getEntities();
    T get(UUID userId);
    void insert(T input);
}
