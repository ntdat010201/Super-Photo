# 🌐 Frontend Development Rules - Universal Standards

> **⚡ Comprehensive frontend development guidelines for modern web applications**  
> React/Vue/Angular with TypeScript, responsive design, and performance optimization

---

## 🎯 Core Technology Stack

### Primary Frameworks (Choose One)
```json
// React + TypeScript
{
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "typescript": "^5.0.0",
    "@types/react": "^18.2.0",
    "@types/react-dom": "^18.2.0"
  }
}

// Vue 3 + TypeScript
{
  "dependencies": {
    "vue": "^3.3.0",
    "typescript": "^5.0.0",
    "@vue/typescript": "^1.8.0"
  }
}

// Angular + TypeScript
{
  "dependencies": {
    "@angular/core": "^16.0.0",
    "@angular/common": "^16.0.0",
    "typescript": "^5.0.0"
  }
}
```

### Essential Dependencies
```json
{
  "dependencies": {
    // Routing
    "react-router-dom": "^6.8.0", // React
    "vue-router": "^4.2.0", // Vue
    "@angular/router": "^16.0.0", // Angular
    
    // State Management
    "zustand": "^4.3.0", // React (lightweight)
    "@reduxjs/toolkit": "^1.9.0", // React (complex apps)
    "pinia": "^2.1.0", // Vue
    "@ngrx/store": "^16.0.0", // Angular
    
    // HTTP Client
    "axios": "^1.4.0",
    "@tanstack/react-query": "^4.29.0", // React
    "@vue/apollo-composable": "^4.0.0", // Vue
    "@angular/common/http": "^16.0.0", // Angular
    
    // UI Components
    "@headlessui/react": "^1.7.0", // React
    "@headlessui/vue": "^1.7.0", // Vue
    "@angular/material": "^16.0.0", // Angular
    
    // Styling
    "tailwindcss": "^3.3.0",
    "@emotion/react": "^11.11.0", // React CSS-in-JS
    "styled-components": "^6.0.0", // React CSS-in-JS
    
    // Forms
    "react-hook-form": "^7.43.0", // React
    "vee-validate": "^4.9.0", // Vue
    "@angular/reactive-forms": "^16.0.0", // Angular
    
    // Utilities
    "date-fns": "^2.30.0",
    "lodash-es": "^4.17.21",
    "clsx": "^1.2.1",
    "uuid": "^9.0.0"
  },
  "devDependencies": {
    // Build Tools
    "vite": "^4.3.0",
    "webpack": "^5.88.0",
    
    // Testing
    "vitest": "^0.32.0",
    "@testing-library/react": "^13.4.0", // React
    "@testing-library/vue": "^7.0.0", // Vue
    "@testing-library/jest-dom": "^5.16.0",
    "cypress": "^12.17.0",
    
    // Code Quality
    "eslint": "^8.43.0",
    "prettier": "^2.8.0",
    "@typescript-eslint/eslint-plugin": "^5.60.0",
    "@typescript-eslint/parser": "^5.60.0"
  }
}
```

---

## 📁 Standard Project Structure

