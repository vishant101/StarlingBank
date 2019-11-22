package com.example.starlingbank.views.main

import android.util.Log
import com.example.starlingbank.data.database.AccountDao
import com.example.starlingbank.data.manager.AppDataManager
import com.example.starlingbank.data.model.Account
import com.example.starlingbank.network.AccountApi
import com.example.starlingbank.utils.RESULT
import com.example.starlingbank.views.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainViewModel(private val accountDao: AccountDao): BaseViewModel() {
    @Inject
    lateinit var accountApi: AccountApi
    private var subscription: Disposable? = null

    init {
        loadAccounts()
    }

    private fun loadAccounts(){
        subscription = Observable.fromCallable{ accountDao.all }
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
                { result -> onRetrieveAccountsSuccess(result) },
                { error -> onLoadError(error) }
            )
    }

    private fun onLoadError(error: Throwable) {
        Log.e(RESULT, error.toString())
    }

    private fun onRetrieveAccountsSuccess(accountsList: List<Account>) {
        Log.i(RESULT, accountsList.toString())
    }
}