// Copyright 2026, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package top.yukonga.miuix.micxx.compiler

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import top.yukonga.miuix.micxx.data.CompileResult
import top.yukonga.miuix.micxx.data.CompilerMode
import top.yukonga.miuix.micxx.data.WandboxCompiler
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object CompilerService {

    private const val WANDBOX_URL = "https://wandbox.org/api/compile.json"
    private const val TERMUX_PACKAGE = "com.termux"
    private const val TERMUX_RUN_ACTION = "com.termux.RUN_COMMAND"

    suspend fun compile(
        code: String,
        stdin: String,
        mode: CompilerMode,
        compiler: WandboxCompiler,
        context: Context? = null,
    ): CompileResult = withContext(Dispatchers.IO) {
        when (mode) {
            CompilerMode.ONLINE_WANDBOX -> compileWithWandbox(code, stdin, compiler)
            CompilerMode.LOCAL_TERMUX -> compileWithTermux(code, stdin, context)
        }
    }

    private fun compileWithWandbox(
        code: String,
        stdin: String,
        compiler: WandboxCompiler,
    ): CompileResult {
        val startTime = System.currentTimeMillis()
        val compilerId = if (compiler.isCpp && !code.contains("#include <stdio.h>")) {
            compiler.id
        } else if (!compiler.isCpp && code.contains("#include <stdio.h>")) {
            compiler.id
        } else {
            compiler.id
        }

        val jsonBody = JSONObject().apply {
            put("code", code)
            put("compiler", compilerId)
            put("stdin", stdin)
            put("compiler-option-raw", "-Wall -Wextra")
            put("save", false)
        }.toString()

        return try {
            val url = URL(WANDBOX_URL)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 30000
                readTimeout = 30000
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
            }

            OutputStreamWriter(conn.outputStream).use { it.write(jsonBody) }

            val responseCode = conn.responseCode
            val responseBody = if (responseCode in 200..299) {
                conn.inputStream.bufferedReader().use { it.readText() }
            } else {
                conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "HTTP $responseCode"
            }

            conn.disconnect()
            val elapsed = System.currentTimeMillis() - startTime

            val json = JSONObject(responseBody)
            val status = json.optString("status", "1")
            val compilerOutput = json.optString("compiler_output", "") + json.optString("compiler_error", "")
            val programOutput = json.optString("program_output", "")
            val programError = json.optString("program_error", "")
            val exitCode = json.optInt("signal", if (status == "0") 0 else 1)

            CompileResult(
                success = status == "0",
                compilerOutput = compilerOutput,
                programOutput = programOutput,
                programError = programError,
                durationMs = elapsed,
                exitCode = exitCode,
            )
        } catch (e: Exception) {
            CompileResult(
                success = false,
                compilerOutput = "Network error: ${e.message}\n\nMake sure you have an internet connection.",
                programOutput = "",
                programError = "",
                durationMs = System.currentTimeMillis() - startTime,
                exitCode = -1,
            )
        }
    }

    private fun compileWithTermux(
        code: String,
        stdin: String,
        context: Context?,
    ): CompileResult {
        if (context == null) {
            return CompileResult(
                success = false,
                compilerOutput = "Termux mode requires app context.",
                programOutput = "",
                programError = "",
                durationMs = 0,
                exitCode = -1,
            )
        }

        val startTime = System.currentTimeMillis()

        // Write code to a temp file in the app's files dir
        val tmpDir = context.filesDir
        val sourceFile = java.io.File(tmpDir, "micxx_main.cpp")
        sourceFile.writeText(code)

        val isCpp = code.contains("#include <iostream>") ||
            code.contains("#include <vector>") ||
            code.contains("cout") ||
            code.contains("std::")

        val compiler = if (isCpp) "g++" else "gcc"
        val outputFile = java.io.File(tmpDir, "micxx_main")
        val compileCmd = "$compiler -o $outputFile ${sourceFile.absolutePath} -Wall -Wextra"
        val runCmd = "$outputFile"

        try {
            // Try to launch Termux with the compile command
            val intent = Intent(TERMUX_RUN_ACTION).apply {
                setClassName(TERMUX_PACKAGE, "com.termux.app.RunCommandService")
                putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/bash")
                putExtra("com.termux.RUN_COMMAND_ARGUMENTS", arrayOf("-c", "$compileCmd && echo '---COMPILE OK---' && $runCmd || echo '---COMPILE FAILED---'"))
                putExtra("com.termux.RUN_COMMAND_BACKGROUND", false)
                putExtra("com.termux.RUN_COMMAND_SESSION_ACTION", "0")
            }
            context.startService(intent)

            return CompileResult(
                success = true,
                compilerOutput = "Code sent to Termux.\nCompiler: $compiler\nFile: ${sourceFile.absolutePath}\n\nCheck Termux for compilation and output.",
                programOutput = "",
                programError = "",
                durationMs = System.currentTimeMillis() - startTime,
                exitCode = 0,
            )
        } catch (e: Exception) {
            return CompileResult(
                success = false,
                compilerOutput = "Failed to launch Termux: ${e.message}\n\nMake sure Termux is installed:\nhttps://play.google.com/store/apps/details?id=com.termux\n\nAnd install gcc/g++:\npkg install clang",
                programOutput = "",
                programError = "",
                durationMs = System.currentTimeMillis() - startTime,
                exitCode = -1,
            )
        }
    }

    fun isTermuxInstalled(context: Context): Boolean {
        return try {
            context.packageManager.getPackageInfo(TERMUX_PACKAGE, 0)
            true
        } catch (e: Exception) {
            false
        }
    }
}
