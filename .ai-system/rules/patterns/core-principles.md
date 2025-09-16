# Core Development Principles & Patterns

> **🎯 Fundamental Design Principles for Consistent Development**  
> Essential patterns and principles that guide all development decisions

## Design Philosophy

**Mission**: Establish consistent, maintainable, and scalable development patterns  
**Approach**: Principle-driven development with proven patterns  
**Goal**: Reduce cognitive load and increase development velocity through standardization

---

## 🏗️ SOLID Principles Implementation

### Single Responsibility Principle (SRP)

**Definition**: Each class/function should have only one reason to change

**Implementation Patterns**:
```typescript
// ❌ BAD: Multiple responsibilities
class UserManager {
    validateUser(user: User): boolean { /* validation logic */ }
    saveUser(user: User): void { /* database logic */ }
    sendWelcomeEmail(user: User): void { /* email logic */ }
    generateReport(users: User[]): string { /* reporting logic */ }
}

// ✅ GOOD: Single responsibility
class UserValidator {
    validate(user: User): ValidationResult { /* only validation */ }
}

class UserRepository {
    save(user: User): Promise<void> { /* only data persistence */ }
}

class EmailService {
    sendWelcome(user: User): Promise<void> { /* only email sending */ }
}

class UserReportGenerator {
    generate(users: User[]): Report { /* only report generation */ }
}
```

**Validation Checklist**:
```markdown
☐ Class has single, well-defined purpose
☐ Methods are cohesive and related
☐ Changes to one feature don't affect others
☐ Class name clearly indicates its responsibility
☐ No mixed concerns (UI + business logic + data access)
```

### Open/Closed Principle (OCP)

**Definition**: Open for extension, closed for modification

**Implementation Patterns**:
```typescript
// ✅ GOOD: Extensible without modification
abstract class PaymentProcessor {
    abstract process(amount: number): Promise<PaymentResult>;
    
    protected validateAmount(amount: number): boolean {
        return amount > 0;
    }
}

class CreditCardProcessor extends PaymentProcessor {
    async process(amount: number): Promise<PaymentResult> {
        if (!this.validateAmount(amount)) throw new Error('Invalid amount');
        // Credit card specific logic
        return { success: true, transactionId: 'cc_123' };
    }
}

class PayPalProcessor extends PaymentProcessor {
    async process(amount: number): Promise<PaymentResult> {
        if (!this.validateAmount(amount)) throw new Error('Invalid amount');
        // PayPal specific logic
        return { success: true, transactionId: 'pp_456' };
    }
}

// Usage: No modification needed to add new payment methods
class PaymentService {
    constructor(private processor: PaymentProcessor) {}
    
    async processPayment(amount: number): Promise<PaymentResult> {
        return this.processor.process(amount);
    }
}
```

### Liskov Substitution Principle (LSP)

**Definition**: Derived classes must be substitutable for their base classes

**Implementation Patterns**:
```typescript
// ✅ GOOD: Proper substitution
abstract class Bird {
    abstract move(): void;
    eat(): void { /* common eating behavior */ }
}

class FlyingBird extends Bird {
    move(): void {
        this.fly();
    }
    
    private fly(): void { /* flying implementation */ }
}

class WalkingBird extends Bird {
    move(): void {
        this.walk();
    }
    
    private walk(): void { /* walking implementation */ }
}

// Both can be used interchangeably
function moveBird(bird: Bird): void {
    bird.move(); // Works for any Bird subclass
}
```

### Interface Segregation Principle (ISP)

**Definition**: Clients shouldn't depend on interfaces they don't use

