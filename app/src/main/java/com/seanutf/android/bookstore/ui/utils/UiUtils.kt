package com.seanutf.android.bookstore.ui.utils

private val BOOK_TITLE_REGEX by lazy {
    Regex("^[\\u4e00-\\u9fa5 a-zA-Z]")
}
private val BOOK_AUTHOR_REGEX by lazy {
    Regex("^[\\u4e00-\\u9fa5 ·•a-zA-Z]")
}
private val BOOK_ISBN_REGEX by lazy {
    Regex("^[0-9-]")
}

fun isMatchTitleRequire(titleStr: String): Boolean {
    val titleStrArr = titleStr.toCharArray()
    var match = true
    for (char in titleStrArr) {
        if (!BOOK_TITLE_REGEX.matches(char.toString())) {
            match = false
            break
        }
    }
    return match
}

fun isMatchAuthorNameRequire(authorNameStr: String): Boolean {
    val nameStrArr = authorNameStr.toCharArray()
    var match = true
    for (char in nameStrArr) {
        if (!BOOK_AUTHOR_REGEX.matches(char.toString())) {
            match = false
            break
        }
    }
    return match
}

fun isMatchISBNRequire(isbnStr: String): Boolean {
    val isbnStrArr = isbnStr.toCharArray()
    var match = true
    for (char in isbnStrArr) {
        if (!BOOK_ISBN_REGEX.matches(char.toString())) {
            match = false
            break
        }
    }
    return match
}
