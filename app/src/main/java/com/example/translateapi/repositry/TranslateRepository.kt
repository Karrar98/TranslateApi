package com.example.translateapi.repositry

import com.example.translateapi.model.ResultTranslated
import com.example.translateapi.network.Client
import com.example.translateapi.utils.Constant
import com.example.translateapi.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object TranslateRepository {

    fun getTranslateData(queryMap: Map<String, String>) = flow <Status<ResultTranslated>>{
        emit(Status.Loading)
        emit(Client.initRequest(queryMap = queryMap, typeRequest = Constant.TRANSLATE))
    }.flowOn(Dispatchers.IO)


}