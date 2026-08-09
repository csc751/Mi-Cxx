// Copyright 2025, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package top.yukonga.miuix.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

@Composable
fun AboutPage(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier
            .overScrollVertical()
            .scrollEndHaptic(),
        contentPadding = contentPadding,
    ) {
        item {
            SmallTitle(text = "About")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Miuix Demo",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MiuixTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "Version 1.0.0",
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    )
                    Text(
                        text = "A demo app showcasing all components from the Miuix Compose Multiplatform UI library.",
                        color = MiuixTheme.colorScheme.onSurfaceSecondary,
                    )
                }
            }
        }

        item {
            SmallTitle(text = "Included Components")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val components = listOf(
                        "BasicComponent" to "Custom layout with start/end actions",
                        "Switch" to "Toggle switches with preference variants",
                        "Checkbox" to "Tri-state checkboxes with preferences",
                        "RadioButton" to "Single-select radio button groups",
                        "Button" to "Text buttons with primary/disabled states",
                        "TabRow" to "Tab navigation with contour variant + Pager",
                        "BreadcrumbBar" to "Path navigation breadcrumbs",
                        "ArrowPreference" to "Clickable rows with arrow indicator",
                        "Dialog" to "Overlay & centered dialogs",
                        "BottomSheet" to "Overlay bottom sheets with actions",
                        "Dropdown" to "Overlay & window dropdown preferences",
                        "Spinner" to "Spinner preferences with icons",
                        "Snackbar" to "Actionable & dismissible snackbars",
                        "TextField" to "Input fields with labels",
                        "ProgressIndicator" to "Linear, circular & infinite progress",
                        "Slider" to "Sliders, range sliders & vertical sliders",
                        "Card" to "Cards with sink/tilt press feedback",
                        "Tooltip" to "Simple & rich tooltips",
                        "Badge" to "Notification badges with counts",
                        "NumberPicker" to "Time picker style number pickers",
                        "ColorPicker" to "HSV color picker with hex input",
                        "ColorPalette" to "Color palette selector",
                    )
                    components.forEach { (name, desc) ->
                        Text(
                            text = name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MiuixTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                        Text(
                            text = desc,
                            fontSize = 14.sp,
                            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        )
                    }
                }
            }
        }

        item {
            SmallTitle(text = "Library")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 24.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Miuix",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MiuixTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "A Compose Multiplatform UI library inspired by MIUI",
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    )
                    Spacer(Modifier.padding(top = 8.dp))
                    Text(
                        text = "Modules: miuix-core, miuix-ui, miuix-preference, miuix-icons, miuix-squircle, miuix-shader, miuix-blur, miuix-nav",
                        color = MiuixTheme.colorScheme.onSurfaceSecondary,
                        fontSize = 14.sp,
                    )
                    Spacer(Modifier.padding(top = 8.dp))
                    Text(
                        text = "github.com/compose-miuix-ui/miuix",
                        color = MiuixTheme.colorScheme.primary,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}
