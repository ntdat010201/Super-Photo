# API Development Patterns

> **🔌 Comprehensive API Development Patterns with Sub-Agent Integration**  
> Advanced patterns for building robust, scalable, and secure APIs with intelligent Sub-Agent support

## Overview

This document provides proven patterns for API development, each enhanced with deep Sub-Agent integration for optimal performance, security, and reliability. These patterns cover RESTful APIs, GraphQL, real-time APIs, and microservices communication.

## 🌐 RESTful API Patterns

### 1. Resource-Based API Pattern

**Description**: Design APIs around resources with standard HTTP methods  
**Best For**: CRUD operations, resource management, standard web APIs  
**Sub-Agent Integration**: Security Auditor for endpoint security, Performance Analyzer for response optimization

```typescript
// Resource-Based API Pattern with Sub-Agent Integration
import express, { Request, Response, NextFunction } from 'express';
import { SubAgentContainer } from '../types/SubAgents';
import { z } from 'zod';
import rateLimit from 'express-rate-limit';
import helmet from 'helmet';

interface ResourceController {
  list(req: Request, res: Response): Promise<void>;
  get(req: Request, res: Response): Promise<void>;
  create(req: Request, res: Response): Promise<void>;
  update(req: Request, res: Response): Promise<void>;
  delete(req: Request, res: Response): Promise<void>;
}

interface APIConfig {
  basePath: string;
  version: string;
  rateLimit: {
    windowMs: number;
    max: number;
  };
  security: {
    enableHelmet: boolean;
    corsOrigins: string[];
    jwtSecret: string;
  };
}

class ResourceAPIController implements ResourceController {
  private subAgents?: SubAgentContainer;
  private resourceName: string;
  private validationSchemas: {
    create: z.ZodSchema;
    update: z.ZodSchema;
    query: z.ZodSchema;
  };
  
  constructor(
    resourceName: string,
    validationSchemas: {
      create: z.ZodSchema;
      update: z.ZodSchema;
      query: z.ZodSchema;
    },
    subAgents?: SubAgentContainer
  ) {
    this.resourceName = resourceName;
    this.validationSchemas = validationSchemas;
    this.subAgents = subAgents;
  }
  
  public async list(req: Request, res: Response): Promise<void> {
    const startTime = performance.now();
    
    try {
      // Validate query parameters with Context Optimizer
      const optimizedQuery = await this.subAgents?.contextOptimizer?.optimizeQueryParameters({
        originalQuery: req.query,
        resourceType: this.resourceName,
        userContext: req.user,
        requestMetadata: {
          ip: req.ip,
          userAgent: req.get('User-Agent'),
          timestamp: new Date()
        }
      }) || req.query;
      
      // Security validation
      const securityCheck = await this.subAgents?.securityAuditor?.validateResourceAccess({
        resource: this.resourceName,
        action: 'list',
        user: req.user,
        query: optimizedQuery,
        request: {
          ip: req.ip,
          headers: req.headers,
          method: req.method,
          path: req.path
        }
      });
      
      if (!securityCheck?.allowed) {
        res.status(403).json({
          error: 'Access denied',
          message: securityCheck?.reason || 'Insufficient permissions',
          code: 'FORBIDDEN'
        });
        return;
      }
      
      // Validate query schema
      const validatedQuery = this.validationSchemas.query.parse(optimizedQuery);
      
      // Fetch resources (mock implementation)
      const resources = await this.fetchResources(validatedQuery);
      
      // Optimize response with Context Optimizer
      const optimizedResponse = await this.subAgents?.contextOptimizer?.optimizeAPIResponse({
        originalData: resources,
        resourceType: this.resourceName,
        action: 'list',
        userContext: req.user,
        requestQuery: validatedQuery
      }) || resources;
      
      const executionTime = performance.now() - startTime;
      
      // Performance monitoring
      await this.subAgents?.performanceAnalyzer?.analyzeAPIEndpoint({
        endpoint: `${req.method} ${req.path}`,
        executionTime,
        responseSize: JSON.stringify(optimizedResponse).length,
        statusCode: 200,
        resourceCount: Array.isArray(optimizedResponse.data) ? optimizedResponse.data.length : 1,
        cacheHit: optimizedResponse.cached || false
      });
      
      res.status(200).json({
        success: true,
        data: optimizedResponse.data || optimizedResponse,
        meta: {
          total: optimizedResponse.total || (Array.isArray(optimizedResponse.data) ? optimizedResponse.data.length : 1),
          page: validatedQuery.page || 1,
          limit: validatedQuery.limit || 10,
          executionTime: Math.round(executionTime)
        }
      });
      
    } catch (error) {
      await this.handleAPIError(error as Error, req, res, startTime);
    }
  }
  
  public async get(req: Request, res: Response): Promise<void> {
    const startTime = performance.now();
    
    try {
      const resourceId = req.params.id;
      
      // Security validation
      const securityCheck = await this.subAgents?.securityAuditor?.validateResourceAccess({
        resource: this.resourceName,
        action: 'get',
        resourceId,
        user: req.user,
        request: {
          ip: req.ip,
          headers: req.headers,
          method: req.method,
          path: req.path
        }
      });
      
      if (!securityCheck?.allowed) {
        res.status(403).json({
          error: 'Access denied',
          message: securityCheck?.reason || 'Insufficient permissions',
          code: 'FORBIDDEN'
        });
        return;
      }
      
      // Fetch resource
      const resource = await this.fetchResource(resourceId);
      
      if (!resource) {
        res.status(404).json({
          error: 'Resource not found',
          message: `${this.resourceName} with id ${resourceId} not found`,
          code: 'NOT_FOUND'
        });
        return;
      }
      
      // Optimize response
      const optimizedResponse = await this.subAgents?.contextOptimizer?.optimizeAPIResponse({
        originalData: resource,
        resourceType: this.resourceName,
        action: 'get',
        userContext: req.user,
        resourceId
      }) || resource;
      
      const executionTime = performance.now() - startTime;
      
      // Performance monitoring
      await this.subAgents?.performanceAnalyzer?.analyzeAPIEndpoint({
        endpoint: `${req.method} ${req.path}`,
        executionTime,
        responseSize: JSON.stringify(optimizedResponse).length,
        statusCode: 200,
        resourceCount: 1,
        cacheHit: optimizedResponse.cached || false
      });
      
      res.status(200).json({
        success: true,
        data: optimizedResponse.data || optimizedResponse,
        meta: {
          executionTime: Math.round(executionTime)
        }
      });
      
    } catch (error) {
      await this.handleAPIError(error as Error, req, res, startTime);
    }
  }
  
  public async create(req: Request, res: Response): Promise<void> {
    const startTime = performance.now();
    
    try {
      // Security validation
      const securityCheck = await this.subAgents?.securityAuditor?.validateResourceAccess({
        resource: this.resourceName,
        action: 'create',
        user: req.user,
        data: req.body,
        request: {
          ip: req.ip,
          headers: req.headers,
          method: req.method,
          path: req.path
        }
      });
      
      if (!securityCheck?.allowed) {
        res.status(403).json({
          error: 'Access denied',
          message: securityCheck?.reason || 'Insufficient permissions',
          code: 'FORBIDDEN'
        });
        return;
      }
      
      // Validate and sanitize input data
      const sanitizedData = await this.subAgents?.securityAuditor?.sanitizeInputData({
        data: req.body,
        resourceType: this.resourceName,
        action: 'create'
      }) || req.body;
      
      // Validate schema
      const validatedData = this.validationSchemas.create.parse(sanitizedData);
      
      // Optimize data for creation
      const optimizedData = await this.subAgents?.contextOptimizer?.optimizeResourceData({
        originalData: validatedData,
        resourceType: this.resourceName,
        action: 'create',
        userContext: req.user
      }) || validatedData;
      
      // Create resource
      const createdResource = await this.createResource(optimizedData);
      
      const executionTime = performance.now() - startTime;
      
      // Performance monitoring
      await this.subAgents?.performanceAnalyzer?.analyzeAPIEndpoint({
        endpoint: `${req.method} ${req.path}`,
        executionTime,
        responseSize: JSON.stringify(createdResource).length,
        statusCode: 201,
        resourceCount: 1,
        operationType: 'create'
      });
      
      res.status(201).json({
        success: true,
        data: createdResource,
        meta: {
          executionTime: Math.round(executionTime)
        }
      });
      
    } catch (error) {
      await this.handleAPIError(error as Error, req, res, startTime);
    }
  }
  
  public async update(req: Request, res: Response): Promise<void> {
    const startTime = performance.now();
    
    try {
      const resourceId = req.params.id;
      
      // Security validation
      const securityCheck = await this.subAgents?.securityAuditor?.validateResourceAccess({
        resource: this.resourceName,
        action: 'update',
        resourceId,
        user: req.user,
        data: req.body,
        request: {
          ip: req.ip,
          headers: req.headers,
          method: req.method,
          path: req.path
        }
      });
      
      if (!securityCheck?.allowed) {
        res.status(403).json({
          error: 'Access denied',
          message: securityCheck?.reason || 'Insufficient permissions',
          code: 'FORBIDDEN'
        });
        return;
      }
      
      // Check if resource exists
      const existingResource = await this.fetchResource(resourceId);
      if (!existingResource) {
        res.status(404).json({
          error: 'Resource not found',
          message: `${this.resourceName} with id ${resourceId} not found`,
          code: 'NOT_FOUND'
        });
        return;
      }
      
      // Validate and sanitize input data
      const sanitizedData = await this.subAgents?.securityAuditor?.sanitizeInputData({
        data: req.body,
        resourceType: this.resourceName,
        action: 'update',
        existingData: existingResource
      }) || req.body;
      
      // Validate schema
      const validatedData = this.validationSchemas.update.parse(sanitizedData);
      
      // Optimize data for update
      const optimizedData = await this.subAgents?.contextOptimizer?.optimizeResourceData({
        originalData: validatedData,
        resourceType: this.resourceName,
        action: 'update',
        userContext: req.user,
        existingData: existingResource
      }) || validatedData;
      
      // Update resource
      const updatedResource = await this.updateResource(resourceId, optimizedData);
      
      const executionTime = performance.now() - startTime;
      
      // Performance monitoring
      await this.subAgents?.performanceAnalyzer?.analyzeAPIEndpoint({
        endpoint: `${req.method} ${req.path}`,
        executionTime,
        responseSize: JSON.stringify(updatedResource).length,
        statusCode: 200,
        resourceCount: 1,
        operationType: 'update'
      });
      
      res.status(200).json({
        success: true,
        data: updatedResource,
        meta: {
          executionTime: Math.round(executionTime)
        }
      });
      
    } catch (error) {
      await this.handleAPIError(error as Error, req, res, startTime);
    }
  }
  
  public async delete(req: Request, res: Response): Promise<void> {
    const startTime = performance.now();
    
    try {
      const resourceId = req.params.id;
      
      // Security validation
      const securityCheck = await this.subAgents?.securityAuditor?.validateResourceAccess({
        resource: this.resourceName,
        action: 'delete',
        resourceId,
        user: req.user,
        request: {
          ip: req.ip,
          headers: req.headers,
          method: req.method,
          path: req.path
        }
      });
      
      if (!securityCheck?.allowed) {
        res.status(403).json({
          error: 'Access denied',
          message: securityCheck?.reason || 'Insufficient permissions',
          code: 'FORBIDDEN'
        });
        return;
      }
      
      // Check if resource exists
      const existingResource = await this.fetchResource(resourceId);
      if (!existingResource) {
        res.status(404).json({
          error: 'Resource not found',
          message: `${this.resourceName} with id ${resourceId} not found`,
          code: 'NOT_FOUND'
        });
        return;
      }
      
      // Delete resource
      await this.deleteResource(resourceId);
      
      const executionTime = performance.now() - startTime;
      
      // Performance monitoring
      await this.subAgents?.performanceAnalyzer?.analyzeAPIEndpoint({
        endpoint: `${req.method} ${req.path}`,
        executionTime,
        responseSize: 0,
        statusCode: 204,
        resourceCount: 1,
        operationType: 'delete'
      });
      
      res.status(204).send();
      
    } catch (error) {
      await this.handleAPIError(error as Error, req, res, startTime);
    }
  }
  
  private async handleAPIError(
    error: Error,
    req: Request,
    res: Response,
    startTime: number
  ): Promise<void> {
    const executionTime = performance.now() - startTime;
    
    // Comprehensive error analysis with Bug Hunter
    await this.subAgents?.bugHunter?.analyzeAPIError({
      error,
      endpoint: `${req.method} ${req.path}`,
      resourceType: this.resourceName,
      request: {
        method: req.method,
        path: req.path,
        params: req.params,
        query: req.query,
        body: req.body,
        headers: req.headers,
        ip: req.ip,
        userAgent: req.get('User-Agent')
      },
      executionTime,
      user: req.user
    });
    
    // Determine error response
    let statusCode = 500;
    let errorCode = 'INTERNAL_ERROR';
    let message = 'An unexpected error occurred';
    
    if (error.name === 'ZodError') {
      statusCode = 400;
      errorCode = 'VALIDATION_ERROR';
      message = 'Invalid input data';
    } else if (error.message.includes('not found')) {
      statusCode = 404;
      errorCode = 'NOT_FOUND';
      message = error.message;
    } else if (error.message.includes('unauthorized')) {
      statusCode = 401;
      errorCode = 'UNAUTHORIZED';
      message = 'Authentication required';
    } else if (error.message.includes('forbidden')) {
      statusCode = 403;
      errorCode = 'FORBIDDEN';
      message = 'Access denied';
    }
    
    res.status(statusCode).json({
      success: false,
      error: message,
      code: errorCode,
      meta: {
        executionTime: Math.round(executionTime),
        timestamp: new Date().toISOString()
      }
    });
  }
  
  // Mock implementation methods (replace with actual data layer)
  private async fetchResources(query: any): Promise<any> {
    // Implementation depends on your data layer
    return { data: [], total: 0 };
  }
  
  private async fetchResource(id: string): Promise<any> {
    // Implementation depends on your data layer
    return null;
  }
  
  private async createResource(data: any): Promise<any> {
    // Implementation depends on your data layer
    return { id: 'new-id', ...data };
  }
  
  private async updateResource(id: string, data: any): Promise<any> {
    // Implementation depends on your data layer
    return { id, ...data };
  }
  
  private async deleteResource(id: string): Promise<void> {
    // Implementation depends on your data layer
  }
}

// API Router Setup
class ResourceAPIRouter {
  private router = express.Router();
  private config: APIConfig;
  private subAgents?: SubAgentContainer;
  
  constructor(config: APIConfig, subAgents?: SubAgentContainer) {
    this.config = config;
    this.subAgents = subAgents;
    this.setupMiddleware();
  }
  
  private setupMiddleware(): void {
    // Security middleware
    if (this.config.security.enableHelmet) {
      this.router.use(helmet());
    }
    
    // Rate limiting
    const limiter = rateLimit({
      windowMs: this.config.rateLimit.windowMs,
      max: this.config.rateLimit.max,
      message: {
        error: 'Too many requests',
        message: 'Rate limit exceeded. Please try again later.',
        code: 'RATE_LIMIT_EXCEEDED'
      },
      standardHeaders: true,
      legacyHeaders: false
    });
    
    this.router.use(limiter);
    
    // Request logging and monitoring
    this.router.use(async (req: Request, res: Response, next: NextFunction) => {
      const startTime = Date.now();
      
      // Log request with Performance Analyzer
      await this.subAgents?.performanceAnalyzer?.logAPIRequest({
        method: req.method,
        path: req.path,
        ip: req.ip,
        userAgent: req.get('User-Agent'),
        timestamp: new Date(),
        headers: req.headers
      });
      
      // Continue to next middleware
      next();
      
      // Log response after request completes
      res.on('finish', async () => {
        const duration = Date.now() - startTime;
        
        await this.subAgents?.performanceAnalyzer?.logAPIResponse({
          method: req.method,
          path: req.path,
          statusCode: res.statusCode,
          duration,
          responseSize: res.get('Content-Length') ? parseInt(res.get('Content-Length')!) : 0
        });
      });
    });
  }
  
  public addResource<T>(
    resourceName: string,
    validationSchemas: {
      create: z.ZodSchema<T>;
      update: z.ZodSchema<Partial<T>>;
      query: z.ZodSchema;
    },
    customController?: ResourceController
  ): void {
    const controller = customController || new ResourceAPIController(
      resourceName,
      validationSchemas,
      this.subAgents
    );
    
    const resourcePath = `/${resourceName}`;
    
    // Define routes
    this.router.get(resourcePath, controller.list.bind(controller));
    this.router.get(`${resourcePath}/:id`, controller.get.bind(controller));
    this.router.post(resourcePath, controller.create.bind(controller));
    this.router.put(`${resourcePath}/:id`, controller.update.bind(controller));
    this.router.patch(`${resourcePath}/:id`, controller.update.bind(controller));
    this.router.delete(`${resourcePath}/:id`, controller.delete.bind(controller));
  }
  
  public getRouter(): express.Router {
    return this.router;
  }
}

// Usage Example
const userSchema = z.object({
  name: z.string().min(1).max(100),
  email: z.string().email(),
  age: z.number().min(0).max(150).optional(),
  role: z.enum(['user', 'admin']).default('user')
});

const querySchema = z.object({
  page: z.number().min(1).default(1),
  limit: z.number().min(1).max(100).default(10),
  search: z.string().optional(),
  sortBy: z.string().optional(),
  sortOrder: z.enum(['asc', 'desc']).default('asc')
});

const apiConfig: APIConfig = {
  basePath: '/api/v1',
  version: '1.0.0',
  rateLimit: {
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 100 // limit each IP to 100 requests per windowMs
  },
  security: {
    enableHelmet: true,
    corsOrigins: ['http://localhost:3000'],
    jwtSecret: process.env.JWT_SECRET || 'your-secret-key'
  }
};

// Create API router with Sub-Agent integration
const apiRouter = new ResourceAPIRouter(apiConfig, subAgents);

// Add resources
apiRouter.addResource('users', {
  create: userSchema,
  update: userSchema.partial(),
  query: querySchema
});

apiRouter.addResource('posts', {
  create: z.object({
    title: z.string().min(1).max(200),
    content: z.string().min(1),
    authorId: z.string().uuid(),
    tags: z.array(z.string()).optional()
  }),
  update: z.object({
    title: z.string().min(1).max(200),
    content: z.string().min(1),
    tags: z.array(z.string()).optional()
  }).partial(),
  query: querySchema.extend({
    authorId: z.string().uuid().optional(),
    tags: z.array(z.string()).optional()
  })
});

export { ResourceAPIRouter, ResourceAPIController, APIConfig };
```

