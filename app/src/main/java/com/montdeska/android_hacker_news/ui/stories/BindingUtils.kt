package com.montdeska.android_hacker_news.ui.stories

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.montdeska.android_hacker_news.data.local.models.Story
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.math.floor

@BindingAdapter("storyTitle")
fun TextView.setStoryTitleHit(item: Story?) {
    item?.let {
        text = item.story_title ?: item.title
    }
}

@BindingAdapter("storyAuthor")
fun TextView.setStoryAuthorHit(item: Story?) {
    item?.let {
        text = item.author.trim()
    }
}

@BindingAdapter("storyCreatedAt")
fun TextView.setStoryCreatedAtHit(item: Story?) {
    item?.let {
        text = timeToNow(item.created_at)
    }
}

@SuppressLint("SimpleDateFormat")
fun timeToNow(dateToCompare: String): String {
    val createdAtMillis = prepareOffsetUTCForStoryCreation(dateToCompare)
    val nowInMillis = prepareOffsetUTCForLocale()
    return getDiffTime(nowInMillis, createdAtMillis)
}

private fun getDiffTime(nowInMillis: Long, createdAtMillis: Long): String {
    var seconds: Double = (nowInMillis - createdAtMillis) / 1000.0
    val hours = floor(seconds / 3600).toInt()
    seconds %= 3600
    val minutes = floor(seconds / 60).toInt()
    return if (hours in 1..23) "${hours}h" else if (hours > 24) "Yesterday" else if (minutes <= 0) "Just now" else "${minutes}m"
}

private fun prepareOffsetUTCForStoryCreation(dateToCompare: String): Long {
    val offsetTime = OffsetDateTime.parse(dateToCompare)
    return offsetTime.toInstant().toEpochMilli()
}

private fun prepareOffsetUTCForLocale(): Long {
    val localDateTime = OffsetDateTime.now(ZoneOffset.UTC).toString()
    val offsetTime = OffsetDateTime.parse(localDateTime)
    return offsetTime.toInstant().toEpochMilli()
}

