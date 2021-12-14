package com.example.ourmoney.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ourmoney.Database.AppDatabase;
import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.TransactionWithRelation;
import com.example.ourmoney.Models.Wallet;
import com.example.ourmoney.R;
import com.example.ourmoney.databinding.ActivityAddTargetBinding;
import com.example.ourmoney.databinding.ActivityDetailBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    TransactionWithRelation transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gatau kenapa ga berfungsi :'v
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Detail Transaksi");

        if (getIntent().hasExtra("transaction")){
            transaction = getIntent().getParcelableExtra("transaction");

            String transType;
            if (transaction.category.isPengeluaran()){
                transType = "Pengeluaran";
                binding.tvTransactionAmount.setTextColor(Color.parseColor("#ff0000"));
            }
            else{
                transType = "Pemasukan";
                binding.tvTransactionAmount.setTextColor(Color.parseColor("#228b22"));
            }
            binding.tvTransactionType.setText(transType);
            String displayBalance = "Rp. " + String.format("%,.0f", Float.parseFloat(transaction.transaction.getTransaction_amount()+""));
            binding.tvTransactionAmount.setText(displayBalance);

            binding.tvCategory.setText(transaction.category.getCategoryName());
            binding.tvDate.setText(transaction.transaction.getTransaction_date_formatted());
            binding.tvWallet.setText(transaction.wallet.getWalletName());

            if (transaction.transaction.getTransaction_note().isEmpty()){
                binding.tvNote.setText("-");
            }
            else{
                binding.tvNote.setText(transaction.transaction.getTransaction_note());
            }
        }

        binding.btnEditTransaction.setOnClickListener(this::onClick);
        binding.btnDeleteTransaction.setOnClickListener(this::onClick);
    }

    private void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.btnEditTransaction){
            Intent intent = new Intent(this, AddTransactionActivity.class);
            intent.putExtra("isEdit", true);
            intent.putExtra("transaction", transaction);
            resultLauncher.launch(intent);
        }
        else if (viewId == R.id.btnDeleteTransaction){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(DetailActivity.this);
            builder.setTitle("Warning");
            builder.setMessage("Yakin ingin menghapus transaksi?");

            builder.setPositiveButton("Ya", (dialogInterface, i) -> deleteTransaction());
            builder.setNegativeButton("Tidak", (dialogInterface, i) -> {});
            builder.show();
        }
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 100){
                        Intent intent = new Intent();
                        String data = result.getData().getStringExtra("msg");
                        intent.putExtra("msg", data);
                        setResult(100, intent);
                        finish();
                    }
                }
            }
    );

    private void deleteTransaction(){
        new DeleteTransactionAsync(transaction.transaction, this, new DeleteTransactionAsync.deleteTransactionCallback() {
            @Override
            public void postExecute(String msg) {
                Intent intent = new Intent();
                intent.putExtra("msg", msg);
                setResult(100, intent);
                finish();
            }
        }).execute();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            setResult(10);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

class DeleteTransactionAsync {
    private final WeakReference<Context> weakContext;
    private final WeakReference<deleteTransactionCallback> weakCallback;
    private MoneyTransaction transaction;

    public DeleteTransactionAsync(MoneyTransaction transaction, Context context, deleteTransactionCallback callback){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
        this.transaction = transaction;
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

            appDatabase.appDao().deleteTransaction(transaction);

            // update isi wallet
            Wallet wallet = appDatabase.appDao().getWalletbyID(transaction.getWallet_id()).get(0);
            wallet.setWalletAmount(wallet.getWalletAmount() + transaction.getTransaction_amount());
            appDatabase.appDao().updateWallet(wallet);

            handler.post(()->{
                weakCallback.get().postExecute("Transaksi Berhasil Dihapus");
            });
        });
    }

    interface deleteTransactionCallback{
        void postExecute(String msg);
    }
}