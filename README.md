# Mi Cxx

> A MIUI-style C/C++ IDE for Android, built on top of the [Miuix](https://github.com/compose-miuix-ui/miuix) Compose Multiplatform UI library.

Mi Cxx 是一款运行在 Android 上的轻量级 C/C++ 集成开发环境，整体设计语言遵循 MIUI 风格，使用 Compose Multiplatform + Miuix 组件库构建。它支持多文件管理、语法高亮、自动缩进、在线编译（Wandbox）以及本地编译（Termux 集成），让你在手机上也能随手写一段 C/C++ 并立即运行。

[![Kotlin](https://img.shields.io/badge/kotlin-2.4.10-7F52FF)](https://kotlinlang.org/)
[![Compose Multiplatform](https://img.shields.io/badge/compose-1.11.1-4285F4)](https://kotlinlang.org/compose-multiplatform/)
[![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)](#)
[![License](https://img.shields.io/github/license/csc751/Mi-Cxx)](LICENSE)
[![Release](https://img.shields.io/badge/release-v1.0.0-blue)](#download)

## Features

- **底部 4 Tab 导航** — Files / Editor / Console / About，符合 MIUI 应用习惯
- **多文件管理** — 创建项目、增删改查 `.c` / `.cpp` / `.h` 文件，文件 Tab 栏快速切换
- **自实现代码编辑器** — 基于 `BasicTextField` 实现，包含：
  - 行号显示（与代码同步滚动）
  - C/C++ 语法高亮（关键字、类型、字符串、注释、数字、预处理指令）
  - 自动缩进（继承上一行缩进，遇 `{` / `(` 自动增加一级）
  - Tab → 空格转换（宽度可配置）
  - 等宽字体，字号可调
- **双编译模式**
  - **在线编译**：调用 [Wandbox](https://wandbox.org/) API，支持 GCC / Clang 多个版本的 C 与 C++ 编译器，无需本地工具链
  - **本地编译**：通过 Intent 调起 [Termux](https://termux.dev/)，在 Termux 中执行 `gcc` / `g++` 并回传结果
- **控制台面板** — 编译器消息、stdout、stderr 分区显示，stdin 输入框，自动滚动到底
- **主题与设置** — 跟随系统 / 浅色 / 深色主题切换，字号、Tab 宽度等可调
- **MIUI 风格图标** — 自绘 "M" 字 Adaptive Icon，支持 monochrome 模式

## Screenshots

应用采用底部 Tab 导航，主要页面：

| Files | Editor | Console | About |
| :---: | :---: | :---: | :---: |
| 项目与文件列表 | 代码编辑器（行号 + 高亮） | 编译输出 + stdin/stdout/stderr | 设置与主题 |

> 截图可后续补充到 `assets/` 目录。

## Download

预构建 APK 已包含在仓库根目录，可直接下载安装：

- **[Mi-Cxx-v1.0.0.apk](./Mi-Cxx-v1.0.0.apk)** (约 11.7 MB)
  - MD5: `2d7ba74cc9ebb1a2936cf965d77370ca`
  - applicationId: `top.yukonga.miuix.micxx`
  - versionName: `1.0.0` / versionCode: `1`
  - 最低支持：Android `minSdk`（见 `BuildConfig`），目标 `targetSdk`

> APK 使用 debug 签名，安装时可能需要允许「未知来源」。如需 release 签名，请自行构建。

## Build

### 环境要求

- JDK 17+（推荐 JBR 21）
- Android SDK（`compileSdk` / `buildToolsVersion` 见 `build-plugins/.../BuildConfig.kt`）
- Gradle 9.7.0（仓库自带 `gradlew` wrapper，无需手动安装）

### 构建步骤

```bash
# 1. 克隆仓库
git clone https://github.com/csc751/Mi-Cxx.git
cd Mi-Cxx

# 2.（可选）配置 Android SDK 路径
cp local.properties.example local.properties
#   然后编辑 local.properties，写入 sdk.dir=/path/to/Android/Sdk

# 3. 构建 Debug APK
./gradlew :micxx:assembleDebug

# 4. 构建 Release APK（使用 debug 签名，便于直接安装）
./gradlew :micxx:assembleRelease
```

构建产物位于：

```
micxx/build/outputs/apk/debug/micxx-debug.apk
micxx/build/outputs/apk/release/micxx-release.apk
```

> 仓库根目录的 `Mi-Cxx-v1.0.0.apk` 即为 `assembleRelease` 产物重命名而来。

## Project Structure

```
Mi-Cxx/
├── micxx/                      # ← Mi Cxx 应用模块（本项目核心）
│   └── src/main/kotlin/top/yukonga/miuix/micxx/
│       ├── MiCxxApp.kt         # 应用入口，底部 4 Tab 导航
│       ├── MainActivity.kt     # Activity 宿主
│       ├── compiler/
│       │   └── CompilerService.kt   # Wandbox + Termux 编译服务
│       ├── editor/
│       │   ├── CodeEditor.kt        # 自实现代码编辑器
│       │   ├── SyntaxHighlighter.kt # C/C++ 语法高亮
│       │   └── CodeTheme.kt         # 编辑器配色
│       ├── pages/
│       │   ├── FilesPage.kt         # 文件管理页
│       │   ├── EditorPage.kt        # 编辑器页
│       │   ├── ConsolePage.kt       # 控制台页
│       │   └── AboutPage.kt         # 设置页
│       ├── storage/
│       │   └── ProjectManager.kt    # 项目/文件持久化
│       └── data/
│           └── AppState.kt          # 全局状态
│
├── miuix-ui/                   # Miuix 核心 UI 组件库（上游源码）
├── miuix-preference/           # Miuix 偏好设置组件
├── miuix-icons/                # Miuix 扩展图标
├── miuix-core/  miuix-blur/  miuix-squircle/  miuix-shader/  miuix-nav/
├── build-plugins/              # Gradle 构建逻辑（BuildConfig 等）
├── gradle/                     # Gradle wrapper + 版本目录
├── settings.gradle.kts         # 已 include(":micxx")
└── Mi-Cxx-v1.0.0.apk           # 预构建 APK
```

Mi Cxx 模块依赖 `miuix-ui`、`miuix-preference`、`miuix-icons` 三个库（见 [micxx/build.gradle.kts](./micxx/build.gradle.kts)），因此本仓库一并包含了 Miuix 上游源码，开箱即可构建，无需额外下载依赖项目。

## Tech Stack

- **Kotlin** 2.4.10 + **Compose Multiplatform** 1.11.1
- **Miuix** UI 组件库（`Scaffold` / `NavigationBar` / `TopAppBar` / `Card` / `Button` / `SmallTitle` 等）
- **AndroidX Activity Compose**
- 编译后端：[Wandbox API](https://github.com/melpon/wandbox) (在线) + [Termux](https://termux.dev/) (本地)
- 自实现编辑器：`BasicTextField` + `VisualTransformation` + 正则语法高亮

## Acknowledgements

- [Miuix](https://github.com/compose-miuix-ui/miuix) — 本应用所使用的 Compose Multiplatform UI 组件库，MIUI 设计语言
- [Wandbox](https://wandbox.org/) — 在线编译服务
- [Termux](https://termux.dev/) — Android 上的本地 Linux 环境与编译工具链

## License

继承自上游 Miuix 项目，采用 [Apache License 2.0](./LICENSE)。
