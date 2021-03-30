package com.fozimat.fozimat_github.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fozimat.fozimat_github.BuildConfig
import com.fozimat.fozimat_github.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersViewModel : ViewModel() {

    val dataFollowers = MutableLiveData<ArrayList<User>>()

    companion object {
        private val TAG = FollowersViewModel::class.java.simpleName
    }

    fun setFollowers(user: String) {
        val listFollowers = ArrayList<User>()
        var url = "https://api.github.com/users/$user/followers"
        val client = AsyncHttpClient()
        val apiKey = BuildConfig.API_KEY
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    val result = String(responseBody!!)
                    Log.d(TAG, result)
                    val respondArray = JSONArray(result)

                    for(i in 0 until respondArray.length()) {
                        val user = respondArray.getJSONObject(i)
                        val userList = User().apply {
                            login = user.getString("login")
                            avatar = user.getString("avatar_url")
                        }
                        listFollowers.add(userList)
                    }
                    dataFollowers.postValue(listFollowers)
                } catch (e: Exception) {
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
                Log.d(TAG, error?.message.toString())
            }

        })
    }

    fun getFollowers() : LiveData<ArrayList<User>> {
        return dataFollowers
    }
}