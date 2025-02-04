package com.example.learneng.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learneng.db.DatabaseBuilder
import com.example.learneng.model.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(databaseBuilder: DatabaseBuilder) : ViewModel() {
    private val wordDao = databaseBuilder.getDatabase().wordDao()
    private val _words = MutableLiveData<List<Word>>()
    val words: LiveData<List<Word>> = _words
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading
    suspend fun update(word: Word): Word? {
        val oldWord = wordDao.finWordById(word.id)
        try {
            _errorMessage.value = null
            _isLoading.value = true
            wordDao.update(word)
            _words.value = _words.value?.map { if (it.id == word.id) word else it }
            return oldWord
        } catch (e: Exception) {
            _errorMessage.value = e.message
            return oldWord
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun delete(word: Word): Word {
        try {
            _errorMessage.value = null
            _isLoading.value = true
            wordDao.delete(word)
            _words.value = _words.value?.filter { it.id != word.id }
            return word
        } catch (e: Exception) {
            _errorMessage.value = e.message
            return word
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun add(word: Word): Word {
        try {
            _errorMessage.value = null
            _isLoading.value = true
            val insertedId = wordDao.insert(word)
            val newWord = word.copy(id = insertedId)
            val words = wordDao.findWords()
            _words.value = words
            return newWord
        } catch (e: Exception) {
            _errorMessage.value = e.message
            return word
        } finally {
            _isLoading.value = false
        }
    }

    fun getWords(q: String = "") {
        _words.value = emptyList()
        _errorMessage.value = null
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val words = wordDao.findWords("%" + q.trim() + "%")
                _words.value = words
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}