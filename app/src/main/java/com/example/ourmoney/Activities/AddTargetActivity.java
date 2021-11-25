package com.example.ourmoney.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ourmoney.databinding.ActivityAddTargetBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Target deadline tidak boleh sebelum hari ini", Toast.LENGTH_SHORT).show();
        }
    }
}

class AddUpdateTargetAsync{

}