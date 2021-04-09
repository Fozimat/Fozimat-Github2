package com.fozimat.fozimat_github

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fozimat.fozimat_github.adapter.UserAdapter
import com.fozimat.fozimat_github.databinding.ActivityMainBinding
import com.fozimat.fozimat_github.db.UserHelper
import com.fozimat.fozimat_github.model.User
import com.fozimat.fozimat_github.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUser.setHasFixedSize(true)

        showRecyclerView()

        val userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.btnSearch.setOnClickListener {
            hideKeyboard()

            val user = binding.edtSeachUser.text.toString()
            if (user.isEmpty()) return@setOnClickListener

            showLoading(true)

            mainViewModel.setUser(user)
        }

        mainViewModel.getUsers().observe(this, { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerView() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> {
                val act = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(act)
            }
            R.id.action_fav -> {
                val fav = Intent(this, FavoriteActivity::class.java)
                startActivity(fav)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSelectedUser(user: User) {
        val move = Intent(this, DetailActivity::class.java)
        move.putExtra(DetailActivity.EXTRA_USERNAME, user)
        startActivity(move)
    }

    private fun hideKeyboard() {
        val hide = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        hide?.hideSoftInputFromWindow(binding.btnSearch.windowToken, 0)
    }
}