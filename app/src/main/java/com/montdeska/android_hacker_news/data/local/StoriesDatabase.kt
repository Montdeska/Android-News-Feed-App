package com.montdeska.android_hacker_news.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.montdeska.android_hacker_news.data.local.models.Story

@Database(entities = [Story::class], version = 1, exportSchema = false)
public abstract class StoriesDatabase : RoomDatabase() {

    abstract fun storiesDao(): StoriesDao

    companion object {
        @Volatile
        private var INSTANCE: StoriesDatabase? = null
        fun getDatabase(context: Context): StoriesDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StoriesDatabase::class.java,
                    "stories_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}