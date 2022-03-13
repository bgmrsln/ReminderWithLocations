package com.codemave.mobilecomputing.ui.signup

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

class LoginInfoViewModel(
    private val loginInfoRepository: LoginInfoRepository = Graph.loginInfoRepository,
    //private val categoryRepository: CategoryRepository = Graph.categoryRepository
): ViewModel() {
    //private val _state = MutableStateFlow(LoginInfoViewState())



    suspend fun saveLoginInfo(loginInfo: LoginInfo): Long {
        return loginInfoRepository.addLoginInfo(loginInfo)
    }


}

