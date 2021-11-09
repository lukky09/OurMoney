package com.example.ourmoney.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.R;

import java.util.ArrayList;

public class AddCategoryOrWalletActivity extends AppCompatActivity {

    LinearLayout ll;
    TextView nama;
    EditText etnama, etjum;
    Button b;
    boolean iscat;
    RadioButton rin, rout;
    ArrayList<Wallet> daftarwallet;
    ArrayList<Category> daftarcatmasuk, daftarcatkeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_or_wallet);

        nama = findViewById(R.id.tvnamacatwal);
        etnama = findViewById(R.id.etnamacatwal);
        ll = findViewById(R.id.lladd);
        b = findViewById(R.id.btncatwal);
        iscat = getIntent().getBooleanExtra("iscategory", true);

        if (iscat) {
            rin = findViewById(R.id.rbinwalcat);
            rout = findViewById(R.id.rboutwalcat);
            etnama.setHint("Nama Kategori");
            rin.setChecked(true);
            nama.setText("Nama Kategori");
            ll.removeViewAt(3);
            b.setText("Add Category");
            daftarcatmasuk = getIntent().getParcelableArrayListExtra("in");
            daftarcatkeluar = getIntent().getParcelableArrayListExtra("out");
        } else {
            etjum = findViewById(R.id.etjumwallet);
            ll.removeViewAt(2);
            daftarwallet = getIntent().getParcelableArrayListExtra("dftwallet");
        }
    }

    public void additem(View view) {
        Intent i = new Intent();
        boolean ada = false;
        if (etnama.getText().toString().trim().length() == 0)
            Toast.makeText(this, "Mohon diisi semuanya", Toast.LENGTH_SHORT).show();
        else {
            if (iscat) {
                for (Category c : daftarcatmasuk) {
                    if (c.getCategoryName().equals(etnama)) {
                        ada = true;
                        break;
                    }
                }
                if (!ada) {
                    for (Category c : daftarcatkeluar) {
                        if (c.getCategoryName().equals(etnama)) {
                            ada = true;
                            break;
                        }
                    }
                }
                if (ada) Toast.makeText(this, "Kategori sudah ada", Toast.LENGTH_SHORT).show();
                else {
                    String kategori;
                    if (rin.isChecked()) kategori = "Pemasukan";
                    else kategori = "Pengeluaran";
                    i.putExtra("kat", new Category(etnama.getText().toString(), kategori));
                    setResult(0, i);
                    finish();
                }
            } else {
                if (etjum.getText().toString().trim().length() == 0)
                    Toast.makeText(this, "Mohon diisi semuanya", Toast.LENGTH_SHORT).show();
                else {
                    for (Wallet w : daftarwallet) {
                        if (w.getWalletName().equals(etnama)) {
                            ada = true;
                            break;
                        }
                    }
                    if (ada) Toast.makeText(this, "Kategori sudah ada", Toast.LENGTH_SHORT).show();
                    else {
                        i.putExtra("wal", new Wallet(etnama.getText().toString(),Integer.parseInt(etjum.getText().toString()),new ArrayList<MoneyTransaction>()));
                        setResult(1, i);
                        finish();
                    }
                }
            }
        }
    }
}