package com.example.ourmoney.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.R;

import java.util.ArrayList;

public class CategoryAndWalletActivity extends AppCompatActivity {

    boolean isCategory;
    ListView lv;
    ArrayList<Wallet> daftarwallet;
    ArrayList<Category> katmasuk, katkeluar;
    Button hiddenkeluaran, hiddenmasukan;
    LinearLayout ll;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_and_wallet);

        isCategory = getIntent().getBooleanExtra("isCategory", true);
        lv = findViewById(R.id.listviewcatwal);
        ll = findViewById(R.id.ll);
        title = findViewById(R.id.tvtitlecatandwal);

        if (isCategory) {
            katmasuk = getIntent().getParcelableArrayListExtra("masukan");
            hiddenmasukan = new Button(this);
            hiddenmasukan.setText("Pemasukan");
            hiddenmasukan.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            ll.addView(hiddenmasukan);
            hiddenmasukan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPemasukan();
                }
            });

            katkeluar = getIntent().getParcelableArrayListExtra("keluaran");
            hiddenkeluaran = new Button(this);
            hiddenkeluaran.setText("Pengeluaran");
            hiddenkeluaran.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            ll.addView(hiddenkeluaran);
            hiddenkeluaran.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPengeluaran();
                }
            });

            showPemasukan();

        } else {
            title.setText("Wallet");
            daftarwallet = getIntent().getParcelableArrayListExtra("wallets");
            ArrayAdapter<Wallet> adapter = new ArrayAdapter<Wallet>(this, android.R.layout.simple_list_item_1, android.R.id.text1, daftarwallet);
            lv.setAdapter(adapter);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent();
                if(isCategory){
                    i.putParcelableArrayListExtra("2", katmasuk);
                    i.putParcelableArrayListExtra("3", katkeluar);
                    setResult(69, i);
                }else{
                    i.putParcelableArrayListExtra("1", daftarwallet);
                    setResult(70, i);
                }

                finish();
                break;
            case R.id.additem:
                Intent in = new Intent(this, AddCategoryOrWalletActivity.class);
                in.putExtra("iscategory", isCategory);
                if (isCategory) {
                    in.putParcelableArrayListExtra("in", katmasuk);
                    in.putParcelableArrayListExtra("out", katkeluar);
                } else {
                    in.putParcelableArrayListExtra("dftwallet", daftarwallet);
                }
                startActivityForResult(in, 1);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.additem, menu);
        return true;
    }

    void showPemasukan() {
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(CategoryAndWalletActivity.this, android.R.layout.simple_list_item_1, katmasuk);
        lv.setAdapter(adapter);
        title.setText("Kategori Pemasukan");
    }

    void showPengeluaran() {
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(CategoryAndWalletActivity.this, android.R.layout.simple_list_item_1, katkeluar);
        lv.setAdapter(adapter);
        title.setText("Kategori Pengeluaran");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            //utk result category
            Category c = data.getParcelableExtra("kat");
            if (c.getCategoryType().equals("Pengeluaran")) katkeluar.add(c);
            else katmasuk.add(c);
            showPemasukan();
        } else if (resultCode == 1) {
            //utk result category
            daftarwallet.add(data.getParcelableExtra("wal"));
            ArrayAdapter<Wallet> adapter = new ArrayAdapter<Wallet>(this, android.R.layout.simple_list_item_1, android.R.id.text1, daftarwallet);
            lv.setAdapter(adapter);
        }
    }
}