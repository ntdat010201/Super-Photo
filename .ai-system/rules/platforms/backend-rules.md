# ⚙️ Backend Development Rules - Universal Standards

> **🚀 Comprehensive backend development guidelines for scalable server-side applications**  
> Node.js/Laravel/Python with microservices, APIs, databases, and cloud deployment

---

## 🎯 Core Technology Stack

### Primary Frameworks (Choose One)
```json
// Node.js + Express + TypeScript
{
  "dependencies": {
    "express": "^4.18.0",
    "typescript": "^5.0.0",
    "@types/express": "^4.17.0",
    "@types/node": "^20.0.0",
    "cors": "^2.8.5",
    "helmet": "^7.0.0",
    "compression": "^1.7.4"
  }
}

// Laravel + PHP
{
  "require": {
    "laravel/framework": "^10.0",
    "laravel/sanctum": "^3.2",
    "laravel/horizon": "^5.15",
    "spatie/laravel-permission": "^5.10"
  }
}

// Python + FastAPI
{
  "dependencies": {
    "fastapi": "^0.100.0",
    "uvicorn": "^0.22.0",
    "pydantic": "^2.0.0",
    "sqlalchemy": "^2.0.0",
    "alembic": "^1.11.0"
  }
}
```

### Essential Dependencies
```json
{
  "dependencies": {
    // Database
    "mongoose": "^7.3.0", // MongoDB
    "prisma": "^4.16.0", // ORM
    "redis": "^4.6.0", // Caching
    "pg": "^8.11.0", // PostgreSQL
    "mysql2": "^3.5.0", // MySQL
    
    // Authentication & Security
    "jsonwebtoken": "^9.0.0",
    "bcryptjs": "^2.4.3",
    "passport": "^0.6.0",
    "express-rate-limit": "^6.8.0",
    "express-validator": "^7.0.0",
    
    // File Upload & Storage
    "multer": "^1.4.5",
    "aws-sdk": "^2.1400.0",
    "@aws-sdk/client-s3": "^3.350.0",
    "cloudinary": "^1.37.0",
    
    // Communication
    "nodemailer": "^6.9.0",
    "socket.io": "^4.7.0",
    "bull": "^4.10.0", // Job Queue
    
    // Monitoring & Logging
    "winston": "^3.9.0",
    "morgan": "^1.10.0",
    "@sentry/node": "^7.55.0",
    
    // Utilities
    "lodash": "^4.17.21",
    "moment": "^2.29.0",
    "uuid": "^9.0.0",
    "dotenv": "^16.1.0",
    "joi": "^17.9.0", // Validation
    "axios": "^1.4.0" // HTTP Client
  },
  "devDependencies": {
    // Testing
    "jest": "^29.5.0",
    "supertest": "^6.3.0",
    "@types/jest": "^29.5.0",
    
    // Code Quality
    "eslint": "^8.43.0",
    "prettier": "^2.8.0",
    "@typescript-eslint/eslint-plugin": "^5.60.0",
    "@typescript-eslint/parser": "^5.60.0",
    
    // Development
    "nodemon": "^2.0.22",
    "ts-node": "^10.9.0",
    "concurrently": "^8.2.0"
  }
}
```

---

## 📁 Standard Project Structure

### Node.js + Express Project Structure
```
src/
├── config/              # Configuration files
│   ├── database.ts      # Database configuration
│   ├── redis.ts         # Redis configuration
│   ├── aws.ts           # AWS services configuration
│   ├── email.ts         # Email service configuration
│   └── index.ts         # Main config export
├── controllers/         # Route controllers
│   ├── auth/
│   │   ├── AuthController.ts
│   │   ├── UserController.ts
│   │   └── index.ts
│   ├── api/
│   │   ├── ProductController.ts
│   │   ├── OrderController.ts
│   │   └── index.ts
│   └── admin/
├── middleware/          # Express middleware
│   ├── auth.ts          # Authentication middleware
│   ├── validation.ts    # Request validation
│   ├── errorHandler.ts  # Error handling
│   ├── rateLimiter.ts   # Rate limiting
│   ├── cors.ts          # CORS configuration
│   └── logger.ts        # Request logging
├── models/              # Database models
│   ├── User.ts
│   ├── Product.ts
│   ├── Order.ts
│   └── index.ts
├── routes/              # Route definitions
│   ├── auth.ts
│   ├── api/
│   │   ├── products.ts
│   │   ├── orders.ts
│   │   └── index.ts
│   ├── admin.ts
│   └── index.ts
├── services/            # Business logic services
│   ├── auth/
│   │   ├── AuthService.ts
│   │   ├── TokenService.ts
│   │   └── index.ts
│   ├── email/
│   │   ├── EmailService.ts
│   │   ├── templates/
│   │   └── index.ts
│   ├── storage/
│   │   ├── S3Service.ts
│   │   ├── LocalStorage.ts
│   │   └── index.ts
│   └── payment/
├── utils/               # Utility functions
│   ├── constants.ts     # Application constants
│   ├── helpers.ts       # Helper functions
│   ├── validators.ts    # Custom validators
│   ├── formatters.ts    # Data formatters
│   └── encryption.ts    # Encryption utilities
├── types/               # TypeScript type definitions
│   ├── auth.ts
│   ├── api.ts
│   ├── database.ts
│   └── common.ts
├── database/            # Database related files
│   ├── migrations/      # Database migrations
│   ├── seeders/         # Database seeders
│   ├── factories/       # Model factories
│   └── connection.ts    # Database connection
├── jobs/                # Background jobs
│   ├── EmailJob.ts
│   ├── ImageProcessingJob.ts
│   └── index.ts
├── events/              # Event handlers
│   ├── UserEvents.ts
│   ├── OrderEvents.ts
│   └── index.ts
├── tests/               # Test files
│   ├── unit/
│   ├── integration/
│   ├── e2e/
│   ├── fixtures/
│   └── helpers/
├── docs/                # API documentation
│   ├── swagger.yaml
│   └── README.md
├── app.ts               # Express app configuration
├── server.ts            # Server entry point
└── index.ts             # Main application entry
```

