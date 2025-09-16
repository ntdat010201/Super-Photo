# Logging & Audit Patterns

> **📝 Comprehensive Logging Framework**  
> Advanced logging patterns for .god ecosystem audit trails, debugging, and compliance

## Logging Philosophy

**Mission**: Provide complete audit trails and debugging capabilities for .god ecosystem  
**Approach**: Structured, contextual, and searchable logging with intelligent log management  
**Principle**: Log everything that matters, nothing that doesn't, with privacy and security first

---

## 🏗️ Logging Architecture

### Hierarchical Logging Structure
```typescript
enum LogLevel {
  TRACE = 0,
  DEBUG = 1,
  INFO = 2,
  WARN = 3,
  ERROR = 4,
  FATAL = 5
}

enum LogCategory {
  SYSTEM = 'system',
  AGENT = 'agent',
  WORKFLOW = 'workflow',
  SECURITY = 'security',
  PERFORMANCE = 'performance',
  AUDIT = 'audit',
  USER = 'user',
  INTEGRATION = 'integration'
}

interface LogEntry {
  timestamp: string;
  level: LogLevel;
  category: LogCategory;
  source: string;
  message: string;
  correlationId?: string;
  traceId?: string;
  spanId?: string;
  userId?: string;
  sessionId?: string;
  agentId?: string;
  workflowId?: string;
  stepId?: string;
  context: LogContext;
  metadata?: Record<string, any>;
  tags?: string[];
  duration?: number;
  error?: ErrorDetails;
}

interface LogContext {
  component: string;
  operation: string;
  version: string;
  environment: string;
  hostname: string;
  processId: number;
  threadId?: string;
  requestId?: string;
  clientIp?: string;
  userAgent?: string;
}

interface ErrorDetails {
  name: string;
  message: string;
  stack?: string;
  code?: string;
  cause?: ErrorDetails;
  context?: Record<string, any>;
}

class AdvancedLogger {
  private logLevel: LogLevel;
  private logWriters: LogWriter[];
  private logEnrichers: LogEnricher[];
  private logFilters: LogFilter[];
  private contextManager: LogContextManager;
  private auditLogger: AuditLogger;
  
  constructor(config: LoggerConfig) {
    this.logLevel = config.level || LogLevel.INFO;
    this.logWriters = this.initializeWriters(config.writers);
    this.logEnrichers = this.initializeEnrichers(config.enrichers);
    this.logFilters = this.initializeFilters(config.filters);
    this.contextManager = new LogContextManager();
    this.auditLogger = new AuditLogger();
  }
  
  async log(
    level: LogLevel,
    category: LogCategory,
    message: string,
    context?: Partial<LogContext>,
    metadata?: Record<string, any>
  ): Promise<void> {
    if (level < this.logLevel) return;
    
    const logEntry: LogEntry = {
      timestamp: new Date().toISOString(),
      level,
      category,
      source: context?.component || 'unknown',
      message,
      correlationId: this.contextManager.getCorrelationId(),
      traceId: this.contextManager.getTraceId(),
      spanId: this.contextManager.getSpanId(),
      userId: this.contextManager.getUserId(),
      sessionId: this.contextManager.getSessionId(),
      agentId: this.contextManager.getAgentId(),
      workflowId: this.contextManager.getWorkflowId(),
      stepId: this.contextManager.getStepId(),
      context: {
        ...this.contextManager.getBaseContext(),
        ...context
      },
      metadata,
      tags: this.generateTags(category, level, context)
    };
    
    // Apply enrichers
    let enrichedEntry = logEntry;
    for (const enricher of this.logEnrichers) {
      enrichedEntry = await enricher.enrich(enrichedEntry);
    }
    
    // Apply filters
    let shouldLog = true;
    for (const filter of this.logFilters) {
      if (!await filter.shouldLog(enrichedEntry)) {
        shouldLog = false;
        break;
      }
    }
    
    if (!shouldLog) return;
    
    // Write to all configured writers
    const writePromises = this.logWriters.map(writer => 
      writer.write(enrichedEntry).catch(error => 
        console.error('Log writer error:', error)
      )
    );
    
    await Promise.allSettled(writePromises);
    
    // Audit logging for sensitive operations
    if (this.shouldAudit(enrichedEntry)) {
      await this.auditLogger.log(enrichedEntry);
    }
  }
  
  // Convenience methods
  async trace(message: string, context?: Partial<LogContext>, metadata?: Record<string, any>): Promise<void> {
    await this.log(LogLevel.TRACE, LogCategory.SYSTEM, message, context, metadata);
  }
  
  async debug(message: string, context?: Partial<LogContext>, metadata?: Record<string, any>): Promise<void> {
    await this.log(LogLevel.DEBUG, LogCategory.SYSTEM, message, context, metadata);
  }
  
  async info(message: string, context?: Partial<LogContext>, metadata?: Record<string, any>): Promise<void> {
    await this.log(LogLevel.INFO, LogCategory.SYSTEM, message, context, metadata);
  }
  
  async warn(message: string, context?: Partial<LogContext>, metadata?: Record<string, any>): Promise<void> {
    await this.log(LogLevel.WARN, LogCategory.SYSTEM, message, context, metadata);
  }
  
  async error(message: string, error?: Error, context?: Partial<LogContext>, metadata?: Record<string, any>): Promise<void> {
    const enrichedMetadata = {
      ...metadata,
      error: error ? this.serializeError(error) : undefined
    };
    
    await this.log(LogLevel.ERROR, LogCategory.SYSTEM, message, context, enrichedMetadata);
  }
  
  async fatal(message: string, error?: Error, context?: Partial<LogContext>, metadata?: Record<string, any>): Promise<void> {
    const enrichedMetadata = {
      ...metadata,
      error: error ? this.serializeError(error) : undefined
    };
    
    await this.log(LogLevel.FATAL, LogCategory.SYSTEM, message, context, enrichedMetadata);
  }
  
  // Category-specific logging methods
  async logAgentActivity(
    agentId: string,
    operation: string,
    status: 'started' | 'completed' | 'failed',
    duration?: number,
    result?: any,
    error?: Error
  ): Promise<void> {
    const message = `Agent ${agentId} ${operation} ${status}`;
    const context = {
      component: 'agent',
      operation
    };
    const metadata = {
      agentId,
      operation,
      status,
      duration,
      result: result ? this.sanitizeResult(result) : undefined,
      error: error ? this.serializeError(error) : undefined
    };
    
    const level = status === 'failed' ? LogLevel.ERROR : LogLevel.INFO;
    await this.log(level, LogCategory.AGENT, message, context, metadata);
  }
  
  async logWorkflowExecution(
    workflowId: string,
    stepId: string,
    agentId: string,
    status: 'started' | 'completed' | 'failed' | 'skipped',
    duration?: number,
    input?: any,
    output?: any,
    error?: Error
  ): Promise<void> {
    const message = `Workflow ${workflowId} step ${stepId} ${status}`;
    const context = {
      component: 'workflow',
      operation: 'step_execution'
    };
    const metadata = {
      workflowId,
      stepId,
      agentId,
      status,
      duration,
      input: input ? this.sanitizeInput(input) : undefined,
      output: output ? this.sanitizeResult(output) : undefined,
      error: error ? this.serializeError(error) : undefined
    };
    
    const level = status === 'failed' ? LogLevel.ERROR : LogLevel.INFO;
    await this.log(level, LogCategory.WORKFLOW, message, context, metadata);
  }
  
  async logSecurityEvent(
    eventType: string,
    severity: 'low' | 'medium' | 'high' | 'critical',
    description: string,
    details?: any,
    userId?: string,
    ipAddress?: string
  ): Promise<void> {
    const message = `Security event: ${eventType} (${severity})`;
    const context = {
      component: 'security',
      operation: eventType
    };
    const metadata = {
      eventType,
      severity,
      description,
      details: details ? this.sanitizeSecurityDetails(details) : undefined,
      userId,
      ipAddress: ipAddress ? this.hashSensitiveData(ipAddress) : undefined
    };
    
    const level = severity === 'critical' ? LogLevel.FATAL :
                 severity === 'high' ? LogLevel.ERROR :
                 severity === 'medium' ? LogLevel.WARN : LogLevel.INFO;
    
    await this.log(level, LogCategory.SECURITY, message, context, metadata);
  }
  
  async logPerformanceMetrics(
    component: string,
    operation: string,
    metrics: PerformanceMetrics,
    thresholds?: PerformanceThresholds
  ): Promise<void> {
    const message = `Performance metrics for ${component}.${operation}`;
    const context = {
      component: 'performance',
      operation: 'metrics_collection'
    };
    const metadata = {
      component,
      operation,
      metrics,
      thresholds,
      violations: thresholds ? this.checkThresholdViolations(metrics, thresholds) : undefined
    };
    
    const level = metadata.violations && metadata.violations.length > 0 ? LogLevel.WARN : LogLevel.INFO;
    await this.log(level, LogCategory.PERFORMANCE, message, context, metadata);
  }
  
  async logUserAction(
    userId: string,
    action: string,
    resource: string,
    result: 'success' | 'failure',
    details?: any
  ): Promise<void> {
    const message = `User ${userId} ${action} on ${resource}: ${result}`;
    const context = {
      component: 'user',
      operation: action
    };
    const metadata = {
      userId,
      action,
      resource,
      result,
      details: details ? this.sanitizeUserDetails(details) : undefined
    };
    
    const level = result === 'failure' ? LogLevel.WARN : LogLevel.INFO;
    await this.log(level, LogCategory.USER, message, context, metadata);
  }
  
  async logIntegrationEvent(
    integration: string,
    operation: string,
    status: 'success' | 'failure' | 'timeout',
    duration: number,
    request?: any,
    response?: any,
    error?: Error
  ): Promise<void> {
    const message = `Integration ${integration} ${operation}: ${status}`;
    const context = {
      component: 'integration',
      operation
    };
    const metadata = {
      integration,
      operation,
      status,
      duration,
      request: request ? this.sanitizeRequest(request) : undefined,
      response: response ? this.sanitizeResponse(response) : undefined,
      error: error ? this.serializeError(error) : undefined
    };
    
    const level = status === 'failure' ? LogLevel.ERROR : 
                 status === 'timeout' ? LogLevel.WARN : LogLevel.INFO;
    
    await this.log(level, LogCategory.INTEGRATION, message, context, metadata);
  }
  
  // Utility methods
  private serializeError(error: Error): ErrorDetails {
    return {
      name: error.name,
      message: error.message,
      stack: error.stack,
      code: (error as any).code,
      cause: (error as any).cause ? this.serializeError((error as any).cause) : undefined,
      context: (error as any).context
    };
  }
  
  private sanitizeInput(input: any): any {
    return this.sanitizeData(input, ['password', 'token', 'secret', 'key', 'credential']);
  }
  
  private sanitizeResult(result: any): any {
    const maxSize = 1000;
    const resultStr = JSON.stringify(result);
    
    if (resultStr.length > maxSize) {
      return {
        truncated: true,
        size: resultStr.length,
        preview: resultStr.substring(0, maxSize) + '...'
      };
    }
    
    return this.sanitizeData(result, ['password', 'token', 'secret', 'key', 'credential']);
  }
  
  private sanitizeSecurityDetails(details: any): any {
    return this.sanitizeData(details, [
      'password', 'token', 'secret', 'key', 'credential',
      'ssn', 'credit_card', 'phone', 'email'
    ]);
  }
  
  private sanitizeUserDetails(details: any): any {
    return this.sanitizeData(details, [
      'password', 'ssn', 'credit_card', 'phone'
    ]);
  }
  
  private sanitizeRequest(request: any): any {
    return this.sanitizeData(request, [
      'authorization', 'x-api-key', 'password', 'token'
    ]);
  }
  
  private sanitizeResponse(response: any): any {
    return this.sanitizeData(response, [
      'password', 'token', 'secret', 'key'
    ]);
  }
  
  private sanitizeData(data: any, sensitiveKeys: string[]): any {
    if (typeof data !== 'object' || data === null) {
      return data;
    }
    
    if (Array.isArray(data)) {
      return data.map(item => this.sanitizeData(item, sensitiveKeys));
    }
    
    const sanitized = { ...data };
    
    for (const key of Object.keys(sanitized)) {
      const lowerKey = key.toLowerCase();
      if (sensitiveKeys.some(sensitive => lowerKey.includes(sensitive))) {
        sanitized[key] = '[REDACTED]';
      } else if (typeof sanitized[key] === 'object') {
        sanitized[key] = this.sanitizeData(sanitized[key], sensitiveKeys);
      }
    }
    
    return sanitized;
  }
  
  private hashSensitiveData(data: string): string {
    const crypto = require('crypto');
    return crypto.createHash('sha256').update(data).digest('hex').substring(0, 8);
  }
  
  private generateTags(
    category: LogCategory,
    level: LogLevel,
    context?: Partial<LogContext>
  ): string[] {
    const tags = [category, LogLevel[level].toLowerCase()];
    
    if (context?.component) {
      tags.push(`component:${context.component}`);
    }
    
    if (context?.operation) {
      tags.push(`operation:${context.operation}`);
    }
    
    if (context?.environment) {
      tags.push(`env:${context.environment}`);
    }
    
    return tags;
  }
  
  private shouldAudit(logEntry: LogEntry): boolean {
    // Audit security events, user actions, and errors
    return logEntry.category === LogCategory.SECURITY ||
           logEntry.category === LogCategory.USER ||
           logEntry.category === LogCategory.AUDIT ||
           logEntry.level >= LogLevel.ERROR;
  }
  
  private checkThresholdViolations(
    metrics: PerformanceMetrics,
    thresholds: PerformanceThresholds
  ): string[] {
    const violations: string[] = [];
    
    if (metrics.responseTime > thresholds.maxResponseTime) {
      violations.push(`Response time exceeded: ${metrics.responseTime}ms > ${thresholds.maxResponseTime}ms`);
    }
    
    if (metrics.memoryUsage > thresholds.maxMemoryUsage) {
      violations.push(`Memory usage exceeded: ${metrics.memoryUsage}MB > ${thresholds.maxMemoryUsage}MB`);
    }
    
    if (metrics.cpuUsage > thresholds.maxCpuUsage) {
      violations.push(`CPU usage exceeded: ${metrics.cpuUsage}% > ${thresholds.maxCpuUsage}%`);
    }
    
    return violations;
  }
}
```

