package com.example.criminalintent.model;

import com.example.criminalintent.utils.DateUtils;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mChecked;
    private String mSuspect;

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

    public Crime(UUID id, String title, Date date, boolean solved, String suspect) {
        mId = id;
        mTitle = title;
        mDate = date;
        mSolved = solved;
        mSuspect = suspect;
    }
}