### Laravel Project Structure
```
app/
├── Console/             # Artisan commands
├── Events/              # Event classes
├── Exceptions/          # Exception handlers
├── Http/
│   ├── Controllers/     # Controllers
│   │   ├── Api/
│   │   ├── Auth/
│   │   └── Admin/
│   ├── Middleware/      # HTTP middleware
│   ├── Requests/        # Form request validation
│   └── Resources/       # API resources
├── Jobs/                # Queueable jobs
├── Listeners/           # Event listeners
├── Mail/                # Mailable classes
├── Models/              # Eloquent models
├── Notifications/       # Notification classes
├── Policies/            # Authorization policies
├── Providers/           # Service providers
├── Rules/               # Custom validation rules
└── Services/            # Business logic services

config/                  # Configuration files
database/
├── factories/           # Model factories
├── migrations/          # Database migrations
└── seeders/             # Database seeders

resources/
├── lang/                # Language files
├── views/               # Blade templates
└── js/                  # Frontend assets

routes/
├── api.php              # API routes
├── web.php              # Web routes
├── console.php          # Console routes
└── channels.php         # Broadcast channels

storage/
├── app/                 # Application files
├── framework/           # Framework files
└── logs/                # Log files

tests/
├── Feature/             # Feature tests
├── Unit/                # Unit tests
└── TestCase.php         # Base test case
```

---

## 🏗️ Architecture Patterns

### Clean Architecture Implementation
```typescript
// Domain Layer - Business Entities
// src/domain/entities/User.ts
export class User {
  constructor(
    public readonly id: string,
    public readonly email: string,
    public readonly name: string,
    private _password: string,
    public readonly createdAt: Date,
    public readonly updatedAt: Date
  ) {}

  public validatePassword(password: string): boolean {
    return bcrypt.compareSync(password, this._password);
  }

  public updatePassword(newPassword: string): void {
    this._password = bcrypt.hashSync(newPassword, 10);
  }

  public toJSON() {
    return {
      id: this.id,
      email: this.email,
      name: this.name,
      createdAt: this.createdAt,
      updatedAt: this.updatedAt,
    };
  }
}

// Domain Layer - Repository Interface
// src/domain/repositories/UserRepository.ts
export interface UserRepository {
  findById(id: string): Promise<User | null>;
  findByEmail(email: string): Promise<User | null>;
  create(userData: CreateUserData): Promise<User>;
  update(id: string, userData: UpdateUserData): Promise<User>;
  delete(id: string): Promise<void>;
  findAll(filters?: UserFilters): Promise<User[]>;
}

// Application Layer - Use Cases
// src/application/usecases/auth/LoginUseCase.ts
export class LoginUseCase {
  constructor(
    private userRepository: UserRepository,
    private tokenService: TokenService,
    private logger: Logger
  ) {}

  async execute(email: string, password: string): Promise<LoginResult> {
    try {
      // 1. Find user by email
      const user = await this.userRepository.findByEmail(email);
      if (!user) {
        throw new AuthenticationError('Invalid credentials');
      }

      // 2. Validate password
      if (!user.validatePassword(password)) {
        throw new AuthenticationError('Invalid credentials');
      }

      // 3. Generate tokens
      const accessToken = await this.tokenService.generateAccessToken(user.id);
      const refreshToken = await this.tokenService.generateRefreshToken(user.id);

      // 4. Log successful login
      this.logger.info('User logged in successfully', { userId: user.id });

      return {
        user: user.toJSON(),
        accessToken,
        refreshToken,
      };
    } catch (error) {
      this.logger.error('Login failed', { email, error: error.message });
      throw error;
    }
  }
}

// Infrastructure Layer - Repository Implementation
// src/infrastructure/repositories/MongoUserRepository.ts
export class MongoUserRepository implements UserRepository {
  constructor(private userModel: Model<UserDocument>) {}

  async findById(id: string): Promise<User | null> {
    const userDoc = await this.userModel.findById(id);
    return userDoc ? this.toDomain(userDoc) : null;
  }

  async findByEmail(email: string): Promise<User | null> {
    const userDoc = await this.userModel.findOne({ email });
    return userDoc ? this.toDomain(userDoc) : null;
  }

  async create(userData: CreateUserData): Promise<User> {
    const userDoc = await this.userModel.create({
      ...userData,
      password: bcrypt.hashSync(userData.password, 10),
    });
    return this.toDomain(userDoc);
  }

  private toDomain(userDoc: UserDocument): User {
    return new User(
      userDoc._id.toString(),
      userDoc.email,
      userDoc.name,
      userDoc.password,
      userDoc.createdAt,
      userDoc.updatedAt
    );
  }
}

// Interface Layer - Controller
// src/controllers/auth/AuthController.ts
export class AuthController {
  constructor(
    private loginUseCase: LoginUseCase,
    private registerUseCase: RegisterUseCase
  ) {}

  async login(req: Request, res: Response, next: NextFunction) {
    try {
      const { email, password } = req.body;
      
      const result = await this.loginUseCase.execute(email, password);
      
      res.status(200).json({
        success: true,
        data: result,
      });
    } catch (error) {
      next(error);
    }
  }

  async register(req: Request, res: Response, next: NextFunction) {
    try {
      const userData = req.body;
      
      const result = await this.registerUseCase.execute(userData);
      
      res.status(201).json({
        success: true,
        data: result,
      });
    } catch (error) {
      next(error);
    }
  }
}
```