---

## 📁 Log Writers & Storage

### Multi-Destination Log Writers
```typescript
interface LogWriter {
  write(logEntry: LogEntry): Promise<void>;
  flush?(): Promise<void>;
  close?(): Promise<void>;
}

class FileLogWriter implements LogWriter {
  private filePath: string;
  private rotationConfig: RotationConfig;
  private currentFile: string;
  private writeStream: WriteStream;
  
  constructor(filePath: string, rotationConfig?: RotationConfig) {
    this.filePath = filePath;
    this.rotationConfig = rotationConfig || {
      maxSize: 100 * 1024 * 1024, // 100MB
      maxFiles: 10,
      rotateDaily: true
    };
    this.initializeStream();
  }
  
  async write(logEntry: LogEntry): Promise<void> {
    await this.checkRotation();
    
    const logLine = this.formatLogEntry(logEntry);
    
    return new Promise((resolve, reject) => {
      this.writeStream.write(logLine + '\n', (error) => {
        if (error) reject(error);
        else resolve();
      });
    });
  }
  
  private formatLogEntry(logEntry: LogEntry): string {
    return JSON.stringify({
      '@timestamp': logEntry.timestamp,
      level: LogLevel[logEntry.level],
      category: logEntry.category,
      source: logEntry.source,
      message: logEntry.message,
      correlationId: logEntry.correlationId,
      traceId: logEntry.traceId,
      context: logEntry.context,
      metadata: logEntry.metadata,
      tags: logEntry.tags
    });
  }
  
  private async checkRotation(): Promise<void> {
    if (this.shouldRotate()) {
      await this.rotateLog();
    }
  }
  
  private shouldRotate(): boolean {
    const stats = fs.statSync(this.currentFile);
    
    // Check file size
    if (stats.size >= this.rotationConfig.maxSize) {
      return true;
    }
    
    // Check daily rotation
    if (this.rotationConfig.rotateDaily) {
      const fileDate = new Date(stats.birthtime);
      const today = new Date();
      
      if (fileDate.toDateString() !== today.toDateString()) {
        return true;
      }
    }
    
    return false;
  }
  
  private async rotateLog(): Promise<void> {
    await this.closeStream();
    
    // Move current file to archived name
    const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
    const archivedFile = `${this.filePath}.${timestamp}`;
    
    fs.renameSync(this.currentFile, archivedFile);
    
    // Compress archived file
    await this.compressFile(archivedFile);
    
    // Clean up old files
    await this.cleanupOldFiles();
    
    // Initialize new stream
    this.initializeStream();
  }
}

class ElasticsearchLogWriter implements LogWriter {
  private client: ElasticsearchClient;
  private indexPattern: string;
  private batchSize: number;
  private flushInterval: number;
  private batch: LogEntry[] = [];
  private flushTimer: NodeJS.Timeout;
  
  constructor(config: ElasticsearchConfig) {
    this.client = new ElasticsearchClient(config);
    this.indexPattern = config.indexPattern || 'god-logs-{YYYY.MM.DD}';
    this.batchSize = config.batchSize || 100;
    this.flushInterval = config.flushInterval || 5000;
    this.startFlushTimer();
  }
  
  async write(logEntry: LogEntry): Promise<void> {
    this.batch.push(logEntry);
    
    if (this.batch.length >= this.batchSize) {
      await this.flush();
    }
  }
  
  async flush(): Promise<void> {
    if (this.batch.length === 0) return;
    
    const currentBatch = this.batch.splice(0);
    const indexName = this.generateIndexName();
    
    const body = currentBatch.flatMap(entry => [
      { index: { _index: indexName } },
      this.transformLogEntry(entry)
    ]);
    
    try {
      await this.client.bulk({ body });
    } catch (error) {
      console.error('Failed to write logs to Elasticsearch:', error);
      // Could implement retry logic here
    }
  }
  
  private transformLogEntry(logEntry: LogEntry): any {
    return {
      '@timestamp': logEntry.timestamp,
      level: LogLevel[logEntry.level],
      category: logEntry.category,
      source: logEntry.source,
      message: logEntry.message,
      correlation_id: logEntry.correlationId,
      trace_id: logEntry.traceId,
      span_id: logEntry.spanId,
      user_id: logEntry.userId,
      session_id: logEntry.sessionId,
      agent_id: logEntry.agentId,
      workflow_id: logEntry.workflowId,
      step_id: logEntry.stepId,
      context: logEntry.context,
      metadata: logEntry.metadata,
      tags: logEntry.tags,
      duration: logEntry.duration,
      error: logEntry.error
    };
  }
  
  private generateIndexName(): string {
    const now = new Date();
    return this.indexPattern
      .replace('{YYYY}', now.getFullYear().toString())
      .replace('{MM}', (now.getMonth() + 1).toString().padStart(2, '0'))
      .replace('{DD}', now.getDate().toString().padStart(2, '0'));
  }
}

class DatabaseLogWriter implements LogWriter {
  private database: Database;
  private tableName: string;
  private batchSize: number;
  private batch: LogEntry[] = [];
  
  constructor(database: Database, tableName: string = 'god_logs', batchSize: number = 50) {
    this.database = database;
    this.tableName = tableName;
    this.batchSize = batchSize;
  }
  
  async write(logEntry: LogEntry): Promise<void> {
    this.batch.push(logEntry);
    
    if (this.batch.length >= this.batchSize) {
      await this.flush();
    }
  }
  
  async flush(): Promise<void> {
    if (this.batch.length === 0) return;
    
    const currentBatch = this.batch.splice(0);
    
    const query = `
      INSERT INTO ${this.tableName} (
        timestamp, level, category, source, message,
        correlation_id, trace_id, span_id, user_id, session_id,
        agent_id, workflow_id, step_id, context, metadata,
        tags, duration, error
      ) VALUES ${currentBatch.map(() => '(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)').join(', ')}
    `;
    
    const values = currentBatch.flatMap(entry => [
      entry.timestamp,
      entry.level,
      entry.category,
      entry.source,
      entry.message,
      entry.correlationId,
      entry.traceId,
      entry.spanId,
      entry.userId,
      entry.sessionId,
      entry.agentId,
      entry.workflowId,
      entry.stepId,
      JSON.stringify(entry.context),
      JSON.stringify(entry.metadata),
      JSON.stringify(entry.tags),
      entry.duration,
      JSON.stringify(entry.error)
    ]);
    
    try {
      await this.database.execute(query, values);
    } catch (error) {
      console.error('Failed to write logs to database:', error);
    }
  }
}

class ConsoleLogWriter implements LogWriter {
  private colorize: boolean;
  private format: 'json' | 'pretty';
  
  constructor(colorize: boolean = true, format: 'json' | 'pretty' = 'pretty') {
    this.colorize = colorize;
    this.format = format;
  }
  
  async write(logEntry: LogEntry): Promise<void> {
    if (this.format === 'json') {
      console.log(JSON.stringify(logEntry));
    } else {
      const formatted = this.formatPretty(logEntry);
      console.log(formatted);
    }
  }
  
  private formatPretty(logEntry: LogEntry): string {
    const timestamp = new Date(logEntry.timestamp).toISOString();
    const level = LogLevel[logEntry.level].padEnd(5);
    const category = logEntry.category.padEnd(10);
    const source = logEntry.source.padEnd(15);
    
    let formatted = `${timestamp} [${level}] [${category}] [${source}] ${logEntry.message}`;
    
    if (logEntry.correlationId) {
      formatted += ` (${logEntry.correlationId})`;
    }
    
    if (logEntry.duration) {
      formatted += ` [${logEntry.duration}ms]`;
    }
    
    if (this.colorize) {
      formatted = this.applyColors(formatted, logEntry.level);
    }
    
    return formatted;
  }
  
  private applyColors(text: string, level: LogLevel): string {
    const colors = {
      [LogLevel.TRACE]: '\x1b[90m', // Gray
      [LogLevel.DEBUG]: '\x1b[36m', // Cyan
      [LogLevel.INFO]: '\x1b[32m',  // Green
      [LogLevel.WARN]: '\x1b[33m',  // Yellow
      [LogLevel.ERROR]: '\x1b[31m', // Red
      [LogLevel.FATAL]: '\x1b[35m'  // Magenta
    };
    
    const reset = '\x1b[0m';
    return `${colors[level]}${text}${reset}`;
  }
}
```

