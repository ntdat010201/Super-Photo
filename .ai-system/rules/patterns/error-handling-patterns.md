# Error Handling Patterns & Recovery Strategies

> **🛡️ Comprehensive Error Management Framework**  
> Robust error handling patterns for resilient, self-healing systems

## Error Handling Philosophy

**Mission**: Build resilient systems that gracefully handle failures and recover automatically  
**Approach**: Fail fast, fail safe, recover smart with comprehensive error context  
**Principle**: Every error is an opportunity to improve system reliability and user experience

---

## 🎯 Core Error Handling Principles

### Universal Standards

**Fail Fast**: Detect errors as early as possible in the execution chain  
**Fail Safe**: Ensure system remains in a safe state during failures  
**Graceful Degradation**: Maintain partial functionality when components fail  
**Context Preservation**: Capture comprehensive error context for debugging  
**User-Centric**: Provide meaningful error messages to end users

### Error Categories

1. **System Errors**: Infrastructure, network, database failures
2. **Application Errors**: Business logic, validation, processing errors
3. **User Errors**: Invalid input, unauthorized access, quota exceeded
4. **Integration Errors**: External API failures, service unavailability
5. **Performance Errors**: Timeouts, resource exhaustion, rate limits

---

## 🔍 Error Classification System

### Error Severity Levels
```typescript
enum ErrorSeverity {
  CRITICAL = 'critical',    // System down, data loss risk
  HIGH = 'high',           // Major functionality impacted
  MEDIUM = 'medium',       // Minor functionality impacted
  LOW = 'low',            // Cosmetic issues, warnings
  INFO = 'info'           // Informational, no action needed
}

enum ErrorCategory {
  SYSTEM = 'system',
  APPLICATION = 'application',
  USER = 'user',
  INTEGRATION = 'integration',
  PERFORMANCE = 'performance',
  SECURITY = 'security'
}

enum ErrorType {
  // System errors
  DATABASE_CONNECTION = 'database_connection',
  NETWORK_FAILURE = 'network_failure',
  FILE_SYSTEM = 'file_system',
  MEMORY_EXHAUSTION = 'memory_exhaustion',
  
  // Application errors
  VALIDATION_FAILED = 'validation_failed',
  BUSINESS_RULE_VIOLATION = 'business_rule_violation',
  PROCESSING_FAILED = 'processing_failed',
  
  // User errors
  INVALID_INPUT = 'invalid_input',
  UNAUTHORIZED = 'unauthorized',
  FORBIDDEN = 'forbidden',
  NOT_FOUND = 'not_found',
  
  // Integration errors
  EXTERNAL_API_FAILURE = 'external_api_failure',
  SERVICE_UNAVAILABLE = 'service_unavailable',
  TIMEOUT = 'timeout',
  
  // Performance errors
  RATE_LIMIT_EXCEEDED = 'rate_limit_exceeded',
  QUOTA_EXCEEDED = 'quota_exceeded',
  SLOW_RESPONSE = 'slow_response'
}
```

### Comprehensive Error Interface
```typescript
interface SystemError {
  // Core identification
  id: string;
  code: string;
  type: ErrorType;
  category: ErrorCategory;
  severity: ErrorSeverity;
  
  // Error details
  message: string;
  userMessage?: string;
  technicalDetails?: string;
  
  // Context information
  timestamp: string;
  source: {
    component: string;
    method?: string;
    file?: string;
    line?: number;
  };
  
  // Execution context
  context: {
    requestId?: string;
    userId?: string;
    sessionId?: string;
    operation?: string;
    parameters?: Record<string, any>;
  };
  
  // Technical details
  stackTrace?: string[];
  innerError?: SystemError;
  relatedErrors?: string[];
  
  // Recovery information
  recoverable: boolean;
  retryable: boolean;
  retryAfter?: number;
  suggestedActions?: string[];
  
  // Impact assessment
  impact: {
    affectedUsers?: number;
    affectedOperations?: string[];
    businessImpact?: string;
    estimatedDowntime?: number;
  };
  
  // Metadata
  metadata: {
    environment: string;
    version: string;
    buildId?: string;
    correlationId?: string;
  };
}
```

---

## 🛠️ Error Handling Patterns

