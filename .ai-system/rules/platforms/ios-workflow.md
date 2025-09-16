# 🍎 iOS Development Workflow - Universal Standards

> **⚡ Comprehensive iOS development guidelines with SwiftUI-first approach**  
> MVVM + Clean Architecture with SwiftUI for modern iOS apps

---

## 🎯 Core Architecture Principles

### Mandatory Architecture: MVVM + Clean Architecture
- **UI Layer**: SwiftUI + ViewModels (ObservableObject)
- **Domain Layer**: Use Cases + Models + Repository Protocols
- **Data Layer**: Repository Implementations + Data Sources
- **Unidirectional Data Flow**: View → ViewModel → UseCase → Repository

### Tech Stack Standards
```swift
// Package.swift dependencies
dependencies: [
    // Networking
    .package(url: "https://github.com/Alamofire/Alamofire.git", from: "5.8.0"),
    
    // JSON Parsing
    .package(url: "https://github.com/Flight-School/AnyCodable.git", from: "0.6.0"),
    
    // Dependency Injection
    .package(url: "https://github.com/Swinject/Swinject.git", from: "2.8.0"),
    
    // Async Image Loading
    .package(url: "https://github.com/kean/Nuke.git", from: "12.0.0"),
    
    // Core Data (if needed)
    // Built-in framework
    
    // Keychain
    .package(url: "https://github.com/evgenyneu/keychain-swift.git", from: "20.0.0"),
    
    // Testing
    .package(url: "https://github.com/Quick/Quick.git", from: "7.0.0"),
    .package(url: "https://github.com/Quick/Nimble.git", from: "12.0.0")
]

// Alternative: CocoaPods
pod 'Alamofire', '~> 5.8'
pod 'SwiftyJSON', '~> 5.0'
pod 'Swinject', '~> 2.8'
pod 'Nuke', '~> 12.0'
pod 'KeychainSwift', '~> 20.0'

# Testing
pod 'Quick', '~> 7.0'
pod 'Nimble', '~> 12.0'
```

---

## 📁 Standard Project Structure

```
MyApp/
├── App/                     # App configuration and entry point
│   ├── MyApp.swift         # App entry point
│   ├── ContentView.swift   # Root view
│   └── AppDelegate.swift   # App delegate (if needed)
├── Core/                   # Core modules and utilities
│   ├── DI/                 # Dependency Injection
│   │   ├── Container.swift # DI Container setup
│   │   └── Assemblies/     # Feature assemblies
│   ├── Network/            # Network layer
│   │   ├── NetworkManager.swift
│   │   ├── APIEndpoint.swift
│   │   └── NetworkError.swift
│   ├── Storage/            # Local storage
│   │   ├── CoreDataStack.swift
│   │   ├── UserDefaults+Extensions.swift
│   │   └── KeychainManager.swift
│   ├── Analytics/          # Analytics tracking
│   │   └── AnalyticsManager.swift
│   ├── Navigation/         # Navigation coordination
│   │   └── Coordinator.swift
│   └── Utils/              # Utility classes and extensions
│       ├── Extensions/     # Swift extensions
│       ├── Constants.swift # App constants
│       └── Helpers/        # Helper classes
├── Data/                   # Data layer implementation
│   ├── Repository/         # Repository implementations
│   │   ├── UserRepository.swift
│   │   └── ProductRepository.swift
│   ├── DataSource/         # Data sources
│   │   ├── Remote/         # API services and DTOs
│   │   │   ├── UserAPIService.swift
│   │   │   └── DTOs/       # Data Transfer Objects
│   │   └── Local/          # Core Data entities and managers
│   │       ├── Entities/   # Core Data entities
│   │       └── Managers/   # Local data managers
│   ├── Models/             # Data models
│   │   ├── User.swift
│   │   └── Product.swift
│   └── Mappers/            # Data mappers between layers
│       ├── UserMapper.swift
│       └── ProductMapper.swift
├── Domain/                 # Domain layer (business logic)
│   ├── UseCases/           # Use cases (business logic)
│   │   ├── User/           # User-related use cases
│   │   │   ├── GetUserUseCase.swift
│   │   │   └── UpdateUserUseCase.swift
│   │   └── Product/        # Product-related use cases
│   ├── Models/             # Domain models
│   │   ├── User.swift
│   │   └── Product.swift
│   └── Repositories/       # Repository protocols
│       ├── UserRepositoryProtocol.swift
│       └── ProductRepositoryProtocol.swift
└── Presentation/           # UI layer (presentation)
    ├── Components/         # Shared UI components
    │   ├── Buttons/        # Custom buttons
    │   │   ├── PrimaryButton.swift
    │   │   └── SecondaryButton.swift
    │   ├── TextFields/     # Input components
    │   │   └── CustomTextField.swift
    │   ├── Layout/         # Layout components
    │   │   └── CardView.swift
    │   └── Common/         # Common UI elements
    │       ├── LoadingView.swift
    │       └── ErrorView.swift
    ├── Theme/              # App theme and styling
    │   ├── Colors.swift    # Color definitions
    │   ├── Typography.swift # Typography definitions
    │   ├── Spacing.swift   # Spacing definitions
    │   └── Theme.swift     # Main theme
    └── Features/           # Feature-specific UI
        ├── Authentication/ # Authentication feature
        │   ├── Login/      # Login screen
        │   │   ├── LoginView.swift
        │   │   └── LoginViewModel.swift
        │   ├── Register/   # Registration screen
        │   │   ├── RegisterView.swift
        │   │   └── RegisterViewModel.swift
        │   └── ForgotPassword/
        ├── Home/           # Home feature
        │   ├── HomeView.swift
        │   ├── HomeViewModel.swift
        │   └── Components/
        └── Profile/        # Profile feature
            ├── ProfileView.swift
            ├── ProfileViewModel.swift
            └── Components/
```

