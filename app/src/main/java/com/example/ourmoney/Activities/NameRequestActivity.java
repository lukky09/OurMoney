package com.example.ourmoney.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ourmoney.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
        doWriteSerialize();
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
    public void doWriteSerialize(){
        String pathToOurMoneyFolder = getExternalFilesDir(null).getAbsolutePath();
        String fileName = pathToOurMoneyFolder + File.separator +"saveourmoney.txt";
//        String fileName = "lala/saveourmoney.txt";
        FileOutputStream fos = null;
        try {
//            fos = getApplicationContext().openFileOutput();
            fos = new FileOutputStream(fileName);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.close();
            fos.close();
            Toast.makeText(getApplicationContext(), "Untuk load data, file txt bisa diletakkan di Internal Storage/Android/data/com.example.ourmoney/files", Toast.LENGTH_SHORT).show();
            System.out.println("Save data success!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Save data fail!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Save data fail!");
        }

    }
}