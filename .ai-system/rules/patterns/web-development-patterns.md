# Web Development Patterns

## 🌐 Web-Specific Pattern Library

**Purpose**: Comprehensive patterns for modern web application development  
**Technologies**: React, Vue.js, Angular, Node.js, TypeScript, Next.js, Nuxt.js  
**Integration**: Advanced Sub-Agent integration for web-specific optimization  
**Focus**: Performance, SEO, accessibility, security, scalability

## 🏗️ Frontend Architecture Patterns

### 1. Component-Based Architecture Pattern

**Description**: Modular, reusable UI components with state management  
**Best For**: React, Vue, Angular applications  
**Sub-Agent Integration**: Context Optimizer for component optimization, Performance Analyzer for rendering performance

#### React Implementation with Sub-Agent Integration

```typescript
// Component-Based Architecture for React
import React, { useState, useEffect, useCallback, useMemo } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useSubAgents } from '../hooks/useSubAgents';
import { User, UserService, LoadingStrategy } from '../types';

// Enhanced Hook with Sub-Agent Integration
function useUserProfile(userId: string) {
  const { contextOptimizer, performanceAnalyzer, bugHunter } = useSubAgents();
  const queryClient = useQueryClient();
  
  // Optimize query strategy with Context Optimizer
  const optimizedQueryKey = useMemo(() => {
    return contextOptimizer?.optimizeQueryKey([
      'user',
      userId,
      {
        context: 'profile',
        timestamp: Date.now(),
        userAgent: navigator.userAgent
      }
    ]) || ['user', userId];
  }, [userId, contextOptimizer]);
  
  const {
    data: user,
    isLoading,
    error,
    refetch
  } = useQuery({
    queryKey: optimizedQueryKey,
    queryFn: async () => {
      const startTime = performance.now();
      
      try {
        // Get optimized loading strategy
        const loadingStrategy = await contextOptimizer?.optimizeUserLoading({
          userId,
          context: 'profile',
          networkCondition: navigator.connection?.effectiveType || 'unknown',
          cacheStatus: queryClient.getQueryState(optimizedQueryKey)?.status
        }) || LoadingStrategy.DEFAULT;
        
        const user = await UserService.getUser(userId, loadingStrategy);
        
        // Performance monitoring
        const executionTime = performance.now() - startTime;
        await performanceAnalyzer?.analyzeQueryPerformance({
          queryKey: optimizedQueryKey,
          executionTime,
          strategy: loadingStrategy,
          cacheHit: loadingStrategy.usedCache,
          dataSize: JSON.stringify(user).length
        });
        
        return user;
        
      } catch (error) {
        // Error analysis with Bug Hunter
        await bugHunter?.analyzeQueryError({
          queryKey: optimizedQueryKey,
          error: error as Error,
          context: {
            userId,
            networkStatus: navigator.onLine,
            executionTime: performance.now() - startTime
          }
        });
        
        throw error;
      }
    },
    staleTime: 5 * 60 * 1000, // 5 minutes
    cacheTime: 10 * 60 * 1000, // 10 minutes
    retry: (failureCount, error) => {
      // Intelligent retry with Bug Hunter analysis
      return bugHunter?.shouldRetryQuery({
        failureCount,
        error: error as Error,
        maxRetries: 3
      }) || failureCount < 3;
    }
  });
  
  const updateUserMutation = useMutation({
    mutationFn: async (updatedUser: Partial<User>) => {
      const startTime = performance.now();
      
      try {
        const result = await UserService.updateUser(userId, updatedUser);
        
        // Performance monitoring
        const executionTime = performance.now() - startTime;
        await performanceAnalyzer?.analyzeMutationPerformance({
          mutationType: 'updateUser',
          executionTime,
          dataSize: JSON.stringify(updatedUser).length
        });
        
        return result;
        
      } catch (error) {
        await bugHunter?.analyzeMutationError({
          mutationType: 'updateUser',
          error: error as Error,
          payload: updatedUser,
          executionTime: performance.now() - startTime
        });
        
        throw error;
      }
    },
    onSuccess: (updatedUser) => {
      // Optimistic update with Context Optimizer
      queryClient.setQueryData(optimizedQueryKey, updatedUser);
      
      // Invalidate related queries
      contextOptimizer?.getRelatedQueries(optimizedQueryKey)
        .forEach(queryKey => {
          queryClient.invalidateQueries({ queryKey });
        });
    },
    onError: (error) => {
      // Rollback optimistic update
      queryClient.invalidateQueries({ queryKey: optimizedQueryKey });
    }
  });
  
  return {
    user,
    isLoading,
    error,
    refetch,
    updateUser: updateUserMutation.mutate,
    isUpdating: updateUserMutation.isLoading
  };
}

// Enhanced Component with Sub-Agent Integration
interface UserProfileProps {
  userId: string;
  className?: string;
  onUserUpdate?: (user: User) => void;
}

const UserProfile: React.FC<UserProfileProps> = React.memo(({
  userId,
  className,
  onUserUpdate
}) => {
  const { contextOptimizer, performanceAnalyzer } = useSubAgents();
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState<Partial<User>>({});
  
  const {
    user,
    isLoading,
    error,
    refetch,
    updateUser,
    isUpdating
  } = useUserProfile(userId);
  
  // Optimize component rendering with Performance Analyzer
  useEffect(() => {
    const renderStart = performance.now();
    
    return () => {
      const renderTime = performance.now() - renderStart;
      performanceAnalyzer?.analyzeComponentRender({
        componentName: 'UserProfile',
        renderTime,
        props: { userId, className },
        state: { isEditing, isLoading }
      });
    };
  }, [userId, isEditing, isLoading, performanceAnalyzer]);
  
  // Optimize form handling with Context Optimizer
  const handleEditSubmit = useCallback(async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!user) return;
    
    try {
      // Optimize update payload
      const optimizedPayload = await contextOptimizer?.optimizeUpdatePayload({
        original: user,
        changes: editForm,
        context: 'profile'
      }) || editForm;
      
      await updateUser(optimizedPayload);
      
      setIsEditing(false);
      setEditForm({});
      onUserUpdate?.(user);
      
    } catch (error) {
      console.error('Failed to update user:', error);
    }
  }, [user, editForm, updateUser, onUserUpdate, contextOptimizer]);
  
  const handleEditCancel = useCallback(() => {
    setIsEditing(false);
    setEditForm({});
  }, []);
  
  const handleInputChange = useCallback((field: keyof User, value: string) => {
    setEditForm(prev => ({ ...prev, [field]: value }));
  }, []);
  
  // Memoized components for performance
  const LoadingComponent = useMemo(() => (
    <div className="flex items-center justify-center p-8">
      <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      <span className="ml-2 text-gray-600">Loading user profile...</span>
    </div>
  ), []);
  
  const ErrorComponent = useMemo(() => (
    <div className="bg-red-50 border border-red-200 rounded-lg p-4">
      <div className="flex items-center">
        <svg className="w-5 h-5 text-red-400 mr-2" fill="currentColor" viewBox="0 0 20 20">
          <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
        </svg>
        <h3 className="text-sm font-medium text-red-800">Error loading user profile</h3>
      </div>
      <div className="mt-2">
        <p className="text-sm text-red-700">{error?.message}</p>
        <button
          onClick={() => refetch()}
          className="mt-2 bg-red-100 hover:bg-red-200 text-red-800 px-3 py-1 rounded text-sm transition-colors"
        >
          Try Again
        </button>
      </div>
    </div>
  ), [error, refetch]);
  
  if (isLoading) return LoadingComponent;
  if (error) return ErrorComponent;
  if (!user) return null;
  
  return (
    <div className={`bg-white rounded-lg shadow-md p-6 ${className || ''}`}>
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-2xl font-bold text-gray-900">User Profile</h2>
        {!isEditing && (
          <button
            onClick={() => setIsEditing(true)}
            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md transition-colors"
          >
            Edit Profile
          </button>
        )}
      </div>
      
      {isEditing ? (
        <form onSubmit={handleEditSubmit} className="space-y-4">
          <div>
            <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
              Name
            </label>
            <input
              type="text"
              id="name"
              value={editForm.name || user.name}
              onChange={(e) => handleInputChange('name', e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
              Email
            </label>
            <input
              type="email"
              id="email"
              value={editForm.email || user.email}
              onChange={(e) => handleInputChange('email', e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          
          <div>
            <label htmlFor="bio" className="block text-sm font-medium text-gray-700 mb-1">
              Bio
            </label>
            <textarea
              id="bio"
              value={editForm.bio || user.bio || ''}
              onChange={(e) => handleInputChange('bio', e.target.value)}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          
          <div className="flex space-x-3">
            <button
              type="submit"
              disabled={isUpdating}
              className="bg-green-600 hover:bg-green-700 disabled:bg-gray-400 text-white px-4 py-2 rounded-md transition-colors"
            >
              {isUpdating ? 'Saving...' : 'Save Changes'}
            </button>
            <button
              type="button"
              onClick={handleEditCancel}
              className="bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded-md transition-colors"
            >
              Cancel
            </button>
          </div>
        </form>
      ) : (
        <div className="space-y-4">
          <div className="flex items-center space-x-4">
            <img
              src={user.profileImageUrl || '/default-avatar.png'}
              alt={`${user.name}'s profile`}
              className="w-16 h-16 rounded-full object-cover"
            />
            <div>
              <h3 className="text-xl font-semibold text-gray-900">{user.name}</h3>
              <p className="text-gray-600">{user.email}</p>
            </div>
          </div>
          
          {user.bio && (
            <div>
              <h4 className="text-sm font-medium text-gray-700 mb-1">Bio</h4>
              <p className="text-gray-900">{user.bio}</p>
            </div>
          )}
          
          <div className="grid grid-cols-2 gap-4 text-sm">
            <div>
              <span className="font-medium text-gray-700">Joined:</span>
              <span className="ml-1 text-gray-900">
                {new Date(user.createdAt).toLocaleDateString()}
              </span>
            </div>
            <div>
              <span className="font-medium text-gray-700">Last Active:</span>
              <span className="ml-1 text-gray-900">
                {new Date(user.lastActiveAt).toLocaleDateString()}
              </span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
});

