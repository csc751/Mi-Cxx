// Copyright 2026, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package top.yukonga.miuix.micxx.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Add
import top.yukonga.miuix.kmp.icon.extended.ChevronForward
import top.yukonga.miuix.kmp.icon.extended.ExpandMore
import top.yukonga.miuix.kmp.icon.extended.File
import top.yukonga.miuix.kmp.icon.extended.Folder
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.micxx.data.CodeFile
import top.yukonga.miuix.micxx.data.LocalizedStrings
import top.yukonga.miuix.micxx.data.Project
import top.yukonga.miuix.micxx.storage.ProjectManager

@Composable
fun FilesPage(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    onFileOpen: (CodeFile) -> Unit,
) {
    val context = LocalContext.current
    var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
    var expandedProject by remember { mutableStateOf<String?>(null) }
    var projectFiles by remember { mutableStateOf<List<CodeFile>>(emptyList()) }
    var showNewProjectDialog by remember { mutableStateOf(false) }
    var showNewFileDialog by remember { mutableStateOf<String?>(null) }
    var newProjectName by remember { mutableStateOf("") }
    var newFileName by remember { mutableStateOf("") }

    fun refreshProjects() {
        projects = ProjectManager.getProjects()
    }

    fun refreshFiles(projectName: String) {
        projectFiles = ProjectManager.getFiles(projectName)
    }

    LaunchedEffect(Unit) {
        ProjectManager.init(context)
        refreshProjects()
        if (projects.isEmpty()) {
            ProjectManager.createProject("default")
            refreshProjects()
        }
        expandedProject = projects.firstOrNull()?.name
        expandedProject?.let { refreshFiles(it) }
    }

    Box(modifier = modifier.fillMaxSize().padding(contentPadding)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item { SmallTitle("${LocalizedStrings["projects"]} (${projects.size})") }

            projects.forEach { project ->
                val isExpanded = expandedProject == project.name
                item(key = "project_${project.name}") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            expandedProject = if (isExpanded) null else project.name
                            if (expandedProject != null) {
                                refreshFiles(expandedProject!!)
                            }
                        },
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = MiuixIcons.Folder,
                                contentDescription = null,
                                tint = MiuixTheme.colorScheme.primary,
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = project.name,
                                modifier = Modifier.weight(1f),
                                fontSize = 16.sp,
                            )
                            Icon(
                                imageVector = if (isExpanded) MiuixIcons.ExpandMore else MiuixIcons.ChevronForward,
                                contentDescription = null,
                            )
                        }
                    }
                }

                if (isExpanded) {
                    item(key = "newfile_${project.name}") {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp),
                            onClick = {
                                showNewFileDialog = project.name
                                newFileName = ""
                            },
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = MiuixIcons.Add,
                                    contentDescription = null,
                                    tint = MiuixTheme.colorScheme.primary,
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(text = LocalizedStrings["new_file"], fontSize = 14.sp)
                            }
                        }
                    }

                    items(projectFiles.size) { index ->
                        val file = projectFiles[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp),
                            onClick = {
                                onFileOpen(file)
                            },
                            onLongPress = {
                                ProjectManager.deleteFile(file.path)
                                refreshFiles(project.name)
                            },
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = MiuixIcons.File,
                                    contentDescription = null,
                                    tint = MiuixTheme.colorScheme.onBackgroundVariant,
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = file.name + if (file.isModified) LocalizedStrings["files_dirty"] else "",
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.padding(32.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Button(
                onClick = {
                    showNewProjectDialog = true
                    newProjectName = ""
                },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = MiuixIcons.Add,
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(LocalizedStrings["new_project"])
                }
            }
        }
    }

    OverlayDialog(
        show = showNewProjectDialog,
        title = LocalizedStrings["new_project"],
        onDismissRequest = { showNewProjectDialog = false },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = newProjectName,
                onValueChange = { newProjectName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = LocalizedStrings["new_project"],
                useLabelAsPlaceholder = true,
                singleLine = true,
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(onClick = { showNewProjectDialog = false }) {
                    Text(LocalizedStrings["cancel"])
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    if (newProjectName.isNotBlank()) {
                        ProjectManager.createProject(newProjectName.trim())
                        refreshProjects()
                        showNewProjectDialog = false
                    }
                }) {
                    Text(LocalizedStrings["create"])
                }
            }
        }
    }

    showNewFileDialog?.let { projectName ->
        OverlayDialog(
            show = true,
            title = "${LocalizedStrings["new_file"]} in $projectName",
            summary = LocalizedStrings["new_file_hint"],
            onDismissRequest = { showNewFileDialog = null },
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = newFileName,
                    onValueChange = { newFileName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = LocalizedStrings["new_file_hint"],
                    useLabelAsPlaceholder = true,
                    singleLine = true,
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Button(onClick = { showNewFileDialog = null }) {
                        Text(LocalizedStrings["cancel"])
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        if (newFileName.isNotBlank()) {
                            val file = ProjectManager.createFile(projectName, newFileName.trim())
                            refreshFiles(projectName)
                            onFileOpen(file)
                            showNewFileDialog = null
                        }
                    }) {
                        Text(LocalizedStrings["create"])
                    }
                }
            }
        }
    }
}
