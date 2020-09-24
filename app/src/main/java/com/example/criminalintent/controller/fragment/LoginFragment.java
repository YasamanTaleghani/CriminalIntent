package com.example.criminalintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.criminalintent.R;
import com.example.criminalintent.controller.activity.CrimeListActivity;
import com.example.criminalintent.controller.activity.SignUpActivity;
import com.example.criminalintent.model.User;
import com.example.criminalintent.repository.UserDBRepository;

import java.util.UUID;


public class LoginFragment extends Fragment {


    public static final String EXTRA_USERNAME_LIST = "ExtraUserName";
    private EditText mEditTextUserName;
    private EditText mEditTextPassWord;
    private Button mButtonLogin;
    private Button mButtonSignUp;
    private UserDBRepository mUserDBRepository;
    private String mSignedUpUserName;
    private String mSignedUpPassWord;

    public static final String ARGS_USER_ID = "ARGS_USER_ID";
    public static final int SIGNUP_REQUEST_CODE = 0;
    public static final String EXTRA_USER_NAME = "com.example.criminalintent.controller.username";
    public static final String EXTRA_PASSWORD = "com.example.criminalintent.controller.password";

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(UUID uuid) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_USER_ID,uuid);
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        findView(view);
        setListeners();

        return view;
    }

    private void findView(View view){
        mEditTextUserName = view.findViewById(R.id.editText_username);
        mEditTextPassWord = view.findViewById(R.id.editText_password);
        mButtonLogin = view.findViewById(R.id.btn_login);
        mButtonSignUp = view.findViewById(R.id.btn_tosignupLayout);
    }

    private void setListeners(){

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextUserName.getText().toString().length()==0 ||
                        mEditTextPassWord.getText().toString().length()==0 ){
                    Toast.makeText(
                            getActivity(),
                            "UserName or Password cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    User user = new User(mEditTextUserName.getText().toString().trim(),
                            mEditTextPassWord.getText().toString().trim());

                    if (mUserDBRepository.searchUser(user)){
                        Intent intent = new Intent(getActivity(), CrimeListActivity.class);
                        intent.putExtra(
                                EXTRA_USERNAME_LIST,mEditTextUserName.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "UserName or Password is not correct!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextUserName.getText().toString().trim().length()>0
                        && mEditTextPassWord.getText().toString().trim().length()>0){
                    String userName = mEditTextUserName.getText().toString();
                    String passWord = mEditTextPassWord.getText().toString();
                    Intent intent = new Intent(getActivity(),
                            SignUpActivity.class);
                    intent.putExtra(EXTRA_USER_NAME,userName);
                    intent.putExtra(EXTRA_PASSWORD,passWord);
                    startActivityForResult(intent,SIGNUP_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(getActivity(), SignUpActivity.class);
                    startActivityForResult(intent,SIGNUP_REQUEST_CODE);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != SignUpActivity.RESULT_OK || data == null)
            return;

        if (requestCode == SIGNUP_REQUEST_CODE) {
            mSignedUpUserName = data.getStringExtra(SignUpFragment.SINGED_UP_USERNAME);
            mSignedUpPassWord = data.getStringExtra(SignUpFragment.SIGNED_UP_PASSWORD);
            mEditTextUserName.setText(mSignedUpUserName);
            mEditTextPassWord.setText(mSignedUpPassWord);
        }
    }
}