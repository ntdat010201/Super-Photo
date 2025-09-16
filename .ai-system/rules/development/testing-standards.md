# 🧪 Testing Standards - Universal Quality Assurance Guidelines

> **🎯 Comprehensive testing framework for reliable software delivery**  
> Standardized testing practices across all platforms and technologies

---

## 🎯 Testing Philosophy

### Core Testing Principles
```
🔺 Testing Pyramid
     /\     E2E Tests (10%)
    /  \    - User journey validation
   /____\   - Critical path testing
  /      \  Integration Tests (20%)
 /        \ - API contract testing
/__________\ Unit Tests (70%)
             - Function/method testing
             - Business logic validation
```

### Quality Gates
```bash
# Minimum Coverage Requirements
Unit Tests:        >= 80%
Integration Tests: >= 60%
E2E Tests:         >= 90% critical paths

# Performance Benchmarks
Test Execution:    < 10 minutes (full suite)
Unit Tests:        < 2 minutes
Integration Tests: < 5 minutes
E2E Tests:         < 15 minutes
```

---

## 🔬 Unit Testing Standards

### Test Structure (AAA Pattern)
```javascript
// Arrange - Act - Assert Pattern
describe('UserService', () => {
  describe('createUser', () => {
    it('should create user with valid data', async () => {
      // Arrange
      const userData = {
        email: 'test@example.com',
        password: 'SecurePass123!',
        name: 'John Doe'
      };
      const mockRepository = {
        save: jest.fn().mockResolvedValue({ id: 1, ...userData })
      };
      const userService = new UserService(mockRepository);

      // Act
      const result = await userService.createUser(userData);

      // Assert
      expect(result).toEqual({
        id: 1,
        email: 'test@example.com',
        name: 'John Doe'
      });
      expect(mockRepository.save).toHaveBeenCalledWith(userData);
    });

    it('should throw error for invalid email', async () => {
      // Arrange
      const invalidUserData = {
        email: 'invalid-email',
        password: 'SecurePass123!',
        name: 'John Doe'
      };
      const userService = new UserService(mockRepository);

      // Act & Assert
      await expect(userService.createUser(invalidUserData))
        .rejects
        .toThrow('Invalid email format');
    });
  });
});
```

### Test Naming Conventions
```javascript
// Good Test Names
✅ 'should return user when valid ID is provided'
✅ 'should throw NotFoundError when user does not exist'
✅ 'should hash password before saving to database'
✅ 'should send welcome email after successful registration'

// Bad Test Names
❌ 'test user creation'
❌ 'should work'
❌ 'test1'
❌ 'user test'
```

### Mock and Stub Guidelines
```javascript
// External Dependencies Mocking
const mockEmailService = {
  sendWelcomeEmail: jest.fn().mockResolvedValue(true),
  sendPasswordReset: jest.fn().mockResolvedValue(true)
};

// Database Mocking
const mockUserRepository = {
  findById: jest.fn(),
  save: jest.fn(),
  delete: jest.fn()
};

// HTTP Client Mocking
const mockHttpClient = {
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn()
};

// Test Data Factories
const createMockUser = (overrides = {}) => ({
  id: 1,
  email: 'test@example.com',
  name: 'John Doe',
  createdAt: new Date('2023-01-01'),
  ...overrides
});

// Usage in tests
it('should update user profile', async () => {
  const existingUser = createMockUser({ id: 123 });
  const updateData = { name: 'Jane Doe' };
  
  mockUserRepository.findById.mockResolvedValue(existingUser);
  mockUserRepository.save.mockResolvedValue({ ...existingUser, ...updateData });
  
  const result = await userService.updateProfile(123, updateData);
  
  expect(result.name).toBe('Jane Doe');
});
```

---

## 🔗 Integration Testing Standards

