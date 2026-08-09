package top.yukonga.miuix.micxx.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.LinearProgressIndicator
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.TextStyle
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Clear
import top.yukonga.miuix.kmp.icon.extended.ConvertFile
import top.yukonga.miuix.kmp.icon.extended.Play
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.micxx.compiler.CompilerService
import top.yukonga.miuix.micxx.data.AppState
import top.yukonga.miuix.micxx.data.CompilerMode
import top.yukonga.miuix.micxx.data.WandboxCompiler

private val StdoutGreen = Color(0xFF2E7D32)
private val StdoutGreenBg = StdoutGreen.copy(alpha = 0.10f)
private val StderrRed = Color(0xFFC62828)
private val StderrRedBg = StderrRed.copy(alpha = 0.10f)

@Composable
fun ConsolePage(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val activeFile = AppState.getActiveFile()
    val isCompiling = AppState.isCompiling
    val consoleOutput = AppState.consoleOutput
    val programInput = AppState.programInput
    val programOutput = AppState.programOutput
    val programError = AppState.programError
    val compilerMode = AppState.compilerMode
    val wandboxCompiler = AppState.wandboxCompiler

    val consoleScrollState = rememberScrollState()
    val stdoutScrollState = rememberScrollState()
    val stderrScrollState = rememberScrollState()

    LaunchedEffect(consoleOutput) {
        consoleScrollState.scrollTo(consoleScrollState.maxValue)
    }
    LaunchedEffect(programOutput) {
        stdoutScrollState.scrollTo(stdoutScrollState.maxValue)
    }
    LaunchedEffect(programError) {
        stderrScrollState.scrollTo(stderrScrollState.maxValue)
    }

    val onRun: () -> Unit = {
        val file = activeFile
        if (file != null && !isCompiling) {
            scope.launch {
                AppState.updateCompilingState(true)
                val result = CompilerService.compile(
                    code = file.content,
                    stdin = AppState.programInput,
                    mode = AppState.compilerMode,
                    compiler = AppState.wandboxCompiler,
                    context = context,
                )
                AppState.setCompileResult(result)
            }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        item(key = "title_run") {
            SmallTitle(text = "Run")
        }
        item(key = "actions") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = onRun,
                    modifier = Modifier.weight(1f),
                    enabled = !isCompiling && activeFile != null,
                ) {
                    Icon(
                        imageVector = MiuixIcons.Play,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = if (isCompiling) "Compiling…" else "Run",
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
                Button(
                    onClick = { AppState.clearConsole() },
                    modifier = Modifier.weight(1f),
                    enabled = !isCompiling,
                ) {
                    Icon(
                        imageVector = MiuixIcons.Clear,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = "Clear",
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
            }
        }
        if (isCompiling) {
            item(key = "progress") {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 12.dp),
                )
            }
        }
        item(key = "title_mode") {
            SmallTitle(text = "Compiler Mode")
        }
        item(key = "mode_card") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                CompilerMode.values().forEach { mode ->
                    SelectableRow(
                        title = mode.displayName,
                        selected = compilerMode == mode,
                        onClick = { AppState.compilerMode = mode },
                    )
                }
            }
        }
        if (compilerMode == CompilerMode.ONLINE_WANDBOX) {
            item(key = "title_wandbox") {
                SmallTitle(text = "Wandbox Compiler")
            }
            item(key = "wandbox_card") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 12.dp),
                ) {
                    WandboxCompiler.values().forEach { compiler ->
                        SelectableRow(
                            title = compiler.displayName,
                            summary = if (compiler.isCpp) "C++" else "C",
                            selected = wandboxCompiler == compiler,
                            onClick = { AppState.wandboxCompiler = compiler },
                        )
                    }
                }
            }
        }
        item(key = "title_stdin") {
            SmallTitle(text = "stdin")
        }
        item(key = "stdin_field") {
            BasicTextField(
                value = AppState.programInput,
                onValueChange = { AppState.programInput = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = MiuixTheme.colorScheme.onBackground,
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                ),
                singleLine = false,
            )
        }
        item(key = "title_console") {
            SmallTitle(text = "Console Output")
        }
        item(key = "console_card") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Column(Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = MiuixIcons.ConvertFile,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MiuixTheme.colorScheme.onBackgroundVariant,
                        )
                        Text(
                            text = "Compiler messages",
                            fontSize = 13.sp,
                            color = MiuixTheme.colorScheme.onBackgroundVariant,
                            modifier = Modifier.padding(start = 6.dp),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 220.dp)
                            .verticalScroll(consoleScrollState)
                            .padding(top = 8.dp),
                    ) {
                        Text(
                            text = consoleOutput.ifBlank { "(no output yet)" },
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = if (consoleOutput.isBlank()) {
                                MiuixTheme.colorScheme.onBackgroundVariant
                            } else {
                                MiuixTheme.colorScheme.onBackground
                            },
                        )
                    }
                }
            }
        }
        item(key = "title_stdout") {
            SmallTitle(text = "stdout")
        }
        item(key = "stdout_card") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 220.dp)
                        .background(StdoutGreenBg)
                        .verticalScroll(stdoutScrollState)
                        .padding(12.dp),
                ) {
                    Text(
                        text = programOutput.ifBlank { "(no stdout)" },
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = if (programOutput.isBlank()) {
                            MiuixTheme.colorScheme.onBackgroundVariant
                        } else {
                            StdoutGreen
                        },
                    )
                }
            }
        }
        item(key = "title_stderr") {
            SmallTitle(text = "stderr")
        }
        item(key = "stderr_card") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 220.dp)
                        .background(StderrRedBg)
                        .verticalScroll(stderrScrollState)
                        .padding(12.dp),
                ) {
                    Text(
                        text = programError.ifBlank { "(no stderr)" },
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = if (programError.isBlank()) {
                            MiuixTheme.colorScheme.onBackgroundVariant
                        } else {
                            StderrRed
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectableRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    summary: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (selected) MiuixTheme.colorScheme.primary else Color.Transparent)
                .border(
                    width = 2.dp,
                    color = if (selected) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.outline,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MiuixTheme.colorScheme.onPrimary),
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                color = MiuixTheme.colorScheme.onBackground,
            )
            if (summary != null) {
                Text(
                    text = summary,
                    fontSize = 12.sp,
                    color = MiuixTheme.colorScheme.onBackgroundVariant,
                )
            }
        }
    }
}
