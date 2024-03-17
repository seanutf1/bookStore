package com.seanutf.android.bookstore.ui.item

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seanutf.android.bookstore.BookStoreTopAppBar
import com.seanutf.android.bookstore.R
import com.seanutf.android.bookstore.ui.AppViewModelProvider
import com.seanutf.android.bookstore.ui.navigation.NavigationDestination
import com.seanutf.android.bookstore.ui.theme.BookStoreApplicationTheme
import kotlinx.coroutines.launch

object BookEditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = R.string.edit_book_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            BookStoreTopAppBar(
                title = stringResource(BookEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        BookEntryBody(
            bookUiState = viewModel.bookUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateBook()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookEditScreenPreview() {
    BookStoreApplicationTheme {
        BookEditScreen(navigateBack = { /*Do nothing*/ }, onNavigateUp = { /*Do nothing*/ })
    }
}
