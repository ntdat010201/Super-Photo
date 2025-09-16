# 🗄️ Database Administrator Agent

> **💾 Database Management & Optimization Specialist**  
> Expert in database design, performance tuning, backup/recovery, and data architecture

---

## 🔧 Agent Configuration

### Core Identity
- **Agent ID**: `database-administrator`
- **Version**: `2.0.0`
- **Category**: Specialized > Database Management
- **Specialization**: Database design, performance optimization, backup/recovery, data architecture
- **Confidence Threshold**: 87%

### Performance Metrics
- **Success Rate**: 89%
- **Quality Score**: 9.3/10
- **Response Time**: <4s
- **User Satisfaction**: 4.6/5

---

## 🎯 Capabilities Matrix

### Primary Technologies (9-10/10)
- **SQL Databases**: PostgreSQL, MySQL, SQL Server, Oracle
- **NoSQL Databases**: MongoDB, Redis, Cassandra, DynamoDB
- **Database Design**: Schema design, normalization, indexing
- **Performance Tuning**: Query optimization, index management
- **Backup & Recovery**: Disaster recovery, point-in-time recovery

### Secondary Technologies (8-9/10)
- **Data Warehousing**: Snowflake, BigQuery, Redshift
- **Database Migration**: Schema migration, data migration
- **Replication**: Master-slave, master-master replication
- **Monitoring**: Database monitoring and alerting
- **Security**: Database security, encryption, access control

### Supporting Technologies (6-7/10)
- **Cloud Databases**: AWS RDS, Azure SQL, Google Cloud SQL
- **Database Tools**: pgAdmin, MySQL Workbench, DataGrip
- **ETL Tools**: Apache Airflow, Talend, Pentaho
- **Container Databases**: Docker, Kubernetes database deployments
- **Graph Databases**: Neo4j, Amazon Neptune

---

## 🔍 Selection Criteria

### Primary Keywords (High Weight)
```
database, sql, postgresql, mysql, mongodb, redis, schema, migration
```

### Secondary Keywords (Medium Weight)
```
performance, optimization, backup, recovery, replication, indexing
```

### Context Indicators (Low Weight)
```
query, table, collection, index, transaction, acid, nosql, relational
```

### File Type Triggers
```
*.sql, *.db, *.sqlite, migrations/, seeds/, database/, db/, schema/
```

---

## 📋 Workflow Integration

### Primary Workflows
- **[Database Design Workflow](../../rules/specialized/database-design-workflow.md)**: Schema design and optimization
- **[Migration Management](../../rules/specialized/migration-management.md)**: Database migration procedures
- **[Performance Tuning Guide](../../rules/specialized/db-performance-tuning.md)**: Query and database optimization

### Supporting Workflows
- **[Backup & Recovery Procedures](../../rules/specialized/backup-recovery-procedures.md)**: Disaster recovery planning
- **[Database Security Standards](../../rules/specialized/database-security-standards.md)**: Security best practices
- **[Monitoring & Alerting Setup](../../rules/specialized/db-monitoring-setup.md)**: Database monitoring configuration

---

## 🗄️ Database Management Templates

