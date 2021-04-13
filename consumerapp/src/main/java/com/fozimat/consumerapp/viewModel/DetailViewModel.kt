package com.fozimat.consumerapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fozimat.consumerapp.BuildConfig
import com.fozimat.consumerapp.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    val detailUser = MutableLiveData<User>()

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }

    fun setDetailUser(user: String) {
        val url = "https://api.github.com/users/$user"
        val client = AsyncHttpClient()
        val apiKey = BuildConfig.API_KEY
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        client.get(url, object: AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    val result = String(responseBody!!)
                    Log.d(TAG, result)
                    val respondObject = JSONObject(result)
                    val user = User().apply {
                        name = respondObject.getString("name")
                        login = respondObject.getString("login")
                        location = respondObject.getString("location")
                        company = respondObject.getString("company")
                        repository = respondObject.getInt("public_repos")
                        followers = respondObject.getInt("followers")
                        following = respondObject.getInt("following")
                        avatar = respondObject.getString("avatar_url")
                    }
                    detailUser.postValue(user)
                } catch(e: Exception) {
                    Log.d("Exception: ", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    fun getDetailUser() : LiveData<User> {
        return detailUser
    }
}