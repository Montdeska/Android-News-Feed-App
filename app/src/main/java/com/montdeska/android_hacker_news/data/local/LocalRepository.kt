package com.montdeska.android_hacker_news.data.local

import com.montdeska.android_hacker_news.data.local.models.Story

class LocalRepository(private val storiesDao: StoriesDao) {
    suspend fun allStories(): List<Story> = storiesDao.getStories()

    suspend fun insert(stories: List<Story>) {
        storiesDao.insertStories(stories)
    }

    suspend fun update(story: Story) {
        storiesDao.updateStory(story)
    }

    suspend fun delete() {
        storiesDao.deleteStories()
    }
}