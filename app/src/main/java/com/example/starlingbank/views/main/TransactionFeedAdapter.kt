package com.example.starlingbank.views.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.starlingbank.R
import com.example.starlingbank.data.model.trasnaction_feed.FeedItem
import com.example.starlingbank.data.model.trasnaction_feed.TransactionFeed
import com.example.starlingbank.databinding.ItemFeedElementBinding
import com.example.starlingbank.views.main.feed_element.FeedElementViewModel

class TransactionFeedAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var transactionFeed: TransactionFeed

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemFeedElementBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_feed_element, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(transactionFeed.feedItems[position])
    }

    override fun getItemCount(): Int {
        return if(::transactionFeed.isInitialized) transactionFeed.feedItems.size else 0
    }

    fun updateTransactionFeed(transactionFeed: TransactionFeed){
        this.transactionFeed = transactionFeed
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemFeedElementBinding): RecyclerView.ViewHolder(binding.root){
        private val viewModel = FeedElementViewModel()

        fun bind(feedItem: FeedItem){
            viewModel.bind(feedItem)
            binding.viewModel = viewModel
        }
    }
}