### React Project Structure
```
src/
├── components/           # Reusable UI components
│   ├── ui/              # Basic UI components (Button, Input, etc.)
│   │   ├── Button/
│   │   │   ├── Button.tsx
│   │   │   ├── Button.test.tsx
│   │   │   ├── Button.stories.tsx
│   │   │   └── index.ts
│   │   └── Input/
│   ├── layout/          # Layout components
│   │   ├── Header/
│   │   ├── Footer/
│   │   ├── Sidebar/
│   │   └── Layout/
│   └── common/          # Common components
│       ├── Loading/
│       ├── ErrorBoundary/
│       └── Modal/
├── features/            # Feature-based modules
│   ├── auth/           # Authentication feature
│   │   ├── components/ # Feature-specific components
│   │   ├── hooks/      # Feature-specific hooks
│   │   ├── services/   # API services
│   │   ├── stores/     # State management
│   │   ├── types/      # TypeScript types
│   │   └── utils/      # Feature utilities
│   ├── dashboard/
│   └── profile/
├── hooks/              # Shared custom hooks
│   ├── useApi.ts
│   ├── useLocalStorage.ts
│   └── useDebounce.ts
├── services/           # API and external services
│   ├── api/
│   │   ├── client.ts   # HTTP client configuration
│   │   ├── endpoints.ts # API endpoints
│   │   └── types.ts    # API response types
│   ├── auth/
│   └── storage/
├── stores/             # Global state management
│   ├── authStore.ts
│   ├── userStore.ts
│   └── index.ts
├── styles/             # Global styles and themes
│   ├── globals.css
│   ├── variables.css
│   └── components.css
├── types/              # Global TypeScript types
│   ├── api.ts
│   ├── user.ts
│   └── common.ts
├── utils/              # Utility functions
│   ├── constants.ts
│   ├── helpers.ts
│   ├── formatters.ts
│   └── validators.ts
├── assets/             # Static assets
│   ├── images/
│   ├── icons/
│   └── fonts/
├── App.tsx             # Main App component
├── main.tsx            # Application entry point
└── vite-env.d.ts       # Vite type definitions
```

### Vue Project Structure
```
src/
├── components/         # Reusable components
│   ├── base/          # Base UI components
│   ├── layout/        # Layout components
│   └── common/        # Common components
├── views/             # Page components
│   ├── Home.vue
│   ├── About.vue
│   └── auth/
├── composables/       # Vue 3 composables
│   ├── useApi.ts
│   ├── useAuth.ts
│   └── useLocalStorage.ts
├── stores/            # Pinia stores
│   ├── auth.ts
│   ├── user.ts
│   └── index.ts
├── services/          # API services
├── types/             # TypeScript types
├── utils/             # Utility functions
├── assets/            # Static assets
├── router/            # Vue Router configuration
│   └── index.ts
├── App.vue            # Root component
└── main.ts            # Application entry point
```

### Angular Project Structure
```
src/
├── app/
│   ├── core/          # Core module (singleton services)
│   │   ├── guards/
│   │   ├── interceptors/
│   │   ├── services/
│   │   └── core.module.ts
│   ├── shared/        # Shared module (reusable components)
│   │   ├── components/
│   │   ├── directives/
│   │   ├── pipes/
│   │   └── shared.module.ts
│   ├── features/      # Feature modules
│   │   ├── auth/
│   │   ├── dashboard/
│   │   └── profile/
│   ├── layout/        # Layout components
│   ├── app-routing.module.ts
│   ├── app.component.ts
│   └── app.module.ts
├── assets/            # Static assets
├── environments/      # Environment configurations
└── styles/            # Global styles
```

---

## 🎨 Component Development Standards

### React Component Template
```tsx
// components/ui/Button/Button.tsx
import React from 'react';
import { clsx } from 'clsx';
import { ButtonProps } from './Button.types';
import styles from './Button.module.css';

export const Button: React.FC<ButtonProps> = ({
  children,
  variant = 'primary',
  size = 'medium',
  disabled = false,
  loading = false,
  onClick,
  className,
  ...props
}) => {
  const buttonClasses = clsx(
    styles.button,
    styles[variant],
    styles[size],
    {
      [styles.disabled]: disabled,
      [styles.loading]: loading,
    },
    className
  );

  return (
    <button
      className={buttonClasses}
      disabled={disabled || loading}
      onClick={onClick}
      {...props}
    >
      {loading && <span className={styles.spinner} />}
      {children}
    </button>
  );
};

// components/ui/Button/Button.types.ts
export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost';
  size?: 'small' | 'medium' | 'large';
  loading?: boolean;
  children: React.ReactNode;
}

// components/ui/Button/Button.test.tsx
import { render, screen, fireEvent } from '@testing-library/react';
import { Button } from './Button';

describe('Button', () => {
  it('renders correctly', () => {
    render(<Button>Click me</Button>);
    expect(screen.getByRole('button')).toBeInTheDocument();
  });

  it('calls onClick when clicked', () => {
    const handleClick = jest.fn();
    render(<Button onClick={handleClick}>Click me</Button>);
    fireEvent.click(screen.getByRole('button'));
    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  it('is disabled when loading', () => {
    render(<Button loading>Click me</Button>);
    expect(screen.getByRole('button')).toBeDisabled();
  });
});

// components/ui/Button/index.ts
export { Button } from './Button';
export type { ButtonProps } from './Button.types';
```

