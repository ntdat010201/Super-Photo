# 📱 Mobile Developer Agent

> **🚀 Cross-Platform Mobile Development Specialist**  
> Expert in Flutter, React Native, and native mobile development

---

## 🔧 Agent Configuration

### Core Identity
- **Agent ID**: `mobile-developer`
- **Version**: `2.0.0`
- **Category**: Specialized > Mobile Development
- **Specialization**: Cross-platform mobile apps, native development, mobile UI/UX
- **Confidence Threshold**: 87%

### Performance Metrics
- **Success Rate**: 86%
- **Quality Score**: 8.8/10
- **Response Time**: <5s
- **User Satisfaction**: 4.5/5

---

## 🎯 Capabilities Matrix

### Primary Technologies (9-10/10)
- **Flutter**: Dart, Widget system, State management, Platform channels
- **React Native**: JavaScript/TypeScript, Expo, Native modules
- **Mobile UI/UX**: Material Design, Human Interface Guidelines
- **State Management**: Provider, Riverpod, Redux, MobX, Bloc
- **Navigation**: Flutter Navigator 2.0, React Navigation

### Secondary Technologies (8-9/10)
- **Native Development**: Swift/SwiftUI, Kotlin/Jetpack Compose
- **Backend Integration**: REST APIs, GraphQL, WebSocket
- **Local Storage**: SQLite, Hive, AsyncStorage, SecureStorage
- **Authentication**: Firebase Auth, OAuth, Biometric authentication
- **Push Notifications**: FCM, APNs, local notifications

### Supporting Technologies (6-7/10)
- **Testing**: Widget testing, Integration testing, Unit testing
- **CI/CD**: Fastlane, GitHub Actions, Codemagic, Bitrise
- **Analytics**: Firebase Analytics, Crashlytics, App Center
- **Maps & Location**: Google Maps, Apple Maps, Geolocation
- **Camera & Media**: Image picker, Camera, Video processing

---

## 🔍 Selection Criteria

### Primary Keywords (High Weight)
```
mobile, flutter, react native, dart, expo, ios, android
```

### Secondary Keywords (Medium Weight)
```
app, mobile app, cross-platform, native, widget, component, navigation
```

### Context Indicators (Low Weight)
```
mobile development, app development, smartphone, tablet, mobile ui
```

### File Type Triggers
```
pubspec.yaml, package.json (with react-native), .dart, .tsx (mobile),
android/, ios/, lib/, src/screens/, src/components/, app.json
```

---

## 📋 Workflow Integration

### Primary Workflows
- **[Mobile Development](../../rules/specialized/mobile-development.md)**: Cross-platform app development
- **[Flutter Development](../../rules/specialized/flutter-development.md)**: Flutter-specific workflows
- **[React Native Development](../../rules/specialized/react-native-development.md)**: RN-specific workflows

### Supporting Workflows
- **[Mobile UI/UX](../../rules/specialized/mobile-ui-ux.md)**: Mobile design patterns
- **[Mobile Testing](../../rules/specialized/mobile-testing.md)**: Testing strategies
- **[App Store Deployment](../../rules/specialized/app-store-deployment.md)**: Publishing workflows

---

## 🚀 Mobile Development Templates