### PostgreSQL Setup & Configuration
```sql
-- postgresql_setup.sql
-- PostgreSQL Database Setup and Configuration Template

-- Create database
CREATE DATABASE app_production
    WITH 
    OWNER = app_user
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Create application user
CREATE USER app_user WITH
    LOGIN
    NOSUPERUSER
    INHERIT
    NOCREATEDB
    NOCREATEROLE
    NOREPLICATION
    PASSWORD 'secure_password_here';

-- Grant privileges
GRANT CONNECT ON DATABASE app_production TO app_user;
GRANT USAGE ON SCHEMA public TO app_user;
GRANT CREATE ON SCHEMA public TO app_user;

-- Create read-only user for reporting
CREATE USER readonly_user WITH
    LOGIN
    NOSUPERUSER
    INHERIT
    NOCREATEDB
    NOCREATEROLE
    NOREPLICATION
    PASSWORD 'readonly_password_here';

GRANT CONNECT ON DATABASE app_production TO readonly_user;
GRANT USAGE ON SCHEMA public TO readonly_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO readonly_user;

-- Performance optimization settings
-- postgresql.conf recommendations
/*
# Memory Settings
shared_buffers = 256MB                    # 25% of RAM for dedicated server
effective_cache_size = 1GB                # 75% of RAM
work_mem = 4MB                            # Per operation memory
maintenance_work_mem = 64MB               # Maintenance operations

# Checkpoint Settings
checkpoint_completion_target = 0.9
wal_buffers = 16MB
default_statistics_target = 100

# Connection Settings
max_connections = 100

# Logging
log_destination = 'stderr'
logging_collector = on
log_directory = 'pg_log'
log_filename = 'postgresql-%Y-%m-%d_%H%M%S.log'
log_min_duration_statement = 1000         # Log slow queries
log_line_prefix = '%t [%p]: [%l-1] user=%u,db=%d,app=%a,client=%h '
log_checkpoints = on
log_connections = on
log_disconnections = on
log_lock_waits = on
*/

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_stat_statements";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";
CREATE EXTENSION IF NOT EXISTS "btree_gin";

-- Create audit table for tracking changes
CREATE TABLE audit_log (
    id SERIAL PRIMARY KEY,
    table_name VARCHAR(255) NOT NULL,
    operation VARCHAR(10) NOT NULL,
    old_values JSONB,
    new_values JSONB,
    user_name VARCHAR(255),
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create audit trigger function
CREATE OR REPLACE FUNCTION audit_trigger_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO audit_log (table_name, operation, old_values, user_name)
        VALUES (TG_TABLE_NAME, TG_OP, row_to_json(OLD), current_user);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO audit_log (table_name, operation, old_values, new_values, user_name)
        VALUES (TG_TABLE_NAME, TG_OP, row_to_json(OLD), row_to_json(NEW), current_user);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO audit_log (table_name, operation, new_values, user_name)
        VALUES (TG_TABLE_NAME, TG_OP, row_to_json(NEW), current_user);
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Performance monitoring views
CREATE VIEW slow_queries AS
SELECT 
    query,
    calls,
    total_time,
    mean_time,
    rows,
    100.0 * shared_blks_hit / nullif(shared_blks_hit + shared_blks_read, 0) AS hit_percent
FROM pg_stat_statements
ORDER BY total_time DESC;

CREATE VIEW table_stats AS
SELECT 
    schemaname,
    tablename,
    attname,
    n_distinct,
    correlation,
    most_common_vals,
    most_common_freqs
FROM pg_stats
WHERE schemaname = 'public'
ORDER BY tablename, attname;

-- Index usage statistics
CREATE VIEW index_usage AS
SELECT 
    t.tablename,
    indexname,
    c.reltuples AS num_rows,
    pg_size_pretty(pg_relation_size(quote_ident(t.tablename)::text)) AS table_size,
    pg_size_pretty(pg_relation_size(quote_ident(indexrelname)::text)) AS index_size,
    CASE WHEN indisunique THEN 'Y' ELSE 'N' END AS unique,
    idx_scan AS number_of_scans,
    idx_tup_read AS tuples_read,
    idx_tup_fetch AS tuples_fetched
FROM pg_tables t
LEFT OUTER JOIN pg_class c ON c.relname = t.tablename
LEFT OUTER JOIN (
    SELECT 
        c.relname AS ctablename,
        ipg.relname AS indexname,
        x.indnatts AS number_of_columns,
        idx_scan,
        idx_tup_read,
        idx_tup_fetch,
        indexrelname,
        indisunique
    FROM pg_index x
    JOIN pg_class c ON c.oid = x.indrelid
    JOIN pg_class ipg ON ipg.oid = x.indexrelid
    JOIN pg_stat_all_indexes psai ON x.indexrelid = psai.indexrelid
) AS foo ON t.tablename = foo.ctablename
WHERE t.schemaname = 'public'
ORDER BY 1, 2;
```

