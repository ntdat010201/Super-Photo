# Standard Patterns Library

## 🎯 Pattern Library Overview

**Purpose**: Comprehensive collection of proven development patterns and best practices  
**Scope**: Architecture, design, implementation, and testing patterns  
**Integration**: Deep integration with Sub-Agent system and workflow automation  
**Maintenance**: Living library that evolves with project experience and industry standards

## 🏗️ Architecture Patterns

### 1. Layered Architecture Pattern

**Description**: Organizes code into horizontal layers with clear separation of concerns  
**Use Cases**: Enterprise applications, web applications, API services  
**Sub-Agent Integration**: Context Optimizer for layer optimization, Security Auditor for layer security

```typescript
// Layered Architecture Structure
interface LayeredArchitecture {
  presentationLayer: PresentationLayer;
  businessLayer: BusinessLayer;
  dataAccessLayer: DataAccessLayer;
  infrastructureLayer: InfrastructureLayer;
}

class LayeredArchitectureImplementation {
  // Presentation Layer - UI/Controllers
  private presentationLayer: {
    controllers: Controller[];
    views: View[];
    middlewares: Middleware[];
  };
  
  // Business Layer - Domain Logic
  private businessLayer: {
    services: Service[];
    domainModels: DomainModel[];
    businessRules: BusinessRule[];
  };
  
  // Data Access Layer - Data Operations
  private dataAccessLayer: {
    repositories: Repository[];
    dataMappers: DataMapper[];
    queryBuilders: QueryBuilder[];
  };
  
  // Infrastructure Layer - External Concerns
  private infrastructureLayer: {
    database: DatabaseConnection;
    externalServices: ExternalService[];
    logging: Logger;
    caching: Cache;
  };
}
```

**Best Practices**:
- Each layer should only depend on layers below it
- Use dependency injection for loose coupling
- Implement clear interfaces between layers
- Apply Context Optimizer for performance optimization

### 2. Microservices Architecture Pattern

**Description**: Decomposes application into small, independent services  
**Use Cases**: Large-scale applications, distributed systems, cloud-native applications  
**Sub-Agent Integration**: Performance Analyzer for service monitoring, Security Auditor for service security

```typescript
// Microservices Architecture Structure
interface MicroserviceArchitecture {
  services: Microservice[];
  apiGateway: APIGateway;
  serviceDiscovery: ServiceDiscovery;
  messageQueue: MessageQueue;
  monitoring: MonitoringSystem;
}

class MicroserviceImplementation {
  private services: Map<string, Microservice> = new Map();
  private communicationPatterns: CommunicationPattern[];
  
  async deployService(service: Microservice): Promise<void> {
    // Validate service with Security Auditor
    await this.validateServiceSecurity(service);
    
    // Optimize service with Performance Analyzer
    await this.optimizeServicePerformance(service);
    
    // Deploy with monitoring
    await this.deployWithMonitoring(service);
  }
  
  private async validateServiceSecurity(service: Microservice): Promise<void> {
    // Security validation logic
  }
  
  private async optimizeServicePerformance(service: Microservice): Promise<void> {
    // Performance optimization logic
  }
}
```

**Best Practices**:
- Single responsibility per service
- Database per service
- Asynchronous communication
- Circuit breaker pattern for resilience

### 3. Event-Driven Architecture Pattern

**Description**: Uses events to trigger and communicate between decoupled services  
**Use Cases**: Real-time systems, reactive applications, complex business workflows  
**Sub-Agent Integration**: Bug Hunter for event flow debugging, Context Optimizer for event optimization