UserProfile.displayName = 'UserProfile';

export default UserProfile;
```

#### Vue.js Implementation with Sub-Agent Integration

```vue
<!-- Component-Based Architecture for Vue.js -->
<template>
  <div :class="['user-profile', className]">
    <div class="profile-header">
      <h2>User Profile</h2>
      <button
        v-if="!isEditing"
        @click="startEditing"
        class="edit-button"
      >
        Edit Profile
      </button>
    </div>
    
    <div v-if="isLoading" class="loading-state">
      <div class="spinner"></div>
      <span>Loading user profile...</span>
    </div>
    
    <div v-else-if="error" class="error-state">
      <div class="error-content">
        <svg class="error-icon" fill="currentColor" viewBox="0 0 20 20">
          <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
        </svg>
        <h3>Error loading user profile</h3>
      </div>
      <p>{{ error.message }}</p>
      <button @click="refetch" class="retry-button">
        Try Again
      </button>
    </div>
    
    <form v-else-if="isEditing" @submit.prevent="handleSubmit" class="edit-form">
      <div class="form-group">
        <label for="name">Name</label>
        <input
          id="name"
          v-model="editForm.name"
          type="text"
          required
          class="form-input"
        />
      </div>
      
      <div class="form-group">
        <label for="email">Email</label>
        <input
          id="email"
          v-model="editForm.email"
          type="email"
          required
          class="form-input"
        />
      </div>
      
      <div class="form-group">
        <label for="bio">Bio</label>
        <textarea
          id="bio"
          v-model="editForm.bio"
          rows="3"
          class="form-input"
        ></textarea>
      </div>
      
      <div class="form-actions">
        <button
          type="submit"
          :disabled="isUpdating"
          class="save-button"
        >
          {{ isUpdating ? 'Saving...' : 'Save Changes' }}
        </button>
        <button
          type="button"
          @click="cancelEditing"
          class="cancel-button"
        >
          Cancel
        </button>
      </div>
    </form>
    
    <div v-else-if="user" class="profile-content">
      <div class="profile-info">
        <img
          :src="user.profileImageUrl || '/default-avatar.png'"
          :alt="`${user.name}'s profile`"
          class="profile-image"
        />
        <div class="user-details">
          <h3>{{ user.name }}</h3>
          <p>{{ user.email }}</p>
        </div>
      </div>
      
      <div v-if="user.bio" class="bio-section">
        <h4>Bio</h4>
        <p>{{ user.bio }}</p>
      </div>
      
      <div class="metadata-grid">
        <div class="metadata-item">
          <span class="label">Joined:</span>
          <span class="value">{{ formatDate(user.createdAt) }}</span>
        </div>
        <div class="metadata-item">
          <span class="label">Last Active:</span>
          <span class="value">{{ formatDate(user.lastActiveAt) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onUnmounted, watch } from 'vue';
import { useQuery, useMutation, useQueryClient } from '@tanstack/vue-query';
import { useSubAgents } from '@/composables/useSubAgents';
import { UserService } from '@/services/UserService';
import type { User, LoadingStrategy } from '@/types';

// Props
interface Props {
  userId: string;
  className?: string;
}

const props = withDefaults(defineProps<Props>(), {
  className: ''
});

// Emits
const emit = defineEmits<{
  userUpdate: [user: User];
}>();

// Composables
const { contextOptimizer, performanceAnalyzer, bugHunter } = useSubAgents();
const queryClient = useQueryClient();

// State
const isEditing = ref(false);
const editForm = reactive<Partial<User>>({});
const renderStartTime = ref(0);

// Computed
const optimizedQueryKey = computed(() => {
  return contextOptimizer?.optimizeQueryKey([
    'user',
    props.userId,
    {
      context: 'profile',
      timestamp: Date.now(),
      userAgent: navigator.userAgent
    }
  ]) || ['user', props.userId];
});

// User Query with Sub-Agent Integration
const {
  data: user,
  isLoading,
  error,
  refetch
} = useQuery({
  queryKey: optimizedQueryKey,
  queryFn: async () => {
    const startTime = performance.now();
    
    try {
      // Get optimized loading strategy
      const loadingStrategy = await contextOptimizer?.optimizeUserLoading({
        userId: props.userId,
        context: 'profile',
        networkCondition: (navigator as any).connection?.effectiveType || 'unknown',
        cacheStatus: queryClient.getQueryState(optimizedQueryKey.value)?.status
      }) || LoadingStrategy.DEFAULT;
      
      const userData = await UserService.getUser(props.userId, loadingStrategy);
      
      // Performance monitoring
      const executionTime = performance.now() - startTime;
      await performanceAnalyzer?.analyzeQueryPerformance({
        queryKey: optimizedQueryKey.value,
        executionTime,
        strategy: loadingStrategy,
        cacheHit: loadingStrategy.usedCache,
        dataSize: JSON.stringify(userData).length
      });
      
      return userData;
      
    } catch (err) {
      // Error analysis with Bug Hunter
      await bugHunter?.analyzeQueryError({
        queryKey: optimizedQueryKey.value,
        error: err as Error,
        context: {
          userId: props.userId,
          networkStatus: navigator.onLine,
          executionTime: performance.now() - startTime
        }
      });
      
      throw err;
    }
  },
  staleTime: 5 * 60 * 1000, // 5 minutes
  cacheTime: 10 * 60 * 1000, // 10 minutes
  retry: (failureCount, error) => {
    // Intelligent retry with Bug Hunter analysis
    return bugHunter?.shouldRetryQuery({
      failureCount,
      error: error as Error,
      maxRetries: 3
    }) || failureCount < 3;
  }
});

// Update User Mutation
const updateUserMutation = useMutation({
  mutationFn: async (updatedUser: Partial<User>) => {
    const startTime = performance.now();
    
    try {
      const result = await UserService.updateUser(props.userId, updatedUser);
      
      // Performance monitoring
      const executionTime = performance.now() - startTime;
      await performanceAnalyzer?.analyzeMutationPerformance({
        mutationType: 'updateUser',
        executionTime,
        dataSize: JSON.stringify(updatedUser).length
      });
      
      return result;
      
    } catch (err) {
      await bugHunter?.analyzeMutationError({
        mutationType: 'updateUser',
        error: err as Error,
        payload: updatedUser,
        executionTime: performance.now() - startTime
      });
      
      throw err;
    }
  },
  onSuccess: (updatedUser) => {
    // Optimistic update with Context Optimizer
    queryClient.setQueryData(optimizedQueryKey.value, updatedUser);
    
    // Invalidate related queries
    contextOptimizer?.getRelatedQueries(optimizedQueryKey.value)
      .forEach(queryKey => {
        queryClient.invalidateQueries({ queryKey });
      });
    
    emit('userUpdate', updatedUser);
  },
  onError: () => {
    // Rollback optimistic update
    queryClient.invalidateQueries({ queryKey: optimizedQueryKey.value });
  }
});

// Computed properties
const isUpdating = computed(() => updateUserMutation.isLoading.value);

// Methods
const startEditing = () => {
  if (user.value) {
    Object.assign(editForm, {
      name: user.value.name,
      email: user.value.email,
      bio: user.value.bio || ''
    });
    isEditing.value = true;
  }
};

const cancelEditing = () => {
  isEditing.value = false;
  Object.keys(editForm).forEach(key => {
    delete (editForm as any)[key];
  });
};

const handleSubmit = async () => {
  if (!user.value) return;
  
  try {
    // Optimize update payload
    const optimizedPayload = await contextOptimizer?.optimizeUpdatePayload({
      original: user.value,
      changes: editForm,
      context: 'profile'
    }) || editForm;
    
    await updateUserMutation.mutateAsync(optimizedPayload);
    
    isEditing.value = false;
    Object.keys(editForm).forEach(key => {
      delete (editForm as any)[key];
    });
    
  } catch (error) {
    console.error('Failed to update user:', error);
  }
};

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString();
};

