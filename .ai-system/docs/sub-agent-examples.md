# Sub-Agent System Examples

> **🎯 Real-world Examples and Use Cases**  
> Các ví dụ thực tế về cách Sub-Agent System hoạt động trong development workflow

## 📱 Mobile Development Examples

### Example 1: Android App with Performance Issues

**Scenario**: Developing a social media Android app with slow list scrolling

```kotlin
// Original problematic code
class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostViewHolder>() {
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        
        // Performance issue: Loading image on main thread
        val bitmap = BitmapFactory.decodeFile(post.imagePath)
        holder.imageView.setImageBitmap(bitmap)
        
        // Performance issue: Heavy text processing
        holder.textView.text = processRichText(post.content)
    }
}

// Performance Analyzer detects issues:
// ⚡ Performance Alert: Main thread blocking operations detected
// 💡 Optimization suggestions:

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostViewHolder>() {
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        
        // Optimized: Async image loading with caching
        Glide.with(holder.itemView.context)
            .load(post.imagePath)
            .placeholder(R.drawable.placeholder)
            .into(holder.imageView)
        
        // Optimized: Pre-processed text with caching
        holder.textView.text = post.processedContent ?: processRichText(post.content)
    }
}

// Test Executor automatically generates performance tests:
class PostAdapterPerformanceTest {
    @Test
    fun `should bind 1000 items under 16ms per item`() {
        val posts = generateTestPosts(1000)
        val adapter = PostAdapter(posts)
        
        val startTime = System.nanoTime()
        // Simulate binding
        repeat(1000) { position ->
            val holder = createMockViewHolder()
            adapter.onBindViewHolder(holder, position)
        }
        val endTime = System.nanoTime()
        
        val averageTimePerItem = (endTime - startTime) / 1000000 / 1000 // Convert to ms
        assertThat(averageTimePerItem).isLessThan(16) // 60fps requirement
    }
}
```

### Example 2: iOS App Security Enhancement

**Scenario**: iOS banking app requiring enhanced security

```swift
// Original insecure code
class LoginViewController: UIViewController {
    @IBAction func loginTapped(_ sender: UIButton) {
        let username = usernameField.text ?? ""
        let password = passwordField.text ?? ""
        
        // Security issue: Plain text password
        UserDefaults.standard.set(password, forKey: "user_password")
        
        // Security issue: No input validation
        authenticateUser(username: username, password: password)
    }
}

// Security Auditor detects vulnerabilities:
// 🔒 Security Alert: Sensitive data stored in plain text
// 🔒 Security Alert: Missing input validation
// 💡 Secure implementation:

class LoginViewController: UIViewController {
    private let biometricAuth = BiometricAuthenticator()
    private let keychain = KeychainService()
    
    @IBAction func loginTapped(_ sender: UIButton) {
        guard let username = usernameField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
              let password = passwordField.text,
              validateInput(username: username, password: password) else {
            showError("Invalid input")
            return
        }
        
        // Secure: Use Keychain for sensitive data
        keychain.store(password, for: username)
        
        // Enhanced: Biometric authentication
        biometricAuth.authenticate { [weak self] result in
            switch result {
            case .success:
                self?.authenticateUser(username: username, password: password)
            case .failure(let error):
                self?.showError(error.localizedDescription)
            }
        }
    }
    
    private func validateInput(username: String, password: String) -> Bool {
        // Input validation logic
        return !username.isEmpty && password.count >= 8
    }
}

// Test Executor generates security tests:
class LoginSecurityTests: XCTestCase {
    func testPasswordNotStoredInUserDefaults() {
        let loginVC = LoginViewController()
        loginVC.simulateLogin(username: "test", password: "password123")
        
        XCTAssertNil(UserDefaults.standard.string(forKey: "user_password"))
    }
    
    func testInputValidation() {
        let loginVC = LoginViewController()
        
        XCTAssertFalse(loginVC.validateInput(username: "", password: "123"))
        XCTAssertFalse(loginVC.validateInput(username: "user", password: "123"))
        XCTAssertTrue(loginVC.validateInput(username: "user", password: "password123"))
    }
}
```

## 🌐 Web Development Examples

### Example 3: React App State Management

**Scenario**: E-commerce React app with complex state management