---

## 🔍 Log Enrichers & Filters

### Context Enrichment
```typescript
interface LogEnricher {
  enrich(logEntry: LogEntry): Promise<LogEntry>;
}

class ContextEnricher implements LogEnricher {
  async enrich(logEntry: LogEntry): Promise<LogEntry> {
    return {
      ...logEntry,
      context: {
        ...logEntry.context,
        hostname: process.env.HOSTNAME || require('os').hostname(),
        processId: process.pid,
        nodeVersion: process.version,
        platform: process.platform,
        architecture: process.arch,
        uptime: process.uptime()
      }
    };
  }
}

class PerformanceEnricher implements LogEnricher {
  async enrich(logEntry: LogEntry): Promise<LogEntry> {
    const memoryUsage = process.memoryUsage();
    const cpuUsage = process.cpuUsage();
    
    return {
      ...logEntry,
      metadata: {
        ...logEntry.metadata,
        performance: {
          memory: {
            rss: memoryUsage.rss,
            heapUsed: memoryUsage.heapUsed,
            heapTotal: memoryUsage.heapTotal,
            external: memoryUsage.external
          },
          cpu: {
            user: cpuUsage.user,
            system: cpuUsage.system
          },
          loadAverage: require('os').loadavg()
        }
      }
    };
  }
}

class SecurityEnricher implements LogEnricher {
  async enrich(logEntry: LogEntry): Promise<LogEntry> {
    return {
      ...logEntry,
      metadata: {
        ...logEntry.metadata,
        security: {
          userAgent: logEntry.context.userAgent ? 
            this.hashSensitiveData(logEntry.context.userAgent) : undefined,
          clientIp: logEntry.context.clientIp ? 
            this.hashSensitiveData(logEntry.context.clientIp) : undefined,
          sessionId: logEntry.sessionId
        }
      }
    };
  }
  
  private hashSensitiveData(data: string): string {
    const crypto = require('crypto');
    return crypto.createHash('sha256').update(data).digest('hex').substring(0, 8);
  }
}

class GeoLocationEnricher implements LogEnricher {
  private geoService: GeoLocationService;
  
  constructor(geoService: GeoLocationService) {
    this.geoService = geoService;
  }
  
  async enrich(logEntry: LogEntry): Promise<LogEntry> {
    if (!logEntry.context.clientIp) {
      return logEntry;
    }
    
    try {
      const geoData = await this.geoService.lookup(logEntry.context.clientIp);
      
      return {
        ...logEntry,
        metadata: {
          ...logEntry.metadata,
          geo: {
            country: geoData.country,
            region: geoData.region,
            city: geoData.city,
            timezone: geoData.timezone
          }
        }
      };
    } catch (error) {
      // Geo lookup failed, continue without geo data
      return logEntry;
    }
  }
}

// Log Filters
interface LogFilter {
  shouldLog(logEntry: LogEntry): Promise<boolean>;
}

class LevelFilter implements LogFilter {
  private minLevel: LogLevel;
  
  constructor(minLevel: LogLevel) {
    this.minLevel = minLevel;
  }
  
  async shouldLog(logEntry: LogEntry): Promise<boolean> {
    return logEntry.level >= this.minLevel;
  }
}

class CategoryFilter implements LogFilter {
  private allowedCategories: Set<LogCategory>;
  
  constructor(allowedCategories: LogCategory[]) {
    this.allowedCategories = new Set(allowedCategories);
  }
  
  async shouldLog(logEntry: LogEntry): Promise<boolean> {
    return this.allowedCategories.has(logEntry.category);
  }
}

class RateLimitFilter implements LogFilter {
  private rateLimits: Map<string, RateLimitState> = new Map();
  private maxLogsPerMinute: number;
  
  constructor(maxLogsPerMinute: number = 1000) {
    this.maxLogsPerMinute = maxLogsPerMinute;
  }
  
  async shouldLog(logEntry: LogEntry): Promise<boolean> {
    const key = `${logEntry.source}:${logEntry.category}`;
    const now = Date.now();
    const windowStart = now - 60000; // 1 minute window
    
    let state = this.rateLimits.get(key);
    if (!state) {
      state = { count: 0, windowStart: now };
      this.rateLimits.set(key, state);
    }
    
    // Reset window if needed
    if (now - state.windowStart >= 60000) {
      state.count = 0;
      state.windowStart = now;
    }
    
    state.count++;
    
    if (state.count > this.maxLogsPerMinute) {
      // Log rate limit exceeded message (but only once per window)
      if (state.count === this.maxLogsPerMinute + 1) {
        console.warn(`Rate limit exceeded for ${key}. Suppressing further logs in this window.`);
      }
      return false;
    }
    
    return true;
  }
}

class SensitiveDataFilter implements LogFilter {
  private sensitivePatterns: RegExp[];
  
  constructor() {
    this.sensitivePatterns = [
      /\b\d{4}[\s-]?\d{4}[\s-]?\d{4}[\s-]?\d{4}\b/, // Credit card numbers
      /\b\d{3}-\d{2}-\d{4}\b/, // SSN
      /\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b/, // Email addresses
      /\b(?:\+?1[-.]?)?\(?\d{3}\)?[-.]?\d{3}[-.]?\d{4}\b/, // Phone numbers
      /\b(?:password|token|secret|key)\s*[:=]\s*["']?[^\s"']+["']?/i // Credentials
    ];
  }
  
  async shouldLog(logEntry: LogEntry): Promise<boolean> {
    const textToCheck = `${logEntry.message} ${JSON.stringify(logEntry.metadata)}`;
    
    for (const pattern of this.sensitivePatterns) {
      if (pattern.test(textToCheck)) {
        console.warn(`Sensitive data detected in log entry. Entry suppressed.`);
        return false;
      }
    }
    
    return true;
  }
}
```

