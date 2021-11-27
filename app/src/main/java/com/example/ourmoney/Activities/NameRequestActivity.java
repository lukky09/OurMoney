package com.example.ourmoney.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ourmoney.R;

public class NameRequestActivity extends AppCompatActivity {

    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_request);
        et = findViewById(R.id.etnamaawal);
    }

    public void masukinnama(View view) {
        String nama = et.getText().toString().trim();
        if(nama.length()>0){
            SharedPreferences sharedPref = this.getSharedPreferences("setting",this.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("name",nama);
            editor.apply();
            pindah();
        }else{
            Toast.makeText(this, "Nama jangan kosong", Toast.LENGTH_SHORT).show();
        }
    }

    void pindah(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}