```typescript
// Original problematic code
function ShoppingCart() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  
  // Bug: Race condition in async operations
  const addItem = async (productId: string) => {
    setLoading(true);
    try {
      const product = await fetchProduct(productId);
      setItems([...items, product]); // Bug: Stale closure
    } catch (err) {
      setError(err.message);
    }
    setLoading(false);
  };
  
  // Performance issue: Unnecessary re-renders
  const totalPrice = items.reduce((sum, item) => sum + item.price, 0);
  
  return (
    <div>
      {items.map(item => (
        <CartItem key={item.id} item={item} onRemove={() => removeItem(item.id)} />
      ))}
      <div>Total: ${totalPrice}</div>
    </div>
  );
}

// Bug Hunter detects race condition:
// 🐛 Bug Alert: Potential race condition in state updates
// Context Optimizer suggests performance improvements:
// ⚡ Context Alert: Expensive calculation on every render
// 💡 Optimized implementation:

function ShoppingCart() {
  const [items, setItems] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  // Fixed: Use functional update to avoid stale closure
  const addItem = useCallback(async (productId: string) => {
    setLoading(true);
    setError(null);
    
    try {
      const product = await fetchProduct(productId);
      setItems(prevItems => [...prevItems, product]); // Fixed: Functional update
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unknown error');
    } finally {
      setLoading(false);
    }
  }, []);
  
  // Optimized: Memoized expensive calculation
  const totalPrice = useMemo(
    () => items.reduce((sum, item) => sum + item.price, 0),
    [items]
  );
  
  const removeItem = useCallback((itemId: string) => {
    setItems(prevItems => prevItems.filter(item => item.id !== itemId));
  }, []);
  
  return (
    <div>
      {items.map(item => (
        <CartItem 
          key={item.id} 
          item={item} 
          onRemove={removeItem}
        />
      ))}
      <div>Total: ${totalPrice.toFixed(2)}</div>
      {loading && <LoadingSpinner />}
      {error && <ErrorMessage message={error} />}
    </div>
  );
}

// Test Executor generates comprehensive tests:
describe('ShoppingCart', () => {
  test('should handle concurrent addItem calls correctly', async () => {
    const { result } = renderHook(() => useShoppingCart());
    
    // Simulate concurrent calls
    const promises = [
      result.current.addItem('product1'),
      result.current.addItem('product2'),
      result.current.addItem('product3')
    ];
    
    await Promise.all(promises);
    
    expect(result.current.items).toHaveLength(3);
    expect(result.current.items.map(item => item.id)).toEqual(
      expect.arrayContaining(['product1', 'product2', 'product3'])
    );
  });
  
  test('should memoize total price calculation', () => {
    const { result, rerender } = renderHook(() => useShoppingCart());
    
    const initialTotal = result.current.totalPrice;
    
    // Re-render without changing items
    rerender();
    
    // Should be the same reference (memoized)
    expect(result.current.totalPrice).toBe(initialTotal);
  });
});
```

### Example 4: Node.js API Security

**Scenario**: REST API with authentication and data validation