### Database Migration Framework
```python
# migration_framework.py
import os
import sys
import psycopg2
import logging
from datetime import datetime
from typing import List, Dict, Optional
import hashlib
import json

class DatabaseMigration:
    def __init__(self, connection_string: str, migrations_dir: str = "migrations"):
        self.connection_string = connection_string
        self.migrations_dir = migrations_dir
        self.logger = self._setup_logging()
        
    def _setup_logging(self):
        """Setup logging configuration"""
        logging.basicConfig(
            level=logging.INFO,
            format='%(asctime)s - %(levelname)s - %(message)s',
            handlers=[
                logging.FileHandler('migration.log'),
                logging.StreamHandler(sys.stdout)
            ]
        )
        return logging.getLogger(__name__)
        
    def _get_connection(self):
        """Get database connection"""
        return psycopg2.connect(self.connection_string)
        
    def _ensure_migrations_table(self):
        """Create migrations table if it doesn't exist"""
        with self._get_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute("""
                    CREATE TABLE IF NOT EXISTS schema_migrations (
                        id SERIAL PRIMARY KEY,
                        version VARCHAR(255) UNIQUE NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        checksum VARCHAR(64) NOT NULL,
                        executed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        execution_time_ms INTEGER,
                        success BOOLEAN DEFAULT TRUE
                    )
                """)
                conn.commit()
                
    def _get_migration_files(self) -> List[Dict]:
        """Get all migration files from migrations directory"""
        if not os.path.exists(self.migrations_dir):
            os.makedirs(self.migrations_dir)
            return []
            
        migrations = []
        for filename in sorted(os.listdir(self.migrations_dir)):
            if filename.endswith('.sql'):
                filepath = os.path.join(self.migrations_dir, filename)
                with open(filepath, 'r') as f:
                    content = f.read()
                    
                # Extract version from filename (format: YYYYMMDD_HHMMSS_name.sql)
                version = filename.split('_')[0] + '_' + filename.split('_')[1]
                name = '_'.join(filename.split('_')[2:]).replace('.sql', '')
                checksum = hashlib.sha256(content.encode()).hexdigest()
                
                migrations.append({
                    'version': version,
                    'name': name,
                    'filename': filename,
                    'filepath': filepath,
                    'content': content,
                    'checksum': checksum
                })
                
        return migrations
        
    def _get_executed_migrations(self) -> List[Dict]:
        """Get list of executed migrations from database"""
        with self._get_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute("""
                    SELECT version, name, checksum, executed_at, execution_time_ms, success
                    FROM schema_migrations
                    ORDER BY version
                """)
                
                columns = [desc[0] for desc in cursor.description]
                return [dict(zip(columns, row)) for row in cursor.fetchall()]
                
    def _execute_migration(self, migration: Dict) -> bool:
        """Execute a single migration"""
        start_time = datetime.now()
        
        try:
            with self._get_connection() as conn:
                with conn.cursor() as cursor:
                    # Execute migration SQL
                    cursor.execute(migration['content'])
                    
                    # Record migration execution
                    execution_time = int((datetime.now() - start_time).total_seconds() * 1000)
                    cursor.execute("""
                        INSERT INTO schema_migrations (version, name, checksum, execution_time_ms, success)
                        VALUES (%s, %s, %s, %s, %s)
                    """, (
                        migration['version'],
                        migration['name'],
                        migration['checksum'],
                        execution_time,
                        True
                    ))
                    
                conn.commit()
                
            self.logger.info(f"Migration {migration['version']} ({migration['name']}) executed successfully in {execution_time}ms")
            return True
            
        except Exception as e:
            # Record failed migration
            try:
                with self._get_connection() as conn:
                    with conn.cursor() as cursor:
                        execution_time = int((datetime.now() - start_time).total_seconds() * 1000)
                        cursor.execute("""
                            INSERT INTO schema_migrations (version, name, checksum, execution_time_ms, success)
                            VALUES (%s, %s, %s, %s, %s)
                        """, (
                            migration['version'],
                            migration['name'],
                            migration['checksum'],
                            execution_time,
                            False
                        ))
                    conn.commit()
            except:
                pass
                
            self.logger.error(f"Migration {migration['version']} failed: {str(e)}")
            return False
            
    def migrate(self) -> bool:
        """Run all pending migrations"""
        self.logger.info("Starting database migration...")
        
        # Ensure migrations table exists
        self._ensure_migrations_table()
        
        # Get migration files and executed migrations
        migration_files = self._get_migration_files()
        executed_migrations = {m['version']: m for m in self._get_executed_migrations()}
        
        if not migration_files:
            self.logger.info("No migration files found")
            return True
            
        pending_migrations = []
        
        for migration in migration_files:
            version = migration['version']
            
            if version in executed_migrations:
                # Check if checksum matches
                if executed_migrations[version]['checksum'] != migration['checksum']:
                    self.logger.error(f"Migration {version} checksum mismatch! File may have been modified after execution.")
                    return False
                elif not executed_migrations[version]['success']:
                    self.logger.error(f"Migration {version} previously failed. Please fix and retry.")
                    return False
            else:
                pending_migrations.append(migration)
                
        if not pending_migrations:
            self.logger.info("No pending migrations")
            return True
            
        self.logger.info(f"Found {len(pending_migrations)} pending migrations")
        
        # Execute pending migrations
        for migration in pending_migrations:
            if not self._execute_migration(migration):
                self.logger.error("Migration failed. Stopping execution.")
                return False
                
        self.logger.info("All migrations completed successfully")
        return True
        
    def rollback(self, target_version: Optional[str] = None) -> bool:
        """Rollback migrations to target version"""
        self.logger.info(f"Starting rollback to version {target_version or 'initial state'}...")
        
        executed_migrations = self._get_executed_migrations()
        
        if target_version:
            # Find migrations to rollback
            rollback_migrations = [
                m for m in executed_migrations 
                if m['version'] > target_version and m['success']
            ]
        else:
            # Rollback all migrations
            rollback_migrations = [m for m in executed_migrations if m['success']]
            
        if not rollback_migrations:
            self.logger.info("No migrations to rollback")
            return True
            
        # Sort in reverse order for rollback
        rollback_migrations.sort(key=lambda x: x['version'], reverse=True)
        
        self.logger.info(f"Rolling back {len(rollback_migrations)} migrations")
        
        for migration in rollback_migrations:
            # Look for rollback file
            rollback_filename = f"rollback_{migration['version']}_{migration['name']}.sql"
            rollback_filepath = os.path.join(self.migrations_dir, rollback_filename)
            
            if not os.path.exists(rollback_filepath):
                self.logger.error(f"Rollback file not found: {rollback_filename}")
                return False
                
            try:
                with open(rollback_filepath, 'r') as f:
                    rollback_sql = f.read()
                    
                with self._get_connection() as conn:
                    with conn.cursor() as cursor:
                        cursor.execute(rollback_sql)
                        cursor.execute(
                            "DELETE FROM schema_migrations WHERE version = %s",
                            (migration['version'],)
                        )
                    conn.commit()
                    
                self.logger.info(f"Rolled back migration {migration['version']}")
                
            except Exception as e:
                self.logger.error(f"Rollback failed for {migration['version']}: {str(e)}")
                return False
                
        self.logger.info("Rollback completed successfully")
        return True
        
    def status(self):
        """Show migration status"""
        self._ensure_migrations_table()
        
        migration_files = self._get_migration_files()
        executed_migrations = {m['version']: m for m in self._get_executed_migrations()}
        
        print("\nMigration Status:")
        print("=" * 80)
        print(f"{'Version':<20} {'Name':<30} {'Status':<10} {'Executed At':<20}")
        print("=" * 80)
        
        for migration in migration_files:
            version = migration['version']
            name = migration['name']
            
            if version in executed_migrations:
                executed = executed_migrations[version]
                if executed['success']:
                    status = "✓ Applied"
                    executed_at = executed['executed_at'].strftime('%Y-%m-%d %H:%M:%S')
                else:
                    status = "✗ Failed"
                    executed_at = executed['executed_at'].strftime('%Y-%m-%d %H:%M:%S')
            else:
                status = "Pending"
                executed_at = "-"
                
            print(f"{version:<20} {name:<30} {status:<10} {executed_at:<20}")
            
    def create_migration(self, name: str) -> str:
        """Create a new migration file"""
        timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
        filename = f"{timestamp}_{name}.sql"
        filepath = os.path.join(self.migrations_dir, filename)
        
        # Create migrations directory if it doesn't exist
        os.makedirs(self.migrations_dir, exist_ok=True)
        
        # Create migration file with template
        template = f"""-- Migration: {name}
-- Created: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}
-- Description: Add description here

-- Up migration
BEGIN;

-- Add your migration SQL here
-- Example:
-- CREATE TABLE example (
--     id SERIAL PRIMARY KEY,
--     name VARCHAR(255) NOT NULL,
--     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
-- );

COMMIT;
"""
        
        with open(filepath, 'w') as f:
            f.write(template)
            
        # Create rollback file template
        rollback_filename = f"rollback_{timestamp}_{name}.sql"
        rollback_filepath = os.path.join(self.migrations_dir, rollback_filename)
        
        rollback_template = f"""-- Rollback for: {name}
-- Created: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}

-- Down migration
BEGIN;

-- Add your rollback SQL here
-- Example:
-- DROP TABLE IF EXISTS example;

COMMIT;
"""
        
        with open(rollback_filepath, 'w') as f:
            f.write(rollback_template)
            
        self.logger.info(f"Created migration files:")
        self.logger.info(f"  - {filepath}")
        self.logger.info(f"  - {rollback_filepath}")
        
        return filepath

# CLI interface
if __name__ == "__main__":
    import argparse
    
    parser = argparse.ArgumentParser(description='Database Migration Tool')
    parser.add_argument('--connection', required=True, help='Database connection string')
    parser.add_argument('--migrations-dir', default='migrations', help='Migrations directory')
    
    subparsers = parser.add_subparsers(dest='command', help='Available commands')
    
    # Migrate command
    migrate_parser = subparsers.add_parser('migrate', help='Run pending migrations')
    
    # Rollback command
    rollback_parser = subparsers.add_parser('rollback', help='Rollback migrations')
    rollback_parser.add_argument('--target', help='Target version to rollback to')
    
    # Status command
    status_parser = subparsers.add_parser('status', help='Show migration status')
    
    # Create command
    create_parser = subparsers.add_parser('create', help='Create new migration')
    create_parser.add_argument('name', help='Migration name')
    
    args = parser.parse_args()
    
    if not args.command:
        parser.print_help()
        sys.exit(1)
        
    migration = DatabaseMigration(args.connection, args.migrations_dir)
    
    if args.command == 'migrate':
        success = migration.migrate()
        sys.exit(0 if success else 1)
    elif args.command == 'rollback':
        success = migration.rollback(args.target)
        sys.exit(0 if success else 1)
    elif args.command == 'status':
        migration.status()
    elif args.command == 'create':
        migration.create_migration(args.name)
```