### Flutter App Structure
```yaml
# pubspec.yaml
name: my_flutter_app
description: A new Flutter project.
publish_to: 'none'

version: 1.0.0+1

environment:
  sdk: '>=3.0.0 <4.0.0'
  flutter: ">=3.10.0"

dependencies:
  flutter:
    sdk: flutter
  
  # State Management
  flutter_riverpod: ^2.4.9
  
  # Navigation
  go_router: ^12.1.3
  
  # HTTP Client
  dio: ^5.4.0
  
  # Local Storage
  hive: ^2.2.3
  hive_flutter: ^1.1.0
  
  # UI Components
  flutter_svg: ^2.0.9
  cached_network_image: ^3.3.0
  
  # Utils
  intl: ^0.18.1
  uuid: ^4.2.1
  
  # Firebase
  firebase_core: ^2.24.2
  firebase_auth: ^4.15.3
  cloud_firestore: ^4.13.6
  firebase_messaging: ^14.7.10
  
  # Platform
  device_info_plus: ^9.1.1
  package_info_plus: ^4.2.0
  url_launcher: ^6.2.2
  
dev_dependencies:
  flutter_test:
    sdk: flutter
  
  flutter_lints: ^3.0.1
  build_runner: ^2.4.7
  hive_generator: ^2.0.1
  json_annotation: ^4.8.1
  json_serializable: ^6.7.1
  
flutter:
  uses-material-design: true
  
  assets:
    - assets/images/
    - assets/icons/
    - assets/fonts/
  
  fonts:
    - family: Inter
      fonts:
        - asset: assets/fonts/Inter-Regular.ttf
        - asset: assets/fonts/Inter-Medium.ttf
          weight: 500
        - asset: assets/fonts/Inter-SemiBold.ttf
          weight: 600
        - asset: assets/fonts/Inter-Bold.ttf
          weight: 700
```

```dart
// lib/main.dart
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:hive_flutter/hive_flutter.dart';

import 'core/config/app_config.dart';
import 'core/router/app_router.dart';
import 'core/theme/app_theme.dart';
import 'core/services/notification_service.dart';
import 'firebase_options.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  // Initialize Firebase
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  
  // Initialize Hive
  await Hive.initFlutter();
  
  // Initialize Notification Service
  await NotificationService.initialize();
  
  runApp(
    ProviderScope(
      child: MyApp(),
    ),
  );
}

class MyApp extends ConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final router = ref.watch(appRouterProvider);
    final themeMode = ref.watch(themeModeProvider);
    
    return MaterialApp.router(
      title: AppConfig.appName,
      debugShowCheckedModeBanner: false,
      
      // Theme
      theme: AppTheme.lightTheme,
      darkTheme: AppTheme.darkTheme,
      themeMode: themeMode,
      
      // Router
      routerConfig: router,
      
      // Localization
      supportedLocales: AppConfig.supportedLocales,
      
      // Builder for global configurations
      builder: (context, child) {
        return MediaQuery(
          data: MediaQuery.of(context).copyWith(
            textScaleFactor: 1.0, // Prevent system font scaling
          ),
          child: child!,
        );
      },
    );
  }
}
```