### 1. Try-Catch-Finally Pattern
```typescript
class ErrorHandler {
  static async executeWithErrorHandling<T>(
    operation: () => Promise<T>,
    context: OperationContext,
    options: ErrorHandlingOptions = {}
  ): Promise<Result<T>> {
    const startTime = Date.now();
    let result: Result<T>;
    
    try {
      // Pre-execution validation
      this.validatePreconditions(context);
      
      // Execute operation with timeout
      const timeoutPromise = this.createTimeoutPromise(options.timeout);
      const operationPromise = operation();
      
      const data = await Promise.race([operationPromise, timeoutPromise]);
      
      // Post-execution validation
      this.validatePostconditions(data, context);
      
      result = {
        success: true,
        data,
        metadata: {
          executionTime: Date.now() - startTime,
          context
        }
      };
      
    } catch (error) {
      result = await this.handleError(error, context, {
        executionTime: Date.now() - startTime,
        operation: operation.name
      });
      
    } finally {
      // Cleanup operations
      await this.cleanup(context);
      
      // Record metrics
      this.recordMetrics(result, context, Date.now() - startTime);
      
      // Trigger monitoring alerts if needed
      if (!result.success && result.error?.severity === ErrorSeverity.CRITICAL) {
        await this.triggerAlert(result.error, context);
      }
    }
    
    return result;
  }
  
  private static async handleError(
    error: any,
    context: OperationContext,
    metadata: any
  ): Promise<Result<never>> {
    // Convert to SystemError
    const systemError = this.convertToSystemError(error, context, metadata);
    
    // Log error with full context
    await this.logError(systemError);
    
    // Attempt recovery if possible
    const recoveryResult = await this.attemptRecovery(systemError, context);
    
    return {
      success: false,
      error: systemError,
      recovery: recoveryResult
    };
  }
}

// Usage example
const result = await ErrorHandler.executeWithErrorHandling(
  async () => {
    return await userService.createUser(userData);
  },
  {
    operation: 'create_user',
    userId: 'user_123',
    requestId: 'req_456'
  },
  {
    timeout: 5000,
    retryAttempts: 3,
    retryDelay: 1000
  }
);

if (result.success) {
  console.log('User created:', result.data);
} else {
  console.error('Failed to create user:', result.error);
}
```

### 2. Circuit Breaker Pattern
```typescript
enum CircuitState {
  CLOSED = 'closed',     // Normal operation
  OPEN = 'open',         // Failing, rejecting requests
  HALF_OPEN = 'half_open' // Testing if service recovered
}

class CircuitBreaker {
  private state: CircuitState = CircuitState.CLOSED;
  private failureCount = 0;
  private lastFailureTime = 0;
  private successCount = 0;
  
  constructor(
    private readonly options: {
      failureThreshold: number;
      recoveryTimeout: number;
      monitoringPeriod: number;
      halfOpenMaxCalls: number;
    }
  ) {}
  
  async execute<T>(operation: () => Promise<T>): Promise<T> {
    if (this.state === CircuitState.OPEN) {
      if (this.shouldAttemptReset()) {
        this.state = CircuitState.HALF_OPEN;
        this.successCount = 0;
      } else {
        throw new SystemError({
          id: generateId(),
          code: 'CIRCUIT_BREAKER_OPEN',
          type: ErrorType.SERVICE_UNAVAILABLE,
          category: ErrorCategory.SYSTEM,
          severity: ErrorSeverity.HIGH,
          message: 'Circuit breaker is open, service unavailable',
          recoverable: true,
          retryable: true,
          retryAfter: this.options.recoveryTimeout
        });
      }
    }
    
    try {
      const result = await operation();
      this.onSuccess();
      return result;
      
    } catch (error) {
      this.onFailure();
      throw error;
    }
  }
  
  private onSuccess(): void {
    this.failureCount = 0;
    
    if (this.state === CircuitState.HALF_OPEN) {
      this.successCount++;
      if (this.successCount >= this.options.halfOpenMaxCalls) {
        this.state = CircuitState.CLOSED;
      }
    }
  }
  
  private onFailure(): void {
    this.failureCount++;
    this.lastFailureTime = Date.now();
    
    if (this.failureCount >= this.options.failureThreshold) {
      this.state = CircuitState.OPEN;
    }
  }
  
  private shouldAttemptReset(): boolean {
    return Date.now() - this.lastFailureTime >= this.options.recoveryTimeout;
  }
}

// Usage
const circuitBreaker = new CircuitBreaker({
  failureThreshold: 5,
  recoveryTimeout: 60000, // 1 minute
  monitoringPeriod: 10000, // 10 seconds
  halfOpenMaxCalls: 3
});

const result = await circuitBreaker.execute(async () => {
  return await externalApiService.getData();
});
```

