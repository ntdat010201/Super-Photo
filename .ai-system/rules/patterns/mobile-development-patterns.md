# Mobile Development Patterns

## 📱 Mobile-Specific Pattern Library

**Purpose**: Specialized patterns for mobile application development  
**Platforms**: iOS (Swift/SwiftUI), Android (Kotlin/Jetpack Compose), Cross-platform (Flutter/React Native)  
**Integration**: Deep Sub-Agent integration for mobile-specific optimization  
**Focus**: Performance, battery efficiency, user experience, platform guidelines

## 🏗️ Architecture Patterns for Mobile

### 1. MVVM (Model-View-ViewModel) Pattern

**Description**: Separates UI logic from business logic with data binding  
**Best For**: iOS SwiftUI, Android Jetpack Compose, Flutter  
**Sub-Agent Integration**: Context Optimizer for binding optimization, Performance Analyzer for UI performance

#### iOS SwiftUI Implementation

```swift
// MVVM Pattern for iOS SwiftUI
import SwiftUI
import Combine

// Model
struct User: Codable, Identifiable {
    let id: UUID
    var name: String
    var email: String
    var profileImageURL: String?
    
    init(name: String, email: String) {
        self.id = UUID()
        self.name = name
        self.email = email
    }
}

// ViewModel with Sub-Agent Integration
@MainActor
class UserProfileViewModel: ObservableObject {
    @Published var user: User?
    @Published var isLoading = false
    @Published var errorMessage: String?
    
    private let userService: UserService
    private let contextOptimizer: ContextOptimizerAgent?
    private let performanceAnalyzer: PerformanceAnalyzerAgent?
    private var cancellables = Set<AnyCancellable>()
    
    init(
        userService: UserService,
        subAgents: SubAgentContainer? = nil
    ) {
        self.userService = userService
        self.contextOptimizer = subAgents?.contextOptimizer
        self.performanceAnalyzer = subAgents?.performanceAnalyzer
    }
    
    func loadUser(id: UUID) async {
        let startTime = CFAbsoluteTimeGetCurrent()
        
        isLoading = true
        errorMessage = nil
        
        do {
            // Optimize loading strategy with Context Optimizer
            let loadingStrategy = await contextOptimizer?.optimizeUserLoading(
                userId: id,
                context: .profileView,
                options: [
                    .cacheFirst,
                    .preloadRelatedData,
                    .optimizeForBattery
                ]
            ) ?? .default
            
            user = try await userService.fetchUser(id: id, strategy: loadingStrategy)
            
            // Performance monitoring
            let executionTime = CFAbsoluteTimeGetCurrent() - startTime
            await performanceAnalyzer?.analyzeUserLoadPerformance(
                executionTime: executionTime,
                strategy: loadingStrategy,
                cacheHit: loadingStrategy.usedCache
            )
            
        } catch {
            errorMessage = error.localizedDescription
            
            // Error analysis for mobile-specific issues
            await performanceAnalyzer?.analyzeMobileError(
                error: error,
                context: .userLoading,
                networkCondition: await NetworkMonitor.shared.currentCondition
            )
        }
        
        isLoading = false
    }
    
    func updateUser(_ updatedUser: User) async {
        do {
            // Optimistic update with rollback capability
            let previousUser = user
            user = updatedUser
            
            try await userService.updateUser(updatedUser)
            
            // Success - no rollback needed
            await contextOptimizer?.recordSuccessfulUpdate(
                userId: updatedUser.id,
                updateType: .profile
            )
            
        } catch {
            // Rollback on failure
            user = previousUser
            errorMessage = "Failed to update user: \(error.localizedDescription)"
        }
    }
}

// View with Sub-Agent Enhanced UI
struct UserProfileView: View {
    @StateObject private var viewModel: UserProfileViewModel
    @State private var showingEditSheet = false
    
    init(userId: UUID, subAgents: SubAgentContainer? = nil) {
        _viewModel = StateObject(wrapping: UserProfileViewModel(
            userService: UserService.shared,
            subAgents: subAgents
        ))
    }
    
    var body: some View {
        NavigationView {
            Group {
                if viewModel.isLoading {
                    ProgressView("Loading user...")
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else if let user = viewModel.user {
                    userProfileContent(user: user)
                } else {
                    errorView
                }
            }
            .navigationTitle("Profile")
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("Edit") {
                        showingEditSheet = true
                    }
                    .disabled(viewModel.user == nil)
                }
            }
        }
        .sheet(isPresented: $showingEditSheet) {
            if let user = viewModel.user {
                UserEditView(user: user) { updatedUser in
                    Task {
                        await viewModel.updateUser(updatedUser)
                    }
                }
            }
        }
        .task {
            // Load user when view appears
            // await viewModel.loadUser(id: userId)
        }
    }
    
    @ViewBuilder
    private func userProfileContent(user: User) -> some View {
        ScrollView {
            VStack(spacing: 20) {
                // Profile Image
                AsyncImage(url: URL(string: user.profileImageURL ?? "")) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } placeholder: {
                    Circle()
                        .fill(Color.gray.opacity(0.3))
                        .overlay(
                            Image(systemName: "person.fill")
                                .foregroundColor(.gray)
                        )
                }
                .frame(width: 120, height: 120)
                .clipShape(Circle())
                
                // User Info
                VStack(spacing: 8) {
                    Text(user.name)
                        .font(.title2)
                        .fontWeight(.semibold)
                    
                    Text(user.email)
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                }
                
                Spacer()
            }
            .padding()
        }
    }
    
    @ViewBuilder
    private var errorView: some View {
        VStack(spacing: 16) {
            Image(systemName: "exclamationmark.triangle")
                .font(.system(size: 50))
                .foregroundColor(.orange)
            
            Text("Unable to load user")
                .font(.headline)
            
            if let errorMessage = viewModel.errorMessage {
                Text(errorMessage)
                    .font(.caption)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
            }
            
            Button("Retry") {
                Task {
                    // await viewModel.loadUser(id: userId)
                }
            }
            .buttonStyle(.borderedProminent)
        }
        .padding()
    }
}
```

