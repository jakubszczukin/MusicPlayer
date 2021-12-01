package com.example.musicplayer.radio

import RadioRepository
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class RadioViewModel(private val repository: RadioRepository) : ViewModel() {
    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val radioList: LiveData<List<Radio>> = repository.radioList.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(radio: Radio) = viewModelScope.launch {
        repository.insert(radio)
    }

    class RadioViewModelFactory(private val repository: RadioRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(RadioViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return RadioViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}