### 3. Retry Pattern with Exponential Backoff
```typescript
interface RetryOptions {
  maxAttempts: number;
  baseDelay: number;
  maxDelay: number;
  backoffMultiplier: number;
  jitter: boolean;
  retryableErrors: ErrorType[];
}

class RetryHandler {
  static async executeWithRetry<T>(
    operation: () => Promise<T>,
    options: RetryOptions
  ): Promise<T> {
    let lastError: SystemError;
    
    for (let attempt = 1; attempt <= options.maxAttempts; attempt++) {
      try {
        return await operation();
        
      } catch (error) {
        lastError = this.convertToSystemError(error);
        
        // Check if error is retryable
        if (!this.isRetryable(lastError, options.retryableErrors)) {
          throw lastError;
        }
        
        // Don't delay on last attempt
        if (attempt === options.maxAttempts) {
          break;
        }
        
        // Calculate delay with exponential backoff
        const delay = this.calculateDelay(attempt, options);
        
        // Log retry attempt
        console.warn(`Operation failed, retrying in ${delay}ms (attempt ${attempt}/${options.maxAttempts})`, {
          error: lastError.message,
          attempt,
          delay
        });
        
        await this.sleep(delay);
      }
    }
    
    // All attempts failed
    throw new SystemError({
      ...lastError,
      message: `Operation failed after ${options.maxAttempts} attempts: ${lastError.message}`,
      metadata: {
        ...lastError.metadata,
        totalAttempts: options.maxAttempts,
        finalAttempt: true
      }
    });
  }
  
  private static calculateDelay(attempt: number, options: RetryOptions): number {
    let delay = options.baseDelay * Math.pow(options.backoffMultiplier, attempt - 1);
    delay = Math.min(delay, options.maxDelay);
    
    // Add jitter to prevent thundering herd
    if (options.jitter) {
      delay = delay * (0.5 + Math.random() * 0.5);
    }
    
    return Math.floor(delay);
  }
  
  private static isRetryable(error: SystemError, retryableErrors: ErrorType[]): boolean {
    return error.retryable && retryableErrors.includes(error.type);
  }
  
  private static sleep(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}

// Usage
const result = await RetryHandler.executeWithRetry(
  async () => {
    return await databaseService.query('SELECT * FROM users');
  },
  {
    maxAttempts: 3,
    baseDelay: 1000,
    maxDelay: 10000,
    backoffMultiplier: 2,
    jitter: true,
    retryableErrors: [
      ErrorType.DATABASE_CONNECTION,
      ErrorType.TIMEOUT,
      ErrorType.NETWORK_FAILURE
    ]
  }
);
```