```dart
// lib/core/router/app_router.dart
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../features/auth/presentation/pages/login_page.dart';
import '../../features/auth/presentation/pages/register_page.dart';
import '../../features/home/presentation/pages/home_page.dart';
import '../../features/profile/presentation/pages/profile_page.dart';
import '../../features/settings/presentation/pages/settings_page.dart';
import '../providers/auth_provider.dart';
import 'app_routes.dart';

final appRouterProvider = Provider<GoRouter>((ref) {
  final authState = ref.watch(authStateProvider);
  
  return GoRouter(
    initialLocation: AppRoutes.home,
    redirect: (context, state) {
      final isLoggedIn = authState.when(
        data: (user) => user != null,
        loading: () => false,
        error: (_, __) => false,
      );
      
      final isAuthRoute = state.location.startsWith('/auth');
      
      // Redirect to login if not authenticated and not on auth route
      if (!isLoggedIn && !isAuthRoute) {
        return AppRoutes.login;
      }
      
      // Redirect to home if authenticated and on auth route
      if (isLoggedIn && isAuthRoute) {
        return AppRoutes.home;
      }
      
      return null;
    },
    routes: [
      // Auth Routes
      GoRoute(
        path: AppRoutes.login,
        name: 'login',
        builder: (context, state) => const LoginPage(),
      ),
      GoRoute(
        path: AppRoutes.register,
        name: 'register',
        builder: (context, state) => const RegisterPage(),
      ),
      
      // Main App Routes
      ShellRoute(
        builder: (context, state, child) {
          return MainShell(child: child);
        },
        routes: [
          GoRoute(
            path: AppRoutes.home,
            name: 'home',
            builder: (context, state) => const HomePage(),
          ),
          GoRoute(
            path: AppRoutes.profile,
            name: 'profile',
            builder: (context, state) => const ProfilePage(),
          ),
          GoRoute(
            path: AppRoutes.settings,
            name: 'settings',
            builder: (context, state) => const SettingsPage(),
          ),
        ],
      ),
    ],
    errorBuilder: (context, state) => Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(
              Icons.error_outline,
              size: 64,
              color: Colors.red,
            ),
            const SizedBox(height: 16),
            Text(
              'Page not found',
              style: Theme.of(context).textTheme.headlineSmall,
            ),
            const SizedBox(height: 8),
            Text(
              state.location,
              style: Theme.of(context).textTheme.bodyMedium,
            ),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: () => context.go(AppRoutes.home),
              child: const Text('Go Home'),
            ),
          ],
        ),
      ),
    ),
  );
});

class MainShell extends StatelessWidget {
  final Widget child;
  
  const MainShell({Key? key, required this.child}) : super(key: key);
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: child,
      bottomNavigationBar: const BottomNavBar(),
    );
  }
}

class BottomNavBar extends ConsumerWidget {
  const BottomNavBar({Key? key}) : super(key: key);
  
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final location = GoRouterState.of(context).location;
    
    int selectedIndex = 0;
    switch (location) {
      case AppRoutes.home:
        selectedIndex = 0;
        break;
      case AppRoutes.profile:
        selectedIndex = 1;
        break;
      case AppRoutes.settings:
        selectedIndex = 2;
        break;
    }
    
    return NavigationBar(
      selectedIndex: selectedIndex,
      onDestinationSelected: (index) {
        switch (index) {
          case 0:
            context.go(AppRoutes.home);
            break;
          case 1:
            context.go(AppRoutes.profile);
            break;
          case 2:
            context.go(AppRoutes.settings);
            break;
        }
      },
      destinations: const [
        NavigationDestination(
          icon: Icon(Icons.home_outlined),
          selectedIcon: Icon(Icons.home),
          label: 'Home',
        ),
        NavigationDestination(
          icon: Icon(Icons.person_outline),
          selectedIcon: Icon(Icons.person),
          label: 'Profile',
        ),
        NavigationDestination(
          icon: Icon(Icons.settings_outlined),
          selectedIcon: Icon(Icons.settings),
          label: 'Settings',
        ),
      ],
    );
  }
}
```

