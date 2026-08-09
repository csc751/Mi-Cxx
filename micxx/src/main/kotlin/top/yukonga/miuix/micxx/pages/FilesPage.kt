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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Add
import top.yukonga.miuix.kmp.icon.extended.ChevronForward
import top.yukonga.miuix.kmp.icon.extended.Delete
import top.yukonga.miuix.kmp.icon.extended.ExpandMore
import top.yukonga.miuix.kmp.icon.extended.File
import top.yukonga.miuix.kmp.icon.extended.Folder
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.micxx.data.CodeFile
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
            item { SmallTitle("Projects (${projects.size})") }

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
                                Text(text = "New File", fontSize = 14.sp)
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
                                    text = file.name,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f),
                                )
                                if (file.isModified) {
                                    Text(text = " *", color = MiuixTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.padding(32.dp))
            }
        }

        // New Project FAB-style button at bottom
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
                    Text("New Project")
                }
            }
        }
    }

    // New Project Dialog
    if (showNewProjectDialog) {
        Dialog(onDismissRequest = { showNewProjectDialog = false }) {
            Card(modifier = Modifier.padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("New Project", fontSize = 18.sp)
                    Spacer(Modifier.width(0.dp))
                    BasicTextField(
                        value = newProjectName,
                        onValueChange = { newProjectName = it },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Button(onClick = { showNewProjectDialog = false }) {
                            Text("Cancel")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {
                            if (newProjectName.isNotBlank()) {
                                ProjectManager.createProject(newProjectName.trim())
                                refreshProjects()
                                showNewProjectDialog = false
                            }
                        }) {
                            Text("Create")
                        }
                    }
                }
            }
        }
    }

    // New File Dialog
    showNewFileDialog?.let { projectName ->
        Dialog(onDismissRequest = { showNewFileDialog = null }) {
            Card(modifier = Modifier.padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("New File in $projectName", fontSize = 18.sp)
                    Text("e.g. main.cpp, utils.h, solver.c", fontSize = 12.sp, color = MiuixTheme.colorScheme.onBackgroundVariant)
                    BasicTextField(
                        value = newFileName,
                        onValueChange = { newFileName = it },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Button(onClick = { showNewFileDialog = null }) {
                            Text("Cancel")
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
                            Text("Create")
                        }
                    }
                }
            }
        }
    }
}
