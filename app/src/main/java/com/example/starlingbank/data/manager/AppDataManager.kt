package com.example.starlingbank.data.manager

import com.example.starlingbank.utils.ACCESS_TOKEN

object AppDataManager{
    fun getBearerToken(): String?{
        return "Bearer $ACCESS_TOKEN"
    }
}