package top.yukonga.miuix.micxx

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.PaddingValues
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.NavigationBar
import top.yukonga.miuix.kmp.basic.NavigationBarItem
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.ConvertFile
import top.yukonga.miuix.kmp.icon.extended.Edit
import top.yukonga.miuix.kmp.icon.extended.Folder
import top.yukonga.miuix.kmp.icon.extended.Info
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController
import top.yukonga.miuix.micxx.data.AppState
import top.yukonga.miuix.micxx.data.CodeThemeMode
import top.yukonga.miuix.micxx.data.LocalizedStrings
import top.yukonga.miuix.micxx.pages.AboutPage
import top.yukonga.miuix.micxx.pages.ConsolePage
import top.yukonga.miuix.micxx.pages.EditorPage
import top.yukonga.miuix.micxx.pages.FilesPage

@Composable
fun MiCxxApp() {
    val isDark = isSystemInDarkTheme()
    val effectiveDark = when (AppState.codeThemeMode) {
        CodeThemeMode.DARK -> true
        CodeThemeMode.LIGHT -> false
        CodeThemeMode.SYSTEM -> isDark
    }
    val controller = remember(effectiveDark) {
        ThemeController(if (effectiveDark) ColorSchemeMode.Dark else ColorSchemeMode.Light)
    }
    var selectedPage by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = MiuixScrollBehavior()

    val pages = listOf(
        Triple(LocalizedStrings["tab_files"], MiuixIcons.Folder, LocalizedStrings["title_files"]),
        Triple(LocalizedStrings["tab_editor"], MiuixIcons.Edit, LocalizedStrings["title_editor"]),
        Triple(LocalizedStrings["tab_console"], MiuixIcons.ConvertFile, LocalizedStrings["title_console"]),
        Triple(LocalizedStrings["tab_about"], MiuixIcons.Info, LocalizedStrings["title_about"]),
    )

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
                0 -> FilesPage(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding,
                    onFileOpen = { file ->
                        AppState.openFile(file)
                        selectedPage = 1
                    },
                )
                1 -> EditorPage(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding,
                    onNavigateToConsole = { selectedPage = 2 },
                )
                2 -> ConsolePage(
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
