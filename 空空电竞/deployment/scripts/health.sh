#!/usr/bin/env bash
set -euo pipefail
curl -fsS http://127.0.0.1:8091/actuator/health
echo
curl -sS -X POST http://127.0.0.1:8183/pwyl-api/app/appleIap/verify -H 'Content-Type: application/json' -d '{}'
echo
