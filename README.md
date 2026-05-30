<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Scroll.png">
    <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Scroll.png" width="80">
  </picture>
</p>

<h1 align="center">每日诗文</h1>

<p align="center">
  <b>简洁 · 雅致 · 沉浸</b><br>
  一款 Jetpack Compose 打造的 Android 古诗词应用
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Compose-2026.05.00-2ea44f?logo=jetpackcompose">
  <img src="https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin">
  <img src="https://img.shields.io/badge/minSdk-24-FF6F00?logo=android">
  <img src="https://img.shields.io/badge/targetSdk-36-34A853?logo=android">
  <br>
  <img src="https://github.com/dac114514/daily-poem-android/actions/workflows/android.yml/badge.svg">
</p>

<br>

## ✦ 设计

延续 Claude 温暖编辑风格，以陶土色和暖白为基调，Serif 衬线字体演绎文字之美。

| 色板 | 字体 | 形态 |
|------|------|------|
| 🏺 陶土 `#D97757` | 标题：Serif（衬线） | 18dp 大圆角 |
| 🧈 暖白 `#F5F0EB` | 正文：SansSerif（无衬线） | 温和的阴影 |
| 🌰 深褐 `#5C2A11` | 行距：32sp 舒适间距 | 留白呼吸感 |

<br>

## ✦ 功能

```
┌─────────────────────────────────┐
│  ✦ 每日诗文        [♡] [⚙]    │
├─────────────────────────────────┤
│                                 │
│       ┌─────────────┐           │
│       │  静夜思      │           │
│       │  李白        │           │
│       │             │           │
│       │  床前明月光  │           │
│       │  疑是地上霜  │           │
│       │  举头望明月  │           │
│       │  低头思故乡  │           │
│       └─────────────┘           │
│                                 │
│    [ 🔄 换一首 ]  [ 📤 分享 ]   │
└─────────────────────────────────┘
```

- **📜 每日一诗** — 精选 25 首唐诗宋词，随机呈现
- **🔄 换一首** — 轻点切换，邂逅下一首
- **♡ 收藏** — 心仪的诗句，一键珍藏
- **📤 分享** — 以文字之美，打动更多人
- **📊 统计** — 浏览与收藏数据一览（预设接口）

<br>

## ✦ 技术栈

```
┌─ 表现层 ─────────────────────┐
│  Jetpack Compose  (Material3) │
│  Navigation Compose           │
│  Canvas 图表  (自定义绘制)     │
├─ 状态层 ──────────────────────┤
│  ViewModel + StateFlow        │
│  Flow + collectAsState        │
├─ 数据层 ──────────────────────┤
│  Room   (收藏持久化)           │
│  Gson   (JSON 解析)            │
│  DataStore  (主题设置)         │
└───────────────────────────────┘
```

| 依赖 | 版本 | 用途 |
|------|------|------|
| Kotlin | 2.3.21 | 语言 |
| Compose BOM | 2026.05.00 | UI 框架 |
| Room | 2.7.1 | 本地数据库 |
| Navigation Compose | 2.9.8 | 页面路由 |
| Gson | 2.11.0 | JSON 解析 |
| DataStore | 1.2.1 | 偏好设置 |

<br>

## ✦ 项目结构

```
app/src/main/java/com/example/androidstarter/
├── MainActivity.kt           # 入口 · 主题收集
├── data/
│   ├── model/Poem.kt         # 诗词数据模型 + Room Entity
│   ├── local/
│   │   ├── PoemDao.kt        # Room DAO · 随机/收藏/查询
│   │   ├── AppDatabase.kt    # Room 数据库
│   │   └── ...               # 主题偏好 (DataStore)
│   └── repository/
│       └── PoemRepository.kt # 数据仓库 · JSON 导入
├── ui/
│   ├── home/                 # 首页 · 诗文卡片
│   ├── favorites/            # 我的收藏
│   ├── statistics/           # 数据统计 · 图表
│   ├── settings/             # 设置 · 主题切换
│   ├── components/           # 可复用组件
│   ├── navigation/           # 路由导航
│   └── theme/                # Claude 暖色调主题
└── assets/
    └── poems.json            # 25首古诗词数据
```

<br>

## ✦ 构建

```bash
# Debug APK
./gradlew assembleDebug

# 安装
adb install app/build/outputs/apk/debug/app-debug.apk
```

每次推送自动触发 GitHub Actions 构建，APK 发布至 Releases。

<br>

---

<p align="center">
  <sub>Built with 🩵 and Jetpack Compose</sub>
</p>