### 2. GraphQL API Pattern

**Description**: Flexible query language for APIs with type safety  
**Best For**: Complex data relationships, mobile APIs, real-time applications  
**Sub-Agent Integration**: Context Optimizer for query optimization, Security Auditor for query validation

```typescript
// GraphQL API Pattern with Sub-Agent Integration
import { ApolloServer } from '@apollo/server';
import { expressMiddleware } from '@apollo/server/express4';
import { buildSchema } from 'graphql';
import { SubAgentContainer } from '../types/SubAgents';
import { GraphQLError } from 'graphql';
import { shield, rule, and, or } from 'graphql-shield';
import { RateLimiterMemory } from 'rate-limiter-flexible';

interface GraphQLContext {
  user?: any;
  req: any;
  res: any;
  subAgents?: SubAgentContainer;
  dataSources: any;
}

interface GraphQLConfig {
  typeDefs: string;
  resolvers: any;
  introspection?: boolean;
  playground?: boolean;
  rateLimit: {
    points: number;
    duration: number;
  };
  security: {
    maxQueryDepth: number;
    maxQueryComplexity: number;
    enableQueryWhitelist: boolean;
  };
}

class GraphQLAPIServer {
  private server: ApolloServer;
  private config: GraphQLConfig;
  private subAgents?: SubAgentContainer;
  private rateLimiter: RateLimiterMemory;
  
  constructor(config: GraphQLConfig, subAgents?: SubAgentContainer) {
    this.config = config;
    this.subAgents = subAgents;
    
    // Setup rate limiter
    this.rateLimiter = new RateLimiterMemory({
      points: config.rateLimit.points,
      duration: config.rateLimit.duration
    });
    
    this.server = new ApolloServer({
      typeDefs: config.typeDefs,
      resolvers: config.resolvers,
      introspection: config.introspection || false,
      plugins: [
        this.createPerformancePlugin(),
        this.createSecurityPlugin(),
        this.createErrorPlugin()
      ],
      formatError: this.formatError.bind(this)
    });
  }
  
  private createPerformancePlugin() {
    return {
      requestDidStart: () => ({
        didResolveOperation: async (requestContext: any) => {
          const { request, operationName } = requestContext;
          
          // Analyze query with Context Optimizer
          await this.subAgents?.contextOptimizer?.analyzeGraphQLQuery({
            query: request.query,
            variables: request.variables,
            operationName,
            timestamp: new Date()
          });
        },
        
        didEncounterErrors: async (requestContext: any) => {
          const { errors, request } = requestContext;
          
          // Analyze errors with Bug Hunter
          for (const error of errors) {
            await this.subAgents?.bugHunter?.analyzeGraphQLError({
              error,
              query: request.query,
              variables: request.variables,
              operationName: requestContext.operationName,
              context: requestContext.context
            });
          }
        },
        
        willSendResponse: async (requestContext: any) => {
          const { response, request } = requestContext;
          const executionTime = Date.now() - requestContext.request.http.startTime;
          
          // Performance monitoring
          await this.subAgents?.performanceAnalyzer?.analyzeGraphQLExecution({
            query: request.query,
            variables: request.variables,
            operationName: requestContext.operationName,
            executionTime,
            responseSize: JSON.stringify(response.body).length,
            errors: response.body.errors?.length || 0,
            cacheHits: response.body.extensions?.cacheHits || 0
          });
        }
      })
    };
  }
  
  private createSecurityPlugin() {
    return {
      requestDidStart: () => ({
        didResolveOperation: async (requestContext: any) => {
          const { request, context } = requestContext;
          
          // Rate limiting
          try {
            await this.rateLimiter.consume(context.req.ip);
          } catch (rateLimiterRes) {
            throw new GraphQLError('Rate limit exceeded', {
              extensions: {
                code: 'RATE_LIMIT_EXCEEDED',
                retryAfter: rateLimiterRes.msBeforeNext
              }
            });
          }
          
          // Security validation with Security Auditor
          const securityCheck = await this.subAgents?.securityAuditor?.validateGraphQLQuery({
            query: request.query,
            variables: request.variables,
            operationName: requestContext.operationName,
            user: context.user,
            request: {
              ip: context.req.ip,
              headers: context.req.headers,
              userAgent: context.req.get('User-Agent')
            },
            maxDepth: this.config.security.maxQueryDepth,
            maxComplexity: this.config.security.maxQueryComplexity
          });
          
          if (!securityCheck?.valid) {
            throw new GraphQLError(securityCheck?.reason || 'Security validation failed', {
              extensions: {
                code: 'SECURITY_VIOLATION',
                details: securityCheck?.details
              }
            });
          }
        }
      })
    };
  }
  
  private createErrorPlugin() {
    return {
      requestDidStart: () => ({
        didEncounterErrors: async (requestContext: any) => {
          const { errors, request, context } = requestContext;
          
          for (const error of errors) {
            // Comprehensive error analysis
            await this.subAgents?.bugHunter?.analyzeGraphQLError({
              error,
              query: request.query,
              variables: request.variables,
              operationName: requestContext.operationName,
              user: context.user,
              request: {
                ip: context.req.ip,
                headers: context.req.headers,
                userAgent: context.req.get('User-Agent')
              },
              stackTrace: error.stack,
              timestamp: new Date()
            });
          }
        }
      })
    };
  }
  
  private formatError(formattedError: any, error: any): any {
    // Don't expose internal errors in production
    if (process.env.NODE_ENV === 'production') {
      // Log the original error for debugging
      console.error('GraphQL Error:', error);
      
      // Return sanitized error
      return {
        message: formattedError.message,
        code: formattedError.extensions?.code || 'INTERNAL_ERROR',
        timestamp: new Date().toISOString()
      };
    }
    
    return formattedError;
  }
  
  public async start(port: number = 4000): Promise<void> {
    await this.server.start();
    
    const app = express();
    
    app.use(
      '/graphql',
      express.json(),
      expressMiddleware(this.server, {
        context: async ({ req, res }): Promise<GraphQLContext> => {
          // Extract user from JWT token (implementation depends on your auth system)
          const user = await this.extractUserFromRequest(req);
          
          return {
            user,
            req,
            res,
            subAgents: this.subAgents,
            dataSources: {
              // Initialize your data sources here
            }
          };
        }
      })
    );
    
    app.listen(port, () => {
      console.log(`GraphQL server running at http://localhost:${port}/graphql`);
    });
  }
  
  private async extractUserFromRequest(req: any): Promise<any> {
    // Implementation depends on your authentication system
    const token = req.headers.authorization?.replace('Bearer ', '');
    if (!token) return null;
    
    // Validate token and return user
    return null;
  }
  
  public getServer(): ApolloServer {
    return this.server;
  }
}

