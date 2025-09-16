# Database Patterns Library

> **🗄️ Comprehensive Database Design & Integration Patterns**  
> Advanced database patterns with intelligent Sub-Agent integration for optimal performance, security, and reliability

## Overview

This library provides battle-tested database patterns optimized for modern applications with deep Sub-Agent integration. Each pattern includes performance optimization, security validation, error handling, and context-aware improvements.

## 🏗️ Database Architecture Patterns

### 1. Repository Pattern with Active Record

**Description**: Data access abstraction with intelligent caching and optimization  
**Best For**: Domain-driven design, testable data access, complex business logic  
**Sub-Agent Integration**: Context Optimizer for query optimization, Performance Analyzer for bottleneck detection

```typescript
// Repository Pattern with Sub-Agent Integration
import { SubAgentContainer } from '../types/SubAgents';
import { QueryBuilder, Transaction, ConnectionPool } from '../types/Database';
import { CacheManager } from '../utils/CacheManager';
import { Logger } from '../utils/Logger';

interface RepositoryConfig {
  tableName: string;
  primaryKey: string;
  cacheEnabled: boolean;
  cacheTTL: number;
  queryTimeout: number;
  maxRetries: number;
}

interface QueryOptions {
  select?: string[];
  where?: Record<string, any>;
  orderBy?: Record<string, 'ASC' | 'DESC'>;
  limit?: number;
  offset?: number;
  include?: string[];
  transaction?: Transaction;
}

interface RepositoryMetrics {
  queryCount: number;
  cacheHitRate: number;
  averageQueryTime: number;
  errorRate: number;
  lastOptimization: Date;
}

abstract class BaseRepository<T extends Record<string, any>> {
  protected config: RepositoryConfig;
  protected subAgents?: SubAgentContainer;
  protected cache: CacheManager;
  protected logger: Logger;
  protected metrics: RepositoryMetrics;
  protected queryBuilder: QueryBuilder;
  
  constructor(
    config: RepositoryConfig,
    queryBuilder: QueryBuilder,
    subAgents?: SubAgentContainer
  ) {
    this.config = config;
    this.queryBuilder = queryBuilder;
    this.subAgents = subAgents;
    this.cache = new CacheManager({
      ttl: config.cacheTTL,
      maxSize: 1000
    });
    this.logger = new Logger(`Repository:${config.tableName}`);
    this.metrics = {
      queryCount: 0,
      cacheHitRate: 0,
      averageQueryTime: 0,
      errorRate: 0,
      lastOptimization: new Date()
    };
  }
  
  public async findById(id: string | number, options?: QueryOptions): Promise<T | null> {
    const startTime = performance.now();
    const cacheKey = `${this.config.tableName}:${id}`;
    
    try {
      // Check cache first
      if (this.config.cacheEnabled && !options?.transaction) {
        const cached = await this.cache.get<T>(cacheKey);
        if (cached) {
          await this.updateMetrics('cache_hit', performance.now() - startTime);
          return cached;
        }
      }
      
      // Context optimization
      const optimizedOptions = await this.subAgents?.contextOptimizer?.optimizeQuery({
        operation: 'findById',
        table: this.config.tableName,
        options,
        context: { id, timestamp: new Date() }
      }) || options;
      
      // Build and execute query
      const query = this.queryBuilder
        .select(optimizedOptions?.select || ['*'])
        .from(this.config.tableName)
        .where(this.config.primaryKey, '=', id)
        .timeout(this.config.queryTimeout);
      
      if (optimizedOptions?.include) {
        for (const relation of optimizedOptions.include) {
          query.include(relation);
        }
      }
      
      const result = await this.executeQuery<T>(query, options?.transaction);
      const record = result[0] || null;
      
      // Cache result
      if (record && this.config.cacheEnabled && !options?.transaction) {
        await this.cache.set(cacheKey, record);
      }
      
      await this.updateMetrics('query_success', performance.now() - startTime);
      return record;
      
    } catch (error) {
      await this.handleQueryError(error as Error, 'findById', { id, options });
      throw error;
    }
  }
  
  public async findMany(options?: QueryOptions): Promise<T[]> {
    const startTime = performance.now();
    const cacheKey = this.generateCacheKey('findMany', options);
    
    try {
      // Check cache
      if (this.config.cacheEnabled && !options?.transaction) {
        const cached = await this.cache.get<T[]>(cacheKey);
        if (cached) {
          await this.updateMetrics('cache_hit', performance.now() - startTime);
          return cached;
        }
      }
      
      // Context optimization
      const optimizedOptions = await this.subAgents?.contextOptimizer?.optimizeQuery({
        operation: 'findMany',
        table: this.config.tableName,
        options,
        context: { timestamp: new Date() }
      }) || options;
      
      // Build query
      let query = this.queryBuilder
        .select(optimizedOptions?.select || ['*'])
        .from(this.config.tableName)
        .timeout(this.config.queryTimeout);
      
      // Apply filters
      if (optimizedOptions?.where) {
        for (const [field, value] of Object.entries(optimizedOptions.where)) {
          if (Array.isArray(value)) {
            query = query.whereIn(field, value);
          } else if (typeof value === 'object' && value !== null) {
            // Handle complex conditions
            for (const [operator, operatorValue] of Object.entries(value)) {
              query = query.where(field, operator, operatorValue);
            }
          } else {
            query = query.where(field, '=', value);
          }
        }
      }
      
      // Apply ordering
      if (optimizedOptions?.orderBy) {
        for (const [field, direction] of Object.entries(optimizedOptions.orderBy)) {
          query = query.orderBy(field, direction);
        }
      }
      
      // Apply pagination
      if (optimizedOptions?.limit) {
        query = query.limit(optimizedOptions.limit);
      }
      if (optimizedOptions?.offset) {
        query = query.offset(optimizedOptions.offset);
      }
      
      // Apply includes
      if (optimizedOptions?.include) {
        for (const relation of optimizedOptions.include) {
          query = query.include(relation);
        }
      }
      
      const results = await this.executeQuery<T>(query, options?.transaction);
      
      // Cache results
      if (this.config.cacheEnabled && !options?.transaction) {
        await this.cache.set(cacheKey, results);
      }
      
      await this.updateMetrics('query_success', performance.now() - startTime);
      return results;
      
    } catch (error) {
      await this.handleQueryError(error as Error, 'findMany', { options });
      throw error;
    }
  }
  
  public async create(data: Partial<T>, options?: { transaction?: Transaction }): Promise<T> {
    const startTime = performance.now();
    
    try {
      // Security validation
      const securityCheck = await this.subAgents?.securityAuditor?.validateDataInput({
        operation: 'create',
        table: this.config.tableName,
        data,
        timestamp: new Date()
      });
      
      if (!securityCheck?.valid) {
        throw new Error(`Security validation failed: ${securityCheck?.reason}`);
      }
      
      // Data optimization
      const optimizedData = await this.subAgents?.contextOptimizer?.optimizeDataForInsert({
        table: this.config.tableName,
        data,
        context: { operation: 'create', timestamp: new Date() }
      }) || data;
      
      // Build insert query
      const query = this.queryBuilder
        .insert(optimizedData)
        .into(this.config.tableName)
        .returning('*')
        .timeout(this.config.queryTimeout);
      
      const results = await this.executeQuery<T>(query, options?.transaction);
      const created = results[0];
      
      // Invalidate related cache
      if (this.config.cacheEnabled) {
        await this.invalidateCache(['findMany']);
      }
      
      // Performance monitoring
      await this.subAgents?.performanceAnalyzer?.analyzeInsertOperation({
        table: this.config.tableName,
        recordSize: JSON.stringify(created).length,
        executionTime: performance.now() - startTime,
        timestamp: new Date()
      });
      
      await this.updateMetrics('query_success', performance.now() - startTime);
      return created;
      
    } catch (error) {
      await this.handleQueryError(error as Error, 'create', { data, options });
      throw error;
    }
  }
  
  public async update(
    id: string | number,
    data: Partial<T>,
    options?: { transaction?: Transaction }
  ): Promise<T | null> {
    const startTime = performance.now();
    
    try {
      // Security validation
      const securityCheck = await this.subAgents?.securityAuditor?.validateDataInput({
        operation: 'update',
        table: this.config.tableName,
        data,
        recordId: id,
        timestamp: new Date()
      });
      
      if (!securityCheck?.valid) {
        throw new Error(`Security validation failed: ${securityCheck?.reason}`);
      }
      
      // Data optimization
      const optimizedData = await this.subAgents?.contextOptimizer?.optimizeDataForUpdate({
        table: this.config.tableName,
        data,
        recordId: id,
        context: { operation: 'update', timestamp: new Date() }
      }) || data;
      
      // Build update query
      const query = this.queryBuilder
        .update(optimizedData)
        .table(this.config.tableName)
        .where(this.config.primaryKey, '=', id)
        .returning('*')
        .timeout(this.config.queryTimeout);
      
      const results = await this.executeQuery<T>(query, options?.transaction);
      const updated = results[0] || null;
      
      // Invalidate cache
      if (this.config.cacheEnabled) {
        const cacheKey = `${this.config.tableName}:${id}`;
        await this.cache.delete(cacheKey);
        await this.invalidateCache(['findMany']);
      }
      
      // Performance monitoring
      if (updated) {
        await this.subAgents?.performanceAnalyzer?.analyzeUpdateOperation({
          table: this.config.tableName,
          recordId: id,
          fieldsUpdated: Object.keys(optimizedData).length,
          executionTime: performance.now() - startTime,
          timestamp: new Date()
        });
      }
      
      await this.updateMetrics('query_success', performance.now() - startTime);
      return updated;
      
    } catch (error) {
      await this.handleQueryError(error as Error, 'update', { id, data, options });
      throw error;
    }
  }
  
  public async delete(id: string | number, options?: { transaction?: Transaction }): Promise<boolean> {
    const startTime = performance.now();
    
    try {
      // Security validation
      const securityCheck = await this.subAgents?.securityAuditor?.validateDeleteOperation({
        table: this.config.tableName,
        recordId: id,
        timestamp: new Date()
      });
      
      if (!securityCheck?.allowed) {
        throw new Error(`Delete operation blocked: ${securityCheck?.reason}`);
      }
      
      // Build delete query
      const query = this.queryBuilder
        .delete()
        .from(this.config.tableName)
        .where(this.config.primaryKey, '=', id)
        .timeout(this.config.queryTimeout);
      
      const result = await this.executeQuery(query, options?.transaction);
      const deleted = result.affectedRows > 0;
      
      // Invalidate cache
      if (deleted && this.config.cacheEnabled) {
        const cacheKey = `${this.config.tableName}:${id}`;
        await this.cache.delete(cacheKey);
        await this.invalidateCache(['findMany']);
      }
      
      // Performance monitoring
      if (deleted) {
        await this.subAgents?.performanceAnalyzer?.analyzeDeleteOperation({
          table: this.config.tableName,
          recordId: id,
          executionTime: performance.now() - startTime,
          timestamp: new Date()
        });
      }
      
      await this.updateMetrics('query_success', performance.now() - startTime);
      return deleted;
      
    } catch (error) {
      await this.handleQueryError(error as Error, 'delete', { id, options });
      throw error;
    }
  }
  
  public async count(options?: Pick<QueryOptions, 'where' | 'transaction'>): Promise<number> {
    const startTime = performance.now();
    const cacheKey = this.generateCacheKey('count', options);
    
    try {
      // Check cache
      if (this.config.cacheEnabled && !options?.transaction) {
        const cached = await this.cache.get<number>(cacheKey);
        if (cached !== undefined) {
          await this.updateMetrics('cache_hit', performance.now() - startTime);
          return cached;
        }
      }
      
      // Build count query
      let query = this.queryBuilder
        .count('* as total')
        .from(this.config.tableName)
        .timeout(this.config.queryTimeout);
      
      // Apply filters
      if (options?.where) {
        for (const [field, value] of Object.entries(options.where)) {
          query = query.where(field, '=', value);
        }
      }
      
      const results = await this.executeQuery<{ total: number }>(query, options?.transaction);
      const count = results[0]?.total || 0;
      
      // Cache result
      if (this.config.cacheEnabled && !options?.transaction) {
        await this.cache.set(cacheKey, count);
      }
      
      await this.updateMetrics('query_success', performance.now() - startTime);
      return count;
      
    } catch (error) {
      await this.handleQueryError(error as Error, 'count', { options });
      throw error;
    }
  }
  
  public async exists(id: string | number, options?: { transaction?: Transaction }): Promise<boolean> {
    const startTime = performance.now();
    
    try {
      const query = this.queryBuilder
        .select('1')
        .from(this.config.tableName)
        .where(this.config.primaryKey, '=', id)
        .limit(1)
        .timeout(this.config.queryTimeout);
      
      const results = await this.executeQuery(query, options?.transaction);
      const exists = results.length > 0;
      
      await this.updateMetrics('query_success', performance.now() - startTime);
      return exists;
      
    } catch (error) {
      await this.handleQueryError(error as Error, 'exists', { id, options });
      throw error;
    }
  }
  
  public getMetrics(): RepositoryMetrics {
    return { ...this.metrics };
  }
  
  public async optimizeQueries(): Promise<void> {
    try {
      const optimization = await this.subAgents?.performanceAnalyzer?.analyzeRepositoryPerformance({
        table: this.config.tableName,
        metrics: this.metrics,
        timestamp: new Date()
      });
      
      if (optimization?.recommendations) {
        this.logger.info('Repository optimization recommendations:', optimization.recommendations);
        this.metrics.lastOptimization = new Date();
      }
    } catch (error) {
      this.logger.error('Failed to optimize queries:', error);
    }
  }
  
  protected async executeQuery<R = any>(
    query: QueryBuilder,
    transaction?: Transaction
  ): Promise<R[]> {
    const startTime = performance.now();
    let retries = 0;
    
    while (retries <= this.config.maxRetries) {
      try {
        const result = transaction 
          ? await query.transacting(transaction)
          : await query;
        
        return result;
        
      } catch (error) {
        retries++;
        
        if (retries > this.config.maxRetries) {
          throw error;
        }
        
        // Exponential backoff
        const delay = Math.pow(2, retries) * 100;
        await new Promise(resolve => setTimeout(resolve, delay));
        
        this.logger.warn(`Query retry ${retries}/${this.config.maxRetries}:`, error);
      }
    }
    
    throw new Error('Max retries exceeded');
  }
  
  protected async handleQueryError(
    error: Error,
    operation: string,
    context: Record<string, any>
  ): Promise<void> {
    this.metrics.errorRate = (this.metrics.errorRate + 1) / (this.metrics.queryCount + 1);
    
    await this.subAgents?.bugHunter?.analyzeRepositoryError({
      error,
      operation,
      table: this.config.tableName,
      context,
      timestamp: new Date()
    });
    
    this.logger.error(`Repository ${operation} error:`, {
      error: error.message,
      context,
      stack: error.stack
    });
  }
  
  protected async updateMetrics(type: 'query_success' | 'cache_hit', executionTime: number): Promise<void> {
    this.metrics.queryCount++;
    
    if (type === 'cache_hit') {
      this.metrics.cacheHitRate = (this.metrics.cacheHitRate * (this.metrics.queryCount - 1) + 1) / this.metrics.queryCount;
    }
    
    this.metrics.averageQueryTime = (this.metrics.averageQueryTime * (this.metrics.queryCount - 1) + executionTime) / this.metrics.queryCount;
  }
  
  protected generateCacheKey(operation: string, options?: any): string {
    const optionsHash = options ? JSON.stringify(options) : '';
    return `${this.config.tableName}:${operation}:${optionsHash}`;
  }
  
  protected async invalidateCache(patterns: string[]): Promise<void> {
    for (const pattern of patterns) {
      await this.cache.deletePattern(`${this.config.tableName}:${pattern}:*`);
    }
  }
}

// Example implementation
interface User {
  id: string;
  email: string;
  name: string;
  role: string;
  createdAt: Date;
  updatedAt: Date;
}

class UserRepository extends BaseRepository<User> {
  constructor(queryBuilder: QueryBuilder, subAgents?: SubAgentContainer) {
    super({
      tableName: 'users',
      primaryKey: 'id',
      cacheEnabled: true,
      cacheTTL: 300, // 5 minutes
      queryTimeout: 5000, // 5 seconds
      maxRetries: 3
    }, queryBuilder, subAgents);
  }
  
  public async findByEmail(email: string): Promise<User | null> {
    const users = await this.findMany({
      where: { email },
      limit: 1
    });
    
    return users[0] || null;
  }
  
  public async findActiveUsers(): Promise<User[]> {
    return this.findMany({
      where: { 
        status: 'active',
        deletedAt: null 
      },
      orderBy: { createdAt: 'DESC' }
    });
  }
  
  public async updateLastLogin(userId: string): Promise<void> {
    await this.update(userId, {
      lastLoginAt: new Date()
    });
  }
}

export { BaseRepository, UserRepository, RepositoryConfig, QueryOptions, RepositoryMetrics };
```

