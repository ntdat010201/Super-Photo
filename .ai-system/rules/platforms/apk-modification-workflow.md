# APK Reverse Engineering & Modification Workflow

> **🔧 Specialized APK Modification Agent**  
> Expert in APK reverse engineering, Google Services integration, Firebase SDK updates, and SafeAds implementation

## 🚨 Critical Safety Protocols

### 64K Method Limit Management
- **Monitor**: `find smali* -name "*.smali" | wc -l`
- **Distribute**: smali → smali_classes2 → smali_classes3 → smali_classes4
- **Batch Limit**: <1000 files per operation
- **Emergency Stop**: "Unsigned short value out of range" error

### Automation-First Approach
- **Mandatory**: Execute quick_test.sh before manual operations
- **Validation**: adb logcat monitoring
- **Build Process**: Clean build/ directory before compilation

### Resource Protection
- **Preserve**: DemoSdk components (never delete)
- **Backup**: Create snapshots before major modifications
- **Conflict Resolution**: Remove public.xml if resource conflicts occur
- **Rollback**: Maximum 3 attempts before reverting changes
- **Nguyên tắc**: Không bao giờ được sao chép hàng loạt resource hay code từ APK Decompiled

## 🛠️ Technical Capabilities

### APK Reverse Engineering
- **Decompilation**: APKTool, jadx, dex2jar workflows
- **Smali Analysis**: Method signature identification and modification
- **Resource Extraction**: Assets, layouts, strings, manifests
- **Signature Management**: Re-signing with custom certificates

### Google Services Integration
- **Play Services**: Version compatibility analysis
- **Firebase SDK**: Analytics, Crashlytics, Remote Config integration
- **Authentication**: Google Sign-In implementation
- **Cloud Messaging**: FCM setup and configuration

### SDK Integration Patterns
- **SafeAds**: Revenue optimization and ad placement
- **Analytics**: Event tracking and user behavior analysis
- **Crash Reporting**: Error monitoring and debugging
- **A/B Testing**: Feature flag implementation

### Build System Expertise
- **Gradle**: Build script modification and dependency management
- **ProGuard/R8**: Code obfuscation and optimization
- **Manifest Merging**: Permission and component integration
- **Multi-APK**: Architecture-specific builds

## 🚨 Emergency Response Procedures

### Method Limit Exceeded
1. **Immediate Stop** → Count verification → Rollback
2. **Redistribute**: Spread classes across smali directories
3. **Validate**: Test functionality with quick_test.sh

### Build Failures
1. **Manifest Check**: Verify AndroidManifest.xml syntax
2. **Resource Cleanup**: Remove conflicting public.xml
3. **Dependency Audit**: Ensure all required libraries present

### Runtime Crashes
1. **Logcat Analysis**: Identify crash root cause
2. **Activity Verification**: Confirm main activity registration
3. **Library Check**: Validate missing dependencies

## 🔄 Integration Workflows

### 🚀 Google Services Upgrade Workflow (Source Project Required)

**Note**: "DemoSdk" is used as a placeholder name throughout this workflow. The actual source project can have any name (e.g., "MyApp", "TestProject", "SourceAPK", etc.).

**1.1 Source Project Request Protocol**
```bash
# Request source project from user
echo "🔍 Google Services Upgrade requires source project with latest libraries"
echo "📋 Please provide:"
echo "   - Source project name/path (any name, e.g., MyApp, TestProject, LatestLibsAPK)"
echo "   - Decompiled smali code directory"
echo "   - Target library versions (Google Play Services, Firebase, AdMob)"
echo "   - Compatibility requirements"
echo ""
echo "ℹ️  Note: Throughout this workflow, we use 'DemoSdk' as placeholder name."
echo "   Your actual project can have any name (MyApp, TestProject, etc.)"
```

#### Prerequisites & Source Project Request
1. **User Requirement**: Request source project smali code with latest library versions
   - Google Play Services (latest stable)
   - Firebase SDK (latest compatible version)
   - AdMob SDK (latest version)
   - Any additional Google Services libraries
   - **Project name can be anything, not necessarily 'DemoSdk'**

2. **Source Project Validation**:
   ```bash
   # Verify source project structure (replace with actual project path)
   ls -la /path/to/SourceProject/smali*/com/google/
   
   # Check version indicators in smali files
   grep -r "version" /path/to/SourceProject/smali*/com/google/android/gms/
   ```

#### Phase 2: Current Version Analysis

