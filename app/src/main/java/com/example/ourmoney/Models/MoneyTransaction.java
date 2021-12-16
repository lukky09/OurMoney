package com.example.ourmoney.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.ourmoney.Activities.AddTransactionActivity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "transactions")
public class MoneyTransaction implements Parcelable, Serializable {
    @PrimaryKey(autoGenerate = true)
    private int transaction_id;
    private int wallet_id;
    private int category_id;
    private int transaction_amount;
    private String transaction_note;
    private Date transaction_date;

    public MoneyTransaction(int wallet_id, int category_id, int transaction_amount, String transaction_note, Date transaction_date) {
        this.wallet_id = wallet_id;
        this.category_id = category_id;
        this.transaction_amount = transaction_amount;
        this.transaction_note = transaction_note;
        this.transaction_date = transaction_date;
    }

    protected MoneyTransaction(Parcel in) {
        transaction_id = in.readInt();
        wallet_id = in.readInt();
        category_id = in.readInt();
        transaction_amount = in.readInt();
        transaction_note = in.readString();
        transaction_date = new Date(in.readLong());
    }

    public static final Creator<MoneyTransaction> CREATOR = new Creator<MoneyTransaction>() {
        @Override
        public MoneyTransaction createFromParcel(Parcel in) {
            return new MoneyTransaction(in);
        }

        @Override
        public MoneyTransaction[] newArray(int size) {
            return new MoneyTransaction[size];
        }
    };

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(int transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getTransaction_note() {
        return transaction_note;
    }

    public void setTransaction_note(String transaction_note) {
        this.transaction_note = transaction_note;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public String getTransaction_date_formatted(){
        return new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(this.transaction_date);
    }

    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(transaction_id);
        parcel.writeInt(wallet_id);
        parcel.writeInt(category_id);
        parcel.writeInt(transaction_amount);
        parcel.writeString(transaction_note);
        parcel.writeLong(transaction_date.getTime());
    }
}