// Performance monitoring
onMounted(() => {
  renderStartTime.value = performance.now();
});

onUnmounted(() => {
  const renderTime = performance.now() - renderStartTime.value;
  performanceAnalyzer?.analyzeComponentRender({
    componentName: 'UserProfile',
    renderTime,
    props: { userId: props.userId, className: props.className },
    state: { isEditing: isEditing.value, isLoading: isLoading.value }
  });
});

// Watch for performance analysis
watch([() => props.userId, isEditing, isLoading], () => {
  const renderTime = performance.now() - renderStartTime.value;
  performanceAnalyzer?.analyzeComponentRender({
    componentName: 'UserProfile',
    renderTime,
    props: { userId: props.userId, className: props.className },
    state: { isEditing: isEditing.value, isLoading: isLoading.value }
  });
  renderStartTime.value = performance.now();
});
</script>

<style scoped>
.user-profile {
  @apply bg-white rounded-lg shadow-md p-6;
}

.profile-header {
  @apply flex items-center justify-between mb-6;
}

.profile-header h2 {
  @apply text-2xl font-bold text-gray-900;
}

.edit-button {
  @apply bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md transition-colors;
}

.loading-state {
  @apply flex items-center justify-center p-8;
}

.spinner {
  @apply animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mr-2;
}

.error-state {
  @apply bg-red-50 border border-red-200 rounded-lg p-4;
}

.error-content {
  @apply flex items-center mb-2;
}

.error-icon {
  @apply w-5 h-5 text-red-400 mr-2;
}

.error-content h3 {
  @apply text-sm font-medium text-red-800;
}

.error-state p {
  @apply text-sm text-red-700 mb-2;
}

.retry-button {
  @apply bg-red-100 hover:bg-red-200 text-red-800 px-3 py-1 rounded text-sm transition-colors;
}

.edit-form {
  @apply space-y-4;
}

.form-group {
  @apply space-y-1;
}

.form-group label {
  @apply block text-sm font-medium text-gray-700;
}

.form-input {
  @apply w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500;
}

.form-actions {
  @apply flex space-x-3;
}

.save-button {
  @apply bg-green-600 hover:bg-green-700 disabled:bg-gray-400 text-white px-4 py-2 rounded-md transition-colors;
}

.cancel-button {
  @apply bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded-md transition-colors;
}

.profile-content {
  @apply space-y-4;
}

.profile-info {
  @apply flex items-center space-x-4;
}

.profile-image {
  @apply w-16 h-16 rounded-full object-cover;
}

.user-details h3 {
  @apply text-xl font-semibold text-gray-900;
}

.user-details p {
  @apply text-gray-600;
}

.bio-section h4 {
  @apply text-sm font-medium text-gray-700 mb-1;
}

.bio-section p {
  @apply text-gray-900;
}

.metadata-grid {
  @apply grid grid-cols-2 gap-4 text-sm;
}

.metadata-item .label {
  @apply font-medium text-gray-700;
}

.metadata-item .value {
  @apply ml-1 text-gray-900;
}
</style>
```

### 2. State Management Pattern (Redux/Vuex/Pinia)

**Description**: Centralized state management with predictable state updates  
**Best For**: Complex applications with shared state  
**Sub-Agent Integration**: Context Optimizer for state optimization, Performance Analyzer for state update performance

#### Redux Toolkit Implementation with Sub-Agent Integration

```typescript
// Redux Toolkit with Sub-Agent Integration
import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { UserService } from '../services/UserService';
import { SubAgentContainer } from '../types/SubAgents';
import type { User, LoadingStrategy } from '../types';

// State interface
interface UserState {
  users: Record<string, User>;
  currentUserId: string | null;
  loading: Record<string, boolean>;
  errors: Record<string, string | null>;
  lastFetch: Record<string, number>;
  optimizationMetrics: {
    cacheHits: number;
    totalRequests: number;
    averageResponseTime: number;
  };
}

const initialState: UserState = {
  users: {},
  currentUserId: null,
  loading: {},
  errors: {},
  lastFetch: {},
  optimizationMetrics: {
    cacheHits: 0,
    totalRequests: 0,
    averageResponseTime: 0
  }
};

// Sub-Agent enhanced async thunks
export const fetchUser = createAsyncThunk(
  'users/fetchUser',
  async (
    { userId, subAgents }: { userId: string; subAgents?: SubAgentContainer },
    { getState, rejectWithValue }
  ) => {
    const startTime = performance.now();
    const state = getState() as { users: UserState };
    
    try {
      // Check if we need to fetch (with Context Optimizer)
      const shouldFetch = await subAgents?.contextOptimizer?.shouldRefreshUser({
        userId,
        lastFetch: state.users.lastFetch[userId],
        currentData: state.users.users[userId],
        context: 'userSlice'
      }) ?? true;
      
      if (!shouldFetch && state.users.users[userId]) {
        // Return cached data
        await subAgents?.performanceAnalyzer?.recordCacheHit({
          operation: 'fetchUser',
          userId,
          executionTime: performance.now() - startTime
        });
        
        return {
          user: state.users.users[userId],
          fromCache: true,
          executionTime: performance.now() - startTime
        };
      }
      
      // Optimize loading strategy
      const loadingStrategy = await subAgents?.contextOptimizer?.optimizeUserLoading({
        userId,
        context: 'redux',
        existingData: state.users.users[userId],
        networkCondition: (navigator as any).connection?.effectiveType || 'unknown'
      }) || LoadingStrategy.DEFAULT;
      
      const user = await UserService.getUser(userId, loadingStrategy);
      const executionTime = performance.now() - startTime;
      
      // Performance monitoring
      await subAgents?.performanceAnalyzer?.analyzeAsyncThunkPerformance({
        thunkName: 'fetchUser',
        executionTime,
        strategy: loadingStrategy,
        dataSize: JSON.stringify(user).length,
        fromCache: false
      });
      
      return {
        user,
        fromCache: false,
        executionTime
      };
      
    } catch (error) {
      const executionTime = performance.now() - startTime;
      
      // Error analysis with Bug Hunter
      await subAgents?.bugHunter?.analyzeAsyncThunkError({
        thunkName: 'fetchUser',
        error: error as Error,
        parameters: { userId },
        executionTime,
        state: {
          hasExistingData: !!state.users.users[userId],
          lastFetch: state.users.lastFetch[userId]
        }
      });
      
      return rejectWithValue({
        message: (error as Error).message,
        userId,
        executionTime
      });
    }
  }
);

