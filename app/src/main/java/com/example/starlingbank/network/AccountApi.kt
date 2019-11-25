package com.example.starlingbank.network

import com.example.starlingbank.data.model.accounts.Accounts
import com.example.starlingbank.data.model.savings_goal.SavingsGoalBody
import com.example.starlingbank.data.model.savings_goal.SavingsGoalResponse
import com.example.starlingbank.data.model.savings_goal.SavingsGoalTransferBody
import com.example.starlingbank.data.model.savings_goal.SavingsGoalTransferResponse
import com.example.starlingbank.data.model.transaction_feed.TransactionFeed
import com.example.starlingbank.data.model.transaction_feed.Amount
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


    @PUT("/api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}")
    fun putSavingsGoalTransfer(
        @Header("Authorization") accessToken: String?,
        @Path(value = "accountUid", encoded = true) accountUid:String,
        @Path(value = "savingsGoalUid", encoded = true) savingsGoalUid:String,
        @Path(value = "transferUid", encoded = true) transferUid:String,
        @Body parameters : SavingsGoalTransferBody
    ) : Observable<SavingsGoalTransferResponse>

}