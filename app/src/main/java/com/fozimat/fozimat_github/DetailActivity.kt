package com.fozimat.fozimat_github

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fozimat.fozimat_github.adapter.SectionsPagerAdapter
import com.fozimat.fozimat_github.databinding.ActivityDetailBinding
import com.fozimat.fozimat_github.model.User
import com.fozimat.fozimat_github.viewModel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    companion object {
        const val EXTRA_USERNAME = "extra_username"

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

        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME) as User
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        user.login?.let {
            detailViewModel.setDetailUser(it)
        }

        detailViewModel.getDetailUser().observe(this, {
            binding.apply {
                Glide.with(this@DetailActivity)
                    .load(it.avatar)
                    .apply(RequestOptions())
                    .into(imgAvatar)
                tvFollowers.text = it.followers.toString()
                tvFollowing.text = it.following.toString()
                tvName.text = it.name
                tvLocation.text = it.location
                tvCompany.text = it.company
                tvRepository.text = it.repository.toString()
            }
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = user.login.toString()
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_settings) {
            val act = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(act)
        }
        return super.onOptionsItemSelected(item)
    }

}