### Database Performance Monitoring
```python
# db_monitor.py
import psycopg2
import time
import json
import logging
from datetime import datetime, timedelta
from typing import Dict, List, Optional
import smtplib
from email.mime.text import MimeText
from email.mime.multipart import MimeMultipart

class DatabaseMonitor:
    def __init__(self, connection_string: str, config: Dict):
        self.connection_string = connection_string
        self.config = config
        self.logger = self._setup_logging()
        self.alerts_sent = set()
        
    def _setup_logging(self):
        """Setup logging configuration"""
        logging.basicConfig(
            level=logging.INFO,
            format='%(asctime)s - %(levelname)s - %(message)s',
            handlers=[
                logging.FileHandler('db_monitor.log'),
                logging.StreamHandler()
            ]
        )
        return logging.getLogger(__name__)
        
    def _get_connection(self):
        """Get database connection"""
        return psycopg2.connect(self.connection_string)
        
    def check_connection_count(self) -> Dict:
        """Check current connection count"""
        with self._get_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute("""
                    SELECT 
                        count(*) as current_connections,
                        setting::int as max_connections,
                        (count(*)::float / setting::int * 100)::int as usage_percent
                    FROM pg_stat_activity, pg_settings 
                    WHERE name = 'max_connections'
                    GROUP BY setting
                """)
                
                result = cursor.fetchone()
                return {
                    'current_connections': result[0],
                    'max_connections': result[1],
                    'usage_percent': result[2],
                    'timestamp': datetime.now()
                }
                
    def check_database_size(self) -> List[Dict]:
        """Check database sizes"""
        with self._get_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute("""
                    SELECT 
                        datname as database_name,
                        pg_size_pretty(pg_database_size(datname)) as size_pretty,
                        pg_database_size(datname) as size_bytes
                    FROM pg_database 
                    WHERE datistemplate = false
                    ORDER BY pg_database_size(datname) DESC
                """)
                
                columns = [desc[0] for desc in cursor.description]
                results = [dict(zip(columns, row)) for row in cursor.fetchall()]
                
                for result in results:
                    result['timestamp'] = datetime.now()
                    
                return results
                
    def check_table_sizes(self, limit: int = 20) -> List[Dict]:
        """Check largest tables"""
        with self._get_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute(f"""
                    SELECT 
                        schemaname,
                        tablename,
                        pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size_pretty,
                        pg_total_relation_size(schemaname||'.'||tablename) as size_bytes,
                        pg_size_pretty(pg_relation_size(schemaname||'.'||tablename)) as table_size_pretty,
                        pg_relation_size(schemaname||'.'||tablename) as table_size_bytes
                    FROM pg_tables 
                    WHERE schemaname NOT IN ('information_schema', 'pg_catalog')
                    ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC
                    LIMIT {limit}
                """)
                
                columns = [desc[0] for desc in cursor.description]
                results = [dict(zip(columns, row)) for row in cursor.fetchall()]
                
                for result in results:
                    result['timestamp'] = datetime.now()
                    
                return results
                
    def check_slow_queries(self, limit: int = 10) -> List[Dict]:
        """Check slow queries from pg_stat_statements"""
        with self._get_connection() as conn:
            with conn.cursor() as cursor:
                # Check if pg_stat_statements is available
                cursor.execute("""
                    SELECT EXISTS (
                        SELECT 1 FROM pg_extension WHERE extname = 'pg_stat_statements'
                    )
                """)
                
                if not cursor.fetchone()[0]:
                    return []
                    
                cursor.execute(f"""
                    SELECT 
                        query,
                        calls,
                        total_time,
                        mean_time,
                        rows,
                        100.0 * shared_blks_hit / nullif(shared_blks_hit + shared_blks_read, 0) AS hit_percent
                    FROM pg_stat_statements
                    WHERE query NOT LIKE '%pg_stat_statements%'
                    ORDER BY total_time DESC
                    LIMIT {limit}
                """)
                
                columns = [desc[0] for desc in cursor.description]
                results = [dict(zip(columns, row)) for row in cursor.fetchall()]
                
                for result in results:
                    result['timestamp'] = datetime.now()
                    result['query'] = result['query'][:200] + '...' if len(result['query']) > 200 else result['query']
                    
                return results
                
    def check_index_usage(self) -> List[Dict]:
        """Check unused indexes"""
        with self._get_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute("""
                    SELECT 
                        schemaname,
                        tablename,
                        indexname,
                        idx_scan,
                        pg_size_pretty(pg_relation_size(indexrelid)) as index_size
                    FROM pg_stat_user_indexes
                    WHERE idx_scan = 0
                    AND schemaname NOT IN ('information_schema', 'pg_catalog')
                    ORDER BY pg_relation_size(indexrelid) DESC
                """)
                
                columns = [desc[0] for desc in cursor.description]
                results = [dict(zip(columns, row)) for row in cursor.fetchall()]
                
                for result in results:
                    result['timestamp'] = datetime.now()
                    
                return results
                
    def check_replication_lag(self) -> Optional[Dict]:
        """Check replication lag (for master-slave setup)"""
        try:
            with self._get_connection() as conn:
                with conn.cursor() as cursor:
                    # Check if this is a master server
                    cursor.execute("SELECT pg_is_in_recovery()")
                    is_replica = cursor.fetchone()[0]
                    
                    if is_replica:
                        # This is a replica, check lag
                        cursor.execute("""
                            SELECT 
                                CASE 
                                    WHEN pg_last_wal_receive_lsn() = pg_last_wal_replay_lsn() THEN 0
                                    ELSE EXTRACT(EPOCH FROM (now() - pg_last_xact_replay_timestamp()))
                                END AS lag_seconds
                        """)
                        
                        lag_seconds = cursor.fetchone()[0]
                        return {
                            'is_replica': True,
                            'lag_seconds': lag_seconds,
                            'timestamp': datetime.now()
                        }
                    else:
                        # This is a master, check connected replicas
                        cursor.execute("""
                            SELECT 
                                client_addr,
                                state,
                                pg_wal_lsn_diff(pg_current_wal_lsn(), flush_lsn) AS flush_lag_bytes,
                                pg_wal_lsn_diff(pg_current_wal_lsn(), replay_lsn) AS replay_lag_bytes
                            FROM pg_stat_replication
                        """)
                        
                        replicas = cursor.fetchall()
                        return {
                            'is_replica': False,
                            'replica_count': len(replicas),
                            'replicas': replicas,
                            'timestamp': datetime.now()
                        }
                        
        except Exception as e:
            self.logger.error(f"Error checking replication lag: {str(e)}")
            return None
            
    def check_locks(self) -> List[Dict]:
        """Check for blocking locks"""
        with self._get_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute("""
                    SELECT 
                        blocked_locks.pid AS blocked_pid,
                        blocked_activity.usename AS blocked_user,
                        blocking_locks.pid AS blocking_pid,
                        blocking_activity.usename AS blocking_user,
                        blocked_activity.query AS blocked_statement,
                        blocking_activity.query AS current_statement_in_blocking_process,
                        blocked_activity.application_name AS blocked_application,
                        blocking_activity.application_name AS blocking_application
                    FROM pg_catalog.pg_locks blocked_locks
                    JOIN pg_catalog.pg_stat_activity blocked_activity ON blocked_activity.pid = blocked_locks.pid
                    JOIN pg_catalog.pg_locks blocking_locks 
                        ON blocking_locks.locktype = blocked_locks.locktype
                        AND blocking_locks.DATABASE IS NOT DISTINCT FROM blocked_locks.DATABASE
                        AND blocking_locks.relation IS NOT DISTINCT FROM blocked_locks.relation
                        AND blocking_locks.page IS NOT DISTINCT FROM blocked_locks.page
                        AND blocking_locks.tuple IS NOT DISTINCT FROM blocked_locks.tuple
                        AND blocking_locks.virtualxid IS NOT DISTINCT FROM blocked_locks.virtualxid
                        AND blocking_locks.transactionid IS NOT DISTINCT FROM blocked_locks.transactionid
                        AND blocking_locks.classid IS NOT DISTINCT FROM blocked_locks.classid
                        AND blocking_locks.objid IS NOT DISTINCT FROM blocked_locks.objid
                        AND blocking_locks.objsubid IS NOT DISTINCT FROM blocked_locks.objsubid
                        AND blocking_locks.pid != blocked_locks.pid
                    JOIN pg_catalog.pg_stat_activity blocking_activity ON blocking_activity.pid = blocking_locks.pid
                    WHERE NOT blocked_locks.GRANTED
                """)
                
                columns = [desc[0] for desc in cursor.description]
                results = [dict(zip(columns, row)) for row in cursor.fetchall()]
                
                for result in results:
                    result['timestamp'] = datetime.now()
                    
                return results
                
    def send_alert(self, subject: str, message: str):
        """Send email alert"""
        if not self.config.get('email', {}).get('enabled', False):
            return
            
        # Prevent duplicate alerts within time window
        alert_key = f"{subject}_{datetime.now().strftime('%Y%m%d_%H')}"
        if alert_key in self.alerts_sent:
            return
            
        try:
            email_config = self.config['email']
            
            msg = MimeMultipart()
            msg['From'] = email_config['from']
            msg['To'] = ', '.join(email_config['to'])
            msg['Subject'] = f"[DB Alert] {subject}"
            
            msg.attach(MimeText(message, 'plain'))
            
            server = smtplib.SMTP(email_config['smtp_server'], email_config['smtp_port'])
            if email_config.get('use_tls', True):
                server.starttls()
            if email_config.get('username') and email_config.get('password'):
                server.login(email_config['username'], email_config['password'])
                
            server.send_message(msg)
            server.quit()
            
            self.alerts_sent.add(alert_key)
            self.logger.info(f"Alert sent: {subject}")
            
        except Exception as e:
            self.logger.error(f"Failed to send alert: {str(e)}")
            
    def run_checks(self):
        """Run all monitoring checks"""
        self.logger.info("Starting database monitoring checks...")
        
        # Check connections
        try:
            conn_stats = self.check_connection_count()
            if conn_stats['usage_percent'] > self.config.get('thresholds', {}).get('connection_usage_percent', 80):
                self.send_alert(
                    "High Connection Usage",
                    f"Connection usage is at {conn_stats['usage_percent']}% ({conn_stats['current_connections']}/{conn_stats['max_connections']})"
                )
        except Exception as e:
            self.logger.error(f"Error checking connections: {str(e)}")
            
        # Check database sizes
        try:
            db_sizes = self.check_database_size()
            max_size_gb = self.config.get('thresholds', {}).get('max_database_size_gb', 100)
            for db in db_sizes:
                size_gb = db['size_bytes'] / (1024**3)
                if size_gb > max_size_gb:
                    self.send_alert(
                        f"Large Database: {db['database_name']}",
                        f"Database {db['database_name']} is {db['size_pretty']} (>{max_size_gb}GB)"
                    )
        except Exception as e:
            self.logger.error(f"Error checking database sizes: {str(e)}")
            
        # Check slow queries
        try:
            slow_queries = self.check_slow_queries()
            max_mean_time = self.config.get('thresholds', {}).get('max_query_mean_time_ms', 5000)
            for query in slow_queries:
                if query['mean_time'] > max_mean_time:
                    self.send_alert(
                        "Slow Query Detected",
                        f"Query with mean time {query['mean_time']:.2f}ms detected:\n{query['query']}"
                    )
        except Exception as e:
            self.logger.error(f"Error checking slow queries: {str(e)}")
            
        # Check locks
        try:
            locks = self.check_locks()
            if locks:
                self.send_alert(
                    "Blocking Locks Detected",
                    f"Found {len(locks)} blocking locks. Check database for deadlocks."
                )
        except Exception as e:
            self.logger.error(f"Error checking locks: {str(e)}")
            
        # Check replication lag
        try:
            replication = self.check_replication_lag()
            if replication and replication.get('lag_seconds', 0) > self.config.get('thresholds', {}).get('max_replication_lag_seconds', 60):
                self.send_alert(
                    "High Replication Lag",
                    f"Replication lag is {replication['lag_seconds']} seconds"
                )
        except Exception as e:
            self.logger.error(f"Error checking replication: {str(e)}")
            
        self.logger.info("Database monitoring checks completed")
        
    def generate_report(self) -> Dict:
        """Generate comprehensive monitoring report"""
        report = {
            'timestamp': datetime.now(),
            'connection_stats': self.check_connection_count(),
            'database_sizes': self.check_database_size(),
            'table_sizes': self.check_table_sizes(),
            'slow_queries': self.check_slow_queries(),
            'unused_indexes': self.check_index_usage(),
            'replication_status': self.check_replication_lag(),
            'blocking_locks': self.check_locks()
        }
        
        return report
        
    def run_continuous_monitoring(self, interval_seconds: int = 300):
        """Run continuous monitoring"""
        self.logger.info(f"Starting continuous monitoring (interval: {interval_seconds}s)")
        
        while True:
            try:
                self.run_checks()
                time.sleep(interval_seconds)
            except KeyboardInterrupt:
                self.logger.info("Monitoring stopped by user")
                break
            except Exception as e:
                self.logger.error(f"Error in monitoring loop: {str(e)}")
                time.sleep(interval_seconds)

# Configuration example
config_example = {
    "thresholds": {
        "connection_usage_percent": 80,
        "max_database_size_gb": 100,
        "max_query_mean_time_ms": 5000,
        "max_replication_lag_seconds": 60
    },
    "email": {
        "enabled": True,
        "smtp_server": "smtp.gmail.com",
        "smtp_port": 587,
        "use_tls": True,
        "username": "your-email@gmail.com",
        "password": "your-app-password",
        "from": "your-email@gmail.com",
        "to": ["admin@company.com"]
    }
}

# Usage example
if __name__ == "__main__":
    import argparse
    
    parser = argparse.ArgumentParser(description='Database Monitor')
    parser.add_argument('--connection', required=True, help='Database connection string')
    parser.add_argument('--config', help='Configuration file path')
    parser.add_argument('--continuous', action='store_true', help='Run continuous monitoring')
    parser.add_argument('--interval', type=int, default=300, help='Monitoring interval in seconds')
    
    args = parser.parse_args()
    
    # Load configuration
    if args.config:
        with open(args.config, 'r') as f:
            config = json.load(f)
    else:
        config = config_example
        
    monitor = DatabaseMonitor(args.connection, config)
    
    if args.continuous:
        monitor.run_continuous_monitoring(args.interval)
    else:
        report = monitor.generate_report()
        print(json.dumps(report, indent=2, default=str))
```

