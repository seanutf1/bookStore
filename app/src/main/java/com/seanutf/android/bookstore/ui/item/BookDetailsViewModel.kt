package com.seanutf.android.bookstore.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanutf.android.bookstore.data.BooksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val booksRepository: BooksRepository
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])
    val uiState: StateFlow<BookDetailsUiState> =
        booksRepository.getBookStream(itemId)
            .filterNotNull()
            .map {
                BookDetailsUiState(
                    bookDetails = it.toBookDetails(),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = BookDetailsUiState()
            )

    fun reduceQuantityByOne() {
        viewModelScope.launch {
            val currentItem = uiState.value.bookDetails.toBook()
            booksRepository.updateBook(currentItem.copy())
        }
    }

    suspend fun deleteBook() {
        booksRepository.deleteBook(uiState.value.bookDetails.toBook())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class BookDetailsUiState(
    val bookDetails: BookDetails = BookDetails()
)