### 2. Database Connection Pool Pattern

**Description**: Efficient database connection management with monitoring  
**Best For**: High-traffic applications, microservices, scalable systems  
**Sub-Agent Integration**: Performance Analyzer for connection monitoring, Bug Hunter for connection error analysis

```typescript
// Database Connection Pool Pattern with Sub-Agent Integration
import { EventEmitter } from 'events';
import { SubAgentContainer } from '../types/SubAgents';

interface ConnectionConfig {
  host: string;
  port: number;
  database: string;
  username: string;
  password: string;
  ssl?: boolean;
  connectionTimeout: number;
  idleTimeout: number;
  maxLifetime: number;
}

interface PoolConfig {
  min: number;
  max: number;
  acquireTimeoutMillis: number;
  createTimeoutMillis: number;
  destroyTimeoutMillis: number;
  idleTimeoutMillis: number;
  reapIntervalMillis: number;
  createRetryIntervalMillis: number;
  propagateCreateError: boolean;
}

interface Connection {
  id: string;
  native: any; // Database-specific connection object
  createdAt: Date;
  lastUsedAt: Date;
  isIdle: boolean;
  isValid: boolean;
  queryCount: number;
}

interface PoolMetrics {
  totalConnections: number;
  activeConnections: number;
  idleConnections: number;
  pendingRequests: number;
  totalQueries: number;
  averageQueryTime: number;
  connectionErrors: number;
  poolUtilization: number;
}

class DatabaseConnectionPool extends EventEmitter {
  private config: ConnectionConfig;
  private poolConfig: PoolConfig;
  private subAgents?: SubAgentContainer;
  private connections: Map<string, Connection> = new Map();
  private availableConnections: Connection[] = [];
  private pendingRequests: Array<{
    resolve: (connection: Connection) => void;
    reject: (error: Error) => void;
    timestamp: Date;
  }> = [];
  private metrics: PoolMetrics;
  private healthCheckInterval?: NodeJS.Timeout;
  private metricsInterval?: NodeJS.Timeout;
  
  constructor(
    connectionConfig: ConnectionConfig,
    poolConfig: PoolConfig,
    subAgents?: SubAgentContainer
  ) {
    super();
    this.config = connectionConfig;
    this.poolConfig = poolConfig;
    this.subAgents = subAgents;
    this.metrics = {
      totalConnections: 0,
      activeConnections: 0,
      idleConnections: 0,
      pendingRequests: 0,
      totalQueries: 0,
      averageQueryTime: 0,
      connectionErrors: 0,
      poolUtilization: 0
    };
    
    this.startHealthCheck();
    this.startMetricsCollection();
  }
  
  public async initialize(): Promise<void> {
    try {
      // Create minimum connections
      const promises = [];
      for (let i = 0; i < this.poolConfig.min; i++) {
        promises.push(this.createConnection());
      }
      
      await Promise.all(promises);
      
      this.emit('initialized', {
        totalConnections: this.connections.size,
        timestamp: new Date()
      });
      
      await this.subAgents?.performanceAnalyzer?.analyzeConnectionPoolInitialization({
        poolSize: this.connections.size,
        config: this.poolConfig,
        timestamp: new Date()
      });
      
    } catch (error) {
      await this.subAgents?.bugHunter?.analyzeConnectionPoolError({
        error: error as Error,
        operation: 'initialize',
        config: this.poolConfig,
        timestamp: new Date()
      });
      
      throw error;
    }
  }
  
  public async acquire(): Promise<Connection> {
    const startTime = performance.now();
    
    return new Promise((resolve, reject) => {
      const timeout = setTimeout(() => {
        const index = this.pendingRequests.findIndex(req => req.resolve === resolve);
        if (index !== -1) {
          this.pendingRequests.splice(index, 1);
        }
        
        reject(new Error(`Connection acquire timeout after ${this.poolConfig.acquireTimeoutMillis}ms`));
      }, this.poolConfig.acquireTimeoutMillis);
      
      const request = {
        resolve: (connection: Connection) => {
          clearTimeout(timeout);
          
          // Performance monitoring
          const acquireTime = performance.now() - startTime;
          this.subAgents?.performanceAnalyzer?.analyzeConnectionAcquisition({
            acquireTime,
            poolUtilization: this.getPoolUtilization(),
            timestamp: new Date()
          });
          
          resolve(connection);
        },
        reject: (error: Error) => {
          clearTimeout(timeout);
          reject(error);
        },
        timestamp: new Date()
      };
      
      this.pendingRequests.push(request);
      this.updateMetrics();
      
      this.processNextRequest();
    });
  }
  
  public async release(connection: Connection): Promise<void> {
    try {
      if (!this.connections.has(connection.id)) {
        throw new Error('Connection not found in pool');
      }
      
      connection.isIdle = true;
      connection.lastUsedAt = new Date();
      
      // Validate connection before returning to pool
      const isValid = await this.validateConnection(connection);
      if (!isValid) {
        await this.destroyConnection(connection);
        return;
      }
      
      this.availableConnections.push(connection);
      this.updateMetrics();
      
      // Process pending requests
      this.processNextRequest();
      
    } catch (error) {
      await this.subAgents?.bugHunter?.analyzeConnectionReleaseError({
        error: error as Error,
        connectionId: connection.id,
        timestamp: new Date()
      });
      
      throw error;
    }
  }
  
  public async query<T = any>(
    sql: string,
    params?: any[],
    connection?: Connection
  ): Promise<T[]> {
    const startTime = performance.now();
    let conn = connection;
    let shouldRelease = false;
    
    try {
      if (!conn) {
        conn = await this.acquire();
        shouldRelease = true;
      }
      
      conn.isIdle = false;
      conn.queryCount++;
      
      // Security validation
      const securityCheck = await this.subAgents?.securityAuditor?.validateQuery({
        sql,
        params,
        connectionId: conn.id,
        timestamp: new Date()
      });
      
      if (!securityCheck?.safe) {
        throw new Error(`Query blocked by security auditor: ${securityCheck?.reason}`);
      }
      
      // Execute query
      const result = await this.executeQuery(conn, sql, params);
      
      // Performance monitoring
      const queryTime = performance.now() - startTime;
      this.metrics.totalQueries++;
      this.metrics.averageQueryTime = (this.metrics.averageQueryTime * (this.metrics.totalQueries - 1) + queryTime) / this.metrics.totalQueries;
      
      await this.subAgents?.performanceAnalyzer?.analyzeQuery({
        sql,
        params,
        executionTime: queryTime,
        connectionId: conn.id,
        timestamp: new Date()
      });
      
      return result;
      
    } catch (error) {
      this.metrics.connectionErrors++;
      
      await this.subAgents?.bugHunter?.analyzeQueryError({
        error: error as Error,
        sql,
        params,
        connectionId: conn?.id,
        timestamp: new Date()
      });
      
      throw error;
      
    } finally {
      if (conn && shouldRelease) {
        await this.release(conn);
      }
    }
  }
  
  public async transaction<T>(
    callback: (connection: Connection) => Promise<T>
  ): Promise<T> {
    const connection = await this.acquire();
    
    try {
      await this.executeQuery(connection, 'BEGIN');
      
      const result = await callback(connection);
      
      await this.executeQuery(connection, 'COMMIT');
      return result;
      
    } catch (error) {
      await this.executeQuery(connection, 'ROLLBACK');
      
      await this.subAgents?.bugHunter?.analyzeTransactionError({
        error: error as Error,
        connectionId: connection.id,
        timestamp: new Date()
      });
      
      throw error;
      
    } finally {
      await this.release(connection);
    }
  }
  
  public getMetrics(): PoolMetrics {
    this.updateMetrics();
    return { ...this.metrics };
  }
  
  public async close(): Promise<void> {
    try {
      // Clear intervals
      if (this.healthCheckInterval) {
        clearInterval(this.healthCheckInterval);
      }
      if (this.metricsInterval) {
        clearInterval(this.metricsInterval);
      }
      
      // Reject pending requests
      for (const request of this.pendingRequests) {
        request.reject(new Error('Connection pool is closing'));
      }
      this.pendingRequests = [];
      
      // Close all connections
      const closePromises = Array.from(this.connections.values())
        .map(conn => this.destroyConnection(conn));
      
      await Promise.all(closePromises);
      
      this.emit('closed', {
        timestamp: new Date()
      });
      
    } catch (error) {
      await this.subAgents?.bugHunter?.analyzeConnectionPoolError({
        error: error as Error,
        operation: 'close',
        timestamp: new Date()
      });
      
      throw error;
    }
  }
  
  private async createConnection(): Promise<Connection> {
    const startTime = performance.now();
    
    try {
      const connectionId = `conn_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
      
      // Create native connection (implementation depends on database driver)
      const nativeConnection = await this.createNativeConnection();
      
      const connection: Connection = {
        id: connectionId,
        native: nativeConnection,
        createdAt: new Date(),
        lastUsedAt: new Date(),
        isIdle: true,
        isValid: true,
        queryCount: 0
      };
      
      this.connections.set(connectionId, connection);
      this.availableConnections.push(connection);
      
      const creationTime = performance.now() - startTime;
      await this.subAgents?.performanceAnalyzer?.analyzeConnectionCreation({
        connectionId,
        creationTime,
        totalConnections: this.connections.size,
        timestamp: new Date()
      });
      
      this.emit('connectionCreated', {
        connectionId,
        totalConnections: this.connections.size,
        timestamp: new Date()
      });
      
      return connection;
      
    } catch (error) {
      await this.subAgents?.bugHunter?.analyzeConnectionCreationError({
        error: error as Error,
        config: this.config,
        timestamp: new Date()
      });
      
      throw error;
    }
  }
  
  private async destroyConnection(connection: Connection): Promise<void> {
    try {
      // Remove from available connections
      const index = this.availableConnections.indexOf(connection);
      if (index !== -1) {
        this.availableConnections.splice(index, 1);
      }
      
      // Close native connection
      if (connection.native && typeof connection.native.end === 'function') {
        await connection.native.end();
      }
      
      this.connections.delete(connection.id);
      
      this.emit('connectionDestroyed', {
        connectionId: connection.id,
        totalConnections: this.connections.size,
        timestamp: new Date()
      });
      
    } catch (error) {
      await this.subAgents?.bugHunter?.analyzeConnectionDestructionError({
        error: error as Error,
        connectionId: connection.id,
        timestamp: new Date()
      });
    }
  }
  
  private async processNextRequest(): Promise<void> {
    if (this.pendingRequests.length === 0 || this.availableConnections.length === 0) {
      // Try to create new connection if under max limit
      if (this.connections.size < this.poolConfig.max && this.pendingRequests.length > 0) {
        try {
          await this.createConnection();
          this.processNextRequest(); // Recursive call to process the request
        } catch (error) {
          // Handle connection creation failure
          const request = this.pendingRequests.shift();
          if (request) {
            request.reject(error as Error);
          }
        }
      }
      return;
    }
    
    const request = this.pendingRequests.shift();
    const connection = this.availableConnections.shift();
    
    if (request && connection) {
      connection.isIdle = false;
      request.resolve(connection);
    }
  }
  
  private async validateConnection(connection: Connection): Promise<boolean> {
    try {
      // Simple validation query
      await this.executeQuery(connection, 'SELECT 1');
      connection.isValid = true;
      return true;
    } catch (error) {
      connection.isValid = false;
      return false;
    }
  }
  
  private async executeQuery(
    connection: Connection,
    sql: string,
    params?: any[]
  ): Promise<any> {
    // Implementation depends on database driver
    // This is a mock implementation
    return new Promise((resolve, reject) => {
      // Mock query execution
      setTimeout(() => {
        if (sql === 'SELECT 1') {
          resolve([{ '1': 1 }]);
        } else {
          resolve([]);
        }
      }, 10);
    });
  }
  
  private async createNativeConnection(): Promise<any> {
    // Implementation depends on database driver
    // This is a mock implementation
    return {
      host: this.config.host,
      port: this.config.port,
      database: this.config.database,
      end: async () => { /* Close connection */ }
    };
  }
  
  private startHealthCheck(): void {
    this.healthCheckInterval = setInterval(async () => {
      try {
        await this.performHealthCheck();
      } catch (error) {
        await this.subAgents?.bugHunter?.analyzeHealthCheckError({
          error: error as Error,
          timestamp: new Date()
        });
      }
    }, this.poolConfig.reapIntervalMillis);
  }
  
  private async performHealthCheck(): Promise<void> {
    const now = new Date();
    const connectionsToDestroy: Connection[] = [];
    
    for (const connection of this.connections.values()) {
      // Check for idle timeout
      if (connection.isIdle && 
          (now.getTime() - connection.lastUsedAt.getTime()) > this.poolConfig.idleTimeoutMillis) {
        connectionsToDestroy.push(connection);
      }
      
      // Check for max lifetime
      if ((now.getTime() - connection.createdAt.getTime()) > this.config.maxLifetime) {
        connectionsToDestroy.push(connection);
      }
    }
    
    // Destroy expired connections
    for (const connection of connectionsToDestroy) {
      await this.destroyConnection(connection);
    }
    
    // Ensure minimum connections
    while (this.connections.size < this.poolConfig.min) {
      try {
        await this.createConnection();
      } catch (error) {
        break; // Stop trying if creation fails
      }
    }
  }
  
  private startMetricsCollection(): void {
    this.metricsInterval = setInterval(async () => {
      try {
        this.updateMetrics();
        
        await this.subAgents?.performanceAnalyzer?.analyzeConnectionPoolMetrics({
          metrics: this.metrics,
          timestamp: new Date()
        });
      } catch (error) {
        // Silently handle metrics collection errors
      }
    }, 30000); // Every 30 seconds
  }
  
  private updateMetrics(): void {
    this.metrics.totalConnections = this.connections.size;
    this.metrics.activeConnections = Array.from(this.connections.values())
      .filter(conn => !conn.isIdle).length;
    this.metrics.idleConnections = this.availableConnections.length;
    this.metrics.pendingRequests = this.pendingRequests.length;
    this.metrics.poolUtilization = this.getPoolUtilization();
  }
  
  private getPoolUtilization(): number {
    if (this.poolConfig.max === 0) return 0;
    return (this.metrics.activeConnections / this.poolConfig.max) * 100;
  }
}

