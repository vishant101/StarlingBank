package com.example.starlingbank.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.starlingbank.data.model.accounts.Account

@Dao
interface AccountDao {
    @get:Query("SELECT * FROM Account ")
    val all: List<Account>

    @Insert
    fun insertAll(vararg account: Account)

    @Insert
    fun insert(account: Account)
}