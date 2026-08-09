// Copyright 2026, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package top.yukonga.miuix.micxx.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import top.yukonga.miuix.kmp.basic.Text

@Composable
fun CodeEditor(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    theme: CodeColorScheme = CodeThemes.dark,
    fontSizeSp: Float = 14f,
    tabWidth: Int = 4,
    enabled: Boolean = true,
) {
    var textFieldValue by remember(text) {
        mutableStateOf(TextFieldValue(text = text))
    }

    val lineCount = remember(textFieldValue.text) {
        if (textFieldValue.text.isEmpty()) 1 else textFieldValue.text.count { it == '\n' } + 1
    }

    val codeScrollState = rememberScrollState()
    val lineNumberScrollState = rememberScrollState()

    LaunchedEffect(codeScrollState.value) {
        lineNumberScrollState.scrollTo(codeScrollState.value)
    }

    val highlightedText = remember(textFieldValue.text, theme) {
        SyntaxHighlighter.highlight(textFieldValue.text, theme)
    }

    val fontFamily = FontFamily.Monospace

    Row(modifier = modifier.fillMaxSize().background(theme.background)) {
        // Line numbers
        Box(
            modifier = Modifier
                .width(44.dp)
                .fillMaxSize()
                .background(theme.background)
                .verticalScroll(lineNumberScrollState)
                .padding(top = 8.dp, start = 4.dp, end = 4.dp),
            contentAlignment = Alignment.TopEnd,
        ) {
            Column {
                for (i in 1..lineCount) {
                    Text(
                        text = i.toString(),
                        fontSize = fontSizeSp.sp,
                        color = theme.lineNumber,
                        fontFamily = fontFamily,
                    )
                }
            }
        }

        // Code area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .verticalScroll(codeScrollState),
        ) {
            BasicTextField(
                value = textFieldValue,
                onValueChange = { newTfv ->
                    var processed = newTfv

                    if (newTfv.text.length > textFieldValue.text.length) {
                        val (insertStart, insertEnd, inserted) = findInserted(textFieldValue.text, newTfv.text)
                        if (inserted != null && insertStart >= 0) {
                            // Auto-indent on Enter
                            if (inserted == "\n") {
                                val lineStart = newTfv.text.lastIndexOf('\n', insertStart - 1)
                                val prevLineStart = if (lineStart >= 0) lineStart + 1 else 0
                                val prevLine = newTfv.text.substring(prevLineStart, insertStart)
                                val indent = prevLine.takeWhile { it == ' ' || it == '\t' }
                                val extraIndent = if (prevLine.trimEnd().endsWith("{") || prevLine.trimEnd().endsWith("(")) {
                                    " ".repeat(tabWidth)
                                } else ""
                                val insertion = "\n$indent$extraIndent"
                                val newText = newTfv.text.substring(0, insertStart) +
                                    insertion +
                                    newTfv.text.substring(insertEnd)
                                processed = TextFieldValue(
                                    text = newText,
                                    selection = TextRange(insertStart + insertion.length)
                                )
                            }

                            // Tab to spaces
                            if (inserted == "\t") {
                                val tabSpaces = " ".repeat(tabWidth)
                                val newText = newTfv.text.substring(0, insertStart) +
                                    tabSpaces +
                                    newTfv.text.substring(insertEnd)
                                processed = TextFieldValue(
                                    text = newText,
                                    selection = TextRange(insertStart + tabSpaces.length)
                                )
                            }
                        }
                    }

                    textFieldValue = processed
                    onTextChange(processed.text)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                enabled = enabled,
                textStyle = TextStyle(
                    fontSize = fontSizeSp.sp,
                    color = theme.foreground,
                    fontFamily = fontFamily,
                ),
                cursorBrush = SolidColor(theme.cursor),
                visualTransformation = CodeVisualTransformation(highlightedText),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                ),
            )
        }
    }
}

private fun findInserted(oldText: String, newText: String): Triple<Int, Int, String?> {
    if (newText.length <= oldText.length) return Triple(-1, -1, null)

    var prefixLen = 0
    while (prefixLen < oldText.length && prefixLen < newText.length &&
        oldText[prefixLen] == newText[prefixLen]
    ) {
        prefixLen++
    }

    var suffixLen = 0
    while (suffixLen < oldText.length - prefixLen &&
        suffixLen < newText.length - prefixLen &&
        oldText[oldText.length - 1 - suffixLen] == newText[newText.length - 1 - suffixLen]
    ) {
        suffixLen++
    }

    val insertedStart = prefixLen
    val insertedEnd = newText.length - suffixLen
    if (insertedStart >= insertedEnd) return Triple(-1, -1, null)

    return Triple(insertedStart, insertedEnd, newText.substring(insertedStart, insertedEnd))
}

private class CodeVisualTransformation(
    private val highlighted: AnnotatedString,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(highlighted, OffsetMapping.Identity)
    }
}
