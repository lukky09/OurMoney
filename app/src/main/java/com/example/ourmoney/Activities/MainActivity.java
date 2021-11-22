package com.example.ourmoney.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ourmoney.Database.AppDatabase;
import com.example.ourmoney.Fragments.AddTransactionFragment;
import com.example.ourmoney.Fragments.HomeFragment;
import com.example.ourmoney.Fragments.ProfileFragment;
import com.example.ourmoney.Fragments.SavingCashFragment;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ArrayList<Wallet> daftarwallet;
    ArrayList<Category> daftarkategori;
    BottomNavigationView navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AddPresetData(this, () -> {
            getData();
        }).execute();


        navbar = findViewById(R.id.bottomNavigationView);

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag;
                switch (item.getItemId()) {
                    case R.id.homeFragment:
                        frag = HomeFragment.newInstance(daftarwallet);
                        getSupportFragmentManager().beginTransaction().replace(R.id.penampungFragment, frag).commit();
                        break;
                    case R.id.manageFragment:
                        frag = SavingCashFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.penampungFragment, frag).commit();
                        break;
                    case R.id.reportFragment:
                        Toast.makeText(MainActivity.this, "Reporto", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.userFragment:
                        frag = ProfileFragment.newInstance(daftarwallet);
                        getSupportFragmentManager().beginTransaction().replace(R.id.penampungFragment, frag).commit();
                        break;
                }

                return true;
            }
        });


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }

    void getData(){
        new GetBoth(this, new GetBoth.AddTransactionCallback() {
            @Override
            public void postExecute(List<Wallet> wallet, List<Category> categories) {
                daftarwallet = new ArrayList<>();
                daftarkategori = new ArrayList<>();
                daftarwallet.addAll(wallet);
                daftarkategori.addAll(categories);
                Fragment welcomeFragment;
                welcomeFragment = HomeFragment.newInstance(daftarwallet);
                getSupportFragmentManager().beginTransaction().replace(R.id.penampungFragment, welcomeFragment).commit();
            }
        }).execute();
    }

}

class AddPresetData {
    private final WeakReference<Context> weakContext;
    private final WeakReference<CallbackIGuess> weakCallback;

    public AddPresetData(Context context, CallbackIGuess callback){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

            List<Category> cek1 = appDatabase.appDao().getallCategories();
            if(cek1.size()==0){
                appDatabase.appDao().insertCategory(new Category("Makanan", true));
                appDatabase.appDao().insertCategory(new Category("Minuman", true));
                appDatabase.appDao().insertCategory(new Category("Gaji", false));
                appDatabase.appDao().insertCategory(new Category("Bonus", false));
                appDatabase.appDao().insertWallet(new Wallet("Wallet1", 0));
                appDatabase.appDao().insertWallet(new Wallet("Wallet2", 0));
            }

            handler.post(()->{
                weakCallback.get().postExecute();
            });
        });
    }

    interface CallbackIGuess{
        void postExecute();
    }
}