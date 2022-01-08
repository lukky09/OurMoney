package com.example.ourmoney.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.ourmoney.Database.AppDatabase;
import com.example.ourmoney.Models.Adapter.ReportAdapter;
import com.example.ourmoney.Models.Adapter.TransactionAdapter;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.TransactionWithRelation;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.databinding.FragmentHomeTransactionBinding;
import com.example.ourmoney.databinding.FragmentReportBinding;
import com.github.mikephil.*;

import com.example.ourmoney.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportFragment extends Fragment {

    ArrayList<PieEntry> entries;
    ArrayList<PieEntry> entries2;
    FragmentReportBinding binding;
    ArrayList<TransactionWithRelation> trans;
    ArrayList<Category> cats;
    ArrayList<Wallet> wallets;
    int currmonth,pilihanwallet,pilihantime;

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReportBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GetAll(getContext(), new GetAll.GetTransactionAndCategoriesCallback() {
            @Override
            public void postExecute(List<Category> c, List<TransactionWithRelation> t, List<Wallet> w) {
                pilihantime = 0;
                pilihanwallet = 0;
                cats = new ArrayList<>();
                cats.addAll(c);
                trans = new ArrayList<>();
                trans.addAll(t);
                wallets = new ArrayList<>();
                Wallet wtemp = new Wallet("Seluruh Wallet",0);
                wtemp.setWallet_id(-1);
                wallets.add(wtemp);
                wallets.addAll(w);
                ArrayAdapter<Wallet> spinnerArrayAdapter = new ArrayAdapter<Wallet>(getContext(), android.R.layout.simple_spinner_item,wallets);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spnPilihwallet.setAdapter(spinnerArrayAdapter);
                entries = new ArrayList<>();
                entries2 = new ArrayList<>();
                setdata(0,0);
                binding.spnPilihrentang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        pilihantime = i;
                        setdata(i,pilihanwallet);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                binding.spnPilihwallet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        pilihanwallet = i;
                        setdata(pilihantime,i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }).execute();
        currmonth = Integer.parseInt((String) DateFormat.format("MM", new Date()));
    }

    void setdata(int indextime,int indexwallet) {
        ArrayList<TransactionWithRelation> filteredtrans = new ArrayList<>();
        if (indexwallet == 0) {
            filteredtrans.addAll(trans);
        }else{
            for (TransactionWithRelation tr: trans) {
                if(tr.transaction.getWallet_id() == wallets.get(indexwallet).getWallet_id()){
                    filteredtrans.add(tr);
                }
            }
        }
        int tempmonth, total;
        total = 0;
        entries = new ArrayList<>();
        entries2 = new ArrayList<>();
        ArrayList<Float> jumlah = new ArrayList<>();
        ArrayList<Integer> colors1 = new ArrayList<>();
        ArrayList<Integer> colors2 = new ArrayList<>();
        for (int i = 0; i < cats.size(); i++) {
            jumlah.add((float) 0);
        }
        if (indextime == 0) {
            for (int i = 0; i < filteredtrans.size(); i++) {
                for (int j = 0; j < cats.size(); j++) {
                    if (filteredtrans.get(i).category.getCategoryId() == cats.get(j).getCategoryId()) {
                        jumlah.set(j, Math.abs(filteredtrans.get(i).transaction.getTransaction_amount()) + jumlah.get(j) + indextime);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < filteredtrans.size(); i++) {
                for (int j = 0; j < cats.size(); j++) {
                    tempmonth = currmonth - indextime + 1;
                    if (tempmonth < 0) tempmonth += 12;
                    tempmonth = Integer.parseInt((String) DateFormat.format("MM", filteredtrans.get(i).transaction.getTransaction_date())) - tempmonth;
                    if (filteredtrans.get(i).category.getCategoryId() == cats.get(j).getCategoryId() && tempmonth == 0) {
                        jumlah.set(j, Math.abs(filteredtrans.get(i).transaction.getTransaction_amount()) + jumlah.get(j));
                        break;
                    }
                }
            }
        }
        ReportAdapter adapter1 = new ReportAdapter(jumlah, cats, false);
        binding.rvmasuk.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvmasuk.setAdapter(adapter1);
        ReportAdapter adapter2 = new ReportAdapter(jumlah, cats, true);
        binding.rvkeluar.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvkeluar.setAdapter(adapter2);
        for (int i = 0; i < cats.size(); i++) {
            if (jumlah.get(i) > 0) {
                if (cats.get(i).isPengeluaran()) {
                    entries.add(new PieEntry(jumlah.get(i), cats.get(i).getCategoryName()));
                    total -= jumlah.get(i);
                    colors1.add(Color.rgb(220 + colors1.size() * 5, 20 + colors1.size() * 30, 20 + colors1.size() * 35));
                } else {
                    entries2.add(new PieEntry(jumlah.get(i), cats.get(i).getCategoryName()));
                    total += jumlah.get(i);
                    colors2.add(Color.rgb(0 + colors2.size() * 30, 0 + colors2.size() * 30, 255));
                }
            }
        }
        if(total>=0){
            String displayBalance = "Rp. " + String.format("%,.0f", Float.parseFloat(total+""));
            binding.tvtotalreport.setText("Pemasukan Bersih : "+displayBalance);
        }else{
            total = total * -1;
            String displayBalance = "Rp. " + String.format("%,.0f", Float.parseFloat(total+""));
            binding.tvtotalreport.setText("Pengeluaran Bersih : "+displayBalance);
        }


        PieDataSet dataSet = new PieDataSet(entries, "");
        PieDataSet dataSet2 = new PieDataSet(entries2, "");
        dataSet.setColors(colors1);
        dataSet2.setColors(colors2);
        dataSet.setValueFormatter(new PercentFormatter(binding.pie1));
        dataSet2.setValueFormatter(new PercentFormatter(binding.pie2));
        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.WHITE);
        PieData data2 = new PieData(dataSet2);
        data2.setValueTextColor(Color.WHITE);
        binding.pie1.getDescription().setEnabled(false);
        binding.pie2.getDescription().setEnabled(false);
        binding.pie1.setUsePercentValues(true);
        binding.pie2.setUsePercentValues(true);
        binding.pie1.getLegend().setEnabled(false);
        binding.pie2.getLegend().setEnabled(false);
        binding.pie1.setData(data);
        binding.pie1.highlightValues(null);
        binding.pie1.invalidate();
        binding.pie2.setData(data2);
        binding.pie2.highlightValues(null);
        binding.pie2.invalidate();

    }
}

class GetAll {
    private final WeakReference<Context> weakContext;
    private final WeakReference<GetTransactionAndCategoriesCallback> weakCallback;

    public GetAll( Context context, GetTransactionAndCategoriesCallback callback){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

            List<Category> cat = appDatabase.appDao().getallCategories();
            List<TransactionWithRelation> tra = appDatabase.appDao().getAllTransactions();
            List<Wallet> wal = appDatabase.appDao().getallWallets();

            handler.post(()->{
                weakCallback.get().postExecute(cat,tra,wal);
            });
        });
    }

    interface GetTransactionAndCategoriesCallback{
        void postExecute(List<Category> c, List<TransactionWithRelation> t,List<Wallet> w);
    }
}