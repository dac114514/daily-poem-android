# Android Template

基于 **Jetpack Compose + Material Design 3** 的 Android 项目初始模板，提供常用组件展示和开箱即用的项目结构。

[![Android CI](https://github.com/dac114514/android-starter/actions/workflows/android.yml/badge.svg)](https://github.com/dac114514/android-starter/actions/workflows/android.yml)

---

## 特性

- **Material 3 蓝色主题** — 完整 light/dark color scheme，支持 Android 12+ Material You 动态取色
- **卡片化组件** — `SettingsCard`、`ExpandableSettingsCard` 可复用 UI 组件
- **底部导航** — 3 个 Tab（组件 / 数据展示 / 设置），Navigation Compose 管理路由
- **ViewModel + StateFlow** — 主题偏好通过 DataStore 持久化，重启保留
- **GitHub Actions CI** — 推送自动触发 `assembleDebug`

## 快速开始

1. **替换包名** — 全局搜索 `com.example.androidstarter` 替换为你的包名
2. **修改应用名** — `app/src/main/res/values/strings.xml`
3. **修改模块配置** — `app/build.gradle.kts` 中的 `namespace` 和 `applicationId`
4. **重命名目录** — `app/src/main/java/com/example/androidstarter/` 重命名为你的包路径
5. **替换图标** — `res/mipmap-*/` 和 `res/drawable/ic_launcher_*`

## 技术栈

| 项目 | 版本 |
|---|---|
| Kotlin | 2.3.21 |
| Android Gradle Plugin | 9.2.0 |
| Compose BOM | 2026.05.00 |
| Navigation Compose | 2.9.8 |
| Lifecycle ViewModel Compose | 2.10.0 |
| DataStore Preferences | 1.2.1 |
| minSdk / targetSdk | 24 / 36 |

## 项目结构

```
app/src/main/java/com/example/androidstarter/
├── MainActivity.kt                  # 入口：底部导航 + 主题收集
├── data/
│   └── local/
│       ├── ThemeMode.kt             # 主题模式枚举
│       └── PreferencesRepository.kt # DataStore 读写封装
└── ui/
    ├── theme/
    │   ├── Color.kt                 # 蓝色调色板（含深色变体）
    │   ├── Theme.kt                 # 主题 + 动态取色
    │   └── Type.kt                  # 字体排版
    ├── navigation/
    │   ├── Routes.kt                # 路由常量
    │   └── AppNavHost.kt            # NavHost 配置
    ├── components/
    │   ├── SettingsCard.kt          # 通用卡片组件
    │   └── ExpandableSettingsCard.kt# 可展开卡片
    ├── widget/
    │   └── WidgetScreen.kt          # 组件展示页
    ├── display/
    │   └── DisplayScreen.kt         # 数据展示页
    └── settings/
        ├── SettingsScreen.kt        # 设置页
        └── SettingsViewModel.kt
```