// Enhanced Resolvers with Sub-Agent Integration
class EnhancedResolvers {
  private subAgents?: SubAgentContainer;
  
  constructor(subAgents?: SubAgentContainer) {
    this.subAgents = subAgents;
  }
  
  public createUserResolvers() {
    return {
      Query: {
        users: async (parent: any, args: any, context: GraphQLContext) => {
          const startTime = performance.now();
          
          try {
            // Optimize query parameters
            const optimizedArgs = await this.subAgents?.contextOptimizer?.optimizeGraphQLArgs({
              args,
              fieldName: 'users',
              user: context.user,
              context: 'query'
            }) || args;
            
            // Security check
            const securityCheck = await this.subAgents?.securityAuditor?.validateFieldAccess({
              fieldName: 'users',
              args: optimizedArgs,
              user: context.user,
              operation: 'query'
            });
            
            if (!securityCheck?.allowed) {
              throw new GraphQLError(securityCheck?.reason || 'Access denied', {
                extensions: { code: 'FORBIDDEN' }
              });
            }
            
            // Fetch users (mock implementation)
            const users = await this.fetchUsers(optimizedArgs);
            
            // Performance monitoring
            const executionTime = performance.now() - startTime;
            await this.subAgents?.performanceAnalyzer?.analyzeResolverExecution({
              fieldName: 'users',
              args: optimizedArgs,
              executionTime,
              resultCount: users.length,
              user: context.user
            });
            
            return users;
            
          } catch (error) {
            await this.subAgents?.bugHunter?.analyzeResolverError({
              error: error as Error,
              fieldName: 'users',
              args,
              user: context.user,
              executionTime: performance.now() - startTime
            });
            
            throw error;
          }
        },
        
        user: async (parent: any, args: { id: string }, context: GraphQLContext) => {
          const startTime = performance.now();
          
          try {
            // Security check
            const securityCheck = await this.subAgents?.securityAuditor?.validateFieldAccess({
              fieldName: 'user',
              args,
              user: context.user,
              operation: 'query'
            });
            
            if (!securityCheck?.allowed) {
              throw new GraphQLError(securityCheck?.reason || 'Access denied', {
                extensions: { code: 'FORBIDDEN' }
              });
            }
            
            // Fetch user
            const user = await this.fetchUser(args.id);
            
            if (!user) {
              throw new GraphQLError('User not found', {
                extensions: { code: 'NOT_FOUND' }
              });
            }
            
            // Performance monitoring
            const executionTime = performance.now() - startTime;
            await this.subAgents?.performanceAnalyzer?.analyzeResolverExecution({
              fieldName: 'user',
              args,
              executionTime,
              resultCount: 1,
              user: context.user
            });
            
            return user;
            
          } catch (error) {
            await this.subAgents?.bugHunter?.analyzeResolverError({
              error: error as Error,
              fieldName: 'user',
              args,
              user: context.user,
              executionTime: performance.now() - startTime
            });
            
            throw error;
          }
        }
      },
      
      Mutation: {
        createUser: async (parent: any, args: { input: any }, context: GraphQLContext) => {
          const startTime = performance.now();
          
          try {
            // Security validation
            const securityCheck = await this.subAgents?.securityAuditor?.validateFieldAccess({
              fieldName: 'createUser',
              args,
              user: context.user,
              operation: 'mutation'
            });
            
            if (!securityCheck?.allowed) {
              throw new GraphQLError(securityCheck?.reason || 'Access denied', {
                extensions: { code: 'FORBIDDEN' }
              });
            }
            
            // Sanitize input data
            const sanitizedInput = await this.subAgents?.securityAuditor?.sanitizeInputData({
              data: args.input,
              resourceType: 'user',
              action: 'create'
            }) || args.input;
            
            // Optimize data for creation
            const optimizedInput = await this.subAgents?.contextOptimizer?.optimizeResourceData({
              originalData: sanitizedInput,
              resourceType: 'user',
              action: 'create',
              userContext: context.user
            }) || sanitizedInput;
            
            // Create user
            const newUser = await this.createUser(optimizedInput);
            
            // Performance monitoring
            const executionTime = performance.now() - startTime;
            await this.subAgents?.performanceAnalyzer?.analyzeResolverExecution({
              fieldName: 'createUser',
              args: { input: optimizedInput },
              executionTime,
              resultCount: 1,
              user: context.user,
              operationType: 'create'
            });
            
            return newUser;
            
          } catch (error) {
            await this.subAgents?.bugHunter?.analyzeResolverError({
              error: error as Error,
              fieldName: 'createUser',
              args,
              user: context.user,
              executionTime: performance.now() - startTime
            });
            
            throw error;
          }
        }
      }
    };
  }
  