#### Android Jetpack Compose Implementation

```kotlin
// MVVM Pattern for Android Jetpack Compose
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Model
data class User(
    val id: String,
    val name: String,
    val email: String,
    val profileImageUrl: String? = null
)

// ViewModel with Sub-Agent Integration
class UserProfileViewModel(
    private val userRepository: UserRepository,
    private val subAgentContainer: SubAgentContainer? = null
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()
    
    private val contextOptimizer = subAgentContainer?.contextOptimizer
    private val performanceAnalyzer = subAgentContainer?.performanceAnalyzer
    
    fun loadUser(userId: String) {
        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // Optimize loading with Context Optimizer
                val loadingStrategy = contextOptimizer?.optimizeUserLoading(
                    userId = userId,
                    context = LoadingContext.PROFILE_VIEW,
                    options = setOf(
                        LoadingOption.CACHE_FIRST,
                        LoadingOption.PRELOAD_RELATED_DATA,
                        LoadingOption.OPTIMIZE_FOR_BATTERY
                    )
                ) ?: LoadingStrategy.DEFAULT
                
                val user = userRepository.getUser(userId, loadingStrategy)
                
                _uiState.value = _uiState.value.copy(
                    user = user,
                    isLoading = false
                )
                
                // Performance monitoring
                val executionTime = System.currentTimeMillis() - startTime
                performanceAnalyzer?.analyzeUserLoadPerformance(
                    executionTime = executionTime,
                    strategy = loadingStrategy,
                    cacheHit = loadingStrategy.usedCache
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
                
                // Error analysis for mobile-specific issues
                performanceAnalyzer?.analyzeMobileError(
                    error = e,
                    context = ErrorContext.USER_LOADING,
                    networkCondition = NetworkMonitor.getCurrentCondition()
                )
            }
        }
    }
    
    fun updateUser(updatedUser: User) {
        viewModelScope.launch {
            try {
                // Optimistic update with rollback capability
                val previousUser = _uiState.value.user
                _uiState.value = _uiState.value.copy(user = updatedUser)
                
                userRepository.updateUser(updatedUser)
                
                // Success - record for optimization
                contextOptimizer?.recordSuccessfulUpdate(
                    userId = updatedUser.id,
                    updateType = UpdateType.PROFILE
                )
                
            } catch (e: Exception) {
                // Rollback on failure
                _uiState.value = _uiState.value.copy(
                    user = previousUser,
                    error = "Failed to update user: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

// UI State
data class UserProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

// Composable View
@Composable
fun UserProfileScreen(
    userId: String,
    viewModel: UserProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            uiState.isLoading -> {
                LoadingContent()
            }
            uiState.error != null -> {
                ErrorContent(
                    error = uiState.error,
                    onRetry = { viewModel.loadUser(userId) },
                    onDismiss = { viewModel.clearError() }
                )
            }
            uiState.user != null -> {
                UserProfileContent(
                    user = uiState.user,
                    onUpdateUser = { updatedUser ->
                        viewModel.updateUser(updatedUser)
                    }
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading user...")
        }
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Unable to load user",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row {
                Button(
                    onClick = onRetry,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Retry")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Dismiss")
                }
            }
        }
    }
}

@Composable
private fun UserProfileContent(
    user: User,
    onUpdateUser: (User) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        AsyncImage(
            model = user.profileImageUrl,
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            placeholder = painterResource(R.drawable.ic_person_placeholder),
            error = painterResource(R.drawable.ic_person_placeholder),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // User Info
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Edit Button
        Button(
            onClick = {
                // Navigate to edit screen or show edit dialog
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Profile")
        }
    }
}
```

