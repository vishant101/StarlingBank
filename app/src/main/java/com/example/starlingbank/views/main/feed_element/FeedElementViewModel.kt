package com.example.starlingbank.views.main.feed_element

import androidx.lifecycle.MutableLiveData
import com.example.starlingbank.data.model.trasnaction_feed.FeedItem
import com.example.starlingbank.views.base.BaseViewModel

class FeedElementViewModel: BaseViewModel() {
    private val feedItem = MutableLiveData<FeedItem>()
    val transactionName = MutableLiveData<String>()
    val transactionAmount = MutableLiveData<String>()

    fun bind(feedItem: FeedItem){
        this.feedItem.value = feedItem
        transactionName.value = feedItem.reference
        transactionAmount.value = "£${feedItem.amount.minorUnits/100}"
    }
}