#!/bin/bash
set -euo pipefail
Username=$1
HOST="${PGHOST:-localhost}"
PORT="${PGPORT:-5432}"
USER="${PGUSER:-postgres}"

echo "Waiting for container mb..."
until bash -c "/home/$Username/bin/docker ps --format '{{.Names}}' | grep -Fxq mb"; do
  sleep 1
done
echo "Waiting for PostgreSQL at $HOST:$PORT..."
/home/$Username/bin/docker exec -it -d mb /bin/bash -c "until pg_isready -p $PORT -h $HOST -U $USER >/dev/null 2>&1; do sleep 1; done"
/home/$Username/bin/docker exec -it mb /bin/bash -c /pg.sh
cd ./musicbrainz/.postgrest && chmod +x ../mb.conf && /usr/local/bin/postgrest ../mb.conf