---

## 🔄 Blueprint-First Development Process

### Step 1: Create Feature Blueprint
**MANDATORY**: Create blueprint before writing any code

#### Feature Blueprint Template
```markdown
# FEATURE BLUEPRINT: [Feature Name]

## DESCRIPTION
[Detailed feature description and user stories]

## DOMAIN LAYER
### Models
- **[Model1]**: [Description, properties]
  ```swift
  struct User {
      let id: String
      let name: String
      let email: String
  }
  ```

### Use Cases
- **[UseCase1]**: 
  - Input: [Parameters]
  - Output: [Return type]
  - Description: [Business logic description]
  ```swift
  protocol GetUserUseCaseProtocol {
      func execute(userId: String) async throws -> User
  }
  ```

### Repository Protocols
- **[Repository1]**: [Methods and descriptions]
  ```swift
  protocol UserRepositoryProtocol {
      func getUser(id: String) async throws -> User
      func updateUser(_ user: User) async throws -> User
  }
  ```

## DATA LAYER
### Repository Implementation
- **[Repository1Impl]**: [Implementation details]

### Data Sources
#### Remote Data Source
- **API Endpoints**:
  - `GET /api/users/{id}`: [Description, parameters]
  - `PUT /api/users/{id}`: [Description, request body]

#### Local Data Source
- **Core Data Entities**:
  - **UserEntity**: [Entity structure, relationships]

### Data Models
- **DTOs**: [API response models]
- **Entities**: [Core Data models]
- **Mappers**: [Conversion logic]

## PRESENTATION LAYER
### ViewModels
- **[ViewModel1]**: 
  - States: [UI states]
  - Actions: [User actions]
  - Published Properties: [Observable properties]
  ```swift
  @MainActor
  class UserViewModel: ObservableObject {
      @Published var user: User?
      @Published var isLoading = false
      @Published var errorMessage: String?
  }
  ```

### Views
- **[View1]**: 
  - Layout: [View layout description]
  - Components: [Used components]
  - Navigation: [Navigation flow]
  ```swift
  struct UserView: View {
      @StateObject private var viewModel = UserViewModel()
      
      var body: some View {
          // View implementation
      }
  }
  ```

### Components
- **[Component1]**: [Purpose, reusability, props]

## RESOURCES
### Localizable Strings
- `"feature.title"` = "[Title]";
- `"feature.description"` = "[Description]";
- `"error.message"` = "[Error message]";

### Colors
- `featurePrimary`: [Color value]
- `featureAccent`: [Color value]

### Images
- `ic_feature_icon`: [Icon description]
- `img_feature_placeholder`: [Image description]

## TESTING STRATEGY
### Unit Tests
- **ViewModels**: [Test scenarios]
- **Use Cases**: [Test scenarios]
- **Repositories**: [Test scenarios]

### UI Tests
- **Views**: [User interaction tests]
- **Navigation**: [Navigation flow tests]

## DEPENDENCIES
- **New Dependencies**: [List any new dependencies needed]
- **Existing Components**: [List reusable components]
```

