// Copyright 2025, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package top.yukonga.miuix.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Badge
import top.yukonga.miuix.kmp.basic.BadgedBox
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BreadcrumbBar
import top.yukonga.miuix.kmp.basic.BreadcrumbItem
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.ColorPalette
import top.yukonga.miuix.kmp.basic.ColorPicker
import top.yukonga.miuix.kmp.basic.ColorSpace
import top.yukonga.miuix.kmp.basic.DropdownEntry
import top.yukonga.miuix.kmp.basic.DropdownItem
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.LinearProgressIndicator
import top.yukonga.miuix.kmp.basic.NumberPicker
import top.yukonga.miuix.kmp.basic.RichTooltipBox
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.SnackbarDuration
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.SnackbarResult
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.TabRowWithContour
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TooltipBox
import top.yukonga.miuix.kmp.basic.VerticalSlider
import top.yukonga.miuix.kmp.basic.joinToPath
import top.yukonga.miuix.kmp.basic.rememberTooltipState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.icon.extended.Contacts
import top.yukonga.miuix.kmp.icon.extended.Edit
import top.yukonga.miuix.kmp.icon.extended.Email
import top.yukonga.miuix.kmp.icon.extended.Favorites
import top.yukonga.miuix.kmp.icon.extended.Info
import top.yukonga.miuix.kmp.icon.extended.Messages
import top.yukonga.miuix.kmp.icon.extended.Ok
import top.yukonga.miuix.kmp.icon.extended.Settings
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.CheckboxLocation
import top.yukonga.miuix.kmp.preference.CheckboxPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.OverlaySpinnerPreference
import top.yukonga.miuix.kmp.preference.RadioButtonPreference
import top.yukonga.miuix.kmp.preference.RangeSliderPreference
import top.yukonga.miuix.kmp.preference.SliderPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.preference.WindowDropdownPreference
import top.yukonga.miuix.kmp.preference.WindowSpinnerPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import kotlin.math.round

