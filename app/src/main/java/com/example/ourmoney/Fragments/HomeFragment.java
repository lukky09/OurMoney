package com.example.ourmoney.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ourmoney.Activities.AddTransactionActivity;
import com.example.ourmoney.Activities.DetailActivity;
import com.example.ourmoney.Database.AppDatabase;
import com.example.ourmoney.Models.Adapter.TransactionAdapter;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.TransactionWithRelation;
import com.example.ourmoney.R;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.databinding.FragmentHomeTransactionBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    ArrayList<Wallet> daftarwallet;
    ArrayList<Category> daftarkateg;
    ArrayList<TransactionWithRelation> transactions = new ArrayList<>();
    TransactionAdapter adapter;

    FragmentHomeTransactionBinding binding;


    public static HomeFragment newInstance(ArrayList<Wallet> w ){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, w);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            daftarwallet = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home_transaction, container, false);
        binding = FragmentHomeTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int totalBalance = 0;
        for (int i = 0; i < daftarwallet.size(); i++) {
            totalBalance+=daftarwallet.get(i).getWalletAmount();
        }
        String displayBalance = String.format("%,8d%n",totalBalance);
        binding.lblBalance.setText("Rp "+displayBalance);

        System.out.println("Current amount of transaction is "+daftarwallet.size());

        binding.fabAddTransaction.setOnClickListener(this::onClick);

        new getTransactionAsync(getActivity(), new getTransactionAsync.getTransactionCallback() {
            @Override
            public void preExecute() {  }

            @Override
            public void postExecute(List<TransactionWithRelation> transactionList) {
                setupRV(transactionList);
            }
        }).execute();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 100){
                        Intent data = result.getData();
                        Toast.makeText(getActivity(), data.getStringExtra("msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void onClick(View view){
        int viewID = view.getId();
        if (viewID == R.id.fabAddTransaction){
            Intent moveData = new Intent(getActivity(), AddTransactionActivity.class);
            resultLauncher.launch(moveData);
        }
    }

    private void setupRV(List<TransactionWithRelation> transactionList){
        binding.rvHome.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvHome.setHasFixedSize(true);

        transactions.addAll(transactionList);

        System.out.println("tes");
        adapter = new TransactionAdapter(transactions);
        adapter.setOnItemClickCallback(new TransactionAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TransactionWithRelation transaction) {
                Intent moveData = new Intent(getActivity(), DetailActivity.class);
                moveData.putExtra("transaction", transaction);
                resultLauncher.launch(moveData);
            }
        });
        binding.rvHome.setAdapter(adapter);
    }
}

class getTransactionAsync {
    private final WeakReference<Context> weakContext;
    private final WeakReference<getTransactionCallback> weakCallback;
    private List<TransactionWithRelation> transactionList;

    public getTransactionAsync(Context context, getTransactionCallback callback){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        weakCallback.get().preExecute();
        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

            transactionList = appDatabase.appDao().getAllTransactions();

            handler.post(()->{
                weakCallback.get().postExecute(transactionList);
            });
        });
    }

    interface getTransactionCallback{
        void preExecute();
        void postExecute(List<TransactionWithRelation> transactionList);
    }
}