### Step 2: Create Feature Structure
1. Create feature folder in `Presentation/Features/[FeatureName]/`
2. Create sub-folders:
   ```
   Presentation/Features/[Feature]/
   ├── Views/           # SwiftUI views
   ├── ViewModels/      # ViewModels
   ├── Components/      # Feature-specific components
   └── Navigation/      # Navigation coordination
   
   Domain/UseCases/[Feature]/  # Use cases
   Data/Repository/[Feature]/  # Repository implementation
   ```

### Step 3: Implementation Order
1. **Domain Layer First**:
   - Create domain models
   - Create repository protocols
   - Create use cases

2. **Data Layer Second**:
   - Create data models (DTOs, Core Data entities)
   - Create data sources (API, Core Data)
   - Create repository implementations
   - Create mappers

3. **Presentation Layer Last**:
   - Create ViewModels
   - Create UI components
   - Create views
   - Setup navigation

### Step 4: Update Registry
1. Update `blueprint/module-registry.md` with new components
2. Update `blueprint/component-catalog.md` with UI components
3. Update navigation coordinator
4. Update dependency injection container

---

## 🚫 Anti-Duplication Rules

### MANDATORY Pre-Creation Checks
Before creating any new class/function/resource:

1. **Search Project**: Use Xcode search for similar names/functionality
2. **Check Module Registry**: Review `blueprint/module-registry.md`
3. **Check Component Catalog**: Review `blueprint/component-catalog.md`
4. **Check Core Utils**: Look in `Core/Utils` for utility functions
5. **Check UI Components**: Look in `Presentation/Components` for reusable UI
6. **Prefer Extension**: Extend existing code rather than create new

### Duplication Detection Rules
When similar logic exists in 2+ places:

1. **Extract to Protocol**: Create protocol for common functionality
2. **Create Extension**: For common operations on types
3. **Update All Occurrences**: Refactor to use shared code
4. **Document in Registry**: Update component registry

### Similarity Handling
When code is similar but slightly different:

1. **Find Common Pattern**: Identify shared logic
2. **Parameterize Differences**: Make differences configurable
3. **Create Generic Version**: Build flexible, reusable solution
4. **Refactor Existing Code**: Update to use generic version

---

## 🏗️ Base Classes and Templates

### Base ViewModel
```swift
import Foundation
import Combine

@MainActor
class BaseViewModel: ObservableObject {
    @Published var isLoading = false
    @Published var errorMessage: String?
    
    protected var cancellables = Set<AnyCancellable>()
    
    func handleError(_ error: Error) {
        isLoading = false
        errorMessage = error.localizedDescription
        print("Error in \(String(describing: type(of: self))): \(error)")
    }
    
    func clearError() {
        errorMessage = nil
    }
    
    func setLoading(_ loading: Bool) {
        isLoading = loading
        if loading {
            clearError()
        }
    }
}

// Example implementation
@MainActor
class UserViewModel: BaseViewModel {
    @Published var user: User?
    
    private let getUserUseCase: GetUserUseCaseProtocol
    
    init(getUserUseCase: GetUserUseCaseProtocol) {
        self.getUserUseCase = getUserUseCase
        super.init()
    }
    
    func loadUser(id: String) {
        setLoading(true)
        
        Task {
            do {
                let user = try await getUserUseCase.execute(userId: id)
                await MainActor.run {
                    self.user = user
                    self.setLoading(false)
                }
            } catch {
                await MainActor.run {
                    self.handleError(error)
                }
            }
        }
    }
}
```

### Base Use Case
```swift
import Foundation

protocol BaseUseCaseProtocol {
    associatedtype Input
    associatedtype Output
    
    func execute(_ input: Input) async throws -> Output
}

// Example implementation
protocol GetUserUseCaseProtocol: BaseUseCaseProtocol where Input == String, Output == User {}

class GetUserUseCase: GetUserUseCaseProtocol {
    private let userRepository: UserRepositoryProtocol
    
    init(userRepository: UserRepositoryProtocol) {
        self.userRepository = userRepository
    }
    
    func execute(_ userId: String) async throws -> User {
        return try await userRepository.getUser(id: userId)
    }
}
```