---

## 📊 Audit Logging

### Compliance Audit Trail
```typescript
class AuditLogger {
  private auditWriter: LogWriter;
  private encryptionKey: string;
  private signatureKey: string;
  
  constructor(config: AuditConfig) {
    this.auditWriter = new FileLogWriter(
      config.auditLogPath || '/var/log/god/audit.log',
      {
        maxSize: 50 * 1024 * 1024, // 50MB
        maxFiles: 100,
        rotateDaily: true
      }
    );
    this.encryptionKey = config.encryptionKey;
    this.signatureKey = config.signatureKey;
  }
  
  async log(logEntry: LogEntry): Promise<void> {
    const auditEntry: AuditEntry = {
      id: this.generateAuditId(),
      timestamp: new Date().toISOString(),
      originalEntry: logEntry,
      checksum: this.calculateChecksum(logEntry),
      signature: this.signEntry(logEntry)
    };
    
    // Encrypt sensitive audit entries
    if (this.shouldEncrypt(logEntry)) {
      auditEntry.encrypted = true;
      auditEntry.originalEntry = this.encryptEntry(logEntry);
    }
    
    await this.auditWriter.write(auditEntry as any);
  }
  
  async verifyAuditTrail(startDate: Date, endDate: Date): Promise<AuditVerificationResult> {
    // Implementation for audit trail verification
    // This would read audit logs and verify checksums and signatures
    return {
      verified: true,
      totalEntries: 0,
      verifiedEntries: 0,
      failedEntries: [],
      integrityScore: 100
    };
  }
  
  private generateAuditId(): string {
    return `audit_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
  }
  
  private calculateChecksum(logEntry: LogEntry): string {
    const crypto = require('crypto');
    const data = JSON.stringify(logEntry);
    return crypto.createHash('sha256').update(data).digest('hex');
  }
  
  private signEntry(logEntry: LogEntry): string {
    const crypto = require('crypto');
    const data = JSON.stringify(logEntry);
    const hmac = crypto.createHmac('sha256', this.signatureKey);
    hmac.update(data);
    return hmac.digest('hex');
  }
  
  private shouldEncrypt(logEntry: LogEntry): boolean {
    return logEntry.category === LogCategory.SECURITY ||
           logEntry.category === LogCategory.USER ||
           logEntry.level >= LogLevel.ERROR;
  }
  
  private encryptEntry(logEntry: LogEntry): any {
    const crypto = require('crypto');
    const algorithm = 'aes-256-gcm';
    const iv = crypto.randomBytes(16);
    const cipher = crypto.createCipher(algorithm, this.encryptionKey);
    
    const data = JSON.stringify(logEntry);
    let encrypted = cipher.update(data, 'utf8', 'hex');
    encrypted += cipher.final('hex');
    
    return {
      algorithm,
      iv: iv.toString('hex'),
      data: encrypted,
      tag: cipher.getAuthTag().toString('hex')
    };
  }
}

