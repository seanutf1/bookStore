package com.seanutf.android.bookstore.data

import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    fun getAllBooksStream(): Flow<List<Book>>

    fun getBookStream(id: Int): Flow<Book?>

    suspend fun insertBook(book: Book)

    suspend fun deleteBook(book: Book)

    suspend fun updateBook(book: Book)
}