### 4. Bulkhead Pattern
```typescript
class ResourcePool {
  private readonly pools: Map<string, Semaphore> = new Map();
  
  constructor(private readonly config: Record<string, number>) {
    for (const [resource, limit] of Object.entries(config)) {
      this.pools.set(resource, new Semaphore(limit));
    }
  }
  
  async executeWithResourceLimit<T>(
    resource: string,
    operation: () => Promise<T>
  ): Promise<T> {
    const semaphore = this.pools.get(resource);
    if (!semaphore) {
      throw new SystemError({
        id: generateId(),
        code: 'UNKNOWN_RESOURCE',
        type: ErrorType.PROCESSING_FAILED,
        category: ErrorCategory.APPLICATION,
        severity: ErrorSeverity.MEDIUM,
        message: `Unknown resource pool: ${resource}`,
        recoverable: false,
        retryable: false
      });
    }
    
    const acquired = await semaphore.acquire();
    if (!acquired) {
      throw new SystemError({
        id: generateId(),
        code: 'RESOURCE_EXHAUSTED',
        type: ErrorType.QUOTA_EXCEEDED,
        category: ErrorCategory.PERFORMANCE,
        severity: ErrorSeverity.HIGH,
        message: `Resource pool '${resource}' is exhausted`,
        recoverable: true,
        retryable: true,
        retryAfter: 1000
      });
    }
    
    try {
      return await operation();
    } finally {
      semaphore.release();
    }
  }
}

class Semaphore {
  private permits: number;
  private readonly waitQueue: Array<(value: boolean) => void> = [];
  
  constructor(private readonly maxPermits: number) {
    this.permits = maxPermits;
  }
  
  async acquire(timeout: number = 5000): Promise<boolean> {
    if (this.permits > 0) {
      this.permits--;
      return true;
    }
    
    return new Promise((resolve) => {
      const timeoutId = setTimeout(() => {
        const index = this.waitQueue.indexOf(resolve);
        if (index > -1) {
          this.waitQueue.splice(index, 1);
          resolve(false);
        }
      }, timeout);
      
      this.waitQueue.push((acquired) => {
        clearTimeout(timeoutId);
        resolve(acquired);
      });
    });
  }
  
  release(): void {
    if (this.waitQueue.length > 0) {
      const next = this.waitQueue.shift()!;
      next(true);
    } else {
      this.permits++;
    }
  }
}

// Usage
const resourcePool = new ResourcePool({
  database: 10,
  external_api: 5,
  file_processing: 3
});

const result = await resourcePool.executeWithResourceLimit(
  'database',
  async () => {
    return await databaseService.complexQuery();
  }
);
```

---

## 🔄 Recovery Strategies

### Automatic Recovery System
```typescript
interface RecoveryStrategy {
  canRecover(error: SystemError): boolean;
  recover(error: SystemError, context: OperationContext): Promise<RecoveryResult>;
  priority: number;
}

interface RecoveryResult {
  success: boolean;
  strategy: string;
  actions: string[];
  timeToRecover: number;
  preventionMeasures?: string[];
}

class RecoveryManager {
  private strategies: RecoveryStrategy[] = [];
  
  registerStrategy(strategy: RecoveryStrategy): void {
    this.strategies.push(strategy);
    this.strategies.sort((a, b) => b.priority - a.priority);
  }
  
  async attemptRecovery(
    error: SystemError,
    context: OperationContext
  ): Promise<RecoveryResult | null> {
    for (const strategy of this.strategies) {
      if (strategy.canRecover(error)) {
        try {
          const result = await strategy.recover(error, context);
          if (result.success) {
            await this.logRecoverySuccess(error, strategy, result);
            return result;
          }
        } catch (recoveryError) {
          await this.logRecoveryFailure(error, strategy, recoveryError);
        }
      }
    }
    
    return null;
  }
}

// Database Connection Recovery Strategy
class DatabaseRecoveryStrategy implements RecoveryStrategy {
  priority = 10;
  
  canRecover(error: SystemError): boolean {
    return error.type === ErrorType.DATABASE_CONNECTION;
  }
  
  async recover(error: SystemError, context: OperationContext): Promise<RecoveryResult> {
    const actions: string[] = [];
    const startTime = Date.now();
    
    try {
      // Step 1: Test connection
      actions.push('Testing database connectivity');
      await this.testConnection();
      
      // Step 2: Recreate connection pool
      actions.push('Recreating connection pool');
      await this.recreateConnectionPool();
      
      // Step 3: Verify recovery
      actions.push('Verifying database recovery');
      await this.verifyRecovery();
      
      return {
        success: true,
        strategy: 'database_reconnection',
        actions,
        timeToRecover: Date.now() - startTime,
        preventionMeasures: [
          'Implement connection health checks',
          'Add connection pool monitoring',
          'Set up database failover'
        ]
      };
      
    } catch (recoveryError) {
      return {
        success: false,
        strategy: 'database_reconnection',
        actions: [...actions, `Recovery failed: ${recoveryError.message}`],
        timeToRecover: Date.now() - startTime
      };
    }
  }
  
  private async testConnection(): Promise<void> {
    // Implementation for testing database connection
  }
  
  private async recreateConnectionPool(): Promise<void> {
    // Implementation for recreating connection pool
  }
  
  private async verifyRecovery(): Promise<void> {
    // Implementation for verifying recovery
  }
}

// Cache Recovery Strategy
class CacheRecoveryStrategy implements RecoveryStrategy {
  priority = 8;
  
  canRecover(error: SystemError): boolean {
    return error.source.component === 'cache_service';
  }
  
  async recover(error: SystemError, context: OperationContext): Promise<RecoveryResult> {
    const actions: string[] = [];
    const startTime = Date.now();
    
    try {
      // Step 1: Clear corrupted cache
      actions.push('Clearing corrupted cache entries');
      await this.clearCorruptedCache(context);
      
      // Step 2: Rebuild critical cache entries
      actions.push('Rebuilding critical cache entries');
      await this.rebuildCriticalCache(context);
      
      // Step 3: Enable fallback mode
      actions.push('Enabling cache fallback mode');
      await this.enableFallbackMode();
      
      return {
        success: true,
        strategy: 'cache_recovery',
        actions,
        timeToRecover: Date.now() - startTime,
        preventionMeasures: [
          'Implement cache health monitoring',
          'Add cache entry validation',
          'Set up cache replication'
        ]
      };
      
    } catch (recoveryError) {
      return {
        success: false,
        strategy: 'cache_recovery',
        actions: [...actions, `Recovery failed: ${recoveryError.message}`],
        timeToRecover: Date.now() - startTime
      };
    }
  }
}
```

