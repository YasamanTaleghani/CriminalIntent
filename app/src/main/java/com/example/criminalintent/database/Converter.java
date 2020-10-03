package com.example.criminalintent.database;


import androidx.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

public class Converter {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public static String UUIDToString(UUID uuid){
        return uuid.toString();
    }

    @TypeConverter
    public static UUID StringToUUId(String string){
        return UUID.fromString(string);
    }
}
