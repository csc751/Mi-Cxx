// Copyright 2025, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package top.yukonga.miuix.demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.NavigationBar
import top.yukonga.miuix.kmp.basic.NavigationBarItem
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.GridView
import top.yukonga.miuix.kmp.icon.extended.Home
import top.yukonga.miuix.kmp.icon.extended.Info
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController

@Composable
fun DemoApp() {
    val controller = remember { ThemeController(ColorSchemeMode.System) }
    var selectedPage by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = MiuixScrollBehavior()
    val lazyListState = rememberLazyListState()

    val pages = remember {
        listOf(
            Triple("Components", MiuixIcons.Home, "Miuix Components"),
            Triple("Icons", MiuixIcons.GridView, "Icon Gallery"),
            Triple("About", MiuixIcons.Info, "About"),
        )
    }

    MiuixTheme(controller = controller) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = pages[selectedPage].third,
                    scrollBehavior = scrollBehavior,
                )
            },
            bottomBar = {
                NavigationBar {
                    pages.forEachIndexed { index, (label, icon, _) ->
                        NavigationBarItem(
                            selected = selectedPage == index,
                            onClick = { selectedPage = index },
                            icon = icon,
                            label = label,
                        )
                    }
                }
            },
            snackbarHost = { SnackbarHost(state = snackbarHostState) },
        ) { innerPadding ->
            when (selectedPage) {
                0 -> ComponentsPage(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding,
                    snackbarHostState = snackbarHostState,
                    lazyListState = lazyListState,
                )
                1 -> IconsPage(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding,
                )
                else -> AboutPage(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding,
                )
            }
        }
    }
}
