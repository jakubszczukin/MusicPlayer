package com.example.musicplayer.radio

import RadioRepository
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RadioViewModel(private val repository: RadioRepository) : ViewModel() {
    // Using LiveData and caching what radioList returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val radioList: LiveData<List<Radio>> = repository.radioList.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     * Use dispacher to run code outside of the main thread.
     * Dispachers.IO -> optimized to perform disk/network I/O, ex. Room
     */
    fun insert(radio: Radio) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(radio)
    }

    fun delete(radio: Radio) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(radio)
    }

    fun deleteById(id: Long) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteById(id)
    }

    fun getFirst() = viewModelScope.launch(Dispatchers.IO){
        repository.getFirst()
    }

    /**
     Create a ViewModel using ViewModelProvider Factory for magic stuff
     Factory is needed to create ViewModel with arguments that are needed by instance
    */
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