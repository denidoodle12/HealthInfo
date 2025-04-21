package com.expert.healthinfo.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "headlines")
data class HeadlinesEntity(
    @PrimaryKey
    @ColumnInfo("id")
    var idHeadlines: String,

    @ColumnInfo("author")
    var author: String?,

    @ColumnInfo("urlToImage")
    var urlToImage: String?,

    @ColumnInfo("description")
    var description: String?,

    @ColumnInfo("title")
    var title: String?,

    @ColumnInfo("isFavorite")
    var isFavorite: Boolean?
)