### Base Repository
```swift
import Foundation
import Alamofire

class BaseRepository {
    protected let networkManager: NetworkManager
    
    init(networkManager: NetworkManager) {
        self.networkManager = networkManager
    }
    
    protected func safeAPICall<T: Codable>(
        endpoint: APIEndpoint,
        responseType: T.Type
    ) async throws -> T {
        do {
            return try await networkManager.request(endpoint: endpoint, responseType: responseType)
        } catch {
            print("API call failed in \(String(describing: type(of: self))): \(error)")
            throw error
        }
    }
    
    protected func safeCoreDataCall<T>(
        operation: () throws -> T
    ) throws -> T {
        do {
            return try operation()
        } catch {
            print("Core Data operation failed in \(String(describing: type(of: self))): \(error)")
            throw error
        }
    }
}
```

### Base UI Components
```swift
import SwiftUI

// Base Button
struct BaseButton: View {
    let title: String
    let action: () -> Void
    var style: ButtonStyle = .primary
    var isLoading: Bool = false
    var isEnabled: Bool = true
    
    var body: some View {
        Button(action: action) {
            HStack {
                if isLoading {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle(tint: .white))
                        .scaleEffect(0.8)
                } else {
                    Text(title)
                        .font(.headline)
                        .foregroundColor(style.textColor)
                }
            }
            .frame(maxWidth: .infinity)
            .frame(height: 50)
            .background(style.backgroundColor)
            .cornerRadius(12)
        }
        .disabled(!isEnabled || isLoading)
        .opacity(isEnabled ? 1.0 : 0.6)
    }
}

enum ButtonStyle {
    case primary
    case secondary
    case outline
    
    var backgroundColor: Color {
        switch self {
        case .primary:
            return .blue
        case .secondary:
            return .gray
        case .outline:
            return .clear
        }
    }
    
    var textColor: Color {
        switch self {
        case .primary, .secondary:
            return .white
        case .outline:
            return .blue
        }
    }
}

// Base Text Field
struct BaseTextField: View {
    let title: String
    @Binding var text: String
    var placeholder: String = ""
    var isSecure: Bool = false
    var keyboardType: UIKeyboardType = .default
    var errorMessage: String?
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(title)
                .font(.headline)
                .foregroundColor(.primary)
            
            Group {
                if isSecure {
                    SecureField(placeholder, text: $text)
                } else {
                    TextField(placeholder, text: $text)
                        .keyboardType(keyboardType)
                }
            }
            .textFieldStyle(RoundedBorderTextFieldStyle())
            .frame(height: 44)
            
            if let errorMessage = errorMessage {
                Text(errorMessage)
                    .font(.caption)
                    .foregroundColor(.red)
            }
        }
    }
}

// Base Card View
struct BaseCardView<Content: View>: View {
    let content: Content
    var padding: EdgeInsets = EdgeInsets(top: 16, leading: 16, bottom: 16, trailing: 16)
    var cornerRadius: CGFloat = 12
    var shadowRadius: CGFloat = 4
    
    init(padding: EdgeInsets = EdgeInsets(top: 16, leading: 16, bottom: 16, trailing: 16),
         cornerRadius: CGFloat = 12,
         shadowRadius: CGFloat = 4,
         @ViewBuilder content: () -> Content) {
        self.padding = padding
        self.cornerRadius = cornerRadius
        self.shadowRadius = shadowRadius
        self.content = content()
    }
    
    var body: some View {
        content
            .padding(padding)
            .background(Color(.systemBackground))
            .cornerRadius(cornerRadius)
            .shadow(radius: shadowRadius)
    }
}

// Loading View
struct LoadingView: View {
    var message: String = "Loading..."
    
    var body: some View {
        VStack(spacing: 16) {
            ProgressView()
                .progressViewStyle(CircularProgressViewStyle())
                .scaleEffect(1.5)
            
            Text(message)
                .font(.body)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(.systemBackground))
    }
}

// Error View
struct ErrorView: View {
    let message: String
    let retryAction: (() -> Void)?
    
    init(message: String, retryAction: (() -> Void)? = nil) {
        self.message = message
        self.retryAction = retryAction
    }
    
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "exclamationmark.triangle")
                .font(.system(size: 48))
                .foregroundColor(.red)
            
            Text("Error")
                .font(.title2)
                .fontWeight(.bold)
            
            Text(message)
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
            
            if let retryAction = retryAction {
                BaseButton(title: "Retry", action: retryAction)
                    .frame(maxWidth: 200)
            }
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(.systemBackground))
    }
}
```

