package com.example.criminalintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.criminalintent.R;
import com.example.criminalintent.controller.activity.LoginActivity;
import com.example.criminalintent.model.User;
import com.example.criminalintent.repository.UserDBRepository;

import static android.app.Activity.RESULT_OK;


public class SignUpFragment extends Fragment {

    private EditText mEditTextUserName;
    private EditText mEditTextPassword;
    private Button mButtonSignup;
    private UserDBRepository mUserDBRepository;

    public static final String SINGED_UP_USERNAME = "Singed_UP_Username";
    public static final String SIGNED_UP_PASSWORD = "signed_up_password";

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserDBRepository = UserDBRepository.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        findViews(view);
        setListeners();

        Intent intent = getActivity().getIntent();
        String name = intent.getStringExtra(LoginFragment.EXTRA_USER_NAME);
        String user = intent.getStringExtra(LoginFragment.EXTRA_PASSWORD);
        if (name.length()!=0 && user.length()!=0) {
            mEditTextUserName.setText(name);
            mEditTextPassword.setText(user);
        }

        return view;
    }

    private void findViews(View view){
        mEditTextUserName = view.findViewById(R.id.editText_username_signup);
        mEditTextPassword = view.findViewById(R.id.editText_password_signup);
        mButtonSignup = view.findViewById(R.id.btn_signup);
    }

    private void setListeners(){
        mButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextUserName.getText().toString().trim().length()>0 &&
                        mEditTextPassword.getText().toString().trim().length()>0) {

                    User user = new User(mEditTextUserName.getText().toString().trim(),
                            mEditTextPassword.getText().toString().trim());

                    mUserDBRepository.insertUser(user);

                    setShownAnswerResult();

                }
            }
        });
    }

    private void setShownAnswerResult(){
        Intent intent = new Intent();
        intent.putExtra(SINGED_UP_USERNAME,mEditTextUserName.getText().toString());
        intent.putExtra(SIGNED_UP_PASSWORD,mEditTextPassword.getText().toString());
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

}