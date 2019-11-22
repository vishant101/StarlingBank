package com.example.starlingbank.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.starlingbank.data.model.accounts.Account

@Database(entities = [Account::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}