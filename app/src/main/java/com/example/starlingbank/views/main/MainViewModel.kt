package com.example.starlingbank.views.main

import android.util.Log
import com.example.starlingbank.data.database.AccountDao
import com.example.starlingbank.data.manager.AppDataManager
import com.example.starlingbank.data.model.accounts.Account
import com.example.starlingbank.data.model.savings_goal.SavingsGoalBody
import com.example.starlingbank.data.model.savings_goal.SavingsGoalResponse
import com.example.starlingbank.data.model.transaction_feed.Amount
import com.example.starlingbank.data.model.transaction_feed.TransactionFeed
import com.example.starlingbank.network.AccountApi
import com.example.starlingbank.utils.*
import com.example.starlingbank.views.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainViewModel(private val accountDao: AccountDao): BaseViewModel() {
    @Inject
    lateinit var accountApi: AccountApi
    private var accountSubscription: Disposable? = null
    private var transactionFeedSubscription: Disposable? = null
    private var createSavingsGoalSubscription: Disposable? =  null


    val transactionFeedAdapter: TransactionFeedAdapter = TransactionFeedAdapter()

    init {
        loadAccounts()
    }

    override fun onCleared() {
        super.onCleared()
        accountSubscription?.dispose()
        transactionFeedSubscription?.dispose()

    }

    private fun loadAccounts(){
        accountSubscription = Observable.fromCallable{ accountDao.all }
            .concatMap {
                    dbAccountList ->
                if(dbAccountList.isEmpty())
                    accountApi.getAccounts( AppDataManager.getBearerToken() ).concatMap {
                            apiAccountList ->
                                accountDao.insertAll(*apiAccountList.accounts.toTypedArray())
                                Observable.just(apiAccountList.accounts)
                    }
                else
                    Observable.just(dbAccountList)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setIsLoading(true) }
            .doOnTerminate { setIsLoading(false) }
            .subscribe(
                { result -> onLoadAccountsSuccess(result) },
                { error -> onLoadAccountsError(error) }
            )
    }

    private fun loadTransactionFeed(accountUid: String, categoryUid: String){
        transactionFeedSubscription = Observable.fromCallable { }
            .concatMap { accountApi.getTransactionFeed(AppDataManager.getBearerToken(), accountUid, categoryUid) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setIsLoading(true) }
            .doOnTerminate { setIsLoading(false) }
            .subscribe(
                { result -> onLoadTransactionFeedSuccess(result) },
                { error -> onLoadTransactionFeedError(error) }
            )
    }

    private fun createSavingsGoal(accountUid: String, savingsGoal: SavingsGoalBody){
        createSavingsGoalSubscription = Observable.fromCallable { }
            .concatMap { accountApi.putSavingsGoal( AppDataManager.getBearerToken(), accountUid, savingsGoal)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setIsLoading(true) }
            .doOnTerminate { setIsLoading(false) }
            .subscribe(
                { result -> onCreateSavingsGoalSuccess(result) },
                { error -> onCreateSavingsGoalError(error) }
            )
    }

    private fun onLoadAccountsError(error: Throwable) {
        Log.e(RESULT, error.toString())
    }

    private fun onLoadAccountsSuccess(accountsList: List<Account>) {
        Log.i(RESULT, accountsList.toString())
        val accountUid = accountsList[0].accountUid
        loadTransactionFeed(accountUid, accountsList[0].defaultCategory)

        val savingsGoalBody = SavingsGoalBody(
            name = SAVING_GOAL_NAME,
            currency = SAVING_GOAL_CURRENCY,
            target = Amount(SAVING_GOAL_CURRENCY, SAVING_GOAL_TARGET_MINOR_UNITS),
            base64EncodedPhoto = SAVING_GOAL_BASE64_ENCODED
        )
        createSavingsGoal(accountUid, savingsGoalBody)
    }

    private fun onLoadTransactionFeedError(error: Throwable) {
        Log.e(RESULT, error.toString())
    }

    private fun onLoadTransactionFeedSuccess(transactionFeed: TransactionFeed) {
        Log.i(RESULT, transactionFeed.toString())
        transactionFeedAdapter.updateTransactionFeed(transactionFeed)
    }

    private fun onCreateSavingsGoalError(error: Throwable) {
        Log.e(RESULT, error.toString())
    }

    private fun onCreateSavingsGoalSuccess(savingsGoalResponse: SavingsGoalResponse) {
        Log.i(RESULT, savingsGoalResponse.toString())
    }
    }