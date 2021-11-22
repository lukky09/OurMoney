package com.example.ourmoney.Fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourmoney.Activities.AddTransactionActivity;
import com.example.ourmoney.R;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.databinding.FragmentHomeTransactionBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    ArrayList<Wallet> daftarwallet;

    FragmentHomeTransactionBinding binding;

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(ArrayList<Wallet> w) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, w);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            daftarwallet = getArguments().getParcelableArrayList(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home_transaction, container, false);
        binding = FragmentHomeTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int totalBalance = 0;
        for (int i = 0; i < daftarwallet.size(); i++) {
            totalBalance+=daftarwallet.get(i).getWalletAmount();
        }
        String displayBalance = String.format("%,8d%n",totalBalance);
        binding.lblBalance.setText("Rp "+displayBalance);

        System.out.println("Current amount of transaction is "+daftarwallet.size());

        binding.fabAddTransaction.setOnClickListener(this::onClick);

    }

    ActivityResultLauncher<Intent> addTransRL = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 100){
                        Intent data = result.getData();
                        Toast.makeText(getActivity(), data.getStringExtra("msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void onClick(View view){
        int viewID = view.getId();
        if (viewID == R.id.fabAddTransaction){
            Intent moveData = new Intent(getActivity(), AddTransactionActivity.class);
            addTransRL.launch(moveData);
        }
    }
}