```dart
// lib/core/theme/app_theme.dart
import 'package:flutter/material.dart';

class AppTheme {
  static const Color primaryColor = Color(0xFF6366F1);
  static const Color secondaryColor = Color(0xFF8B5CF6);
  static const Color errorColor = Color(0xFFEF4444);
  static const Color successColor = Color(0xFF10B981);
  static const Color warningColor = Color(0xFFF59E0B);
  
  static ThemeData get lightTheme {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.light,
      colorScheme: ColorScheme.fromSeed(
        seedColor: primaryColor,
        brightness: Brightness.light,
      ),
      
      // App Bar Theme
      appBarTheme: const AppBarTheme(
        centerTitle: true,
        elevation: 0,
        scrolledUnderElevation: 1,
      ),
      
      // Card Theme
      cardTheme: CardTheme(
        elevation: 2,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
        ),
      ),
      
      // Input Decoration Theme
      inputDecorationTheme: InputDecorationTheme(
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
        ),
        contentPadding: const EdgeInsets.symmetric(
          horizontal: 16,
          vertical: 12,
        ),
      ),
      
      // Elevated Button Theme
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          padding: const EdgeInsets.symmetric(
            horizontal: 24,
            vertical: 12,
          ),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
        ),
      ),
      
      // Text Button Theme
      textButtonTheme: TextButtonThemeData(
        style: TextButton.styleFrom(
          padding: const EdgeInsets.symmetric(
            horizontal: 16,
            vertical: 8,
          ),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
        ),
      ),
      
      // Bottom Navigation Bar Theme
      bottomNavigationBarTheme: const BottomNavigationBarThemeData(
        type: BottomNavigationBarType.fixed,
        elevation: 8,
      ),
    );
  }
  
  static ThemeData get darkTheme {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.dark,
      colorScheme: ColorScheme.fromSeed(
        seedColor: primaryColor,
        brightness: Brightness.dark,
      ),
      
      // App Bar Theme
      appBarTheme: const AppBarTheme(
        centerTitle: true,
        elevation: 0,
        scrolledUnderElevation: 1,
      ),
      
      // Card Theme
      cardTheme: CardTheme(
        elevation: 2,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
        ),
      ),
      
      // Input Decoration Theme
      inputDecorationTheme: InputDecorationTheme(
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
        ),
        contentPadding: const EdgeInsets.symmetric(
          horizontal: 16,
          vertical: 12,
        ),
      ),
      
      // Elevated Button Theme
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          padding: const EdgeInsets.symmetric(
            horizontal: 24,
            vertical: 12,
          ),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
        ),
      ),
      
      // Text Button Theme
      textButtonTheme: TextButtonThemeData(
        style: TextButton.styleFrom(
          padding: const EdgeInsets.symmetric(
            horizontal: 16,
            vertical: 8,
          ),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
        ),
      ),
      
      // Bottom Navigation Bar Theme
      bottomNavigationBarTheme: const BottomNavigationBarThemeData(
        type: BottomNavigationBarType.fixed,
        elevation: 8,
      ),
    );
  }
}

// Theme Mode Provider
final themeModeProvider = StateNotifierProvider<ThemeModeNotifier, ThemeMode>(
  (ref) => ThemeModeNotifier(),
);

class ThemeModeNotifier extends StateNotifier<ThemeMode> {
  ThemeModeNotifier() : super(ThemeMode.system);
  
  void setThemeMode(ThemeMode mode) {
    state = mode;
  }
  
  void toggleTheme() {
    state = state == ThemeMode.light ? ThemeMode.dark : ThemeMode.light;
  }
}
```

### React Native App Structure
```json
{
  "name": "MyReactNativeApp",
  "version": "1.0.0",
  "private": true,
  "scripts": {
    "android": "react-native run-android",
    "ios": "react-native run-ios",
    "start": "react-native start",
    "test": "jest",
    "lint": "eslint . --ext .js,.jsx,.ts,.tsx",
    "type-check": "tsc --noEmit"
  },
  "dependencies": {
    "react": "18.2.0",
    "react-native": "0.72.6",
    
    "@react-navigation/native": "^6.1.9",
    "@react-navigation/stack": "^6.3.20",
    "@react-navigation/bottom-tabs": "^6.5.11",
    "react-native-screens": "^3.27.0",
    "react-native-safe-area-context": "^4.7.4",
    
    "@reduxjs/toolkit": "^1.9.7",
    "react-redux": "^8.1.3",
    "redux-persist": "^6.0.0",
    
    "@react-native-async-storage/async-storage": "^1.19.5",
    "react-native-keychain": "^8.1.3",
    
    "axios": "^1.6.2",
    "react-query": "^3.39.3",
    
    "react-native-vector-icons": "^10.0.2",
    "react-native-svg": "^13.14.0",
    "react-native-fast-image": "^8.6.3",
    
    "@react-native-firebase/app": "^18.6.1",
    "@react-native-firebase/auth": "^18.6.1",
    "@react-native-firebase/firestore": "^18.6.1",
    "@react-native-firebase/messaging": "^18.6.1",
    
    "react-native-device-info": "^10.11.0",
    "react-native-permissions": "^3.10.1",
    "react-native-image-picker": "^7.0.3"
  },
  "devDependencies": {
    "@babel/core": "^7.20.0",
    "@babel/preset-env": "^7.20.0",
    "@babel/runtime": "^7.20.0",
    "@react-native/eslint-config": "^0.72.2",
    "@react-native/metro-config": "^0.72.11",
    "@tsconfig/react-native": "^3.0.0",
    "@types/react": "^18.0.24",
    "@types/react-test-renderer": "^18.0.0",
    "babel-jest": "^29.2.1",
    "eslint": "^8.19.0",
    "jest": "^29.2.1",
    "metro-react-native-babel-preset": "0.76.8",
    "prettier": "^2.4.1",
    "react-test-renderer": "18.2.0",
    "typescript": "4.8.4"
  },
  "engines": {
    "node": ">=16"
  }
}
```