export const updateUser = createAsyncThunk(
  'users/updateUser',
  async (
    {
      userId,
      updates,
      subAgents
    }: {
      userId: string;
      updates: Partial<User>;
      subAgents?: SubAgentContainer;
    },
    { getState, rejectWithValue }
  ) => {
    const startTime = performance.now();
    const state = getState() as { users: UserState };
    
    try {
      // Optimize update payload
      const optimizedUpdates = await subAgents?.contextOptimizer?.optimizeUpdatePayload({
        original: state.users.users[userId],
        changes: updates,
        context: 'redux'
      }) || updates;
      
      const updatedUser = await UserService.updateUser(userId, optimizedUpdates);
      const executionTime = performance.now() - startTime;
      
      // Performance monitoring
      await subAgents?.performanceAnalyzer?.analyzeAsyncThunkPerformance({
        thunkName: 'updateUser',
        executionTime,
        dataSize: JSON.stringify(optimizedUpdates).length,
        fromCache: false
      });
      
      return {
        user: updatedUser,
        executionTime
      };
      
    } catch (error) {
      const executionTime = performance.now() - startTime;
      
      await subAgents?.bugHunter?.analyzeAsyncThunkError({
        thunkName: 'updateUser',
        error: error as Error,
        parameters: { userId, updates },
        executionTime
      });
      
      return rejectWithValue({
        message: (error as Error).message,
        userId,
        executionTime
      });
    }
  }
);

// Enhanced slice with Sub-Agent integration
const userSlice = createSlice({
  name: 'users',
  initialState,
  reducers: {
    setCurrentUser: (state, action: PayloadAction<string>) => {
      state.currentUserId = action.payload;
    },
    clearUserError: (state, action: PayloadAction<string>) => {
      delete state.errors[action.payload];
    },
    updateOptimizationMetrics: (
      state,
      action: PayloadAction<{
        cacheHit?: boolean;
        responseTime?: number;
      }>
    ) => {
      const { cacheHit, responseTime } = action.payload;
      
      state.optimizationMetrics.totalRequests += 1;
      
      if (cacheHit) {
        state.optimizationMetrics.cacheHits += 1;
      }
      
      if (responseTime) {
        const currentAvg = state.optimizationMetrics.averageResponseTime;
        const totalRequests = state.optimizationMetrics.totalRequests;
        state.optimizationMetrics.averageResponseTime = 
          (currentAvg * (totalRequests - 1) + responseTime) / totalRequests;
      }
    },
    // Optimistic update for better UX
    optimisticUpdateUser: (
      state,
      action: PayloadAction<{ userId: string; updates: Partial<User> }>
    ) => {
      const { userId, updates } = action.payload;
      if (state.users[userId]) {
        state.users[userId] = { ...state.users[userId], ...updates };
      }
    }
  },
  extraReducers: (builder) => {
    builder
      // Fetch User
      .addCase(fetchUser.pending, (state, action) => {
        const userId = action.meta.arg.userId;
        state.loading[userId] = true;
        state.errors[userId] = null;
      })
      .addCase(fetchUser.fulfilled, (state, action) => {
        const userId = action.meta.arg.userId;
        const { user, fromCache, executionTime } = action.payload;
        
        state.users[userId] = user;
        state.loading[userId] = false;
        state.lastFetch[userId] = Date.now();
        
        // Update optimization metrics
        userSlice.caseReducers.updateOptimizationMetrics(state, {
          type: 'updateOptimizationMetrics',
          payload: {
            cacheHit: fromCache,
            responseTime: executionTime
          }
        });
      })
      .addCase(fetchUser.rejected, (state, action) => {
        const userId = action.meta.arg.userId;
        const error = action.payload as { message: string; executionTime: number };
        
        state.loading[userId] = false;
        state.errors[userId] = error.message;
        
        // Update metrics even for errors
        userSlice.caseReducers.updateOptimizationMetrics(state, {
          type: 'updateOptimizationMetrics',
          payload: {
            responseTime: error.executionTime
          }
        });
      })
      // Update User
      .addCase(updateUser.pending, (state, action) => {
        const userId = action.meta.arg.userId;
        state.loading[userId] = true;
        state.errors[userId] = null;
      })
      .addCase(updateUser.fulfilled, (state, action) => {
        const userId = action.meta.arg.userId;
        const { user, executionTime } = action.payload;
        
        state.users[userId] = user;
        state.loading[userId] = false;
        state.lastFetch[userId] = Date.now();
        
        userSlice.caseReducers.updateOptimizationMetrics(state, {
          type: 'updateOptimizationMetrics',
          payload: {
            responseTime: executionTime
          }
        });
      })
      .addCase(updateUser.rejected, (state, action) => {
        const userId = action.meta.arg.userId;
        const error = action.payload as { message: string; executionTime: number };
        
        state.loading[userId] = false;
        state.errors[userId] = error.message;
        
        // Rollback optimistic update if it was applied
        // This would require storing the previous state
        
        userSlice.caseReducers.updateOptimizationMetrics(state, {
          type: 'updateOptimizationMetrics',
          payload: {
            responseTime: error.executionTime
          }
        });
      });
  }
});

// Selectors with memoization for performance
export const selectUser = (state: { users: UserState }, userId: string) => 
  state.users.users[userId];

export const selectCurrentUser = (state: { users: UserState }) => {
  const currentUserId = state.users.currentUserId;
  return currentUserId ? state.users.users[currentUserId] : null;
};

export const selectUserLoading = (state: { users: UserState }, userId: string) => 
  state.users.loading[userId] || false;

export const selectUserError = (state: { users: UserState }, userId: string) => 
  state.users.errors[userId] || null;

export const selectOptimizationMetrics = (state: { users: UserState }) => 
  state.users.optimizationMetrics;

export const selectCacheHitRate = (state: { users: UserState }) => {
  const metrics = state.users.optimizationMetrics;
  return metrics.totalRequests > 0 
    ? (metrics.cacheHits / metrics.totalRequests) * 100 
    : 0;
};

// Actions
export const {
  setCurrentUser,
  clearUserError,
  updateOptimizationMetrics,
  optimisticUpdateUser
} = userSlice.actions;

// Enhanced hooks for components
export const useUserWithSubAgents = (userId: string, subAgents?: SubAgentContainer) => {
  const dispatch = useAppDispatch();
  const user = useAppSelector(state => selectUser(state, userId));
  const loading = useAppSelector(state => selectUserLoading(state, userId));
  const error = useAppSelector(state => selectUserError(state, userId));
  
  const fetchUserWithSubAgents = useCallback(() => {
    dispatch(fetchUser({ userId, subAgents }));
  }, [dispatch, userId, subAgents]);
  
  const updateUserWithSubAgents = useCallback((updates: Partial<User>) => {
    // Optimistic update
    dispatch(optimisticUpdateUser({ userId, updates }));
    
    // Actual update
    dispatch(updateUser({ userId, updates, subAgents }));
  }, [dispatch, userId, subAgents]);
  
  return {
    user,
    loading,
    error,
    fetchUser: fetchUserWithSubAgents,
    updateUser: updateUserWithSubAgents
  };
};

