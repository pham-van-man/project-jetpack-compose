package com.example.learneng.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learneng.db.DatabaseBuilder
import com.example.learneng.model.Group
import com.example.learneng.model.GroupWithWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(databaseBuilder: DatabaseBuilder) : ViewModel() {
    private val groupDao = databaseBuilder.getDatabase().groupDao()
    private val _groups = MutableLiveData<List<GroupWithWord>>()
    val groups: LiveData<List<GroupWithWord>> = _groups
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading
    suspend fun add(group: Group): Group {
        try {
            _errorMessage.value = null
            _isLoading.value = true
            val insertedId = groupDao.insert(group)
            val newGroup = group.copy(id = insertedId)
            val groups = groupDao.findGroups()
            _groups.value = groups
            return newGroup
        } catch (e: Exception) {
            _errorMessage.value = e.message
            return group
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun delete(group: Group): Group {
        try {
            _errorMessage.value = null
            _isLoading.value = true
            groupDao.delete(group)
            _groups.value = _groups.value?.filter { it.group.id != group.id }
            return group
        } catch (e: Exception) {
            _errorMessage.value = e.message
            return group
        } finally {
            _isLoading.value = false
        }
    }

    fun getGroups(q: String = "") {
        _groups.value = emptyList()
        _errorMessage.value = null
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val groups = groupDao.findGroups("%" + q.trim() + "%")
                _groups.value = groups
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun update(group: Group): Group? {
        val oldGroup = groupDao.findGroupById(group.id)
        try {
            _errorMessage.value = null
            _isLoading.value = true
            groupDao.update(group)
            val groups = groupDao.findGroups()
            _groups.value = groups
            return oldGroup
        } catch (e: Exception) {
            _errorMessage.value = e.message
            return oldGroup
        } finally {
            _isLoading.value = false
        }
    }
}