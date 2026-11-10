#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

APP_DIR="android/app/src/main/java/com/vocab3500/app"
mkdir -p "$APP_DIR"
cp android-patch/TtsPlugin.java "$APP_DIR/TtsPlugin.java"
cp android-patch/MainActivity.java "$APP_DIR/MainActivity.java"

python3 - <<'PY'
from pathlib import Path
p = Path("android/app/src/main/AndroidManifest.xml")
s = p.read_text(encoding="utf-8")
block = (
    '    <queries>\n'
    '        <intent><action android:name="android.intent.action.TTS_SERVICE" /></intent>\n'
    '    </queries>\n'
)
if "TTS_SERVICE" not in s:
    s = s.replace("<application", block + "    <application", 1)
    p.write_text(s, encoding="utf-8")
PY

echo "native TTS plugin injected"
