# {PROJECT_NAME} - Implementation Tasks

## 1. Project Overview

**Feature**: {FEATURE_NAME}  
**Current Status**: {OVERALL_STATUS} (e.g., ❌ Not Started, 🟡 In Progress, ✅ Completed)  
**Priority**: {PRIORITY_LEVEL} (High/Medium/Low)  
**Estimated Effort**: {EFFORT_ESTIMATE} (e.g., 2-3 weeks)  
**Dependencies**: {DEPENDENCIES}  

## 2. Implementation Status Overview

### 2.1 Completed Components ✅

- [x] **{Component1}**: {Brief description of completed work}
  - Implementation: {Implementation details}
  - Status: ✅ Completed
  - Requirements: {Requirement references}

- [x] **{Component2}**: {Brief description of completed work}
  - Implementation: {Implementation details}
  - Status: ✅ Completed
  - Requirements: {Requirement references}

- [x] **{Component3}**: {Brief description of completed work}
  - Implementation: {Implementation details}
  - Status: ✅ Completed
  - Requirements: {Requirement references}

### 2.2 Missing/Incomplete Components ❌

- [ ] **{MissingComponent1}**: {Description of what needs to be implemented}
  - Priority: {Priority level}
  - Estimated Effort: {Time estimate}
  - Dependencies: {Required components}
  - Requirements: {Requirement references}

- [ ] **{MissingComponent2}**: {Description of what needs to be implemented}
  - Priority: {Priority level}
  - Estimated Effort: {Time estimate}
  - Dependencies: {Required components}
  - Requirements: {Requirement references}

- [ ] **{MissingComponent3}**: {Description of what needs to be implemented}
  - Priority: {Priority level}
  - Estimated Effort: {Time estimate}
  - Dependencies: {Required components}
  - Requirements: {Requirement references}

## 3. Detailed Task Breakdown

### Module 1: {Module1Name} (Data Layer)

**Status**: {Module1Status} (❌ Not Started / 🟡 In Progress / ✅ Completed)  
**Priority**: {Module1Priority}  
**Estimated Effort**: {Module1Effort}  

#### 3.1.1 Database Implementation

**Task**: Implement {Entity} database schema and DAOs  
**Status**: {Task1Status}  
**Requirements**: {Requirement references}  

```kotlin
// Enhanced {Entity} Entity with {advanced features}
@Entity(tableName = "{entity_table}")
data class {Entity}Entity(
    @PrimaryKey val id: String,
    val {field1}: {DataType},
    val {field2}: {DataType},
    val {field3}: {DataType},
    val timestamp: Long,
    val notes: String?,
    
    // {Advanced Feature 1} fields
    val {advancedField1}: {DataType}?,
    val {advancedField2}: String?, // JSON metadata
    val {advancedField3}: {DataType}?,
    
    // {Advanced Feature 2} fields
    val {feature2Field1}: {DataType}?,
    val {feature2Field2}: String?,
    val {feature2Field3}: Boolean = false,
    
    // Metadata
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: String = "pending",
    val deviceId: String
)

@Dao
interface {Entity}Dao {
    @Query("SELECT * FROM {entity_table} ORDER BY timestamp DESC")
    fun getAll{Entity}s(): Flow<List<{Entity}Entity>>
    
    @Query("SELECT * FROM {entity_table} WHERE timestamp BETWEEN :start AND :end")
    fun get{Entity}sInRange(start: Long, end: Long): Flow<List<{Entity}Entity>>
    
    @Query("SELECT * FROM {entity_table} WHERE {advancedField1} IS NOT NULL")
    fun get{Entity}sWithAdvancedData(): Flow<List<{Entity}Entity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert{Entity}(entity: {Entity}Entity)
    
    @Update
    suspend fun update{Entity}(entity: {Entity}Entity)
    
    @Delete
    suspend fun delete{Entity}(entity: {Entity}Entity)
    
    @Query("DELETE FROM {entity_table} WHERE timestamp < :cutoffDate")
    suspend fun deleteOld{Entity}s(cutoffDate: Long)
}
```

**Acceptance Criteria**:
- [ ] Entity properly maps all required fields
- [ ] DAO supports all CRUD operations
- [ ] Advanced feature fields are properly handled
- [ ] Database migrations are implemented
- [ ] Unit tests cover all DAO methods

#### 3.1.2 Repository Implementation

**Task**: Implement {Entity}Repository with caching and sync  
**Status**: {Task2Status}  
**Requirements**: {Requirement references}  

