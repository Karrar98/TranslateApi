package com.example.translateapi.repositry

import com.example.translateapi.model.Languages
import com.example.translateapi.network.Client
import com.example.translateapi.utils.Constant
import com.example.translateapi.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object LanguageRepository {

    fun getLanguage() = flow <Status<Languages>>{
        emit(Status.Loading)
        emit(Client.initRequest(queryMap = null, typeRequest = Constant.LANGUAGE))
    }.flowOn(Dispatchers.IO)
}