**Implementation Patterns**:
```typescript
// ❌ BAD: Fat interface
interface Worker {
    work(): void;
    eat(): void;
    sleep(): void;
    code(): void;
    design(): void;
}

// ✅ GOOD: Segregated interfaces
interface Workable {
    work(): void;
}

interface Eatable {
    eat(): void;
}

interface Sleepable {
    sleep(): void;
}

interface Codeable {
    code(): void;
}

interface Designable {
    design(): void;
}

// Implement only what's needed
class Developer implements Workable, Eatable, Sleepable, Codeable {
    work(): void { this.code(); }
    eat(): void { /* eating logic */ }
    sleep(): void { /* sleeping logic */ }
    code(): void { /* coding logic */ }
}

class Designer implements Workable, Eatable, Sleepable, Designable {
    work(): void { this.design(); }
    eat(): void { /* eating logic */ }
    sleep(): void { /* sleeping logic */ }
    design(): void { /* design logic */ }
}
```

### Dependency Inversion Principle (DIP)

**Definition**: Depend on abstractions, not concretions

**Implementation Patterns**:
```typescript
// ✅ GOOD: Dependency inversion
interface Logger {
    log(message: string): void;
}

interface Database {
    save(data: any): Promise<void>;
    find(id: string): Promise<any>;
}

class UserService {
    constructor(
        private logger: Logger,
        private database: Database
    ) {}
    
    async createUser(userData: any): Promise<void> {
        this.logger.log('Creating user...');
        await this.database.save(userData);
        this.logger.log('User created successfully');
    }
}

// Concrete implementations
class ConsoleLogger implements Logger {
    log(message: string): void {
        console.log(message);
    }
}

class PostgreSQLDatabase implements Database {
    async save(data: any): Promise<void> { /* PostgreSQL logic */ }
    async find(id: string): Promise<any> { /* PostgreSQL logic */ }
}

// Easy to test and swap implementations
const userService = new UserService(
    new ConsoleLogger(),
    new PostgreSQLDatabase()
);
```

---

## 🎨 Design Patterns Implementation

### Creational Patterns

#### Factory Pattern
```typescript
interface Product {
    operation(): string;
}

class ConcreteProductA implements Product {
    operation(): string {
        return 'Result of ConcreteProductA';
    }
}

class ConcreteProductB implements Product {
    operation(): string {
        return 'Result of ConcreteProductB';
    }
}

abstract class Creator {
    abstract factoryMethod(): Product;
    
    someOperation(): string {
        const product = this.factoryMethod();
        return `Creator: ${product.operation()}`;
    }
}

class ConcreteCreatorA extends Creator {
    factoryMethod(): Product {
        return new ConcreteProductA();
    }
}

class ConcreteCreatorB extends Creator {
    factoryMethod(): Product {
        return new ConcreteProductB();
    }
}
```

#### Singleton Pattern (with proper implementation)
```typescript
class DatabaseConnection {
    private static instance: DatabaseConnection;
    private constructor() {
        // Private constructor prevents direct instantiation
    }
    
    static getInstance(): DatabaseConnection {
        if (!DatabaseConnection.instance) {
            DatabaseConnection.instance = new DatabaseConnection();
        }
        return DatabaseConnection.instance;
    }
    
    connect(): void {
        console.log('Database connected');
    }
}

// Usage
const db1 = DatabaseConnection.getInstance();
const db2 = DatabaseConnection.getInstance();
console.log(db1 === db2); // true
```

#### Builder Pattern
```typescript
class Product {
    private parts: string[] = [];
    
    add(part: string): void {
        this.parts.push(part);
    }
    
    listParts(): void {
        console.log(`Product parts: ${this.parts.join(', ')}`);
    }
}

interface Builder {
    producePartA(): void;
    producePartB(): void;
    producePartC(): void;
}

class ConcreteBuilder implements Builder {
    private product: Product;
    
    constructor() {
        this.reset();
    }
    
    reset(): void {
        this.product = new Product();
    }
    
    producePartA(): void {
        this.product.add('PartA1');
    }
    
    producePartB(): void {
        this.product.add('PartB1');
    }
    
    producePartC(): void {
        this.product.add('PartC1');
    }
    
    getProduct(): Product {
        const result = this.product;
        this.reset();
        return result;
    }
}

class Director {
    private builder: Builder;
    
    setBuilder(builder: Builder): void {
        this.builder = builder;
    }
    
    buildMinimalViableProduct(): void {
        this.builder.producePartA();
    }
    
    buildFullFeaturedProduct(): void {
        this.builder.producePartA();
        this.builder.producePartB();
        this.builder.producePartC();
    }
}
```

