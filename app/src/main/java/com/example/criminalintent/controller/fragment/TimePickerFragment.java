package com.example.criminalintent.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.criminalintent.R;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class TimePickerFragment extends DialogFragment {

    public static final String ARGS_CRIME_TIME = "com.example.criminalintent.args_crime_time";
    public static final String EXTRA_USER_SELECTED_HOUR = "com.example.criminalintent.userSelectedHour";
    private Date mCrimeHour;
    private TimePicker mHourPicker;
    private DatePicker mDatePicker;

    public TimePickerFragment() {
        // Required empty public constructor
    }

    public static TimePickerFragment newInstance(Date crimeDate) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_TIME, crimeDate);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCrimeHour = (Date) getArguments().getSerializable(ARGS_CRIME_TIME);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_time_picker, null);

        findViews(view);
        initViews();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.time_picker_title)
                .setIcon(R.mipmap.ic_launcher)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date userSelectedTime = extractTimeFromTimePicker();
                        sendResult(userSelectedTime);

                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void findViews(View view) {
        mHourPicker = view.findViewById(R.id.time_picker_crime);

    }

    private void initViews() {
        initDatePicker();
    }

    private void initDatePicker() {
        // i have a date and i want to set it in date picker.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCrimeHour);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        /*mHourPicker.setHour(hour);
        mHourPicker.setMinute(minute);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Date extractTimeFromTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = mHourPicker.getHour();
        int miniute = mHourPicker.getMinute();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(mCrimeHour);
        int year = calendar1.get(Calendar.YEAR);
        int monthOfYear = calendar1.get(Calendar.MONTH);
        int dayOfMonth = calendar1.get(Calendar.DAY_OF_MONTH);
        calendar1.getTime();
        Date date = new Date();
        
        return date;
    }

    private void sendResult(Date userSelectedDate) {
        Fragment fragment = getTargetFragment();

        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER_SELECTED_HOUR, userSelectedDate);

        fragment.onActivityResult(requestCode, resultCode, intent);
    }
}