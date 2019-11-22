package com.example.starlingbank.data.model.trasnaction_feed

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FeedItem (
    @PrimaryKey
    val feedItemUid: String,
    val categoryUid: String,
    val amount: Amount,
    val sourceAmount: Amount,
    val direction: String,
    val updatedAt: String,
    val transactionTime: String,
    val settlementTime: String,
    val source: String,
    val status: String,
    val counterPartyType: String,
    val counterPartyName: String,
    val counterPartySubEntityName: String,
    val counterPartySubEntityIdentifier: Int,
    val counterPartySubEntitySubIdentifier: Int,
    val reference: String,
    val country: String,
    val spendingCategory: String
)