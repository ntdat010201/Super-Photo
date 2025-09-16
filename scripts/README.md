# Scripts Directory

This directory contains utility scripts for the project.

## Available Scripts

### Backup and Restore
- `backup_file.sh` - Script to backup files
- `restore_file.sh` - Script to restore files from backup
- `cleanup_backups.sh` - Script to clean up old backup files


#### Backup Script (`backup_file.sh`)
- Creates timestamped backups of files
- Maintains directory structure
- Logs backup operations

#### Restore Script (`restore_file.sh`)
- Restores files from backup directory
- Lists available backups
- Preserves file permissions

#### Cleanup Script (`cleanup_backups.sh`)
- Removes old backup files
- Configurable retention period
- Safe cleanup with confirmation

