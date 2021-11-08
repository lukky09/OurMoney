package com.example.ourmoney.Models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MoneyTransaction {
    private int transactionAmount;
    private String transactionType;
    private Category transactionCategory;
    private Date transactionDate;

    public MoneyTransaction(int transactionAmount, String transactionType, Category transactionCategory) {
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.transactionCategory = transactionCategory;
        this.transactionDate = new Date();
    }

    public int getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Category getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(Category transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public Date getTransactionDate() { return transactionDate; }
    public String getFormattedTransactionDate() {
        DateFormat df = new SimpleDateFormat("E, dd MMM yyyy");
        return df.format(this.transactionDate);
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }


}
