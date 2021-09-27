package com.example.translateapi.repositry

import com.example.translateapi.network.Client
import com.example.translateapi.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object LanguageRepository {

    fun getLanguage() = flow {
        emit(Status.Loading)
        emit(Client.initRequestLanguage())
    }.flowOn(Dispatchers.IO)
}