### 2. Repository Pattern for Mobile

**Description**: Abstracts data sources with mobile-specific optimizations  
**Best For**: Offline-first apps, data synchronization, caching strategies  
**Sub-Agent Integration**: Context Optimizer for caching, Performance Analyzer for data access optimization

```kotlin
// Mobile Repository Pattern with Sub-Agent Integration
interface UserRepository {
    suspend fun getUser(id: String, strategy: LoadingStrategy = LoadingStrategy.DEFAULT): User
    suspend fun getUsers(query: UserQuery): List<User>
    suspend fun updateUser(user: User): User
    suspend fun deleteUser(id: String)
    suspend fun syncUsers(): SyncResult
}

class UserRepositoryImpl(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
    private val subAgentContainer: SubAgentContainer? = null
) : UserRepository {
    
    private val contextOptimizer = subAgentContainer?.contextOptimizer
    private val performanceAnalyzer = subAgentContainer?.performanceAnalyzer
    private val bugHunter = subAgentContainer?.bugHunter
    
    override suspend fun getUser(id: String, strategy: LoadingStrategy): User {
        val startTime = System.currentTimeMillis()
        
        try {
            // Optimize data access strategy with Context Optimizer
            val optimizedStrategy = contextOptimizer?.optimizeDataAccess(
                operation = DataOperation.READ,
                entityType = "User",
                identifier = id,
                currentStrategy = strategy,
                context = mapOf(
                    "networkCondition" to NetworkMonitor.getCurrentCondition(),
                    "batteryLevel" to BatteryMonitor.getCurrentLevel(),
                    "storageAvailable" to StorageMonitor.getAvailableSpace()
                )
            ) ?: strategy
            
            val user = when (optimizedStrategy.source) {
                DataSource.CACHE_FIRST -> {
                    getCacheFirstUser(id, optimizedStrategy)
                }
                DataSource.NETWORK_FIRST -> {
                    getNetworkFirstUser(id, optimizedStrategy)
                }
                DataSource.CACHE_ONLY -> {
                    localDataSource.getUser(id)
                        ?: throw UserNotFoundException("User not found in cache: $id")
                }
                DataSource.NETWORK_ONLY -> {
                    remoteDataSource.getUser(id).also { user ->
                        if (optimizedStrategy.shouldCache) {
                            localDataSource.saveUser(user)
                        }
                    }
                }
            }
            
            // Performance monitoring
            val executionTime = System.currentTimeMillis() - startTime
            performanceAnalyzer?.analyzeDataAccess(
                operation = "getUser",
                executionTime = executionTime,
                strategy = optimizedStrategy,
                resultSize = 1,
                cacheHit = optimizedStrategy.usedCache
            )
            
            return user
            
        } catch (e: Exception) {
            // Error analysis with Bug Hunter
            bugHunter?.analyzeRepositoryError(
                operation = "getUser",
                parameters = mapOf("id" to id, "strategy" to strategy.toString()),
                error = e,
                context = mapOf(
                    "networkAvailable" to NetworkMonitor.isNetworkAvailable(),
                    "cacheSize" to localDataSource.getCacheSize(),
                    "executionTime" to (System.currentTimeMillis() - startTime)
                )
            )
            
            throw e
        }
    }
    
    private suspend fun getCacheFirstUser(id: String, strategy: LoadingStrategy): User {
        // Try cache first
        val cachedUser = localDataSource.getUser(id)
        
        if (cachedUser != null && !strategy.shouldRefresh) {
            return cachedUser
        }
        
        // Fallback to network if cache miss or refresh needed
        return try {
            val networkUser = remoteDataSource.getUser(id)
            localDataSource.saveUser(networkUser)
            networkUser
        } catch (e: NetworkException) {
            // Return cached version if network fails
            cachedUser ?: throw e
        }
    }
    
    private suspend fun getNetworkFirstUser(id: String, strategy: LoadingStrategy): User {
        return try {
            val networkUser = remoteDataSource.getUser(id)
            if (strategy.shouldCache) {
                localDataSource.saveUser(networkUser)
            }
            networkUser
        } catch (e: NetworkException) {
            // Fallback to cache if network fails
            localDataSource.getUser(id) ?: throw e
        }
    }
    
    override suspend fun syncUsers(): SyncResult {
        val startTime = System.currentTimeMillis()
        
        try {
            // Optimize sync strategy with Context Optimizer
            val syncStrategy = contextOptimizer?.optimizeSyncStrategy(
                entityType = "User",
                context = mapOf(
                    "networkCondition" to NetworkMonitor.getCurrentCondition(),
                    "batteryLevel" to BatteryMonitor.getCurrentLevel(),
                    "isCharging" to BatteryMonitor.isCharging(),
                    "pendingChanges" to localDataSource.getPendingChangesCount()
                )
            ) ?: SyncStrategy.DEFAULT
            
            val result = when (syncStrategy.type) {
                SyncType.FULL -> performFullSync(syncStrategy)
                SyncType.INCREMENTAL -> performIncrementalSync(syncStrategy)
                SyncType.SELECTIVE -> performSelectiveSync(syncStrategy)
            }
            
            // Performance monitoring
            val executionTime = System.currentTimeMillis() - startTime
            performanceAnalyzer?.analyzeSyncPerformance(
                syncType = syncStrategy.type,
                executionTime = executionTime,
                itemsSynced = result.itemsSynced,
                dataTransferred = result.dataTransferred
            )
            
            return result
            
        } catch (e: Exception) {
            bugHunter?.analyzeSyncError(
                error = e,
                context = mapOf(
                    "networkCondition" to NetworkMonitor.getCurrentCondition(),
                    "executionTime" to (System.currentTimeMillis() - startTime)
                )
            )
            
            throw e
        }
    }
    
    private suspend fun performFullSync(strategy: SyncStrategy): SyncResult {
        // Implementation for full synchronization
        val remoteUsers = remoteDataSource.getAllUsers()
        val localUsers = localDataSource.getAllUsers()
        
        // Sync logic here
        return SyncResult(
            itemsSynced = remoteUsers.size,
            dataTransferred = remoteUsers.sumOf { it.toString().length },
            conflicts = emptyList()
        )
    }
    
    private suspend fun performIncrementalSync(strategy: SyncStrategy): SyncResult {
        // Implementation for incremental synchronization
        val lastSyncTime = localDataSource.getLastSyncTime()
        val changedUsers = remoteDataSource.getUsersChangedSince(lastSyncTime)
        
        // Sync logic here
        return SyncResult(
            itemsSynced = changedUsers.size,
            dataTransferred = changedUsers.sumOf { it.toString().length },
            conflicts = emptyList()
        )
    }
    
    private suspend fun performSelectiveSync(strategy: SyncStrategy): SyncResult {
        // Implementation for selective synchronization
        val priorityUsers = localDataSource.getPriorityUsers()
        
        // Sync only priority users
        return SyncResult(
            itemsSynced = priorityUsers.size,
            dataTransferred = priorityUsers.sumOf { it.toString().length },
            conflicts = emptyList()
        )
    }
}
```

