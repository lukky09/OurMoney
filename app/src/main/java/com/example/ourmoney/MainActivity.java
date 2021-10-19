package com.example.ourmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Wallet> daftarwallet;
    public static ArrayList<Category> daftarkategorikeluar, daftarkategorimasuk;
    Spinner spinnertag, spinnerwallet;
    ArrayAdapter adaptercategorykeluar,adaptercategorymasuk,adapterwallet;
    BottomNavigationView navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navbar = findViewById(R.id.bottomNavigationView);

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag;
                switch (item.getItemId()) {
                    case R.id.homeFragment:
                        frag = HomeFragment.newInstance(daftarwallet);
                        getSupportFragmentManager().beginTransaction().replace(R.id.penampungFragment, frag).commit();
                        break;
                    case R.id.manageFragment:
                        Toast.makeText(MainActivity.this, "Management", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addFragment:
                        frag = AddTransactionFragment.newInstance(daftarwallet,daftarkategorikeluar,daftarkategorimasuk);
                        getSupportFragmentManager().beginTransaction().replace(R.id.penampungFragment, frag).commit();
                        break;
                    case R.id.reportFragment:
                        Toast.makeText(MainActivity.this, "Reporto", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.userFragment:
                        Toast.makeText(MainActivity.this, "Profileu", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        daftarwallet = new ArrayList<>();
        daftarkategorikeluar = new ArrayList<>();
        daftarkategorimasuk = new ArrayList<>();

        daftarwallet.add(new Wallet("Wallet1", 0, new ArrayList<MoneyTransaction>()));
        daftarwallet.add(new Wallet("Wallet2", 0, new ArrayList<MoneyTransaction>()));

        daftarkategorikeluar.add(new Category("Makanan", "Pengeluaran"));
        daftarkategorikeluar.add(new Category("Minuman", "Pengeluaran"));

        daftarkategorimasuk.add(new Category("Gaji", "Pemasukan"));
        daftarkategorimasuk.add(new Category("Bonus", "Pemasukan"));

        Fragment welcomeFragment;
        welcomeFragment = HomeFragment.newInstance(daftarwallet);
        getSupportFragmentManager().beginTransaction().replace(R.id.penampungFragment, welcomeFragment).commit();

    }

    //BottomNavigationView.OnNavigationItemSelectedListener(){

    //}



}