```typescript
// Event-Driven Architecture Structure
interface EventDrivenArchitecture {
  eventBus: EventBus;
  eventStore: EventStore;
  eventHandlers: EventHandler[];
  eventProducers: EventProducer[];
  eventConsumers: EventConsumer[];
}

class EventDrivenImplementation {
  private eventBus: EventBus;
  private eventHandlers: Map<string, EventHandler[]> = new Map();
  
  async publishEvent(event: DomainEvent): Promise<void> {
    // Validate event with Bug Hunter
    await this.validateEvent(event);
    
    // Optimize event routing with Context Optimizer
    const optimizedRouting = await this.optimizeEventRouting(event);
    
    // Publish event
    await this.eventBus.publish(event, optimizedRouting);
  }
  
  async subscribeToEvent(
    eventType: string,
    handler: EventHandler
  ): Promise<void> {
    const handlers = this.eventHandlers.get(eventType) || [];
    handlers.push(handler);
    this.eventHandlers.set(eventType, handlers);
  }
}
```

## 🎨 Design Patterns

### 1. Repository Pattern

**Description**: Encapsulates data access logic and provides a uniform interface  
**Use Cases**: Data access abstraction, testing, multiple data sources  
**Sub-Agent Integration**: Performance Analyzer for query optimization, Security Auditor for data security

```typescript
// Repository Pattern Implementation
interface Repository<T> {
  findById(id: string): Promise<T | null>;
  findAll(criteria?: SearchCriteria): Promise<T[]>;
  save(entity: T): Promise<T>;
  update(id: string, entity: Partial<T>): Promise<T>;
  delete(id: string): Promise<void>;
}

class UserRepository implements Repository<User> {
  constructor(
    private dataSource: DataSource,
    private performanceAnalyzer?: PerformanceAnalyzerAgent,
    private securityAuditor?: SecurityAuditorAgent
  ) {}
  
  async findById(id: string): Promise<User | null> {
    // Security validation
    if (this.securityAuditor) {
      await this.securityAuditor.validateDataAccess({
        operation: 'read',
        resource: 'user',
        identifier: id
      });
    }
    
    // Performance monitoring
    const startTime = Date.now();
    
    try {
      const user = await this.dataSource.findOne('users', { id });
      
      // Performance analysis
      if (this.performanceAnalyzer) {
        await this.performanceAnalyzer.analyzeQueryPerformance({
          operation: 'findById',
          table: 'users',
          executionTime: Date.now() - startTime,
          resultSize: user ? 1 : 0
        });
      }
      
      return user;
    } catch (error) {
      // Error analysis with Bug Hunter would be integrated here
      throw error;
    }
  }
  
  async findAll(criteria?: SearchCriteria): Promise<User[]> {
    // Implementation with Sub-Agent integration
    return [];
  }
  
  async save(entity: User): Promise<User> {
    // Implementation with validation and monitoring
    return entity;
  }
  
  async update(id: string, entity: Partial<User>): Promise<User> {
    // Implementation with security and performance monitoring
    return entity as User;
  }
  
  async delete(id: string): Promise<void> {
    // Implementation with audit logging
  }
}
```

### 2. Factory Pattern

**Description**: Creates objects without specifying their concrete classes  
**Use Cases**: Object creation abstraction, dependency injection, plugin systems  
**Sub-Agent Integration**: Context Optimizer for factory optimization, Bug Hunter for creation validation

```typescript
// Factory Pattern Implementation
interface ServiceFactory {
  createService(type: string, config: ServiceConfig): Service;
}

class EnhancedServiceFactory implements ServiceFactory {
  private serviceRegistry: Map<string, ServiceConstructor> = new Map();
  private contextOptimizer?: ContextOptimizerAgent;
  private bugHunter?: BugHunterAgent;
  
  constructor(subAgents?: Map<string, SubAgent>) {
    this.contextOptimizer = subAgents?.get('context-optimizer') as ContextOptimizerAgent;
    this.bugHunter = subAgents?.get('bug-hunter') as BugHunterAgent;
  }
  
  registerService(type: string, constructor: ServiceConstructor): void {
    this.serviceRegistry.set(type, constructor);
  }
  
  async createService(type: string, config: ServiceConfig): Promise<Service> {
    // Validate service type with Bug Hunter
    if (this.bugHunter) {
      await this.bugHunter.validateServiceCreation({
        serviceType: type,
        config,
        availableTypes: Array.from(this.serviceRegistry.keys())
      });
    }
    
    const ServiceConstructor = this.serviceRegistry.get(type);
    if (!ServiceConstructor) {
      throw new Error(`Service type '${type}' not registered`);
    }
    
    // Optimize service configuration with Context Optimizer
    let optimizedConfig = config;
    if (this.contextOptimizer) {
      optimizedConfig = await this.contextOptimizer.optimizeServiceConfig(
        type,
        config,
        {
          optimizeForPerformance: true,
          optimizeForMemory: true,
          optimizeForScalability: true
        }
      );
    }
    
    // Create service instance
    const service = new ServiceConstructor(optimizedConfig);
    
    // Post-creation validation
    if (this.bugHunter) {
      await this.bugHunter.validateServiceInstance(service, {
        expectedInterface: type,
        configValidation: true,
        dependencyValidation: true
      });
    }
    
    return service;
  }
}
```

