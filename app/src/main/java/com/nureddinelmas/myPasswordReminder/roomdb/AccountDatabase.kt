package com.nureddinelmas.myPasswordReminder.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nureddinelmas.myPasswordReminder.model.Accounts


@Database(entities = [Accounts::class], version = 1)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accountsDao() : AccountDao
}