package com.seanutf.android.bookstore.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.seanutf.android.bookstore.BookStoreApplication
import com.seanutf.android.bookstore.ui.home.HomeViewModel
import com.seanutf.android.bookstore.ui.item.BookDetailsViewModel
import com.seanutf.android.bookstore.ui.item.BookEditViewModel
import com.seanutf.android.bookstore.ui.item.BookEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            BookEditViewModel(
                this.createSavedStateHandle(),
                bookStoreApplication().container.booksRepository
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            BookEntryViewModel(bookStoreApplication().container.booksRepository)
        }

        // Initializer for ItemDetailsViewModel
        initializer {
            BookDetailsViewModel(
                this.createSavedStateHandle(),
                bookStoreApplication().container.booksRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(bookStoreApplication().container.booksRepository)
        }
    }
}

fun CreationExtras.bookStoreApplication(): BookStoreApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as BookStoreApplication)
