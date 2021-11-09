package com.example.ourmoney.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourmoney.Activities.CategoryAndWalletActivity;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.R;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    TextView jumduit;
    Button setting, kategori, wallet, expor;
    ArrayList<Wallet> daftarwalletfrag;
    ArrayList<Category> daftarcatmasukfrag, daftarcatkeluarfrag;

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(ArrayList<Wallet> w, ArrayList<Category> kmasuk, ArrayList<Category> kkeluar) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, w);
        args.putParcelableArrayList(ARG_PARAM2, kmasuk);
        args.putParcelableArrayList(ARG_PARAM3, kkeluar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            daftarwalletfrag = getArguments().getParcelableArrayList(ARG_PARAM1);
            daftarcatmasukfrag = getArguments().getParcelableArrayList(ARG_PARAM2);
            daftarcatkeluarfrag = getArguments().getParcelableArrayList(ARG_PARAM3);
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
        setting = v.findViewById(R.id.btnSetting);
        kategori = v.findViewById(R.id.btnKategori);
        wallet = v.findViewById(R.id.btnWallet);
        expor = v.findViewById(R.id.btnEkspor);
        jumduit = v.findViewById(R.id.tvnamauser);

        kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CategoryAndWalletActivity.class);
                i.putExtra("isCategory",true);
                i.putParcelableArrayListExtra("masukan",daftarcatmasukfrag);
                i.putParcelableArrayListExtra("keluaran",daftarcatkeluarfrag);
                startActivityForResult(i,69);
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CategoryAndWalletActivity.class);
                i.putExtra("isCategory",false);
                i.putParcelableArrayListExtra("wallets",daftarwalletfrag);
                startActivityForResult(i,69);
            }
        });

        int totalBalance = 0;
        for (int i = 0; i < daftarwalletfrag.size(); i++) {
            totalBalance += daftarwalletfrag.get(i).getWalletAmount();
        }

        String displayBalance = String.format("%,8d%n", totalBalance);
        jumduit.setText("Saldo Anda: Rp " + displayBalance);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==69){
            daftarcatmasukfrag.clear();
            daftarcatmasukfrag.addAll(data.getParcelableArrayListExtra("3"));

            daftarcatkeluarfrag.clear();
            daftarcatkeluarfrag.addAll(data.getParcelableArrayListExtra("2")) ;
        }else if(resultCode==70){
            daftarwalletfrag.clear();
            daftarwalletfrag.addAll(data.getParcelableArrayListExtra("1")) ;
        }
    }
}