```kotlin
class {Entity}RepositoryImpl @Inject constructor(
    private val dao: {Entity}Dao,
    private val {advancedService}: {AdvancedService},
    private val preferences: {Feature}Preferences
) : {Entity}Repository {
    
    override fun getAll{Entity}s(): Flow<List<{Entity}>> {
        return dao.getAll{Entity}s().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun add{Entity}(entity: {Entity}): Result<Unit> {
        return try {
            val entityWithAdvancedData = entity.copy(
                {advancedField1} = {advancedService}.process{Data}(entity.{field1}),
                {advancedField2} = {advancedService}.generate{Metadata}(entity)
            )
            dao.insert{Entity}(entityWithAdvancedData.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun analyze{Entity}Trends(period: DateRange): {Entity}Statistics {
        // Implementation for trend analysis
    }
}
```

**Acceptance Criteria**:
- [ ] Repository implements all interface methods
- [ ] Error handling is properly implemented
- [ ] Advanced features are integrated
- [ ] Caching strategy is implemented
- [ ] Unit tests achieve >90% coverage

### Module 2: {Module2Name} (Advanced Feature Integration)

**Status**: {Module2Status}  
**Priority**: {Module2Priority}  
**Estimated Effort**: {Module2Effort}  

#### 3.2.1 {Advanced Feature} Implementation

**Task**: Implement {advanced feature} processing system  
**Status**: {Task3Status}  
**Requirements**: {Requirement references}  

```kotlin
class {AdvancedFeature}Engine @Inject constructor(
    private val {dependency1}: {Dependency1},
    private val {dependency2}: {Dependency2}
) {
    
    suspend fun process{Input}(
        input: {InputType},
        context: {ContextType}
    ): Result<{OutputType}> {
        return try {
            val processedData = {dependency1}.process(input)
            val enhancedResult = {dependency2}.enhance(processedData, context)
            Result.success(enhancedResult)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun analyze{Pattern}(
        data: List<{DataType}>,
        parameters: {ParameterType}
    ): {AnalysisResult} {
        // Advanced analysis implementation
        return {AnalysisResult}(
            {resultField1} = {calculation1},
            {resultField2} = {calculation2},
            confidence = {confidence_calculation},
            recommendations = generate{Recommendations}(data)
        )
    }
    
    private fun generate{Recommendations}(data: List<{DataType}>): List<String> {
        // Recommendation generation logic
        return listOf(
            "{Recommendation template 1}",
            "{Recommendation template 2}",
            "{Recommendation template 3}"
        )
    }
}
```

**Acceptance Criteria**:
- [ ] Advanced processing works with sample data
- [ ] Error handling covers edge cases
- [ ] Performance meets requirements (<{time_limit}ms)
- [ ] Integration tests pass
- [ ] Documentation is complete

#### 3.2.2 {Feature2} System

**Task**: Implement {feature2} with {specific capabilities}  
**Status**: {Task4Status}  
**Requirements**: {Requirement references}  

```kotlin
class {Feature2}Processor @Inject constructor(
    private val {service1}: {Service1},
    private val {service2}: {Service2}
) {
    
    suspend fun {primaryMethod}(
        input: {InputType},
        options: {OptionsType} = {OptionsType}.default()
    ): {OutputType} {
        // Primary processing logic
        val step1Result = {service1}.{method1}(input)
        val step2Result = {service2}.{method2}(step1Result, options)
        
        return {OutputType}(
            {outputField1} = step2Result.{field1},
            {outputField2} = step2Result.{field2},
            metadata = generate{Metadata}(input, step2Result)
        )
    }
    
    private fun generate{Metadata}(
        input: {InputType},
        result: {ResultType}
    ): {MetadataType} {
        return {MetadataType}(
            {metaField1} = {calculation1},
            {metaField2} = {calculation2},
            timestamp = System.currentTimeMillis()
        )
    }
}
```

**Acceptance Criteria**:
- [ ] Feature processes all supported input types
- [ ] Output quality meets acceptance criteria
- [ ] Performance is within acceptable limits
- [ ] Error handling is comprehensive
- [ ] Unit and integration tests pass

### Module 3: {Module3Name} (UI Layer)

**Status**: {Module3Status}  
**Priority**: {Module3Priority}  
**Estimated Effort**: {Module3Effort}  

#### 3.3.1 {Screen1} Implementation

**Task**: Implement {screen1} with {specific features}  
**Status**: {Task5Status}  
**Requirements**: {Requirement references}  

```kotlin
@Composable
fun {Screen1}Screen(
    viewModel: {Screen1}ViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.load{Data}()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            uiState.isLoading -> {
                {LoadingComponent}()
            }
            uiState.error != null -> {
                {ErrorComponent}(
                    error = uiState.error,
                    onRetry = viewModel::retry{Action}
                )
            }
            else -> {
                {MainContent}(
                    data = uiState.{dataField},
                    onAction = viewModel::{primaryAction}
                )
            }
        }
    }
}

class {Screen1}ViewModel @Inject constructor(
    private val {useCase1}: {UseCase1},
    private val {useCase2}: {UseCase2}
) : ViewModel() {
    
    private val _uiState = MutableStateFlow({Screen1}UiState())
    val uiState: StateFlow<{Screen1}UiState> = _uiState.asStateFlow()
    
    fun load{Data}() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            {useCase1}.execute()
                .onSuccess { data ->
                    _uiState.value = _uiState.value.copy(
                        {dataField} = data,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message,
                        isLoading = false
                    )
                }
        }
    }
    
    fun {primaryAction}({param}: {ParamType}) {
        viewModelScope.launch {
            {useCase2}.execute({param})
                .onSuccess {
                    load{Data}() // Refresh data
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }
}
```

