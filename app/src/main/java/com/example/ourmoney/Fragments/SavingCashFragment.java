package com.example.ourmoney.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ourmoney.Activities.AddTargetActivity;
import com.example.ourmoney.Activities.MainActivity;
import com.example.ourmoney.Models.SavingTarget;
import com.example.ourmoney.R;
import com.example.ourmoney.databinding.FragmentSavingCashBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavingCashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavingCashFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private SavingTarget target;
//    private String mParam2;

    public SavingCashFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SavingCashFragment newInstance(SavingTarget target) {
        SavingCashFragment fragment = new SavingCashFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, target);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            target = getArguments().getParcelable(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    FragmentSavingCashBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSavingCashBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        MainActivity parent = (MainActivity) getActivity();
//        parent.getData();
        ActivityResultLauncher<Intent> act = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == 110){
                    Intent results = result.getData();
                    target = results.getParcelableExtra("Target");
                }
            }
        });

        long today = new Date().getTime();
        long deadline = target.getTargetDeadline().getTime();
        if(today> deadline){
            target.setActive(false);
        }
        DateFormat a = new SimpleDateFormat("dd MMM yyyy HH:mm");
        if(target.isActive()){
            binding.tvduedateangka.setText(a.format(target.getTargetDeadline()));
            binding.tvtarget.setText(target.getTargetAmount().toString());
            binding.tvprogressangka.setText("Rp "+target.getAccumulated().toString());
        }else{
            binding.tvduedateangka.setText(a.format(target.getTargetDeadline()));
            binding.tvtarget.setText("No active target");
            binding.tvprogressangka.setText("No active target");
        }

        binding.btnAddSaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddTargetActivity.class);
                act.launch(i);
            }
        });

    }
}