### Dependency Injection Container
```typescript
// src/container/Container.ts
import { Container } from 'inversify';
import { TYPES } from './types';

// Repositories
import { UserRepository } from '../domain/repositories/UserRepository';
import { MongoUserRepository } from '../infrastructure/repositories/MongoUserRepository';

// Services
import { TokenService } from '../services/auth/TokenService';
import { EmailService } from '../services/email/EmailService';
import { Logger } from '../utils/Logger';

// Use Cases
import { LoginUseCase } from '../application/usecases/auth/LoginUseCase';
import { RegisterUseCase } from '../application/usecases/auth/RegisterUseCase';

// Controllers
import { AuthController } from '../controllers/auth/AuthController';

const container = new Container();

// Bind repositories
container.bind<UserRepository>(TYPES.UserRepository).to(MongoUserRepository);

// Bind services
container.bind<TokenService>(TYPES.TokenService).to(TokenService);
container.bind<EmailService>(TYPES.EmailService).to(EmailService);
container.bind<Logger>(TYPES.Logger).to(Logger);

// Bind use cases
container.bind<LoginUseCase>(TYPES.LoginUseCase).to(LoginUseCase);
container.bind<RegisterUseCase>(TYPES.RegisterUseCase).to(RegisterUseCase);

// Bind controllers
container.bind<AuthController>(TYPES.AuthController).to(AuthController);

export { container };

// src/container/types.ts
export const TYPES = {
  // Repositories
  UserRepository: Symbol.for('UserRepository'),
  
  // Services
  TokenService: Symbol.for('TokenService'),
  EmailService: Symbol.for('EmailService'),
  Logger: Symbol.for('Logger'),
  
  // Use Cases
  LoginUseCase: Symbol.for('LoginUseCase'),
  RegisterUseCase: Symbol.for('RegisterUseCase'),
  
  // Controllers
  AuthController: Symbol.for('AuthController'),
};
```

---

## 🔐 Authentication & Authorization

### JWT Authentication Implementation
```typescript
// src/services/auth/TokenService.ts
import jwt from 'jsonwebtoken';
import { Redis } from 'ioredis';

export class TokenService {
  constructor(
    private redis: Redis,
    private accessTokenSecret: string,
    private refreshTokenSecret: string,
    private accessTokenExpiry: string = '15m',
    private refreshTokenExpiry: string = '7d'
  ) {}

  async generateAccessToken(userId: string): Promise<string> {
    const payload = {
      userId,
      type: 'access',
      iat: Math.floor(Date.now() / 1000),
    };

    return jwt.sign(payload, this.accessTokenSecret, {
      expiresIn: this.accessTokenExpiry,
    });
  }

  async generateRefreshToken(userId: string): Promise<string> {
    const payload = {
      userId,
      type: 'refresh',
      iat: Math.floor(Date.now() / 1000),
    };

    const token = jwt.sign(payload, this.refreshTokenSecret, {
      expiresIn: this.refreshTokenExpiry,
    });

    // Store refresh token in Redis
    await this.redis.setex(
      `refresh_token:${userId}`,
      7 * 24 * 60 * 60, // 7 days in seconds
      token
    );

    return token;
  }

  async verifyAccessToken(token: string): Promise<{ userId: string }> {
    try {
      const payload = jwt.verify(token, this.accessTokenSecret) as any;
      
      if (payload.type !== 'access') {
        throw new Error('Invalid token type');
      }

      return { userId: payload.userId };
    } catch (error) {
      throw new Error('Invalid access token');
    }
  }

  async verifyRefreshToken(token: string): Promise<{ userId: string }> {
    try {
      const payload = jwt.verify(token, this.refreshTokenSecret) as any;
      
      if (payload.type !== 'refresh') {
        throw new Error('Invalid token type');
      }

      // Check if token exists in Redis
      const storedToken = await this.redis.get(`refresh_token:${payload.userId}`);
      if (storedToken !== token) {
        throw new Error('Token not found or expired');
      }

      return { userId: payload.userId };
    } catch (error) {
      throw new Error('Invalid refresh token');
    }
  }

  async revokeRefreshToken(userId: string): Promise<void> {
    await this.redis.del(`refresh_token:${userId}`);
  }

  async refreshAccessToken(refreshToken: string): Promise<{ accessToken: string; refreshToken: string }> {
    const { userId } = await this.verifyRefreshToken(refreshToken);
    
    // Revoke old refresh token
    await this.revokeRefreshToken(userId);
    
    // Generate new tokens
    const newAccessToken = await this.generateAccessToken(userId);
    const newRefreshToken = await this.generateRefreshToken(userId);
    
    return {
      accessToken: newAccessToken,
      refreshToken: newRefreshToken,
    };
  }
}

// src/middleware/auth.ts
import { Request, Response, NextFunction } from 'express';
import { TokenService } from '../services/auth/TokenService';
import { UserRepository } from '../domain/repositories/UserRepository';

interface AuthenticatedRequest extends Request {
  user?: {
    id: string;
    email: string;
    name: string;
    roles: string[];
  };
}

export const authenticate = (tokenService: TokenService, userRepository: UserRepository) => {
  return async (req: AuthenticatedRequest, res: Response, next: NextFunction) => {
    try {
      const authHeader = req.headers.authorization;
      
      if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return res.status(401).json({
          success: false,
          message: 'Access token required',
        });
      }

      const token = authHeader.substring(7);
      const { userId } = await tokenService.verifyAccessToken(token);
      
      const user = await userRepository.findById(userId);
      if (!user) {
        return res.status(401).json({
          success: false,
          message: 'User not found',
        });
      }

      req.user = {
        id: user.id,
        email: user.email,
        name: user.name,
        roles: user.roles || [],
      };

      next();
    } catch (error) {
      return res.status(401).json({
        success: false,
        message: 'Invalid access token',
      });
    }
  };
};

export const authorize = (roles: string[]) => {
  return (req: AuthenticatedRequest, res: Response, next: NextFunction) => {
    if (!req.user) {
      return res.status(401).json({
        success: false,
        message: 'Authentication required',
      });
    }

    const hasRole = roles.some(role => req.user!.roles.includes(role));
    
    if (!hasRole) {
      return res.status(403).json({
        success: false,
        message: 'Insufficient permissions',
      });
    }

    next();
  };
};
```

