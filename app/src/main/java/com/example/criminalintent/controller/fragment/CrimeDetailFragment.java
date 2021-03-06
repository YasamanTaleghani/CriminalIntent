package com.example.criminalintent.controller.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;

import com.example.criminalintent.R;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.repository.CrimeDBRepository;

import java.text.SimpleDateFormat;
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
    private static final int REQUEST_CODE_SELECT_CONTACT = 2;
    private static final int REQUEST_SELECT_PHONE_NUMBER = 3;
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

    private Button mButtonSuspect;
    private Button mButtonReport;
    private Button mButtonDial;
    private Button mButtonCall;

    private Crime mCrime;
    private Date mDate;
    private CrimeDBRepository mRepository;

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

        mRepository = CrimeDBRepository.getInstance(getActivity());

        //this is storage of this fragment
        UUID crimeId = (UUID) getArguments().getSerializable(ARGS_CRIME_ID);
        mCrime = mRepository.get(crimeId);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_detail, container, false);

        findViews(view);

        if (savedInstanceState != null) {
            mEditTextTitle.setText(savedInstanceState.getString(BUNDLE_TITLE));
            mCheckBoxSolved.setChecked(savedInstanceState.getBoolean(BUNDLE_ISSOLVED));
            mButtonDate.setText(savedInstanceState.getString(BUNDLE_DATE));
        }

        initViews();
        setListeners();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_detail_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_remove:

                CrimeDBRepository.getInstance(getActivity()).deleteCrime(mCrime);
                getActivity().onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        updateCrime();
        Log.d(TAG, "onResume");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            Date userSelectedDate =
                    (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);

            updateCrimeDate(userSelectedDate);
        } else if (requestCode == REQUEST_CODE_HOUR_PICKER) {
            Date userSelectedDate =
                    (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);

            updateCrimeDate(userSelectedDate);
        } else if (requestCode == REQUEST_CODE_SELECT_CONTACT) {

            Uri contactUri = data.getData();

            String name = retrieveContactName(contactUri);
            String phoneNumber = retreiveContactNumber(contactUri);

                mButtonSuspect.setText(name);
                mCrime.setSuspect(name);
                mCrime.setPhoneNumber(phoneNumber);
                mButtonCall.setEnabled(true);
                mButtonCall.setText("Call " + phoneNumber );
                mButtonDial.setEnabled(true);
                mButtonDial.setText("Dial " + phoneNumber);

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
        mButtonSuspect = view.findViewById(R.id.choose_suspect);
        mButtonReport = view.findViewById(R.id.send_report);
        mButtonDial = view.findViewById(R.id.btn_dial);
        mButtonCall = view.findViewById(R.id.btn_call);
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        mDate = mCrime.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateStr = simpleDateFormat.format(mDate);

        SimpleDateFormat simpleDateFormatHour = new SimpleDateFormat("hh:mm:ss");
        String hourStr = simpleDateFormatHour.format(mDate);

        mEditTextTitle.setText(mCrime.getTitle());
        mCheckBoxSolved.setChecked(mCrime.isSolved());
        mButtonDate.setText(dateStr);
        mButtonHour.setText(hourStr);

        if (mCrime.getSuspect() == null) {
            mButtonDial.setEnabled(false);
            mButtonCall.setEnabled(false);
        } else {
            mButtonSuspect.setText(mCrime.getSuspect());
            mButtonDial.setText("Dial " + mCrime.getPhoneNumber());
            mButtonCall.setText("Call "+ mCrime.getPhoneNumber());
        }

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
                if (!checkNext()) {
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
                UUID btnId = mRepository.getEntities().get(0).getId();
                mCrime = mRepository.get(btnId);
                initViews();
            }
        });

        mButtonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UUID btnId = mRepository.getEntities().get(
                        mRepository.getEntities().size() - 1).getId();
                mCrime = mRepository.get(btnId);
                initViews();
            }
        });

        mButtonSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectContact();

            }
        });

        mButtonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shareReportIntent();
                shareText(getReport());
            }
        });

        mButtonDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(
                        Intent.ACTION_DIAL, Uri.parse("tel:" + mCrime.getPhoneNumber()));
                startActivity(intent);
            }
        });

        mButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        PackageManager.PERMISSION_GRANTED);

                Intent intent = new Intent(
                        Intent.ACTION_CALL, Uri.parse("tel:" + mCrime.getPhoneNumber()));
                startActivity(intent);
            }
        });
    }

    private void updateCrime() {
        mRepository.updateCrime(mCrime);
    }

    private void next() {
        for (int i = 0; i < mRepository.getEntities().size(); i++) {
            if (mCrime.equals(mRepository.getEntities().get(i))) {
                UUID btnId = mRepository.getEntities().get(i + 1).getId();
                mCrime = mRepository.get(btnId);
                break;
            }
        }
    }

    private void previous() {
        for (int i = 0; i < mRepository.getEntities().size(); i++) {
            if (mCrime.equals(mRepository.getEntities().get(i))) {
                UUID btnId = mRepository.getEntities().get(i - 1).getId();
                mCrime = mRepository.get(btnId);
                break;
            }
        }
    }

    private boolean checkNext() {
        if (mCrime.equals(mRepository.getEntities().get(mRepository.getEntities().size() - 1))) {
            UUID btnId = mRepository.getEntities().get(0).getId();
            mCrime = mRepository.get(btnId);
            return true;
        }

        return false;
    }

    private boolean checkPrevious() {
        if (mCrime.equals(mRepository.getEntities().get(0))) {
            UUID btnId = mRepository.getEntities().get(mRepository.getEntities().size() - 1).getId();
            mCrime = mRepository.get(btnId);
            return true;
        }

        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_TITLE, mCrime.getTitle());
        outState.putBoolean(BUNDLE_ISSOLVED, mCrime.isSolved());
        outState.putString(BUNDLE_DATE, mCrime.getDate().toString());

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

    private String getReport() {
        String title = mCrime.getTitle();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:SS");
        String dateString = simpleDateFormat.format(mCrime.getDate());

        String solvedString = mCrime.isSolved() ?
                getString(R.string.crime_report_solved) :
                getString(R.string.crime_report_unsolved);

        String suspectString = mCrime.getSuspect() == null ?
                getString(R.string.crime_report_no_suspect) :
                getString(R.string.crime_report_suspect, mCrime.getSuspect());

        String report = getString(
                R.string.crime_report,
                title,
                dateString,
                solvedString,
                suspectString);

        return report;
    }

    private void shareReportIntent() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getReport());
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
        sendIntent.setType("text/plain");

        Intent shareIntent =
                Intent.createChooser(sendIntent, getString(R.string.send_report));

        //we prevent app from crash if the intent has no destination.
        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(shareIntent);
    }

    private void selectContact() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.READ_CONTACTS},
                PackageManager.PERMISSION_GRANTED);

        Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
        //intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_CONTACT);
        }
    }

    private void shareText(String string) {
        String mimeType = "text/plain";

        Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                .setType(mimeType)
                .setText(string)
                .getIntent();
        if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(shareIntent);
        }

    }

    public void selectContactPhone() {
        // Start an activity for the user to pick a phone number from contacts
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String retrieveContactName(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);
        try {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        } finally {
            cursor.close();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String  retreiveContactNumber(Uri uri){

        String contactID;
        String phoneNumber;
        Cursor cursorId = getActivity().getContentResolver().query(
                uri,
                new String[]{ContactsContract.Contacts._ID},
                null,
                null,
                null,
                null);

        try {
            cursorId.moveToFirst();
            contactID = cursorId.getString(cursorId.getColumnIndex(ContactsContract.Contacts._ID));
        } finally {
            cursorId.close();
        }

        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};

        String selection =
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

        Cursor cursorPhone = getActivity().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                selection,
                new String[]{contactID},
                null
        );

        try {
            cursorPhone.moveToFirst();
            phoneNumber = cursorPhone.getString(
                    cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        } finally {
            cursorPhone.close();
        }

        return phoneNumber;
    }
}