### API Integration Tests
```javascript
// Express.js API Testing
const request = require('supertest');
const app = require('../app');

describe('User API Integration', () => {
  beforeEach(async () => {
    // Setup test database
    await setupTestDatabase();
  });

  afterEach(async () => {
    // Cleanup test database
    await cleanupTestDatabase();
  });

  describe('POST /api/users', () => {
    it('should create user and return 201', async () => {
      const userData = {
        email: 'test@example.com',
        password: 'SecurePass123!',
        name: 'John Doe'
      };

      const response = await request(app)
        .post('/api/users')
        .send(userData)
        .expect(201);

      expect(response.body).toMatchObject({
        id: expect.any(Number),
        email: 'test@example.com',
        name: 'John Doe'
      });
      expect(response.body.password).toBeUndefined();
    });

    it('should return 400 for invalid data', async () => {
      const invalidData = {
        email: 'invalid-email',
        password: '123' // Too short
      };

      const response = await request(app)
        .post('/api/users')
        .send(invalidData)
        .expect(400);

      expect(response.body.errors).toEqual(
        expect.arrayContaining([
          expect.objectContaining({
            field: 'email',
            message: 'Invalid email format'
          }),
          expect.objectContaining({
            field: 'password',
            message: 'Password must be at least 8 characters'
          })
        ])
      );
    });
  });

  describe('GET /api/users/:id', () => {
    it('should return user when exists', async () => {
      const user = await createTestUser({
        email: 'test@example.com',
        name: 'John Doe'
      });

      const response = await request(app)
        .get(`/api/users/${user.id}`)
        .expect(200);

      expect(response.body).toMatchObject({
        id: user.id,
        email: 'test@example.com',
        name: 'John Doe'
      });
    });

    it('should return 404 when user not found', async () => {
      await request(app)
        .get('/api/users/999999')
        .expect(404);
    });
  });
});
```

### Database Integration Tests
```javascript
// Database Integration Testing
const { Pool } = require('pg');
const UserRepository = require('../repositories/UserRepository');

describe('UserRepository Integration', () => {
  let pool;
  let userRepository;

  beforeAll(async () => {
    pool = new Pool({
      connectionString: process.env.TEST_DATABASE_URL
    });
    userRepository = new UserRepository(pool);
  });

  afterAll(async () => {
    await pool.end();
  });

  beforeEach(async () => {
    await pool.query('TRUNCATE TABLE users RESTART IDENTITY CASCADE');
  });

  describe('save', () => {
    it('should save user to database', async () => {
      const userData = {
        email: 'test@example.com',
        password: 'hashedPassword',
        name: 'John Doe'
      };

      const savedUser = await userRepository.save(userData);

      expect(savedUser).toMatchObject({
        id: expect.any(Number),
        email: 'test@example.com',
        name: 'John Doe',
        createdAt: expect.any(Date)
      });

      // Verify in database
      const result = await pool.query(
        'SELECT * FROM users WHERE id = $1',
        [savedUser.id]
      );
      expect(result.rows).toHaveLength(1);
      expect(result.rows[0].email).toBe('test@example.com');
    });

    it('should enforce unique email constraint', async () => {
      const userData = {
        email: 'test@example.com',
        password: 'hashedPassword',
        name: 'John Doe'
      };

      await userRepository.save(userData);

      await expect(userRepository.save(userData))
        .rejects
        .toThrow('Email already exists');
    });
  });
});
```

---

## 🎭 End-to-End Testing Standards

