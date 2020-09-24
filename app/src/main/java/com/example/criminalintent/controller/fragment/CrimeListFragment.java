package com.example.criminalintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.criminalintent.R;
import com.example.criminalintent.controller.activity.CrimeDetailActivity;
import com.example.criminalintent.controller.activity.CrimePagerActivity;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.repository.CrimeRepository;
import com.example.criminalintent.repository.IRepository;

import java.util.List;

public class CrimeListFragment extends Fragment {

    public static final String TAG = "CLF";

    private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private IRepository mRepository;
    private ImageView mImageViewEmpty;
    private TextView mTextViewEmpty;
    private Button mButtonAddNewCrime;

    public static CrimeListFragment newInstance() {

        Bundle args = new Bundle();

        CrimeListFragment fragment = new CrimeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CrimeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRepository = CrimeRepository.getInstance();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        findViews(view);
        initViews();
        setListeners();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_list_fragment_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.menu_btn_delete_selected:

                int i=0;
                while (i<mRepository.getCrimes().size()){
                    if (mRepository.getCrimes().get(i).getChecked()){
                        mRepository.deleteCrime(mRepository.getCrimes().get(i));
                    } else {
                        i++;
                    }
                }

                Toast.makeText(getActivity(),
                        "size is:" + mRepository.getCrimes().size(), Toast.LENGTH_SHORT).show();
                updateUI();

                return true;

            case R.id.menu_btn_select_all:
                for (int j = 0; j < mRepository.getCrimes().size() ; j++) {
                    mRepository.getCrimes().get(j).setChecked(true);
                }
                updateUI();
                return true;

            case R.id.menu_btn_unselect_all:
                for (int j = 0; j < mRepository.getCrimes().size() ; j++) {
                    mRepository.getCrimes().get(j).setChecked(false);
                }
                updateUI();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_crime_list);
        mImageViewEmpty = view.findViewById(R.id.imageView_empty);
        mTextViewEmpty = view.findViewById(R.id.textView_empty);
        mButtonAddNewCrime = view.findViewById(R.id.btn_addCrime);
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    private void setListeners(){
        mButtonAddNewCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Crime crime = new Crime();
                mRepository.insertCrime(crime);
                Intent intent = CrimeDetailActivity.newIntent(getContext(),crime.getId());
                startActivity(intent);
            }
        });
    }

    private void updateUI() {
        List<Crime> crimes = mRepository.getCrimes();

        if (crimes.size()==0){
            mImageViewEmpty.setVisibility(View.VISIBLE);
            mTextViewEmpty.setVisibility(View.VISIBLE);
            mButtonAddNewCrime.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mImageViewEmpty.setVisibility(View.GONE);
            mTextViewEmpty.setVisibility(View.GONE);
            mButtonAddNewCrime.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            if (mCrimeAdapter == null) {
                mCrimeAdapter = new CrimeAdapter(crimes);
                mRecyclerView.setAdapter(mCrimeAdapter);
            } else {
                mCrimeAdapter.setCrimes(crimes);
                mCrimeAdapter.notifyDataSetChanged();
            }
        }

    }

    private class CrimeHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private ImageView mImageViewSolved;
        private CheckBox mCheckBox;
        private Crime mCrime;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewTitle = itemView.findViewById(R.id.row_item_crime_title);
            mTextViewDate = itemView.findViewById(R.id.row_item_crime_date);
            mImageViewSolved = itemView.findViewById(R.id.imgview_solved);
            mCheckBox = itemView.findViewById(R.id.checkbox_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
                    startActivity(intent);
                }
            });

            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()){
                        mCrime.setChecked(true);
                    } else {
                        mCrime.setChecked(false);
                    }

                }
            });

        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTextViewTitle.setText(crime.getTitle());
            mTextViewDate.setText(crime.getDate().toString());
            mImageViewSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            mCheckBox.setChecked(mCrime.getChecked());
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public List<Crime> getCrimes() {
            return mCrimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder");

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.crime_row_list, parent, false);
            CrimeHolder crimeHolder = new CrimeHolder(view);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: " + position);

            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }
    }


}