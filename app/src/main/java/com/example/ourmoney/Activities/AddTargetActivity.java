package com.example.ourmoney.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import com.example.ourmoney.Database.AppDatabase;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.SavingTarget;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.databinding.ActivityAddTargetBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTargetActivity extends AppCompatActivity {

    MaterialDatePicker<Long> datePicker;
    Date selectedDate = new Date();
    ActivityAddTargetBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTargetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // buat date picker dari material design
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Pilih Tanggal");
        datePicker = builder.build();
        String today = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        binding.edBtndatetime.setText(today);
        binding.edBtndatetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                selectedDate = new Date(selection);
                binding.edBtndatetime.setText(datePicker.getHeaderText());
            }
        });
        binding.btnAddtarget.setOnClickListener(this::addTarget);
    }

    public void addTarget(View view){
        Date today = new Date();
        if(today.getTime() < selectedDate.getTime()){
            SavingTarget newTarget = new SavingTarget(Double.parseDouble(binding.edTargetamount.getText().toString()),today,selectedDate);
            new AddUpdateTargetAsync(newTarget, getApplicationContext(), new AddUpdateTargetAsync.TargetCallback() {
                @Override
                public void preExecute() {

                }

                @Override
                public void postExecute(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }).execute();
            Intent back = new Intent();
            back.putExtra("Target", (Parcelable) newTarget);
            setResult(110, back);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Target deadline tidak boleh sebelum hari ini", Toast.LENGTH_SHORT).show();
        }
    }
}

class AddUpdateTargetAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<AddUpdateTargetAsync.TargetCallback> weakCallback;
    private SavingTarget target;

    public AddUpdateTargetAsync(SavingTarget target, Context context, AddUpdateTargetAsync.TargetCallback callback){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
        this.target = target;
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        weakCallback.get().preExecute();
        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

            List<SavingTarget> list = appDatabase.appDao().getTarget();
            SavingTarget current = list.get(0);
            current.setTargetCreated(target.getTargetCreated());
            current.setTargetDeadline(target.getTargetDeadline());
            current.setTargetAmount(target.getTargetAmount());
            current.setAccumulated(target.getAccumulated());
            current.setActive(true);

            appDatabase.appDao().updateTarget(current);

            handler.post(()->{
                weakCallback.get().postExecute("Target Baru Ditambahkan");
            });
        });
    }

    interface TargetCallback{
        void preExecute();
        void postExecute(String msg);
    }
}