### Cypress E2E Tests
```javascript
// cypress/integration/user-registration.spec.js
describe('User Registration Flow', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('should complete full registration process', () => {
    // Fill registration form
    cy.get('[data-testid="email-input"]')
      .type('test@example.com');
    
    cy.get('[data-testid="password-input"]')
      .type('SecurePass123!');
    
    cy.get('[data-testid="confirm-password-input"]')
      .type('SecurePass123!');
    
    cy.get('[data-testid="name-input"]')
      .type('John Doe');
    
    cy.get('[data-testid="terms-checkbox"]')
      .check();

    // Submit form
    cy.get('[data-testid="register-button"]')
      .click();

    // Verify success
    cy.url().should('include', '/welcome');
    cy.get('[data-testid="welcome-message"]')
      .should('contain', 'Welcome, John Doe!');
    
    // Verify email sent
    cy.get('[data-testid="email-verification-notice"]')
      .should('be.visible')
      .and('contain', 'verification email sent to test@example.com');
  });

  it('should show validation errors for invalid data', () => {
    cy.get('[data-testid="register-button"]')
      .click();

    // Check validation errors
    cy.get('[data-testid="email-error"]')
      .should('be.visible')
      .and('contain', 'Email is required');
    
    cy.get('[data-testid="password-error"]')
      .should('be.visible')
      .and('contain', 'Password is required');

    // Fill invalid email
    cy.get('[data-testid="email-input"]')
      .type('invalid-email');
    
    cy.get('[data-testid="register-button"]')
      .click();
    
    cy.get('[data-testid="email-error"]')
      .should('contain', 'Invalid email format');
  });

  it('should handle server errors gracefully', () => {
    // Mock server error
    cy.intercept('POST', '/api/users', {
      statusCode: 500,
      body: { error: 'Internal server error' }
    }).as('registerError');

    // Fill valid form
    cy.get('[data-testid="email-input"]').type('test@example.com');
    cy.get('[data-testid="password-input"]').type('SecurePass123!');
    cy.get('[data-testid="confirm-password-input"]').type('SecurePass123!');
    cy.get('[data-testid="name-input"]').type('John Doe');
    cy.get('[data-testid="terms-checkbox"]').check();
    
    cy.get('[data-testid="register-button"]').click();
    
    cy.wait('@registerError');
    
    // Verify error handling
    cy.get('[data-testid="error-message"]')
      .should('be.visible')
      .and('contain', 'Registration failed. Please try again.');
  });
});
```

### Playwright E2E Tests
```javascript
// tests/e2e/user-authentication.spec.js
const { test, expect } = require('@playwright/test');

test.describe('User Authentication', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
  });

  test('should login with valid credentials', async ({ page }) => {
    // Create test user
    await page.request.post('/api/test/users', {
      data: {
        email: 'test@example.com',
        password: 'SecurePass123!',
        name: 'John Doe'
      }
    });

    // Login
    await page.fill('[data-testid="email-input"]', 'test@example.com');
    await page.fill('[data-testid="password-input"]', 'SecurePass123!');
    await page.click('[data-testid="login-button"]');

    // Verify successful login
    await expect(page).toHaveURL('/dashboard');
    await expect(page.locator('[data-testid="user-name"]'))
      .toHaveText('John Doe');
  });

  test('should show error for invalid credentials', async ({ page }) => {
    await page.fill('[data-testid="email-input"]', 'wrong@example.com');
    await page.fill('[data-testid="password-input"]', 'wrongpassword');
    await page.click('[data-testid="login-button"]');

    await expect(page.locator('[data-testid="error-message"]'))
      .toHaveText('Invalid email or password');
  });

  test('should handle network errors', async ({ page }) => {
    // Simulate network failure
    await page.route('/api/auth/login', route => {
      route.abort('failed');
    });

    await page.fill('[data-testid="email-input"]', 'test@example.com');
    await page.fill('[data-testid="password-input"]', 'SecurePass123!');
    await page.click('[data-testid="login-button"]');

    await expect(page.locator('[data-testid="error-message"]'))
      .toHaveText('Network error. Please check your connection.');
  });
});
```

---

## 📱 Mobile Testing Standards