## 🎨 UI Patterns for Mobile

### 1. Adaptive UI Pattern

**Description**: UI that adapts to different screen sizes and orientations  
**Best For**: Universal apps, tablet support, responsive design  
**Sub-Agent Integration**: Context Optimizer for layout optimization, Performance Analyzer for rendering performance

```swift
// iOS Adaptive UI Pattern
import SwiftUI

struct AdaptiveUserListView: View {
    @StateObject private var viewModel = UserListViewModel()
    @Environment(\.horizontalSizeClass) private var horizontalSizeClass
    @Environment(\.verticalSizeClass) private var verticalSizeClass
    
    private var isCompact: Bool {
        horizontalSizeClass == .compact
    }
    
    private var isRegular: Bool {
        horizontalSizeClass == .regular
    }
    
    var body: some View {
        GeometryReader { geometry in
            Group {
                if isCompact {
                    compactLayout(geometry: geometry)
                } else {
                    regularLayout(geometry: geometry)
                }
            }
        }
        .onAppear {
            Task {
                await viewModel.loadUsers()
            }
        }
    }
    
    @ViewBuilder
    private func compactLayout(geometry: GeometryProxy) -> some View {
        NavigationView {
            List(viewModel.users) { user in
                NavigationLink(destination: UserDetailView(user: user)) {
                    UserRowView(user: user, style: .compact)
                }
            }
            .navigationTitle("Users")
            .refreshable {
                await viewModel.refreshUsers()
            }
        }
    }
    
    @ViewBuilder
    private func regularLayout(geometry: GeometryProxy) -> some View {
        NavigationView {
            // Master view
            List(viewModel.users, selection: $viewModel.selectedUser) { user in
                UserRowView(user: user, style: .regular)
                    .tag(user)
            }
            .navigationTitle("Users")
            .frame(minWidth: 300, maxWidth: 400)
            
            // Detail view
            Group {
                if let selectedUser = viewModel.selectedUser {
                    UserDetailView(user: selectedUser)
                } else {
                    Text("Select a user")
                        .foregroundColor(.secondary)
                }
            }
            .frame(minWidth: 400)
        }
        .navigationViewStyle(DoubleColumnNavigationViewStyle())
    }
}

struct UserRowView: View {
    let user: User
    let style: RowStyle
    
    enum RowStyle {
        case compact
        case regular
    }
    
    var body: some View {
        HStack(spacing: 12) {
            AsyncImage(url: URL(string: user.profileImageURL ?? "")) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
            } placeholder: {
                Circle()
                    .fill(Color.gray.opacity(0.3))
                    .overlay(
                        Image(systemName: "person.fill")
                            .foregroundColor(.gray)
                    )
            }
            .frame(
                width: style == .compact ? 40 : 60,
                height: style == .compact ? 40 : 60
            )
            .clipShape(Circle())
            
            VStack(alignment: .leading, spacing: 4) {
                Text(user.name)
                    .font(style == .compact ? .body : .headline)
                    .fontWeight(.medium)
                
                Text(user.email)
                    .font(style == .compact ? .caption : .subheadline)
                    .foregroundColor(.secondary)
                
                if style == .regular {
                    Text("Last seen: \(user.lastSeenFormatted)")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
            }
            
            Spacer()
            
            if style == .regular {
                VStack {
                    Image(systemName: user.isOnline ? "circle.fill" : "circle")
                        .foregroundColor(user.isOnline ? .green : .gray)
                        .font(.caption)
                    
                    Text(user.isOnline ? "Online" : "Offline")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
            }
        }
        .padding(.vertical, style == .compact ? 4 : 8)
    }
}
```

