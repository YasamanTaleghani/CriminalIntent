package com.example.criminalintent.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.criminalintent.utils.DateUtils;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "crimeTable")
public class Crime {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long primaryId;

    @ColumnInfo(name = "uuid")
    private UUID mId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "date")
    private Date mDate;

    @ColumnInfo(name = "solved")
    private boolean mSolved;

    @ColumnInfo(name = "suspect")
    private String mSuspect;

    @ColumnInfo(name = "phoneNumber")
    private String mPhoneNumber;

    @Ignore
    private boolean mChecked;

    public Long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(Long primaryId) {
        this.primaryId = primaryId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public boolean getChecked() {
        return mChecked;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Crime() {
        mId = UUID.randomUUID();
        mDate = DateUtils.randomDate();
    }

    public Crime(
            UUID id, String title, Date date, boolean solved, String suspect, String phoneNumber) {
        mId = id;
        mTitle = title;
        mDate = date;
        mSolved = solved;
        mSuspect = suspect;
        mPhoneNumber = phoneNumber;
    }
}