**2.1 Detect Current Library Versions**
```bash
# Google Play Services version detection
detect_gms_version() {
    local apk_path="$1"
    
    echo "🔍 Detecting Google Play Services version in target APK..."
    
    # Search for version indicators in smali files
    find "$apk_path" -name "*.smali" -path "*/com/google/android/gms/*" \
        -exec grep -l "version\|VERSION" {} \; | head -10
    
    # Check AndroidManifest.xml for version info
    grep -i "google.*play.*services" "$apk_path/AndroidManifest.xml" || echo "No manifest version found"
    
    # Look for version constants
    find "$apk_path" -name "*.smali" -exec grep -l "const.*string.*version" {} \;
}

# Firebase version detection
detect_firebase_version() {
    local apk_path="$1"
    
    echo "🔍 Detecting Firebase version in target APK..."
    
    find "$apk_path" -name "*.smali" -path "*/com/google/firebase/*" \
        -exec grep -l "version\|VERSION" {} \; | head -5
    
    # Check for Firebase config
    find "$apk_path" -name "google-services.json" -o -name "*firebase*config*"
}

# AdMob SDK version detection
detect_admob_version() {
    local apk_path="$1"
    
    echo "🔍 Detecting AdMob SDK version in target APK..."
    
    find "$apk_path" -name "*.smali" -path "*/com/google/android/gms/ads/*" \
        -exec grep -l "version\|VERSION" {} \; | head -5
}

# Source project version detection (replaces DemoSdk references)
detect_source_versions() {
    local source_path="$1"
    local project_name="$(basename "$source_path")"
    
    echo "🔍 Detecting library versions in source project: $project_name"
    
    # Detect versions in source project
    detect_gms_version "$source_path"
    detect_firebase_version "$source_path"
    detect_admob_version "$source_path"
}
```

3. **Current Project Assessment**:
   ```bash
   # Analyze current Google Services versions
   find smali* -path "*/com/google/android/gms/*" -name "*.smali" | head -20
   
   # Check Firebase version indicators
   find smali* -path "*/com/google/firebase/*" -name "*.smali" | grep -i version
   
   # AdMob version detection
   find smali* -path "*/com/google/android/gms/ads/*" -name "*.smali" | head -10
   ```

4. **Version Documentation in .project-identity**:
   ```json
   "apkModification": {
     "sourceProjectInfo": {
       "name": "ActualProjectName",
       "path": "/path/to/source/project",
       "note": "DemoSdk is placeholder - actual name can be anything"
     },
     "currentLibraryVersions": {
       "googlePlayServices": "detected_version_from_smali",
       "firebaseCore": "detected_version_from_smali", 
       "firebaseAnalytics": "detected_version_from_smali",
       "admobSdk": "detected_version_from_smali",
       "lastVersionCheck": "2024-01-XX"
     },
     "targetLibraryVersions": {
     "googlePlayServices": "source_project_version",
     "firebaseCore": "source_project_version",
     "firebaseAnalytics": "source_project_version", 
     "admobSdk": "source_project_version"
   },
     "upgradeStatus": "planning|in_progress|completed|failed"
   }
   ```

#### Upgrade Planning & Execution
5. **Compatibility Matrix Analysis**:
   - Compare current vs target versions
   - Identify breaking changes and deprecated methods
   - Plan migration strategy for incompatible components
   - Estimate method count impact

6. **Incremental Upgrade Strategy**:
   ```bash
   # Phase 1: Backup current state
   cp -r smali* backup_pre_upgrade/
   
   # Phase 2: Remove old Google Services (selective)
   find smali* -path "*/com/google/android/gms" -type d -exec rm -rf {} +
   
   # Phase 3: Copy new versions from source project (monitored)
   # Note: Replace with actual source project path (can be any name)
   SOURCE_PROJECT_PATH="/path/to/your/source/project"  # Not necessarily DemoSdk
   cp -r "$SOURCE_PROJECT_PATH"/smali*/com/google/android/gms/ ./smali_classes2/com/google/android/
   
   # Phase 4: Method count validation
   find smali* -name "*.smali" | wc -l
   ```

7. **Post-Upgrade Validation**:
   - Update .project-identity with new versions
   - Test core functionality with quick_test.sh
   - Verify Google Services initialization
   - Monitor logcat for integration issues

### Firebase Integration Process
1. **Version Analysis**: Check compatibility matrix
2. **Dependency Addition**: Update build.gradle
3. **Configuration**: Add google-services.json
4. **Initialization**: Implement FirebaseApp.initializeApp()
5. **Testing**: Verify analytics events

### Google Ads Implementation
1. **AdMob Setup**: Configure ad units
2. **Banner Integration**: Layout and lifecycle management
3. **Interstitial Ads**: Timing and frequency optimization
4. **Rewarded Video**: User engagement strategies

### SafeAds SDK Integration
1. **Library Import**: Add SafeAds dependencies
2. **Configuration**: Set up ad network mediation
3. **Revenue Optimization**: Implement waterfall strategies
4. **Performance Monitoring**: Track fill rates and eCPM

## 📋 Quality Assurance

### Pre-Integration Checklist
- [ ] Backup original APK
- [ ] Document current method count
- [ ] Verify target SDK compatibility
- [ ] Test baseline functionality

