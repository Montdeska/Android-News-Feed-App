package com.montdeska.android_hacker_news.ui.stories

import android.app.Application
import androidx.lifecycle.*
import com.montdeska.android_hacker_news.data.api.RetrofitBuilder
import com.montdeska.android_hacker_news.data.local.StoriesDatabase
import com.montdeska.android_hacker_news.data.local.models.Story
import com.montdeska.android_hacker_news.data.local.LocalRepository
import kotlinx.coroutines.launch

class StoriesViewModel(application: Application) : AndroidViewModel(application) {
    private var headers = HashMap<String, String>()
    private val _stories = MutableLiveData<List<Story>>()
    private val _offlineModeOn = MutableLiveData<Boolean>()

    val stories: LiveData<List<Story>> get() = _stories
    val offlineModeOn: LiveData<Boolean> get() = _offlineModeOn
    private var repository: LocalRepository

    init {
        val storiesDao = StoriesDatabase.getDatabase(application).storiesDao()
        repository = LocalRepository(storiesDao)
    }

    fun getStories(hasConnectivity: Boolean) {
        viewModelScope.launch {
            if (!hasConnectivity) {
                _offlineModeOn.value = true
                filterDeletedBeforePresent(repository.allStories())
            } else {
                _offlineModeOn.value = false
                try {
                    repository.insert(RetrofitBuilder.apiService.getStories(headers)!!.hits as List<Story>)
                } catch (e: Exception) {
                    e.printStackTrace()
                    _offlineModeOn.value = true
                } finally {
                    filterDeletedBeforePresent(repository.allStories())
                }
            }
        }
    }

    private fun filterDeletedBeforePresent(localStories: List<Story>) {
        val storiesToShow = localStories.filter { story ->
            story.local_deleted == null || story.local_deleted == false
        }
        _stories.value = storiesToShow
    }

    fun updateStory(story: Story, isDeleted: Boolean) {
        viewModelScope.launch {
            story.local_deleted = isDeleted
            repository.update(story)
        }
    }

}