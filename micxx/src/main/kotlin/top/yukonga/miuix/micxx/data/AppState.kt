package top.yukonga.miuix.micxx.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

enum class Language(val ext: String, val displayName: String) {
    C("c", "C"),
    CPP("cpp", "C++"),
    HEADER("h", "Header");

    companion object {
        fun fromFileName(name: String): Language {
            val ext = name.substringAfterLast('.', "").lowercase()
            return when (ext) {
                "c" -> C
                "cpp", "cc", "cxx", "c++" -> CPP
                "h", "hpp", "hh" -> HEADER
                else -> CPP
            }
        }
    }
}

data class CodeFile(
    val name: String,
    val path: String,
    var content: String,
    val language: Language = Language.fromFileName(name),
    var isModified: Boolean = false,
)

data class Project(
    val name: String,
    val path: String,
)

data class CompileResult(
    val success: Boolean,
    val compilerOutput: String,
    val programOutput: String,
    val programError: String,
    val durationMs: Long,
    val exitCode: Int,
)

enum class CompilerMode(val displayName: String) {
    ONLINE_WANDBOX("Online (Wandbox)"),
    LOCAL_TERMUX("Local (Termux)")
}

enum class WandboxCompiler(val id: String, val displayName: String) {
    GCC_LATEST("gcc-head", "GCC (latest)"),
    GCC_13("gcc-13.2.0", "GCC 13.2.0"),
    GCC_12("gcc-12.2.0", "GCC 12.2.0"),
    CLANG_LATEST("clang-head", "Clang (latest)"),
    CLANG_17("clang-17.0.0", "Clang 17.0.0"),
    CPP_GCC_LATEST("g++-head", "G++ (latest)"),
    CPP_GCC_13("g++-13.2.0", "G++ 13.2.0"),
    CPP_CLANG_LATEST("clang++-head", "Clang++ (latest)");

    val isCpp: Boolean get() = id.startsWith("g++") || id.startsWith("clang++")
}

enum class CodeThemeMode(val displayName: String) {
    DARK("Dark"),
    LIGHT("Light"),
    SYSTEM("Follow System")
}

object AppState {
    // --- Projects & Files ---
    var projects by mutableStateOf<List<Project>>(emptyList())
        private set

    var openFiles = mutableStateListOf<CodeFile>()
        private set

    var activeFileIndex by mutableStateOf(-1)
        private set

    // --- Console ---
    var consoleOutput by mutableStateOf("")
        private set

    var programInput by mutableStateOf("")
    var programOutput by mutableStateOf("")
    var programError by mutableStateOf("")

    var isCompiling by mutableStateOf(false)
        private set

    var lastCompileResult by mutableStateOf<CompileResult?>(null)
        private set

    // --- Settings ---
    var compilerMode by mutableStateOf(CompilerMode.ONLINE_WANDBOX)
    var wandboxCompiler by mutableStateOf(WandboxCompiler.CPP_GCC_LATEST)
    var codeThemeMode by mutableStateOf(CodeThemeMode.SYSTEM)
    var fontSize by mutableStateOf(14f)
    var tabWidth by mutableStateOf(4)

    // --- File operations ---
    fun openFile(file: CodeFile) {
        val existing = openFiles.indexOfFirst { it.path == file.path }
        if (existing >= 0) {
            activeFileIndex = existing
        } else {
            openFiles.add(file)
            activeFileIndex = openFiles.lastIndex
        }
    }

    fun closeFile(index: Int) {
        if (index < 0 || index >= openFiles.size) return
        openFiles.removeAt(index)
        activeFileIndex = when {
            openFiles.isEmpty() -> -1
            index <= activeFileIndex -> (activeFileIndex - 1).coerceAtLeast(0)
            else -> activeFileIndex
        }
    }

    fun updateActiveFileContent(newContent: String) {
        if (activeFileIndex < 0 || activeFileIndex >= openFiles.size) return
        openFiles[activeFileIndex].let {
            it.content = newContent
            it.isModified = true
        }
    }

    fun saveActiveFile() {
        if (activeFileIndex < 0 || activeFileIndex >= openFiles.size) return
        openFiles[activeFileIndex].isModified = false
    }

    fun setActiveFile(index: Int) {
        if (index >= 0 && index < openFiles.size) {
            activeFileIndex = index
        }
    }

    // --- Console operations ---
    fun appendConsole(text: String) {
        consoleOutput += text
    }

    fun clearConsole() {
        consoleOutput = ""
        programOutput = ""
        programError = ""
    }

    fun setCompileResult(result: CompileResult) {
        lastCompileResult = result
        isCompiling = false
        consoleOutput += "\n--- Compile ${if (result.success) "succeeded" else "failed"} (${result.durationMs}ms) ---\n"
        if (result.compilerOutput.isNotBlank()) {
            consoleOutput += result.compilerOutput + "\n"
        }
        programOutput = result.programOutput
        programError = result.programError
    }

    fun updateCompilingState(compiling: Boolean) {
        isCompiling = compiling
        if (compiling) {
            consoleOutput += "\n--- Compiling... ---\n"
        }
    }

    fun getActiveFile(): CodeFile? {
        return if (activeFileIndex in openFiles.indices) openFiles[activeFileIndex] else null
    }

    // --- Project operations ---
    fun updateProjects(list: List<Project>) {
        projects = list
    }
}
