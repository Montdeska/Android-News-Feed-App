package com.montdeska.android_hacker_news.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story")
data class Story(
    @PrimaryKey @ColumnInfo(name = "story_id") val story_id: Long,
    @ColumnInfo(name = "story_title")
    val story_title: String? = "",
    @ColumnInfo(name = "title")
    val title: String? = "",
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "created_at")
    val created_at: String,
    @ColumnInfo(name = "story_url")
    val story_url: String? = "",
    @ColumnInfo(name = "local_deleted")
    var local_deleted: Boolean? = false
)