### Structural Patterns

#### Adapter Pattern
```typescript
class OldSystem {
    specificRequest(): string {
        return 'Special behavior from old system';
    }
}

interface Target {
    request(): string;
}

class Adapter implements Target {
    private adaptee: OldSystem;
    
    constructor(adaptee: OldSystem) {
        this.adaptee = adaptee;
    }
    
    request(): string {
        const result = this.adaptee.specificRequest();
        return `Adapter: (TRANSLATED) ${result}`;
    }
}

// Usage
const oldSystem = new OldSystem();
const adapter = new Adapter(oldSystem);
console.log(adapter.request());
```

#### Decorator Pattern
```typescript
interface Component {
    operation(): string;
}

class ConcreteComponent implements Component {
    operation(): string {
        return 'ConcreteComponent';
    }
}

class BaseDecorator implements Component {
    protected component: Component;
    
    constructor(component: Component) {
        this.component = component;
    }
    
    operation(): string {
        return this.component.operation();
    }
}

class ConcreteDecoratorA extends BaseDecorator {
    operation(): string {
        return `ConcreteDecoratorA(${super.operation()})`;
    }
}

class ConcreteDecoratorB extends BaseDecorator {
    operation(): string {
        return `ConcreteDecoratorB(${super.operation()})`;
    }
}

// Usage
let simple = new ConcreteComponent();
console.log(simple.operation()); // ConcreteComponent

let decorator1 = new ConcreteDecoratorA(simple);
console.log(decorator1.operation()); // ConcreteDecoratorA(ConcreteComponent)

let decorator2 = new ConcreteDecoratorB(decorator1);
console.log(decorator2.operation()); // ConcreteDecoratorB(ConcreteDecoratorA(ConcreteComponent))
```

### Behavioral Patterns

#### Observer Pattern
```typescript
interface Observer {
    update(subject: Subject): void;
}

interface Subject {
    attach(observer: Observer): void;
    detach(observer: Observer): void;
    notify(): void;
}

class ConcreteSubject implements Subject {
    private observers: Observer[] = [];
    private state: number;
    
    attach(observer: Observer): void {
        const isExist = this.observers.includes(observer);
        if (isExist) {
            return console.log('Subject: Observer has been attached already.');
        }
        
        console.log('Subject: Attached an observer.');
        this.observers.push(observer);
    }
    
    detach(observer: Observer): void {
        const observerIndex = this.observers.indexOf(observer);
        if (observerIndex === -1) {
            return console.log('Subject: Nonexistent observer.');
        }
        
        this.observers.splice(observerIndex, 1);
        console.log('Subject: Detached an observer.');
    }
    
    notify(): void {
        console.log('Subject: Notifying observers...');
        for (const observer of this.observers) {
            observer.update(this);
        }
    }
    
    someBusinessLogic(): void {
        console.log('Subject: I\'m doing something important.');
        this.state = Math.floor(Math.random() * (10 + 1));
        
        console.log(`Subject: My state has just changed to: ${this.state}`);
        this.notify();
    }
    
    getState(): number {
        return this.state;
    }
}

class ConcreteObserverA implements Observer {
    update(subject: Subject): void {
        if (subject instanceof ConcreteSubject && subject.getState() < 3) {
            console.log('ConcreteObserverA: Reacted to the event.');
        }
    }
}

class ConcreteObserverB implements Observer {
    update(subject: Subject): void {
        if (subject instanceof ConcreteSubject && (subject.getState() === 0 || subject.getState() >= 2)) {
            console.log('ConcreteObserverB: Reacted to the event.');
        }
    }
}
```