@Composable
fun ComponentsPage(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
) {
    LazyColumn(
        modifier = modifier
            .overScrollVertical()
            .scrollEndHaptic(),
        state = lazyListState,
        contentPadding = contentPadding,
    ) {
        item(key = "basicComponent") {
            SmallTitle(text = "Basic Component")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                BasicComponent(
                    title = "Title",
                    summary = "Summary",
                    startAction = { Text("Start") },
                    endActions = {
                        Text(
                            text = "End1",
                            fontSize = MiuixTheme.textStyles.body2.fontSize,
                            color = MiuixTheme.colorScheme.onSurfaceVariantActions,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "End2",
                            fontSize = MiuixTheme.textStyles.body2.fontSize,
                            color = MiuixTheme.colorScheme.onSurfaceVariantActions,
                        )
                    },
                )
                BasicComponent(
                    title = "Disabled",
                    summary = "Not interactive",
                    enabled = false,
                )
            }
        }

        item(key = "switch") {
            val switch = remember { mutableStateOf(false) }
            val switchTrue = remember { mutableStateOf(true) }
            val superSwitchAnimState = remember { mutableStateOf(false) }
            val superSwitchState = remember { mutableStateOf(false) }

            SmallTitle(text = "Switch")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Switch(checked = switch.value, onCheckedChange = { switch.value = it })
                    Switch(checked = switchTrue.value, onCheckedChange = { switchTrue.value = it }, modifier = Modifier.padding(start = 6.dp))
                    Switch(checked = false, onCheckedChange = {}, modifier = Modifier.padding(start = 6.dp), enabled = false)
                    Switch(checked = true, onCheckedChange = {}, modifier = Modifier.padding(start = 6.dp), enabled = false)
                }
                SwitchPreference(
                    title = "Expandable Switch",
                    summary = "Click to expand",
                    checked = superSwitchAnimState.value,
                    onCheckedChange = { superSwitchAnimState.value = it },
                )
                AnimatedVisibility(
                    visible = superSwitchAnimState.value,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    SwitchPreference(
                        title = "Sub Switch",
                        checked = superSwitchState.value,
                        onCheckedChange = { superSwitchState.value = it },
                    )
                }
                SwitchPreference(title = "Disabled Switch", checked = true, enabled = false, onCheckedChange = {})
            }
        }

        item(key = "checkbox") {
            var checkbox by remember { mutableStateOf(false) }
            var checkboxTrue by remember { mutableStateOf(true) }
            var checkboxIndeterminate by remember { mutableStateOf(ToggleableState.Indeterminate) }
            var prefChecked by remember { mutableStateOf(false) }

            SmallTitle(text = "Checkbox")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Checkbox(state = ToggleableState(checkbox), onClick = { checkbox = !checkbox })
                    Checkbox(state = ToggleableState(checkboxTrue), onClick = { checkboxTrue = !checkboxTrue }, modifier = Modifier.padding(start = 8.dp))
                    Checkbox(
                        state = checkboxIndeterminate,
                        onClick = {
                            checkboxIndeterminate = when (checkboxIndeterminate) {
                                ToggleableState.Off -> ToggleableState.Indeterminate
                                ToggleableState.Indeterminate -> ToggleableState.On
                                ToggleableState.On -> ToggleableState.Off
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp),
                    )
                    Checkbox(state = ToggleableState.Off, onClick = null, modifier = Modifier.padding(start = 8.dp), enabled = false)
                    Checkbox(state = ToggleableState.On, onClick = null, modifier = Modifier.padding(start = 8.dp), enabled = false)
                }
                CheckboxPreference(
                    checkboxLocation = CheckboxLocation.End,
                    title = "Checkbox Preference",
                    checked = prefChecked,
                    endActions = {
                        Text(
                            text = "$prefChecked",
                            fontSize = MiuixTheme.textStyles.body2.fontSize,
                            color = MiuixTheme.colorScheme.onSurfaceVariantActions,
                        )
                    },
                    onCheckedChange = { prefChecked = it },
                )
                CheckboxPreference(title = "Disabled Checkbox", checked = true, enabled = false, onCheckedChange = {})
            }
        }

        item(key = "radioButton") {
            var selectedIndex by remember { mutableIntStateOf(0) }

            SmallTitle(text = "RadioButton")
            listOf("Option A", "Option B", "Option C").forEachIndexed { index, title ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 6.dp),
                ) {
                    RadioButtonPreference(
                        title = title,
                        summary = "Selected: ${selectedIndex == index}",
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                    )
                }
            }
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                RadioButtonPreference(title = "Disabled", summary = "Unavailable", selected = true, enabled = false, onClick = {})
            }
        }

        item(key = "button") {
            var clickCount by remember { mutableIntStateOf(0) }
            var submitClickCount by remember { mutableIntStateOf(0) }

            SmallTitle(text = "Button")
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                TextButton(
                    text = "Click: $clickCount",
                    onClick = { clickCount++ },
                    modifier = Modifier.weight(1f),
                )
                TextButton(
                    text = "Submit: $submitClickCount",
                    onClick = { submitClickCount++ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColorsPrimary(),
                )
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                TextButton(text = "Disabled", onClick = {}, modifier = Modifier.weight(1f), enabled = false)
                TextButton(text = "Disabled", onClick = {}, enabled = false, modifier = Modifier.weight(1f), colors = ButtonDefaults.textButtonColorsPrimary())
            }
        }

        item(key = "tabRow") {
            val tabTexts = remember { listOf("Tab 1", "Tab 2", "Tab 3") }
            val tabTexts1 = remember { listOf("Tab 1", "Tab 2", "Tab 3", "Tab 4", "Tab 5") }
            var selectedTabIndex by remember { mutableIntStateOf(0) }

            SmallTitle(text = "TabRow")
            TabRow(
                tabs = tabTexts,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                insideMargin = PaddingValues(16.dp),
            ) {
                val scope = rememberCoroutineScope()
                val pagerState = rememberPagerState(pageCount = { tabTexts1.size })
                TabRowWithContour(
                    tabs = tabTexts1,
                    selectedTabIndex = pagerState.currentPage,
                    onTabSelected = { scope.launch { pagerState.animateScrollToPage(it) } },
                )
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    pageContent = { page ->
                        Text(text = "Content of ${tabTexts1[page]}", modifier = Modifier.fillMaxWidth())
                    },
                )
            }
        }

        item(key = "breadcrumbBar") {
            val items = remember {
                listOf(
                    BreadcrumbItem(path = "/storage/emulated/0", text = "Internal storage"),
                    BreadcrumbItem(path = "DataBackup"),
                    BreadcrumbItem(path = "apps"),
                    BreadcrumbItem(path = "com.tencent.mobileqq"),
                    BreadcrumbItem(path = "user_0"),
                )
            }
            var highlightIndex by remember { mutableIntStateOf(items.size - 1) }

            SmallTitle(text = "BreadcrumbBar")
            BreadcrumbBar(
                items = items,
                onItemClick = { highlightIndex = it },
                highlightIndex = highlightIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            )
        }

        item(key = "arrow") {
            var volume by remember { mutableFloatStateOf(0.5f) }

            SmallTitle(text = "Arrow")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                ArrowPreference(
                    title = "Arrow with Icon",
                    startAction = {
                        Icon(
                            imageVector = MiuixIcons.Contacts,
                            contentDescription = "Personal",
                            tint = MiuixTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(end = 8.dp),
                        )
                    },
                    endActions = {
                        Text(
                            text = "End",
                            fontSize = MiuixTheme.textStyles.body2.fontSize,
                            color = MiuixTheme.colorScheme.onSurfaceVariantActions,
                        )
                    },
                    onClick = {},
                )
                SliderPreference(
                    title = "Volume",
                    valueText = "${(volume * 100).toInt()}%",
                    value = volume,
                    onValueChange = { volume = it },
                )
                ArrowPreference(
                    title = "Disabled Arrow",
                    endActions = {
                        Text(
                            text = "End",
                            fontSize = MiuixTheme.textStyles.body2.fontSize,
                            color = MiuixTheme.colorScheme.disabledOnSecondaryVariant,
                        )
                    },
                    enabled = false,
                )
            }
        }

        item(key = "dialog") {
            var showOverlayDialog by remember { mutableStateOf(false) }
            var showWindowDialog by remember { mutableStateOf(false) }

            SmallTitle(text = "Dialog")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                ArrowPreference(
                    title = "Overlay Dialog",
                    summary = "Click to show",
                    onClick = { showOverlayDialog = true },
                )
                ArrowPreference(
                    title = "Window Dialog",
                    summary = "Click to show",
                    onClick = { showWindowDialog = true },
                )
            }

            OverlayDialog(
                show = showOverlayDialog,
                title = "Overlay Dialog",
                summary = "A dialog inside MiuixPopupHost.",
                onDismissRequest = { showOverlayDialog = false },
                content = {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(text = "Cancel", onClick = { showOverlayDialog = false }, modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(20.dp))
                        TextButton(text = "Confirm", onClick = { showOverlayDialog = false }, modifier = Modifier.weight(1f), colors = ButtonDefaults.textButtonColorsPrimary())
                    }
                },
            )
            OverlayDialog(
                show = showWindowDialog,
                title = "Window Dialog",
                summary = "A centered dialog presentation.",
                largeScreen = true,
                onDismissRequest = { showWindowDialog = false },
                content = {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(text = "Cancel", onClick = { showWindowDialog = false }, modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(20.dp))
                        TextButton(text = "Confirm", onClick = { showWindowDialog = false }, modifier = Modifier.weight(1f), colors = ButtonDefaults.textButtonColorsPrimary())
                    }
                },
            )
        }

        item(key = "bottomSheet") {
            var showOverlaySheet by remember { mutableStateOf(false) }
            var showWindowSheet by remember { mutableStateOf(false) }
            var sheetSwitch by remember { mutableStateOf(true) }

            SmallTitle(text = "BottomSheet")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                ArrowPreference(
                    title = "Overlay BottomSheet",
                    summary = "Click to show",
                    onClick = { showOverlaySheet = true },
                )
                ArrowPreference(
                    title = "Window BottomSheet",
                    summary = "Click to show",
                    onClick = { showWindowSheet = true },
                )
            }

            OverlayBottomSheet(
                title = "Overlay BottomSheet",
                show = showOverlaySheet,
                onDismissRequest = { showOverlaySheet = false },
                startAction = {
                    IconButton(onClick = { showOverlaySheet = false }) {
                        Icon(imageVector = MiuixIcons.Close, contentDescription = "Cancel", tint = MiuixTheme.colorScheme.onBackground)
                    }
                },
                endAction = {
                    IconButton(onClick = { showOverlaySheet = false }) {
                        Icon(imageVector = MiuixIcons.Ok, contentDescription = "Confirm", tint = MiuixTheme.colorScheme.onBackground)
                    }
                },
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "BottomSheet content goes here.", modifier = Modifier.padding(bottom = 12.dp))
                    SwitchPreference(
                        title = "Sheet Switch",
                        checked = sheetSwitch,
                        onCheckedChange = { sheetSwitch = it },
                    )
                }
            }
        }

        item(key = "dropdown") {
            var overlaySelected by remember { mutableIntStateOf(0) }
            var windowSelected by remember { mutableIntStateOf(0) }
            val options = remember { listOf("Option 1", "Option 2", "Option 3", "Option 4") }

            SmallTitle(text = "Dropdown")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                OverlayDropdownPreference(
                    title = "Dropdown (O)",
                    items = options,
                    selectedIndex = overlaySelected,
                    onSelectedIndexChange = { overlaySelected = it },
                )
                WindowDropdownPreference(
                    title = "Dropdown (W)",
                    items = options,
                    selectedIndex = windowSelected,
                    onSelectedIndexChange = { windowSelected = it },
                )
                OverlayDropdownPreference(
                    title = "Disabled Dropdown",
                    items = listOf("Option 1"),
                    selectedIndex = 0,
                    onSelectedIndexChange = {},
                    enabled = false,
                )
            }
        }

        item(key = "spinner") {
            var overlaySelected by remember { mutableIntStateOf(0) }
            var windowSelected by remember { mutableIntStateOf(1) }
            val spinnerOptions = remember {
                listOf(
                    DropdownItem(text = "Red", summary = "Color 1"),
                    DropdownItem(text = "Green", summary = "Color 2"),
                    DropdownItem(text = "Blue", summary = "Color 3"),
                    DropdownItem(text = "Yellow", summary = "Color 4"),
                )
            }

            SmallTitle(text = "Spinner")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                OverlaySpinnerPreference(
                    title = "Spinner (O)",
                    items = spinnerOptions,
                    selectedIndex = overlaySelected,
                    onSelectedIndexChange = { overlaySelected = it },
                )
                WindowSpinnerPreference(
                    title = "Spinner (W)",
                    items = spinnerOptions,
                    selectedIndex = windowSelected,
                    onSelectedIndexChange = { windowSelected = it },
                )
                OverlaySpinnerPreference(
                    title = "Disabled Spinner",
                    items = listOf(DropdownItem(text = "N/A")),
                    selectedIndex = 0,
                    onSelectedIndexChange = {},
                    enabled = false,
                )
            }
        }

        item(key = "snackbar") {
            val scope = rememberCoroutineScope()

            SmallTitle(text = "Snackbar")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        TextButton(
                            text = "Short",
                            onClick = { scope.launch { snackbarHostState.showSnackbar("This is a short message.") } },
                            modifier = Modifier.weight(1f),
                        )
                        TextButton(
                            text = "Long",
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "This is a longer message.",
                                        duration = SnackbarDuration.Long,
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        var actionText by remember { mutableStateOf("Action") }
                        TextButton(
                            text = actionText,
                            onClick = {
                                scope.launch {
                                    actionText = "Action: Alive"
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Message with action button.",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short,
                                    )
                                    actionText = when (result) {
                                        SnackbarResult.ActionPerformed -> "Action: Undo"
                                        else -> "Action: Expired"
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColorsPrimary(),
                        )
                        TextButton(
                            text = "Dismissible",
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Tap close to dismiss.",
                                        withDismissAction = true,
                                        duration = SnackbarDuration.Long,
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }

        item(key = "textField") {
            var text1 by remember { mutableStateOf("") }
            var text2 by remember { mutableStateOf("") }

            SmallTitle(text = "TextField")
            TextField(
                value = text1,
                onValueChange = { text1 = it },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 6.dp),
            )
            TextField(
                value = text2,
                onValueChange = { text2 = it },
                label = "With label",
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            )
        }

        item(key = "progressIndicator") {
            SmallTitle(text = "ProgressIndicator")
            val animatedProgress by rememberInfiniteTransition().animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(animation = tween(1000), repeatMode = RepeatMode.Reverse),
            )
            val staticValues = listOf(0.0f, 0.25f, 0.5f, 0.75f, 1.0f, null)

            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .padding(bottom = 6.dp),
            )
            staticValues.forEach { value ->
                LinearProgressIndicator(
                    progress = value,
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .padding(bottom = 6.dp),
                )
            }
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                CircularProgressIndicator(progress = animatedProgress)
                staticValues.forEach { value -> CircularProgressIndicator(progress = value) }
                InfiniteProgressIndicator(modifier = Modifier.align(Alignment.CenterVertically))
            }
        }

        item(key = "slider") {
            var sliderValue by remember { mutableFloatStateOf(0.3f) }
            var stepsValue by remember { mutableFloatStateOf(100f) }
            var rangeValue by remember { mutableStateOf(0.2f..0.8f) }

            SmallTitle(text = "Slider")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                SliderPreference(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    title = "Normal",
                    valueText = "${(sliderValue * 100).toInt()}%",
                    insideMargin = PaddingValues(16.dp, 16.dp, 16.dp, 0.dp),
                )
                SliderPreference(
                    value = stepsValue,
                    onValueChange = { stepsValue = it },
                    title = "Steps",
                    valueText = "${stepsValue.toInt()}/200",
                    valueRange = 0f..200f,
                    steps = 199,
                    insideMargin = PaddingValues(16.dp, 16.dp, 16.dp, 0.dp),
                )
            }
            SmallTitle(text = "RangeSlider")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                RangeSliderPreference(
                    value = rangeValue,
                    onValueChange = { rangeValue = it },
                    title = "Range",
                    valueText = "${(rangeValue.start * 100).toInt()}% - ${(rangeValue.endInclusive * 100).toInt()}%",
                    insideMargin = PaddingValues(16.dp, 16.dp, 16.dp, 0.dp),
                )
            }
            SmallTitle(text = "VerticalSlider")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    var verticalValue by remember { mutableFloatStateOf(0.3f) }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        VerticalSlider(value = verticalValue, onValueChange = { verticalValue = it }, modifier = Modifier.size(25.dp, 160.dp))
                        Text(text = "Normal\n${(verticalValue * 100).toInt()}%", fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                    var verticalSteps by remember { mutableFloatStateOf(5f) }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        VerticalSlider(
                            value = verticalSteps,
                            onValueChange = { verticalSteps = it },
                            valueRange = 0f..6f,
                            steps = 5,
                            modifier = Modifier.size(25.dp, 160.dp),
                        )
                        Text(text = "Steps\n${verticalSteps.toInt()}/6", fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }

        item(key = "card") {
            SmallTitle(text = "Card")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 6.dp),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryVariant),
                insideMargin = PaddingValues(16.dp),
                pressFeedbackType = PressFeedbackType.None,
                showIndication = true,
            ) {
                Text(color = MiuixTheme.colorScheme.onPrimaryVariant, text = "Primary Card", fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
                Text(color = MiuixTheme.colorScheme.onPrimaryVariant, text = "ShowIndication: true", fontSize = 17.sp)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    insideMargin = PaddingValues(16.dp),
                    pressFeedbackType = PressFeedbackType.Sink,
                    onClick = {},
                ) {
                    Text(text = "Card", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Text(text = "Type: Sink", style = MiuixTheme.textStyles.paragraph, color = MiuixTheme.colorScheme.onSurfaceVariantSummary)
                }
                Card(
                    modifier = Modifier.weight(1f),
                    insideMargin = PaddingValues(16.dp),
                    pressFeedbackType = PressFeedbackType.Tilt,
                ) {
                    Text(text = "Card", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Text(text = "Type: Tilt", style = MiuixTheme.textStyles.paragraph, color = MiuixTheme.colorScheme.onSurfaceVariantSummary)
                }
            }
        }

        item(key = "tooltip") {
            val richState = rememberTooltipState(isPersistent = true)

            SmallTitle(text = "Tooltip")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    TooltipBox(text = "Edit") {
                        IconButton(onClick = {}) {
                            Icon(imageVector = MiuixIcons.Edit, contentDescription = "Edit")
                        }
                    }
                    RichTooltipBox(
                        title = "Rich tooltip",
                        text = "Rich tooltips show a title, text, and an optional action.",
                        actionText = "Got it",
                        onActionClick = {},
                        state = richState,
                    ) {
                        IconButton(onClick = {}) {
                            Icon(imageVector = MiuixIcons.Info, contentDescription = "Info")
                        }
                    }
                }
            }
        }

        item(key = "badge") {
            SmallTitle(text = "Badge")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    BadgedBox(badge = { Badge() }) {
                        Icon(imageVector = MiuixIcons.Messages, contentDescription = "Messages", modifier = Modifier.size(28.dp))
                    }
                    BadgedBox(badge = { Badge { Text("8") } }) {
                        Icon(imageVector = MiuixIcons.Email, contentDescription = "Email", modifier = Modifier.size(28.dp))
                    }
                    BadgedBox(badge = { Badge { Text("99+") } }) {
                        Icon(imageVector = MiuixIcons.Settings, contentDescription = "Settings", modifier = Modifier.size(28.dp))
                    }
                    BadgedBox(badge = { Badge { Text("5") } }) {
                        Icon(imageVector = MiuixIcons.Favorites, contentDescription = "Favorites", modifier = Modifier.size(28.dp))
                    }
                }
            }
        }

        item(key = "numberPicker") {
            var hourValue by remember { mutableIntStateOf(16) }
            var minuteValue by remember { mutableIntStateOf(30) }

            SmallTitle(text = "NumberPicker")
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NumberPicker(
                        value = hourValue,
                        onValueChange = { hourValue = it },
                        range = 0..23,
                        label = { it.toString().padStart(2, '0') },
                        wrapAround = true,
                        modifier = Modifier.weight(1f),
                    )
                    Text(text = ":", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    NumberPicker(
                        value = minuteValue,
                        onValueChange = { minuteValue = it },
                        range = 0..59,
                        label = { it.toString().padStart(2, '0') },
                        wrapAround = true,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        item(key = "colorPicker") {
            val initialColor = MiuixTheme.colorScheme.primary
            var selectedColor by remember { mutableStateOf(initialColor) }
            var colorHex by remember(selectedColor) {
                mutableStateOf(selectedColor.toArgb().toHexString(HexFormat.UpperCase))
            }

            SmallTitle(text = "ColorPicker")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                insideMargin = PaddingValues(16.dp),
            ) {
                Text(
                    text = "RGBA: ${(selectedColor.red * 255).toInt()}, " +
                        "${(selectedColor.green * 255).toInt()}, " +
                        "${(selectedColor.blue * 255).toInt()}, " +
                        "${round(selectedColor.alpha * 100) / 100.0}",
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                ColorPicker(
                    color = selectedColor,
                    onColorChanged = { selectedColor = it },
                    colorSpace = ColorSpace.HSV,
                    showPreview = false,
                )
                TextField(
                    value = colorHex,
                    onValueChange = { newHex ->
                        if (newHex.length <= 8 && newHex.all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }) {
                            val upperHex = newHex.uppercase()
                            colorHex = upperHex
                            if (newHex.length == 8) {
                                selectedColor = Color(upperHex.toUInt(16).toInt())
                            }
                        }
                    },
                    leadingIcon = { Text("HEX: #", modifier = Modifier.padding(start = 16.dp)) },
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }

        item(key = "colorPalette") {
            val initialColor = MiuixTheme.colorScheme.primary
            var selectedColor by remember { mutableStateOf(initialColor) }

            SmallTitle(text = "ColorPalette")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                insideMargin = PaddingValues(16.dp),
            ) {
                ColorPalette(
                    color = selectedColor,
                    onColorChanged = { selectedColor = it },
                    showPreview = false,
                )
            }
        }

        item(key = "endSpacer") {
            Spacer(Modifier.padding(bottom = 24.dp))
        }
    }
}