### 3. Observer Pattern

**Description**: Defines a one-to-many dependency between objects  
**Use Cases**: Event handling, model-view architectures, reactive programming  
**Sub-Agent Integration**: Performance Analyzer for observer performance, Bug Hunter for notification debugging

```typescript
// Observer Pattern Implementation
interface Observer<T> {
  update(data: T): Promise<void>;
}

interface Subject<T> {
  subscribe(observer: Observer<T>): void;
  unsubscribe(observer: Observer<T>): void;
  notify(data: T): Promise<void>;
}

class EnhancedSubject<T> implements Subject<T> {
  private observers: Set<Observer<T>> = new Set();
  private performanceAnalyzer?: PerformanceAnalyzerAgent;
  private bugHunter?: BugHunterAgent;
  
  constructor(subAgents?: Map<string, SubAgent>) {
    this.performanceAnalyzer = subAgents?.get('performance-analyzer') as PerformanceAnalyzerAgent;
    this.bugHunter = subAgents?.get('bug-hunter') as BugHunterAgent;
  }
  
  subscribe(observer: Observer<T>): void {
    this.observers.add(observer);
  }
  
  unsubscribe(observer: Observer<T>): void {
    this.observers.delete(observer);
  }
  
  async notify(data: T): Promise<void> {
    const startTime = Date.now();
    const notificationPromises: Promise<void>[] = [];
    
    // Notify all observers
    for (const observer of this.observers) {
      const notificationPromise = this.notifyObserver(observer, data);
      notificationPromises.push(notificationPromise);
    }
    
    try {
      await Promise.all(notificationPromises);
      
      // Performance analysis
      if (this.performanceAnalyzer) {
        await this.performanceAnalyzer.analyzeNotificationPerformance({
          observerCount: this.observers.size,
          executionTime: Date.now() - startTime,
          dataSize: this.estimateDataSize(data),
          successfulNotifications: notificationPromises.length
        });
      }
    } catch (error) {
      // Error analysis with Bug Hunter
      if (this.bugHunter) {
        await this.bugHunter.analyzeNotificationFailure({
          error,
          observerCount: this.observers.size,
          data,
          failedNotifications: await this.identifyFailedNotifications(notificationPromises)
        });
      }
      throw error;
    }
  }
  
  private async notifyObserver(observer: Observer<T>, data: T): Promise<void> {
    try {
      await observer.update(data);
    } catch (error) {
      // Log observer-specific errors for Bug Hunter analysis
      if (this.bugHunter) {
        await this.bugHunter.logObserverError({
          observer,
          error,
          data
        });
      }
      throw error;
    }
  }
  
  private estimateDataSize(data: T): number {
    return JSON.stringify(data).length;
  }
  
  private async identifyFailedNotifications(promises: Promise<void>[]): Promise<number> {
    let failedCount = 0;
    for (const promise of promises) {
      try {
        await promise;
      } catch {
        failedCount++;
      }
    }
    return failedCount;
  }
}
```

## 🧪 Testing Patterns

### 1. Test Data Builder Pattern

**Description**: Provides a fluent interface for creating test data  
**Use Cases**: Complex object creation, test data management, readable tests  
**Sub-Agent Integration**: Test Executor for test data optimization, Context Optimizer for builder efficiency