```typescript
// Original insecure code
app.post('/api/users', (req, res) => {
  const { name, email, password, role } = req.body;
  
  // Security issue: No input validation
  // Security issue: No authentication check
  // Security issue: Direct role assignment
  
  const user = new User({
    name,
    email,
    password, // Security issue: Plain text password
    role: role || 'admin' // Security issue: Default admin role
  });
  
  user.save();
  res.json({ success: true, user });
});

// Security Auditor detects multiple vulnerabilities:
// 🔒 Security Alert: Missing authentication
// 🔒 Security Alert: No input validation
// 🔒 Security Alert: Plain text password storage
// 🔒 Security Alert: Privilege escalation vulnerability
// 💡 Secure implementation:

import bcrypt from 'bcrypt';
import joi from 'joi';
import rateLimit from 'express-rate-limit';

// Rate limiting
const createUserLimiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 minutes
  max: 5, // Limit each IP to 5 requests per windowMs
  message: 'Too many user creation attempts'
});

// Input validation schema
const createUserSchema = joi.object({
  name: joi.string().min(2).max(50).required(),
  email: joi.string().email().required(),
  password: joi.string().min(8).pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/).required(),
  role: joi.string().valid('user', 'moderator').default('user')
});

app.post('/api/users', 
  createUserLimiter,
  authenticateToken, // Require authentication
  authorizeRole(['admin']), // Only admins can create users
  async (req, res) => {
    try {
      // Input validation
      const { error, value } = createUserSchema.validate(req.body);
      if (error) {
        return res.status(400).json({ 
          error: 'Validation failed', 
          details: error.details 
        });
      }
      
      const { name, email, password, role } = value;
      
      // Check if user already exists
      const existingUser = await User.findOne({ email });
      if (existingUser) {
        return res.status(409).json({ error: 'User already exists' });
      }
      
      // Hash password
      const saltRounds = 12;
      const hashedPassword = await bcrypt.hash(password, saltRounds);
      
      // Create user with validated data
      const user = new User({
        name,
        email,
        password: hashedPassword,
        role: role || 'user', // Default to 'user', not 'admin'
        createdBy: req.user.id // Audit trail
      });
      
      await user.save();
      
      // Log user creation for audit
      await AuditLog.create({
        action: 'USER_CREATED',
        performedBy: req.user.id,
        targetUser: user.id,
        timestamp: new Date()
      });
      
      // Return user without sensitive data
      const { password: _, ...userResponse } = user.toObject();
      res.status(201).json({ success: true, user: userResponse });
      
    } catch (error) {
      console.error('User creation error:', error);
      res.status(500).json({ error: 'Internal server error' });
    }
  }
);

// Test Executor generates security tests:
describe('POST /api/users', () => {
  test('should reject unauthenticated requests', async () => {
    const response = await request(app)
      .post('/api/users')
      .send({ name: 'Test', email: 'test@example.com', password: 'Password123' });
    
    expect(response.status).toBe(401);
  });
  
  test('should reject requests from non-admin users', async () => {
    const userToken = generateToken({ id: 'user1', role: 'user' });
    
    const response = await request(app)
      .post('/api/users')
      .set('Authorization', `Bearer ${userToken}`)
      .send({ name: 'Test', email: 'test@example.com', password: 'Password123' });
    
    expect(response.status).toBe(403);
  });
  
  test('should validate password strength', async () => {
    const adminToken = generateToken({ id: 'admin1', role: 'admin' });
    
    const response = await request(app)
      .post('/api/users')
      .set('Authorization', `Bearer ${adminToken}`)
      .send({ name: 'Test', email: 'test@example.com', password: 'weak' });
    
    expect(response.status).toBe(400);
    expect(response.body.error).toBe('Validation failed');
  });
  
  test('should not store plain text passwords', async () => {
    const adminToken = generateToken({ id: 'admin1', role: 'admin' });
    
    await request(app)
      .post('/api/users')
      .set('Authorization', `Bearer ${adminToken}`)
      .send({ 
        name: 'Test User', 
        email: 'test@example.com', 
        password: 'Password123' 
      });
    
    const user = await User.findOne({ email: 'test@example.com' });
    expect(user.password).not.toBe('Password123');
    expect(user.password).toMatch(/^\$2[aby]\$\d+\$/);
  });
});
```

## 🔄 Cross-Platform Examples

### Example 5: Flutter App with State Management

**Scenario**: Flutter e-learning app with complex state and navigation

