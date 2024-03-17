package com.seanutf.android.bookstore.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.seanutf.android.bookstore.data.Book
import com.seanutf.android.bookstore.data.BooksRepository

class BookEntryViewModel(private val booksRepository: BooksRepository) : ViewModel() {

    var bookUiState by mutableStateOf(BookUiState())
        private set

    fun updateUiState(bookDetails: BookDetails) {
        bookUiState =
            BookUiState(bookDetails = bookDetails, isEntryValid = validateInput(bookDetails))
    }

    private fun validateInput(uiState: BookDetails = bookUiState.bookDetails): Boolean {
        return with(uiState) {
            title.isNotBlank()
                    && isTitleValid
                    && author.isNotBlank()
                    && isAuthorValid
                    && publishYear > 0
                    && isPublishYearValid
                    && ISBN.isNotBlank()
                    && isISBNValid
        }
    }

    suspend fun saveBook() {
        if (validateInput()) {
            booksRepository.insertBook(bookUiState.bookDetails.toBook())
        }
    }
}

data class BookUiState(
    val bookDetails: BookDetails = BookDetails(),
    val isEntryValid: Boolean = false
)

data class BookDetails(
    val id: Int = 0,
    val title: String = "",
    val isTitleValid: Boolean = true,
    val author: String = "",
    val isAuthorValid: Boolean = true,
    val publishYear: Int = 0,
    val isPublishYearValid: Boolean = true,
    val ISBN: String = "",
    val isISBNValid: Boolean = true,
)

fun BookDetails.toBook(): Book = Book(
    id = id,
    title = title,
    author = author,
    publishYear = publishYear,
    ISBN = ISBN,
)

fun Book.toBookUiState(isEntryValid: Boolean = false): BookUiState = BookUiState(
    bookDetails = this.toBookDetails(),
    isEntryValid = isEntryValid
)

fun Book.toBookDetails(): BookDetails = BookDetails(
    id = id,
    title = title,
    author = author,
    publishYear = publishYear,
    ISBN = ISBN,
)
