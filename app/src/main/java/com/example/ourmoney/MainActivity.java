package com.example.ourmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Wallet> daftarwallet;
    ArrayList<Category> daftarkategorikeluar, daftarkategorimasuk;
    Button baddtransaction;
    Spinner spinnertag, spinnerwallet;
    EditText etduit;
    TextView tvdata,tvwalletamount;
    RadioButton rbkeluar, rbmasuk;
    ArrayAdapter adaptercategorykeluar,adaptercategorymasuk,adapterwallet;
    BottomNavigationView navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        daftarwallet = new ArrayList<Wallet>();
        daftarkategorikeluar = new ArrayList<Category>();
        daftarkategorimasuk = new ArrayList<Category>();
        etduit = findViewById(R.id.tvtransaksi);
        baddtransaction = findViewById(R.id.btambahtransaksi);
        spinnertag = findViewById(R.id.spinnertag);
        spinnerwallet = findViewById(R.id.spinnerwallet);
        tvdata = findViewById(R.id.tvdata);
        tvwalletamount = findViewById(R.id.tvwalletamount);
        rbkeluar = findViewById(R.id.rkeluar);
        rbmasuk = findViewById(R.id.rdapat);
        navbar = findViewById(R.id.bottomNavigationView);

        spinnerwallet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refreshdata();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        daftarwallet.add(new Wallet("Wallet1", 0, new ArrayList<MoneyTransaction>()));
        daftarwallet.add(new Wallet("Wallet2", 0, new ArrayList<MoneyTransaction>()));

        daftarkategorikeluar.add(new Category("Makanan", "Pengeluaran"));
        daftarkategorikeluar.add(new Category("Minuman", "Pengeluaran"));

        daftarkategorimasuk.add(new Category("Gaji", "Pemasukan"));
        daftarkategorimasuk.add(new Category("Bonus", "Pemasukan"));

        adaptercategorykeluar = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, daftarkategorikeluar);
        adaptercategorykeluar.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        adaptercategorymasuk = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, daftarkategorimasuk);
        adaptercategorymasuk.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        adapterwallet = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, daftarwallet);
        adapterwallet.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinnerwallet.setAdapter(adapterwallet);

        rbmasuk.performClick();

    }

    //BottomNavigationView.OnNavigationItemSelectedListener(){

    //}



    public void addtransaksi(View view) {
        if(etduit.getText().length()>0){
            MoneyTransaction m;
            if(rbkeluar.isChecked()){
                m = new MoneyTransaction(Integer.parseInt(etduit.getText().toString()), rbkeluar.getText().toString(), daftarkategorikeluar.get(spinnertag.getSelectedItemPosition()));
            }else{
                m = new MoneyTransaction(Integer.parseInt(etduit.getText().toString()), rbmasuk.getText().toString(), daftarkategorimasuk.get(spinnertag.getSelectedItemPosition()));
            }
            daftarwallet.get(spinnerwallet.getSelectedItemPosition()).addTransaction(m);
            refreshdata();
            etduit.getText().clear();
        }
    }

    public void refreshdata(){
        tvdata.setText("");
        for (int i = 0; i < daftarwallet.get(spinnerwallet.getSelectedItemPosition()).getWalletTransactions().size(); i++) {
            int trAmount = daftarwallet.get(spinnerwallet.getSelectedItemPosition()).getWalletTransactions().get(i).getTransactionAmount();
            String trType = daftarwallet.get(spinnerwallet.getSelectedItemPosition()).getWalletTransactions().get(i).getTransactionType();
            String trCategory = daftarwallet.get(spinnerwallet.getSelectedItemPosition()).getWalletTransactions().get(i).getTransactionCategory().getCategoryName();
            tvdata.setText(tvdata.getText().toString()+""+trAmount+" - "+trType+" - "+ trCategory+"\n");
        }

        tvwalletamount.setText(daftarwallet.get(spinnerwallet.getSelectedItemPosition()).getWalletAmount()+"");


    }

    public void klikradio(View view) {
        if(view.getId() == R.id.rkeluar){
            spinnertag.setAdapter(adaptercategorykeluar);
        }else{
            spinnertag.setAdapter(adaptercategorymasuk);
        }
    }
}