### 2. Pull-to-Refresh Pattern

**Description**: Standard mobile gesture for refreshing content  
**Best For**: List views, feed updates, data synchronization  
**Sub-Agent Integration**: Performance Analyzer for refresh optimization, Context Optimizer for refresh strategy

```kotlin
// Android Pull-to-Refresh Pattern
@Composable
fun PullToRefreshUserList(
    viewModel: UserListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refreshUsers() }
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = uiState.users,
                key = { user -> user.id }
            ) { user ->
                UserCard(
                    user = user,
                    onClick = { viewModel.selectUser(user) }
                )
            }
            
            if (uiState.isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        
        PullRefreshIndicator(
            refreshing = uiState.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        
        // Error handling
        uiState.error?.let { error ->
            ErrorSnackbar(
                error = error,
                onDismiss = { viewModel.clearError() },
                onRetry = { viewModel.retryLastOperation() }
            )
        }
    }
}

@Composable
fun UserCard(
    user: User,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.profileImageUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                placeholder = painterResource(R.drawable.ic_person_placeholder),
                error = painterResource(R.drawable.ic_person_placeholder)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View details",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
```

## 🔄 Performance Patterns

### 1. Lazy Loading Pattern

**Description**: Load content only when needed to improve performance  
**Best For**: Large lists, image galleries, complex views  
**Sub-Agent Integration**: Performance Analyzer for loading optimization, Context Optimizer for preloading strategy

