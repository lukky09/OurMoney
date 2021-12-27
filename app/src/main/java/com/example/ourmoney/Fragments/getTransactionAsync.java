package com.example.ourmoney.Fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ourmoney.Database.AppDatabase;
import com.example.ourmoney.Models.TransactionWithRelation;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class getTransactionAsync {
    private final WeakReference<Context> weakContext;
    private final WeakReference<getTransactionCallback> weakCallback;
    private List<TransactionWithRelation> transactionList;

    public getTransactionAsync(Context context, getTransactionCallback callback) {
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(callback);
    }

    public void execute() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        weakCallback.get().preExecute();
        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);

            transactionList = appDatabase.appDao().getAllTransactions();

            handler.post(() -> {
                weakCallback.get().postExecute(transactionList);
            });
        });
    }

    public interface getTransactionCallback {
        void preExecute();

        void postExecute(List<TransactionWithRelation> transactionList);
    }
}