---

## 📊 Error Monitoring & Alerting

### Error Tracking System
```typescript
class ErrorTracker {
  private static readonly errorCounts = new Map<string, number>();
  private static readonly errorHistory: SystemError[] = [];
  private static readonly MAX_HISTORY_SIZE = 1000;
  
  static async trackError(error: SystemError): Promise<void> {
    // Update error counts
    const errorKey = `${error.type}:${error.code}`;
    const currentCount = this.errorCounts.get(errorKey) || 0;
    this.errorCounts.set(errorKey, currentCount + 1);
    
    // Add to history
    this.errorHistory.unshift(error);
    if (this.errorHistory.length > this.MAX_HISTORY_SIZE) {
      this.errorHistory.pop();
    }
    
    // Check for error patterns
    await this.analyzeErrorPatterns(error);
    
    // Send to monitoring systems
    await this.sendToMonitoring(error);
    
    // Trigger alerts if needed
    await this.checkAlertConditions(error);
  }
  
  private static async analyzeErrorPatterns(error: SystemError): Promise<void> {
    const recentErrors = this.errorHistory.slice(0, 10);
    
    // Check for error spikes
    const sameTypeErrors = recentErrors.filter(e => e.type === error.type);
    if (sameTypeErrors.length >= 5) {
      await this.triggerAlert({
        type: 'error_spike',
        message: `Error spike detected: ${error.type}`,
        severity: ErrorSeverity.HIGH,
        context: { errorType: error.type, count: sameTypeErrors.length }
      });
    }
    
    // Check for cascading failures
    const recentTimeWindow = Date.now() - 60000; // 1 minute
    const recentCriticalErrors = recentErrors.filter(
      e => e.severity === ErrorSeverity.CRITICAL &&
           new Date(e.timestamp).getTime() > recentTimeWindow
    );
    
    if (recentCriticalErrors.length >= 3) {
      await this.triggerAlert({
        type: 'cascading_failure',
        message: 'Cascading failure pattern detected',
        severity: ErrorSeverity.CRITICAL,
        context: { criticalErrors: recentCriticalErrors.length }
      });
    }
  }
  
  static getErrorStatistics(): ErrorStatistics {
    const now = Date.now();
    const oneHourAgo = now - 3600000;
    const oneDayAgo = now - 86400000;
    
    const recentErrors = this.errorHistory.filter(
      e => new Date(e.timestamp).getTime() > oneHourAgo
    );
    
    const dailyErrors = this.errorHistory.filter(
      e => new Date(e.timestamp).getTime() > oneDayAgo
    );
    
    return {
      totalErrors: this.errorHistory.length,
      errorsLastHour: recentErrors.length,
      errorsLastDay: dailyErrors.length,
      errorsByType: this.groupErrorsByType(recentErrors),
      errorsBySeverity: this.groupErrorsBySeverity(recentErrors),
      topErrors: this.getTopErrors(),
      errorRate: recentErrors.length / 60, // errors per minute
      trends: this.calculateTrends()
    };
  }
}

// Alert System
class AlertManager {
  private static readonly alertChannels: AlertChannel[] = [];
  
  static registerChannel(channel: AlertChannel): void {
    this.alertChannels.push(channel);
  }
  
  static async sendAlert(alert: Alert): Promise<void> {
    const applicableChannels = this.alertChannels.filter(
      channel => channel.shouldSendAlert(alert)
    );
    
    await Promise.all(
      applicableChannels.map(channel => channel.sendAlert(alert))
    );
  }
}

interface AlertChannel {
  name: string;
  shouldSendAlert(alert: Alert): boolean;
  sendAlert(alert: Alert): Promise<void>;
}

class SlackAlertChannel implements AlertChannel {
  name = 'slack';
  
  shouldSendAlert(alert: Alert): boolean {
    return alert.severity === ErrorSeverity.CRITICAL ||
           alert.severity === ErrorSeverity.HIGH;
  }
  
  async sendAlert(alert: Alert): Promise<void> {
    const message = {
      text: `🚨 ${alert.type.toUpperCase()}: ${alert.message}`,
      attachments: [
        {
          color: this.getSeverityColor(alert.severity),
          fields: [
            { title: 'Severity', value: alert.severity, short: true },
            { title: 'Time', value: new Date().toISOString(), short: true },
            { title: 'Context', value: JSON.stringify(alert.context, null, 2), short: false }
          ]
        }
      ]
    };
    
    // Send to Slack webhook
    await this.sendToSlack(message);
  }
  
  private getSeverityColor(severity: ErrorSeverity): string {
    switch (severity) {
      case ErrorSeverity.CRITICAL: return 'danger';
      case ErrorSeverity.HIGH: return 'warning';
      case ErrorSeverity.MEDIUM: return 'good';
      default: return '#808080';
    }
  }
}
```

