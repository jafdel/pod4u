#!/bin/bash
set -euo pipefail
HOST="${PGHOST:-localhost}"
PORT="${PGPORT:-5432}"
USER="${PGUSER:-postgres}"

echo "Waiting for container mb..."
until bash -c "/home/$1/bin/docker ps --format '{{.Names}}' | grep -Fxq mb"; do
  sleep 1
done
echo "Waiting for PostgreSQL at $HOST:$PORT..."
/home/"$1"/bin/docker exec -it -d mb /bin/bash -c "until pg_isready -p $PORT -h $HOST -U $USER >/dev/null 2>&1; do sleep 10; done"
cd ./.postgrest && chmod +x ../mb.conf && /usr/local/bin/postgrest ../mb.conf