interface AuditEntry {
  id: string;
  timestamp: string;
  originalEntry: LogEntry | any;
  checksum: string;
  signature: string;
  encrypted?: boolean;
}

interface AuditVerificationResult {
  verified: boolean;
  totalEntries: number;
  verifiedEntries: number;
  failedEntries: AuditFailure[];
  integrityScore: number;
}

interface AuditFailure {
  entryId: string;
  reason: string;
  timestamp: string;
}
```

---

## 🔧 Log Management & Retention

### Automated Log Management
```typescript
class LogManager {
  private retentionPolicies: Map<LogCategory, RetentionPolicy> = new Map();
  private archiveService: ArchiveService;
  private compressionService: CompressionService;
  
  constructor() {
    this.archiveService = new ArchiveService();
    this.compressionService = new CompressionService();
    this.initializeRetentionPolicies();
  }
  
  private initializeRetentionPolicies(): void {
    // Security logs - keep for 7 years for compliance
    this.retentionPolicies.set(LogCategory.SECURITY, {
      retentionDays: 2555, // 7 years
      archiveAfterDays: 90,
      compressionAfterDays: 30,
      deleteAfterDays: 2555
    });
    
    // Audit logs - keep for 7 years for compliance
    this.retentionPolicies.set(LogCategory.AUDIT, {
      retentionDays: 2555, // 7 years
      archiveAfterDays: 90,
      compressionAfterDays: 30,
      deleteAfterDays: 2555
    });
    
    // User logs - keep for 2 years
    this.retentionPolicies.set(LogCategory.USER, {
      retentionDays: 730, // 2 years
      archiveAfterDays: 90,
      compressionAfterDays: 30,
      deleteAfterDays: 730
    });
    
    // System logs - keep for 1 year
    this.retentionPolicies.set(LogCategory.SYSTEM, {
      retentionDays: 365, // 1 year
      archiveAfterDays: 30,
      compressionAfterDays: 7,
      deleteAfterDays: 365
    });
    
    // Agent logs - keep for 6 months
    this.retentionPolicies.set(LogCategory.AGENT, {
      retentionDays: 180, // 6 months
      archiveAfterDays: 30,
      compressionAfterDays: 7,
      deleteAfterDays: 180
    });
    
    // Performance logs - keep for 3 months
    this.retentionPolicies.set(LogCategory.PERFORMANCE, {
      retentionDays: 90, // 3 months
      archiveAfterDays: 30,
      compressionAfterDays: 7,
      deleteAfterDays: 90
    });
  }
  