---

## 🗄️ Database Management

### Prisma ORM Setup
```prisma
// prisma/schema.prisma
generator client {
  provider = "prisma-client-js"
}

datasource db {
  provider = "postgresql"
  url      = env("DATABASE_URL")
}

model User {
  id        String   @id @default(cuid())
  email     String   @unique
  name      String
  password  String
  avatar    String?
  roles     Role[]
  posts     Post[]
  comments  Comment[]
  createdAt DateTime @default(now())
  updatedAt DateTime @updatedAt

  @@map("users")
}

model Role {
  id          String       @id @default(cuid())
  name        String       @unique
  description String?
  permissions Permission[]
  users       User[]
  createdAt   DateTime     @default(now())
  updatedAt   DateTime     @updatedAt

  @@map("roles")
}

model Permission {
  id          String @id @default(cuid())
  name        String @unique
  description String?
  roles       Role[]
  createdAt   DateTime @default(now())
  updatedAt   DateTime @updatedAt

  @@map("permissions")
}

model Post {
  id        String    @id @default(cuid())
  title     String
  content   String
  published Boolean   @default(false)
  authorId  String
  author    User      @relation(fields: [authorId], references: [id], onDelete: Cascade)
  comments  Comment[]
  tags      Tag[]
  createdAt DateTime  @default(now())
  updatedAt DateTime  @updatedAt

  @@map("posts")
}

model Comment {
  id        String   @id @default(cuid())
  content   String
  postId    String
  post      Post     @relation(fields: [postId], references: [id], onDelete: Cascade)
  authorId  String
  author    User     @relation(fields: [authorId], references: [id], onDelete: Cascade)
  createdAt DateTime @default(now())
  updatedAt DateTime @updatedAt

  @@map("comments")
}

model Tag {
  id    String @id @default(cuid())
  name  String @unique
  posts Post[]
  createdAt DateTime @default(now())
  updatedAt DateTime @updatedAt

  @@map("tags")
}
```

