<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Scroll.png">
    <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Scroll.png" width="80">
  </picture>
</p>

<h1 align="center">每日诗文 · Daily Poem</h1>

<p align="center">
  <b>简洁 · 雅致 · 沉浸</b><br>
  一款 Jetpack Compose 打造的 Android 古诗词应用<br>
  <sub>A minimalist Android app for classical Chinese poems, built with Jetpack Compose</sub>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Compose-2026.05.00-2ea44f?logo=jetpackcompose">
  <img src="https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin">
  <img src="https://img.shields.io/badge/minSdk-24-FF6F00?logo=android">
  <img src="https://img.shields.io/badge/targetSdk-36-34A853?logo=android">
  <img src="https://img.shields.io/badge/license-Apache--2.0-blue">
  <br>
  <img src="https://github.com/dac114514/daily-poem-android/actions/workflows/android.yml/badge.svg">
  <a href="https://github.com/dac114514/daily-poem-android/releases/latest"><img src="https://img.shields.io/github/v/release/dac114514/daily-poem-android?include_prereleases&label=download"></a>
</p>

<p align="center">
  <a href="https://github.com/dac114514/daily-poem-android/releases/latest">📥 下载最新 APK</a>
  ·
  <a href="https://github.com/dac114514/daily-poem-android/issues/new">🐛 提 Issue</a>
  ·
  <a href="https://github.com/dac114514/daily-poem-android/stargazers">⭐ Star</a>
</p>

<br>

## ✦ 简介 · Introduction

> 每日诗文,是一款面向古诗词爱好者的轻量级 Android 应用。打开它,邂逅一首诗,遇见一段字。每天 30 秒,让文字之美融入生活。
>
> Daily Poem is a lightweight Android application for poetry lovers. Open it, meet a poem, encounter a piece of writing. Just 30 seconds a day, let the beauty of words become part of your life.

## ✦ 特性 · Features

- **📜 每日一诗** — 精选 25 首唐诗宋词,随机呈现
- **🔄 换一首** — 轻点切换,邂逅下一首(当前诗自动保存,旋转不丢)
- **♡ 收藏** — 心仪的诗句,一键珍藏(Room 持久化)
- **📤 图片分享** — 渲染为精美图片(陶土色装饰 + 朝代水印),通过任意 IM 分享
- **📊 真实统计** — 浏览次数、周活跃、月度热力图,自动记录你的阅读节奏
- **🌗 双主题** — 跟随系统 / 浅色 / 深色,陶土暖色基调
- **📐 Edge-to-Edge** — 沉浸式布局,适配全面屏
- **🪶 离线优先** — 内置资产,无需网络即可阅读全部 25 首

## ✦ 预览 · Preview

<!-- TODO: 添加截图。准备两张 PNG 放入 docs/preview-home.png 与 docs/preview-stats.png -->

| 首页 | 收藏 | 统计 |
|:---:|:---:|:---:|
| ![Home](docs/preview-home.png) | ![Favorites](docs/preview-favorites.png) | ![Statistics](docs/preview-stats.png) |
| _每日一诗_ | _我的收藏_ | _数据统计_ |

> 截图 TODO:把 `app-debug.apk` 安装到设备截屏后放到 `docs/preview-*.png`

## ✦ 设计 · Design

延续 Claude 温暖编辑风格,以陶土色和暖白为基调,Serif 衬线字体演绎文字之美。

| 色板 | 字体 | 形态 |
|------|------|------|
| 🏺 陶土 `#D97757` | 标题:Serif(衬线) | 18dp 大圆角 |
| 🧈 暖白 `#F5F0EB` | 正文:SansSerif(无衬线) | 温和的阴影 |
| 🌰 深褐 `#5C2A11` | 行距:32sp 舒适间距 | 留白呼吸感 |
| 🍂 暗褐 `#1A1816` | 诗词:Serif 18sp | 朝代大字水印 |

完整 Light/Dark 双套色板定义在 `ui/theme/Color.kt`,字号阶梯在 `ui/theme/Type.kt`。

## ✦ 技术栈 · Tech Stack

```
┌─ 表现层 ─────────────────────┐
│  Jetpack Compose (Material3)  │
│  Navigation Compose           │
│  Canvas 图表 (自绘)            │
├─ 状态层 ──────────────────────┤
│  ViewModel + StateFlow        │
│  SavedStateHandle (状态保留)    │
│  Flow + collectAsState         │
├─ 数据层 ──────────────────────┤
│  Room    (诗词 + 收藏)         │
│  DataStore Preferences (主题)  │
│  DataStore Preferences (统计)  │
│  Gson    (JSON 解析)           │
└───────────────────────────────┘
```

| 依赖 | 版本 | 用途 |
|------|------|------|
| Kotlin | 2.3.21 | 语言 |
| Compose BOM | 2026.05.00 | UI 框架 |
| Room | 2.7.1 | 本地数据库(诗词 + 收藏) |
| Navigation Compose | 2.9.8 | 页面路由 |
| DataStore Preferences | 1.2.1 | 主题 + 统计数据 |
| Gson | 2.11.0 | JSON 解析 |
| KSP | 2.3.8 | 代码生成 |
| AGP | 9.2.0 | 构建工具 |

## ✦ 项目结构 · Project Layout

