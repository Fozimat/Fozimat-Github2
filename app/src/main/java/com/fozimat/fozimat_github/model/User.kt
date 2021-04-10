package com.fozimat.fozimat_github.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int? = null,
    var name: String? = null,
    var login: String?= null,
    var location: String? = null,
    var repository: Int? = 0,
    var company: String? = null,
    var followers: Int? = 0,
    var following: Int? = 0,
    var avatar: String? = null,
    var favorite: String? = null
) : Parcelable