### Database Service Implementation
```typescript
// src/services/database/DatabaseService.ts
import { PrismaClient } from '@prisma/client';
import { Logger } from '../../utils/Logger';

export class DatabaseService {
  private static instance: DatabaseService;
  private prisma: PrismaClient;
  private logger: Logger;

  private constructor() {
    this.logger = new Logger('DatabaseService');
    this.prisma = new PrismaClient({
      log: [
        { emit: 'event', level: 'query' },
        { emit: 'event', level: 'error' },
        { emit: 'event', level: 'info' },
        { emit: 'event', level: 'warn' },
      ],
    });

    this.setupEventListeners();
  }

  public static getInstance(): DatabaseService {
    if (!DatabaseService.instance) {
      DatabaseService.instance = new DatabaseService();
    }
    return DatabaseService.instance;
  }

  public getClient(): PrismaClient {
    return this.prisma;
  }

  private setupEventListeners(): void {
    this.prisma.$on('query', (e) => {
      this.logger.debug('Query executed', {
        query: e.query,
        params: e.params,
        duration: e.duration,
      });
    });

    this.prisma.$on('error', (e) => {
      this.logger.error('Database error', { error: e });
    });
  }

  public async connect(): Promise<void> {
    try {
      await this.prisma.$connect();
      this.logger.info('Database connected successfully');
    } catch (error) {
      this.logger.error('Failed to connect to database', { error });
      throw error;
    }
  }

  public async disconnect(): Promise<void> {
    try {
      await this.prisma.$disconnect();
      this.logger.info('Database disconnected successfully');
    } catch (error) {
      this.logger.error('Failed to disconnect from database', { error });
      throw error;
    }
  }

  public async healthCheck(): Promise<boolean> {
    try {
      await this.prisma.$queryRaw`SELECT 1`;
      return true;
    } catch (error) {
      this.logger.error('Database health check failed', { error });
      return false;
    }
  }

  public async runTransaction<T>(fn: (prisma: PrismaClient) => Promise<T>): Promise<T> {
    return this.prisma.$transaction(fn);
  }
}

// src/repositories/PrismaUserRepository.ts
import { PrismaClient, User as PrismaUser } from '@prisma/client';
import { UserRepository } from '../domain/repositories/UserRepository';
import { User } from '../domain/entities/User';
import { CreateUserData, UpdateUserData, UserFilters } from '../types/user';

export class PrismaUserRepository implements UserRepository {
  constructor(private prisma: PrismaClient) {}

  async findById(id: string): Promise<User | null> {
    const user = await this.prisma.user.findUnique({
      where: { id },
      include: {
        roles: {
          include: {
            permissions: true,
          },
        },
      },
    });

    return user ? this.toDomain(user) : null;
  }

  async findByEmail(email: string): Promise<User | null> {
    const user = await this.prisma.user.findUnique({
      where: { email },
      include: {
        roles: {
          include: {
            permissions: true,
          },
        },
      },
    });

    return user ? this.toDomain(user) : null;
  }

  async create(userData: CreateUserData): Promise<User> {
    const user = await this.prisma.user.create({
      data: {
        email: userData.email,
        name: userData.name,
        password: userData.password,
        avatar: userData.avatar,
      },
      include: {
        roles: {
          include: {
            permissions: true,
          },
        },
      },
    });

    return this.toDomain(user);
  }

  async update(id: string, userData: UpdateUserData): Promise<User> {
    const user = await this.prisma.user.update({
      where: { id },
      data: userData,
      include: {
        roles: {
          include: {
            permissions: true,
          },
        },
      },
    });

    return this.toDomain(user);
  }

  async delete(id: string): Promise<void> {
    await this.prisma.user.delete({
      where: { id },
    });
  }

  async findAll(filters?: UserFilters): Promise<User[]> {
    const where: any = {};

    if (filters?.search) {
      where.OR = [
        { name: { contains: filters.search, mode: 'insensitive' } },
        { email: { contains: filters.search, mode: 'insensitive' } },
      ];
    }

    if (filters?.roles && filters.roles.length > 0) {
      where.roles = {
        some: {
          name: {
            in: filters.roles,
          },
        },
      };
    }

    const users = await this.prisma.user.findMany({
      where,
      include: {
        roles: {
          include: {
            permissions: true,
          },
        },
      },
      orderBy: {
        createdAt: 'desc',
      },
      take: filters?.limit || 50,
      skip: filters?.offset || 0,
    });

    return users.map(user => this.toDomain(user));
  }

  private toDomain(prismaUser: any): User {
    return new User(
      prismaUser.id,
      prismaUser.email,
      prismaUser.name,
      prismaUser.password,
      prismaUser.avatar,
      prismaUser.roles?.map((role: any) => role.name) || [],
      prismaUser.createdAt,
      prismaUser.updatedAt
    );
  }
}
```

---

## 🔄 API Design Standards

### RESTful API Implementation
```typescript
// src/routes/api/users.ts
import { Router } from 'express';
import { container } from '../../container/Container';
import { TYPES } from '../../container/types';
import { UserController } from '../../controllers/api/UserController';
import { authenticate, authorize } from '../../middleware/auth';
import { validateRequest } from '../../middleware/validation';
import { createUserSchema, updateUserSchema } from '../../validators/userValidators';

const router = Router();
const userController = container.get<UserController>(TYPES.UserController);
const authMiddleware = container.get(TYPES.AuthMiddleware);

// GET /api/users - Get all users
router.get(
  '/',
  authMiddleware,
  authorize(['admin', 'user']),
  userController.getUsers.bind(userController)
);

// GET /api/users/:id - Get user by ID
router.get(
  '/:id',
  authMiddleware,
  authorize(['admin', 'user']),
  userController.getUserById.bind(userController)
);

// POST /api/users - Create new user
router.post(
  '/',
  authMiddleware,
  authorize(['admin']),
  validateRequest(createUserSchema),
  userController.createUser.bind(userController)
);

// PUT /api/users/:id - Update user
router.put(
  '/:id',
  authMiddleware,
  authorize(['admin', 'user']),
  validateRequest(updateUserSchema),
  userController.updateUser.bind(userController)
);

// DELETE /api/users/:id - Delete user
router.delete(
  '/:id',
  authMiddleware,
  authorize(['admin']),
  userController.deleteUser.bind(userController)
);

export default router;

// src/controllers/api/UserController.ts
import { Request, Response, NextFunction } from 'express';
import { GetUsersUseCase } from '../../application/usecases/user/GetUsersUseCase';
import { GetUserByIdUseCase } from '../../application/usecases/user/GetUserByIdUseCase';
import { CreateUserUseCase } from '../../application/usecases/user/CreateUserUseCase';
import { UpdateUserUseCase } from '../../application/usecases/user/UpdateUserUseCase';
import { DeleteUserUseCase } from '../../application/usecases/user/DeleteUserUseCase';
import { ApiResponse } from '../../types/api';

export class UserController {
  constructor(
    private getUsersUseCase: GetUsersUseCase,
    private getUserByIdUseCase: GetUserByIdUseCase,
    private createUserUseCase: CreateUserUseCase,
    private updateUserUseCase: UpdateUserUseCase,
    private deleteUserUseCase: DeleteUserUseCase
  ) {}

  async getUsers(req: Request, res: Response<ApiResponse>, next: NextFunction) {
    try {
      const { search, roles, page = 1, limit = 20 } = req.query;
      
      const filters = {
        search: search as string,
        roles: roles ? (roles as string).split(',') : undefined,
        offset: (Number(page) - 1) * Number(limit),
        limit: Number(limit),
      };

      const result = await this.getUsersUseCase.execute(filters);

      res.status(200).json({
        success: true,
        data: result.users,
        meta: {
          total: result.total,
          page: Number(page),
          limit: Number(limit),
          totalPages: Math.ceil(result.total / Number(limit)),
        },
      });
    } catch (error) {
      next(error);
    }
  }

  async getUserById(req: Request, res: Response<ApiResponse>, next: NextFunction) {
    try {
      const { id } = req.params;
      
      const user = await this.getUserByIdUseCase.execute(id);

      res.status(200).json({
        success: true,
        data: user,
      });
    } catch (error) {
      next(error);
    }
  }

  async createUser(req: Request, res: Response<ApiResponse>, next: NextFunction) {
    try {
      const userData = req.body;
      
      const user = await this.createUserUseCase.execute(userData);

      res.status(201).json({
        success: true,
        data: user,
        message: 'User created successfully',
      });
    } catch (error) {
      next(error);
    }
  }

  async updateUser(req: Request, res: Response<ApiResponse>, next: NextFunction) {
    try {
      const { id } = req.params;
      const userData = req.body;
      
      const user = await this.updateUserUseCase.execute(id, userData);

      res.status(200).json({
        success: true,
        data: user,
        message: 'User updated successfully',
      });
    } catch (error) {
      next(error);
    }
  }

  async deleteUser(req: Request, res: Response<ApiResponse>, next: NextFunction) {
    try {
      const { id } = req.params;
      
      await this.deleteUserUseCase.execute(id);

      res.status(200).json({
        success: true,
        message: 'User deleted successfully',
      });
    } catch (error) {
      next(error);
    }
  }
}
```

