package com.nureddinelmas.myPasswordReminder.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nureddinelmas.myPasswordReminder.model.Accounts
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface AccountDao {

    @Query("SELECT * FROM Accounts")
    fun getAll() : Flowable<List<Accounts>>

    @Insert()
    fun insert(account: Accounts): Completable


    @Delete
    fun delete(account: Accounts) : Completable
}