// Usage Example
const connectionConfig: ConnectionConfig = {
  host: 'localhost',
  port: 5432,
  database: 'myapp',
  username: 'user',
  password: 'password',
  ssl: true,
  connectionTimeout: 5000,
  idleTimeout: 30000,
  maxLifetime: 3600000 // 1 hour
};

const poolConfig: PoolConfig = {
  min: 2,
  max: 10,
  acquireTimeoutMillis: 5000,
  createTimeoutMillis: 5000,
  destroyTimeoutMillis: 5000,
  idleTimeoutMillis: 30000,
  reapIntervalMillis: 10000,
  createRetryIntervalMillis: 200,
  propagateCreateError: false
};

const pool = new DatabaseConnectionPool(connectionConfig, poolConfig, subAgents);

// Initialize pool
await pool.initialize();

// Use pool for queries
const users = await pool.query('SELECT * FROM users WHERE active = ?', [true]);

// Use transactions
const result = await pool.transaction(async (connection) => {
  await pool.query('INSERT INTO users (name, email) VALUES (?, ?)', ['John', 'john@example.com'], connection);
  await pool.query('INSERT INTO profiles (user_id, bio) VALUES (?, ?)', [userId, 'Software Developer'], connection);
  return { success: true };
});

export { DatabaseConnectionPool, ConnectionConfig, PoolConfig, Connection, PoolMetrics };
```

## 📊 Benefits of Database Patterns

### 🎯 **Proven Architecture**
- Battle-tested patterns for data access and management
- Consistent implementation across different database systems
- Reduced development time and maintenance overhead
- Lower risk of data integrity issues

### 🤖 **Deep Sub-Agent Integration**
- **Context Optimizer**: Query optimization, caching strategies, data structure optimization
- **Performance Analyzer**: Connection monitoring, query performance analysis, bottleneck identification
- **Security Auditor**: SQL injection prevention, data access validation, audit logging
- **Bug Hunter**: Error analysis, connection issue debugging, performance problem detection

### 🔒 **Security by Design**
- Built-in SQL injection protection
- Data access validation and authorization
- Comprehensive audit logging
- Secure connection management

### ⚡ **Performance Optimized**
- Intelligent query optimization
- Connection pooling and reuse
- Caching strategies
- Resource usage monitoring

### 🛠 **Developer Experience**
- Type-safe database operations
- Comprehensive error handling
- Detailed logging and monitoring
- Easy testing and debugging

## 📋 Usage Guidelines

### 🎯 **Pattern Selection Criteria**

1. **Data Access Requirements**:
   - Simple CRUD operations → Repository Pattern
   - Complex queries → Query Builder Pattern
   - High-performance needs → Connection Pool Pattern
   - Domain-driven design → Active Record Pattern

2. **Scalability Needs**:
   - High throughput → Connection pooling
   - Read-heavy workloads → Caching strategies
   - Write-heavy workloads → Optimized insert patterns
   - Distributed systems → Stateless patterns

3. **Security Requirements**:
   - Sensitive data → Enhanced validation patterns
   - Audit requirements → Comprehensive logging
   - Multi-tenant → Row-level security patterns

### ✅ **Implementation Checklist**

- [ ] **Sub-Agent Integration**: Verify all Sub-Agents are properly configured
- [ ] **Security Validation**: Implement input validation and SQL injection protection
- [ ] **Performance Monitoring**: Add comprehensive metrics and logging
- [ ] **Error Handling**: Implement robust error handling and recovery
- [ ] **Connection Management**: Configure appropriate connection pooling
- [ ] **Caching Strategy**: Implement intelligent caching where appropriate
- [ ] **Testing**: Implement unit, integration, and performance tests
- [ ] **Monitoring**: Set up alerts and dashboards for production monitoring

### ❌ **Anti-Patterns to Avoid**

- **SQL Injection**: Not using parameterized queries
- **N+1 Queries**: Loading related data inefficiently
- **Connection Leaks**: Not properly releasing database connections
- **Missing Indexes**: Poor query performance due to missing indexes
- **Hardcoded Queries**: SQL embedded directly in business logic
- **No Error Handling**: Ignoring database errors or connection failures
- **Synchronous Operations**: Blocking operations in async contexts

---

**Integration**: Works seamlessly with all Sub-Agent systems  
**Maintenance**: Self-optimizing patterns with continuous learning  
**Scalability**: Designed for high-performance, distributed database environments