```typescript
// Test Data Builder Pattern
class UserTestDataBuilder {
  private user: Partial<User> = {};
  private contextOptimizer?: ContextOptimizerAgent;
  
  constructor(subAgents?: Map<string, SubAgent>) {
    this.contextOptimizer = subAgents?.get('context-optimizer') as ContextOptimizerAgent;
  }
  
  withId(id: string): UserTestDataBuilder {
    this.user.id = id;
    return this;
  }
  
  withName(name: string): UserTestDataBuilder {
    this.user.name = name;
    return this;
  }
  
  withEmail(email: string): UserTestDataBuilder {
    this.user.email = email;
    return this;
  }
  
  withRole(role: UserRole): UserTestDataBuilder {
    this.user.role = role;
    return this;
  }
  
  withRandomData(): UserTestDataBuilder {
    this.user.id = `user-${Math.random().toString(36).substr(2, 9)}`;
    this.user.name = `Test User ${Math.floor(Math.random() * 1000)}`;
    this.user.email = `test${Math.floor(Math.random() * 1000)}@example.com`;
    this.user.role = 'user';
    return this;
  }
  
  async build(): Promise<User> {
    // Optimize test data with Context Optimizer
    if (this.contextOptimizer) {
      const optimizedData = await this.contextOptimizer.optimizeTestData(
        this.user,
        {
          ensureUniqueness: true,
          validateConstraints: true,
          optimizeForTestSpeed: true
        }
      );
      this.user = { ...this.user, ...optimizedData };
    }
    
    // Apply defaults for missing fields
    const defaultUser: User = {
      id: this.user.id || `user-${Date.now()}`,
      name: this.user.name || 'Test User',
      email: this.user.email || 'test@example.com',
      role: this.user.role || 'user',
      createdAt: new Date(),
      updatedAt: new Date()
    };
    
    return { ...defaultUser, ...this.user } as User;
  }
  
  async buildMany(count: number): Promise<User[]> {
    const users: User[] = [];
    for (let i = 0; i < count; i++) {
      const builder = new UserTestDataBuilder();
      builder.user = { ...this.user }; // Copy current state
      
      // Make each user unique
      if (!this.user.id) {
        builder.withId(`user-${Date.now()}-${i}`);
      }
      if (!this.user.email) {
        builder.withEmail(`test${Date.now()}${i}@example.com`);
      }
      
      users.push(await builder.build());
    }
    return users;
  }
}

// Usage Example
class UserServiceTest {
  private userBuilder: UserTestDataBuilder;
  
  constructor() {
    this.userBuilder = new UserTestDataBuilder();
  }
  
  async testCreateUser(): Promise<void> {
    // Create test user with builder
    const testUser = await this.userBuilder
      .withName('John Doe')
      .withEmail('john@example.com')
      .withRole('admin')
      .build();
    
    // Test logic here
    const createdUser = await userService.createUser(testUser);
    expect(createdUser.id).toBeDefined();
    expect(createdUser.name).toBe('John Doe');
  }
  
  async testBulkUserCreation(): Promise<void> {
    // Create multiple test users
    const testUsers = await this.userBuilder
      .withRole('user')
      .buildMany(10);
    
    // Test bulk creation
    const createdUsers = await userService.createUsers(testUsers);
    expect(createdUsers).toHaveLength(10);
  }
}
```

### 2. Page Object Pattern

**Description**: Encapsulates web page elements and actions in objects  
**Use Cases**: UI testing, test maintenance, element abstraction  
**Sub-Agent Integration**: Test Executor for page interaction optimization, Bug Hunter for element debugging

