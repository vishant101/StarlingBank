package com.example.starlingbank.network

import com.example.starlingbank.data.model.Accounts
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header

interface AccountApi {
    @GET("api/v2/accounts")
    fun getAccounts(@Header("Authorization") accessToken: String?): Observable<Accounts>
}