```typescript
// src/App.tsx
import React from 'react';
import { StatusBar } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { Provider } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';
import { QueryClient, QueryClientProvider } from 'react-query';

import { store, persistor } from './store';
import { AppNavigator } from './navigation/AppNavigator';
import { LoadingScreen } from './components/LoadingScreen';
import { ErrorBoundary } from './components/ErrorBoundary';
import { ThemeProvider } from './contexts/ThemeContext';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 2,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
});

const App: React.FC = () => {
  return (
    <ErrorBoundary>
      <Provider store={store}>
        <PersistGate loading={<LoadingScreen />} persistor={persistor}>
          <QueryClientProvider client={queryClient}>
            <ThemeProvider>
              <NavigationContainer>
                <StatusBar
                  barStyle="dark-content"
                  backgroundColor="transparent"
                  translucent
                />
                <AppNavigator />
              </NavigationContainer>
            </ThemeProvider>
          </QueryClientProvider>
        </PersistGate>
      </Provider>
    </ErrorBoundary>
  );
};

export default App;
```

```typescript
// src/navigation/AppNavigator.tsx
import React from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import { useSelector } from 'react-redux';

import { RootState } from '../store';
import { AuthNavigator } from './AuthNavigator';
import { MainNavigator } from './MainNavigator';
import { LoadingScreen } from '../components/LoadingScreen';

export type RootStackParamList = {
  Auth: undefined;
  Main: undefined;
};

const Stack = createStackNavigator<RootStackParamList>();

export const AppNavigator: React.FC = () => {
  const { isAuthenticated, isLoading } = useSelector(
    (state: RootState) => state.auth
  );

  if (isLoading) {
    return <LoadingScreen />;
  }

  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      {isAuthenticated ? (
        <Stack.Screen name="Main" component={MainNavigator} />
      ) : (
        <Stack.Screen name="Auth" component={AuthNavigator} />
      )}
    </Stack.Navigator>
  );
};
```

```typescript
// src/navigation/MainNavigator.tsx
import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import Icon from 'react-native-vector-icons/Ionicons';

import { HomeScreen } from '../screens/HomeScreen';
import { ProfileScreen } from '../screens/ProfileScreen';
import { SettingsScreen } from '../screens/SettingsScreen';
import { useTheme } from '../contexts/ThemeContext';

export type MainTabParamList = {
  Home: undefined;
  Profile: undefined;
  Settings: undefined;
};

const Tab = createBottomTabNavigator<MainTabParamList>();

export const MainNavigator: React.FC = () => {
  const { colors } = useTheme();

  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        tabBarIcon: ({ focused, color, size }) => {
          let iconName: string;

          switch (route.name) {
            case 'Home':
              iconName = focused ? 'home' : 'home-outline';
              break;
            case 'Profile':
              iconName = focused ? 'person' : 'person-outline';
              break;
            case 'Settings':
              iconName = focused ? 'settings' : 'settings-outline';
              break;
            default:
              iconName = 'help-outline';
          }

          return <Icon name={iconName} size={size} color={color} />;
        },
        tabBarActiveTintColor: colors.primary,
        tabBarInactiveTintColor: colors.textSecondary,
        tabBarStyle: {
          backgroundColor: colors.background,
          borderTopColor: colors.border,
        },
        headerStyle: {
          backgroundColor: colors.background,
        },
        headerTintColor: colors.text,
      })}
    >
      <Tab.Screen
        name="Home"
        component={HomeScreen}
        options={{ title: 'Home' }}
      />
      <Tab.Screen
        name="Profile"
        component={ProfileScreen}
        options={{ title: 'Profile' }}
      />
      <Tab.Screen
        name="Settings"
        component={SettingsScreen}
        options={{ title: 'Settings' }}
      />
    </Tab.Navigator>
  );
};
```

