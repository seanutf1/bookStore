package com.seanutf.android.bookstore.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seanutf.android.bookstore.BookStoreTopAppBar
import com.seanutf.android.bookstore.R
import com.seanutf.android.bookstore.ui.AppViewModelProvider
import com.seanutf.android.bookstore.ui.navigation.NavigationDestination
import com.seanutf.android.bookstore.ui.theme.BookStoreApplicationTheme
import com.seanutf.android.bookstore.ui.utils.isMatchAuthorNameRequire
import com.seanutf.android.bookstore.ui.utils.isMatchISBNRequire
import com.seanutf.android.bookstore.ui.utils.isMatchTitleRequire
import kotlinx.coroutines.launch

object BookEntryDestination : NavigationDestination {
    override val route = "book_entry"
    override val titleRes = R.string.book_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: BookEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            BookStoreTopAppBar(
                title = stringResource(BookEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        BookEntryBody(
            bookUiState = viewModel.bookUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveBook()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun BookEntryBody(
    bookUiState: BookUiState,
    onItemValueChange: (BookDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        BookInputForm(
            bookDetails = bookUiState.bookDetails,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = bookUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

val options = 2000..2024
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookInputForm(
    bookDetails: BookDetails,
    modifier: Modifier = Modifier,
    onValueChange: (BookDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = bookDetails.title,
            onValueChange = {
                val isTitleValid = isMatchTitleRequire(it)
                onValueChange(bookDetails.copy(title = it, isTitleValid = isTitleValid))
            },
            label = { Text(stringResource(R.string.book_title_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            isError = !bookDetails.isTitleValid,
            singleLine = true
        )
        if (!bookDetails.isTitleValid) {
            Text(
                text = "*the title only support Chinese and English",
                style = TextStyle(fontSize = 16.sp),
                color = Color.Red,
            )
        }
        OutlinedTextField(
            value = bookDetails.author,
            onValueChange = {
                val isAuthorValid = isMatchAuthorNameRequire(it)
                onValueChange(bookDetails.copy(author = it, isAuthorValid = isAuthorValid))
            },
            label = { Text(stringResource(R.string.book_author_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            isError = !bookDetails.isAuthorValid,
            singleLine = true
        )
        if (!bookDetails.isAuthorValid) {
            Text(
                text = "*the author name only support Chinese and English",
                style = TextStyle(fontSize = 16.sp),
                color = Color.Red,
            )
        }
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableIntStateOf(options.last) }
        val showYearText = if (bookDetails.publishYear == 0) {
            ""
        } else {
            bookDetails.publishYear.toString()
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            OutlinedTextField(
                readOnly = true,
                value = showYearText,
                onValueChange = {},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(stringResource(R.string.book_publish_req)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true,
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOptionText = selectionOption
                            onValueChange(bookDetails.copy(publishYear = selectionOption, isPublishYearValid = true))
                            expanded = false
                        }
                    ) {
                        Text(text = selectionOption.toString())
                    }
                }
            }
        }
        OutlinedTextField(
            value = bookDetails.ISBN,
            onValueChange = {
                val isISBNValid = isMatchISBNRequire(it)
                onValueChange(bookDetails.copy(ISBN = it, isISBNValid = isISBNValid))
            },
            label = { Text(stringResource(R.string.book_isbn_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            isError = !bookDetails.isISBNValid,
            singleLine = true
        )
        if (!bookDetails.isISBNValid) {
            Text(
                text = "*the ISBN only support number and -",
                style = TextStyle(fontSize = 16.sp),
                color = Color.Red,
            )
        }
        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookEntryScreenPreview() {
    BookStoreApplicationTheme {
        BookEntryBody(bookUiState = BookUiState(
            BookDetails(
                id = 1,
                title = "Men Game",
                isTitleValid = true,
                author = "Sean Eil",
                isAuthorValid = true,
                publishYear = 2024,
                isPublishYearValid = true,
                ISBN = "978-1-104-56434-9",
                isISBNValid = true
            ),
        ), onItemValueChange = {}, onSaveClick = {})
    }
}