```dart
// Original problematic code
class CourseListScreen extends StatefulWidget {
  @override
  _CourseListScreenState createState() => _CourseListScreenState();
}

class _CourseListScreenState extends State<CourseListScreen> {
  List<Course> courses = [];
  bool isLoading = false;
  
  @override
  void initState() {
    super.initState();
    loadCourses(); // Bug: Not handling errors
  }
  
  // Performance issue: Rebuilding entire list
  void loadCourses() async {
    setState(() => isLoading = true);
    
    try {
      final response = await http.get(Uri.parse('/api/courses'));
      final List<dynamic> data = json.decode(response.body);
      
      // Bug: Not checking response status
      courses = data.map((json) => Course.fromJson(json)).toList();
    } catch (e) {
      // Bug: Silent error handling
      print('Error: $e');
    }
    
    setState(() => isLoading = false);
  }
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: isLoading 
        ? CircularProgressIndicator()
        : ListView.builder(
            itemCount: courses.length,
            itemBuilder: (context, index) {
              // Performance issue: Creating new widgets on every build
              return CourseCard(course: courses[index]);
            },
          ),
    );
  }
}

// Bug Hunter detects error handling issues:
// 🐛 Bug Alert: Unhandled HTTP errors
// 🐛 Bug Alert: Silent exception handling
// Performance Analyzer suggests optimizations:
// ⚡ Performance Alert: Unnecessary widget rebuilds
// 💡 Optimized implementation:

class CourseListScreen extends StatefulWidget {
  @override
  _CourseListScreenState createState() => _CourseListScreenState();
}

class _CourseListScreenState extends State<CourseListScreen> {
  List<Course> courses = [];
  bool isLoading = false;
  String? errorMessage;
  
  @override
  void initState() {
    super.initState();
    _loadCourses();
  }
  
  Future<void> _loadCourses() async {
    setState(() {
      isLoading = true;
      errorMessage = null;
    });
    
    try {
      final response = await http.get(
        Uri.parse('/api/courses'),
        headers: {'Content-Type': 'application/json'},
      );
      
      // Fixed: Check response status
      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(response.body);
        setState(() {
          courses = data.map((json) => Course.fromJson(json)).toList();
        });
      } else {
        throw HttpException('Failed to load courses: ${response.statusCode}');
      }
    } catch (e) {
      // Fixed: Proper error handling
      setState(() {
        errorMessage = e.toString();
      });
      
      // Log error for debugging
      debugPrint('Error loading courses: $e');
    } finally {
      setState(() => isLoading = false);
    }
  }
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Courses'),
        actions: [
          IconButton(
            icon: Icon(Icons.refresh),
            onPressed: _loadCourses,
          ),
        ],
      ),
      body: _buildBody(),
    );
  }
  
  Widget _buildBody() {
    if (isLoading) {
      return Center(child: CircularProgressIndicator());
    }
    
    if (errorMessage != null) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.error, size: 64, color: Colors.red),
            SizedBox(height: 16),
            Text(errorMessage!, textAlign: TextAlign.center),
            SizedBox(height: 16),
            ElevatedButton(
              onPressed: _loadCourses,
              child: Text('Retry'),
            ),
          ],
        ),
      );
    }
    
    if (courses.isEmpty) {
      return Center(
        child: Text('No courses available'),
      );
    }
    
    // Optimized: Use ListView.separated for better performance
    return ListView.separated(
      padding: EdgeInsets.all(16),
      itemCount: courses.length,
      separatorBuilder: (context, index) => SizedBox(height: 8),
      itemBuilder: (context, index) {
        // Optimized: Use const constructor when possible
        return CourseCard(
          key: ValueKey(courses[index].id),
          course: courses[index],
        );
      },
    );
  }
}

// Test Executor generates comprehensive tests:
void main() {
  group('CourseListScreen', () {
    late MockHttpClient mockHttpClient;
    
    setUp(() {
      mockHttpClient = MockHttpClient();
    });
    
    testWidgets('should display loading indicator initially', (tester) async {
      await tester.pumpWidget(MaterialApp(home: CourseListScreen()));
      
      expect(find.byType(CircularProgressIndicator), findsOneWidget);
    });
    
    testWidgets('should display courses when loaded successfully', (tester) async {
      // Mock successful response
      when(mockHttpClient.get(any))
          .thenAnswer((_) async => http.Response(
              json.encode([{'id': '1', 'title': 'Test Course'}]), 
              200
          ));
      
      await tester.pumpWidget(MaterialApp(home: CourseListScreen()));
      await tester.pumpAndSettle();
      
      expect(find.text('Test Course'), findsOneWidget);
      expect(find.byType(CircularProgressIndicator), findsNothing);
    });
    
    testWidgets('should display error message on failure', (tester) async {
      // Mock error response
      when(mockHttpClient.get(any))
          .thenAnswer((_) async => http.Response('Error', 500));
      
      await tester.pumpWidget(MaterialApp(home: CourseListScreen()));
      await tester.pumpAndSettle();
      
      expect(find.byIcon(Icons.error), findsOneWidget);
      expect(find.text('Retry'), findsOneWidget);
    });
    
    testWidgets('should retry loading when retry button is pressed', (tester) async {
      // Mock initial error, then success
      when(mockHttpClient.get(any))
          .thenAnswer((_) async => http.Response('Error', 500))
          .thenAnswer((_) async => http.Response(
              json.encode([{'id': '1', 'title': 'Test Course'}]), 
              200
          ));
      
      await tester.pumpWidget(MaterialApp(home: CourseListScreen()));
      await tester.pumpAndSettle();
      
      // Tap retry button
      await tester.tap(find.text('Retry'));
      await tester.pumpAndSettle();
      
      expect(find.text('Test Course'), findsOneWidget);
      expect(find.byIcon(Icons.error), findsNothing);
    });
  });
}
```

## 📊 Performance Monitoring Examples

### Example 6: Real-time Performance Tracking

