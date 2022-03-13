package com.codemave.mobilecomputing.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemave.mobilecomputing.Graph
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.LoginInfo
import com.codemave.mobilecomputing.data.entity.Task
import com.codemave.mobilecomputing.data.repository.CategoryRepository
import com.codemave.mobilecomputing.data.repository.LoginInfoRepository
import com.codemave.mobilecomputing.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditViewModel(
    private val loginInfoRepository: LoginInfoRepository = Graph.loginInfoRepository,
    //private val categoryRepository: CategoryRepository = Graph.categoryRepository
): ViewModel() {
    suspend fun updateLogin(loginInfo: LoginInfo){
        return this.loginInfoRepository.updateLogin(loginInfo)
    }
    suspend fun getPasswordWithUsername(username: String): LoginInfo?{
        return loginInfoRepository.getPasswordWithUsername(username)
    }


}

