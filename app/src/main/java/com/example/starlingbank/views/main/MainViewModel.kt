package com.example.starlingbank.views.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.starlingbank.data.database.AccountDao
import com.example.starlingbank.data.manager.AppDataManager
import com.example.starlingbank.data.model.accounts.Account
import com.example.starlingbank.data.model.savings_goal.SavingsGoalBody
import com.example.starlingbank.data.model.savings_goal.SavingsGoalResponse
import com.example.starlingbank.data.model.savings_goal.SavingsGoalTransferBody
import com.example.starlingbank.data.model.savings_goal.SavingsGoalTransferResponse
import com.example.starlingbank.data.model.transaction_feed.TransactionFeed
import com.example.starlingbank.data.model.transaction_feed.Amount
import com.example.starlingbank.network.AccountApi
import com.example.starlingbank.utils.*
import com.example.starlingbank.views.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import javax.inject.Inject


class MainViewModel(private val accountDao: AccountDao): BaseViewModel() {
    @Inject
    lateinit var accountApi: AccountApi
    private var accountSubscription: Disposable? = null
    private var transactionFeedSubscription: Disposable? = null
    private var createSavingsGoalSubscription: Disposable? = null
    private var transferSubscription: Disposable? = null

    private var transferAmount = 0
    private var accountUid: String? = null
    private var savingsGoalUid: String? = null
    val transferString = MutableLiveData<String>()
    val transactionFeedAdapter: TransactionFeedAdapter = TransactionFeedAdapter()

    init {
        loadAccounts()
        updateTransferString()
    }

    override fun onCleared() {
        super.onCleared()
        accountSubscription?.dispose()
        transactionFeedSubscription?.dispose()
        createSavingsGoalSubscription?.dispose()
        transferSubscription?.dispose()
    }

    private fun loadAccounts() {
        accountSubscription = Observable.fromCallable { accountDao.all }
            .concatMap { dbAccountList ->
                if (dbAccountList.isEmpty())
                    accountApi.getAccounts(AppDataManager.getBearerToken()).concatMap { apiAccountList ->
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

    private fun loadTransactionFeed(accountUid: String, categoryUid: String) {
        transactionFeedSubscription = Observable.fromCallable { }
            .concatMap {
                accountApi.getTransactionFeed(
                    AppDataManager.getBearerToken(),
                    accountUid,
                    categoryUid
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setIsLoading(true) }
            .doOnTerminate { setIsLoading(false) }
            .subscribe(
                { result -> onLoadTransactionFeedSuccess(result) },
                { error -> onLoadTransactionFeedError(error) }
            )
    }

    private fun createSavingsGoal(accountUid: String, savingsGoal: SavingsGoalBody) {
        createSavingsGoalSubscription = Observable.fromCallable { }
            .concatMap {
                accountApi.putSavingsGoal(
                    AppDataManager.getBearerToken(),
                    accountUid,
                    savingsGoal
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setIsLoading(true) }
            .doOnTerminate { setIsLoading(false) }
            .subscribe(
                { result -> onCreateSavingsGoalSuccess(result) },
                { error -> onCreateSavingsGoalError(error) }
            )
    }

    fun transfer() {
        transferSubscription = Observable.fromCallable { }
            .concatMap {
                accountApi.putSavingsGoalTransfer(
                    AppDataManager.getBearerToken(),
                    accountUid!!,
                    savingsGoalUid!!,
                    savingsGoalUid!!,
                    SavingsGoalTransferBody(Amount(SAVING_GOAL_CURRENCY, transferAmount))
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setIsLoading(true) }
            .doOnTerminate { setIsLoading(false) }
            .subscribe(
                { result -> onTransferSuccess(result) },
                { error -> onTransferError(error) }
            )
    }

    private fun onLoadAccountsError(error: Throwable) {
        Log.e(RESULT, error.toString())
    }

    private fun onLoadAccountsSuccess(accountsList: List<Account>) {
        Log.i(RESULT, accountsList.toString())
        accountUid = accountsList[0].accountUid
        loadTransactionFeed(accountsList[0].accountUid, accountsList[0].defaultCategory)

        val savingsGoalBody = SavingsGoalBody(
            SAVING_GOAL_NAME,
            SAVING_GOAL_CURRENCY,
            Amount(SAVING_GOAL_CURRENCY, SAVING_GOAL_TARGET_MINOR_UNITS),
            SAVING_GOAL_BASE64_ENCODED
        )
        createSavingsGoal(accountsList[0].accountUid, savingsGoalBody)
    }

    private fun onLoadTransactionFeedError(error: Throwable) {
        Log.e(RESULT, error.toString())
    }

    private fun onLoadTransactionFeedSuccess(transactionFeed: TransactionFeed) {
        Log.i(RESULT, transactionFeed.toString())
        transactionFeedAdapter.updateTransactionFeed(transactionFeed)
        transferAmount = calculateTransferAmount(transactionFeed)
        updateTransferString()
    }

    private fun onCreateSavingsGoalError(error: Throwable) {
        Log.e(RESULT, error.toString())
    }

    private fun onCreateSavingsGoalSuccess(savingsGoalResponse: SavingsGoalResponse) {
        Log.i(RESULT, savingsGoalResponse.toString())
        savingsGoalUid = savingsGoalResponse.savingsGoalUid
    }

    private fun onTransferError(error: Throwable) {
        Log.e(RESULT, error.toString())
    }

    private fun onTransferSuccess(savingsGoalTransferResponse: SavingsGoalTransferResponse) {
        Log.i(RESULT, savingsGoalTransferResponse.toString())
        loadAccounts()
    }

    private fun calculateTransferAmount(transactionFeed: TransactionFeed): Int {
        var total = 0
        for (item in transactionFeed.feedItems) {
            if (item.direction == "OUT") total = total + ((item.amount.minorUnits + 99) / 100) * 100 - item.amount.minorUnits
        }
        return total
    }

    private fun updateTransferString() {
        if (transferAmount == 0) {
            transferString.value = "There is nothing to transfer at the moment"
        } else {
            transferString.value = "Round your outgoing transactions and add £${round(
                transferAmount.toFloat() / 100,
                2
            )} to your savings!"
        }
    }
}