### Vue Component Template
```vue
<!-- components/base/BaseButton.vue -->
<template>
  <button
    :class="buttonClasses"
    :disabled="disabled || loading"
    @click="handleClick"
    v-bind="$attrs"
  >
    <span v-if="loading" class="spinner" />
    <slot />
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue';

interface Props {
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost';
  size?: 'small' | 'medium' | 'large';
  disabled?: boolean;
  loading?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  variant: 'primary',
  size: 'medium',
  disabled: false,
  loading: false,
});

const emit = defineEmits<{
  click: [event: MouseEvent];
}>();

const buttonClasses = computed(() => [
  'btn',
  `btn--${props.variant}`,
  `btn--${props.size}`,
  {
    'btn--disabled': props.disabled,
    'btn--loading': props.loading,
  },
]);

const handleClick = (event: MouseEvent) => {
  if (!props.disabled && !props.loading) {
    emit('click', event);
  }
};
</script>

<style scoped>
.btn {
  @apply px-4 py-2 rounded-md font-medium transition-colors;
}

.btn--primary {
  @apply bg-blue-600 text-white hover:bg-blue-700;
}

.btn--secondary {
  @apply bg-gray-600 text-white hover:bg-gray-700;
}

.btn--outline {
  @apply border border-blue-600 text-blue-600 hover:bg-blue-50;
}

.btn--small {
  @apply px-3 py-1 text-sm;
}

.btn--large {
  @apply px-6 py-3 text-lg;
}

.btn--disabled {
  @apply opacity-50 cursor-not-allowed;
}

.spinner {
  @apply inline-block w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin mr-2;
}
</style>
```

### Angular Component Template
```typescript
// shared/components/button/button.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';

export type ButtonVariant = 'primary' | 'secondary' | 'outline' | 'ghost';
export type ButtonSize = 'small' | 'medium' | 'large';

@Component({
  selector: 'app-button',
  template: `
    <button
      [class]="buttonClasses"
      [disabled]="disabled || loading"
      (click)="handleClick($event)"
    >
      <mat-spinner *ngIf="loading" diameter="16"></mat-spinner>
      <ng-content></ng-content>
    </button>
  `,
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent {
  @Input() variant: ButtonVariant = 'primary';
  @Input() size: ButtonSize = 'medium';
  @Input() disabled = false;
  @Input() loading = false;
  @Output() clicked = new EventEmitter<MouseEvent>();

  get buttonClasses(): string {
    return [
      'btn',
      `btn--${this.variant}`,
      `btn--${this.size}`,
      this.disabled ? 'btn--disabled' : '',
      this.loading ? 'btn--loading' : ''
    ].filter(Boolean).join(' ');
  }

  handleClick(event: MouseEvent): void {
    if (!this.disabled && !this.loading) {
      this.clicked.emit(event);
    }
  }
}

// button.component.spec.ts
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ButtonComponent } from './button.component';

describe('ButtonComponent', () => {
  let component: ButtonComponent;
  let fixture: ComponentFixture<ButtonComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ButtonComponent]
    });
    fixture = TestBed.createComponent(ButtonComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should emit click event', () => {
    spyOn(component.clicked, 'emit');
    const button = fixture.nativeElement.querySelector('button');
    button.click();
    expect(component.clicked.emit).toHaveBeenCalled();
  });
});
```

---

## 🔄 State Management Patterns