---

## 🧪 Error Testing Strategies

### Chaos Engineering for Error Handling
```typescript
class ChaosTestRunner {
  private readonly scenarios: ChaosScenario[] = [];
  
  registerScenario(scenario: ChaosScenario): void {
    this.scenarios.push(scenario);
  }
  
  async runChaosTest(scenarioName: string): Promise<ChaosTestResult> {
    const scenario = this.scenarios.find(s => s.name === scenarioName);
    if (!scenario) {
      throw new Error(`Chaos scenario '${scenarioName}' not found`);
    }
    
    const startTime = Date.now();
    const results: ChaosTestResult = {
      scenario: scenarioName,
      startTime: new Date().toISOString(),
      duration: 0,
      success: false,
      errors: [],
      recoveryTime: 0,
      systemBehavior: []
    };
    
    try {
      // Inject failure
      await scenario.injectFailure();
      results.systemBehavior.push('Failure injected successfully');
      
      // Monitor system behavior
      const monitoringPromise = this.monitorSystemBehavior(results);
      
      // Wait for recovery or timeout
      const recoveryStartTime = Date.now();
      await this.waitForRecovery(scenario, 30000); // 30 second timeout
      results.recoveryTime = Date.now() - recoveryStartTime;
      
      // Stop monitoring
      clearInterval(monitoringPromise);
      
      // Verify system health
      await this.verifySystemHealth();
      results.systemBehavior.push('System recovered successfully');
      
      results.success = true;
      
    } catch (error) {
      results.errors.push(error.message);
      results.systemBehavior.push(`Test failed: ${error.message}`);
      
    } finally {
      // Cleanup
      await scenario.cleanup();
      results.duration = Date.now() - startTime;
    }
    
    return results;
  }
}

// Database Connection Failure Scenario
class DatabaseFailureScenario implements ChaosScenario {
  name = 'database_connection_failure';
  
  async injectFailure(): Promise<void> {
    // Simulate database connection failure
    await this.blockDatabaseConnections();
  }
  
  async cleanup(): Promise<void> {
    // Restore database connections
    await this.restoreDatabaseConnections();
  }
  
  async isRecovered(): Promise<boolean> {
    try {
      await this.testDatabaseConnection();
      return true;
    } catch {
      return false;
    }
  }
}

// Network Partition Scenario
class NetworkPartitionScenario implements ChaosScenario {
  name = 'network_partition';
  
  async injectFailure(): Promise<void> {
    // Simulate network partition
    await this.blockNetworkTraffic();
  }
  
  async cleanup(): Promise<void> {
    // Restore network connectivity
    await this.restoreNetworkTraffic();
  }
  
  async isRecovered(): Promise<boolean> {
    try {
      await this.testNetworkConnectivity();
      return true;
    } catch {
      return false;
    }
  }
}
```

