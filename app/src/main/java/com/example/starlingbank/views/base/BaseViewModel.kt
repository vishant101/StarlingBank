package com.example.starlingbank.views.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.example.starlingbank.injection.DaggerViewModelInjector
import com.example.starlingbank.injection.ViewModelInjector
import com.example.starlingbank.network.NetworkModule
import com.example.starlingbank.views.main.MainViewModel

abstract class BaseViewModel: ViewModel(){
    private val mIsLoading = ObservableBoolean()

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    fun setIsLoading(isLoading: Boolean) {
        mIsLoading.set(isLoading)
    }

    fun getIsLoading(): ObservableBoolean {
        return mIsLoading
    }

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is MainViewModel -> injector.inject(this)
        }
    }
}