  async runRetentionPolicy(): Promise<RetentionResult> {
    const results: RetentionResult = {
      processed: 0,
      compressed: 0,
      archived: 0,
      deleted: 0,
      errors: []
    };
    
    for (const [category, policy] of this.retentionPolicies) {
      try {
        const categoryResult = await this.processCategory(category, policy);
        results.processed += categoryResult.processed;
        results.compressed += categoryResult.compressed;
        results.archived += categoryResult.archived;
        results.deleted += categoryResult.deleted;
      } catch (error) {
        results.errors.push({
          category,
          error: error.message
        });
      }
    }
    
    return results;
  }
  
  private async processCategory(
    category: LogCategory,
    policy: RetentionPolicy
  ): Promise<RetentionResult> {
    const now = new Date();
    const result: RetentionResult = {
      processed: 0,
      compressed: 0,
      archived: 0,
      deleted: 0,
      errors: []
    };
    
    // Find log files for this category
    const logFiles = await this.findLogFiles(category);
    
    for (const logFile of logFiles) {
      const fileAge = this.calculateFileAge(logFile.path);
      result.processed++;
      
      try {
        // Delete old files
        if (fileAge >= policy.deleteAfterDays) {
          await this.deleteLogFile(logFile.path);
          result.deleted++;
          continue;
        }
        
        // Archive files
        if (fileAge >= policy.archiveAfterDays && !logFile.archived) {
          await this.archiveService.archive(logFile.path);
          result.archived++;
        }
        
        // Compress files
        if (fileAge >= policy.compressionAfterDays && !logFile.compressed) {
          await this.compressionService.compress(logFile.path);
          result.compressed++;
        }
      } catch (error) {
        result.errors.push({
          file: logFile.path,
          error: error.message
        });
      }
    }
    
    return result;
  }
}

