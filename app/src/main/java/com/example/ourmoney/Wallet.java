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
