package com.example.ourmoney.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.example.ourmoney.Database.AppDatabase;
import com.example.ourmoney.Fragments.getTransactionAsync;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.SavingTarget;
import com.example.ourmoney.Models.TransactionWithRelation;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.databinding.ActivityStoreDataBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StoreDataActivity extends AppCompatActivity {

    ActivityStoreDataBinding binding;

    //models to be stored
    ArrayList<Wallet> daftarwallet;
    ArrayList<Category> daftarkategori;
    ArrayList<MoneyTransaction> daftartrans;
    private SavingTarget currentTarget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoreDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent par = getIntent();
        if(par.hasExtra("daftarwallet") && par.hasExtra("daftarkategori") && par.hasExtra("savingtarget")){
            daftarwallet = par.getParcelableArrayListExtra("daftarwallet");
            daftarkategori = par.getParcelableArrayListExtra("daftarkategori");
            currentTarget = par.getParcelableExtra("savingtarget");

            new getTransactionAsync(this, new getTransactionAsync.getTransactionCallback() {
                @Override
                public void preExecute() {

                }

                @Override
                public void postExecute(List<TransactionWithRelation> transactionList) {
                    daftartrans = new ArrayList<MoneyTransaction>();
                    for (int i = 0; i < transactionList.size(); i++) {
                        daftartrans.add(transactionList.get(i).transaction);
                    }
                }
            }).execute();

            System.out.println("Wallets : "+daftarwallet.size());
            System.out.println("Categories : "+daftarkategori.size());
            System.out.println("Current target : "+currentTarget.toString());
        }else{
            System.out.println("extra failed");
        }

        binding.btnDosave.setOnClickListener(this::doWriteSerialize);
        binding.btnDoload.setOnClickListener(this::doReadSerialize);


    }

    public void doWriteSerialize(View view){
        String pathToOurMoneyFolder = getExternalFilesDir(null).getAbsolutePath();
        String fileName = pathToOurMoneyFolder + File.separator +"saveourmoney.txt";
//        String fileName = "lala/saveourmoney.txt";
        FileOutputStream fos = null;
        try {
//            fos = getApplicationContext().openFileOutput();
            fos = new FileOutputStream(fileName);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(currentTarget);
            os.writeObject(daftarwallet);
            os.writeObject(daftarkategori);
            os.writeObject(daftartrans);
            os.close();
            fos.close();
            Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
            System.out.println("Save data success!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Save data fail!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Save data fail!");
        }

    }

    public void doReadSerialize(View view){
        String pathToOurMoneyFolder = getExternalFilesDir(null).getAbsolutePath();
        String fileName = pathToOurMoneyFolder + File.separator +"saveourmoney.txt";
//        String fileName = "lala/saveourmoney.txt";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
//            SimpleClass simpleClass = (SimpleClass) is.readObject();
            currentTarget = (SavingTarget) is.readObject();
            daftarwallet = (ArrayList<Wallet>) is.readObject();
            daftarkategori = (ArrayList<Category>) is.readObject();
            daftartrans = (ArrayList<MoneyTransaction>) is.readObject();
            System.out.println("Wallets : "+daftarwallet.size());
            System.out.println("Categories : "+daftarkategori.size());
            System.out.println("Current target : "+currentTarget.toString());
            System.out.println("Trans : "+daftartrans.toString());
            new SaveDatabaseAsync(daftarwallet, daftarkategori, daftartrans, currentTarget, getApplicationContext(), new SaveDatabaseAsync.SaveDatabaseCallback() {
                @Override
                public void postExecute(String message) {
                    Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                }
            }).execute();
            is.close();
            fis.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}

class SaveDatabaseAsync{
    private final WeakReference<Context> weakContext;
    private final WeakReference<SaveDatabaseCallback> weakCallback;
    private List<Wallet> daftarwallet;
    private List<Category> daftarkategori;
    private List<MoneyTransaction> daftartrans;
    private SavingTarget currentTarget;
    public SaveDatabaseAsync(ArrayList<Wallet> daftarWallet, ArrayList<Category> daftarkategori,ArrayList<MoneyTransaction> daftartransaksi, SavingTarget currentTarget, Context context, SaveDatabaseCallback callback){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
        this.daftarwallet = new ArrayList<>();
        this.daftarkategori = new ArrayList<>();
        this.daftartrans = new ArrayList<>();
        this.daftarwallet.addAll(daftarWallet);
        this.daftarkategori.addAll(daftarkategori);
        this.daftartrans.addAll(daftartransaksi);
        this. currentTarget = currentTarget;
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

            appDatabase.appDao().nukeCategory();
            appDatabase.appDao().insertAllCategory(daftarkategori);
            appDatabase.appDao().nukeWallet();
            appDatabase.appDao().insertAllWallet(daftarwallet);
            appDatabase.appDao().nukeTransaction();
            appDatabase.appDao().insertAllTransaction(daftartrans);
            appDatabase.appDao().updateTarget(currentTarget);

            handler.post(()->{
                weakCallback.get().postExecute("Transaksi Ditambahkan");
            });
        });
    }

    interface SaveDatabaseCallback{
        void postExecute(String message);
    }
}