### React with Zustand
```typescript
// stores/authStore.ts
import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';
import { AuthService } from '../services/auth';

interface User {
  id: string;
  email: string;
  name: string;
}

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
  
  // Actions
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  clearError: () => void;
  checkAuth: () => Promise<void>;
}

export const useAuthStore = create<AuthState>()()
  devtools(
    persist(
      (set, get) => ({
        user: null,
        isAuthenticated: false,
        isLoading: false,
        error: null,

        login: async (email: string, password: string) => {
          set({ isLoading: true, error: null });
          try {
            const { user, token } = await AuthService.login(email, password);
            localStorage.setItem('token', token);
            set({ user, isAuthenticated: true, isLoading: false });
          } catch (error) {
            set({ 
              error: error instanceof Error ? error.message : 'Login failed',
              isLoading: false 
            });
          }
        },

        logout: () => {
          localStorage.removeItem('token');
          set({ user: null, isAuthenticated: false });
        },

        clearError: () => set({ error: null }),

        checkAuth: async () => {
          const token = localStorage.getItem('token');
          if (!token) return;

          set({ isLoading: true });
          try {
            const user = await AuthService.verifyToken(token);
            set({ user, isAuthenticated: true, isLoading: false });
          } catch {
            localStorage.removeItem('token');
            set({ user: null, isAuthenticated: false, isLoading: false });
          }
        },
      }),
      {
        name: 'auth-storage',
        partialize: (state) => ({ user: state.user, isAuthenticated: state.isAuthenticated }),
      }
    )
  )
);

// Usage in component
import { useAuthStore } from '../stores/authStore';

const LoginForm: React.FC = () => {
  const { login, isLoading, error, clearError } = useAuthStore();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await login(email, password);
  };

  return (
    <form onSubmit={handleSubmit}>
      {error && (
        <div className="error">
          {error}
          <button onClick={clearError}>×</button>
        </div>
      )}
      {/* Form fields */}
    </form>
  );
};
```

### Vue with Pinia
```typescript
// stores/auth.ts
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { AuthService } from '../services/auth';

export const useAuthStore = defineStore('auth', () => {
  // State
  const user = ref<User | null>(null);
  const isLoading = ref(false);
  const error = ref<string | null>(null);

  // Getters
  const isAuthenticated = computed(() => !!user.value);

  // Actions
  const login = async (email: string, password: string) => {
    isLoading.value = true;
    error.value = null;
    
    try {
      const response = await AuthService.login(email, password);
      user.value = response.user;
      localStorage.setItem('token', response.token);
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Login failed';
    } finally {
      isLoading.value = false;
    }
  };

  const logout = () => {
    user.value = null;
    localStorage.removeItem('token');
  };

  const clearError = () => {
    error.value = null;
  };

  return {
    // State
    user,
    isLoading,
    error,
    
    // Getters
    isAuthenticated,
    
    // Actions
    login,
    logout,
    clearError,
  };
}, {
  persist: {
    paths: ['user'],
  },
});

// Usage in component
<script setup lang="ts">
import { useAuthStore } from '../stores/auth';

const authStore = useAuthStore();
const { login, isLoading, error, clearError } = authStore;

const handleLogin = async (email: string, password: string) => {
  await login(email, password);
};
</script>
```

---

## 🌐 API Integration Patterns

### React with React Query
```typescript
// services/api/client.ts
import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:3000/api';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// hooks/useUsers.ts
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { apiClient } from '../services/api/client';

interface User {
  id: string;
  name: string;
  email: string;
}

const userKeys = {
  all: ['users'] as const,
  lists: () => [...userKeys.all, 'list'] as const,
  list: (filters: string) => [...userKeys.lists(), { filters }] as const,
  details: () => [...userKeys.all, 'detail'] as const,
  detail: (id: string) => [...userKeys.details(), id] as const,
};

export const useUsers = (filters?: string) => {
  return useQuery({
    queryKey: userKeys.list(filters || ''),
    queryFn: async () => {
      const { data } = await apiClient.get<User[]>('/users', {
        params: { filters },
      });
      return data;
    },
    staleTime: 5 * 60 * 1000, // 5 minutes
  });
};

export const useUser = (id: string) => {
  return useQuery({
    queryKey: userKeys.detail(id),
    queryFn: async () => {
      const { data } = await apiClient.get<User>(`/users/${id}`);
      return data;
    },
    enabled: !!id,
  });
};

export const useCreateUser = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: async (userData: Omit<User, 'id'>) => {
      const { data } = await apiClient.post<User>('/users', userData);
      return data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: userKeys.lists() });
    },
  });
};

export const useUpdateUser = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: async ({ id, ...userData }: User) => {
      const { data } = await apiClient.put<User>(`/users/${id}`, userData);
      return data;
    },
    onSuccess: (data) => {
      queryClient.setQueryData(userKeys.detail(data.id), data);
      queryClient.invalidateQueries({ queryKey: userKeys.lists() });
    },
  });
};

// Usage in component
const UserList: React.FC = () => {
  const { data: users, isLoading, error } = useUsers();
  const createUser = useCreateUser();

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <div>
      {users?.map(user => (
        <div key={user.id}>{user.name}</div>
      ))}
    </div>
  );
};
```

