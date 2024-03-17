package com.seanutf.android.bookstore.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanutf.android.bookstore.data.BooksRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BookEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val booksRepository: BooksRepository,
) : ViewModel() {

    var bookUiState by mutableStateOf(BookUiState())
        private set
    private val bookId: Int = checkNotNull(savedStateHandle[BookEditDestination.itemIdArg])

    init {
        viewModelScope.launch {
            bookUiState = booksRepository.getBookStream(bookId)
                .filterNotNull()
                .first()
                .toBookUiState(true)
        }
    }

    fun updateUiState(bookDetails: BookDetails) {
        bookUiState =
            BookUiState(bookDetails = bookDetails, isEntryValid = validateInput(bookDetails))
    }

    suspend fun updateBook() {
        if (validateInput(bookUiState.bookDetails)) {
            booksRepository.updateBook(bookUiState.bookDetails.toBook())
        }
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
}
