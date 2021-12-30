package com.example.ourmoney.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourmoney.Activities.CategoryAndWalletActivity;
import com.example.ourmoney.Activities.StoreDataActivity;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.SavingTarget;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.R;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    TextView jumduit,user;
    Button editName, kategori, wallet, expor, btnSimpan;
    EditText edtNewName;
    Dialog editNameDialog;
    ArrayList<Wallet> daftarwalletfrag;
    ArrayList<Category> daftarkategori;
    private SavingTarget currentTarget;

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(ArrayList<Wallet> w, ArrayList<Category> c, SavingTarget s) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, w);
        args.putParcelableArrayList(ARG_PARAM2, c);
        args.putParcelable(ARG_PARAM3, s);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            daftarwalletfrag = getArguments().getParcelableArrayList(ARG_PARAM1);
            daftarkategori = getArguments().getParcelableArrayList(ARG_PARAM2);
            currentTarget = getArguments().getParcelable(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
//        setting = v.findViewById(R.id.btnSetting);
        editName = v.findViewById(R.id.btnEditName);
        kategori = v.findViewById(R.id.btnKategori);
        wallet = v.findViewById(R.id.btnWallet);
        expor = v.findViewById(R.id.btnEkspor);
        jumduit = v.findViewById(R.id.tvasaldo);
        user = v.findViewById(R.id.tvnamauser);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("setting",getActivity().MODE_PRIVATE);
        user.setText(sharedPref.getString("name","no"));

        editNameDialog = new Dialog(getContext());
        editNameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editNameDialog.setContentView(R.layout.dialog_change_name);

        btnSimpan = editNameDialog.findViewById(R.id.btnSimpan);
        edtNewName = editNameDialog.findViewById(R.id.edtNewName);

        btnSimpan.setOnClickListener(view -> {
            String newName = edtNewName.getText().toString();
            if (newName.isEmpty()){
                Toast.makeText(getContext(), "Mohon masukkan nama baru", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("setting", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", newName);
            editor.apply();

            editNameDialog.hide();
            user.setText(newName);
        });

        kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CategoryAndWalletActivity.class);
                i.putExtra("isCategory",true);
                startActivityForResult(i,69);
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CategoryAndWalletActivity.class);
                i.putExtra("isCategory",false);
                startActivityForResult(i,69);
            }
        });

        expor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), StoreDataActivity.class);
                i.putExtra("savingtarget", (Parcelable) currentTarget);
                i.putExtra("daftarwallet", daftarwalletfrag);
                i.putExtra("daftarkategori", daftarkategori);
                startActivityForResult(i,69);
            }
        });

        editName.setOnClickListener(view -> {
            editNameDialog.show();
        });

        int totalBalance = 0;
        for (int i = 0; i < daftarwalletfrag.size(); i++) {
            totalBalance += daftarwalletfrag.get(i).getWalletAmount();
        }

        String displayBalance = String.format("%,8d%n", totalBalance);
        jumduit.setText("Saldo : Rp. " + displayBalance);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}