  // Mock data layer methods
  private async fetchUsers(args: any): Promise<any[]> {
    // Implementation depends on your data layer
    return [];
  }
  
  private async fetchUser(id: string): Promise<any> {
    // Implementation depends on your data layer
    return null;
  }
  
  private async createUser(input: any): Promise<any> {
    // Implementation depends on your data layer
    return { id: 'new-id', ...input };
  }
}

// Usage Example
const typeDefs = `
  type User {
    id: ID!
    name: String!
    email: String!
    age: Int
    role: Role!
    createdAt: String!
    updatedAt: String!
  }
  
  enum Role {
    USER
    ADMIN
  }
  
  input CreateUserInput {
    name: String!
    email: String!
    age: Int
    role: Role = USER
  }
  
  input UpdateUserInput {
    name: String
    email: String
    age: Int
    role: Role
  }
  
  type Query {
    users(limit: Int = 10, offset: Int = 0, search: String): [User!]!
    user(id: ID!): User
  }
  
  type Mutation {
    createUser(input: CreateUserInput!): User!
    updateUser(id: ID!, input: UpdateUserInput!): User!
    deleteUser(id: ID!): Boolean!
  }
  
  type Subscription {
    userCreated: User!
    userUpdated: User!
    userDeleted: ID!
  }
`;

const enhancedResolvers = new EnhancedResolvers(subAgents);
const resolvers = enhancedResolvers.createUserResolvers();