### Android Testing (Kotlin)
```kotlin
// Unit Test Example
class UserViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    @Mock
    private lateinit var userRepository: UserRepository
    
    @Mock
    private lateinit var authService: AuthService
    
    private lateinit var viewModel: UserViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = UserViewModel(userRepository, authService)
    }
    
    @Test
    fun `loginUser should update uiState with success when credentials are valid`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "SecurePass123!"
        val expectedUser = User(1, email, "John Doe")
        
        whenever(authService.login(email, password))
            .thenReturn(Result.success(expectedUser))
        
        // Act
        viewModel.loginUser(email, password)
        
        // Assert
        val uiState = viewModel.uiState.value
        assertThat(uiState.isLoading).isFalse()
        assertThat(uiState.user).isEqualTo(expectedUser)
        assertThat(uiState.error).isNull()
    }
    
    @Test
    fun `loginUser should update uiState with error when credentials are invalid`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "wrongpassword"
        val expectedError = "Invalid credentials"
        
        whenever(authService.login(email, password))
            .thenReturn(Result.failure(Exception(expectedError)))
        
        // Act
        viewModel.loginUser(email, password)
        
        // Assert
        val uiState = viewModel.uiState.value
        assertThat(uiState.isLoading).isFalse()
        assertThat(uiState.user).isNull()
        assertThat(uiState.error).isEqualTo(expectedError)
    }
}

// UI Test Example
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)
    
    @Test
    fun loginWithValidCredentials_shouldNavigateToDashboard() {
        // Arrange
        val email = "test@example.com"
        val password = "SecurePass123!"
        
        // Act
        onView(withId(R.id.emailEditText))
            .perform(typeText(email), closeSoftKeyboard())
        
        onView(withId(R.id.passwordEditText))
            .perform(typeText(password), closeSoftKeyboard())
        
        onView(withId(R.id.loginButton))
            .perform(click())
        
        // Assert
        intended(hasComponent(DashboardActivity::class.java.name))
    }
    
    @Test
    fun loginWithInvalidCredentials_shouldShowError() {
        // Arrange
        val email = "invalid@example.com"
        val password = "wrongpassword"
        
        // Act
        onView(withId(R.id.emailEditText))
            .perform(typeText(email), closeSoftKeyboard())
        
        onView(withId(R.id.passwordEditText))
            .perform(typeText(password), closeSoftKeyboard())
        
        onView(withId(R.id.loginButton))
            .perform(click())
        
        // Assert
        onView(withId(R.id.errorTextView))
            .check(matches(withText("Invalid email or password")))
            .check(matches(isDisplayed()))
    }
}
```

### iOS Testing (Swift)
```swift
// Unit Test Example
class UserViewModelTests: XCTestCase {
    var viewModel: UserViewModel!
    var mockUserService: MockUserService!
    var mockAuthService: MockAuthService!
    
    override func setUp() {
        super.setUp()
        mockUserService = MockUserService()
        mockAuthService = MockAuthService()
        viewModel = UserViewModel(
            userService: mockUserService,
            authService: mockAuthService
        )
    }
    
    override func tearDown() {
        viewModel = nil
        mockUserService = nil
        mockAuthService = nil
        super.tearDown()
    }
    
    func testLoginUser_WithValidCredentials_ShouldUpdateStateWithSuccess() async {
        // Arrange
        let email = "test@example.com"
        let password = "SecurePass123!"
        let expectedUser = User(id: 1, email: email, name: "John Doe")
        
        mockAuthService.loginResult = .success(expectedUser)
        
        // Act
        await viewModel.loginUser(email: email, password: password)
        
        // Assert
        XCTAssertFalse(viewModel.isLoading)
        XCTAssertEqual(viewModel.user?.id, expectedUser.id)
        XCTAssertEqual(viewModel.user?.email, expectedUser.email)
        XCTAssertNil(viewModel.errorMessage)
    }
    
    func testLoginUser_WithInvalidCredentials_ShouldUpdateStateWithError() async {
        // Arrange
        let email = "test@example.com"
        let password = "wrongpassword"
        let expectedError = AuthError.invalidCredentials
        
        mockAuthService.loginResult = .failure(expectedError)
        
        // Act
        await viewModel.loginUser(email: email, password: password)
        
        // Assert
        XCTAssertFalse(viewModel.isLoading)
        XCTAssertNil(viewModel.user)
        XCTAssertEqual(viewModel.errorMessage, "Invalid email or password")
    }
}

// UI Test Example
class LoginUITests: XCTestCase {
    var app: XCUIApplication!
    
    override func setUp() {
        super.setUp()
        continueAfterFailure = false
        app = XCUIApplication()
        app.launch()
    }
    
    func testLoginWithValidCredentials_ShouldNavigateToDashboard() {
        // Arrange
        let emailField = app.textFields["emailTextField"]
        let passwordField = app.secureTextFields["passwordTextField"]
        let loginButton = app.buttons["loginButton"]
        
        // Act
        emailField.tap()
        emailField.typeText("test@example.com")
        
        passwordField.tap()
        passwordField.typeText("SecurePass123!")
        
        loginButton.tap()
        
        // Assert
        let dashboardTitle = app.navigationBars["Dashboard"]
        XCTAssertTrue(dashboardTitle.waitForExistence(timeout: 5))
    }
    
    func testLoginWithInvalidCredentials_ShouldShowError() {
        // Arrange
        let emailField = app.textFields["emailTextField"]
        let passwordField = app.secureTextFields["passwordTextField"]
        let loginButton = app.buttons["loginButton"]
        
        // Act
        emailField.tap()
        emailField.typeText("invalid@example.com")
        
        passwordField.tap()
        passwordField.typeText("wrongpassword")
        
        loginButton.tap()
        
        // Assert
        let errorAlert = app.alerts["Error"]
        XCTAssertTrue(errorAlert.waitForExistence(timeout: 5))
        XCTAssertTrue(errorAlert.staticTexts["Invalid email or password"].exists)
    }
}
```

