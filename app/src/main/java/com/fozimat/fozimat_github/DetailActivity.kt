package com.fozimat.fozimat_github

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fozimat.fozimat_github.adapter.SectionsPagerAdapter
import com.fozimat.fozimat_github.databinding.ActivityDetailBinding
import com.fozimat.fozimat_github.db.DatabaseContract
import com.fozimat.fozimat_github.db.DatabaseContract.NoteColumns.Companion.AVATAR
import com.fozimat.fozimat_github.db.DatabaseContract.NoteColumns.Companion.COMPANY
import com.fozimat.fozimat_github.db.DatabaseContract.NoteColumns.Companion.FAVORITE
import com.fozimat.fozimat_github.db.DatabaseContract.NoteColumns.Companion.FOLLOWERS
import com.fozimat.fozimat_github.db.DatabaseContract.NoteColumns.Companion.FOLLOWING
import com.fozimat.fozimat_github.db.DatabaseContract.NoteColumns.Companion.LOCATION
import com.fozimat.fozimat_github.db.DatabaseContract.NoteColumns.Companion.LOGIN
import com.fozimat.fozimat_github.db.DatabaseContract.NoteColumns.Companion.NAME
import com.fozimat.fozimat_github.db.DatabaseContract.NoteColumns.Companion.REPOSITORY
import com.fozimat.fozimat_github.db.UserHelper
import com.fozimat.fozimat_github.helper.MappingHelper
import com.fozimat.fozimat_github.model.User
import com.fozimat.fozimat_github.viewModel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var userHelper: UserHelper
    private var isFavorite: Boolean = false
    private lateinit var uriWithId: Uri

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

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        setTitleFav()

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

        binding.btnFav.setOnClickListener(this)
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME) as User
        val cursor: Cursor = userHelper.queryById(user.login.toString())
        if (cursor.moveToNext()){
            isFavorite = true
            setStatusFavorite(isFavorite)
        }
        checkFav(cursor)
        setViewPager()
    }

    private fun setViewPager() {
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME) as User
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

    private fun setTitleFav() {
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME) as User
        user.login?.let {
            supportActionBar?.title= it
            detailViewModel.setDetailUser(it)
        }
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

    private fun setStatusFavorite(statFav: Boolean) {
        if(statFav) {
            binding.btnFav.setImageResource(R.drawable.ic_favorited)
        } else {
            binding.btnFav.setImageResource(R.drawable.ic_favorite)
        }
    }

    private fun checkFav(cursor: Cursor) {
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME) as User
        val fav = MappingHelper.mapCursorToArrayList(cursor)
        for(data in fav){
            if(user.login == data.login){
                isFavorite= true
            }
        }
    }

    private fun insertData() {
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME) as User
        val name = binding.tvName.text.toString()
        val login = supportActionBar?.title.toString()
        val company = binding.tvCompany.text.toString()
        val location = binding.tvLocation.text.toString()
        val followers = binding.tvFollowers.text.toString()
        val following = binding.tvFollowing.text.toString()
        val repository = binding.tvRepository.text.toString()
        val avatar = user.avatar
        val favorite = "true"

        val values = ContentValues()

        values.put(NAME, name)
        values.put(LOGIN, login)
        values.put(COMPANY, company)
        values.put(LOCATION, location)
        values.put(FOLLOWERS, followers)
        values.put(FOLLOWING, following)
        values.put(REPOSITORY, repository)
        values.put(AVATAR, avatar)
        values.put(FAVORITE, favorite)

        contentResolver.insert(DatabaseContract.NoteColumns.CONTENT_URI, values)

        val message = resources.getString(R.string.add_success)
        showSnackbarMessage(message)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_fav -> {
                if(!isFavorite) {
                    isFavorite = !isFavorite
                    setStatusFavorite(isFavorite)
                    insertData()
                } else {
                    isFavorite = !isFavorite
                    setStatusFavorite(isFavorite)
                    deleteData()
                }
            }
        }
    }

    private fun deleteData() {
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME) as User
        uriWithId = Uri.parse("${DatabaseContract.NoteColumns.CONTENT_URI}/${user.login}")
        contentResolver.delete(uriWithId, null, null)
        showSnackbarMessage("Data success deleted")
    }


}