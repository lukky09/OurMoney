package com.example.ourmoney.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.appsearch.GetSchemaResponse;
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
import com.example.ourmoney.Fragments.ReportFragment;
import com.example.ourmoney.Fragments.SavingCashFragment;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.SavingTarget;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ArrayList<Wallet> daftarwallet;
    ArrayList<Category> daftarkategori;
    private SavingTarget currentTarget;
    private BottomNavigationView navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AddPresetData(this, this::getData).execute();


        navbar = findViewById(R.id.bottomNavigationView);

//        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                getData();
//            }
//        });


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
                        frag = SavingCashFragment.newInstance(currentTarget);
                        getSupportFragmentManager().beginTransaction().replace(R.id.penampungFragment, frag).commit();

                        break;
                    case R.id.reportFragment:
                        frag = ReportFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.penampungFragment, frag).commit();
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

    public void getData(){
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
                navbar.setSelectedItemId(R.id.homeFragment);
            }
        }).execute();
        new GetTarget(this, new GetTarget.TargetCallback() {
            @Override
            public void postExecute(SavingTarget target) {
                currentTarget = target;
            }
        }).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getData();
    }

    public SavingTarget getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(SavingTarget currentTarget) {
        this.currentTarget = currentTarget;
    }

//    public void setNavbarSelect(int i){
//        switch (i){
//            case 0:
//                navbar.setSelectedItemId(R.id.homeFragment);
//            case 1:
//                navbar.setSelectedItemId(R.id.manageFragment);
//            case 2:
//                navbar.setSelectedItemId(R.id.reportFragment);
//            default:
//                navbar.setSelectedItemId(R.id.userFragment);
//        }
//    }
}

class GetTarget{
    private final WeakReference<Context> weakContext;
    private final WeakReference<GetTarget.TargetCallback> weakCallback;

    public GetTarget(Context context, GetTarget.TargetCallback callback){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
    }
    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

            List<SavingTarget> list = appDatabase.appDao().getTarget();
            SavingTarget target = list.get(0);

            handler.post(()->{
                weakCallback.get().postExecute(target);
            });
        });
    }

    interface TargetCallback{
        void postExecute(SavingTarget target);
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
                Calendar cal = Calendar.getInstance();
                cal.set(2021, 11 ,25);
                appDatabase.appDao().insertTarget(new SavingTarget(50000d, new Date(),cal.getTime()));

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