---

## 🧪 Testing Standards

### Unit Test Template
```swift
import XCTest
import Quick
import Nimble
@testable import MyApp

class UserViewModelSpec: QuickSpec {
    override func spec() {
        describe("UserViewModel") {
            var viewModel: UserViewModel!
            var mockGetUserUseCase: MockGetUserUseCase!
            
            beforeEach {
                mockGetUserUseCase = MockGetUserUseCase()
                viewModel = UserViewModel(getUserUseCase: mockGetUserUseCase)
            }
            
            describe("loadUser") {
                context("when use case succeeds") {
                    it("should update user and stop loading") {
                        // Given
                        let expectedUser = User(id: "1", name: "John Doe", email: "john@example.com")
                        mockGetUserUseCase.result = .success(expectedUser)
                        
                        // When
                        viewModel.loadUser(id: "1")
                        
                        // Then
                        expect(viewModel.isLoading).toEventually(beFalse())
                        expect(viewModel.user).toEventually(equal(expectedUser))
                        expect(viewModel.errorMessage).toEventually(beNil())
                    }
                }
                
                context("when use case fails") {
                    it("should set error message and stop loading") {
                        // Given
                        let error = NSError(domain: "TestError", code: 1, userInfo: [NSLocalizedDescriptionKey: "Network error"])
                        mockGetUserUseCase.result = .failure(error)
                        
                        // When
                        viewModel.loadUser(id: "1")
                        
                        // Then
                        expect(viewModel.isLoading).toEventually(beFalse())
                        expect(viewModel.user).toEventually(beNil())
                        expect(viewModel.errorMessage).toEventually(equal("Network error"))
                    }
                }
            }
        }
    }
}

// Mock Use Case
class MockGetUserUseCase: GetUserUseCaseProtocol {
    var result: Result<User, Error>!
    
    func execute(_ userId: String) async throws -> User {
        switch result! {
        case .success(let user):
            return user
        case .failure(let error):
            throw error
        }
    }
}
```

### UI Test Template
```swift
import XCTest
@testable import MyApp

class UserViewUITests: XCTestCase {
    var app: XCUIApplication!
    
    override func setUpWithError() throws {
        continueAfterFailure = false
        app = XCUIApplication()
        app.launch()
    }
    
    func testUserViewDisplaysUserInfo() throws {
        // Given
        let userNameText = app.staticTexts["John Doe"]
        let userEmailText = app.staticTexts["john@example.com"]
        
        // When
        app.buttons["Load User"].tap()
        
        // Then
        XCTAssertTrue(userNameText.waitForExistence(timeout: 5))
        XCTAssertTrue(userEmailText.exists)
    }
    
    func testUserViewDisplaysLoadingState() throws {
        // Given
        let loadingIndicator = app.activityIndicators["Loading"]
        
        // When
        app.buttons["Load User"].tap()
        
        // Then
        XCTAssertTrue(loadingIndicator.waitForExistence(timeout: 2))
    }
    
    func testUserViewDisplaysErrorState() throws {
        // Given
        app.buttons["Simulate Error"].tap()
        let errorMessage = app.staticTexts["Network error"]
        
        // When
        app.buttons["Load User"].tap()
        
        // Then
        XCTAssertTrue(errorMessage.waitForExistence(timeout: 5))
    }
}
```

---

## 🚀 Performance Optimization

### Build Configuration
```swift
// Build Settings
// SWIFT_COMPILATION_MODE = wholemodule (Release)
// SWIFT_OPTIMIZATION_LEVEL = -O (Release)
// GCC_OPTIMIZATION_LEVEL = s (Release)
// ENABLE_BITCODE = YES
// STRIP_INSTALLED_PRODUCT = YES (Release)

// Info.plist optimizations
<key>UIApplicationSupportsIndirectInputEvents</key>
<true/>
<key>UILaunchStoryboardName</key>
<string>LaunchScreen</string>
<key>UISupportedInterfaceOrientations</key>
<array>
    <string>UIInterfaceOrientationPortrait</string>
</array>
```