**Acceptance Criteria**:
- [ ] Screen displays all required information
- [ ] User interactions work correctly
- [ ] Loading and error states are handled
- [ ] Navigation flows work properly
- [ ] UI tests cover main scenarios

#### 3.3.2 {Component} Implementation

**Task**: Implement reusable {component} component  
**Status**: {Task6Status}  
**Requirements**: {Requirement references}  

```kotlin
@Composable
fun {Component}(
    data: {DataType},
    onAction: ({ActionType}) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Component implementation
            {SubComponent1}(data.{field1})
            {SubComponent2}(data.{field2}, onAction)
            {SubComponent3}(data.{field3})
        }
    }
}
```

**Acceptance Criteria**:
- [ ] Component is reusable across screens
- [ ] All props are properly handled
- [ ] Component follows design system
- [ ] Accessibility is implemented
- [ ] Component tests are written

## 4. Testing Strategy

### 4.1 Unit Tests

- [ ] **Repository Tests**: Test all repository methods with mocked dependencies
- [ ] **Use Case Tests**: Test business logic with various scenarios
- [ ] **ViewModel Tests**: Test state management and user interactions
- [ ] **{Advanced Feature} Tests**: Test advanced processing with sample data

### 4.2 Integration Tests

- [ ] **Database Tests**: Test DAO operations with real database
- [ ] **{Advanced Feature} Integration**: Test end-to-end advanced workflows
- [ ] **API Tests**: Test external service integrations

### 4.3 UI Tests

- [ ] **Screen Tests**: Test user flows and navigation
- [ ] **Component Tests**: Test individual component behavior
- [ ] **Accessibility Tests**: Test screen reader and accessibility features

## 5. Deployment Tasks

### 5.1 Build Configuration

- [ ] **Gradle Setup**: Configure build scripts and dependencies
- [ ] **ProGuard Rules**: Add obfuscation rules for release builds
- [ ] **Signing Config**: Set up release signing configuration

### 5.2 Quality Assurance

- [ ] **Code Review**: Complete peer review of all modules
- [ ] **Performance Testing**: Validate performance requirements
- [ ] **Security Review**: Audit security implementations
- [ ] **Documentation**: Complete technical documentation

## 6. Risk Assessment

### 6.1 Technical Risks

| Risk | Impact | Probability | Mitigation |
|------|--------|-------------|------------|
| {Risk1} | {Impact level} | {Probability} | {Mitigation strategy} |
| {Risk2} | {Impact level} | {Probability} | {Mitigation strategy} |
| {Risk3} | {Impact level} | {Probability} | {Mitigation strategy} |

### 6.2 Timeline Risks

- **{Advanced Feature} Complexity**: May require additional research time
- **Integration Challenges**: Third-party service dependencies
- **Testing Coverage**: Comprehensive testing may extend timeline

## 7. Success Metrics

### 7.1 Technical Metrics

- [ ] **Code Coverage**: >90% for critical modules
- [ ] **Performance**: All operations complete within {time_limit}
- [ ] **Memory Usage**: Stay within {memory_limit} limits
- [ ] **Crash Rate**: <0.1% in production

### 7.2 Feature Metrics

- [ ] **{Feature1} Accuracy**: >{accuracy_percentage}% success rate
- [ ] **{Feature2} Performance**: <{time_limit} processing time
- [ ] **User Experience**: {ux_metric} satisfaction score

---

**Last Updated**: {LAST_UPDATE_DATE}  
**Next Review**: {NEXT_REVIEW_DATE}  
**Assigned Team**: {TEAM_MEMBERS}  
**Project Manager**: {PROJECT_MANAGER}  

## Template Usage Instructions

1. **Replace all placeholders** in curly braces `{placeholder}` with project-specific values
2. **Update task statuses** as implementation progresses
3. **Add specific code examples** relevant to your domain
4. **Customize modules** based on your architecture
5. **Adjust acceptance criteria** to match your quality standards
6. **Update risk assessment** based on your project context

### Common Placeholders:
- `{PROJECT_NAME}` - Your project name
- `{FEATURE_NAME}` - Main feature being implemented
- `{Entity}` - Primary domain entity
- `{Module1/2/3Name}` - Module names in your architecture
- `{Component1/2/3}` - UI component names
- `{AdvancedFeature}` - Advanced technology integration
- `{Screen1}` - Screen names in your application
- `{UseCase1/2}` - Business logic use cases
- `{DataType}` - Data types specific to your domain