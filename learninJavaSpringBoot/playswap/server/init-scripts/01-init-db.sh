#!/bin/sh
set -e

echo "==> [PostgreSQL Init] Creating databases playswap_auth and playswap_transfer..."

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE playswap_auth;
    CREATE DATABASE playswap_transfer;
EOSQL

echo "==> [PostgreSQL Init] Enabling uuid-ossp extensions..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "playswap_auth" -c 'CREATE EXTENSION IF NOT EXISTS "uuid-ossp";'
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "playswap_transfer" -c 'CREATE EXTENSION IF NOT EXISTS "uuid-ossp";'

echo "==> [PostgreSQL Init] Importing auth_service_data.sql into playswap_auth..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "playswap_auth" -f /docker-entrypoint-initdb.d/auth_service_data.sql || echo "Warning: auth_service_data import completed with warnings."

echo "==> [PostgreSQL Init] Importing transfer_service_data.sql into playswap_transfer..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "playswap_transfer" -f /docker-entrypoint-initdb.d/transfer_service_data.sql || echo "Warning: transfer_service_data import completed with warnings."

if [ -f /docker-entrypoint-initdb.d/schema_indexes.sql ]; then
    echo "==> [PostgreSQL Init] Applying performance indexes to playswap_transfer..."
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "playswap_transfer" -f /docker-entrypoint-initdb.d/schema_indexes.sql || echo "Warning: schema_indexes applied with warnings."
fi

echo "==> [PostgreSQL Init] Database setup completed successfully!"