const graphqlConfig: GraphQLConfig = {
  typeDefs,
  resolvers,
  introspection: process.env.NODE_ENV !== 'production',
  playground: process.env.NODE_ENV !== 'production',
  rateLimit: {
    points: 100, // Number of requests
    duration: 60 // Per 60 seconds
  },
  security: {
    maxQueryDepth: 10,
    maxQueryComplexity: 1000,
    enableQueryWhitelist: process.env.NODE_ENV === 'production'
  }
};

const graphqlServer = new GraphQLAPIServer(graphqlConfig, subAgents);
graphqlServer.start(4000);

export { GraphQLAPIServer, EnhancedResolvers, GraphQLConfig };
```

## 🔄 Real-time API Patterns

### 1. WebSocket API Pattern

**Description**: Real-time bidirectional communication using WebSockets  
**Best For**: Chat applications, live updates, gaming, collaborative tools  
**Sub-Agent Integration**: Performance Analyzer for connection monitoring, Security Auditor for message validation

```typescript
// WebSocket API Pattern with Sub-Agent Integration
import WebSocket, { WebSocketServer } from 'ws';
import { IncomingMessage } from 'http';
import { SubAgentContainer } from '../types/SubAgents';
import { EventEmitter } from 'events';
import jwt from 'jsonwebtoken';
import { RateLimiterMemory } from 'rate-limiter-flexible';

interface WebSocketMessage {
  type: string;
  payload: any;
  id?: string;
  timestamp?: number;
  userId?: string;
}

interface WebSocketClient {
  id: string;
  userId?: string;
  socket: WebSocket;
  isAlive: boolean;
  subscriptions: Set<string>;
  metadata: {
    ip: string;
    userAgent: string;
    connectedAt: Date;
    lastActivity: Date;
  };
}

interface WebSocketConfig {
  port: number;
  path: string;
  maxConnections: number;
  heartbeatInterval: number;
  messageRateLimit: {
    points: number;
    duration: number;
  };
  security: {
    jwtSecret: string;
    requireAuth: boolean;
    allowedOrigins: string[];
  };
}

class WebSocketAPIServer extends EventEmitter {
  private wss: WebSocketServer;
  private clients: Map<string, WebSocketClient> = new Map();
  private config: WebSocketConfig;
  private subAgents?: SubAgentContainer;
  private rateLimiter: RateLimiterMemory;
  private heartbeatInterval: NodeJS.Timeout;
  private messageHandlers: Map<string, (client: WebSocketClient, message: WebSocketMessage) => Promise<void>> = new Map();
  
  constructor(config: WebSocketConfig, subAgents?: SubAgentContainer) {
    super();
    this.config = config;
    this.subAgents = subAgents;
    
    // Setup rate limiter
    this.rateLimiter = new RateLimiterMemory({
      points: config.messageRateLimit.points,
      duration: config.messageRateLimit.duration
    });
    
    // Create WebSocket server
    this.wss = new WebSocketServer({
      port: config.port,
      path: config.path,
      verifyClient: this.verifyClient.bind(this)
    });
    
    this.setupEventHandlers();
    this.startHeartbeat();
  }
  
  private async verifyClient(info: {
    origin: string;
    secure: boolean;
    req: IncomingMessage;
  }): Promise<boolean> {
    try {
      // Check origin
      if (this.config.security.allowedOrigins.length > 0) {
        if (!this.config.security.allowedOrigins.includes(info.origin)) {
          await this.subAgents?.securityAuditor?.logSecurityViolation({
            type: 'invalid_origin',
            origin: info.origin,
            ip: info.req.socket.remoteAddress,
            timestamp: new Date()
          });
          return false;
        }
      }
      
      // Check connection limit
      if (this.clients.size >= this.config.maxConnections) {
        await this.subAgents?.performanceAnalyzer?.logConnectionLimit({
          currentConnections: this.clients.size,
          maxConnections: this.config.maxConnections,
          rejectedIp: info.req.socket.remoteAddress,
          timestamp: new Date()
        });
        return false;
      }
      
      return true;
      
    } catch (error) {
      await this.subAgents?.bugHunter?.analyzeWebSocketError({
        error: error as Error,
        context: 'client_verification',
        clientInfo: info,
        timestamp: new Date()
      });
      return false;
    }
  }
  
  private setupEventHandlers(): void {
    this.wss.on('connection', async (socket: WebSocket, request: IncomingMessage) => {
      const startTime = performance.now();
      
      try {
        // Create client
        const client = await this.createClient(socket, request);
        
        // Setup client event handlers
        this.setupClientHandlers(client);
        
        // Performance monitoring
        const connectionTime = performance.now() - startTime;
        await this.subAgents?.performanceAnalyzer?.analyzeWebSocketConnection({
          clientId: client.id,
          userId: client.userId,
          ip: client.metadata.ip,
          userAgent: client.metadata.userAgent,
          connectionTime,
          totalConnections: this.clients.size,
          timestamp: new Date()
        });
        
        this.emit('clientConnected', client);
        
      } catch (error) {
        await this.subAgents?.bugHunter?.analyzeWebSocketError({
          error: error as Error,
          context: 'connection_setup',
          request: {
            url: request.url,
            headers: request.headers,
            ip: request.socket.remoteAddress
          },
          timestamp: new Date()
        });
        
        socket.close(1011, 'Internal server error');
      }
    });
    
    this.wss.on('error', async (error: Error) => {
      await this.subAgents?.bugHunter?.analyzeWebSocketError({
        error,
        context: 'server_error',
        timestamp: new Date()
      });
    });
  }
  
  private async createClient(socket: WebSocket, request: IncomingMessage): Promise<WebSocketClient> {
    const clientId = this.generateClientId();
    const ip = request.socket.remoteAddress || 'unknown';
    const userAgent = request.headers['user-agent'] || 'unknown';
    
    // Extract user from token if auth is required
    let userId: string | undefined;
    if (this.config.security.requireAuth) {
      const token = this.extractTokenFromRequest(request);
      if (token) {
        try {
          const decoded = jwt.verify(token, this.config.security.jwtSecret) as any;
          userId = decoded.userId || decoded.sub;
        } catch (error) {
          await this.subAgents?.securityAuditor?.logSecurityViolation({
            type: 'invalid_token',
            token,
            ip,
            userAgent,
            error: error as Error,
            timestamp: new Date()
          });
          throw new Error('Invalid authentication token');
        }
      } else {
        throw new Error('Authentication required');
      }
    }
    
    const client: WebSocketClient = {
      id: clientId,
      userId,
      socket,
      isAlive: true,
      subscriptions: new Set(),
      metadata: {
        ip,
        userAgent,
        connectedAt: new Date(),
        lastActivity: new Date()
      }
    };
    
    this.clients.set(clientId, client);
    return client;
  }
  
