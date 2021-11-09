package com.example.ourmoney.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ourmoney.Fragments.AddTransactionFragment;
import com.example.ourmoney.Fragments.HomeFragment;
import com.example.ourmoney.Fragments.ProfileFragment;
import com.example.ourmoney.Fragments.SavingCashFragment;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Wallet> daftarwallet;
    public static ArrayList<Category> daftarkategorikeluar, daftarkategorimasuk;
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
                        frag = SavingCashFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.penampungFragment, frag).commit();
                        break;
                    case R.id.addFragment:
                        frag = AddTransactionFragment.newInstance(daftarwallet,daftarkategorikeluar,daftarkategorimasuk);
                        getSupportFragmentManager().beginTransaction().replace(R.id.penampungFragment, frag).commit();
                        break;
                    case R.id.reportFragment:
                        Toast.makeText(MainActivity.this, "Reporto", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.userFragment:
                        frag = ProfileFragment.newInstance(daftarwallet,daftarkategorimasuk,daftarkategorikeluar);
                        getSupportFragmentManager().beginTransaction().replace(R.id.penampungFragment, frag).commit();
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }


    //BottomNavigationView.OnNavigationItemSelectedListener(){

    //}



}