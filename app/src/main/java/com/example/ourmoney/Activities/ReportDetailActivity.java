package com.example.ourmoney.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ourmoney.Models.Adapter.TransactionAdapter;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.TransactionWithRelation;
import com.example.ourmoney.R;
import com.example.ourmoney.databinding.ActivityReportDetailBinding;

import java.util.ArrayList;
import java.util.Objects;

public class ReportDetailActivity extends AppCompatActivity {

    ActivityReportDetailBinding binding;
    ArrayList<TransactionWithRelation> transactions, transactionSorted;
    boolean isPengeluaran;
    String category;
    int total;
    TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReportDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        isPengeluaran = getIntent().getBooleanExtra("isPengeluaran", false);
        category = getIntent().getStringExtra("category");
        total = getIntent().getIntExtra("total", 0);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).show();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Detail " + category);

        transactions = getIntent().getParcelableArrayListExtra("transList");
        transactionSorted = new ArrayList<>();
        for (int i=0; i<transactions.size(); i++){
            Category transCat = transactions.get(i).category;
            if (transCat.isPengeluaran() == isPengeluaran && transCat.getCategoryName().equals(category)){
                transactionSorted.add(transactions.get(i));
            }
        }

        String displayTotal = "Rp " + String.format("%,.0f", Float.parseFloat(total+""));
        binding.tvHeader.setText(transactionSorted.size()+" Transaksi");
        binding.tvTotal.append(" "+displayTotal);

        binding.rvTransaction.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTransaction.setHasFixedSize(true);
        adapter = new TransactionAdapter(transactionSorted);
        adapter.setOnItemClickCallback(new TransactionAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TransactionWithRelation transaction) { }
        });
        binding.rvTransaction.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}