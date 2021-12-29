package com.example.ourmoney.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.example.ourmoney.Database.AppDatabase;
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
    int currmonth;

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
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
        new GetTransactionAndCategories(getContext(), new GetTransactionAndCategories.GetTransactionAndCategoriesCallback() {
            @Override
            public void postExecute(List<Category> c, List<TransactionWithRelation> t) {
                cats = new ArrayList<>();
                cats.addAll(c);
                trans = new ArrayList<>();
                trans.addAll(t);
                entries = new ArrayList<>();
                entries2 = new ArrayList<>();
                setdata(0);
            }
        }).execute();
        currmonth = Integer.parseInt((String) DateFormat.format("MM", new Date()));
        binding.spnPilihrentang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setdata(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setdata(int index) {
        int tempmonth;
        entries = new ArrayList<>();
        entries2 = new ArrayList<>();
        ArrayList<Float> jumlah = new ArrayList<>();
        ArrayList<Integer> colors1 = new ArrayList<>();
        ArrayList<Integer> colors2 = new ArrayList<>();
        for (int i = 0; i < cats.size(); i++) {
            jumlah.add((float) 0);
        }
        if (index == 0) {
            for (int i = 0; i < trans.size(); i++) {
                for (int j = 0; j < cats.size(); j++) {
                    if (trans.get(i).category.getCategoryId() == cats.get(j).getCategoryId()) {
                        jumlah.set(j, Math.abs(trans.get(i).transaction.getTransaction_amount()) + jumlah.get(j) + index);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < trans.size(); i++) {
                for (int j = 0; j < cats.size(); j++) {
                    tempmonth = currmonth - index + 1;
                    if (tempmonth < 0) tempmonth += 12;
                    tempmonth = Integer.parseInt((String) DateFormat.format("MM", trans.get(i).transaction.getTransaction_date())) - tempmonth;
                    if (trans.get(i).category.getCategoryId() == cats.get(j).getCategoryId() && tempmonth == 0) {
                        jumlah.set(j, Math.abs(trans.get(i).transaction.getTransaction_amount()) + jumlah.get(j));
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < cats.size(); i++) {
            if (jumlah.get(i) > 0) {
                if (cats.get(i).isPengeluaran()) {
                    entries.add(new PieEntry(jumlah.get(i), cats.get(i).getCategoryName()));
                    colors1.add(Color.rgb(220 + colors1.size() * 5, 20 + colors1.size() * 30, 20 + colors1.size() * 35));
                } else {
                    entries2.add(new PieEntry(jumlah.get(i), cats.get(i).getCategoryName()));
                    colors2.add(Color.rgb(0 + colors2.size() * 30, 0 + colors2.size() * 30, 255));
                }
            }
        }
        PieDataSet dataSet = new PieDataSet(entries, "Pengeluaran");
        PieDataSet dataSet2 = new PieDataSet(entries2, "Pemasukan");
        dataSet.setColors(colors1);
        dataSet2.setColors(colors2);
        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.WHITE);
        PieData data2 = new PieData(dataSet2);
        data2.setValueTextColor(Color.WHITE);
        binding.pie1.getDescription().setEnabled(false);
        binding.pie2.getDescription().setEnabled(false);
        binding.pie1.getLegend().setEnabled(false);
        binding.pie1.getLegend().setEnabled(false);
        binding.pie1.setData(data);
        binding.pie1.highlightValues(null);
        binding.pie1.invalidate();
        binding.pie2.setData(data2);
        binding.pie2.highlightValues(null);
        binding.pie2.invalidate();

    }
}

class GetTransactionAndCategories {
    private final WeakReference<Context> weakContext;
    private final WeakReference<GetTransactionAndCategoriesCallback> weakCallback;

    public GetTransactionAndCategories( Context context, GetTransactionAndCategoriesCallback callback){
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

            handler.post(()->{
                weakCallback.get().postExecute(cat,tra);
            });
        });
    }

    interface GetTransactionAndCategoriesCallback{
        void postExecute(List<Category> c, List<TransactionWithRelation> t);
    }
}