```typescript
// Page Object Pattern Implementation
abstract class BasePage {
  protected driver: WebDriver;
  protected bugHunter?: BugHunterAgent;
  protected testExecutor?: TestExecutorAgent;
  
  constructor(
    driver: WebDriver,
    subAgents?: Map<string, SubAgent>
  ) {
    this.driver = driver;
    this.bugHunter = subAgents?.get('bug-hunter') as BugHunterAgent;
    this.testExecutor = subAgents?.get('test-executor') as TestExecutorAgent;
  }
  
  protected async findElement(
    locator: By,
    timeout: number = 10000
  ): Promise<WebElement> {
    try {
      const element = await this.driver.wait(
        until.elementLocated(locator),
        timeout
      );
      
      // Validate element with Test Executor
      if (this.testExecutor) {
        await this.testExecutor.validateElement(element, {
          checkVisibility: true,
          checkInteractability: true,
          logElementState: true
        });
      }
      
      return element;
    } catch (error) {
      // Debug element location issues with Bug Hunter
      if (this.bugHunter) {
        await this.bugHunter.debugElementLocation({
          locator,
          timeout,
          error,
          pageSource: await this.driver.getPageSource(),
          currentUrl: await this.driver.getCurrentUrl()
        });
      }
      throw error;
    }
  }
  
  protected async clickElement(locator: By): Promise<void> {
    const element = await this.findElement(locator);
    
    try {
      await element.click();
      
      // Log successful interaction
      if (this.testExecutor) {
        await this.testExecutor.logInteraction({
          action: 'click',
          locator,
          success: true
        });
      }
    } catch (error) {
      // Debug click issues
      if (this.bugHunter) {
        await this.bugHunter.debugClickIssue({
          element,
          locator,
          error,
          elementState: await this.getElementState(element)
        });
      }
      throw error;
    }
  }
  
  protected async enterText(locator: By, text: string): Promise<void> {
    const element = await this.findElement(locator);
    
    try {
      await element.clear();
      await element.sendKeys(text);
      
      // Validate text entry
      const enteredText = await element.getAttribute('value');
      if (enteredText !== text && this.bugHunter) {
        await this.bugHunter.reportTextEntryMismatch({
          expected: text,
          actual: enteredText,
          locator,
          element
        });
      }
    } catch (error) {
      if (this.bugHunter) {
        await this.bugHunter.debugTextEntry({
          locator,
          text,
          error,
          elementState: await this.getElementState(element)
        });
      }
      throw error;
    }
  }
  
  private async getElementState(element: WebElement): Promise<ElementState> {
    return {
      isDisplayed: await element.isDisplayed(),
      isEnabled: await element.isEnabled(),
      tagName: await element.getTagName(),
      text: await element.getText(),
      value: await element.getAttribute('value')
    };
  }
}

class LoginPage extends BasePage {
  private readonly usernameInput = By.id('username');
  private readonly passwordInput = By.id('password');
  private readonly loginButton = By.id('login-button');
  private readonly errorMessage = By.className('error-message');
  
  async login(username: string, password: string): Promise<void> {
    await this.enterText(this.usernameInput, username);
    await this.enterText(this.passwordInput, password);
    await this.clickElement(this.loginButton);
  }
  
  async getErrorMessage(): Promise<string> {
    try {
      const errorElement = await this.findElement(this.errorMessage, 5000);
      return await errorElement.getText();
    } catch (error) {
      return ''; // No error message found
    }
  }
  
  async isLoginSuccessful(): Promise<boolean> {
    try {
      // Check if we're redirected to dashboard or similar
      await this.driver.wait(
        until.urlContains('/dashboard'),
        10000
      );
      return true;
    } catch (error) {
      return false;
    }
  }
}
```

## 🔄 Integration Patterns

### 1. API Gateway Pattern

**Description**: Single entry point for all client requests to microservices  
**Use Cases**: Microservices architecture, API management, cross-cutting concerns  
**Sub-Agent Integration**: Security Auditor for gateway security, Performance Analyzer for routing optimization

