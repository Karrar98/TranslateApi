package com.example.translateapi.network

import com.example.translateapi.model.Languages
import com.example.translateapi.model.ResultTranslated
import com.example.translateapi.utils.Constant
import com.example.translateapi.utils.Status
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

object Client {

    val client = OkHttpClient()
    val gson = Gson()


    fun BaseUrl(pathSegment: String, queryMap: Map<String, String>?) =
        HttpUrl.Builder().scheme(Constant.Url.SCHEME).
        host(Constant.Url.HOST).addPathSegment(pathSegment).apply {
        if (pathSegment == Constant.TRANSLATE) {
            addQueryParameter(Constant.Translate.QUERY, queryMap?.get(Constant.Translate.QUERY))
            addQueryParameter(Constant.Translate.SOURCE, queryMap?.get(Constant.Translate.SOURCE))
            addQueryParameter(Constant.Translate.TARGET, queryMap?.get(Constant.Translate.TARGET))
        }
    }.toString()


    inline fun <reified T> initRequest(typeRequest: String, queryMap: Map<String, String>?): Status<T>{
        val postBody = FormBody.Builder().build()
        val request = Request.Builder().url(BaseUrl(typeRequest, queryMap)).apply {
            if (typeRequest == Constant.TRANSLATE) {
                post(postBody)
            }
        }.build()
        val response = client.newCall(request).execute()

        return if (response.isSuccessful) {
            val parserResponse = gson.fromJson(
                response.body?.string(),
                T::class.java
            )
            Status.Success(parserResponse)
        } else {
            Status.Error(response.message)
        }
    }
}