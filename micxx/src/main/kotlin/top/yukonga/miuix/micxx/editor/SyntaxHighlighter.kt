package top.yukonga.miuix.micxx.editor

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

object SyntaxHighlighter {

    private val keywords = setOf(
        // C keywords
        "auto", "break", "case", "char", "const", "continue", "default", "do",
        "double", "else", "enum", "extern", "float", "for", "goto", "if",
        "inline", "int", "long", "register", "restrict", "return", "short",
        "signed", "sizeof", "static", "struct", "switch", "typedef", "union",
        "unsigned", "void", "volatile", "while",
        // C++ keywords
        "alignas", "alignof", "and", "asm", "auto", "bool", "catch", "class",
        "compl", "concept", "constexpr", "const_cast", "co_await", "co_return",
        "co_yield", "decltype", "delete", "dynamic_cast", "explicit", "export",
        "false", "final", "friend", "mutable", "namespace", "new", "noexcept",
        "nullptr", "operator", "override", "private", "protected", "public",
        "reinterpret_cast", "requires", "static_assert", "static_cast",
        "template", "this", "thread_local", "throw", "true", "try", "typeid",
        "typename", "using", "virtual", "wchar_t",
    )

    private val types = setOf(
        "size_t", "ssize_t", "ptrdiff_t", "uint8_t", "uint16_t", "uint32_t",
        "uint64_t", "int8_t", "int16_t", "int32_t", "int64_t", "FILE", "NULL",
        "std", "string", "vector", "map", "set", "unordered_map", "unordered_set",
        "list", "deque", "queue", "stack", "pair", "tuple", "array",
        "shared_ptr", "unique_ptr", "weak_ptr", "function", "optional",
        "variant", "any", "cin", "cout", "cerr", "endl", "string_view",
        "wstring", "size_type", "iterator", "const_iterator",
        "printf", "scanf", "malloc", "free", "calloc", "realloc",
        "fopen", "fclose", "fgets", "fputs", "fprintf", "fscanf",
        "strlen", "strcpy", "strcat", "strcmp", "memcpy", "memset",
        "abs", "exit", "atoi", "atof", "system", "sprintf", "sscanf",
    )

    private val keywordPattern = Regex("\\b(${keywords.joinToString("|")})\\b")
    private val typePattern = Regex("\\b(${types.joinToString("|")})\\b")
    private val numberPattern = Regex("\\b(0[xX][0-9a-fA-F]+|0[bB][01]+|\\d+\\.?\\d*([eE][+-]?\\d+)?[fFuUlL]*)\\b")
    private val stringPattern = Regex("\"(?:\\\\.|[^\"\\\\])*\"")
    private val charPattern = Regex("'(?:\\\\.|[^'\\\\])'")
    private val lineCommentPattern = Regex("//[^\\n]*")
    private val blockCommentPattern = Regex("/\\*[\\s\\S]*?\\*/")
    private val preprocessorPattern = Regex("^\\s*#[^\\n]*", setOf(RegexOption.MULTILINE))
    private val functionPattern = Regex("\\b([a-zA-Z_]\\w*)\\s*(?=\\()")

    fun highlight(code: String, theme: CodeColorScheme): AnnotatedString {
        if (code.isEmpty()) return AnnotatedString("")

        // Collect all matches with their positions
        data class Match(val start: Int, val end: Int, val style: SpanStyle)

        val matches = mutableListOf<Match>()

        // Block comments first (they can span multiple lines and override others)
        blockCommentPattern.findAll(code).forEach {
            matches.add(Match(it.range.first, it.range.last + 1,
                SpanStyle(color = theme.comment, fontStyle = FontStyle.Italic)))
        }

        // Line comments
        lineCommentPattern.findAll(code).forEach {
            matches.add(Match(it.range.first, it.range.last + 1,
                SpanStyle(color = theme.comment, fontStyle = FontStyle.Italic)))
        }

        // Strings
        stringPattern.findAll(code).forEach {
            matches.add(Match(it.range.first, it.range.last + 1,
                SpanStyle(color = theme.string)))
        }

        // Chars
        charPattern.findAll(code).forEach {
            matches.add(Match(it.range.first, it.range.last + 1,
                SpanStyle(color = theme.string)))
        }

        // Preprocessor
        preprocessorPattern.findAll(code).forEach {
            matches.add(Match(it.range.first, it.range.last + 1,
                SpanStyle(color = theme.preprocessor)))
        }

        // Keywords
        keywordPattern.findAll(code).forEach {
            matches.add(Match(it.range.first, it.range.last + 1,
                SpanStyle(color = theme.keyword, fontWeight = FontWeight.Bold)))
        }

        // Types / known identifiers
        typePattern.findAll(code).forEach {
            matches.add(Match(it.range.first, it.range.last + 1,
                SpanStyle(color = theme.type)))
        }

        // Numbers
        numberPattern.findAll(code).forEach {
            matches.add(Match(it.range.first, it.range.last + 1,
                SpanStyle(color = theme.number)))
        }

        // Functions (identifier followed by '(') — only if not already matched
        functionPattern.findAll(code).forEach { mr ->
            val nameStart = mr.range.first
            val nameEnd = mr.range.last + 1
            // Skip if already covered by keyword/type/preprocessor
            val overlap = matches.any { it.start <= nameStart && it.end >= nameEnd }
            if (!overlap) {
                matches.add(Match(nameStart, nameEnd,
                    SpanStyle(color = theme.function)))
            }
        }

        // Sort by start position, remove overlaps (earlier match wins)
        matches.sortBy { it.start }
        val filtered = mutableListOf<Match>()
        var lastEnd = 0
        for (m in matches) {
            if (m.start >= lastEnd) {
                filtered.add(m)
                lastEnd = m.end
            }
        }

        return buildAnnotatedString {
            append(code)
            for (m in filtered) {
                addStyle(m.style, m.start, m.end)
            }
        }
    }

    fun getLineCount(text: String): Int {
        if (text.isEmpty()) return 1
        return text.count { it == '\n' } + 1
    }
}