export default userSlice.reducer;
```

## 🔧 Backend Patterns

### 1. API Gateway Pattern

**Description**: Single entry point for all client requests with routing, authentication, and rate limiting  
**Best For**: Microservices architecture, API management  
**Sub-Agent Integration**: Security Auditor for security policies, Performance Analyzer for routing optimization

```typescript
// API Gateway Pattern with Sub-Agent Integration
import express, { Request, Response, NextFunction } from 'express';
import rateLimit from 'express-rate-limit';
import helmet from 'helmet';
import cors from 'cors';
import { createProxyMiddleware } from 'http-proxy-middleware';
import { SubAgentContainer } from '../types/SubAgents';
import { Logger } from '../utils/Logger';

interface GatewayConfig {
  port: number;
  services: ServiceConfig[];
  security: SecurityConfig;
  rateLimit: RateLimitConfig;
  subAgents?: SubAgentContainer;
}

interface ServiceConfig {
  name: string;
  path: string;
  target: string;
  timeout: number;
  retries: number;
  healthCheck: string;
  authentication: boolean;
}

interface SecurityConfig {
  corsOrigins: string[];
  jwtSecret: string;
  encryptionKey: string;
  rateLimitWindowMs: number;
  maxRequestsPerWindow: number;
}

interface RateLimitConfig {
  windowMs: number;
  max: number;
  message: string;
  standardHeaders: boolean;
  legacyHeaders: boolean;
}

class APIGateway {
  private app: express.Application;
  private config: GatewayConfig;
  private subAgents?: SubAgentContainer;
  private logger: Logger;
  private serviceHealthStatus: Map<string, boolean> = new Map();
  
  constructor(config: GatewayConfig) {
    this.config = config;
    this.subAgents = config.subAgents;
    this.app = express();
    this.logger = new Logger('APIGateway');
    
    this.setupMiddleware();
    this.setupRoutes();
    this.setupHealthChecks();
    this.setupErrorHandling();
  }
  
