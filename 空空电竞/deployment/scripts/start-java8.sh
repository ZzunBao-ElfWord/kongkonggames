#!/usr/bin/env bash
set -euo pipefail
BASE="$(cd "$(dirname "$0")/.." && pwd)"
mkdir -p "$BASE/logs" "$BASE/run"
set -a
. "$BASE/env/java8.env"
set +a
if grep -q '__DB_' "$BASE/env/java8.env"; then
  echo "Please fill database placeholders in env/java8.env"
  exit 1
fi
if grep -q '__FILL_' "$BASE/env/java8.env"; then
  echo "Please fill the database password in env/java8.env"
  exit 1
fi
if [ -f "$BASE/run/java8.pid" ] && kill -0 "$(cat "$BASE/run/java8.pid")" 2>/dev/null; then
  echo "Java8 test service already running"
  exit 0
fi
if [ ! -x "$JAVA8_BIN" ]; then
  echo "Existing Java8 not found: $JAVA8_BIN"
  exit 1
fi
if ss -lnt 2>/dev/null | grep -q ':8183 '; then
  echo "Port 8183 is already occupied; no process was started."
  exit 1
fi
nohup "$JAVA8_BIN" -jar "$BASE/java8/playwith.jar" > "$BASE/logs/java8.log" 2>&1 &
echo $! > "$BASE/run/java8.pid"
sleep 5
if ! kill -0 "$(cat "$BASE/run/java8.pid")" 2>/dev/null; then
  echo "Java8 test service failed to start. Last log lines:"
  tail -n 60 "$BASE/logs/java8.log"
  exit 1
fi
echo "Java8 test service started PID $(cat "$BASE/run/java8.pid")"
