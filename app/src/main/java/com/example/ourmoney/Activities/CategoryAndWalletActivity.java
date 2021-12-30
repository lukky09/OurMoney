package com.example.ourmoney.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.example.ourmoney.Database.AppDatabase;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.R;
import com.example.ourmoney.databinding.ActivityCategoryAndWalletBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryAndWalletActivity extends AppCompatActivity {

    boolean isCategory;
    ActivityCategoryAndWalletBinding binding;
    ArrayList<Wallet> daftarwallet;
    ArrayList<Category> daftarkategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_and_wallet);

        isCategory = getIntent().getBooleanExtra("isCategory", true);
        binding = ActivityCategoryAndWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (isCategory) {
            binding.ll.setVisibility(View.VISIBLE);
            binding.btnIncome.setOnClickListener(view -> showKategori(false));
            binding.btnExpense.setOnClickListener(view -> showKategori(true));

        } else {
            binding.tvtitlecatandwal.setText("Wallet");
        }

        getdata(false);

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
                setResult(69, i);
                finish();
                break;
            case R.id.additem:
                Intent in = new Intent(this, AddCategoryOrWalletActivity.class);
                in.putExtra("iscategory", isCategory);
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

    void getdata(boolean isPengeluaran){
        new GetBoth(this, new GetBoth.AddTransactionCallback() {
            @Override
            public void postExecute(List<Wallet> wallet, List<Category> categories) {
                daftarwallet = new ArrayList<>();
                daftarwallet.addAll(wallet);
                daftarkategori = new ArrayList<>();
                daftarkategori.addAll(categories);
                if(isCategory){
                    showKategori(isPengeluaran);
                }else{
                    ArrayAdapter<Wallet> adapter = new ArrayAdapter<Wallet>(CategoryAndWalletActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, daftarwallet);
                    binding.listviewcatwal.setAdapter(adapter);
                }
            }
        }).execute();
    }

    void showKategori(boolean isPengeluaran) {
        ArrayList<Category> listca = new ArrayList<>();
        for (Category c : daftarkategori) {
            if (isPengeluaran == c.isPengeluaran()) {
                listca.add(c);
            }
        }
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listca);
        binding.listviewcatwal.setAdapter(adapter);
        if (isPengeluaran) {
            binding.tvtitlecatandwal.setText("Kategori Pengeluaran");
            binding.btnExpense.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.crimson_red));
            binding.btnIncome.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.silver));
        } else {
            binding.tvtitlecatandwal.setText("Kategori Pemasukan");
            binding.btnExpense.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.silver));
            binding.btnIncome.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.lime_green));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getdata(false);
    }
}

class GetBoth{
    private WeakReference<Context> weakContext;
    private WeakReference<AddTransactionCallback> weakCallback;

    public GetBoth( Context context, AddTransactionCallback callback){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

            List<Wallet> lw = appDatabase.appDao().getallWallets();
            List<Category> lc = appDatabase.appDao().getallCategories();

            handler.post(()->{
                weakCallback.get().postExecute(lw,lc);
            });
        });
    }

    interface AddTransactionCallback{
        void postExecute(List<Wallet> wallet,List<Category> categories);
    }
}