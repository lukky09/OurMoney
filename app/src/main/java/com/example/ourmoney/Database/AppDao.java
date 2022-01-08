package com.example.ourmoney.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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

    @Query("DELETE FROM transactions")
    void nukeTransaction();

    @Query("DELETE FROM transactions where wallet_id=:id")
    void deleteTransactionByWalletId(int id);

    @Query("DELETE FROM transactions where category_id=:id")
    void deleteTransactionByCategoryId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllTransaction(List<MoneyTransaction> trans);

    @Insert
    void insertTransaction(MoneyTransaction transaction);

    @Update
    void updateTransaction(MoneyTransaction transaction);

    @Delete
    void deleteTransaction(MoneyTransaction transaction);

    // DAO CATEGORY ------------------------------------------------------

    @Query("select * from category")
    List<Category> getallCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCategory(List<Category> cats);

    @Query("select * from category where lower(categoryName)=lower(:name)")
    List<Category> getCategorybyName(String name);

    @Query("select * from category where categoryId=:id")
    List<Category> getCategorybyID(int id);

    @Query("DELETE FROM category")
    void nukeCategory();

    @Insert
    void insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    // DAO WALLET --------------------------------------------------------

    @Query("select * from wallets")
    List<Wallet> getallWallets();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllWallet(List<Wallet> wals);

    @Query("select * from wallets where lower(walletName)=lower(:name)")
    List<Wallet> getWalletbyName(String name);

    @Query("select * from wallets where wallet_id=:id")
    List<Wallet> getWalletbyID(int id);

    @Query("DELETE FROM wallets")
    void nukeWallet();

    @Insert
    void insertWallet(Wallet wallet);

    @Update
    void updateWallet(Wallet wallet);

    @Delete
    void deleteWallet(Wallet wallet);

    //DAO TARGET --------------------------------------------------------
    @Query("select * from savingtarget where id = 1")
    List<SavingTarget> getTarget();

    @Query("DELETE FROM savingtarget")
    void nukeTarget();

    @Insert
    void insertTarget(SavingTarget target);

    @Update
    void updateTarget(SavingTarget target);

    //DAO RELATIONSHIP --------------------------------------------------------
    @Transaction
    @Query("SELECT * FROM transactions order by transaction_date desc")
    List<TransactionWithRelation> getAllTransactions();
}
