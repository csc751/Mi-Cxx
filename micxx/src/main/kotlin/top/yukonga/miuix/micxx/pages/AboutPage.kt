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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DrawScope
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.blur.Backdrop
import top.yukonga.miuix.kmp.blur.BlurColors
import top.yukonga.miuix.kmp.blur.drawBackdrop
import top.yukonga.miuix.kmp.blur.isRuntimeShaderSupported
import top.yukonga.miuix.kmp.blur.textureBlurEffect
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Edit
import top.yukonga.miuix.kmp.icon.extended.Info
import top.yukonga.miuix.kmp.icon.extended.Settings
import top.yukonga.miuix.kmp.icon.extended.Theme
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.micxx.data.AppLanguage
import top.yukonga.miuix.micxx.data.AppState
import top.yukonga.miuix.micxx.data.CodeThemeMode
import top.yukonga.miuix.micxx.data.LocalizedStrings

private const val GITHUB_URL = "https://github.com/csc751/Mi-Cxx"

private val SimpleBackdrop = object : Backdrop {
    override val isCoordinatesDependent = false

    override fun DrawScope.drawBackdrop(
        density: Density,
        coordinates: LayoutCoordinates?,
        layerBlock: (GraphicsLayerScope.() -> Unit)?,
        downscaleFactor: Int,
    ) {
        drawRect(Color(0xFF8A7CFF))
        drawRect(Color(0xFF1E6BFF), alpha = 0.6f)
        drawCircle(
            color = Color.White.copy(alpha = 0.15f),
            radius = size.minDimension * 0.4f,
            center = Offset(size.width * 0.2f, size.height * 0.2f),
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.1f),
            radius = size.minDimension * 0.35f,
            center = Offset(size.width * 0.85f, size.height * 0.8f),
        )
    }
}

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
    val appLanguage = AppState.appLanguage

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
            SmallTitle(text = LocalizedStrings["about"])
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
                            text = LocalizedStrings["app_name"],
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MiuixTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(start = 10.dp),
                        )
                    }
                    Text(
                        text = LocalizedStrings["version"],
                        fontSize = 13.sp,
                        color = MiuixTheme.colorScheme.onBackgroundVariant,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    Text(
                        text = LocalizedStrings["powered_by"],
                        color = MiuixTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }
        }

        item(key = "title_blur") {
            SmallTitle(text = LocalizedStrings["about_micxx"])
        }
        item(key = "blur_card") {
            if (isRuntimeShaderSupported()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 12.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(196.dp)
                            .drawBackdrop(
                                backdrop = SimpleBackdrop,
                                shape = { RoundedCornerShape(24.dp) },
                                effects = {
                                    textureBlurEffect(
                                        blurRadiusX = 30f,
                                        colors = BlurColors(
                                            brightness = 0.1f,
                                            saturation = 1.2f,
                                        ),
                                    )
                                },
                                contentBlendMode = BlendMode.DstIn,
                            )
                            .clip(RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = LocalizedStrings["foreground_blur_title"],
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = LocalizedStrings["powered_by"],
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.85f),
                            )
                            Spacer(Modifier.height(20.dp))
                            Box(
                                modifier = Modifier
                                    .clickable(onClick = openGithub)
                                    .padding(horizontal = 18.dp, vertical = 10.dp)
                                    .background(
                                        color = Color.White.copy(alpha = 0.22f),
                                        shape = RoundedCornerShape(14.dp),
                                    ),
                            ) {
                                Text(
                                    text = LocalizedStrings["view_on_github"],
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 12.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(196.dp)
                            .background(
                                RoundedCornerShape(24.dp),
                                brush = Brush.linearGradient(
                                    listOf(Color(0xFF8A7CFF), Color(0xFF1E6BFF)),
                                ),
                            )
                            .clip(RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = LocalizedStrings["foreground_blur_title"],
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = LocalizedStrings["powered_by"],
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.85f),
                            )
                            Spacer(Modifier.height(20.dp))
                            Box(
                                modifier = Modifier
                                    .clickable(onClick = openGithub)
                                    .padding(horizontal = 18.dp, vertical = 10.dp)
                                    .background(
                                        color = Color.White.copy(alpha = 0.22f),
                                        shape = RoundedCornerShape(14.dp),
                                    ),
                            ) {
                                Text(
                                    text = LocalizedStrings["view_on_github"],
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }

        item(key = "title_language") {
            SmallTitle(text = LocalizedStrings["language"])
        }
        item(key = "language_card") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                AppLanguage.values().forEach { lang ->
                    SelectableRow(
                        title = lang.displayName,
                        selected = appLanguage == lang,
                        onClick = { AppState.appLanguage = lang },
                    )
                }
            }
        }

        item(key = "title_theme") {
            SmallTitle(text = LocalizedStrings["code_theme"])
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
                            text = LocalizedStrings["appearance"],
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
            SmallTitle(text = LocalizedStrings["font_size"])
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
                            text = LocalizedStrings["editor"],
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
            SmallTitle(text = LocalizedStrings["tab_width"])
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
                        title = "$width ${LocalizedStrings["editor"]}",
                        selected = tabWidth == width,
                        onClick = { AppState.tabWidth = width },
                    )
                }
            }
        }
        item(key = "title_compiler") {
            SmallTitle(text = LocalizedStrings["compiler_settings"])
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
                            text = LocalizedStrings["build_mode"],
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
                        text = LocalizedStrings["switch_modes"],
                        fontSize = 13.sp,
                        color = MiuixTheme.colorScheme.onBackgroundVariant,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }
        }
        item(key = "title_licenses") {
            SmallTitle(text = LocalizedStrings["licenses"])
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
                        text = LocalizedStrings["licenses_text"],
                        color = MiuixTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = LocalizedStrings["licenses_list"],
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
                    Text(text = LocalizedStrings["view_github"])
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
