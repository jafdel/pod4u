#!/bin/bash
set -e
VOLUME_PATH="/pgdata"
#echo 'postgres ALL=(ALL) NOPASSWD: /usr/sbin/reboot, /usr/sbin/shutdown -r now' >> /etc/sudoers.d/custom-nopasswd
sudo -k && sudo apt install setfacl && sudo setfacl -m u:$(id -u):rwx -R /var/lib/postgresql/18/docker
#chown postgres:postgres /var/lib/postgresql/18/docker
#psql -U musicbrainz -c "CREATE ROLE postgres LOGIN SUPERUSER PASSWORD 'postgres';"
#psql -U musicbrainz -c "CREATE DATABASE musicbrainz_db;"
psql -U musicbrainz -c "CREATE ROLE web_anon NOLOGIN;"
#psql -U musicbrainz =c "CREATE SCHEMA musicbrainz;"
psql -U musicbrainz -c "GRANT USAGE ON SCHEMA musicbrainz TO web_anon;"
psql -U musicbrainz -c "GRANT SELECT ON musicbrainz.* TO web_anon;"
psql -U musicbrainz -c "CREATE ROLE authenticator NOINHERIT LOGIN PASSWORD 'p455w0rd';"
psql -U musicbrainz -c "GRANT web_anon TO authenticator;"
pg_isready -d "musicbrainz"
#pg_ctl restart -wt9999 -mfast -o "-c fsync=off" -o "-c log_statement=none" -o "-c log_temp_files=-1" -o "-c log_checkpoints=off" -o "-c log_min_duration_statement=-1" -o "-c maintenance_work_mem=2GB" -o "-c synchronous_commit=off" -o "-c archive_mode=off" -o "-c full_page_writes=off" -o "-c checkpoint_timeout=1h" -o "-c max_wal_size=16GB" -o "-c wal_level=minimal" -o "-c max_wal_senders=0" -o "-c autovacuum=off"
#unset DOCKER_HOST
#/usr/local/bin/musicbrainz-server/admin/InitDb.pl --import /pgdata/mbdump.tar.bz2 --echo
#pg_restore -d musicbrainz_db -h localhost -p 5432 --clean --if-exists --verbose -F tar /pgdata/mbdump.tar
#vacuumdb --analyze-only
#pg_ctl stop -wt9999 -mfast