### Post-Integration Validation
- [ ] Method count within limits
- [ ] All features functional
- [ ] No memory leaks detected
- [ ] Performance benchmarks met

### Documentation Requirements
- **Integration Log**: Changes made and rationale
- **Version Matrix**: SDK compatibility tracking
- **Emergency Contacts**: Rollback procedures
- **Performance Metrics**: Before/after comparisons
- **.project-identity Updates**: Mandatory version tracking

### 📋 .project-identity Version Tracking Template

#### Mandatory APK Modification Section
```json
{
  "apkModification": {
    "projectPath": "/path/to/decompiled/apk",
    "originalApkInfo": {
      "packageName": "com.example.app",
      "versionName": "1.0.0",
      "versionCode": 1,
      "targetSdk": 33,
      "minSdk": 21
    },
    "currentLibraryVersions": {
      "googlePlayServices": "unknown",
      "firebaseCore": "unknown",
      "firebaseAnalytics": "unknown",
      "firebaseMessaging": "unknown",
      "admobSdk": "unknown",
      "detectionMethod": "smali_analysis",
      "lastVersionCheck": "2024-01-XX",
      "methodCount": 0
    },
    "targetLibraryVersions": {
       "googlePlayServices": "from_source_project",
       "firebaseCore": "from_source_project",
       "firebaseAnalytics": "from_source_project",
       "firebaseMessaging": "from_source_project",
       "admobSdk": "from_source_project",
       "sourceProjectPath": "/path/to/ActualSourceProject",
       "sourceProjectNote": "DemoSdk used as placeholder throughout workflow",
      "expectedMethodCount": 0
    },
    "upgradeStatus": "not_started",
    "upgradeLog": [
       {
         "timestamp": "2024-01-XX",
         "action": "version_detection",
         "details": "Detected current library versions",
         "methodCountBefore": 0,
         "methodCountAfter": 0
       },
       {
         "timestamp": "2024-01-XX",
         "action": "source_project_integration",
         "source": "/path/to/ActualSourceProject",
         "note": "DemoSdk is placeholder - actual project name used",
         "libraries": ["GMS latest", "Firebase latest"]
       }
     ],
    "backupInfo": {
      "hasBackup": false,
      "backupPath": "/path/to/backup",
      "backupTimestamp": "2024-01-XX"
    }
  }
}
```

#### Version Detection Validation Checklist
- [ ] Current Google Play Services version detected from smali
- [ ] Firebase Core version identified
- [ ] AdMob SDK version confirmed
- [ ] Method count baseline established
- [ ] Source project path validated and accessible
- [ ] Target versions extracted from source project
- [ ] Compatibility matrix analyzed
- [ ] Backup created before upgrade
- [ ] .project-identity updated with findings
- [ ] Upgrade plan documented in upgradeLog

## 🔧 Essential Commands for APK Modification

### Method Count Monitoring
```bash
# Count total methods across all smali directories
find smali* -name "*.smali" | wc -l

# Check distribution across smali directories
for dir in smali*; do echo "$dir: $(find $dir -name '*.smali' | wc -l)"; done

# Monitor during incremental copy
watch 'find smali* -name "*.smali" | wc -l'
```

### Incremental Copy Strategy
```bash
# Safe incremental copy with monitoring
cp -r /source/smali_classes2/* ./smali_classes2/ && \
echo "Current count: $(find smali* -name '*.smali' | wc -l)" && \
if [ $(find smali* -name "*.smali" | wc -l) -gt 64000 ]; then \
  echo "WARNING: Approaching 64K limit!"; \
fi
```

### Google Services Version Detection
```bash
# Detect Google Play Services version from smali
grep -r "GOOGLE_PLAY_SERVICES_VERSION" smali*/com/google/android/gms/ | head -5

# Find Firebase version indicators
grep -r "FirebaseApp" smali*/com/google/firebase/ | grep -i version | head -3

# AdMob SDK version detection
find smali* -path "*/com/google/android/gms/ads/*" -name "*.smali" -exec grep -l "version\|VERSION" {} \; | head -5

# Extract version strings from smali constants
grep -r "const-string.*version" smali*/com/google/ | grep -E "[0-9]+\.[0-9]+" | head -10
```