---

## 🎨 Styling Standards

### Tailwind CSS Configuration
```javascript
// tailwind.config.js
module.exports = {
  content: [
    './index.html',
    './src/**/*.{js,ts,jsx,tsx,vue}',
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#eff6ff',
          500: '#3b82f6',
          600: '#2563eb',
          700: '#1d4ed8',
          900: '#1e3a8a',
        },
        gray: {
          50: '#f9fafb',
          100: '#f3f4f6',
          200: '#e5e7eb',
          300: '#d1d5db',
          400: '#9ca3af',
          500: '#6b7280',
          600: '#4b5563',
          700: '#374151',
          800: '#1f2937',
          900: '#111827',
        },
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
      spacing: {
        '18': '4.5rem',
        '88': '22rem',
      },
      animation: {
        'fade-in': 'fadeIn 0.5s ease-in-out',
        'slide-up': 'slideUp 0.3s ease-out',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        slideUp: {
          '0%': { transform: 'translateY(10px)', opacity: '0' },
          '100%': { transform: 'translateY(0)', opacity: '1' },
        },
      },
    },
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/typography'),
    require('@tailwindcss/aspect-ratio'),
  ],
};
```

### CSS Modules Pattern
```css
/* Button.module.css */
.button {
  @apply px-4 py-2 rounded-md font-medium transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2;
}

.primary {
  @apply bg-primary-600 text-white hover:bg-primary-700 focus:ring-primary-500;
}

.secondary {
  @apply bg-gray-600 text-white hover:bg-gray-700 focus:ring-gray-500;
}

.outline {
  @apply border border-primary-600 text-primary-600 hover:bg-primary-50 focus:ring-primary-500;
}

.small {
  @apply px-3 py-1 text-sm;
}

.large {
  @apply px-6 py-3 text-lg;
}

.disabled {
  @apply opacity-50 cursor-not-allowed;
}

.loading {
  @apply cursor-wait;
}

.spinner {
  @apply inline-block w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin mr-2;
}
```

---

## 📱 Responsive Design Standards

### Breakpoint System
```css
/* styles/breakpoints.css */
:root {
  --breakpoint-sm: 640px;
  --breakpoint-md: 768px;
  --breakpoint-lg: 1024px;
  --breakpoint-xl: 1280px;
  --breakpoint-2xl: 1536px;
}

/* Mobile-first approach */
.container {
  width: 100%;
  padding: 0 1rem;
}

@media (min-width: 640px) {
  .container {
    max-width: 640px;
    margin: 0 auto;
  }
}

@media (min-width: 768px) {
  .container {
    max-width: 768px;
    padding: 0 2rem;
  }
}

@media (min-width: 1024px) {
  .container {
    max-width: 1024px;
  }
}

@media (min-width: 1280px) {
  .container {
    max-width: 1280px;
  }
}
```

