package com.example.starlingbank.network

import com.example.starlingbank.data.model.accounts.Accounts
import com.example.starlingbank.data.model.trasnaction_feed.TransactionFeed
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AccountApi {
    @GET("api/v2/accounts")
    fun getAccounts(@Header("Authorization") accessToken: String?): Observable<Accounts>

    @GET("/api/v2/feed/account/{accountUid}/category/{categoryUid}")
    fun getTransactionFeed(
        @Header("Authorization") accessToken: String?,
        @Path(value = "accountUid", encoded = true) accountUid:String,
        @Path(value = "categoryUid", encoded = true) categoryUid:String
        ):Observable<TransactionFeed>

}