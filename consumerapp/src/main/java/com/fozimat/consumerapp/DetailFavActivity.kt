package com.fozimat.consumerapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fozimat.consumerapp.adapter.SectionsPagerAdapter
import com.fozimat.consumerapp.databinding.ActivityDetailBinding
import com.fozimat.consumerapp.model.User
import com.google.android.material.tabs.TabLayoutMediator

class DetailFavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_USERNAME_FAV = "extra_username_fav"
        const val EXTRA_POSITION = "extra_position"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitleFav()
        setFav()
        setViewPager()
    }

    private fun setViewPager() {
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME_FAV) as User
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = user.name.toString()
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setTitleFav() {
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME_FAV) as User
        user.name?.let {
            supportActionBar?.title = it
        }
    }

    private fun setFav() {
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME_FAV) as User
        binding.apply {
            Glide.with(this@DetailFavActivity)
                .load(user.avatar)
                .apply(RequestOptions())
                .into(imgAvatar)
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()
            tvName.text = user.login
            tvLocation.text = user.location
            tvCompany.text = user.company
            tvRepository.text = user.repository.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val act = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(act)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}