#### Strategy Pattern
```typescript
interface Strategy {
    doAlgorithm(data: string[]): string[];
}

class ConcreteStrategyA implements Strategy {
    doAlgorithm(data: string[]): string[] {
        return data.sort();
    }
}

class ConcreteStrategyB implements Strategy {
    doAlgorithm(data: string[]): string[] {
        return data.reverse();
    }
}

class Context {
    private strategy: Strategy;
    
    constructor(strategy: Strategy) {
        this.strategy = strategy;
    }
    
    setStrategy(strategy: Strategy): void {
        this.strategy = strategy;
    }
    
    doSomeBusinessLogic(): void {
        console.log('Context: Sorting data using the strategy (not sure how it\'ll do it)');
        const result = this.strategy.doAlgorithm(['a', 'b', 'c', 'd', 'e']);
        console.log(result.join(','));
    }
}

// Usage
const context = new Context(new ConcreteStrategyA());
console.log('Client: Strategy is set to normal sorting.');
context.doSomeBusinessLogic();

console.log('Client: Strategy is set to reverse sorting.');
context.setStrategy(new ConcreteStrategyB());
context.doSomeBusinessLogic();
```

---

## 🏛️ Architectural Patterns

### Model-View-Controller (MVC)

```typescript
// Model
class UserModel {
    private users: User[] = [];
    
    addUser(user: User): void {
        this.users.push(user);
    }
    
    getUsers(): User[] {
        return this.users;
    }
    
    getUserById(id: string): User | undefined {
        return this.users.find(user => user.id === id);
    }
}

// View
class UserView {
    displayUsers(users: User[]): void {
        console.log('Users:');
        users.forEach(user => {
            console.log(`- ${user.name} (${user.email})`);
        });
    }
    
    displayUser(user: User): void {
        console.log(`User: ${user.name} - ${user.email}`);
    }
}

// Controller
class UserController {
    constructor(
        private model: UserModel,
        private view: UserView
    ) {}
    
    addUser(name: string, email: string): void {
        const user = new User(generateId(), name, email);
        this.model.addUser(user);
    }
    
    showUsers(): void {
        const users = this.model.getUsers();
        this.view.displayUsers(users);
    }
    
    showUser(id: string): void {
        const user = this.model.getUserById(id);
        if (user) {
            this.view.displayUser(user);
        } else {
            console.log('User not found');
        }
    }
}
```

### Repository Pattern

```typescript
interface Repository<T> {
    findById(id: string): Promise<T | null>;
    findAll(): Promise<T[]>;
    save(entity: T): Promise<T>;
    update(id: string, entity: Partial<T>): Promise<T | null>;
    delete(id: string): Promise<boolean>;
}

class UserRepository implements Repository<User> {
    private database: Database;
    
    constructor(database: Database) {
        this.database = database;
    }
    
    async findById(id: string): Promise<User | null> {
        const userData = await this.database.query('SELECT * FROM users WHERE id = ?', [id]);
        return userData ? this.mapToUser(userData) : null;
    }
    
    async findAll(): Promise<User[]> {
        const usersData = await this.database.query('SELECT * FROM users');
        return usersData.map(this.mapToUser);
    }
    
    async save(user: User): Promise<User> {
        const result = await this.database.query(
            'INSERT INTO users (name, email) VALUES (?, ?)',
            [user.name, user.email]
        );
        user.id = result.insertId;
        return user;
    }
    
    async update(id: string, userData: Partial<User>): Promise<User | null> {
        await this.database.query(
            'UPDATE users SET name = ?, email = ? WHERE id = ?',
            [userData.name, userData.email, id]
        );
        return this.findById(id);
    }
    
    async delete(id: string): Promise<boolean> {
        const result = await this.database.query('DELETE FROM users WHERE id = ?', [id]);
        return result.affectedRows > 0;
    }
    
    private mapToUser(data: any): User {
        return new User(data.id, data.name, data.email);
    }
}
```

### Service Layer Pattern