```
app/src/main/java/com/claude/poem/
├── MainActivity.kt            # 入口 · 主题收集 · Edge-to-Edge
├── data/
│   ├── model/Poem.kt          # 诗词数据模型 + Room Entity
│   ├── local/
│   │   ├── AppDatabase.kt     # Room 数据库
│   │   ├── PoemDao.kt         # 随机 / 收藏 / 查询
│   │   ├── PreferencesRepository.kt  # 主题 (DataStore)
│   │   ├── StatsRepository.kt # 浏览/收藏统计 (DataStore)
│   │   └── ThemeMode.kt
│   └── repository/
│       └── PoemRepository.kt  # 数据仓库 · JSON 导入
├── ui/
│   ├── home/                  # 首页 · 诗文卡片 · 图片分享渲染
│   ├── favorites/             # 我的收藏 · 弹窗预览
│   ├── statistics/            # 数据统计 · 折线 + 热力
│   ├── settings/              # 设置 · 主题 / 关于
│   ├── components/            # PoemCard / WeekChart / CalendarHeatmap / SettingsCard
│   ├── navigation/            # NavHost + Routes
│   └── theme/                 # Claude 暖色调主题
└── assets/
    └── poems.json             # 25 首古诗词 (UTF-8)
```

## ✦ 快速开始 · Getting Started

### 前置要求

- **JDK 17**(Temurin 推荐)
- **Android Studio Koala (2024.1.1)** 或更新版本
- **Android SDK 36** + Build-Tools
- 设备或模拟器:**Android 7.0 (API 24)** 及以上

### 克隆与构建

```bash
git clone https://github.com/dac114514/daily-poem-android.git
cd daily-poem-android

# Debug APK
./gradlew assembleDebug

# 安装到已连接设备
adb install app/build/outputs/apk/debug/app-debug.apk

# Release APK (启用 R8 + 资源压缩)
./gradlew assembleRelease
```

### 调试提示

- 首次启动时,首次切换诗词会写入浏览统计,可在「数据统计」页查看
- 旋转屏幕时当前诗不会丢失(`SavedStateHandle`)
- 主题切换实时生效,跨进程持久化

## ✦ 路线图 · Roadmap

### ✓ 已完成

- [x] Jetpack Compose + Material 3 主体
- [x] Room 持久化诗词与收藏
- [x] DataStore 主题模式(浅/深/系统)
- [x] 图片分享(Canvas 渲染 + 朝代水印)
- [x] 收藏弹窗预览 + 设置可展开卡片
- [x] 暗色模式完整配色
- [x] 月度热力图 + 周活跃折线图
- [x] GitHub Actions 自动构建 + Release

### → 近期

- [x] 浏览统计真实化(DataStore 累计 + 周桶 + 月活跃)
- [x] SavedStateHandle 保留当前诗
- [x] R8 minify + 资源压缩
- [x] 清理项目内残留的 `androidstarter` 命名
- [x] Apache-2.0 许可证
- [ ] 单元测试覆盖(Repository / ViewModel)

### ◆ 中期

- [ ] 诗词扩展(目标 500+ 首,按朝代/作者/标签筛选)
- [ ] tags 字段 UI 落地(分类/搜索/筛选)
- [ ] 分享背景模板选择(纯色 / 山水纹理)
- [ ] 桌面快捷方式(Quick Tile)
- [ ] Material You 动态取色

### ◇ 远期

- [ ] 每日定时推送(WorkManager + 通知)
- [ ] 云同步收藏(账号体系)
- [ ] Wear OS 表盘组件
- [ ] 跨平台(Desktop / iOS via KMP)

## ✦ FAQ

**Q: 数据存在哪里?**
A: 诗词与收藏存 Room(`daily_poem_db`),主题与统计存 DataStore Preferences。卸载 App 数据清空。

**Q: 怎么换主题?**
A: 设置 → 外观 → 主题模式,可选「跟随系统 / 浅色 / 深色」。

**Q: 分享为什么是图片不是文字?**
A: 文字在 IM 客户端排版易乱,图片更稳定美观,且本应用有朝代水印装饰,出图更有"诗"的氛围。

**Q: 怎么添加一首诗?**
A: 编辑 `app/src/main/assets/poems.json`,按现有格式加一条,然后卸载重装(或清数据)让 Room 重新初始化。

**Q: 怎么贡献?**
A: Fork → 创建分支 → 提交 PR。改动前建议先看 `docs/superpowers/specs/` 的设计文档,保持设计语言一致。

**Q: Release APK 在哪?**
A: 每次推送到 main 分支,GitHub Actions 会自动构建 Debug APK 并发布到 [Releases/latest](https://github.com/dac114514/daily-poem-android/releases/latest)。

## ✦ 致谢 · Acknowledgements

- **[Jetpack Compose](https://developer.android.com/jetpack/compose)** — Google 现代 UI 工具包
- **[Material 3](https://m3.material.io/)** — 设计语言与组件
- **[Material Icons Extended](https://developer.android.com/reference/kotlin/androidx/compose/material/icons/Icons)** — 图标库
- **[Room](https://developer.android.com/training/data-storage/room)** — 本地数据库
- **[DataStore](https://developer.android.com/topic/libraries/architecture/datastore)** — 键值持久化
- **[Gson](https://github.com/google/gson)** — JSON 解析
- **Claude 编辑风格** — 配色与排版灵感
- 所有为本项目点过 ⭐ Star 的朋友

## ✦ 许可证 · License

```
Copyright 2026 dac114514

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```

详见 [LICENSE](LICENSE) 文件。

## ✦ 链接 · Links

- 📦 [Releases](https://github.com/dac114514/daily-poem-android/releases)
- 🐛 [Issues](https://github.com/dac114514/daily-poem-android/issues)
- 💬 [Discussions](https://github.com/dac114514/daily-poem-android/discussions)
- ⭐ [Star History](https://star-history.com/#dac114514/daily-poem-android)

---

<p align="center">
  <sub>Built with 🩵 and Jetpack Compose</sub>
  <br>
  <sub><a href="https://github.com/dac114514/daily-poem-android">每日诗文</a> · Apache-2.0 · 2026</sub>
</p>