  private setupClientHandlers(client: WebSocketClient): void {
    client.socket.on('message', async (data: Buffer) => {
      const startTime = performance.now();
      
      try {
        // Rate limiting
        await this.rateLimiter.consume(client.id);
        
        // Parse message
        const message: WebSocketMessage = JSON.parse(data.toString());
        message.timestamp = Date.now();
        message.userId = client.userId;
        
        // Update client activity
        client.metadata.lastActivity = new Date();
        client.isAlive = true;
        
        // Security validation
        const securityCheck = await this.subAgents?.securityAuditor?.validateWebSocketMessage({
          message,
          client: {
            id: client.id,
            userId: client.userId,
            ip: client.metadata.ip,
            userAgent: client.metadata.userAgent
          },
          timestamp: new Date()
        });
        
        if (!securityCheck?.valid) {
          await this.sendError(client, {
            type: 'security_error',
            message: securityCheck?.reason || 'Security validation failed',
            code: 'SECURITY_VIOLATION'
          });
          return;
        }
        
        // Handle message
        await this.handleMessage(client, message);
        
        // Performance monitoring
        const processingTime = performance.now() - startTime;
        await this.subAgents?.performanceAnalyzer?.analyzeWebSocketMessage({
          messageType: message.type,
          clientId: client.id,
          userId: client.userId,
          messageSize: data.length,
          processingTime,
          timestamp: new Date()
        });
        
      } catch (error) {
        if (error.name === 'RateLimiterError') {
          await this.sendError(client, {
            type: 'rate_limit_error',
            message: 'Rate limit exceeded',
            code: 'RATE_LIMIT_EXCEEDED'
          });
        } else {
          const processingTime = performance.now() - startTime;
          
          await this.subAgents?.bugHunter?.analyzeWebSocketError({
            error: error as Error,
            context: 'message_handling',
            client: {
              id: client.id,
              userId: client.userId,
              ip: client.metadata.ip
            },
            message: data.toString(),
            processingTime,
            timestamp: new Date()
          });
          
          await this.sendError(client, {
            type: 'internal_error',
            message: 'An error occurred while processing your message',
            code: 'INTERNAL_ERROR'
          });
        }
      }
    });
    
    client.socket.on('close', async (code: number, reason: Buffer) => {
      await this.handleClientDisconnect(client, code, reason.toString());
    });
    
    client.socket.on('error', async (error: Error) => {
      await this.subAgents?.bugHunter?.analyzeWebSocketError({
        error,
        context: 'client_socket_error',
        client: {
          id: client.id,
          userId: client.userId,
          ip: client.metadata.ip
        },
        timestamp: new Date()
      });
    });
    
    client.socket.on('pong', () => {
      client.isAlive = true;
      client.metadata.lastActivity = new Date();
    });
  }
  
  private async handleMessage(client: WebSocketClient, message: WebSocketMessage): Promise<void> {
    const handler = this.messageHandlers.get(message.type);
    
    if (!handler) {
      await this.sendError(client, {
        type: 'unknown_message_type',
        message: `Unknown message type: ${message.type}`,
        code: 'UNKNOWN_MESSAGE_TYPE'
      });
      return;
    }
    
    try {
      await handler(client, message);
    } catch (error) {
      await this.subAgents?.bugHunter?.analyzeWebSocketError({
        error: error as Error,
        context: 'message_handler_execution',
        messageType: message.type,
        client: {
          id: client.id,
          userId: client.userId,
          ip: client.metadata.ip
        },
        message,
        timestamp: new Date()
      });
      
      throw error;
    }
  }
  
  private async handleClientDisconnect(client: WebSocketClient, code: number, reason: string): Promise<void> {
    const connectionDuration = Date.now() - client.metadata.connectedAt.getTime();
    
    // Remove client
    this.clients.delete(client.id);
    
    // Performance monitoring
    await this.subAgents?.performanceAnalyzer?.analyzeWebSocketDisconnection({
      clientId: client.id,
      userId: client.userId,
      connectionDuration,
      disconnectCode: code,
      disconnectReason: reason,
      totalConnections: this.clients.size,
      timestamp: new Date()
    });
    
    this.emit('clientDisconnected', { client, code, reason });
  }
  
  public registerMessageHandler(
    messageType: string,
    handler: (client: WebSocketClient, message: WebSocketMessage) => Promise<void>
  ): void {
    this.messageHandlers.set(messageType, handler);
  }
  
  public async sendToClient(clientId: string, message: WebSocketMessage): Promise<boolean> {
    const client = this.clients.get(clientId);
    if (!client || client.socket.readyState !== WebSocket.OPEN) {
      return false;
    }
    
    try {
      const messageData = JSON.stringify({
        ...message,
        timestamp: Date.now()
      });
      
      client.socket.send(messageData);
      
      // Performance monitoring
      await this.subAgents?.performanceAnalyzer?.analyzeWebSocketSend({
        clientId,
        userId: client.userId,
        messageType: message.type,
        messageSize: messageData.length,
        timestamp: new Date()
      });
      
      return true;
      
    } catch (error) {
      await this.subAgents?.bugHunter?.analyzeWebSocketError({
        error: error as Error,
        context: 'send_to_client',
        clientId,
        message,
        timestamp: new Date()
      });
      
      return false;
    }
  }
  
  public async sendToUser(userId: string, message: WebSocketMessage): Promise<number> {
    let sentCount = 0;
    
    for (const client of this.clients.values()) {
      if (client.userId === userId) {
        const sent = await this.sendToClient(client.id, message);
        if (sent) sentCount++;
      }
    }
    
    return sentCount;
  }
  
  public async broadcast(message: WebSocketMessage, excludeClients: string[] = []): Promise<number> {
    let sentCount = 0;
    
    for (const client of this.clients.values()) {
      if (!excludeClients.includes(client.id)) {
        const sent = await this.sendToClient(client.id, message);
        if (sent) sentCount++;
      }
    }
    
    // Performance monitoring
    await this.subAgents?.performanceAnalyzer?.analyzeWebSocketBroadcast({
      messageType: message.type,
      totalClients: this.clients.size,
      sentCount,
      excludedCount: excludeClients.length,
      messageSize: JSON.stringify(message).length,
      timestamp: new Date()
    });
    
    return sentCount;
  }
  
  private async sendError(client: WebSocketClient, error: {
    type: string;
    message: string;
    code: string;
  }): Promise<void> {
    await this.sendToClient(client.id, {
      type: 'error',
      payload: error
    });
  }
  
  private startHeartbeat(): void {
    this.heartbeatInterval = setInterval(async () => {
      const deadClients: string[] = [];
      
      for (const [clientId, client] of this.clients.entries()) {
        if (!client.isAlive) {
          deadClients.push(clientId);
          client.socket.terminate();
        } else {
          client.isAlive = false;
          client.socket.ping();
        }
      }
      
      // Remove dead clients
      for (const clientId of deadClients) {
        this.clients.delete(clientId);
      }
      
      // Performance monitoring
      if (deadClients.length > 0) {
        await this.subAgents?.performanceAnalyzer?.analyzeWebSocketHeartbeat({
          totalClients: this.clients.size,
          deadClients: deadClients.length,
          timestamp: new Date()
        });
      }
      
    }, this.config.heartbeatInterval);
  }
  
  private generateClientId(): string {
    return `client_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
  }
  
  private extractTokenFromRequest(request: IncomingMessage): string | null {
    const authHeader = request.headers.authorization;
    if (authHeader && authHeader.startsWith('Bearer ')) {
      return authHeader.substring(7);
    }
    
    // Check query parameter
    const url = new URL(request.url!, `http://${request.headers.host}`);
    return url.searchParams.get('token');
  }
  
  public getConnectionStats(): {
    totalConnections: number;
    authenticatedConnections: number;
    connectionsByUser: Map<string, number>;
    averageConnectionDuration: number;
  } {
    const authenticatedConnections = Array.from(this.clients.values())
      .filter(client => client.userId).length;
    
    const connectionsByUser = new Map<string, number>();
    for (const client of this.clients.values()) {
      if (client.userId) {
        connectionsByUser.set(client.userId, (connectionsByUser.get(client.userId) || 0) + 1);
      }
    }
    
    const now = Date.now();
    const totalDuration = Array.from(this.clients.values())
      .reduce((sum, client) => sum + (now - client.metadata.connectedAt.getTime()), 0);
    const averageConnectionDuration = this.clients.size > 0 ? totalDuration / this.clients.size : 0;
    
    return {
      totalConnections: this.clients.size,
      authenticatedConnections,
      connectionsByUser,
      averageConnectionDuration
    };
  }
  
  public async close(): Promise<void> {
    // Clear heartbeat
    if (this.heartbeatInterval) {
      clearInterval(this.heartbeatInterval);
    }
    
    // Close all client connections
    for (const client of this.clients.values()) {
      client.socket.close(1001, 'Server shutting down');
    }
    
    // Close server
    this.wss.close();
    
    // Performance monitoring
    await this.subAgents?.performanceAnalyzer?.analyzeWebSocketServerShutdown({
      totalConnections: this.clients.size,
      timestamp: new Date()
    });
  }
}

