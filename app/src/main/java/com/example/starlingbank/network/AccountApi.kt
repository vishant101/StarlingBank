package com.example.starlingbank.network

import com.example.starlingbank.data.model.accounts.Accounts
import com.example.starlingbank.data.model.savings_goal.SavingsGoalBody
import com.example.starlingbank.data.model.savings_goal.SavingsGoalResponse
import com.example.starlingbank.data.model.transaction_feed.TransactionFeed
import io.reactivex.Observable
import retrofit2.http.*

interface AccountApi {
    @GET("api/v2/accounts")
    fun getAccounts(@Header("Authorization") accessToken: String?): Observable<Accounts>

    @GET("/api/v2/feed/account/{accountUid}/category/{categoryUid}")
    fun getTransactionFeed(
        @Header("Authorization") accessToken: String?,
        @Path(value = "accountUid", encoded = true) accountUid:String,
        @Path(value = "categoryUid", encoded = true) categoryUid:String
        ):Observable<TransactionFeed>

    @PUT("/api/v2/account/{accountUid}/savings-goals")
    fun putSavingsGoal(
        @Header("Authorization") accessToken: String?,
        @Path(value = "accountUid", encoded = true) accountUid:String,
        @Body parameters : SavingsGoalBody
    ) : Observable<SavingsGoalResponse>

}