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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fozimat.fozimat_github.adapter.SectionsPagerAdapter
import com.fozimat.fozimat_github.databinding.ActivityDetailBinding
import com.fozimat.fozimat_github.db.DatabaseContract
import com.fozimat.fozimat_github.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.fozimat.fozimat_github.db.UserHelper
import com.fozimat.fozimat_github.helper.MappingHelper
import com.fozimat.fozimat_github.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailFavActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var userHelper: UserHelper
    private var isFavorite: Boolean = false
    private lateinit var uriWithId: Uri

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

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        binding.btnFav.setOnClickListener(this)

        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME_FAV) as User

        val cursor: Cursor = userHelper.queryById(user.name.toString())
        if (cursor.moveToNext()){
            isFavorite = true
            setStatusFavorite(isFavorite)
        }
        checkFav(cursor)
        setTitleFav()
        setFav()
        setViewPager()
    }

    private fun checkFav(cursor: Cursor) {
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME_FAV) as User
        val fav = MappingHelper.mapCursorToArrayList(cursor)
        for(data in fav){
            if(user.name == data.login){
                isFavorite= true
            }
        }
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
            supportActionBar?.title= it
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
        when(item.itemId) {
            R.id.action_settings -> {
                val act = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(act)
            }
            R.id.action_fav -> {
                val fav = Intent(this, FavoriteActivity::class.java)
                startActivity(fav)
            }
            R.id.action_remind -> {
                val rem = Intent(this, AlarmActivity::class.java)
                startActivity(rem)
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

    private fun insertData() {
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME_FAV) as User

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

        values.put(DatabaseContract.NoteColumns.NAME, name)
        values.put(DatabaseContract.NoteColumns.LOGIN, login)
        values.put(DatabaseContract.NoteColumns.COMPANY, company)
        values.put(DatabaseContract.NoteColumns.LOCATION, location)
        values.put(DatabaseContract.NoteColumns.FOLLOWERS, followers)
        values.put(DatabaseContract.NoteColumns.FOLLOWING, following)
        values.put(DatabaseContract.NoteColumns.REPOSITORY, repository)
        values.put(DatabaseContract.NoteColumns.AVATAR, avatar)
        values.put(DatabaseContract.NoteColumns.FAVORITE, favorite)

        contentResolver.insert(CONTENT_URI, values)

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
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME_FAV) as User
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user?.name)
        contentResolver.delete(uriWithId, null, null)
        val message = resources.getString(R.string.deleted_success)
        showSnackbarMessage(message)
    }
}