### Mobile UI Components
```dart
// lib/shared/widgets/custom_button.dart
import 'package:flutter/material.dart';

enum ButtonVariant { primary, secondary, outline, ghost }
enum ButtonSize { small, medium, large }

class CustomButton extends StatelessWidget {
  final String text;
  final VoidCallback? onPressed;
  final ButtonVariant variant;
  final ButtonSize size;
  final bool isLoading;
  final bool isDisabled;
  final Widget? icon;
  final double? width;
  
  const CustomButton({
    Key? key,
    required this.text,
    this.onPressed,
    this.variant = ButtonVariant.primary,
    this.size = ButtonSize.medium,
    this.isLoading = false,
    this.isDisabled = false,
    this.icon,
    this.width,
  }) : super(key: key);
  
  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final colorScheme = theme.colorScheme;
    
    // Size configurations
    double height;
    EdgeInsets padding;
    double fontSize;
    
    switch (size) {
      case ButtonSize.small:
        height = 32;
        padding = const EdgeInsets.symmetric(horizontal: 12, vertical: 6);
        fontSize = 14;
        break;
      case ButtonSize.medium:
        height = 40;
        padding = const EdgeInsets.symmetric(horizontal: 16, vertical: 8);
        fontSize = 16;
        break;
      case ButtonSize.large:
        height = 48;
        padding = const EdgeInsets.symmetric(horizontal: 20, vertical: 12);
        fontSize = 18;
        break;
    }
    
    // Variant configurations
    Color backgroundColor;
    Color foregroundColor;
    Color? borderColor;
    
    switch (variant) {
      case ButtonVariant.primary:
        backgroundColor = colorScheme.primary;
        foregroundColor = colorScheme.onPrimary;
        borderColor = null;
        break;
      case ButtonVariant.secondary:
        backgroundColor = colorScheme.secondary;
        foregroundColor = colorScheme.onSecondary;
        borderColor = null;
        break;
      case ButtonVariant.outline:
        backgroundColor = Colors.transparent;
        foregroundColor = colorScheme.primary;
        borderColor = colorScheme.primary;
        break;
      case ButtonVariant.ghost:
        backgroundColor = Colors.transparent;
        foregroundColor = colorScheme.primary;
        borderColor = null;
        break;
    }
    
    final isEnabled = !isDisabled && !isLoading && onPressed != null;
    
    return SizedBox(
      width: width,
      height: height,
      child: ElevatedButton(
        onPressed: isEnabled ? onPressed : null,
        style: ElevatedButton.styleFrom(
          backgroundColor: backgroundColor,
          foregroundColor: foregroundColor,
          padding: padding,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
            side: borderColor != null
                ? BorderSide(color: borderColor, width: 1)
                : BorderSide.none,
          ),
          elevation: variant == ButtonVariant.outline || variant == ButtonVariant.ghost ? 0 : 2,
        ),
        child: isLoading
            ? SizedBox(
                width: 20,
                height: 20,
                child: CircularProgressIndicator(
                  strokeWidth: 2,
                  valueColor: AlwaysStoppedAnimation<Color>(foregroundColor),
                ),
              )
            : Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  if (icon != null) ..[
                    icon!,
                    const SizedBox(width: 8),
                  ],
                  Text(
                    text,
                    style: TextStyle(
                      fontSize: fontSize,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ],
              ),
      ),
    );
  }
}
```

