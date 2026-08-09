// Copyright 2026, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package top.yukonga.miuix.micxx.editor

import androidx.compose.ui.graphics.Color

data class CodeColorScheme(
    val background: Color,
    val foreground: Color,
    val keyword: Color,
    val string: Color,
    val comment: Color,
    val number: Color,
    val preprocessor: Color,
    val type: Color,
    val function: Color,
    val operator: Color,
    val lineNumber: Color,
    val lineNumberActive: Color,
    val selection: Color,
    val cursor: Color,
    val bracketMatch: Color,
)

object CodeThemes {
    val dark = CodeColorScheme(
        background = Color(0xFF1E1E2E),
        foreground = Color(0xFFCDD6F4),
        keyword = Color(0xFFCBA6F7),
        string = Color(0xFFA6E3A1),
        comment = Color(0xFF6C7086),
        number = Color(0xFFFAB387),
        preprocessor = Color(0xFFF9E2AF),
        type = Color(0xFF89B4FA),
        function = Color(0xFF89DCEB),
        operator = Color(0xFF94E2D5),
        lineNumber = Color(0xFF45475A),
        lineNumberActive = Color(0xFFCBA6F7),
        selection = Color(0x4089B4FA),
        cursor = Color(0xFFF5E0DC),
        bracketMatch = Color(0x60F9E2AF),
    )

    val light = CodeColorScheme(
        background = Color(0xFFFAFAFA),
        foreground = Color(0xFF383A42),
        keyword = Color(0xFFA626A4),
        string = Color(0xFF50A14F),
        comment = Color(0xFFA0A1A7),
        number = Color(0xFFD75F00),
        preprocessor = Color(0xFFC18401),
        type = Color(0xFF4078F2),
        function = Color(0xFF0184BC),
        operator = Color(0xFF383A42),
        lineNumber = Color(0xFFE5E5E5),
        lineNumberActive = Color(0xFFA626A4),
        selection = Color(0x404078F2),
        cursor = Color(0xFF526FFF),
        bracketMatch = Color(0x60C18401),
    )

    fun resolve(isDark: Boolean): CodeColorScheme = if (isDark) dark else light
}
