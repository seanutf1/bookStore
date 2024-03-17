package com.seanutf.android.bookstore

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.seanutf.android.bookstore.data.Book
import com.seanutf.android.bookstore.data.BookDao
import com.seanutf.android.bookstore.data.BookDatabase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BookDaoTest {
    private lateinit var bookDao: BookDao
    private lateinit var bookDatabase: BookDatabase
    private var book1 = Book(1, "Book one", "Sean Eil", 2014, "984038-1384-2343")
    private var book2 = Book(2, "Book Two", "Jo Joe", 2003, "36784-344")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        bookDatabase = Room.inMemoryDatabaseBuilder(context, BookDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        bookDao = bookDatabase.bookDao()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsBookIntoDB() = runBlocking {
        addOneBookToDb()
        val allItems = bookDao.getAllBooks().first()
        assertEquals(allItems[0], book1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllBooks_returnsAllBooksFromDB() = runBlocking {
        addTwoBooksToDb()
        val allItems = bookDao.getAllBooks().first()
        assertEquals(allItems[0], book1)
        assertEquals(allItems[1], book2)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateBooks_updatesBooksInDB() = runBlocking {
        addTwoBooksToDb()
        bookDao.update(Book(1, "Book one", "Sean Eil", 2015, "984038-1384-2343"))
        bookDao.update(Book(2, "Book Two", "Jo Joe", 2013, "36784-344"))
        val allItems = bookDao.getAllBooks().first()
        assertEquals(allItems[0], Book(1, "Book one", "Sean Eil", 2015, "984038-1384-2343"))
        assertEquals(allItems[1], Book(2, "Book Two", "Jo Joe", 2013, "36784-344"))
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllItemsFromDB() = runBlocking {
        addTwoBooksToDb()
        bookDao.delete(book1)
        bookDao.delete(book2)
        val allItems = bookDao.getAllBooks().first()
        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDB() = runBlocking {
        addOneBookToDb()
        val book = bookDao.getBook(1)
        assertEquals(book.first(), book1)
    }

    private suspend fun addOneBookToDb() {
        bookDao.insert(book1)
    }

    private suspend fun addTwoBooksToDb() {
        bookDao.insert(book1)
        bookDao.insert(book2)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        bookDatabase.close()
    }
}