---

## 🔧 Error Handling Utilities

### Error Conversion and Normalization
```typescript
class ErrorConverter {
  static convertToSystemError(
    error: any,
    context?: OperationContext,
    metadata?: any
  ): SystemError {
    // Handle already converted errors
    if (error instanceof SystemError || error.id) {
      return error;
    }
    
    // Handle standard JavaScript errors
    if (error instanceof Error) {
      return this.convertJavaScriptError(error, context, metadata);
    }
    
    // Handle HTTP errors
    if (error.status || error.statusCode) {
      return this.convertHttpError(error, context, metadata);
    }
    
    // Handle database errors
    if (error.code && error.sqlMessage) {
      return this.convertDatabaseError(error, context, metadata);
    }
    
    // Handle validation errors
    if (error.errors && Array.isArray(error.errors)) {
      return this.convertValidationError(error, context, metadata);
    }
    
    // Handle unknown errors
    return this.convertUnknownError(error, context, metadata);
  }
  
  private static convertJavaScriptError(
    error: Error,
    context?: OperationContext,
    metadata?: any
  ): SystemError {
    let errorType: ErrorType;
    let category: ErrorCategory;
    let severity: ErrorSeverity;
    
    // Classify based on error type
    switch (error.constructor.name) {
      case 'TypeError':
      case 'ReferenceError':
        errorType = ErrorType.PROCESSING_FAILED;
        category = ErrorCategory.APPLICATION;
        severity = ErrorSeverity.HIGH;
        break;
        
      case 'TimeoutError':
        errorType = ErrorType.TIMEOUT;
        category = ErrorCategory.PERFORMANCE;
        severity = ErrorSeverity.MEDIUM;
        break;
        
      case 'NetworkError':
        errorType = ErrorType.NETWORK_FAILURE;
        category = ErrorCategory.SYSTEM;
        severity = ErrorSeverity.HIGH;
        break;
        
      default:
        errorType = ErrorType.PROCESSING_FAILED;
        category = ErrorCategory.APPLICATION;
        severity = ErrorSeverity.MEDIUM;
    }
    
    return {
      id: generateId(),
      code: error.constructor.name.toUpperCase(),
      type: errorType,
      category,
      severity,
      message: error.message,
      timestamp: new Date().toISOString(),
      source: {
        component: context?.operation || 'unknown',
        method: this.extractMethodFromStack(error.stack),
        file: this.extractFileFromStack(error.stack),
        line: this.extractLineFromStack(error.stack)
      },
      context: context || {},
      stackTrace: error.stack?.split('\n') || [],
      recoverable: this.isRecoverable(errorType),
      retryable: this.isRetryable(errorType),
      metadata: {
        environment: process.env.NODE_ENV || 'unknown',
        version: process.env.APP_VERSION || 'unknown',
        ...metadata
      }
    };
  }
  
  private static convertHttpError(
    error: any,
    context?: OperationContext,
    metadata?: any
  ): SystemError {
    const statusCode = error.status || error.statusCode;
    let errorType: ErrorType;
    let severity: ErrorSeverity;
    
    if (statusCode >= 400 && statusCode < 500) {
      errorType = statusCode === 401 ? ErrorType.UNAUTHORIZED :
                 statusCode === 403 ? ErrorType.FORBIDDEN :
                 statusCode === 404 ? ErrorType.NOT_FOUND :
                 ErrorType.INVALID_INPUT;
      severity = ErrorSeverity.MEDIUM;
    } else if (statusCode >= 500) {
      errorType = ErrorType.EXTERNAL_API_FAILURE;
      severity = ErrorSeverity.HIGH;
    } else {
      errorType = ErrorType.PROCESSING_FAILED;
      severity = ErrorSeverity.MEDIUM;
    }
    
    return {
      id: generateId(),
      code: `HTTP_${statusCode}`,
      type: errorType,
      category: ErrorCategory.INTEGRATION,
      severity,
      message: error.message || `HTTP ${statusCode} error`,
      timestamp: new Date().toISOString(),
      source: {
        component: 'http_client',
        method: error.config?.method?.toUpperCase(),
        file: error.config?.url
      },
      context: {
        ...context,
        httpStatus: statusCode,
        url: error.config?.url,
        method: error.config?.method
      },
      recoverable: statusCode >= 500,
      retryable: statusCode >= 500 || statusCode === 429,
      retryAfter: error.headers?.['retry-after'] ? 
                  parseInt(error.headers['retry-after']) * 1000 : undefined,
      metadata: {
        environment: process.env.NODE_ENV || 'unknown',
        version: process.env.APP_VERSION || 'unknown',
        ...metadata
      }
    };
  }
}

// Error aggregation for batch operations
class ErrorAggregator {
  private errors: SystemError[] = [];
  
  addError(error: SystemError): void {
    this.errors.push(error);
  }
  
  hasErrors(): boolean {
    return this.errors.length > 0;
  }
  
  getErrors(): SystemError[] {
    return [...this.errors];
  }
  
  getAggregatedError(): SystemError | null {
    if (this.errors.length === 0) {
      return null;
    }
    
    if (this.errors.length === 1) {
      return this.errors[0];
    }
    
    // Create aggregated error
    const highestSeverity = this.getHighestSeverity();
    const errorTypes = [...new Set(this.errors.map(e => e.type))];
    
    return {
      id: generateId(),
      code: 'MULTIPLE_ERRORS',
      type: ErrorType.PROCESSING_FAILED,
      category: ErrorCategory.APPLICATION,
      severity: highestSeverity,
      message: `Multiple errors occurred (${this.errors.length} total)`,
      timestamp: new Date().toISOString(),
      source: {
        component: 'error_aggregator'
      },
      context: {
        errorCount: this.errors.length,
        errorTypes,
        errors: this.errors.map(e => ({
          id: e.id,
          type: e.type,
          message: e.message
        }))
      },
      recoverable: this.errors.some(e => e.recoverable),
      retryable: this.errors.some(e => e.retryable),
      metadata: {
        environment: process.env.NODE_ENV || 'unknown',
        version: process.env.APP_VERSION || 'unknown',
        aggregated: true
      }
    };
  }
  
  private getHighestSeverity(): ErrorSeverity {
    const severityOrder = {
      [ErrorSeverity.CRITICAL]: 5,
      [ErrorSeverity.HIGH]: 4,
      [ErrorSeverity.MEDIUM]: 3,
      [ErrorSeverity.LOW]: 2,
      [ErrorSeverity.INFO]: 1
    };
    
    return this.errors.reduce((highest, error) => {
      return severityOrder[error.severity] > severityOrder[highest] ?
             error.severity : highest;
    }, ErrorSeverity.INFO);
  }
  
  clear(): void {
    this.errors = [];
  }
}
```

---

## Integration with .god Ecosystem

### Sub-Agent Error Handling
- **Bug Hunter**: Advanced error pattern detection and analysis
- **Security Auditor**: Security-related error validation and response
- **Performance Analyzer**: Performance error detection and optimization
- **Test Executor**: Error scenario testing and validation
- **Context Optimizer**: Error context optimization and compression

### Workflow Integration
- **TSDDR 2.0**: Error-driven test design and validation
- **Kiro Workflow**: Error handling checkpoints in task execution
- **Agent Coordination**: Cross-agent error propagation and recovery

---

**Usage**: All system components and inter-agent communication  
**Enforcement**: Automated error handling validation in CI/CD  
**Monitoring**: Real-time error tracking and alerting  
**Evolution**: Continuous improvement based on error patterns and recovery success rates