### Responsive Component Example
```tsx
// components/layout/Grid.tsx
import React from 'react';
import { clsx } from 'clsx';

interface GridProps {
  children: React.ReactNode;
  cols?: {
    default?: number;
    sm?: number;
    md?: number;
    lg?: number;
    xl?: number;
  };
  gap?: number;
  className?: string;
}

export const Grid: React.FC<GridProps> = ({
  children,
  cols = { default: 1 },
  gap = 4,
  className,
}) => {
  const gridClasses = clsx(
    'grid',
    `gap-${gap}`,
    cols.default && `grid-cols-${cols.default}`,
    cols.sm && `sm:grid-cols-${cols.sm}`,
    cols.md && `md:grid-cols-${cols.md}`,
    cols.lg && `lg:grid-cols-${cols.lg}`,
    cols.xl && `xl:grid-cols-${cols.xl}`,
    className
  );

  return <div className={gridClasses}>{children}</div>;
};

// Usage
<Grid
  cols={{
    default: 1,
    sm: 2,
    md: 3,
    lg: 4,
  }}
  gap={6}
>
  {items.map(item => (
    <Card key={item.id}>{item.content}</Card>
  ))}
</Grid>
```

---

## 🧪 Testing Standards

### Unit Testing Setup
```typescript
// vitest.config.ts
import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./src/test/setup.ts'],
    css: true,
  },
});

// src/test/setup.ts
import '@testing-library/jest-dom';
import { cleanup } from '@testing-library/react';
import { afterEach } from 'vitest';

afterEach(() => {
  cleanup();
});

// Test utilities
// src/test/utils.tsx
import React from 'react';
import { render, RenderOptions } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter } from 'react-router-dom';

const AllTheProviders: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        retry: false,
      },
    },
  });

  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        {children}
      </BrowserRouter>
    </QueryClientProvider>
  );
};

const customRender = (
  ui: React.ReactElement,
  options?: Omit<RenderOptions, 'wrapper'>
) => render(ui, { wrapper: AllTheProviders, ...options });

export * from '@testing-library/react';
export { customRender as render };
```

### Component Testing Example
```typescript
// components/UserCard/UserCard.test.tsx
import { render, screen, fireEvent, waitFor } from '../../test/utils';
import { UserCard } from './UserCard';
import { User } from '../../types/user';

const mockUser: User = {
  id: '1',
  name: 'John Doe',
  email: 'john@example.com',
  avatar: 'https://example.com/avatar.jpg',
};

describe('UserCard', () => {
  it('renders user information correctly', () => {
    render(<UserCard user={mockUser} />);
    
    expect(screen.getByText('John Doe')).toBeInTheDocument();
    expect(screen.getByText('john@example.com')).toBeInTheDocument();
    expect(screen.getByRole('img')).toHaveAttribute('src', mockUser.avatar);
  });

  it('calls onEdit when edit button is clicked', async () => {
    const onEdit = vi.fn();
    render(<UserCard user={mockUser} onEdit={onEdit} />);
    
    fireEvent.click(screen.getByRole('button', { name: /edit/i }));
    
    await waitFor(() => {
      expect(onEdit).toHaveBeenCalledWith(mockUser);
    });
  });

  it('shows loading state when updating', () => {
    render(<UserCard user={mockUser} isUpdating />);
    
    expect(screen.getByRole('button', { name: /edit/i })).toBeDisabled();
    expect(screen.getByTestId('loading-spinner')).toBeInTheDocument();
  });
});
```

### E2E Testing with Cypress
```typescript
// cypress/e2e/auth.cy.ts
describe('Authentication', () => {
  beforeEach(() => {
    cy.visit('/login');
  });

  it('should login successfully with valid credentials', () => {
    cy.get('[data-testid="email-input"]').type('user@example.com');
    cy.get('[data-testid="password-input"]').type('password123');
    cy.get('[data-testid="login-button"]').click();

    cy.url().should('include', '/dashboard');
    cy.get('[data-testid="user-menu"]').should('be.visible');
  });

  it('should show error message with invalid credentials', () => {
    cy.get('[data-testid="email-input"]').type('invalid@example.com');
    cy.get('[data-testid="password-input"]').type('wrongpassword');
    cy.get('[data-testid="login-button"]').click();

    cy.get('[data-testid="error-message"]')
      .should('be.visible')
      .and('contain', 'Invalid credentials');
  });

  it('should navigate to register page', () => {
    cy.get('[data-testid="register-link"]').click();
    cy.url().should('include', '/register');
  });
});
```