```typescript
// API Gateway Pattern Implementation
interface APIGateway {
  route(request: APIRequest): Promise<APIResponse>;
  authenticate(request: APIRequest): Promise<AuthenticationResult>;
  authorize(request: APIRequest, user: User): Promise<AuthorizationResult>;
  rateLimit(request: APIRequest): Promise<RateLimitResult>;
}

class EnhancedAPIGateway implements APIGateway {
  private serviceRegistry: Map<string, ServiceEndpoint> = new Map();
  private securityAuditor?: SecurityAuditorAgent;
  private performanceAnalyzer?: PerformanceAnalyzerAgent;
  private contextOptimizer?: ContextOptimizerAgent;
  
  constructor(subAgents?: Map<string, SubAgent>) {
    this.securityAuditor = subAgents?.get('security-auditor') as SecurityAuditorAgent;
    this.performanceAnalyzer = subAgents?.get('performance-analyzer') as PerformanceAnalyzerAgent;
    this.contextOptimizer = subAgents?.get('context-optimizer') as ContextOptimizerAgent;
  }
  
  async route(request: APIRequest): Promise<APIResponse> {
    const startTime = Date.now();
    
    try {
      // Security validation
      const authResult = await this.authenticate(request);
      if (!authResult.success) {
        return {
          status: 401,
          body: { error: 'Authentication failed' },
          headers: {}
        };
      }
      
      // Authorization check
      const authzResult = await this.authorize(request, authResult.user!);
      if (!authzResult.success) {
        return {
          status: 403,
          body: { error: 'Authorization failed' },
          headers: {}
        };
      }
      
      // Rate limiting
      const rateLimitResult = await this.rateLimit(request);
      if (!rateLimitResult.allowed) {
        return {
          status: 429,
          body: { error: 'Rate limit exceeded' },
          headers: {
            'Retry-After': rateLimitResult.retryAfter?.toString() || '60'
          }
        };
      }
      
      // Route optimization with Context Optimizer
      const optimizedRoute = this.contextOptimizer ? 
        await this.contextOptimizer.optimizeRoute(request, {
          considerLoadBalancing: true,
          considerLatency: true,
          considerServiceHealth: true
        }) : await this.getDefaultRoute(request);
      
      // Forward request to service
      const response = await this.forwardRequest(request, optimizedRoute);
      
      // Performance monitoring
      if (this.performanceAnalyzer) {
        await this.performanceAnalyzer.analyzeGatewayPerformance({
          request,
          response,
          executionTime: Date.now() - startTime,
          route: optimizedRoute
        });
      }
      
      return response;
      
    } catch (error) {
      // Security incident analysis
      if (this.securityAuditor) {
        await this.securityAuditor.analyzeGatewayError({
          request,
          error,
          executionTime: Date.now() - startTime
        });
      }
      
      return {
        status: 500,
        body: { error: 'Internal server error' },
        headers: {}
      };
    }
  }
  
  async authenticate(request: APIRequest): Promise<AuthenticationResult> {
    // Security validation with Security Auditor
    if (this.securityAuditor) {
      const securityValidation = await this.securityAuditor.validateAuthenticationRequest(
        request,
        {
          checkTokenFormat: true,
          validateTokenSignature: true,
          checkTokenExpiration: true,
          detectSuspiciousPatterns: true
        }
      );
      
      if (!securityValidation.isValid) {
        return {
          success: false,
          error: securityValidation.error,
          securityIncident: securityValidation.securityIncident
        };
      }
    }
    
    // Perform authentication logic
    const token = this.extractToken(request);
    if (!token) {
      return { success: false, error: 'No authentication token provided' };
    }
    
    try {
      const user = await this.validateToken(token);
      return { success: true, user };
    } catch (error) {
      return { success: false, error: 'Invalid authentication token' };
    }
  }
  
  async authorize(request: APIRequest, user: User): Promise<AuthorizationResult> {
    // Authorization logic with Security Auditor validation
    if (this.securityAuditor) {
      const authzValidation = await this.securityAuditor.validateAuthorization(
        request,
        user,
        {
          checkResourcePermissions: true,
          checkRoleBasedAccess: true,
          checkAttributeBasedAccess: true,
          logAccessAttempts: true
        }
      );
      
      return {
        success: authzValidation.isAuthorized,
        permissions: authzValidation.permissions,
        restrictions: authzValidation.restrictions
      };
    }
    
    // Default authorization logic
    return { success: true, permissions: [], restrictions: [] };
  }
  
  async rateLimit(request: APIRequest): Promise<RateLimitResult> {
    // Rate limiting with Performance Analyzer insights
    if (this.performanceAnalyzer) {
      const rateLimitStrategy = await this.performanceAnalyzer.optimizeRateLimit(
        request,
        {
          considerUserTier: true,
          considerSystemLoad: true,
          considerHistoricalUsage: true
        }
      );
      
      return await this.applyRateLimit(request, rateLimitStrategy);
    }
    
    // Default rate limiting
    return { allowed: true };
  }
  
  private async getDefaultRoute(request: APIRequest): Promise<ServiceRoute> {
    // Default routing logic
    const serviceName = this.extractServiceName(request.path);
    const endpoint = this.serviceRegistry.get(serviceName);
    
    if (!endpoint) {
      throw new Error(`Service not found: ${serviceName}`);
    }
    
    return {
      serviceName,
      endpoint,
      loadBalancingStrategy: 'round-robin'
    };
  }
  
  private async forwardRequest(
    request: APIRequest,
    route: ServiceRoute
  ): Promise<APIResponse> {
    // Forward request to the target service
    // Implementation would use HTTP client to forward the request
    return {
      status: 200,
      body: { message: 'Request forwarded successfully' },
      headers: {}
    };
  }
  
  private extractToken(request: APIRequest): string | null {
    const authHeader = request.headers['Authorization'];
    if (authHeader && authHeader.startsWith('Bearer ')) {
      return authHeader.substring(7);
    }
    return null;
  }
  
  private async validateToken(token: string): Promise<User> {
    // Token validation logic
    // This would typically involve JWT verification or database lookup
    throw new Error('Token validation not implemented');
  }
  
  private extractServiceName(path: string): string {
    // Extract service name from request path
    const pathSegments = path.split('/').filter(segment => segment.length > 0);
    return pathSegments[0] || 'default';
  }
  
  private async applyRateLimit(
    request: APIRequest,
    strategy: RateLimitStrategy
  ): Promise<RateLimitResult> {
    // Apply rate limiting based on strategy
    return { allowed: true };
  }
}
```

