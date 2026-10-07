#!/bin/bash
set -e

echo "==> [PostgreSQL Init] Creating databases playswap_auth and playswap_transfer..."

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE playswap_auth;
    CREATE DATABASE playswap_transfer;
EOSQL

echo "==> [PostgreSQL Init] Importing auth_service_data.sql into playswap_auth..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "playswap_auth" -f /docker-entrypoint-initdb.d/auth_service_data.sql || echo "Warning: auth_service_data import completed with warnings."

echo "==> [PostgreSQL Init] Importing transfer_service_data.sql into playswap_transfer..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "playswap_transfer" -f /docker-entrypoint-initdb.d/transfer_service_data.sql || echo "Warning: transfer_service_data import completed with warnings."

echo "==> [PostgreSQL Init] Database setup completed successfully!"
