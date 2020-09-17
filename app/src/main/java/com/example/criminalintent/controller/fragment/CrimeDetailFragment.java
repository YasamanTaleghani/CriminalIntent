package com.example.criminalintent.controller.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.criminalintent.R;
import com.example.criminalintent.controller.activity.CrimeDetailActivity;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.repository.CrimeRepository;
import com.example.criminalintent.repository.IRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CrimeDetailFragment extends Fragment {

    public static final String TAG = "CDF";
    public static final String ARGS_CRIME_ID = "crimeId";
    public static final String BUNDLE_TITLE = "com.example.criminalintent.Bundle_title";
    public static final String BUNDLE_ISSOLVED = "com.example.criminalintent.Bundle_issolved";
    public static final String BUNDLE_DATE = "com.example.criminalintent.Bundle_Date";

    public static final int REQUEST_CODE_DATE_PICKER = 0;
    public static final int REQUEST_CODE_HOUR_PICKER = 1;
    public static final String FRAGMENT_TAG_DATE_PICKER = "DatePicker";
    public static final String FRAGMENT_TAG_HOUR_PICKER = "HourPicker";

    private EditText mEditTextTitle;
    private Button mButtonDate;
    private Button mButtonHour;
    private CheckBox mCheckBoxSolved;
    private Button mButtonNext;
    private Button mButtonLast;
    private Button mButtonPrevious;
    private Button mButtonFirst;

    private Crime mCrime;
    private Date mDate;
    private IRepository mRepository;

    public static CrimeDetailFragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_ID, crimeId);

        CrimeDetailFragment fragment = new CrimeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CrimeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        mRepository = CrimeRepository.getInstance();

        //this is storage of this fragment
        UUID crimeId = (UUID) getArguments().getSerializable(ARGS_CRIME_ID);
        mCrime = mRepository.getCrime(crimeId);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_detail, container, false);

        findViews(view);

        if (savedInstanceState!=null){
            mEditTextTitle.setText(savedInstanceState.getString(BUNDLE_TITLE));
            mCheckBoxSolved.setChecked(savedInstanceState.getBoolean(BUNDLE_ISSOLVED));
            mButtonDate.setText(savedInstanceState.getString(BUNDLE_DATE));
        }

        setListeners();
        initViews();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_detail_fragment_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_item_remove:

                CrimeRepository.getInstance().deleteCrime(mCrime);
                getActivity().onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCrime();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            Date userSelectedDate =
                    (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);

            updateCrimeDate(userSelectedDate);
        }

        if (requestCode == REQUEST_CODE_HOUR_PICKER) {
            Date userSelectedDate =
                    (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);

            updateCrimeDate(userSelectedDate);
        }
    }

    private void findViews(View view) {
        mEditTextTitle = view.findViewById(R.id.crime_title);
        mButtonDate = view.findViewById(R.id.crime_date);
        mButtonHour = view.findViewById(R.id.crime_hour);
        mCheckBoxSolved = view.findViewById(R.id.crime_solved);
        mButtonFirst = view.findViewById(R.id.btn_first);
        mButtonLast = view.findViewById(R.id.btn_last);
        mButtonNext = view.findViewById(R.id.btn_next);
        mButtonPrevious = view.findViewById(R.id.btn_previous);
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        mDate = mCrime.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minutesOfDay = calendar.get(Calendar.MINUTE);
        int secondsOfDay = calendar.get(Calendar.SECOND);
        mEditTextTitle.setText(mCrime.getTitle());
        mCheckBoxSolved.setChecked(mCrime.isSolved());
        mButtonDate.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
        mButtonHour.setText(hourOfDay + ":" + minutesOfDay + ":" + secondsOfDay);
    }

    private void setListeners() {
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s + ", " + start + ", " + before + ", " + count);

                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
            
        });

        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment =
                        DatePickerFragment.newInstance(mCrime.getDate());

                //create parent-child relations between CDF and DPF
                datePickerFragment.setTargetFragment(
                        CrimeDetailFragment.this,
                        REQUEST_CODE_DATE_PICKER);

                datePickerFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_DATE_PICKER);
            }
        });

        mButtonHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment =
                        TimePickerFragment.newInstance(mCrime.getDate());

                timePickerFragment.setTargetFragment(
                        CrimeDetailFragment.this,
                        REQUEST_CODE_HOUR_PICKER);

                timePickerFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_HOUR_PICKER);
            }
        });

        mCheckBoxSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        //Buttons
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(! checkNext()) {
                   next();
                }
                initViews();
            }
        });

        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPrevious()) {
                    previous();
                }
                initViews();
            }
        });

        mButtonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UUID btnId = mRepository.getCrimes().get(0).getId();
                mCrime =  mRepository.getCrime(btnId);
                initViews();
            }
        });

        mButtonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UUID btnId = mRepository.getCrimes().get(mRepository.getCrimes().size()-1).getId();
                mCrime =  mRepository.getCrime(btnId);
                initViews();
            }
        });
    }

    private void updateCrime() {
        mRepository.updateCrime(mCrime);
    }

    private void next(){
        for (int i = 0; i < mRepository.getCrimes().size() ; i++) {
            if (mCrime.equals(mRepository.getCrimes().get(i))){
                UUID btnId = mRepository.getCrimes().get(i+1).getId();
                mCrime = mRepository.getCrime(btnId);
                break;
            }
        }
    }

    private void previous(){
        for (int i = 0; i < mRepository.getCrimes().size() ; i++) {
            if (mCrime.equals(mRepository.getCrimes().get(i))){
                UUID btnId = mRepository.getCrimes().get(i-1).getId();
                mCrime = mRepository.getCrime(btnId);
                break;
            }
        }
    }

    private boolean checkNext(){
        if (mCrime.equals(mRepository.getCrimes().get(mRepository.getCrimes().size()-1))){
            UUID btnId = mRepository.getCrimes().get(0).getId();
            mCrime = mRepository.getCrime(btnId);
            return true;
        }

        return false;
    }

    private boolean checkPrevious(){
        if (mCrime.equals(mRepository.getCrimes().get(0))){
            UUID btnId = mRepository.getCrimes().get(mRepository.getCrimes().size()-1).getId();
            mCrime = mRepository.getCrime(btnId);
            return true;
        }

        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_TITLE,mCrime.getTitle());
        outState.putBoolean(BUNDLE_ISSOLVED,mCrime.isSolved());
        outState.putString(BUNDLE_DATE,mCrime.getDate().toString());

    }

    private void updateCrimeDate(Date userSelectedDate) {
        mCrime.setDate(userSelectedDate);
        updateCrime();

        mDate = mCrime.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);

        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        mButtonDate.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
    }

}