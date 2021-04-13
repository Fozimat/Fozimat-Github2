package com.fozimat.consumerapp.helper

import android.database.Cursor
import com.fozimat.consumerapp.db.DatabaseContract.NoteColumns.Companion.AVATAR
import com.fozimat.consumerapp.db.DatabaseContract.NoteColumns.Companion.COMPANY
import com.fozimat.consumerapp.db.DatabaseContract.NoteColumns.Companion.FAVORITE
import com.fozimat.consumerapp.db.DatabaseContract.NoteColumns.Companion.FOLLOWERS
import com.fozimat.consumerapp.db.DatabaseContract.NoteColumns.Companion.FOLLOWING
import com.fozimat.consumerapp.db.DatabaseContract.NoteColumns.Companion.LOCATION
import com.fozimat.consumerapp.db.DatabaseContract.NoteColumns.Companion.LOGIN
import com.fozimat.consumerapp.db.DatabaseContract.NoteColumns.Companion.NAME
import com.fozimat.consumerapp.db.DatabaseContract.NoteColumns.Companion.REPOSITORY
import com.fozimat.consumerapp.model.User

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val login = getString(getColumnIndexOrThrow(LOGIN))
                val name = getString(getColumnIndexOrThrow(NAME))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val repository = getInt(getColumnIndexOrThrow(REPOSITORY))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                val followers = getInt(getColumnIndexOrThrow(FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(FOLLOWING))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val favorite = getString(getColumnIndexOrThrow(FAVORITE))
                userList.add(
                    User(
                        login,
                        name,
                        location,
                        repository,
                        company,
                        followers,
                        following,
                        avatar,
                        favorite
                    )
                )
            }
        }
        return userList
    }
}