```typescript
// Performance Analyzer continuously monitors application metrics
class PerformanceMonitor {
  private metrics: PerformanceMetrics = {
    responseTime: [],
    memoryUsage: [],
    errorRate: 0,
    throughput: 0
  };
  
  // Automatic performance tracking
  @PerformanceTrack()
  async processUserRequest(request: UserRequest): Promise<Response> {
    const startTime = performance.now();
    
    try {
      const result = await this.handleRequest(request);
      
      // Performance Analyzer automatically logs metrics
      const endTime = performance.now();
      this.recordMetric('responseTime', endTime - startTime);
      
      return result;
    } catch (error) {
      // Bug Hunter automatically analyzes errors
      this.recordError(error);
      throw error;
    }
  }
  
  // Context Optimizer suggests improvements based on patterns
  private recordMetric(type: string, value: number) {
    this.metrics[type].push(value);
    
    // Trigger optimization suggestions if thresholds exceeded
    if (type === 'responseTime' && value > 1000) {
      this.suggestOptimization('Response time exceeded 1s', {
        currentValue: value,
        threshold: 1000,
        suggestions: [
          'Consider caching frequently accessed data',
          'Optimize database queries',
          'Implement request batching'
        ]
      });
    }
  }
}
```

## 🎯 Integration Patterns

### Example 7: Multi-Agent Collaboration

```typescript
// Real-world scenario: E-commerce checkout process
class CheckoutService {
  // Multiple Sub-Agents collaborate on this critical function
  async processCheckout(order: Order): Promise<CheckoutResult> {
    // Security Auditor: Validates payment data
    // Performance Analyzer: Monitors transaction time
    // Bug Hunter: Watches for edge cases
    // Test Executor: Ensures comprehensive testing
    
    try {
      // Step 1: Validate order (Security Auditor active)
      await this.validateOrder(order);
      
      // Step 2: Process payment (Performance Analyzer monitoring)
      const paymentResult = await this.processPayment(order.payment);
      
      // Step 3: Update inventory (Bug Hunter watching for race conditions)
      await this.updateInventory(order.items);
      
      // Step 4: Send confirmation (Context Optimizer suggests async)
      await this.sendConfirmation(order.customer.email);
      
      return { success: true, orderId: order.id };
      
    } catch (error) {
      // All Sub-Agents contribute to error analysis
      await this.handleCheckoutError(error, order);
      throw error;
    }
  }
}

// Sub-Agents automatically generate comprehensive test suite
describe('CheckoutService Integration', () => {
  // Security tests (Security Auditor)
  test('should reject invalid payment data', async () => {
    const invalidOrder = createOrderWithInvalidPayment();
    await expect(checkoutService.processCheckout(invalidOrder))
      .rejects.toThrow('Invalid payment data');
  });
  
  // Performance tests (Performance Analyzer)
  test('should complete checkout within 3 seconds', async () => {
    const order = createValidOrder();
    const startTime = Date.now();
    
    await checkoutService.processCheckout(order);
    
    const duration = Date.now() - startTime;
    expect(duration).toBeLessThan(3000);
  });
  
  // Concurrency tests (Bug Hunter)
  test('should handle concurrent checkouts correctly', async () => {
    const orders = Array(10).fill(null).map(() => createValidOrder());
    
    const results = await Promise.allSettled(
      orders.map(order => checkoutService.processCheckout(order))
    );
    
    const successful = results.filter(r => r.status === 'fulfilled');
    expect(successful).toHaveLength(10);
  });
  
  // Edge case tests (Context Optimizer)
  test('should handle inventory shortage gracefully', async () => {
    const order = createOrderWithUnavailableItems();
    
    await expect(checkoutService.processCheckout(order))
      .rejects.toThrow('Insufficient inventory');
    
    // Verify no partial updates occurred
    const inventory = await getInventoryState();
    expect(inventory).toEqual(initialInventoryState);
  });
});
```

---

**🎯 Key Takeaways**:

1. **Automatic Detection**: Sub-Agents work continuously in the background
2. **Collaborative Analysis**: Multiple agents contribute to complex problems
3. **Proactive Suggestions**: Issues are caught before they become problems
4. **Comprehensive Testing**: All aspects are automatically tested
5. **Real-world Impact**: Significant improvements in code quality, security, and performance

**📚 Next Steps**: 
- Explore [Sub-Agent System Guide](sub-agent-system-guide.md) for detailed configuration
- Review [Agent Coordination System](../workflows/coordination/agent-coordination-system.md) for advanced orchestration
- Check [Performance Optimization Patterns](../rules/patterns/performance-optimization-patterns.md) for optimization strategies