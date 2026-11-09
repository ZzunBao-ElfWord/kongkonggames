#!/usr/bin/env bash
set -euo pipefail
BASE="$(cd "$(dirname "$0")/.." && pwd)"
mkdir -p "$BASE/logs" "$BASE/run" "$BASE/data"
set -a
. "$BASE/env/java11.env"
set +a
APPLE_PRIVATE_KEY_PATH="${APPLE_PRIVATE_KEY_PATH/__BASE__/$BASE}"
APPLE_ROOT_CERT_PATHS="${APPLE_ROOT_CERT_PATHS//__BASE__/$BASE}"
DB_URL="${DB_URL/__BASE__/$BASE}"
export APPLE_PRIVATE_KEY_PATH APPLE_ROOT_CERT_PATHS DB_URL
if [ -f "$BASE/run/java11.pid" ] && kill -0 "$(cat "$BASE/run/java11.pid")" 2>/dev/null; then
  echo "Java11 already running"
  exit 0
fi
if [ ! -x "$JAVA11_BIN" ]; then
  echo "Java11 not found: $JAVA11_BIN"
  echo "Install openjdk-11-jre-headless first."
  exit 1
fi
if ss -lnt 2>/dev/null | grep -q ':8091 '; then
  echo "Port 8091 is already occupied; no process was started."
  exit 1
fi
nohup "$JAVA11_BIN" -jar "$BASE/java11/apple-iap-service.jar" > "$BASE/logs/java11.log" 2>&1 &
echo $! > "$BASE/run/java11.pid"
sleep 3
if ! kill -0 "$(cat "$BASE/run/java11.pid")" 2>/dev/null; then
  echo "Java11 failed to start. Last log lines:"
  tail -n 40 "$BASE/logs/java11.log"
  exit 1
fi
echo "Java11 started PID $(cat "$BASE/run/java11.pid")"