```typescript
// src/components/CustomButton.tsx
import React from 'react';
import {
  TouchableOpacity,
  Text,
  ActivityIndicator,
  StyleSheet,
  ViewStyle,
  TextStyle,
} from 'react-native';
import { useTheme } from '../contexts/ThemeContext';

type ButtonVariant = 'primary' | 'secondary' | 'outline' | 'ghost';
type ButtonSize = 'small' | 'medium' | 'large';

interface CustomButtonProps {
  title: string;
  onPress?: () => void;
  variant?: ButtonVariant;
  size?: ButtonSize;
  isLoading?: boolean;
  disabled?: boolean;
  style?: ViewStyle;
  textStyle?: TextStyle;
}

export const CustomButton: React.FC<CustomButtonProps> = ({
  title,
  onPress,
  variant = 'primary',
  size = 'medium',
  isLoading = false,
  disabled = false,
  style,
  textStyle,
}) => {
  const { colors } = useTheme();

  const getButtonStyle = (): ViewStyle => {
    const baseStyle: ViewStyle = {
      borderRadius: 8,
      alignItems: 'center',
      justifyContent: 'center',
      flexDirection: 'row',
    };

    // Size styles
    switch (size) {
      case 'small':
        baseStyle.paddingHorizontal = 12;
        baseStyle.paddingVertical = 6;
        baseStyle.minHeight = 32;
        break;
      case 'medium':
        baseStyle.paddingHorizontal = 16;
        baseStyle.paddingVertical = 8;
        baseStyle.minHeight = 40;
        break;
      case 'large':
        baseStyle.paddingHorizontal = 20;
        baseStyle.paddingVertical = 12;
        baseStyle.minHeight = 48;
        break;
    }

    // Variant styles
    switch (variant) {
      case 'primary':
        baseStyle.backgroundColor = colors.primary;
        break;
      case 'secondary':
        baseStyle.backgroundColor = colors.secondary;
        break;
      case 'outline':
        baseStyle.backgroundColor = 'transparent';
        baseStyle.borderWidth = 1;
        baseStyle.borderColor = colors.primary;
        break;
      case 'ghost':
        baseStyle.backgroundColor = 'transparent';
        break;
    }

    // Disabled state
    if (disabled || isLoading) {
      baseStyle.opacity = 0.6;
    }

    return baseStyle;
  };

  const getTextStyle = (): TextStyle => {
    const baseTextStyle: TextStyle = {
      fontWeight: '600',
    };

    // Size styles
    switch (size) {
      case 'small':
        baseTextStyle.fontSize = 14;
        break;
      case 'medium':
        baseTextStyle.fontSize = 16;
        break;
      case 'large':
        baseTextStyle.fontSize = 18;
        break;
    }

    // Variant styles
    switch (variant) {
      case 'primary':
      case 'secondary':
        baseTextStyle.color = colors.white;
        break;
      case 'outline':
      case 'ghost':
        baseTextStyle.color = colors.primary;
        break;
    }

    return baseTextStyle;
  };

  const handlePress = () => {
    if (!disabled && !isLoading && onPress) {
      onPress();
    }
  };

  return (
    <TouchableOpacity
      style={[getButtonStyle(), style]}
      onPress={handlePress}
      activeOpacity={0.8}
      disabled={disabled || isLoading}
    >
      {isLoading ? (
        <ActivityIndicator
          size="small"
          color={variant === 'primary' || variant === 'secondary' ? colors.white : colors.primary}
        />
      ) : (
        <Text style={[getTextStyle(), textStyle]}>{title}</Text>
      )}
    </TouchableOpacity>
  );
};
```

---

## 🎯 Task Specializations

### High Confidence Tasks (>90%)
- Cross-platform mobile app development
- Flutter widget development and state management
- React Native component development
- Mobile UI/UX implementation
- Navigation and routing setup
- Local storage and data persistence
- API integration and data fetching