```swift
// iOS Lazy Loading Pattern
struct LazyImageGrid: View {
    @StateObject private var viewModel = ImageGridViewModel()
    
    private let columns = [
        GridItem(.adaptive(minimum: 150), spacing: 8)
    ]
    
    var body: some View {
        NavigationView {
            ScrollView {
                LazyVGrid(columns: columns, spacing: 8) {
                    ForEach(viewModel.images.indices, id: \.self) { index in
                        LazyImageCell(
                            imageItem: viewModel.images[index],
                            onAppear: {
                                Task {
                                    await viewModel.loadMoreIfNeeded(currentIndex: index)
                                }
                            }
                        )
                    }
                }
                .padding(.horizontal)
            }
            .navigationTitle("Photos")
            .refreshable {
                await viewModel.refresh()
            }
        }
        .task {
            await viewModel.loadInitialImages()
        }
    }
}

struct LazyImageCell: View {
    let imageItem: ImageItem
    let onAppear: () -> Void
    
    @State private var isLoaded = false
    
    var body: some View {
        AsyncImage(url: URL(string: imageItem.thumbnailURL)) { phase in
            switch phase {
            case .empty:
                Rectangle()
                    .fill(Color.gray.opacity(0.3))
                    .aspectRatio(1, contentMode: .fit)
                    .overlay(
                        ProgressView()
                            .scaleEffect(0.8)
                    )
                
            case .success(let image):
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .clipped()
                    .onAppear {
                        isLoaded = true
                    }
                
            case .failure(_):
                Rectangle()
                    .fill(Color.gray.opacity(0.3))
                    .aspectRatio(1, contentMode: .fit)
                    .overlay(
                        Image(systemName: "photo")
                            .foregroundColor(.gray)
                    )
                
            @unknown default:
                EmptyView()
            }
        }
        .aspectRatio(1, contentMode: .fit)
        .clipShape(RoundedRectangle(cornerRadius: 8))
        .onAppear {
            onAppear()
        }
    }
}

@MainActor
class ImageGridViewModel: ObservableObject {
    @Published var images: [ImageItem] = []
    @Published var isLoading = false
    @Published var hasMoreImages = true
    
    private let imageService = ImageService()
    private let contextOptimizer: ContextOptimizerAgent?
    private let performanceAnalyzer: PerformanceAnalyzerAgent?
    
    private var currentPage = 0
    private let pageSize = 20
    
    init(subAgents: SubAgentContainer? = nil) {
        self.contextOptimizer = subAgents?.contextOptimizer
        self.performanceAnalyzer = subAgents?.performanceAnalyzer
    }
    
    func loadInitialImages() async {
        guard !isLoading else { return }
        
        isLoading = true
        currentPage = 0
        
        do {
            let newImages = try await imageService.fetchImages(
                page: currentPage,
                pageSize: pageSize
            )
            
            images = newImages
            currentPage += 1
            hasMoreImages = newImages.count == pageSize
            
            // Optimize preloading strategy
            if let contextOptimizer = contextOptimizer {
                await contextOptimizer.optimizeImagePreloading(
                    images: newImages,
                    context: .gridView,
                    options: [
                        .preloadVisible,
                        .preloadNext,
                        .respectBandwidth
                    ]
                )
            }
            
        } catch {
            // Handle error
        }
        
        isLoading = false
    }
    
    func loadMoreIfNeeded(currentIndex: Int) async {
        guard !isLoading && hasMoreImages else { return }
        
        // Trigger loading when approaching the end
        let threshold = images.count - 5
        guard currentIndex >= threshold else { return }
        
        isLoading = true
        
        do {
            let newImages = try await imageService.fetchImages(
                page: currentPage,
                pageSize: pageSize
            )
            
            images.append(contentsOf: newImages)
            currentPage += 1
            hasMoreImages = newImages.count == pageSize
            
            // Performance monitoring
            await performanceAnalyzer?.analyzePaginationPerformance(
                currentPage: currentPage,
                itemsLoaded: newImages.count,
                totalItems: images.count
            )
            
        } catch {
            // Handle error
        }
        
        isLoading = false
    }
    
    func refresh() async {
        await loadInitialImages()
    }
}
```

---

**Mobile Development Patterns Benefits**:
- **Platform Optimization**: Patterns optimized for mobile constraints and capabilities
- **Performance Focus**: Battery efficiency, memory management, smooth animations
- **User Experience**: Native platform conventions and accessibility
- **Sub-Agent Integration**: Intelligent optimization and monitoring
- **Offline Support**: Robust offline-first patterns and data synchronization
- **Responsive Design**: Adaptive layouts for different screen sizes and orientations

**Usage**: Apply these patterns when developing mobile applications to ensure optimal performance, user experience, and platform compliance while leveraging Sub-Agent intelligence for continuous optimization.