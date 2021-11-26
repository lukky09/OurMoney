package com.example.ourmoney.Models.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourmoney.Models.TransactionWithRelation;
import com.example.ourmoney.databinding.RvItemHomeBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.Holder> {

    private ArrayList<TransactionWithRelation> transactions;
    private OnItemClickCallback onItemClickCallback;

    public TransactionAdapter(ArrayList<TransactionWithRelation> transactions){
        this.transactions = transactions;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvItemHomeBinding binding = RvItemHomeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        TransactionWithRelation transaction = transactions.get(position);
        holder.bind(transaction);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(transaction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private final RvItemHomeBinding binding;
        public Holder(@NonNull RvItemHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TransactionWithRelation transaction){
            String dateFormatted = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(transaction.transaction.getTransaction_date());
            String date = dateFormatted.substring(0, 6);
            String year = dateFormatted.substring(7, 11);
            binding.tvDate.setText(date);
            binding.tvYear.setText(year);
            binding.tvCategoryName.setText(transaction.category.getCategoryName());

            if (transaction.category.isPengeluaran()){
                binding.tvTransactionAmount.setTextColor(Color.parseColor("#ff0000"));
            }
            else{
                binding.tvTransactionAmount.setTextColor(Color.parseColor("#228b22"));
            }
            binding.tvTransactionAmount.setText("Rp. "+transaction.transaction.getTransaction_amount());

            String note = transaction.transaction.getTransaction_note().isEmpty() ? "" : transaction.transaction.getTransaction_note();
            binding.tvTransactionNote.setText(note);
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(TransactionWithRelation transaction);
    }
}
