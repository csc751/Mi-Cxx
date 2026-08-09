package top.yukonga.miuix.micxx.pages

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Edit
import top.yukonga.miuix.kmp.icon.extended.Info
import top.yukonga.miuix.kmp.icon.extended.Settings
import top.yukonga.miuix.kmp.icon.extended.Theme
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.micxx.data.AppState
import top.yukonga.miuix.micxx.data.CodeThemeMode

private const val GITHUB_URL = "https://github.com/compose-miuix-ui/miuix"

@Composable
fun AboutPage(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    val context = LocalContext.current
    val codeThemeMode = AppState.codeThemeMode
    val fontSize = AppState.fontSize
    val tabWidth = AppState.tabWidth
    val compilerMode = AppState.compilerMode

    val openGithub: () -> Unit = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { context.startActivity(intent) }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        item(key = "title_about") {
            SmallTitle(text = "About")
        }
        item(key = "app_info") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = MiuixIcons.Info,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = MiuixTheme.colorScheme.primary,
                        )
                        Text(
                            text = "Mi Cxx",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MiuixTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(start = 10.dp),
                        )
                    }
                    Text(
                        text = "Version 1.0.0",
                        fontSize = 13.sp,
                        color = MiuixTheme.colorScheme.onBackgroundVariant,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    Text(
                        text = "C/C++ IDE powered by MIUIX",
                        color = MiuixTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }
        }
        item(key = "title_theme") {
            SmallTitle(text = "Code Theme")
        }
        item(key = "theme_card") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Column(Modifier.padding(top = 12.dp)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Theme,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MiuixTheme.colorScheme.onBackgroundVariant,
                        )
                        Text(
                            text = "Appearance",
                            fontSize = 13.sp,
                            color = MiuixTheme.colorScheme.onBackgroundVariant,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                    CodeThemeMode.values().forEach { mode ->
                        SelectableRow(
                            title = mode.displayName,
                            selected = codeThemeMode == mode,
                            onClick = { AppState.codeThemeMode = mode },
                        )
                    }
                }
            }
        }
        item(key = "title_font") {
            SmallTitle(text = "Font Size")
        }
        item(key = "font_card") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = MiuixIcons.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MiuixTheme.colorScheme.onBackgroundVariant,
                        )
                        Text(
                            text = "Editor",
                            fontSize = 13.sp,
                            color = MiuixTheme.colorScheme.onBackgroundVariant,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "${fontSize.toInt()} sp",
                            color = MiuixTheme.colorScheme.primary,
                        )
                    }
                    Slider(
                        value = fontSize,
                        onValueChange = { AppState.fontSize = it },
                        valueRange = 10f..24f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                    )
                }
            }
        }
        item(key = "title_tab") {
            SmallTitle(text = "Tab Width")
        }
        item(key = "tab_card") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                listOf(2, 4, 8).forEach { width ->
                    SelectableRow(
                        title = "$width spaces",
                        selected = tabWidth == width,
                        onClick = { AppState.tabWidth = width },
                    )
                }
            }
        }
        item(key = "title_compiler") {
            SmallTitle(text = "Compiler")
        }
        item(key = "compiler_card") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = MiuixIcons.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MiuixTheme.colorScheme.onBackgroundVariant,
                        )
                        Text(
                            text = "Build mode",
                            fontSize = 13.sp,
                            color = MiuixTheme.colorScheme.onBackgroundVariant,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                    Text(
                        text = compilerMode.displayName,
                        color = MiuixTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    Text(
                        text = "Switch between Online (Wandbox) and Local (Termux) on the Console tab.",
                        fontSize = 13.sp,
                        color = MiuixTheme.colorScheme.onBackgroundVariant,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }
        }
        item(key = "title_licenses") {
            SmallTitle(text = "Open Source Licenses")
        }
        item(key = "licenses_card") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Mi Cxx is built on open source software:",
                        color = MiuixTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "• MIUIX (Apache 2.0)\n• Jetpack Compose / AndroidX\n• Wandbox compilation API\n• Kotlin (Apache 2.0)",
                        fontSize = 13.sp,
                        color = MiuixTheme.colorScheme.onBackgroundVariant,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        }
        item(key = "github_button") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = openGithub,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "View on GitHub")
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
        Text(
            text = title,
            color = MiuixTheme.colorScheme.onBackground,
        )
    }
}