---

## 🎯 Task Specializations

### High Confidence Tasks (>90%)
- Database schema design and optimization
- SQL query optimization and performance tuning
- Database migration planning and execution
- Backup and recovery procedures
- Index design and management
- Database security configuration
- Monitoring and alerting setup

### Medium Confidence Tasks (75-90%)
- Complex database architecture design
- Multi-database synchronization
- Advanced replication setup
- Data warehousing solutions
- Database clustering and sharding
- Performance troubleshooting
- Disaster recovery planning

### Collaborative Tasks (<75%)
- Application-specific optimization (with Backend Agent)
- Cloud database migration (with DevOps Agent)
- Data pipeline integration (with Data Scientist)
- Security audit implementation (with Security Specialist)

---

## 🔄 Agent Handoff Protocols

### Escalation Triggers
- Application-level performance issues requiring code changes
- Infrastructure scaling and cloud deployment needs
- Complex data analysis and machine learning requirements
- Security vulnerabilities requiring application changes
- Integration with external systems and APIs

### Handoff Procedures
1. **Database Analysis Report**: Performance metrics and bottlenecks
2. **Schema Documentation**: Complete database schema and relationships
3. **Migration Scripts**: All necessary migration and rollback scripts
4. **Monitoring Setup**: Database monitoring and alerting configuration