### Code Optimization
```swift
// Use @MainActor for ViewModels
@MainActor
class UserViewModel: ObservableObject {
    // ViewModel implementation
}

// Use lazy loading for expensive operations
class DataManager {
    lazy var expensiveResource: ExpensiveResource = {
        return ExpensiveResource()
    }()
}

// Use weak references to avoid retain cycles
class NetworkManager {
    weak var delegate: NetworkManagerDelegate?
}

// Use value types (structs) when possible
struct User {
    let id: String
    let name: String
    let email: String
}

// Use @Published sparingly and combine updates
@MainActor
class ViewModel: ObservableObject {
    @Published var state: ViewState = .loading
    
    // Instead of multiple @Published properties
    // @Published var isLoading = false
    // @Published var user: User?
    // @Published var errorMessage: String?
}

enum ViewState {
    case loading
    case loaded(User)
    case error(String)
}
```

### Image Optimization
- **MANDATORY**: Use SF Symbols for icons when possible
- **MANDATORY**: Compress images before adding to project
- **MANDATORY**: Use appropriate image formats (HEIF for photos, PNG for graphics)
- **RECOMMENDED**: Use Nuke for efficient image loading and caching

---

## 📊 Analytics and Monitoring

### Firebase Integration
```swift
// AppDelegate.swift or App.swift
import FirebaseCore
import FirebaseAnalytics
import FirebaseCrashlytics

@main
struct MyApp: App {
    init() {
        FirebaseApp.configure()
        
        #if DEBUG
        Analytics.setAnalyticsCollectionEnabled(false)
        Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(false)
        #endif
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

// Analytics Manager
class AnalyticsManager {
    static let shared = AnalyticsManager()
    
    private init() {}
    
    func logEvent(_ name: String, parameters: [String: Any]? = nil) {
        #if !DEBUG
        Analytics.logEvent(name, parameters: parameters)
        #endif
    }
    
    func logScreenView(_ screenName: String, screenClass: String? = nil) {
        logEvent(AnalyticsEventScreenView, parameters: [
            AnalyticsParameterScreenName: screenName,
            AnalyticsParameterScreenClass: screenClass ?? screenName
        ])
    }
    
    func setUserProperty(_ value: String?, forName name: String) {
        #if !DEBUG
        Analytics.setUserProperty(value, forName: name)
        #endif
    }
}

// Usage in ViewModels
@MainActor
class UserViewModel: BaseViewModel {
    override init() {
        super.init()
        AnalyticsManager.shared.logScreenView("UserScreen")
    }
    
    func loadUser(id: String) {
        AnalyticsManager.shared.logEvent("user_load_started", parameters: [
            "user_id": id
        ])
        
        // Implementation...
    }
}
```

---

## ✅ Quality Checklist

### Architecture Compliance
- [ ] MVVM + Clean Architecture implemented correctly
- [ ] SwiftUI used for all new UI components
- [ ] Unidirectional Data Flow maintained
- [ ] Proper separation of concerns across layers
- [ ] Dependency injection setup correctly

### Code Quality
- [ ] Blueprint created before implementation
- [ ] Project structure follows standards
- [ ] No code duplication detected
- [ ] Base classes/protocols used appropriately
- [ ] UI components reused when possible
- [ ] Error handling implemented comprehensively
- [ ] @MainActor used correctly for UI updates

### Testing
- [ ] Unit tests cover ViewModels and Use Cases (>80% coverage)
- [ ] UI tests cover critical user flows
- [ ] Repository tests include error scenarios
- [ ] Integration tests validate data flow
- [ ] Quick/Nimble used for readable tests

### Performance
- [ ] Build settings optimized for release
- [ ] Images optimized and compressed
- [ ] SF Symbols used where appropriate
- [ ] Lazy loading implemented for expensive operations
- [ ] Memory leaks checked and resolved
- [ ] App launch time optimized

### Documentation
- [ ] Feature blueprint completed
- [ ] Module registry updated
- [ ] Component catalog updated
- [ ] API documentation current
- [ ] README files updated

### Security
- [ ] Input validation implemented
- [ ] Keychain used for sensitive data
- [ ] Network security configured (App Transport Security)
- [ ] Code obfuscation applied for sensitive logic
- [ ] No hardcoded secrets in code

### App Store Compliance
- [ ] Privacy policy implemented
- [ ] App Store guidelines followed
- [ ] Required permissions properly requested
- [ ] Accessibility features implemented
- [ ] Localization setup (if required)

---

**🍎 Modern iOS development with SwiftUI, Clean Architecture, and comprehensive quality standards for scalable, maintainable applications.**