interface RetentionPolicy {
  retentionDays: number;
  archiveAfterDays: number;
  compressionAfterDays: number;
  deleteAfterDays: number;
}

interface RetentionResult {
  processed: number;
  compressed: number;
  archived: number;
  deleted: number;
  errors: any[];
}
```

---

## Integration with .god Ecosystem

### Sub-Agent Logging Integration
- **Security Auditor**: Security event logging, vulnerability tracking, compliance audit trails
- **Bug Hunter**: Bug detection logging, code quality metrics, technical debt tracking
- **Test Executor**: Test execution logging, coverage tracking, performance testing logs
- **Performance Analyzer**: Performance metrics logging, bottleneck detection, resource usage tracking
- **Context Optimizer**: Context optimization logging, cache performance, efficiency metrics

### Workflow Logging Integration
- **TSDDR 2.0**: End-to-end workflow logging, step-by-step execution tracking
- **Kiro Workflow**: Task execution logging, quality gate tracking
- **Agent Coordination**: Cross-agent communication logging, load balancing metrics

### Logging Quality Gates
```typescript
class LoggingQualityGates {
  async validateLoggingHealth(): Promise<LoggingHealthResult> {
    const validations = [
      this.validateLogWriters(),
      this.validateLogLevels(),
      this.validateRetentionPolicies(),
      this.validateAuditTrail(),
      this.validateSensitiveDataHandling()
    ];
    
    const results = await Promise.all(validations);
    const passed = results.every(result => result.passed);
    
    return {
      passed,
      results,
      overallHealth: this.calculateLoggingHealth(results),
      recommendations: this.generateRecommendations(results)
    };
  }
}
```

---

**Usage**: All .god ecosystem components requiring logging  
**Enforcement**: Automated logging health checks in CI/CD  
**Compliance**: GDPR, SOX, HIPAA compliant audit trails  
**Evolution**: Continuous logging pattern optimization based on usage analytics