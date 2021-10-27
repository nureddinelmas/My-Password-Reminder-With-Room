package com.nureddinelmas.myPasswordReminder.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Accounts")
data class Accounts (

    @ColumnInfo(name = "accountName")
    var accountName: String,

    @ColumnInfo(name = "userName")
    var userName : String,

    @ColumnInfo(name = "password")
    var password: String

    ) {

    @PrimaryKey(autoGenerate = true)
        var id = 0

}