---

## ⚡ Performance Testing Standards

### Load Testing with Artillery
```yaml
# artillery-config.yml
config:
  target: 'http://localhost:3000'
  phases:
    - duration: 60
      arrivalRate: 10
      name: "Warm up"
    - duration: 120
      arrivalRate: 50
      name: "Ramp up load"
    - duration: 300
      arrivalRate: 100
      name: "Sustained load"
  payload:
    path: "users.csv"
    fields:
      - "email"
      - "password"

scenarios:
  - name: "User Registration and Login"
    weight: 70
    flow:
      - post:
          url: "/api/auth/register"
          json:
            email: "{{ email }}"
            password: "{{ password }}"
            name: "Test User"
          capture:
            - json: "$.token"
              as: "authToken"
      - post:
          url: "/api/auth/login"
          json:
            email: "{{ email }}"
            password: "{{ password }}"
          headers:
            Authorization: "Bearer {{ authToken }}"
  
  - name: "API Endpoints Load Test"
    weight: 30
    flow:
      - get:
          url: "/api/users/profile"
          headers:
            Authorization: "Bearer {{ authToken }}"
      - put:
          url: "/api/users/profile"
          json:
            name: "Updated Name"
          headers:
            Authorization: "Bearer {{ authToken }}"
```

### Performance Benchmarking
```javascript
// performance/benchmark.js
const autocannon = require('autocannon');

const runBenchmark = async () => {
  const result = await autocannon({
    url: 'http://localhost:3000',
    connections: 100,
    duration: 30,
    requests: [
      {
        method: 'GET',
        path: '/api/health'
      },
      {
        method: 'POST',
        path: '/api/auth/login',
        headers: {
          'content-type': 'application/json'
        },
        body: JSON.stringify({
          email: 'test@example.com',
          password: 'SecurePass123!'
        })
      }
    ]
  });

  console.log('Benchmark Results:');
  console.log(`Requests/sec: ${result.requests.average}`);
  console.log(`Latency: ${result.latency.average}ms`);
  console.log(`Throughput: ${result.throughput.average} bytes/sec`);
  
  // Performance assertions
  if (result.requests.average < 1000) {
    throw new Error('Performance below threshold: < 1000 req/sec');
  }
  
  if (result.latency.average > 100) {
    throw new Error('Latency above threshold: > 100ms');
  }
};

runBenchmark().catch(console.error);
```

---

## 🔒 Security Testing Standards

