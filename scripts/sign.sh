#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

APK="android/app/build/outputs/apk/debug/app-debug.apk"
BT="$(ls -d "$ANDROID_HOME"/build-tools/* | sort -V | tail -1)"
OUT="专升本3500词.apk"

"$BT/zipalign" -f 4 "$APK" app-aligned.apk
"$BT/apksigner" sign \
  --ks keystore.p12 \
  --ks-type PKCS12 \
  --ks-pass pass:vocab3500 \
  --ks-key-alias vocab3500 \
  --out "$OUT" \
  app-aligned.apk
"$BT/apksigner" verify --print-certs "$OUT" | head -4