```typescript
class UserService {
    constructor(
        private userRepository: UserRepository,
        private emailService: EmailService,
        private logger: Logger
    ) {}
    
    async createUser(userData: CreateUserRequest): Promise<User> {
        try {
            // Validation
            this.validateUserData(userData);
            
            // Business logic
            const existingUser = await this.userRepository.findByEmail(userData.email);
            if (existingUser) {
                throw new Error('User with this email already exists');
            }
            
            // Create user
            const user = new User(generateId(), userData.name, userData.email);
            const savedUser = await this.userRepository.save(user);
            
            // Side effects
            await this.emailService.sendWelcomeEmail(savedUser);
            this.logger.log(`User created: ${savedUser.id}`);
            
            return savedUser;
        } catch (error) {
            this.logger.error(`Failed to create user: ${error.message}`);
            throw error;
        }
    }
    
    async getUserById(id: string): Promise<User> {
        const user = await this.userRepository.findById(id);
        if (!user) {
            throw new Error('User not found');
        }
        return user;
    }
    
    async updateUser(id: string, userData: UpdateUserRequest): Promise<User> {
        try {
            this.validateUserData(userData);
            
            const updatedUser = await this.userRepository.update(id, userData);
            if (!updatedUser) {
                throw new Error('User not found');
            }
            
            this.logger.log(`User updated: ${id}`);
            return updatedUser;
        } catch (error) {
            this.logger.error(`Failed to update user: ${error.message}`);
            throw error;
        }
    }
    
    private validateUserData(userData: Partial<CreateUserRequest>): void {
        if (userData.email && !this.isValidEmail(userData.email)) {
            throw new Error('Invalid email format');
        }
        if (userData.name && userData.name.trim().length < 2) {
            throw new Error('Name must be at least 2 characters long');
        }
    }
    
    private isValidEmail(email: string): boolean {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }
}
```

---

## 🎯 Best Practices Checklist

### Code Organization
```markdown
☐ **Single Responsibility**: Each class/function has one clear purpose
☐ **Separation of Concerns**: UI, business logic, and data access are separated
☐ **Dependency Injection**: Dependencies are injected, not created internally
☐ **Interface Segregation**: Interfaces are small and focused
☐ **Composition over Inheritance**: Favor composition when possible
```

### Error Handling
```markdown
☐ **Fail Fast**: Validate inputs early and throw meaningful errors
☐ **Exception Safety**: Ensure resources are properly cleaned up
☐ **Error Propagation**: Don't swallow exceptions without handling
☐ **Logging**: Log errors with sufficient context for debugging
☐ **Recovery**: Provide graceful degradation when possible
```

### Performance
```markdown
☐ **Lazy Loading**: Load resources only when needed
☐ **Caching**: Cache expensive operations appropriately
☐ **Resource Management**: Properly dispose of resources
☐ **Async Operations**: Use async/await for I/O operations
☐ **Memory Management**: Avoid memory leaks and excessive allocations
```

### Testing
```markdown
☐ **Unit Tests**: Test individual components in isolation
☐ **Integration Tests**: Test component interactions
☐ **Mocking**: Mock external dependencies in tests
☐ **Test Coverage**: Maintain high test coverage (>90%)
☐ **Test Clarity**: Tests are readable and maintainable
```

### Security
```markdown
☐ **Input Validation**: Validate all inputs at boundaries
☐ **Output Encoding**: Encode outputs to prevent injection
☐ **Authentication**: Implement proper authentication mechanisms
☐ **Authorization**: Enforce access controls consistently
☐ **Encryption**: Encrypt sensitive data at rest and in transit
```

---

## Integration with .god Ecosystem

### Sub-Agent Integration
- **Bug Hunter**: Pattern violation detection
- **Security Auditor**: Security pattern compliance
- **Performance Analyzer**: Performance pattern optimization
- **Test Executor**: Pattern-based test generation

### Workflow Integration
- **TSDDR 2.0**: Pattern-driven design and testing
- **Kiro Workflow**: Pattern compliance in task execution
- **Code Review**: Pattern adherence validation

---

**Usage**: Foundation for all development decisions  
**Enforcement**: Automated pattern detection and validation  
**Evolution**: Continuous pattern refinement based on project needs