// Usage Example
const wsConfig: WebSocketConfig = {
  port: 8080,
  path: '/ws',
  maxConnections: 1000,
  heartbeatInterval: 30000, // 30 seconds
  messageRateLimit: {
    points: 10, // 10 messages
    duration: 1 // per second
  },
  security: {
    jwtSecret: process.env.JWT_SECRET || 'your-secret-key',
    requireAuth: true,
    allowedOrigins: ['http://localhost:3000', 'https://yourdomain.com']
  }
};

const wsServer = new WebSocketAPIServer(wsConfig, subAgents);

// Register message handlers
wsServer.registerMessageHandler('chat_message', async (client, message) => {
  // Broadcast chat message to all clients
  await wsServer.broadcast({
    type: 'chat_message',
    payload: {
      userId: client.userId,
      message: message.payload.message,
      timestamp: Date.now()
    }
  }, [client.id]); // Exclude sender
});

wsServer.registerMessageHandler('subscribe', async (client, message) => {
  const { channel } = message.payload;
  client.subscriptions.add(channel);
  
  await wsServer.sendToClient(client.id, {
    type: 'subscription_confirmed',
    payload: { channel }
  });
});

export { WebSocketAPIServer, WebSocketConfig, WebSocketClient, WebSocketMessage };
```

## 🔐 API Security Patterns

### 1. JWT Authentication Pattern

**Description**: Stateless authentication using JSON Web Tokens  
**Best For**: Distributed systems, microservices, mobile APIs  
**Sub-Agent Integration**: Security Auditor for token validation, Context Optimizer for token optimization

```typescript
// JWT Authentication Pattern with Sub-Agent Integration
import jwt from 'jsonwebtoken';
import bcrypt from 'bcrypt';
import { Request, Response, NextFunction } from 'express';
import { SubAgentContainer } from '../types/SubAgents';
import { RateLimiterMemory } from 'rate-limiter-flexible';

interface JWTConfig {
  secret: string;
  refreshSecret: string;
  accessTokenExpiry: string;
  refreshTokenExpiry: string;
  issuer: string;
  audience: string;
  algorithm: jwt.Algorithm;
}

interface AuthUser {
  id: string;
  email: string;
  role: string;
  permissions: string[];
  lastLogin?: Date;
  isActive: boolean;
}

interface TokenPayload {
  userId: string;
  email: string;
  role: string;
  permissions: string[];
  iat: number;
  exp: number;
  iss: string;
  aud: string;
}

class JWTAuthenticationService {
  private config: JWTConfig;
  private subAgents?: SubAgentContainer;
  private loginAttemptLimiter: RateLimiterMemory;
  private tokenBlacklist: Set<string> = new Set();
  
  constructor(config: JWTConfig, subAgents?: SubAgentContainer) {
    this.config = config;
    this.subAgents = subAgents;
    
    // Setup rate limiter for login attempts
    this.loginAttemptLimiter = new RateLimiterMemory({
      points: 5, // 5 attempts
      duration: 900, // per 15 minutes
      blockDuration: 900 // block for 15 minutes
    });
  }
  
  public async authenticate(email: string, password: string, request: {
    ip: string;
    userAgent: string;
    timestamp: Date;
  }): Promise<{
    success: boolean;
    tokens?: { accessToken: string; refreshToken: string };
    user?: AuthUser;
    error?: string;
  }> {
    const startTime = performance.now();
    
    try {
      // Rate limiting
      await this.loginAttemptLimiter.consume(request.ip);
      
      // Security validation
      const securityCheck = await this.subAgents?.securityAuditor?.validateAuthenticationAttempt({
        email,
        ip: request.ip,
        userAgent: request.userAgent,
        timestamp: request.timestamp
      });
      
      if (!securityCheck?.allowed) {
        return {
          success: false,
          error: securityCheck?.reason || 'Authentication blocked for security reasons'
        };
      }
      
      // Find user (mock implementation)
      const user = await this.findUserByEmail(email);
      if (!user) {
        await this.subAgents?.securityAuditor?.logAuthenticationFailure({
          email,
          reason: 'user_not_found',
          ip: request.ip,
          userAgent: request.userAgent,
          timestamp: request.timestamp
        });
        
        return {
          success: false,
          error: 'Invalid credentials'
        };
      }
      
      if (!user.isActive) {
        await this.subAgents?.securityAuditor?.logAuthenticationFailure({
          email,
          reason: 'user_inactive',
          ip: request.ip,
          userAgent: request.userAgent,
          timestamp: request.timestamp
        });
        
        return {
          success: false,
          error: 'Account is inactive'
        };
      }
      
      // Verify password
      const passwordValid = await bcrypt.compare(password, user.passwordHash);
      if (!passwordValid) {
        await this.subAgents?.securityAuditor?.logAuthenticationFailure({
          email,
          reason: 'invalid_password',
          ip: request.ip,
          userAgent: request.userAgent,
          timestamp: request.timestamp
        });
        
        return {
          success: false,
          error: 'Invalid credentials'
        };
      }
      
      // Generate tokens
      const tokens = await this.generateTokens(user);
      
      // Update last login
      await this.updateLastLogin(user.id, request);
      
      // Performance monitoring
      const authTime = performance.now() - startTime;
      await this.subAgents?.performanceAnalyzer?.analyzeAuthentication({
        userId: user.id,
        email: user.email,
        authTime,
        ip: request.ip,
        userAgent: request.userAgent,
        timestamp: request.timestamp
      });
      
      // Log successful authentication
      await this.subAgents?.securityAuditor?.logAuthenticationSuccess({
        userId: user.id,
        email: user.email,
        ip: request.ip,
        userAgent: request.userAgent,
        timestamp: request.timestamp
      });
      
      return {
        success: true,
        tokens,
        user: {
          id: user.id,
          email: user.email,
          role: user.role,
          permissions: user.permissions,
          lastLogin: new Date(),
          isActive: user.isActive
        }
      };
      
    } catch (error) {
      if (error.name === 'RateLimiterError') {
        await this.subAgents?.securityAuditor?.logRateLimitViolation({
          type: 'authentication',
          ip: request.ip,
          email,
          timestamp: request.timestamp
        });
        
        return {
          success: false,
          error: 'Too many login attempts. Please try again later.'
        };
      }
      
      const authTime = performance.now() - startTime;
      await this.subAgents?.bugHunter?.analyzeAuthenticationError({
        error: error as Error,
        email,
        ip: request.ip,
        userAgent: request.userAgent,
        authTime,
        timestamp: request.timestamp
      });
      
      return {
        success: false,
        error: 'An error occurred during authentication'
      };
    }
  }
  
  public async generateTokens(user: AuthUser): Promise<{
    accessToken: string;
    refreshToken: string;
  }> {
    const payload: Omit<TokenPayload, 'iat' | 'exp' | 'iss' | 'aud'> = {
      userId: user.id,
      email: user.email,
      role: user.role,
      permissions: user.permissions
    };
    
    // Optimize token payload with Context Optimizer
    const optimizedPayload = await this.subAgents?.contextOptimizer?.optimizeTokenPayload({
      originalPayload: payload,
      user,
      context: 'token_generation'
    }) || payload;
    
    const accessToken = jwt.sign(optimizedPayload, this.config.secret, {
      expiresIn: this.config.accessTokenExpiry,
      issuer: this.config.issuer,
      audience: this.config.audience,
      algorithm: this.config.algorithm
    });
    
    const refreshToken = jwt.sign(
      { userId: user.id, type: 'refresh' },
      this.config.refreshSecret,
      {
        expiresIn: this.config.refreshTokenExpiry,
        issuer: this.config.issuer,
        audience: this.config.audience,
        algorithm: this.config.algorithm
      }
    );
    
    return { accessToken, refreshToken };
  }
  