### API Response Standards
```typescript
// src/types/api.ts
export interface ApiResponse<T = any> {
  success: boolean;
  data?: T;
  message?: string;
  errors?: ValidationError[];
  meta?: {
    total?: number;
    page?: number;
    limit?: number;
    totalPages?: number;
  };
}

export interface ValidationError {
  field: string;
  message: string;
  code: string;
}

export interface PaginationParams {
  page?: number;
  limit?: number;
  offset?: number;
}

export interface SortParams {
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
}

// src/utils/ApiResponseBuilder.ts
export class ApiResponseBuilder {
  static success<T>(data?: T, message?: string): ApiResponse<T> {
    return {
      success: true,
      data,
      message,
    };
  }

  static error(message: string, errors?: ValidationError[]): ApiResponse {
    return {
      success: false,
      message,
      errors,
    };
  }

  static paginated<T>(
    data: T[],
    total: number,
    page: number,
    limit: number,
    message?: string
  ): ApiResponse<T[]> {
    return {
      success: true,
      data,
      message,
      meta: {
        total,
        page,
        limit,
        totalPages: Math.ceil(total / limit),
      },
    };
  }
}
```

---

## 🧪 Testing Standards

### Unit Testing Setup
```typescript
// jest.config.js
module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'node',
  roots: ['<rootDir>/src'],
  testMatch: ['**/__tests__/**/*.test.ts'],
  collectCoverageFrom: [
    'src/**/*.ts',
    '!src/**/*.d.ts',
    '!src/tests/**',
    '!src/types/**',
  ],
  coverageDirectory: 'coverage',
  coverageReporters: ['text', 'lcov', 'html'],
  setupFilesAfterEnv: ['<rootDir>/src/tests/setup.ts'],
  testTimeout: 10000,
};

// src/tests/setup.ts
import { DatabaseService } from '../services/database/DatabaseService';
import { Redis } from 'ioredis';

// Mock Redis
jest.mock('ioredis', () => {
  return jest.fn().mockImplementation(() => ({
    get: jest.fn(),
    set: jest.fn(),
    setex: jest.fn(),
    del: jest.fn(),
    flushall: jest.fn(),
  }));
});

// Setup test database
beforeAll(async () => {
  // Setup test database connection
  process.env.DATABASE_URL = 'postgresql://test:test@localhost:5432/test_db';
});

afterAll(async () => {
  // Cleanup
  await DatabaseService.getInstance().disconnect();
});

beforeEach(async () => {
  // Reset database state
  jest.clearAllMocks();
});
```

