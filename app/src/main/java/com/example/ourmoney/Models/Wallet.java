package com.example.ourmoney.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "wallets")
public class Wallet implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int wallet_id;
    private String walletName;
    private int walletAmount;

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public Wallet(String walletName, int walletAmount) {
        this.walletName = walletName;
        this.walletAmount = walletAmount;
    }

    protected Wallet(Parcel in) {
        walletName = in.readString();
        walletAmount = in.readInt();
    }

    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };

    @Override
    public String toString() {
        return  walletName;
    }

    public void addTransaction(MoneyTransaction newtr){
//        walletTransactions.add(newtr);
//        walletAmount = 0;
//        for (int i = 0; i < walletTransactions.size(); i++) {
//            if(walletTransactions.get(i).getTransactionType().equals("Pengeluaran")){
//                walletAmount -= walletTransactions.get(i).getTransactionAmount();
//            }else{
//                walletAmount += walletTransactions.get(i).getTransactionAmount();
//            }
//        }
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public int getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(int walletAmount) {
        this.walletAmount = walletAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(wallet_id);
        parcel.writeString(walletName);
        parcel.writeInt(walletAmount);
    }
}

/*
package com.example.ourmoney;

        import java.util.ArrayList;

public class Wallet {
    private String walletName;
    private int walletAmount;
    private ArrayList<MoneyTransaction> walletTransactions;

    @Override
    public String toString() {
        return  walletName;
    }

    public void addTransaction(MoneyTransaction newtr){
        walletTransactions.add(newtr);
        walletAmount = 0;
        for (int i = 0; i < walletTransactions.size(); i++) {
            if(walletTransactions.get(i).getTransactionType().equals("Pengeluaran")){
                walletAmount -= walletTransactions.get(i).getTransactionAmount();
            }else{
                walletAmount += walletTransactions.get(i).getTransactionAmount();
            }
        }
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public int getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(int walletAmount) {
        this.walletAmount = walletAmount;
    }

    public ArrayList<MoneyTransaction> getWalletTransactions() {
        return walletTransactions;
    }

    public void setWalletTransactions(ArrayList<MoneyTransaction> walletTransactions) {
        this.walletTransactions = walletTransactions;
    }

    public Wallet(String walletName, int walletAmount, ArrayList<MoneyTransaction> walletTransactions) {
        this.walletName = walletName;
        this.walletAmount = walletAmount;
        this.walletTransactions = walletTransactions;
    }
}
*/

