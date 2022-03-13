package com.codemave.mobilecomputing.ui.login

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

class LoginViewModel(
    private val loginInfoRepository: LoginInfoRepository = Graph.loginInfoRepository,
    //private val categoryRepository: CategoryRepository = Graph.categoryRepository
): ViewModel() {
    //private val _state = MutableStateFlow(LoginInfoViewState())
    //val state: StateFlow<TaskViewState>
      //  get() = _state

    suspend fun checkUsername(loginInfo: LoginInfo): Boolean {
        if(loginInfoRepository.getPasswordWithUsername(username= loginInfo.username) != null){
            val loginInfo2:LoginInfo = loginInfoRepository.getPasswordWithUsername(username= loginInfo.username)!!
            return loginInfo2.password== loginInfo.password
        }else{
            return false
        }

    }


}

