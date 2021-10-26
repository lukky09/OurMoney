package com.example.ourmoney;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTransactionFragment extends Fragment {

    private static String parameterarg1 = "inibolehsembarangtah";
    private static final String parameterarg2 = "gatau";
    private static final String parameterarg3 = "mungkin";

    ArrayList<Wallet> daftarwallet;
    ArrayList<Category> daftarkategorikeluar, daftarkategorimasuk;
    Button baddtransaction;
    Spinner spinnertag, spinnerwallet;
    EditText etduit;
    TextView tvdata,tvwalletamount;
    RadioButton rbkeluar, rbmasuk;
    ArrayAdapter adaptercategorykeluar,adaptercategorymasuk,adapterwallet;


    public AddTransactionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddTransactionFragment newInstance(ArrayList<Wallet> w , ArrayList<Category> keluar, ArrayList<Category> masuk) {
        AddTransactionFragment fragment = new AddTransactionFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(parameterarg1,w);
        args.putParcelableArrayList(parameterarg2,keluar);
        args.putParcelableArrayList(parameterarg3,masuk);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            daftarwallet = getArguments().getParcelableArrayList(parameterarg1);
            daftarkategorikeluar = getArguments().getParcelableArrayList(parameterarg2);
            daftarkategorimasuk = getArguments().getParcelableArrayList(parameterarg3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etduit = view.findViewById(R.id.tvtransaksi);
        baddtransaction = view.findViewById(R.id.btambahtransaksi);
        spinnertag = view.findViewById(R.id.spinnertag);
        spinnerwallet = view.findViewById(R.id.spinnerwallet);
        tvdata = view.findViewById(R.id.tvdata);
        tvwalletamount = view.findViewById(R.id.tvwalletamount);
        rbkeluar = view.findViewById(R.id.rkeluar);
        rbmasuk = view.findViewById(R.id.rdapat);
        rbkeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnertag.setAdapter(adaptercategorykeluar);
            }
        });
        rbmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnertag.setAdapter(adaptercategorymasuk);
            }
        });

        baddtransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etduit.getText().length() > 0) {
                    MoneyTransaction m;
                    if (rbkeluar.isChecked()) {
                        m = new MoneyTransaction(Integer.parseInt(etduit.getText().toString()), rbkeluar.getText().toString(), daftarkategorikeluar.get(spinnertag.getSelectedItemPosition()));
                    } else {
                        m = new MoneyTransaction(Integer.parseInt(etduit.getText().toString()), rbmasuk.getText().toString(), daftarkategorimasuk.get(spinnertag.getSelectedItemPosition()));
                    }
                    daftarwallet.get(spinnerwallet.getSelectedItemPosition()).addTransaction(m);
                    refreshdata();
                    etduit.getText().clear();
                }
            }
        });

        adaptercategorykeluar = new ArrayAdapter(view.getContext(), R.layout.support_simple_spinner_dropdown_item, daftarkategorikeluar);
        adaptercategorykeluar.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        adaptercategorymasuk = new ArrayAdapter(view.getContext(), R.layout.support_simple_spinner_dropdown_item, daftarkategorimasuk);
        adaptercategorymasuk.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        adapterwallet = new ArrayAdapter(view.getContext(), R.layout.support_simple_spinner_dropdown_item, daftarwallet);
        adapterwallet.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinnerwallet.setAdapter(adapterwallet);

        spinnerwallet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refreshdata();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rbmasuk.performClick();
    }

    public void refreshdata(){
        tvdata.setText("");
        for (int i = 0; i < daftarwallet.get(spinnerwallet.getSelectedItemPosition()).getWalletTransactions().size(); i++) {
            int trAmount = daftarwallet.get(spinnerwallet.getSelectedItemPosition()).getWalletTransactions().get(i).getTransactionAmount();
            String trType = daftarwallet.get(spinnerwallet.getSelectedItemPosition()).getWalletTransactions().get(i).getTransactionType();
            String trCategory = daftarwallet.get(spinnerwallet.getSelectedItemPosition()).getWalletTransactions().get(i).getTransactionCategory().getCategoryName();
            String trTime = daftarwallet.get(spinnerwallet.getSelectedItemPosition()).getWalletTransactions().get(i).getFormattedTransactionDate();
            tvdata.setText(tvdata.getText().toString()+""+trAmount+" - "+trType+" - "+ trCategory+" on "+trTime+"\n");
        }
        String displayBalanceWallet = String.format("%,8d%n", daftarwallet.get(spinnerwallet.getSelectedItemPosition()).getWalletAmount());
        tvwalletamount.setText("Rp "+displayBalanceWallet);
    }

}