### Source Project Integration Commands
```bash
# Validate source project before integration (replace with actual project path)
ls -la /path/to/SourceProject/smali*/com/google/android/gms/
ls -la /path/to/SourceProject/smali*/com/google/firebase/

# Safe Google Services upgrade with monitoring
echo "Current method count: $(find smali* -name '*.smali' | wc -l)"
cp -r /SourceProject/smali*/com/google/android/gms/ ./smali_classes2/com/google/android/ && \
echo "Post-GMS count: $(find smali* -name '*.smali' | wc -l)"

# Firebase upgrade with validation
cp -r /SourceProject/smali*/com/google/firebase/ ./smali_classes2/com/google/ && \
echo "Post-Firebase count: $(find smali* -name '*.smali' | wc -l)"

# Firebase upgrade (if method count allows)
if [ $(find smali* -name "*.smali" | wc -l) -lt 55000 ]; then
    find smali* -path "*/com/google/firebase" -type d -exec rm -rf {} +
    cp -r "$SOURCE_PROJECT_PATH"/smali*/com/google/firebase/ ./smali_classes2/com/google/
    echo "✅ Firebase upgraded from source project: $(basename "$SOURCE_PROJECT_PATH")"
fi

# Emergency rollback if method limit exceeded
if [ $(find smali* -name "*.smali" | wc -l) -gt 64000 ]; then \
  echo "CRITICAL: Method limit exceeded, rolling back..."; \
  rm -rf smali*/com/google/android/gms/; \
  rm -rf smali*/com/google/firebase/; \
  cp -r backup_pre_upgrade/smali*/com/google/ ./smali_classes2/com/; \
fi
```

### Datatransport Issue Resolution
```bash
# Backup current datatransport
cp -r smali*/com/google/android/datatransport/ backup_datatransport/

# Remove old version
find smali* -path "*/com/google/android/datatransport" -type d -exec rm -rf {} +

# Copy new version from source project
cp -r /SourceProject/smali*/com/google/android/datatransport/ ./smali_classes2/com/google/android/

# Verify method count
find smali* -name "*.smali" | wc -l
```

## 🔍 Troubleshooting Matrix

### Common Issues
- **Duplicate Classes**: Use exclude groups in Gradle
- **Permission Conflicts**: Merge manifests carefully
- **ProGuard Issues**: Add keep rules for integrated SDKs
- **Signature Mismatch**: Re-sign with consistent certificate

### Performance Optimization
- **Code Shrinking**: Enable R8 optimization
- **Resource Optimization**: Remove unused assets
- **Network Efficiency**: Implement proper caching
- **Battery Usage**: Optimize background processes

## 📊 Success Metrics
- **Integration Success Rate**: >95% first-attempt success
- **Method Count Efficiency**: <60K methods post-integration
- **Performance Impact**: <10% startup time increase
- **Crash Rate**: <0.1% post-integration crashes

## 🔗 Related Workflows
- **[Android Development](android-workflow.md)** - Standard Android development practices
- **[Development Rules](../development/development-rules.md)** - Code quality and safety protocols
- **[Terminal Rules](../core/terminal-rules.md)** - Command line tools for APK manipulation

## 🤖 Automated Workflow Triggers

### Agent Activation Keywords
- **Primary**: `apk modification`, `google services upgrade`, `firebase update`, `admob integration`
- **Source Project**: `source project`, `latest libraries`, `library upgrade`, `demosdk` (legacy placeholder name)
- **Version**: `version detection`, `library version`, `sdk version`, `upgrade planning`
- **Emergency**: `method limit`, `64k limit`, `rollback`, `emergency restore`
- **Placeholder Note**: `DemoSdk` used as example name - actual project can be named anything

### Workflow Integration Points

#### With .project-identity System
1. **Auto-detect APK projects**: Check for `smali*` directories
2. **Load apkModification config**: Parse existing version tracking
3. **Update project stage**: Set to `apk_modification` when triggered
4. **Sync with other agents**: Share version info with Android Agent

#### With Agent Selection System
- **Priority Score**: +40 points for APK/smali keywords
- **Confidence Boost**: +25 points when source project mentioned
- **Emergency Override**: Force selection on method limit issues
- **Collaboration Mode**: Work with Android Agent for native components

### Mandatory Pre-Workflow Checks
```bash
# Validate APK project structure
if [ -d "smali" ] || [ -d "smali_classes2" ]; then
  echo "✅ APK project detected"
  # Load APK modification workflow
else
  echo "❌ Not an APK project, redirecting to Android Agent"
fi

# Check for source project requirement
if grep -q "google.*service\|firebase\|admob" <<< "$user_request"; then
  echo "🔍 Google Services upgrade detected, requesting source project with latest libraries"
  echo "ℹ️  Note: 'DemoSdk' is placeholder name - your project can have any name"
fi
```

### Success Metrics & KPIs
- **Version Detection Accuracy**: >95% correct version identification
- **Upgrade Success Rate**: >90% successful library upgrades
- **Method Count Management**: <5% projects exceed 64K limit
- **Rollback Efficiency**: <2 minutes average rollback time
- **Documentation Compliance**: 100% .project-identity updates

---
**Agent Activation**: Triggered by APK modification, Google Services, Firebase, or SafeAds keywords  
**Collaboration**: Works with Android Agent for native development aspects  
**Integration**: Auto-syncs with .project-identity and agent selection system