package com.seanutf.android.bookstore.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
/**
 * @Author zhangyi
 * @Date 2024/3/17 09:54
 * @Desc TODO
 */
@Dao
interface BookDao {
    @Insert
    suspend fun insert(book: Book)

    @Update
    suspend fun update(book: Book)

    @Delete
    suspend fun delete(book: Book)

    @Query("SELECT * from books WHERE id = :id")
    fun getBook(id: Int): Flow<Book>

    @Query("SELECT * from books ORDER BY id ASC")
    fun getAllBooks(): Flow<List<Book>>
}