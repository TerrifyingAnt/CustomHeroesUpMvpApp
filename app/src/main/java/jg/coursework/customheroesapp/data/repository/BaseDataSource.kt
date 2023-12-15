package jg.coursework.customheroesapp.data.repository

import jg.coursework.customheroesapp.util.Resource
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.error("Ошибка: $message")
    }

}