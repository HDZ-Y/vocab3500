# 专升本 3500 词 · Android 版

单文件网页「专升本英语 3500 词背诵台」的 Android 套壳工程。网页内容在 `www/index.html`，原生层只做两件事：提供启动壳、提供系统 TTS 朗读（Android WebView 不支持网页朗读接口）。

## 出包三步（不用在本机装任何工具）

1. 打开 <https://github.com/new> 新建仓库，名字随意（如 `vocab3500`），选 **Private** 或 Public，不要勾选 README。
2. 把本压缩包解压后，**把里面的全部文件和文件夹**（包括 `.github`、`scripts`、`www`）拖到仓库网页的 "uploading an existing file" 区域，提交。
3. 打开仓库的 **Actions** 标签页 → 左侧 `Build APK` → 右侧 **Run workflow** → 等 5～8 分钟，绿灯后点进这次运行，页面底部 **Artifacts** 里下载 `vocab3500-apk.zip`，解压得到 `专升本3500词.apk`。

> `.github/workflows/build-apk.yml` 必须一起上传，否则没有构建按钮。GitHub 网页上传对隐藏文件夹不友好时，用 GitHub Desktop 或 `git push` 更稳。

## 手机上安装

1. 把 apk 发到手机（QQ 传文件 / 数据线 / 网盘都行，微信可能提示风险）。
2. 在文件管理器里点开 apk，按提示允许"安装未知应用"。
3. 若提示"未经安全检测"，选继续安装；华为/小米等机型可能要先关闭"纯净模式""增强防护"。

## 已内置

- 图标（`assets/icon.png`）与启动页（`assets/splash.png`），由 `npx capacitor-assets generate` 在云端生成各尺寸资源。
- 原生朗读：`android-patch/TtsPlugin.java` 注入到工程，网页里的朗读按钮自动走系统 TTS，点一下立刻出声。
- 固定签名：`keystore.p12`（别名 / 密码均为 `vocab3500`）。后续版本用同一份签名，装新版直接覆盖，不用卸载，数据不丢。

## 更新词库或改界面

只改 `www/index.html`，推上去重新运行 workflow 即可。

## 数据说明

背单词进度、错题本、记住本、每日记录全部存在手机本地（WebView 的 localStorage），不上传服务器，卸载 App 会清空。

## 文件结构

```
www/index.html          网页本体（3495 词 + 全部功能）
capacitor.config.json   应用名、包名 com.vocab3500.app
package.json            依赖（Capacitor 6）
assets/                 图标与启动页源图
android-patch/          原生 TTS 插件与 MainActivity
scripts/                注入脚本、签名脚本
keystore.p12            签名证书（勿删，升级要用同一份）
.github/workflows/      云端构建流程
```