### Medium Confidence Tasks (75-90%)
- Native module development
- Complex animations and gestures
- Camera and media handling
- Push notifications implementation
- Offline functionality and sync
- Performance optimization
- App store deployment

### Collaborative Tasks (<75%)
- Backend API development (with Backend Agent)
- Complex UI/UX design (with UI/UX Agent)
- Advanced security implementations (with Security Agent)
- CI/CD pipeline setup (with DevOps Agent)

---

## 🔄 Agent Handoff Protocols

### Escalation Triggers
- Backend API development requirements
- Complex UI/UX design needs
- Advanced security and authentication
- Infrastructure and deployment automation
- Performance issues requiring specialized optimization

### Handoff Procedures
1. **Mobile App Documentation**: Complete app architecture and component documentation
2. **API Specifications**: Detailed API requirements and integration points
3. **Design Assets**: UI/UX specifications and design system requirements
4. **Testing Coverage**: Mobile-specific test cases and scenarios

---

## 📊 Quality Assurance

### Code Standards
- **Architecture**: Clean architecture with proper separation of concerns
- **State Management**: Consistent state management patterns
- **Performance**: Optimized rendering and memory usage
- **Accessibility**: Mobile accessibility guidelines compliance

### Mobile Standards
- **Platform Guidelines**: iOS HIG and Android Material Design
- **Responsive Design**: Adaptive layouts for different screen sizes
- **Offline Support**: Graceful offline functionality
- **Security**: Secure data storage and transmission

### Process Standards
- **Testing**: Unit, widget, and integration testing
- **Code Review**: Platform-specific best practices
- **Documentation**: Component documentation and usage examples
- **Deployment**: App store guidelines and submission process

---

## 🛠️ Mobile Development Tools Integration

### Flutter Development
- **Flutter SDK**: Latest stable version
- **Dart**: Language and ecosystem
- **Flutter DevTools**: Debugging and profiling
- **Flutter Inspector**: Widget tree inspection

### React Native Development
- **React Native CLI**: Project management
- **Metro**: JavaScript bundler
- **Flipper**: Debugging platform
- **React Native Debugger**: Development tools

### Testing Tools
- **Flutter**: flutter_test, integration_test, mockito
- **React Native**: Jest, Detox, React Native Testing Library
- **Device Testing**: iOS Simulator, Android Emulator, Physical devices

### Deployment Tools
- **Fastlane**: Automated deployment
- **App Center**: Distribution and analytics
- **Firebase**: Backend services and analytics
- **Crashlytics**: Crash reporting

---

## 🚀 Mobile Development Best Practices

### Performance Optimization
- **Lazy Loading**: Load content on demand
- **Image Optimization**: Proper image caching and compression
- **Memory Management**: Efficient memory usage patterns
- **Bundle Size**: Minimize app size and startup time

### User Experience
- **Responsive Design**: Adaptive layouts for all screen sizes
- **Smooth Animations**: 60fps animations and transitions
- **Offline Support**: Graceful degradation without network
- **Loading States**: Clear feedback for async operations

### Security Best Practices
- **Secure Storage**: Encrypted local storage for sensitive data
- **Network Security**: HTTPS and certificate pinning
- **Authentication**: Secure token management
- **Code Obfuscation**: Protect against reverse engineering

---

## 🚀 Continuous Improvement

### Learning Priorities
- Latest Flutter and React Native updates
- Mobile platform guidelines and best practices
- Performance optimization techniques
- Mobile security and privacy standards
- Emerging mobile technologies and trends

### Feedback Integration
- App store reviews and user feedback
- Performance metrics and crash reports
- Code review feedback and best practices
- Platform updates and migration guides
- Industry trends and emerging patterns

---

**📱 Comprehensive mobile development expertise with focus on cross-platform solutions, native performance, and exceptional user experience.**