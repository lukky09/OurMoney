package com.example.ourmoney.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.SavingTarget;
import com.example.ourmoney.Models.TransactionWithRelation;
import com.example.ourmoney.Models.Wallet;

import java.util.List;

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

    @Query("select * from category")
    List<Category> getallCategories();

    @Query("select * from category where lower(categoryName)=lower(:name)")
    List<Category> getCategorybyName(String name);

    @Query("select * from category where categoryId=:id")
    List<Category> getCategorybyID(int id);

    @Insert
    void insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    // DAO WALLET --------------------------------------------------------

    @Query("select * from wallets")
    List<Wallet> getallWallets();

    @Query("select * from wallets where lower(walletName)=lower(:name)")
    List<Wallet> getWalletbyName(String name);

    @Query("select * from wallets where wallet_id=:id")
    List<Wallet> getWalletbyID(int id);

    @Insert
    void insertWallet(Wallet wallet);

    @Update
    void updateWallet(Wallet wallet);
//
//    @Delete
//    void deleteWallet(Wallet wallet);

    //DAO TARGET --------------------------------------------------------
    @Query("select * from savingtarget where id = 1")
    List<SavingTarget> getTarget();

    @Insert
    void insertTarget(SavingTarget target);

    @Update
    void updateTarget(SavingTarget target);

    //DAO RELATIONSHIP --------------------------------------------------------
    @Transaction
    @Query("SELECT * FROM transactions")
    List<TransactionWithRelation> getAllTransactions();
}
