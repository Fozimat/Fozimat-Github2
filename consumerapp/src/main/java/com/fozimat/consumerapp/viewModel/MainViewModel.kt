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
import java.lang.Exception

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    fun setUser(users: String) {
        val listItems = ArrayList<User>()
        val url = "https://api.github.com/search/users?q=$users"
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
                    val list = respondObject.getJSONArray("items")

                    for(i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val userList = User().apply {
                            login = user.getString("login")
                            avatar = user.getString("avatar_url")
                        }
                        listItems.add(userList)
                    }
                    listUsers.postValue(listItems)
                    Log.d("list: ", listUsers.postValue(listItems).toString())
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
                Log.d("onFailure: ", error?.message.toString())
            }

        })
    }

    fun getUsers() : LiveData<ArrayList<User>> {
        return listUsers
    }
}