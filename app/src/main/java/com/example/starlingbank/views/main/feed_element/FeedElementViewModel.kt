package com.example.starlingbank.views.main.feed_element

import androidx.lifecycle.MutableLiveData
import com.example.starlingbank.data.model.transaction_feed.FeedItem
import com.example.starlingbank.utils.round
import com.example.starlingbank.views.base.BaseViewModel

class FeedElementViewModel: BaseViewModel() {
    private val feedItem = MutableLiveData<FeedItem>()
    val transactionName = MutableLiveData<String>()
    val transactionAmount = MutableLiveData<String>()

    fun bind(feedItem: FeedItem){
        this.feedItem.value = feedItem
        transactionName.value = feedItem.spendingCategory
        val amount = round(feedItem.amount.minorUnits.toFloat() / 100, 2)
        if (feedItem.direction == "OUT") {
            transactionAmount.value = "-£$amount"
        } else {
            transactionAmount.value = "+£$amount"
        }
    }
}