package com.codemave.mobilecomputing.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.LoginInfo
import kotlinx.coroutines.flow.Flow

@Dao
abstract class LoginInfoDao {

    @Query(value = "SELECT * FROM logininfos WHERE username = :name")
    abstract suspend fun getPasswordWithUsername(name: String): LoginInfo?

    @Query("SELECT * FROM logininfos")
    abstract fun logininfos(): Flow<List<LoginInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: LoginInfo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(entities: Collection<LoginInfo>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: LoginInfo)

    @Delete
    abstract suspend fun delete(entity: LoginInfo): Int
}