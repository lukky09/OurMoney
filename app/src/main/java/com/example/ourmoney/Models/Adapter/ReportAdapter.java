package com.example.ourmoney.Models.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.TransactionWithRelation;
import com.example.ourmoney.R;
import com.google.android.material.transition.Hold;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.Holder> {

    ArrayList<Float> jumlah;
    ArrayList<Category> kategoris;
    ArrayList<Float> sortedjumlah;
    ArrayList<Category> sortedkategoris;
    Boolean ispengeluaran;
    private OnItemClickCallback onItemClickCallback;

    public ReportAdapter(ArrayList<Float> jumlah, ArrayList<Category> kategoris,Boolean ispengeluaran) {
        sortedjumlah = new ArrayList<>();
        sortedkategoris = new ArrayList<>();
        this.jumlah = jumlah;
        this.kategoris = kategoris;
        this.ispengeluaran = ispengeluaran;
        for (int i = 0; i < jumlah.size(); i++) {
            if (jumlah.get(i) > 0 && kategoris.get(i).isPengeluaran() == ispengeluaran) {
                sortedkategoris.add(kategoris.get(i));
                sortedjumlah.add(jumlah.get(i));
            }
        }
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (sortedkategoris.size() == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.non_item_lmao, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        }
        Holder viewHolder = new Holder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if(sortedkategoris.size() > 0){
            holder.bind(sortedkategoris.get(position).getCategoryName(),sortedjumlah.get(position));

            String category = sortedkategoris.get(position).getCategoryName();
            int value = sortedjumlah.get(position).intValue();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickCallback.onItemClicked(category, value);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(sortedkategoris.size() == 0) return 1;
        else return sortedkategoris.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvnama,tvjum;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvnama = itemView.findViewById(R.id.tvnamakatreport);
            tvjum = itemView.findViewById(R.id.tvjumlahsubreport);
        }

        void bind(String namakat,float jumlah){
            tvnama.setText(namakat);
            String displayBalance = "Rp. " + String.format("%,.0f", Float.parseFloat(jumlah+""));
            tvjum.setText(displayBalance);
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(String category, int value);
    }
}
