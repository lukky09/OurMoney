package com.example.ourmoney.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ourmoney.Database.AppDatabase;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.R;
import com.example.ourmoney.databinding.ActivityAddTransactionBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTransactionActivity extends AppCompatActivity {

    ActivityAddTransactionBinding binding;
    MaterialDatePicker<Long> datePicker;
    Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        // buat date picker dari material design
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Pilih Tanggal");
        datePicker = builder.build();
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

    private void onClick(View view){
        int viewID = view.getId();
        if (viewID == R.id.btnExpense){
            binding.btnExpense.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.crimson_red));
            binding.btnIncome.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.silver));
            binding.outlinedTextField.setHint("Pengeluaran");
        }
        else if (viewID == R.id.btnIncome){
            binding.btnExpense.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.silver));
            binding.btnIncome.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.lime_green));
            binding.outlinedTextField.setHint("Pemasukan");
        }
        else if (viewID == R.id.btnAddTransaction){
            if (TextUtils.isEmpty(binding.tbAmount.getText())){
                binding.tbAmount.setError("Nominal Harus diisi");
                return;
            }
            if (selectedDate == null){
                binding.btnDatePicker.setError("Tanggal harus diisi");
                return;
            }
            int amount = Integer.parseInt(binding.tbAmount.getText().toString());
            if (amount <= 0){
                binding.tbAmount.setError("Harus lebih besar dari 0");
                return;
            }

            String note = binding.tbNote.getText().toString();
            MoneyTransaction transaction = new MoneyTransaction(1, 1, amount, note, selectedDate);

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