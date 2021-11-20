package com.example.ourmoney.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.Wallet;

@Dao
public interface AppDao {

    // DAO MONEY TRANSACTION ---------------------------------------------
    @Insert
    void insertTransaction(MoneyTransaction transaction);

    @Update
    void updateTransaction(MoneyTransaction transaction);

    @Delete
    void deleteTransaction(MoneyTransaction transaction);

    // DAO CATEGORY ------------------------------------------------------
    @Insert
    void insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    // DAO WALLET --------------------------------------------------------
//    @Insert
//    void insertWallet(Wallet wallet);
//
//    @Update
//    void updateWallet(Wallet wallet);
//
//    @Delete
//    void deleteWallet(Wallet wallet);
}