### Security Test Cases
```javascript
// security/auth-security.test.js
describe('Authentication Security', () => {
  describe('Password Security', () => {
    it('should reject weak passwords', async () => {
      const weakPasswords = [
        '123456',
        'password',
        'qwerty',
        '12345678',
        'abc123'
      ];

      for (const password of weakPasswords) {
        const response = await request(app)
          .post('/api/auth/register')
          .send({
            email: 'test@example.com',
            password: password,
            name: 'Test User'
          })
          .expect(400);

        expect(response.body.errors).toContainEqual(
          expect.objectContaining({
            field: 'password',
            message: expect.stringContaining('Password too weak')
          })
        );
      }
    });

    it('should hash passwords before storage', async () => {
      const password = 'SecurePass123!';
      
      await request(app)
        .post('/api/auth/register')
        .send({
          email: 'test@example.com',
          password: password,
          name: 'Test User'
        })
        .expect(201);

      const user = await User.findOne({ email: 'test@example.com' });
      expect(user.password).not.toBe(password);
      expect(user.password).toMatch(/^\$2[aby]\$\d+\$/);
    });
  });

  describe('JWT Security', () => {
    it('should reject expired tokens', async () => {
      const expiredToken = jwt.sign(
        { userId: 1 },
        process.env.JWT_SECRET,
        { expiresIn: '-1h' }
      );

      await request(app)
        .get('/api/users/profile')
        .set('Authorization', `Bearer ${expiredToken}`)
        .expect(401);
    });

    it('should reject malformed tokens', async () => {
      const malformedTokens = [
        'invalid.token.here',
        'Bearer invalid',
        'malformed-token',
        ''
      ];

      for (const token of malformedTokens) {
        await request(app)
          .get('/api/users/profile')
          .set('Authorization', `Bearer ${token}`)
          .expect(401);
      }
    });
  });

  describe('Input Validation', () => {
    it('should prevent SQL injection', async () => {
      const maliciousInputs = [
        "'; DROP TABLE users; --",
        "1' OR '1'='1",
        "admin'--",
        "' UNION SELECT * FROM users --"
      ];

      for (const input of maliciousInputs) {
        await request(app)
          .post('/api/auth/login')
          .send({
            email: input,
            password: 'password'
          })
          .expect(400);
      }
    });

    it('should prevent XSS attacks', async () => {
      const xssPayloads = [
        '<script>alert("XSS")</script>',
        'javascript:alert("XSS")',
        '<img src=x onerror=alert("XSS")>',
        '<svg onload=alert("XSS")>'
      ];

      for (const payload of xssPayloads) {
        const response = await request(app)
          .post('/api/users')
          .send({
            email: 'test@example.com',
            name: payload,
            password: 'SecurePass123!'
          })
          .expect(400);

        expect(response.body.errors).toContainEqual(
          expect.objectContaining({
            field: 'name',
            message: expect.stringContaining('Invalid characters')
          })
        );
      }
    });
  });

  describe('Rate Limiting', () => {
    it('should enforce rate limits on login attempts', async () => {
      const loginData = {
        email: 'test@example.com',
        password: 'wrongpassword'
      };

      // Make multiple failed login attempts
      for (let i = 0; i < 5; i++) {
        await request(app)
          .post('/api/auth/login')
          .send(loginData)
          .expect(401);
      }

      // 6th attempt should be rate limited
      await request(app)
        .post('/api/auth/login')
        .send(loginData)
        .expect(429);
    });
  });
});
```

---

## 📊 Test Reporting & Metrics

### Coverage Reporting
```javascript
// jest.config.js
module.exports = {
  collectCoverage: true,
  coverageDirectory: 'coverage',
  coverageReporters: [
    'text',
    'lcov',
    'html',
    'json-summary'
  ],
  collectCoverageFrom: [
    'src/**/*.{js,ts}',
    '!src/**/*.test.{js,ts}',
    '!src/**/*.spec.{js,ts}',
    '!src/test/**',
    '!src/coverage/**'
  ],
  coverageThreshold: {
    global: {
      branches: 80,
      functions: 80,
      lines: 80,
      statements: 80
    },
    './src/services/': {
      branches: 90,
      functions: 90,
      lines: 90,
      statements: 90
    }
  }
};
```