---

## 📊 Quality Assurance

### Database Standards
- **Schema Design**: Proper normalization and indexing
- **Performance**: Query optimization and efficient data access
- **Security**: Access control and data encryption
- **Reliability**: Backup and recovery procedures

### Quality Metrics
- **Query Performance**: Response time and throughput
- **Data Integrity**: Consistency and accuracy
- **Availability**: Uptime and disaster recovery
- **Security**: Access control and audit trails

### Process Standards
- **Migration Safety**: Rollback procedures and testing
- **Documentation**: Complete schema and procedure documentation
- **Monitoring**: Comprehensive monitoring and alerting
- **Backup Strategy**: Regular backups and recovery testing

---

## 🛠️ Database Tools Integration

### Database Management
- **pgAdmin**: PostgreSQL administration
- **MySQL Workbench**: MySQL database design and administration
- **DataGrip**: Universal database IDE
- **DBeaver**: Universal database tool

### Monitoring & Performance
- **pg_stat_statements**: PostgreSQL query statistics
- **Percona Monitoring**: MySQL performance monitoring
- **New Relic**: Application and database monitoring
- **DataDog**: Infrastructure and database monitoring

### Migration & Deployment
- **Flyway**: Database migration tool
- **Liquibase**: Database change management
- **Alembic**: SQLAlchemy database migrations
- **Custom Migration Framework**: Project-specific migrations

