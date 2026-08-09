// Copyright 2026, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package top.yukonga.miuix.micxx.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.icon.extended.Play
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.micxx.data.AppState
import top.yukonga.miuix.micxx.data.CodeThemeMode
import top.yukonga.miuix.micxx.data.LocalizedStrings
import top.yukonga.miuix.micxx.editor.CodeEditor
import top.yukonga.miuix.micxx.editor.CodeThemes
import top.yukonga.miuix.micxx.storage.ProjectManager

@Composable
fun EditorPage(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    onNavigateToConsole: () -> Unit,
) {
    val isSystemDark = isSystemInDarkTheme()
    val effectiveDark = when (AppState.codeThemeMode) {
        CodeThemeMode.DARK -> true
        CodeThemeMode.LIGHT -> false
        CodeThemeMode.SYSTEM -> isSystemDark
    }
    val codeTheme = CodeThemes.resolve(effectiveDark)

    val activeFile = AppState.getActiveFile()
    val openFiles = AppState.openFiles
    val activeIndex = AppState.activeFileIndex

    Column(modifier = modifier.fillMaxSize().padding(contentPadding)) {
        if (openFiles.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .background(codeTheme.background)
                    .padding(horizontal = 4.dp),
            ) {
                openFiles.forEachIndexed { index, file ->
                    val isActive = index == activeIndex
                    Row(
                        modifier = Modifier
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Card(
                            modifier = Modifier,
                            onClick = { AppState.setActiveFile(index) },
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = file.name + if (file.isModified) " *" else "",
                                    fontSize = 13.sp,
                                    color = if (isActive) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.onBackgroundVariant,
                                )
                                Spacer(Modifier.width(8.dp))
                                Icon(
                                    imageVector = MiuixIcons.Close,
                                    contentDescription = LocalizedStrings["close_tab"],
                                    tint = MiuixTheme.colorScheme.onBackgroundVariant,
                                    modifier = Modifier.size(14.dp),
                                )
                            }
                        }
                    }
                }
            }
        }

        if (activeFile != null) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                CodeEditor(
                    text = activeFile.content,
                    onTextChange = { newContent ->
                        AppState.updateActiveFileContent(newContent)
                    },
                    theme = codeTheme,
                    fontSizeSp = AppState.fontSize,
                    tabWidth = AppState.tabWidth,
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = LocalizedStrings["no_file_open"],
                        fontSize = 18.sp,
                        color = MiuixTheme.colorScheme.onBackgroundVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = LocalizedStrings["go_to_files"],
                        fontSize = 14.sp,
                        color = MiuixTheme.colorScheme.onBackgroundVariant,
                    )
                }
            }
        }

        if (activeFile != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MiuixTheme.colorScheme.background)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(onClick = {
                    ProjectManager.saveFile(activeFile.path, activeFile.content)
                    AppState.saveActiveFile()
                }) {
                    Text(LocalizedStrings["save"])
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    ProjectManager.saveFile(activeFile.path, activeFile.content)
                    AppState.saveActiveFile()
                    onNavigateToConsole()
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = MiuixIcons.Play, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text(LocalizedStrings["run"])
                    }
                }
            }
        }
    }
}