### Test Results Dashboard
```javascript
// scripts/test-report.js
const fs = require('fs');
const path = require('path');

const generateTestReport = () => {
  const coverageData = JSON.parse(
    fs.readFileSync('./coverage/coverage-summary.json', 'utf8')
  );
  
  const testResults = {
    timestamp: new Date().toISOString(),
    coverage: {
      lines: coverageData.total.lines.pct,
      functions: coverageData.total.functions.pct,
      branches: coverageData.total.branches.pct,
      statements: coverageData.total.statements.pct
    },
    thresholds: {
      lines: 80,
      functions: 80,
      branches: 80,
      statements: 80
    },
    status: 'PASSED' // or 'FAILED'
  };
  
  // Generate HTML report
  const htmlReport = `
    <!DOCTYPE html>
    <html>
    <head>
      <title>Test Coverage Report</title>
      <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .metric { display: inline-block; margin: 10px; padding: 20px; border-radius: 5px; }
        .passed { background-color: #d4edda; border: 1px solid #c3e6cb; }
        .failed { background-color: #f8d7da; border: 1px solid #f5c6cb; }
      </style>
    </head>
    <body>
      <h1>Test Coverage Report</h1>
      <p>Generated: ${testResults.timestamp}</p>
      
      <div class="metric ${testResults.coverage.lines >= testResults.thresholds.lines ? 'passed' : 'failed'}">
        <h3>Lines</h3>
        <p>${testResults.coverage.lines}% (Threshold: ${testResults.thresholds.lines}%)</p>
      </div>
      
      <div class="metric ${testResults.coverage.functions >= testResults.thresholds.functions ? 'passed' : 'failed'}">
        <h3>Functions</h3>
        <p>${testResults.coverage.functions}% (Threshold: ${testResults.thresholds.functions}%)</p>
      </div>
      
      <div class="metric ${testResults.coverage.branches >= testResults.thresholds.branches ? 'passed' : 'failed'}">
        <h3>Branches</h3>
        <p>${testResults.coverage.branches}% (Threshold: ${testResults.thresholds.branches}%)</p>
      </div>
      
      <div class="metric ${testResults.coverage.statements >= testResults.thresholds.statements ? 'passed' : 'failed'}">
        <h3>Statements</h3>
        <p>${testResults.coverage.statements}% (Threshold: ${testResults.thresholds.statements}%)</p>
      </div>
    </body>
    </html>
  `;
  
  fs.writeFileSync('./coverage/report.html', htmlReport);
  console.log('Test report generated: ./coverage/report.html');
};

generateTestReport();
```

---

## ✅ Testing Checklist

### Pre-Development
- [ ] Test strategy defined for feature
- [ ] Test cases written before implementation
- [ ] Test data and fixtures prepared
- [ ] Mock services and dependencies identified
- [ ] Performance benchmarks established

### During Development
- [ ] Unit tests written for each function/method
- [ ] Integration tests for API endpoints
- [ ] UI tests for user interactions
- [ ] Edge cases and error scenarios covered
- [ ] Security vulnerabilities tested

### Pre-Deployment
- [ ] All tests passing (unit, integration, E2E)
- [ ] Code coverage meets minimum thresholds
- [ ] Performance tests within acceptable limits
- [ ] Security scans completed
- [ ] Load testing performed
- [ ] Browser/device compatibility verified

### Post-Deployment
- [ ] Smoke tests in production environment
- [ ] Monitoring and alerting configured
- [ ] Performance metrics baseline established
- [ ] User acceptance testing completed
- [ ] Rollback plan tested

---

**🧪 Comprehensive testing framework ensuring reliable, secure, and performant software delivery across all platforms and technologies.**