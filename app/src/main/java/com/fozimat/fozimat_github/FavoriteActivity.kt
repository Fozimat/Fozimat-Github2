package com.fozimat.fozimat_github

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fozimat.fozimat_github.adapter.FavoriteAdapter
import com.fozimat.fozimat_github.databinding.ActivityFavoriteBinding
import com.fozimat.fozimat_github.db.UserHelper
import com.fozimat.fozimat_github.helper.MappingHelper
import com.fozimat.fozimat_github.model.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite"

        showRecycleView()

        if(savedInstanceState == null) {
            loadUserAsync()
        } else {
            val user = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if(user != null) {
                adapter.listUser = user
            }
        }
    }

    private fun showRecycleView() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.rvUser.adapter = adapter
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressbar.visibility = View.VISIBLE
            val noteHelper = UserHelper.getInstance(applicationContext)
            noteHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressbar.visibility = View.INVISIBLE
            val user = deferredNotes.await()
            if (user.size > 0) {
                adapter.listUser = user
            } else {
                adapter.listUser = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
            noteHelper.close()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listUser)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvUser, message, Snackbar.LENGTH_SHORT).show()
    }

}