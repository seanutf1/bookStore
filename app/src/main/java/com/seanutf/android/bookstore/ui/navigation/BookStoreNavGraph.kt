package com.seanutf.android.bookstore.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.seanutf.android.bookstore.ui.home.HomeDestination
import com.seanutf.android.bookstore.ui.home.HomeScreen
import com.seanutf.android.bookstore.ui.item.ItemDetailsDestination
import com.seanutf.android.bookstore.ui.item.BookDetailsScreen
import com.seanutf.android.bookstore.ui.item.BookEditDestination
import com.seanutf.android.bookstore.ui.item.BookEditScreen
import com.seanutf.android.bookstore.ui.item.BookEntryDestination
import com.seanutf.android.bookstore.ui.item.BookEntryScreen

@Composable
fun BookStoreNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(BookEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = BookEntryDestination.route) {
            BookEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            BookDetailsScreen(
                navigateToEditItem = { navController.navigate("${BookEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = BookEditDestination.routeWithArgs,
            arguments = listOf(navArgument(BookEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            BookEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
