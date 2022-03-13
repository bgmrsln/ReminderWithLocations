package com.codemave.mobilecomputing.data.repository

import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.LoginInfo
import com.codemave.mobilecomputing.data.entity.Task
import com.codemave.mobilecomputing.data.room.CategoryDao
import com.codemave.mobilecomputing.data.room.LoginInfoDao
import kotlinx.coroutines.flow.Flow

class LoginInfoRepository(
    private val loginInfoDao: LoginInfoDao
) {
    fun loginInfos(): Flow<List<LoginInfo>> = loginInfoDao.logininfos()
    suspend fun getPasswordWithUsername(username: String): LoginInfo? = loginInfoDao.getPasswordWithUsername(username)

    /**
     * Add a loginInfo to the database if it does not exist
     *
     * @return the username of the newly added/created loginInfo
     */
    /*
    suspend fun addLoginInfo(loginInfo: LoginInfo): Long {
        return when (val local = loginInfoDao.getPasswordWithUsername(loginInfo.username)) {
            null -> loginInfoDao.insert(loginInfo)
            else -> -1
        }
    }
    */

    suspend fun addLoginInfo(loginInfo: LoginInfo) = loginInfoDao.insert(loginInfo)
    suspend fun updateLogin(loginInfo: LoginInfo) = loginInfoDao.update(loginInfo)
}