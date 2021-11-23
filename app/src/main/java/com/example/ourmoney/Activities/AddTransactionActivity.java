package com.example.ourmoney.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.ourmoney.Database.AppDatabase;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.R;
import com.example.ourmoney.databinding.ActivityAddTransactionBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTransactionActivity extends AppCompatActivity {

    ActivityAddTransactionBinding binding;
    MaterialDatePicker<Long> datePicker;
    Date selectedDate = new Date();
    ArrayList<Wallet> daftarwallet;
    ArrayList<Category> daftarcategory,daftarcategorysorted;
    ArrayAdapter<Wallet> walletadapter;
    ArrayAdapter<Category> categoryadapter;
    boolean isPengeluaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getData();

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Tambah Transaksi");

        binding.btnExpense.setOnClickListener(this::onClick);
        binding.btnIncome.setOnClickListener(this::onClick);
        binding.btnAddTransaction.setOnClickListener(this::onClick);

        binding.tbAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() == 1){
                    int input = Integer.parseInt(charSequence.toString());
                    if (input == 0){
                        binding.tbAmount.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        System.out.println(selectedDate);

        // buat date picker dari material design
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Pilih Tanggal");
        datePicker = builder.build();
        String today = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        binding.btnDatePicker.setText(today);
        binding.btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                selectedDate = new Date(selection);
                binding.btnDatePicker.setText(datePicker.getHeaderText());
            }
        });
    }

    private void getData(){
        new GetBoth(this, new GetBoth.AddTransactionCallback() {
            @Override
            public void postExecute(List<Wallet> wallet, List<Category> categories) {
                daftarwallet = new ArrayList<>();
                daftarcategory = new ArrayList<>();
                daftarcategorysorted = new ArrayList<>();
                daftarwallet.addAll(wallet);
                daftarcategory.addAll(categories);
                walletadapter = new ArrayAdapter<>(AddTransactionActivity.this, android.R.layout.simple_list_item_1, daftarwallet);
                binding.spinnerWallet.setAdapter(walletadapter);
                for (Category ca:daftarcategory) {
                    if(ca.isPengeluaran())daftarcategorysorted.add(ca);
                }
                categoryadapter = new ArrayAdapter<>(AddTransactionActivity.this, android.R.layout.simple_list_item_1, daftarcategorysorted);
                binding.spinnerCategory.setAdapter(categoryadapter);
                isPengeluaran = true;
            }
        }).execute();
    }

    private void onClick(View view){
        int viewID = view.getId();
        if (viewID == R.id.btnExpense){
            binding.btnExpense.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.crimson_red));
            binding.btnIncome.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.silver));
            binding.outlinedTextField.setHint("Pengeluaran");

            isPengeluaran = true;
            daftarcategorysorted.clear();
            for (Category ca:daftarcategory) {
                if(ca.isPengeluaran())daftarcategorysorted.add(ca);
            }
            categoryadapter.notifyDataSetChanged();
        }
        else if (viewID == R.id.btnIncome){
            binding.btnExpense.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.silver));
            binding.btnIncome.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.lime_green));
            binding.outlinedTextField.setHint("Pemasukan");

            isPengeluaran = false;
            daftarcategorysorted.clear();
            for (Category ca:daftarcategory) {
                if(!ca.isPengeluaran())daftarcategorysorted.add(ca);
            }
            categoryadapter.notifyDataSetChanged();
        }
        else if (viewID == R.id.btnAddTransaction){
            if (TextUtils.isEmpty(binding.tbAmount.getText())){
                binding.tbAmount.setError("Nominal Harus diisi");
                return;
            }
            int amount = Integer.parseInt(binding.tbAmount.getText().toString());
            if (amount <= 0){
                binding.tbAmount.setError("Harus lebih besar dari 0");
                return;
            }
            String note = binding.tbNote.getText().toString();
            MoneyTransaction transaction = new MoneyTransaction(daftarwallet.get(binding.spinnerWallet.getSelectedItemPosition()).getWallet_id(),
                    daftarcategorysorted.get(binding.spinnerCategory.getSelectedItemPosition()).getCategoryId(), amount, note, selectedDate);

            if (isPengeluaran){
                if (amount > daftarwallet.get(binding.spinnerWallet.getSelectedItemPosition()).getWalletAmount()){
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddTransactionActivity.this);
                    builder.setTitle("Warning");
                    builder.setMessage("Nominal yang anda masukan melebihi budget wallet anda. lanjutkan?");

                    builder.setPositiveButton("Ya", (dialogInterface, i) -> continueTransaction(transaction));
                    builder.setNegativeButton("Tidak", (dialogInterface, i) -> {});
                    builder.show();
                }
                else{
                    continueTransaction(transaction);
                }
            }
            else{
                continueTransaction(transaction);
            }
        }
    }

    private void continueTransaction(MoneyTransaction transaction){
        new AddTransactionAsync(transaction, this, new AddTransactionAsync.AddTransactionCallback() {
            @Override
            public void preExecute() { }

            @Override
            public void postExecute(String msg) {
                Intent intent = new Intent();
                intent.putExtra("msg", msg);
                setResult(100, intent);
                finish();
            }
        }).execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            setResult(10);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

class AddTransactionAsync {
    private final WeakReference<Context> weakContext;
    private final WeakReference<AddTransactionCallback> weakCallback;
    private MoneyTransaction transaction;

    public AddTransactionAsync(MoneyTransaction transaction, Context context, AddTransactionCallback callback){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
        this.transaction = transaction;
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        weakCallback.get().preExecute();
        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

            Wallet w= appDatabase.appDao().getWalletbyID(transaction.getWallet_id()).get(0);
            Category c = appDatabase.appDao().getCategorybyID(transaction.getCategory_id()).get(0);
            if(c.isPengeluaran()){
                w.setWalletAmount(w.getWalletAmount()-transaction.getTransaction_amount());
            }else{
                w.setWalletAmount(w.getWalletAmount()+transaction.getTransaction_amount());
            }
            appDatabase.appDao().updateWallet(w);
            appDatabase.appDao().insertTransaction(transaction);

            handler.post(()->{
                weakCallback.get().postExecute("Transaksi Ditambahkan");
            });
        });
    }

    interface AddTransactionCallback{
        void preExecute();
        void postExecute(String msg);
    }
}