### Unit Test Examples
```typescript
// src/tests/unit/services/auth/TokenService.test.ts
import { TokenService } from '../../../../services/auth/TokenService';
import { Redis } from 'ioredis';
import jwt from 'jsonwebtoken';

describe('TokenService', () => {
  let tokenService: TokenService;
  let mockRedis: jest.Mocked<Redis>;

  beforeEach(() => {
    mockRedis = {
      setex: jest.fn(),
      get: jest.fn(),
      del: jest.fn(),
    } as any;

    tokenService = new TokenService(
      mockRedis,
      'access-secret',
      'refresh-secret',
      '15m',
      '7d'
    );
  });

  describe('generateAccessToken', () => {
    it('should generate a valid access token', async () => {
      const userId = 'user-123';
      const token = await tokenService.generateAccessToken(userId);

      expect(token).toBeDefined();
      expect(typeof token).toBe('string');

      const decoded = jwt.verify(token, 'access-secret') as any;
      expect(decoded.userId).toBe(userId);
      expect(decoded.type).toBe('access');
    });
  });

  describe('generateRefreshToken', () => {
    it('should generate a refresh token and store it in Redis', async () => {
      const userId = 'user-123';
      mockRedis.setex.mockResolvedValue('OK');

      const token = await tokenService.generateRefreshToken(userId);

      expect(token).toBeDefined();
      expect(mockRedis.setex).toHaveBeenCalledWith(
        `refresh_token:${userId}`,
        7 * 24 * 60 * 60,
        token
      );
    });
  });

  describe('verifyAccessToken', () => {
    it('should verify a valid access token', async () => {
      const userId = 'user-123';
      const token = await tokenService.generateAccessToken(userId);

      const result = await tokenService.verifyAccessToken(token);

      expect(result.userId).toBe(userId);
    });

    it('should throw error for invalid token', async () => {
      await expect(
        tokenService.verifyAccessToken('invalid-token')
      ).rejects.toThrow('Invalid access token');
    });
  });

  describe('refreshAccessToken', () => {
    it('should refresh access token with valid refresh token', async () => {
      const userId = 'user-123';
      const refreshToken = await tokenService.generateRefreshToken(userId);
      
      mockRedis.get.mockResolvedValue(refreshToken);
      mockRedis.del.mockResolvedValue(1);
      mockRedis.setex.mockResolvedValue('OK');

      const result = await tokenService.refreshAccessToken(refreshToken);

      expect(result.accessToken).toBeDefined();
      expect(result.refreshToken).toBeDefined();
      expect(mockRedis.del).toHaveBeenCalledWith(`refresh_token:${userId}`);
    });
  });
});
```

### Integration Test Examples
```typescript
// src/tests/integration/auth/AuthController.test.ts
import request from 'supertest';
import { app } from '../../../app';
import { DatabaseService } from '../../../services/database/DatabaseService';
import { PrismaClient } from '@prisma/client';

describe('AuthController Integration Tests', () => {
  let prisma: PrismaClient;

  beforeAll(async () => {
    prisma = DatabaseService.getInstance().getClient();
    await prisma.$connect();
  });

  afterAll(async () => {
    await prisma.$disconnect();
  });

  beforeEach(async () => {
    // Clean database
    await prisma.user.deleteMany();
  });

  describe('POST /api/auth/register', () => {
    it('should register a new user successfully', async () => {
      const userData = {
        email: 'test@example.com',
        name: 'Test User',
        password: 'password123',
      };

      const response = await request(app)
        .post('/api/auth/register')
        .send(userData)
        .expect(201);

      expect(response.body.success).toBe(true);
      expect(response.body.data.user.email).toBe(userData.email);
      expect(response.body.data.accessToken).toBeDefined();
      expect(response.body.data.refreshToken).toBeDefined();

      // Verify user was created in database
      const user = await prisma.user.findUnique({
        where: { email: userData.email },
      });
      expect(user).toBeDefined();
      expect(user!.name).toBe(userData.name);
    });

    it('should return error for duplicate email', async () => {
      const userData = {
        email: 'test@example.com',
        name: 'Test User',
        password: 'password123',
      };

      // Create user first
      await prisma.user.create({
        data: {
          ...userData,
          password: 'hashed-password',
        },
      });

      const response = await request(app)
        .post('/api/auth/register')
        .send(userData)
        .expect(400);

      expect(response.body.success).toBe(false);
      expect(response.body.message).toContain('email already exists');
    });
  });

  describe('POST /api/auth/login', () => {
    beforeEach(async () => {
      // Create test user
      await prisma.user.create({
        data: {
          email: 'test@example.com',
          name: 'Test User',
          password: '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', // password123
        },
      });
    });

    it('should login with valid credentials', async () => {
      const loginData = {
        email: 'test@example.com',
        password: 'password123',
      };

      const response = await request(app)
        .post('/api/auth/login')
        .send(loginData)
        .expect(200);

      expect(response.body.success).toBe(true);
      expect(response.body.data.user.email).toBe(loginData.email);
      expect(response.body.data.accessToken).toBeDefined();
      expect(response.body.data.refreshToken).toBeDefined();
    });

    it('should return error for invalid credentials', async () => {
      const loginData = {
        email: 'test@example.com',
        password: 'wrongpassword',
      };

      const response = await request(app)
        .post('/api/auth/login')
        .send(loginData)
        .expect(401);

      expect(response.body.success).toBe(false);
      expect(response.body.message).toContain('Invalid credentials');
    });
  });
});
```

---

## 🚀 Performance Optimization

