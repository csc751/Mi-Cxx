package top.yukonga.miuix.micxx.storage

import android.content.Context
import top.yukonga.miuix.micxx.data.CodeFile
import top.yukonga.miuix.micxx.data.Language
import top.yukonga.miuix.micxx.data.Project
import java.io.File

object ProjectManager {

    private const val ROOT_DIR = "projects"

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
        ensureRootDir()
    }

    private fun rootDir(): File {
        return File(appContext.filesDir, ROOT_DIR)
    }

    private fun ensureRootDir() {
        val root = rootDir()
        if (!root.exists()) root.mkdirs()
    }

    fun getProjects(): List<Project> {
        ensureRootDir()
        return rootDir().listFiles()
            ?.filter { it.isDirectory }
            ?.map { Project(name = it.name, path = it.absolutePath) }
            ?: emptyList()
    }

    fun createProject(name: String): Project {
        ensureRootDir()
        val projectDir = File(rootDir(), name)
        if (!projectDir.exists()) projectDir.mkdirs()
        // Create a default main.cpp
        val mainFile = File(projectDir, "main.cpp")
        if (!mainFile.exists()) {
            mainFile.writeText(DEFAULT_CPP_TEMPLATE)
        }
        return Project(name = name, path = projectDir.absolutePath)
    }

    fun deleteProject(name: String) {
        val projectDir = File(rootDir(), name)
        if (projectDir.exists()) {
            projectDir.deleteRecursively()
        }
    }

    fun getFiles(projectName: String): List<CodeFile> {
        val projectDir = File(rootDir(), projectName)
        if (!projectDir.exists()) return emptyList()
        return projectDir.listFiles()
            ?.filter { it.isFile }
            ?.sortedBy { it.name }
            ?.map { f ->
                CodeFile(
                    name = f.name,
                    path = f.absolutePath,
                    content = f.readText(),
                    language = Language.fromFileName(f.name),
                )
            }
            ?: emptyList()
    }

    fun createFile(projectName: String, fileName: String): CodeFile {
        val projectDir = File(rootDir(), projectName)
        if (!projectDir.exists()) projectDir.mkdirs()
        val file = File(projectDir, fileName)
        if (!file.exists()) {
            val template = when (Language.fromFileName(fileName)) {
                Language.C -> DEFAULT_C_TEMPLATE
                Language.HEADER -> DEFAULT_HEADER_TEMPLATE
                Language.CPP -> if (fileName.startsWith("main")) "" else ""
            }
            file.writeText(template)
        }
        return CodeFile(
            name = file.name,
            path = file.absolutePath,
            content = file.readText(),
            language = Language.fromFileName(file.name),
        )
    }

    fun deleteFile(path: String) {
        val file = File(path)
        if (file.exists()) file.delete()
    }

    fun renameFile(path: String, newName: String): String {
        val file = File(path)
        val newFile = File(file.parentFile, newName)
        if (file.exists()) file.renameTo(newFile)
        return newFile.absolutePath
    }

    fun saveFile(path: String, content: String) {
        val file = File(path)
        file.writeText(content)
    }

    fun readFile(path: String): String {
        val file = File(path)
        return if (file.exists()) file.readText() else ""
    }

    const val DEFAULT_CPP_TEMPLATE = """#include <iostream>
using namespace std;

int main() {
    cout << "Hello, Mi Cxx!" << endl;
    return 0;
}
"""

    const val DEFAULT_C_TEMPLATE = """#include <stdio.h>

int main() {
    printf("Hello, Mi Cxx!\n");
    return 0;
}
"""

    const val DEFAULT_HEADER_TEMPLATE = """#pragma once

"""
}