## 📋 Pattern Usage Guidelines

### Pattern Selection Criteria

1. **Complexity Assessment**
   - Simple: Direct implementation
   - Medium: Single pattern application
   - Complex: Multiple pattern combination

2. **Sub-Agent Integration Requirements**
   - Performance critical: Include Performance Analyzer
   - Security sensitive: Include Security Auditor
   - Error prone: Include Bug Hunter
   - Resource intensive: Include Context Optimizer

3. **Maintenance Considerations**
   - Long-term projects: Favor maintainable patterns
   - Rapid prototypes: Favor simple patterns
   - Team size: Consider pattern complexity vs team expertise

### Pattern Implementation Checklist

```markdown
☐ Pattern appropriateness validated
☐ Sub-Agent integration points identified
☐ Performance implications assessed
☐ Security considerations addressed
☐ Testing strategy defined
☐ Documentation created
☐ Code review completed
☐ Monitoring and alerting configured
```

### Anti-Patterns to Avoid

1. **Over-Engineering**: Using complex patterns for simple problems
2. **Pattern Mixing**: Combining incompatible patterns
3. **Incomplete Implementation**: Partial pattern implementation
4. **Ignoring Context**: Not considering project-specific requirements
5. **Sub-Agent Overuse**: Integrating Sub-Agents where not beneficial

---

**Standard Patterns Library Benefits**:
- **Proven Solutions**: Battle-tested patterns and implementations
- **Sub-Agent Integration**: Deep integration with intelligent Sub-Agent system
- **Quality Assurance**: Built-in quality validation and optimization
- **Performance Optimization**: Performance-aware pattern implementations
- **Security by Design**: Security considerations built into every pattern
- **Maintainability**: Focus on long-term maintainability and evolution

**Usage**: Reference this library when implementing new features or refactoring existing code. Each pattern includes Sub-Agent integration points for enhanced quality, performance, and security.