### Caching Strategy
```typescript
// src/services/cache/CacheService.ts
import { Redis } from 'ioredis';
import { Logger } from '../../utils/Logger';

export class CacheService {
  private redis: Redis;
  private logger: Logger;
  private defaultTTL: number = 3600; // 1 hour

  constructor(redis: Redis) {
    this.redis = redis;
    this.logger = new Logger('CacheService');
  }

  async get<T>(key: string): Promise<T | null> {
    try {
      const value = await this.redis.get(key);
      if (!value) return null;
      
      return JSON.parse(value) as T;
    } catch (error) {
      this.logger.error('Cache get error', { key, error });
      return null;
    }
  }

  async set(key: string, value: any, ttl?: number): Promise<void> {
    try {
      const serialized = JSON.stringify(value);
      const expiry = ttl || this.defaultTTL;
      
      await this.redis.setex(key, expiry, serialized);
    } catch (error) {
      this.logger.error('Cache set error', { key, error });
    }
  }

  async del(key: string): Promise<void> {
    try {
      await this.redis.del(key);
    } catch (error) {
      this.logger.error('Cache delete error', { key, error });
    }
  }

  async invalidatePattern(pattern: string): Promise<void> {
    try {
      const keys = await this.redis.keys(pattern);
      if (keys.length > 0) {
        await this.redis.del(...keys);
      }
    } catch (error) {
      this.logger.error('Cache invalidate pattern error', { pattern, error });
    }
  }

  // Cache decorator
  cache(key: string, ttl?: number) {
    return (target: any, propertyName: string, descriptor: PropertyDescriptor) => {
      const method = descriptor.value;
      
      descriptor.value = async function (...args: any[]) {
        const cacheKey = `${key}:${JSON.stringify(args)}`;
        
        // Try to get from cache
        const cached = await this.cacheService.get(cacheKey);
        if (cached) {
          return cached;
        }
        
        // Execute method and cache result
        const result = await method.apply(this, args);
        await this.cacheService.set(cacheKey, result, ttl);
        
        return result;
      };
    };
  }
}

// Usage example
export class UserService {
  constructor(
    private userRepository: UserRepository,
    private cacheService: CacheService
  ) {}

  @cache('user:profile', 1800) // 30 minutes
  async getUserProfile(userId: string): Promise<UserProfile> {
    const user = await this.userRepository.findById(userId);
    if (!user) {
      throw new NotFoundError('User not found');
    }
    
    return {
      id: user.id,
      name: user.name,
      email: user.email,
      avatar: user.avatar,
    };
  }

  async updateUserProfile(userId: string, data: UpdateUserData): Promise<User> {
    const user = await this.userRepository.update(userId, data);
    
    // Invalidate cache
    await this.cacheService.del(`user:profile:${JSON.stringify([userId])}`);
    
    return user;
  }
}
```

### Database Query Optimization
```typescript
// src/repositories/OptimizedUserRepository.ts
export class OptimizedUserRepository extends PrismaUserRepository {
  async findUsersWithPosts(filters: UserFilters): Promise<UserWithPosts[]> {
    // Use select to only fetch needed fields
    const users = await this.prisma.user.findMany({
      select: {
        id: true,
        name: true,
        email: true,
        avatar: true,
        posts: {
          select: {
            id: true,
            title: true,
            createdAt: true,
            published: true,
          },
          where: {
            published: true,
          },
          orderBy: {
            createdAt: 'desc',
          },
          take: 5, // Limit posts per user
        },
        _count: {
          select: {
            posts: true,
            comments: true,
          },
        },
      },
      where: this.buildWhereClause(filters),
      orderBy: {
        createdAt: 'desc',
      },
      take: filters.limit || 20,
      skip: filters.offset || 0,
    });

    return users;
  }

  async getUsersWithCounts(): Promise<UserWithCounts[]> {
    // Use raw query for complex aggregations
    const result = await this.prisma.$queryRaw`
      SELECT 
        u.id,
        u.name,
        u.email,
        COUNT(DISTINCT p.id) as post_count,
        COUNT(DISTINCT c.id) as comment_count,
        MAX(p.created_at) as last_post_date
      FROM users u
      LEFT JOIN posts p ON u.id = p.author_id
      LEFT JOIN comments c ON u.id = c.author_id
      GROUP BY u.id, u.name, u.email
      ORDER BY post_count DESC
      LIMIT 50
    `;

    return result as UserWithCounts[];
  }

  private buildWhereClause(filters: UserFilters): any {
    const where: any = {};

    if (filters.search) {
      where.OR = [
        { name: { contains: filters.search, mode: 'insensitive' } },
        { email: { contains: filters.search, mode: 'insensitive' } },
      ];
    }

    if (filters.roles && filters.roles.length > 0) {
      where.roles = {
        some: {
          name: { in: filters.roles },
        },
      };
    }

    if (filters.createdAfter) {
      where.createdAt = {
        gte: filters.createdAfter,
      };
    }

    return where;
  }
}
```

---

## ✅ Quality Checklist

### Code Quality
- [ ] TypeScript strict mode enabled
- [ ] ESLint and Prettier configured
- [ ] Clean Architecture principles followed
- [ ] SOLID principles implemented
- [ ] Dependency injection used
- [ ] Error handling implemented
- [ ] Logging configured
- [ ] Input validation added

### Security
- [ ] Authentication implemented
- [ ] Authorization configured
- [ ] Input sanitization added
- [ ] SQL injection prevention
- [ ] XSS protection implemented
- [ ] CORS configured properly
- [ ] Rate limiting implemented
- [ ] Environment variables secured

### Performance
- [ ] Database queries optimized
- [ ] Caching strategy implemented
- [ ] Connection pooling configured
- [ ] Background jobs setup
- [ ] Monitoring and metrics added
- [ ] Load testing performed

### Testing
- [ ] Unit tests cover business logic (>80%)
- [ ] Integration tests for APIs
- [ ] Database tests with transactions
- [ ] Error scenario testing
- [ ] Performance testing

### Deployment
- [ ] Docker configuration
- [ ] Environment configurations
- [ ] Health check endpoints
- [ ] Graceful shutdown handling
- [ ] CI/CD pipeline setup
- [ ] Database migration strategy
- [ ] Backup and recovery plan

---

**⚙️ Scalable backend development with clean architecture, comprehensive testing, and production-ready deployment strategies for enterprise-grade applications.**