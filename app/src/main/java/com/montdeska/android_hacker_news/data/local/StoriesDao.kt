package com.montdeska.android_hacker_news.data.local

import androidx.room.*
import com.montdeska.android_hacker_news.data.local.models.Story

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStories(stories: List<Story?>?)

    @Update
    suspend fun updateStory(story: Story): Int

    @Query("DELETE FROM Story")
    suspend fun deleteStories()

    @Query("SELECT * FROM Story ORDER BY created_at DESC")
    suspend fun getStories(): List<Story>
}