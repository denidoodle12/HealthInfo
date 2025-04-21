package com.expert.healthinfo.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Headlines (
    val idHeadlines: String?,
    val author: String?,
    val urlToImage: String?,
    val description: String?,
    val title: String?,
    var isFavorite: Boolean?
) : Parcelable