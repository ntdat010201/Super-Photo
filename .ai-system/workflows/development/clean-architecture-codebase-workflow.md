# Clean Architecture Code Base Creation Workflow

> **🏗️ CRITICAL STAGE: Structure-Only Implementation**  
> This workflow ensures AI creates ONLY architectural structure without detailed implementation

## 🔴 MANDATORY RULES - NO EXCEPTIONS

### Core Principle
**"Structure First, Implementation Later"**

- This stage creates the **skeleton** of Clean Architecture
- **ZERO business logic** allowed
- **ZERO detailed implementation** allowed
- **ZERO real content** in UI components

## Workflow Steps

### Step 1: Architecture Layer Setup

**Create folder structure:**
```
/src
  /domain
    /entities
    /repositories
    /usecases
  /data
    /repositories
    /datasources
    /models
  /presentation
    /screens
    /viewmodels
    /widgets
  /core
    /di
    /constants
    /utils
```

### Step 2: Domain Layer (Structure Only)

**Entities:**
```dart
// ✅ ALLOWED: Structure with properties
class User {
  final String id;
  final String name;
  final String email;
  
  User({required this.id, required this.name, required this.email});
  
  // ❌ NO business logic methods here
  // TODO: Add business logic methods in implementation phase
}
```

**Repository Interfaces:**
```dart
// ✅ ALLOWED: Interface with method signatures
abstract class UserRepository {
  Future<User?> getUserById(String id);
  Future<List<User>> getAllUsers();
  Future<void> saveUser(User user);
  
  // ❌ NO implementation here
}
```

**Use Cases:**
```dart
// ✅ ALLOWED: Empty use case structure
class GetUserUseCase {
  final UserRepository repository;
  
  GetUserUseCase(this.repository);
  
  Future<User?> execute(String userId) {
    // TODO: Implement use case logic
    throw UnimplementedError('To be implemented in detailed phase');
  }
}
```

### Step 3: Data Layer (Structure Only)

**Repository Implementation:**
```dart
// ✅ ALLOWED: Empty implementation
class UserRepositoryImpl implements UserRepository {
  final UserDataSource dataSource;
  
  UserRepositoryImpl(this.dataSource);
  
  @override
  Future<User?> getUserById(String id) {
    // TODO: Implement data fetching logic
    throw UnimplementedError('To be implemented');
  }
  
  // ... other empty methods
}
```

**Data Sources:**
```dart
// ✅ ALLOWED: Interface only
abstract class UserDataSource {
  Future<Map<String, dynamic>?> fetchUser(String id);
  Future<List<Map<String, dynamic>>> fetchAllUsers();
}
```

### Step 4: Presentation Layer (Structure Only)

**ViewModels/BLoCs:**
```dart
// ✅ ALLOWED: Empty state management
class UserViewModel extends ChangeNotifier {
  final GetUserUseCase getUserUseCase;
  
  UserViewModel(this.getUserUseCase);
  
  User? _user;
  User? get user => _user;
  
  void loadUser(String id) {
    // TODO: Implement user loading logic
    throw UnimplementedError('To be implemented');
  }
}
```

**Screens:**
```dart
// ✅ ALLOWED: Empty screen structure
class UserScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('User Screen')),
      body: Center(
        child: Text('TODO: Implement user interface'),
      ),
    );
  }
}
```

### Step 5: Dependency Injection Setup

```dart
// ✅ ALLOWED: DI container structure
class DIContainer {
  static void setup() {
    // TODO: Register dependencies
    // GetIt.instance.registerLazySingleton<UserRepository>(() => UserRepositoryImpl());
  }
}
```

## 🚫 VIOLATION EXAMPLES - NEVER DO THIS

### ❌ Business Logic Implementation
```dart
// ❌ WRONG: Implementing actual business logic
class GetUserUseCase {
  Future<User?> execute(String userId) {
    if (userId.isEmpty) {
      throw ArgumentError('User ID cannot be empty');
    }
    
    final user = await repository.getUserById(userId);
    if (user != null && user.isActive) {
      return user;
    }
    return null;
  }
}
```

### ❌ API Implementation
```dart
// ❌ WRONG: Implementing actual API calls
class UserApiDataSource implements UserDataSource {
  @override
  Future<Map<String, dynamic>?> fetchUser(String id) async {
    final response = await http.get(Uri.parse('$baseUrl/users/$id'));
    if (response.statusCode == 200) {
      return json.decode(response.body);
    }
    return null;
  }
}
```

### ❌ UI Implementation
```dart
// ❌ WRONG: Creating detailed UI
class UserScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Consumer<UserViewModel>(
        builder: (context, viewModel, child) {
          if (viewModel.isLoading) {
            return CircularProgressIndicator();
          }
          
          return ListView.builder(
            itemCount: viewModel.users.length,
            itemBuilder: (context, index) {
              final user = viewModel.users[index];
              return ListTile(
                title: Text(user.name),
                subtitle: Text(user.email),
              );
            },
          );
        },
      ),
    );
  }
}
```

## Validation Checklist

### Pre-Implementation Check
```markdown
☐ All classes exist with proper structure
☐ All interfaces are defined with method signatures
☐ All method bodies are empty or throw UnimplementedError
☐ No business logic implementation
☐ No API calls or database operations
☐ No detailed UI components
☐ Dependency injection structure is set up (empty)
☐ Navigation routes are defined (empty destinations)
☐ Constants and configuration files are created
☐ Model classes have properties only (no methods)
```

### Code Review Questions
1. **"Does this method do anything other than throw UnimplementedError?"** → Should be NO
2. **"Is there any business logic in this class?"** → Should be NO
3. **"Does this UI component show real data?"** → Should be NO
4. **"Are there any API calls or database operations?"** → Should be NO
5. **"Is this just architectural structure?"** → Should be YES

## Stage Completion Criteria

### Ready for Detailed Implementation When:
- ✅ Complete folder structure exists
- ✅ All interfaces are defined
- ✅ All classes exist with empty methods
- ✅ Dependency injection container is structured
- ✅ Navigation is set up with empty screens
- ✅ Models are defined with properties only
- ✅ No implementation details exist anywhere

### User Confirmation Required
**Question:** "Xác nhận code base đã hoàn thành và chỉ chứa structure, không có implementation chi tiết?"

**Expected Answer:** "Có, code base chỉ có structure"

**Only then proceed to detailed implementation phase.**

## Error Recovery

If AI violates rules and implements details:

1. **STOP immediately**
2. **Remove detailed implementation**
3. **Replace with TODO comments**
4. **Warn user about violation**
5. **Restart structure creation**

## Platform-Specific Adaptations

### Flutter/Dart
- Use `UnimplementedError()` for empty methods
- Create abstract classes for repositories
- Use proper folder structure under `/lib`

### Android/Kotlin
- Use `TODO()` for empty methods
- Create interfaces for repositories
- Follow Android package structure

### iOS/Swift
- Use `fatalError("Not implemented")` for empty methods
- Create protocols for repositories
- Follow iOS project structure

### React/TypeScript
- Use `throw new Error('Not implemented')` for empty methods
- Create interfaces for contracts
- Follow React project structure

---

**Remember: This stage is about creating the skeleton, not the flesh. Implementation comes later!**