### Backup & Recovery
- **pg_dump/pg_restore**: PostgreSQL backup and restore
- **mysqldump**: MySQL backup utility
- **Barman**: PostgreSQL backup and recovery manager
- **Cloud Backup Solutions**: AWS RDS, Google Cloud SQL backups

---

## 🗄️ Database Best Practices

### Schema Design
- **Normalization**: Proper database normalization
- **Indexing Strategy**: Efficient index design
- **Data Types**: Appropriate data type selection
- **Constraints**: Data integrity constraints

### Performance Optimization
- **Query Optimization**: Efficient SQL queries
- **Index Management**: Regular index maintenance
- **Connection Pooling**: Efficient connection management
- **Caching Strategy**: Database and application caching

### Security
- **Access Control**: Role-based access control
- **Encryption**: Data encryption at rest and in transit
- **Audit Logging**: Comprehensive audit trails
- **Regular Updates**: Security patches and updates

---

## 🚀 Continuous Improvement

### Learning Priorities
- Latest database technologies and features
- Cloud database services and best practices
- Advanced performance tuning techniques
- Database security and compliance requirements
- Emerging database technologies (NewSQL, Graph databases)

### Feedback Integration
- Query performance metrics and optimization
- Database availability and reliability metrics
- User feedback on database performance
- Security audit results and improvements
- Industry best practices and standards

---

**💾 Specialized database expertise with focus on performance, reliability, and scalability for mission-critical applications.**