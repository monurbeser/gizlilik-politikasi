package com.notaday.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.notaday.presentation.calendar.CalendarScreen
import com.notaday.presentation.notedetail.NoteDetailScreen
import com.notaday.presentation.notelist.NoteListScreen
import com.notaday.presentation.settings.SettingsScreen

@Composable
fun NotadayNavHost(initialNoteId: Long = 0L) {
    val navController = rememberNavController()
    val topRoutes = listOf(NavRoutes.Calendar, NavRoutes.List, NavRoutes.Settings)
    LaunchedEffect(initialNoteId) {
        if (initialNoteId > 0L) navController.navigate("${NavRoutes.Detail}/$initialNoteId")
    }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val current = navBackStackEntry?.destination
            NavigationBar {
                topRoutes.forEach { route ->
                    NavigationBarItem(
                        selected = current?.hierarchy?.any { it.route == route } == true,
                        onClick = { navController.navigate(route) },
                        icon = {
                            when (route) {
                                NavRoutes.Calendar -> Icon(Icons.Rounded.CalendarMonth, null)
                                NavRoutes.List -> Icon(Icons.Rounded.List, null)
                                else -> Icon(Icons.Rounded.Settings, null)
                            }
                        },
                        label = { Text(route.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController = navController, startDestination = NavRoutes.Calendar, modifier = Modifier.padding(padding)) {
            composable(NavRoutes.Calendar) { CalendarScreen(onOpenDetail = { id -> navController.navigate("${NavRoutes.Detail}/$id") }) }
            composable(NavRoutes.List) { NoteListScreen(onOpenDetail = { id -> navController.navigate("${NavRoutes.Detail}/$id") }) }
            composable(NavRoutes.Settings) { SettingsScreen() }
            composable("${NavRoutes.Detail}/{noteId}") { entry ->
                val noteId = entry.arguments?.getString("noteId")?.toLongOrNull() ?: 0L
                NoteDetailScreen(noteId = noteId, onBack = { navController.popBackStack() })
            }
        }
    }
}