  public async verifyToken(token: string): Promise<{
    valid: boolean;
    payload?: TokenPayload;
    error?: string;
  }> {
    try {
      // Check if token is blacklisted
      if (this.tokenBlacklist.has(token)) {
        return {
          valid: false,
          error: 'Token has been revoked'
        };
      }
      
      // Verify token
      const payload = jwt.verify(token, this.config.secret, {
        issuer: this.config.issuer,
        audience: this.config.audience,
        algorithms: [this.config.algorithm]
      }) as TokenPayload;
      
      // Security validation
      const securityCheck = await this.subAgents?.securityAuditor?.validateTokenPayload({
        payload,
        token,
        timestamp: new Date()
      });
      
      if (!securityCheck?.valid) {
        return {
          valid: false,
          error: securityCheck?.reason || 'Token security validation failed'
        };
      }
      
      return {
        valid: true,
        payload
      };
      
    } catch (error) {
      await this.subAgents?.bugHunter?.analyzeTokenVerificationError({
        error: error as Error,
        token,
        timestamp: new Date()
      });
      
      if (error.name === 'TokenExpiredError') {
        return {
          valid: false,
          error: 'Token has expired'
        };
      } else if (error.name === 'JsonWebTokenError') {
        return {
          valid: false,
          error: 'Invalid token'
        };
      }
      
      return {
        valid: false,
        error: 'Token verification failed'
      };
    }
  }
  
  public async refreshToken(refreshToken: string): Promise<{
    success: boolean;
    tokens?: { accessToken: string; refreshToken: string };
    error?: string;
  }> {
    try {
      // Verify refresh token
      const payload = jwt.verify(refreshToken, this.config.refreshSecret, {
        issuer: this.config.issuer,
        audience: this.config.audience,
        algorithms: [this.config.algorithm]
      }) as any;
      
      if (payload.type !== 'refresh') {
        return {
          success: false,
          error: 'Invalid refresh token'
        };
      }
      
      // Get user
      const user = await this.findUserById(payload.userId);
      if (!user || !user.isActive) {
        return {
          success: false,
          error: 'User not found or inactive'
        };
      }
      
      // Generate new tokens
      const tokens = await this.generateTokens(user);
      
      // Blacklist old refresh token
      this.tokenBlacklist.add(refreshToken);
      
      return {
        success: true,
        tokens
      };
      
    } catch (error) {
      await this.subAgents?.bugHunter?.analyzeTokenRefreshError({
        error: error as Error,
        refreshToken,
        timestamp: new Date()
      });
      
      return {
        success: false,
        error: 'Token refresh failed'
      };
    }
  }
  
  public revokeToken(token: string): void {
    this.tokenBlacklist.add(token);
  }
  
  public createAuthMiddleware() {
    return async (req: Request, res: Response, next: NextFunction) => {
      const startTime = performance.now();
      
      try {
        const authHeader = req.headers.authorization;
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
          return res.status(401).json({
            error: 'Authentication required',
            message: 'Missing or invalid authorization header',
            code: 'UNAUTHORIZED'
          });
        }
        
        const token = authHeader.substring(7);
        const verification = await this.verifyToken(token);
        
        if (!verification.valid) {
          return res.status(401).json({
            error: 'Authentication failed',
            message: verification.error,
            code: 'INVALID_TOKEN'
          });
        }
        
        // Add user to request
        req.user = verification.payload;
        
        // Performance monitoring
        const authTime = performance.now() - startTime;
        await this.subAgents?.performanceAnalyzer?.analyzeAuthMiddleware({
          userId: verification.payload!.userId,
          authTime,
          endpoint: `${req.method} ${req.path}`,
          ip: req.ip,
          timestamp: new Date()
        });
        
        next();
        
      } catch (error) {
        const authTime = performance.now() - startTime;
        await this.subAgents?.bugHunter?.analyzeAuthMiddlewareError({
          error: error as Error,
          endpoint: `${req.method} ${req.path}`,
          ip: req.ip,
          authTime,
          timestamp: new Date()
        });
        
        res.status(500).json({
          error: 'Authentication error',
          message: 'An error occurred during authentication',
          code: 'AUTH_ERROR'
        });
      }
    };
  }
  
  // Mock data layer methods
  private async findUserByEmail(email: string): Promise<any> {
    // Implementation depends on your data layer
    return null;
  }
  
  private async findUserById(id: string): Promise<any> {
    // Implementation depends on your data layer
    return null;
  }
  
  private async updateLastLogin(userId: string, request: any): Promise<void> {
    // Implementation depends on your data layer
  }
}

// Usage Example
const jwtConfig: JWTConfig = {
  secret: process.env.JWT_SECRET || 'your-access-token-secret',
  refreshSecret: process.env.JWT_REFRESH_SECRET || 'your-refresh-token-secret',
  accessTokenExpiry: '15m',
  refreshTokenExpiry: '7d',
  issuer: 'your-app-name',
  audience: 'your-app-users',
  algorithm: 'HS256'
};

const authService = new JWTAuthenticationService(jwtConfig, subAgents);
const authMiddleware = authService.createAuthMiddleware();

// Apply to routes
app.use('/api/protected', authMiddleware);

export { JWTAuthenticationService, JWTConfig, AuthUser, TokenPayload };
```

## 📊 Benefits of API Development Patterns

### 🎯 **Proven Solutions**
- Battle-tested patterns for common API challenges
- Reduced development time and effort
- Consistent implementation across projects
- Lower risk of architectural mistakes

### 🤖 **Deep Sub-Agent Integration**
- **Context Optimizer**: Query optimization, response caching, payload minimization
- **Security Auditor**: Authentication validation, input sanitization, threat detection
- **Performance Analyzer**: Endpoint monitoring, bottleneck identification, optimization suggestions
- **Bug Hunter**: Error analysis, debugging assistance, issue prevention

### 🔒 **Security by Design**
- Built-in security validations and protections
- Rate limiting and abuse prevention
- Input sanitization and output encoding
- Comprehensive audit logging

### ⚡ **Performance Optimized**
- Intelligent caching strategies
- Query optimization and batching
- Resource usage monitoring
- Scalability considerations

### 🛠 **Developer Experience**
- Type-safe implementations with TypeScript
- Comprehensive error handling
- Detailed logging and monitoring
- Easy testing and debugging

## 📋 Usage Guidelines

### 🎯 **Pattern Selection Criteria**

1. **API Type Requirements**:
   - RESTful APIs → Resource-Based API Pattern
   - Complex queries → GraphQL API Pattern
   - Real-time features → WebSocket API Pattern
   - Authentication needs → JWT Authentication Pattern

2. **Scalability Needs**:
   - High throughput → Performance-optimized patterns
   - Global distribution → Stateless patterns
   - Real-time updates → Event-driven patterns

3. **Security Requirements**:
   - Sensitive data → Enhanced security patterns
   - Public APIs → Rate limiting and validation
   - Enterprise → Comprehensive audit logging

### ✅ **Implementation Checklist**

- [ ] **Sub-Agent Integration**: Verify all Sub-Agents are properly configured
- [ ] **Security Validation**: Implement input validation and output sanitization
- [ ] **Performance Monitoring**: Add comprehensive metrics and logging
- [ ] **Error Handling**: Implement robust error handling and recovery
- [ ] **Rate Limiting**: Configure appropriate rate limits for endpoints
- [ ] **Documentation**: Create API documentation and usage examples
- [ ] **Testing**: Implement unit, integration, and security tests
- [ ] **Monitoring**: Set up alerts and dashboards for production monitoring

### ❌ **Anti-Patterns to Avoid**

- **Ignoring Security**: Skipping input validation or authentication
- **Poor Error Handling**: Exposing internal errors or stack traces
- **No Rate Limiting**: Allowing unlimited requests without protection
- **Inconsistent Responses**: Different response formats across endpoints
- **Missing Monitoring**: No performance or error tracking
- **Hardcoded Values**: Configuration values embedded in code
- **Synchronous Operations**: Blocking operations in async contexts

---

**Integration**: Works seamlessly with all Sub-Agent systems  
**Maintenance**: Self-optimizing patterns with continuous learning  
**Scalability**: Designed for high-performance, distributed environments