  private setupMiddleware(): void {
    // Security middleware with Sub-Agent enhancement
    this.app.use(helmet({
      contentSecurityPolicy: {
        directives: {
          defaultSrc: ["'self'"],
          styleSrc: ["'self'", "'unsafe-inline'"],
          scriptSrc: ["'self'"],
          imgSrc: ["'self'", "data:", "https:"],
        },
      },
    }));
    
    // CORS with Sub-Agent security analysis
    this.app.use(cors({
      origin: async (origin, callback) => {
        if (!origin) return callback(null, true);
        
        // Enhanced origin validation with Security Auditor
        const isAllowed = await this.subAgents?.securityAuditor?.validateOrigin({
          origin,
          allowedOrigins: this.config.security.corsOrigins,
          context: 'api-gateway'
        }) ?? this.config.security.corsOrigins.includes(origin);
        
        callback(null, isAllowed);
      },
      credentials: true
    }));
    
    // Enhanced rate limiting with Sub-Agent analysis
    const rateLimiter = rateLimit({
      windowMs: this.config.rateLimit.windowMs,
      max: async (req: Request) => {
        // Dynamic rate limiting based on user behavior analysis
        const userAnalysis = await this.subAgents?.securityAuditor?.analyzeUserBehavior({
          ip: req.ip,
          userAgent: req.get('User-Agent'),
          path: req.path,
          method: req.method
        });
        
        if (userAnalysis?.riskLevel === 'high') {
          return Math.floor(this.config.rateLimit.max * 0.5); // Reduce limit for high-risk users
        } else if (userAnalysis?.riskLevel === 'low') {
          return Math.floor(this.config.rateLimit.max * 1.5); // Increase limit for trusted users
        }
        
        return this.config.rateLimit.max;
      },
      message: this.config.rateLimit.message,
      standardHeaders: this.config.rateLimit.standardHeaders,
      legacyHeaders: this.config.rateLimit.legacyHeaders,
      handler: async (req: Request, res: Response) => {
        // Log rate limit violations with Security Auditor
        await this.subAgents?.securityAuditor?.logRateLimitViolation({
          ip: req.ip,
          userAgent: req.get('User-Agent'),
          path: req.path,
          method: req.method,
          timestamp: new Date()
        });
        
        res.status(429).json({
          error: 'Too many requests',
          message: this.config.rateLimit.message,
          retryAfter: Math.ceil(this.config.rateLimit.windowMs / 1000)
        });
      }
    });
    
    this.app.use(rateLimiter);
    
    // Request parsing
    this.app.use(express.json({ limit: '10mb' }));
    this.app.use(express.urlencoded({ extended: true, limit: '10mb' }));
    
    // Request logging and performance monitoring
    this.app.use(async (req: Request, res: Response, next: NextFunction) => {
      const startTime = Date.now();
      
      // Enhanced request logging with Context Optimizer
      const requestId = await this.subAgents?.contextOptimizer?.generateRequestId({
        method: req.method,
        path: req.path,
        ip: req.ip,
        timestamp: startTime
      }) ?? `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
      
      req.headers['x-request-id'] = requestId;
      
      this.logger.info(`Incoming request: ${req.method} ${req.path}`, {
        requestId,
        ip: req.ip,
        userAgent: req.get('User-Agent')
      });
      
      // Performance monitoring
      res.on('finish', async () => {
        const duration = Date.now() - startTime;
        
        await this.subAgents?.performanceAnalyzer?.analyzeGatewayRequest({
          method: req.method,
          path: req.path,
          statusCode: res.statusCode,
          duration,
          requestSize: req.get('content-length') ? parseInt(req.get('content-length')!) : 0,
          responseSize: res.get('content-length') ? parseInt(res.get('content-length')!) : 0,
          requestId
        });
        
        this.logger.info(`Request completed: ${req.method} ${req.path}`, {
          requestId,
          statusCode: res.statusCode,
          duration: `${duration}ms`
        });
      });
      
      next();
    });
  }
  
  private setupRoutes(): void {
    // Health check endpoint
    this.app.get('/health', async (req: Request, res: Response) => {
      const healthStatus = await this.getOverallHealth();
      
      res.status(healthStatus.healthy ? 200 : 503).json({
        status: healthStatus.healthy ? 'healthy' : 'unhealthy',
        timestamp: new Date().toISOString(),
        services: healthStatus.services,
        uptime: process.uptime(),
        memory: process.memoryUsage(),
        version: process.env.npm_package_version || '1.0.0'
      });
    });
    
    // Service routes with enhanced proxy
    this.config.services.forEach(service => {
      this.setupServiceProxy(service);
    });
    
    // Catch-all for undefined routes
    this.app.use('*', (req: Request, res: Response) => {
      res.status(404).json({
        error: 'Route not found',
        message: `The requested route ${req.method} ${req.originalUrl} was not found`,
        timestamp: new Date().toISOString()
      });
    });
  }
  
  private setupServiceProxy(service: ServiceConfig): void {
    const proxyMiddleware = createProxyMiddleware({
      target: service.target,
      changeOrigin: true,
      pathRewrite: {
        [`^${service.path}`]: ''
      },
      timeout: service.timeout,
      onProxyReq: async (proxyReq, req, res) => {
        // Enhanced request modification with Context Optimizer
        const optimizedHeaders = await this.subAgents?.contextOptimizer?.optimizeProxyHeaders({
          originalHeaders: req.headers,
          service: service.name,
          path: req.path
        }) ?? req.headers;
        
        // Apply optimized headers
        Object.entries(optimizedHeaders).forEach(([key, value]) => {
          if (typeof value === 'string') {
            proxyReq.setHeader(key, value);
          }
        });
        
        // Add service identification
        proxyReq.setHeader('X-Gateway-Service', service.name);
        proxyReq.setHeader('X-Gateway-Timestamp', Date.now().toString());
      },
      onProxyRes: async (proxyRes, req, res) => {
        // Response analysis with Performance Analyzer
        const responseSize = proxyRes.headers['content-length'] 
          ? parseInt(proxyRes.headers['content-length']) 
          : 0;
        
        await this.subAgents?.performanceAnalyzer?.analyzeServiceResponse({
          service: service.name,
          path: req.path,
          statusCode: proxyRes.statusCode || 0,
          responseSize,
          headers: proxyRes.headers
        });
        
        // Security analysis
        await this.subAgents?.securityAuditor?.analyzeServiceResponse({
          service: service.name,
          statusCode: proxyRes.statusCode || 0,
          headers: proxyRes.headers,
          contentType: proxyRes.headers['content-type']
        });
      },
      onError: async (err, req, res) => {
        this.logger.error(`Proxy error for service ${service.name}:`, err);
        
        // Error analysis with Bug Hunter
        await this.subAgents?.bugHunter?.analyzeProxyError({
          service: service.name,
          error: err,
          request: {
            method: req.method,
            path: req.path,
            headers: req.headers
          }
        });
        
        // Mark service as unhealthy
        this.serviceHealthStatus.set(service.name, false);
        
        if (!res.headersSent) {
          res.status(502).json({
            error: 'Service unavailable',
            message: `The ${service.name} service is currently unavailable`,
            service: service.name,
            timestamp: new Date().toISOString()
          });
        }
      },
      retry: service.retries
    });
    
    // Authentication middleware for protected services
    if (service.authentication) {
      this.app.use(service.path, this.authenticationMiddleware.bind(this));
    }
    
    // Service-specific middleware
    this.app.use(service.path, async (req: Request, res: Response, next: NextFunction) => {
      // Check service health before proxying
      const isHealthy = this.serviceHealthStatus.get(service.name) ?? true;
      
      if (!isHealthy) {
        // Attempt to restore service health
        const healthRestored = await this.checkServiceHealth(service);
        if (!healthRestored) {
          return res.status(503).json({
            error: 'Service unavailable',
            message: `The ${service.name} service is currently down`,
            service: service.name,
            timestamp: new Date().toISOString()
          });
        }
      }
      
      next();
    });
    
    this.app.use(service.path, proxyMiddleware);
  }
  
  private async authenticationMiddleware(req: Request, res: Response, next: NextFunction): Promise<void> {
    try {
      const token = req.headers.authorization?.replace('Bearer ', '');
      
      if (!token) {
        return res.status(401).json({
          error: 'Authentication required',
          message: 'No authentication token provided'
        });
      }
      
      // Enhanced token validation with Security Auditor
      const tokenValidation = await this.subAgents?.securityAuditor?.validateToken({
        token,
        secret: this.config.security.jwtSecret,
        context: 'api-gateway'
      });
      
      if (!tokenValidation?.valid) {
        return res.status(401).json({
          error: 'Invalid token',
          message: tokenValidation?.reason || 'Token validation failed'
        });
      }
      
      // Add user context to request
      (req as any).user = tokenValidation.payload;
      
      next();
      
    } catch (error) {
      this.logger.error('Authentication error:', error);
      
      await this.subAgents?.bugHunter?.analyzeAuthenticationError({
        error: error as Error,
        request: {
          method: req.method,
          path: req.path,
          ip: req.ip,
          userAgent: req.get('User-Agent')
        }
      });
      
      res.status(500).json({
        error: 'Authentication error',
        message: 'An error occurred during authentication'
      });
    }
  }
  
  private setupHealthChecks(): void {
    // Periodic health checks for all services
    setInterval(async () => {
      for (const service of this.config.services) {
        await this.checkServiceHealth(service);
      }
    }, 30000); // Check every 30 seconds
  }
  
  private async checkServiceHealth(service: ServiceConfig): Promise<boolean> {
    try {
      const response = await fetch(`${service.target}${service.healthCheck}`, {
        method: 'GET',
        timeout: 5000
      });
      
      const isHealthy = response.ok;
      this.serviceHealthStatus.set(service.name, isHealthy);
      
      if (!isHealthy) {
          this.logger.warn(`Service ${service.name} health check failed`, {
            service: service.name,
            target: service.target,
            healthCheck: service.healthCheck,
            statusCode: response.status
          });
          
          // Analyze service failure with Bug Hunter
          await this.subAgents?.bugHunter?.analyzeServiceFailure({
            service: service.name,
            target: service.target,
            healthCheck: service.healthCheck,
            statusCode: response.status,
            timestamp: new Date()
          });
        }
        
        return isHealthy;
        
      } catch (error) {
        this.logger.error(`Health check error for service ${service.name}:`, error);
        
        this.serviceHealthStatus.set(service.name, false);
        
        await this.subAgents?.bugHunter?.analyzeHealthCheckError({
          service: service.name,
          error: error as Error,
          target: service.target,
          healthCheck: service.healthCheck
        });
        
        return false;
      }
    }
    
    private async getOverallHealth(): Promise<{
      healthy: boolean;
      services: Record<string, boolean>;
    }> {
      const services: Record<string, boolean> = {};
      let allHealthy = true;
      
      for (const service of this.config.services) {
        const isHealthy = this.serviceHealthStatus.get(service.name) ?? true;
        services[service.name] = isHealthy;
        if (!isHealthy) allHealthy = false;
      }
      
      return { healthy: allHealthy, services };
    }
    
    private setupErrorHandling(): void {
      // Global error handler
      this.app.use(async (err: Error, req: Request, res: Response, next: NextFunction) => {
        this.logger.error('Unhandled error:', err);
        
        // Comprehensive error analysis with Bug Hunter
        await this.subAgents?.bugHunter?.analyzeGatewayError({
          error: err,
          request: {
            method: req.method,
            path: req.path,
            headers: req.headers,
            body: req.body,
            ip: req.ip
          },
          timestamp: new Date()
        });
        
        if (!res.headersSent) {
          res.status(500).json({
            error: 'Internal server error',
            message: 'An unexpected error occurred',
            timestamp: new Date().toISOString(),
            requestId: req.headers['x-request-id']
          });
        }
      });
    }
    
    public async start(): Promise<void> {
      return new Promise((resolve, reject) => {
        try {
          this.app.listen(this.config.port, () => {
            this.logger.info(`API Gateway started on port ${this.config.port}`);
            
            // Log service configuration
            this.config.services.forEach(service => {
              this.logger.info(`Service registered: ${service.name} -> ${service.target}`);
            });
            
            resolve();
          });
        } catch (error) {
          this.logger.error('Failed to start API Gateway:', error);
          reject(error);
        }
      });
    }
    
    public async stop(): Promise<void> {
      this.logger.info('Stopping API Gateway...');
      // Graceful shutdown logic here
    }
  }
  
  // Usage Example
  const gatewayConfig: GatewayConfig = {
    port: 3000,
    services: [
      {
        name: 'user-service',
        path: '/api/users',
        target: 'http://localhost:3001',
        timeout: 30000,
        retries: 3,
        healthCheck: '/health',
        authentication: true
      },
      {
        name: 'product-service',
        path: '/api/products',
        target: 'http://localhost:3002',
        timeout: 30000,
        retries: 3,
        healthCheck: '/health',
        authentication: false
      }
    ],
    security: {
      corsOrigins: ['http://localhost:3000', 'https://myapp.com'],
      jwtSecret: process.env.JWT_SECRET || 'your-secret-key',
      encryptionKey: process.env.ENCRYPTION_KEY || 'your-encryption-key',
      rateLimitWindowMs: 15 * 60 * 1000, // 15 minutes
      maxRequestsPerWindow: 100
    },
    rateLimit: {
      windowMs: 15 * 60 * 1000, // 15 minutes
      max: 100, // limit each IP to 100 requests per windowMs
      message: 'Too many requests from this IP, please try again later.',
      standardHeaders: true,
      legacyHeaders: false
    }
  };
  
  const gateway = new APIGateway(gatewayConfig);
  gateway.start().catch(console.error);
  ```

### 2. Microservices Communication Pattern

**Description**: Service-to-service communication with message queues and event-driven architecture  
**Best For**: Distributed systems, scalable architectures  
**Sub-Agent Integration**: Performance Analyzer for message throughput, Security Auditor for inter-service security

```typescript
// Microservices Communication Pattern
import { EventEmitter } from 'events';
import Redis from 'ioredis';
import { SubAgentContainer } from '../types/SubAgents';

interface Message {
  id: string;
  type: string;
  payload: any;
  timestamp: number;
  source: string;
  target?: string;
  correlationId?: string;
  retryCount?: number;
}

interface ServiceConfig {
  name: string;
  version: string;
  endpoints: string[];
  dependencies: string[];
}

class MicroservicesCommunicator extends EventEmitter {
  private redis: Redis;
  private serviceConfig: ServiceConfig;
  private subAgents?: SubAgentContainer;
  private messageHandlers: Map<string, (message: Message) => Promise<void>> = new Map();
  private circuitBreakers: Map<string, CircuitBreaker> = new Map();
  
  constructor(
    serviceConfig: ServiceConfig,
    redisConfig: { host: string; port: number; password?: string },
    subAgents?: SubAgentContainer
  ) {
    super();
    this.serviceConfig = serviceConfig;
    this.subAgents = subAgents;
    this.redis = new Redis(redisConfig);
    
    this.setupMessageHandling();
    this.setupCircuitBreakers();
  }
  
  private setupMessageHandling(): void {
    // Subscribe to service-specific channels
    this.redis.subscribe(`service:${this.serviceConfig.name}`);
    this.redis.subscribe('broadcast');
    
    this.redis.on('message', async (channel: string, message: string) => {
      try {
        const parsedMessage: Message = JSON.parse(message);
        await this.handleIncomingMessage(parsedMessage);
      } catch (error) {
        await this.subAgents?.bugHunter?.analyzeMessageParsingError({
          error: error as Error,
          channel,
          rawMessage: message,
          service: this.serviceConfig.name
        });
      }
    });
  }
  
  private setupCircuitBreakers(): void {
    this.serviceConfig.dependencies.forEach(dependency => {
      this.circuitBreakers.set(dependency, new CircuitBreaker({
        timeout: 5000,
        errorThresholdPercentage: 50,
        resetTimeout: 30000
      }));
    });
  }
  
  public async sendMessage(
    target: string,
    type: string,
    payload: any,
    options: { correlationId?: string; priority?: number } = {}
  ): Promise<void> {
    const startTime = performance.now();
    
    try {
      // Optimize message payload with Context Optimizer
      const optimizedPayload = await this.subAgents?.contextOptimizer?.optimizeMessagePayload({
        originalPayload: payload,
        messageType: type,
        target,
        source: this.serviceConfig.name
      }) || payload;
      
      const message: Message = {
        id: `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`,
        type,
        payload: optimizedPayload,
        timestamp: Date.now(),
        source: this.serviceConfig.name,
        target,
        correlationId: options.correlationId,
        retryCount: 0
      };
      
      // Security validation with Security Auditor
      const securityValidation = await this.subAgents?.securityAuditor?.validateMessage({
        message,
        source: this.serviceConfig.name,
        target
      });
      
      if (!securityValidation?.valid) {
        throw new Error(`Message security validation failed: ${securityValidation?.reason}`);
      }
      
      // Check circuit breaker for target service
      const circuitBreaker = this.circuitBreakers.get(target);
      if (circuitBreaker?.isOpen()) {
        throw new Error(`Circuit breaker is open for service: ${target}`);
      }
      
      // Send message
      await this.redis.publish(`service:${target}`, JSON.stringify(message));
      
      // Performance monitoring
      const executionTime = performance.now() - startTime;
      await this.subAgents?.performanceAnalyzer?.analyzeMessageSending({
        messageType: type,
        target,
        payloadSize: JSON.stringify(optimizedPayload).length,
        executionTime,
        correlationId: options.correlationId
      });
      
      this.emit('messageSent', { message, executionTime });
      
    } catch (error) {
      const executionTime = performance.now() - startTime;
      
      await this.subAgents?.bugHunter?.analyzeMessageSendingError({
        error: error as Error,
        messageType: type,
        target,
        payload,
        executionTime,
        source: this.serviceConfig.name
      });
      
      // Record circuit breaker failure
      const circuitBreaker = this.circuitBreakers.get(target);
      circuitBreaker?.recordFailure();
      
      throw error;
    }
  }
  
  public async broadcastMessage(
    type: string,
    payload: any,
    options: { excludeServices?: string[] } = {}
  ): Promise<void> {
    const startTime = performance.now();
    
    try {
      const optimizedPayload = await this.subAgents?.contextOptimizer?.optimizeMessagePayload({
        originalPayload: payload,
        messageType: type,
        target: 'broadcast',
        source: this.serviceConfig.name
      }) || payload;
      
      const message: Message = {
        id: `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`,
        type,
        payload: optimizedPayload,
        timestamp: Date.now(),
        source: this.serviceConfig.name
      };
      
      await this.redis.publish('broadcast', JSON.stringify(message));
      
      const executionTime = performance.now() - startTime;
      await this.subAgents?.performanceAnalyzer?.analyzeMessageBroadcast({
        messageType: type,
        payloadSize: JSON.stringify(optimizedPayload).length,
        executionTime,
        excludedServices: options.excludeServices || []
      });
      
      this.emit('messageBroadcast', { message, executionTime });
      
    } catch (error) {
      const executionTime = performance.now() - startTime;
      
      await this.subAgents?.bugHunter?.analyzeMessageBroadcastError({
        error: error as Error,
        messageType: type,
        payload,
        executionTime,
        source: this.serviceConfig.name
      });
      
      throw error;
    }
  }
  
  public registerMessageHandler(
    messageType: string,
    handler: (message: Message) => Promise<void>
  ): void {
    this.messageHandlers.set(messageType, handler);
  }
  
  private async handleIncomingMessage(message: Message): Promise<void> {
    const startTime = performance.now();
    
    try {
      // Skip messages from self
      if (message.source === this.serviceConfig.name) {
        return;
      }
      
      // Security validation
      const securityValidation = await this.subAgents?.securityAuditor?.validateIncomingMessage({
        message,
        targetService: this.serviceConfig.name
      });
      
      if (!securityValidation?.valid) {
        await this.subAgents?.securityAuditor?.logSecurityViolation({
          type: 'invalid_message',
          message,
          reason: securityValidation?.reason,
          targetService: this.serviceConfig.name
        });
        return;
      }
      
      const handler = this.messageHandlers.get(message.type);
      if (!handler) {
        this.emit('unhandledMessage', message);
        return;
      }
      
      // Execute handler with performance monitoring
      await handler(message);
      
      const executionTime = performance.now() - startTime;
      await this.subAgents?.performanceAnalyzer?.analyzeMessageHandling({
        messageType: message.type,
        source: message.source,
        executionTime,
        payloadSize: JSON.stringify(message.payload).length,
        correlationId: message.correlationId
      });
      
      this.emit('messageHandled', { message, executionTime });
      
    } catch (error) {
      const executionTime = performance.now() - startTime;
      
      await this.subAgents?.bugHunter?.analyzeMessageHandlingError({
        error: error as Error,
        message,
        executionTime,
        targetService: this.serviceConfig.name
      });
      
      this.emit('messageHandlingError', { message, error, executionTime });
    }
  }
  
  public async getServiceHealth(): Promise<{
    status: 'healthy' | 'degraded' | 'unhealthy';
    dependencies: Record<string, 'healthy' | 'unhealthy'>;
    metrics: {
      messagesSent: number;
      messagesReceived: number;
      averageResponseTime: number;
      errorRate: number;
    };
  }> {
    const dependencies: Record<string, 'healthy' | 'unhealthy'> = {};
    
    for (const dependency of this.serviceConfig.dependencies) {
      const circuitBreaker = this.circuitBreakers.get(dependency);
      dependencies[dependency] = circuitBreaker?.isOpen() ? 'unhealthy' : 'healthy';
    }
    
    const metrics = await this.subAgents?.performanceAnalyzer?.getServiceMetrics({
      serviceName: this.serviceConfig.name,
      timeWindow: 5 * 60 * 1000 // Last 5 minutes
    }) || {
      messagesSent: 0,
      messagesReceived: 0,
      averageResponseTime: 0,
      errorRate: 0
    };
    
    const unhealthyDependencies = Object.values(dependencies).filter(status => status === 'unhealthy').length;
    const status = unhealthyDependencies === 0 
      ? 'healthy' 
      : unhealthyDependencies < this.serviceConfig.dependencies.length 
        ? 'degraded' 
        : 'unhealthy';
    
    return {
      status,
      dependencies,
      metrics
    };
  }
  
  public async disconnect(): Promise<void> {
    await this.redis.disconnect();
    this.removeAllListeners();
  }
}

// Circuit Breaker Implementation
class CircuitBreaker {
  private state: 'closed' | 'open' | 'half-open' = 'closed';
  private failureCount = 0;
  private lastFailureTime = 0;
  private successCount = 0;
  
  constructor(private config: {
    timeout: number;
    errorThresholdPercentage: number;
    resetTimeout: number;
  }) {}
  
  public isOpen(): boolean {
    if (this.state === 'open') {
      if (Date.now() - this.lastFailureTime > this.config.resetTimeout) {
        this.state = 'half-open';
        this.successCount = 0;
        return false;
      }
      return true;
    }
    return false;
  }
  
  public recordSuccess(): void {
    this.failureCount = 0;
    if (this.state === 'half-open') {
      this.successCount++;
      if (this.successCount >= 3) {
        this.state = 'closed';
      }
    }
  }
  
  public recordFailure(): void {
    this.failureCount++;
    this.lastFailureTime = Date.now();
    
    if (this.failureCount >= 5) {
      this.state = 'open';
    }
  }
}
```

## 🎨 Performance Optimization Patterns

### 1. Lazy Loading Pattern

**Description**: Load resources only when needed to improve initial load time  
**Best For**: Large applications, image-heavy content, code splitting  
**Sub-Agent Integration**: Performance Analyzer for loading metrics, Context Optimizer for loading strategies

```typescript
// Lazy Loading Pattern with Sub-Agent Integration
import React, { useState, useEffect, useRef, useCallback } from 'react';
import { SubAgentContainer } from '../types/SubAgents';

interface LazyImageProps {
  src: string;
  alt: string;
  placeholder?: string;
  className?: string;
  onLoad?: () => void;
  onError?: (error: Error) => void;
  subAgents?: SubAgentContainer;
}

const LazyImage: React.FC<LazyImageProps> = ({
  src,
  alt,
  placeholder = '/placeholder.svg',
  className,
  onLoad,
  onError,
  subAgents
}) => {
  const [isLoaded, setIsLoaded] = useState(false);
  const [isInView, setIsInView] = useState(false);
  const [error, setError] = useState<Error | null>(null);
  const imgRef = useRef<HTMLImageElement>(null);
  const observerRef = useRef<IntersectionObserver | null>(null);
  
  // Intersection Observer for viewport detection
  useEffect(() => {
    const currentImgRef = imgRef.current;
    
    if (!currentImgRef) return;
    
    observerRef.current = new IntersectionObserver(
      async (entries) => {
        const [entry] = entries;
        
        if (entry.isIntersecting) {
          setIsInView(true);
          
          // Analyze lazy loading trigger with Performance Analyzer
          await subAgents?.performanceAnalyzer?.analyzeLazyLoadTrigger({
            element: 'image',
            src,
            intersectionRatio: entry.intersectionRatio,
            boundingRect: entry.boundingClientRect,
            timestamp: Date.now()
          });
          
          observerRef.current?.disconnect();
        }
      },
      {
        threshold: 0.1,
        rootMargin: '50px'
      }
    );
    
    observerRef.current.observe(currentImgRef);
    
    return () => {
      observerRef.current?.disconnect();
    };
  }, [src, subAgents]);
  
  // Image loading with optimization
  useEffect(() => {
    if (!isInView) return;
    
    const loadImage = async () => {
      const startTime = performance.now();
      
      try {
        // Optimize image loading strategy with Context Optimizer
        const optimizedSrc = await subAgents?.contextOptimizer?.optimizeImageSrc({
          originalSrc: src,
          context: 'lazy-loading',
          devicePixelRatio: window.devicePixelRatio,
          viewportWidth: window.innerWidth,
          connectionType: (navigator as any).connection?.effectiveType || 'unknown'
        }) || src;
        
        const img = new Image();
        
        img.onload = async () => {
          const loadTime = performance.now() - startTime;
          
          setIsLoaded(true);
          onLoad?.();
          
          // Performance monitoring
          await subAgents?.performanceAnalyzer?.analyzeImageLoad({
            src: optimizedSrc,
            loadTime,
            fileSize: img.naturalWidth * img.naturalHeight * 4, // Estimated
            dimensions: {
              width: img.naturalWidth,
              height: img.naturalHeight
            },
            optimized: optimizedSrc !== src
          });
        };
        
        img.onerror = async () => {
          const loadTime = performance.now() - startTime;
          const error = new Error(`Failed to load image: ${optimizedSrc}`);
          
          setError(error);
          onError?.(error);
          
          // Error analysis
          await subAgents?.bugHunter?.analyzeImageLoadError({
            error,
            src: optimizedSrc,
            originalSrc: src,
            loadTime,
            context: 'lazy-loading'
          });
        };
        
        img.src = optimizedSrc;
        
      } catch (error) {
        const loadTime = performance.now() - startTime;
        const err = error as Error;
        
        setError(err);
        onError?.(err);
        
        await subAgents?.bugHunter?.analyzeImageOptimizationError({
          error: err,
          src,
          loadTime,
          context: 'lazy-loading'
        });
      }
    };
    
    loadImage();
  }, [isInView, src, onLoad, onError, subAgents]);
  
  if (error) {
    return (
      <div className={`lazy-image-error ${className || ''}`}>
        <svg className="w-12 h-12 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
          <path fillRule="evenodd" d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4-8 3 6 2-4 3 6z" clipRule="evenodd" />
        </svg>
        <p className="text-sm text-gray-500 mt-2">Failed to load image</p>
      </div>
    );
  }
  
  return (
    <div className={`lazy-image-container ${className || ''}`} ref={imgRef}>
      {!isLoaded && (
        <img
          src={placeholder}
          alt="Loading..."
          className="lazy-image-placeholder"
        />
      )}
      {isInView && (
        <img
          src={src}
          alt={alt}
          className={`lazy-image ${isLoaded ? 'loaded' : 'loading'}`}
          onLoad={() => setIsLoaded(true)}
        />
      )}
    </div>
  );
};

export default LazyImage;
```

## 📊 Benefits and Usage Guidelines

### Pattern Selection Criteria

1. **Project Complexity**: Choose patterns based on application size and requirements
2. **Team Expertise**: Consider team familiarity with patterns
3. **Performance Requirements**: Select patterns that meet performance goals
4. **Scalability Needs**: Choose patterns that support future growth
5. **Maintenance Overhead**: Balance functionality with maintainability

### Implementation Checklist

- [ ] **Sub-Agent Integration**: Ensure all patterns include Sub-Agent hooks
- [ ] **Performance Monitoring**: Add performance tracking to critical paths
- [ ] **Error Handling**: Implement comprehensive error analysis
- [ ] **Security Validation**: Include security checks for all data flows
- [ ] **Testing Coverage**: Write tests for pattern implementations
- [ ] **Documentation**: Document pattern usage and customization

### Anti-Patterns to Avoid

1. **Over-Engineering**: Don't use complex patterns for simple problems
2. **Pattern Mixing**: Avoid mixing incompatible architectural patterns
3. **Performance Neglect**: Don't ignore performance implications
4. **Security Gaps**: Ensure security is built into patterns
5. **Testing Shortcuts**: Don't skip testing pattern implementations

### Key Benefits

- **🔧 Proven Solutions**: Battle-tested patterns for common problems
- **🤖 Sub-Agent Integration**: Deep integration with intelligent Sub-Agents
- **📈 Performance Optimized**: Built-in performance monitoring and optimization
- **🔒 Security First**: Security considerations built into every pattern
- **🧪 Quality Assured**: Comprehensive testing and error handling
- **📚 Well Documented**: Clear examples and usage guidelines
- **🔄 Maintainable**: Patterns designed for long-term maintenance
- **⚡ Scalable**: Patterns that grow with your application