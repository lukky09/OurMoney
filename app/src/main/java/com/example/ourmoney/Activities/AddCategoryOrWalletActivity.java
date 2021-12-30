package com.example.ourmoney.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ourmoney.Database.AppDatabase;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.R;
import com.example.ourmoney.databinding.ActivityAddCategoryOrWalletBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddCategoryOrWalletActivity extends AppCompatActivity {

    ActivityAddCategoryOrWalletBinding binding;

    boolean iscat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_or_wallet);

        binding = ActivityAddCategoryOrWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iscat = getIntent().getBooleanExtra("iscategory", true);

        if (iscat) {
            binding.tiLayoutWallet.setHint("Nama Kategori");
            binding.rbinwalcat.setChecked(true);
            binding.tvnamacatwal.setText("Tambah Kategori");
            binding.lladd.removeViewAt(3);
            binding.btncatwal.setText("Tambah Kategori");
        } else {
            binding.lladd.removeViewAt(2);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
    }

    public void additem(View view) {
        Intent i = new Intent();
        String nama = binding.etnamacatwal.getText().toString().trim();
        if (nama.length() == 0)
            Toast.makeText(this, "Mohon diisi semuanya", Toast.LENGTH_SHORT).show();
        else {
            if (iscat) {
                new AddCategory(new Category(nama, binding.rboutwalcat.isChecked()), this, new AddCategory.AddWalletallback() {
                    @Override
                    public void postExecute(String msg, boolean succ) {
                        Toast.makeText(AddCategoryOrWalletActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if(succ){
                            setResult(0, i);
                            finish();
                        }
                    }
                }).execute();
            } else {
                if (binding.etjumwallet.getText().toString().trim().length() == 0)
                    Toast.makeText(this, "Mohon diisi semuanya", Toast.LENGTH_SHORT).show();
                else {
                    new AddWallet(new Wallet(nama, Integer.parseInt(binding.etjumwallet.getText().toString())), this, new AddWallet.AddWalletallback() {
                        @Override
                        public void postExecute(String msg, boolean succ) {
                            Toast.makeText(AddCategoryOrWalletActivity.this, msg, Toast.LENGTH_SHORT).show();
                            if(succ){
                                setResult(1, i);
                                finish();
                            }
                        }
                    }).execute();

                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i = new Intent();
        setResult(0, i);
        finish();
        return true;
    }
}

class AddWallet {
    private WeakReference<Context> weakContext;
    private WeakReference<AddWalletallback> weakCallback;
    private Wallet wallet;

    public AddWallet(Wallet wallet, Context context, AddWalletallback callback){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
        this.wallet = wallet;
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

            String msg;
            Boolean succ;
            List<Wallet> cek = appDatabase.appDao().getWalletbyName(wallet.getWalletName());
            if(cek.size()>0){
                msg = "Wallet sudah ada";
                succ = false;
            }else{
                wallet.setWalletName(wallet.getWalletName().substring(0, 1).toUpperCase() + wallet.getWalletName().substring(1).toLowerCase());
                appDatabase.appDao().insertWallet(wallet);
                msg = "Wallet ditambahkan :D";
                succ = true;
            }

            handler.post(()->{
                weakCallback.get().postExecute(msg,succ);
            });
        });
    }

    interface AddWalletallback{
        void postExecute(String msg,boolean succ);
    }
}

class AddCategory {
    private WeakReference<Context> weakContext;
    private WeakReference<AddWalletallback> weakCallback;
    private Category category;

    public AddCategory(Category category, Context context, AddWalletallback callback){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
        this.category = category;
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

            String msg;
            Boolean succ;
            List<Category> cek = appDatabase.appDao().getCategorybyName(category.getCategoryName());
            if(cek.size()>0){
                msg = "Kategori sudah ada";
                succ = false;
            }else{
                category.setCategoryName(category.getCategoryName().substring(0, 1).toUpperCase() + category.getCategoryName().substring(1).toLowerCase());
                appDatabase.appDao().insertCategory(category);
                msg = "Kategori ditambahkan :D";
                succ = true;
            }

            handler.post(()->{
                weakCallback.get().postExecute(msg,succ);
            });
        });
    }

    interface AddWalletallback{
        void postExecute(String msg,boolean succ);
    }
}