---

## 🚀 Performance Optimization

### Code Splitting
```typescript
// React lazy loading
import { lazy, Suspense } from 'react';
import { Routes, Route } from 'react-router-dom';
import { LoadingSpinner } from './components/LoadingSpinner';

const Home = lazy(() => import('./pages/Home'));
const Dashboard = lazy(() => import('./pages/Dashboard'));
const Profile = lazy(() => import('./pages/Profile'));

function App() {
  return (
    <Suspense fallback={<LoadingSpinner />}>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/profile" element={<Profile />} />
      </Routes>
    </Suspense>
  );
}

// Vue lazy loading
const routes = [
  {
    path: '/',
    component: () => import('./views/Home.vue'),
  },
  {
    path: '/dashboard',
    component: () => import('./views/Dashboard.vue'),
  },
  {
    path: '/profile',
    component: () => import('./views/Profile.vue'),
  },
];
```

### Image Optimization
```typescript
// components/OptimizedImage.tsx
import React, { useState } from 'react';
import { clsx } from 'clsx';

interface OptimizedImageProps {
  src: string;
  alt: string;
  width?: number;
  height?: number;
  className?: string;
  loading?: 'lazy' | 'eager';
  placeholder?: string;
}

export const OptimizedImage: React.FC<OptimizedImageProps> = ({
  src,
  alt,
  width,
  height,
  className,
  loading = 'lazy',
  placeholder = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZGRkIi8+PC9zdmc+',
}) => {
  const [isLoaded, setIsLoaded] = useState(false);
  const [hasError, setHasError] = useState(false);

  const handleLoad = () => setIsLoaded(true);
  const handleError = () => setHasError(true);

  if (hasError) {
    return (
      <div className={clsx('bg-gray-200 flex items-center justify-center', className)}>
        <span className="text-gray-500">Failed to load image</span>
      </div>
    );
  }

  return (
    <div className={clsx('relative overflow-hidden', className)}>
      {!isLoaded && (
        <img
          src={placeholder}
          alt=""
          className="absolute inset-0 w-full h-full object-cover"
        />
      )}
      <img
        src={src}
        alt={alt}
        width={width}
        height={height}
        loading={loading}
        onLoad={handleLoad}
        onError={handleError}
        className={clsx(
          'w-full h-full object-cover transition-opacity duration-300',
          isLoaded ? 'opacity-100' : 'opacity-0'
        )}
      />
    </div>
  );
};
```

### Bundle Analysis
```json
// package.json scripts
{
  "scripts": {
    "build": "vite build",
    "build:analyze": "vite build && npx vite-bundle-analyzer dist",
    "preview": "vite preview"
  }
}
```

---

## ✅ Quality Checklist

### Code Quality
- [ ] TypeScript strict mode enabled
- [ ] ESLint and Prettier configured
- [ ] No console.log statements in production
- [ ] Error boundaries implemented
- [ ] Loading states handled
- [ ] Error states handled
- [ ] Accessibility attributes added
- [ ] SEO meta tags included

### Performance
- [ ] Code splitting implemented
- [ ] Images optimized and lazy loaded
- [ ] Bundle size analyzed and optimized
- [ ] Unused dependencies removed
- [ ] Critical CSS inlined
- [ ] Service worker configured (if needed)

### Testing
- [ ] Unit tests cover critical components (>80%)
- [ ] Integration tests for user flows
- [ ] E2E tests for critical paths
- [ ] Accessibility tests included
- [ ] Performance tests configured

### Security
- [ ] Input validation implemented
- [ ] XSS protection in place
- [ ] CSRF protection configured
- [ ] Content Security Policy set
- [ ] Environment variables secured
- [ ] Dependencies audited for vulnerabilities

### Deployment
- [ ] Build process optimized
- [ ] Environment configurations set
- [ ] CI/CD pipeline configured
- [ ] Error monitoring setup
- [ ] Performance monitoring setup
- [ ] Analytics tracking implemented

---

**🌐 Modern frontend development with TypeScript, component-driven architecture, and comprehensive quality standards for scalable, maintainable web applications.**