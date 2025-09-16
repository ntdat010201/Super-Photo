# 🔒 Security Specialist Agent

> **🛡️ Cybersecurity & Application Security Expert**  
> Specialized in security auditing, vulnerability assessment, and secure development practices

---

## 🔧 Agent Configuration

### Core Identity
- **Agent ID**: `security-specialist`
- **Version**: `2.0.0`
- **Category**: Specialized > Security
- **Specialization**: Application security, vulnerability assessment, secure coding
- **Confidence Threshold**: 85%

### Performance Metrics
- **Success Rate**: 89%
- **Quality Score**: 9.3/10
- **Response Time**: <5s
- **User Satisfaction**: 4.6/5

---

## 🎯 Capabilities Matrix

### Primary Technologies (9-10/10)
- **Security Frameworks**: OWASP Top 10, NIST, ISO 27001
- **Vulnerability Assessment**: OWASP ZAP, Burp Suite, Nessus
- **Static Analysis**: SonarQube, CodeQL, Semgrep
- **Authentication**: OAuth 2.0, JWT, SAML, OpenID Connect
- **Encryption**: AES, RSA, TLS/SSL, PKI

### Secondary Technologies (8-9/10)
- **Container Security**: Docker security, Kubernetes security
- **Cloud Security**: AWS Security, Azure Security, GCP Security
- **Network Security**: Firewalls, VPN, IDS/IPS
- **API Security**: API Gateway security, rate limiting
- **Database Security**: SQL injection prevention, encryption at rest

### Supporting Technologies (6-7/10)
- **Penetration Testing**: Metasploit, Kali Linux tools
- **Compliance**: GDPR, HIPAA, PCI DSS, SOX
- **Incident Response**: SIEM, log analysis, forensics
- **DevSecOps**: Security in CI/CD pipelines

---

## 🔍 Selection Criteria

### Primary Keywords (High Weight)
```
security, vulnerability, authentication, authorization, encryption, owasp
```

### Secondary Keywords (Medium Weight)
```
penetration testing, security audit, secure coding, compliance, privacy
```

### Context Indicators (Low Weight)
```
hacking, exploit, breach, malware, firewall, ssl, tls, jwt, oauth
```

### File Type Triggers
```
security.*, auth.*, login.*, .env, config/, secrets/, certs/
```

---

## 📋 Workflow Integration

### Primary Workflows
- **[Security Standards](../../rules/core/security-standards.md)**: Comprehensive security guidelines
- **[Security Audit Workflow](../../rules/specialized/security-audit-workflow.md)**: Security assessment procedures
- **[Secure Development Lifecycle](../../rules/specialized/secure-development-lifecycle.md)**: Security in SDLC

### Supporting Workflows
- **[Authentication Standards](../../rules/specialized/authentication-standards.md)**: Auth implementation guidelines
- **[Data Protection Guidelines](../../rules/specialized/data-protection-guidelines.md)**: Data security practices
- **[Compliance Framework](../../rules/specialized/compliance-framework.md)**: Regulatory compliance

---

## 🛡️ Security Implementation Templates

### JWT Authentication Implementation
```typescript
// auth/jwt.service.ts
import jwt from 'jsonwebtoken';
import bcrypt from 'bcrypt';
import { User } from '../models/User';
import { AuthConfig } from '../config/auth.config';

export class JWTService {
  private readonly secretKey: string;
  private readonly refreshSecretKey: string;
  private readonly accessTokenExpiry: string;
  private readonly refreshTokenExpiry: string;

  constructor() {
    this.secretKey = process.env.JWT_SECRET_KEY!;
    this.refreshSecretKey = process.env.JWT_REFRESH_SECRET_KEY!;
    this.accessTokenExpiry = process.env.JWT_ACCESS_EXPIRY || '15m';
    this.refreshTokenExpiry = process.env.JWT_REFRESH_EXPIRY || '7d';
    
    if (!this.secretKey || !this.refreshSecretKey) {
      throw new Error('JWT secret keys must be provided');
    }
  }

  async generateTokens(user: User): Promise<{ accessToken: string; refreshToken: string }> {
    const payload = {
      userId: user.id,
      email: user.email,
      role: user.role,
      permissions: user.permissions,
    };

    const accessToken = jwt.sign(payload, this.secretKey, {
      expiresIn: this.accessTokenExpiry,
      issuer: 'your-app-name',
      audience: 'your-app-users',
      subject: user.id.toString(),
    });

    const refreshToken = jwt.sign(
      { userId: user.id, tokenVersion: user.tokenVersion },
      this.refreshSecretKey,
      {
        expiresIn: this.refreshTokenExpiry,
        issuer: 'your-app-name',
        audience: 'your-app-users',
        subject: user.id.toString(),
      }
    );

    return { accessToken, refreshToken };
  }

  async verifyAccessToken(token: string): Promise<any> {
    try {
      return jwt.verify(token, this.secretKey, {
        issuer: 'your-app-name',
        audience: 'your-app-users',
      });
    } catch (error) {
      throw new Error('Invalid or expired access token');
    }
  }

  async verifyRefreshToken(token: string): Promise<any> {
    try {
      return jwt.verify(token, this.refreshSecretKey, {
        issuer: 'your-app-name',
        audience: 'your-app-users',
      });
    } catch (error) {
      throw new Error('Invalid or expired refresh token');
    }
  }

  async hashPassword(password: string): Promise<string> {
    const saltRounds = 12;
    return bcrypt.hash(password, saltRounds);
  }

  async comparePassword(password: string, hashedPassword: string): Promise<boolean> {
    return bcrypt.compare(password, hashedPassword);
  }
}

// middleware/auth.middleware.ts
import { Request, Response, NextFunction } from 'express';
import { JWTService } from '../auth/jwt.service';
import { UserService } from '../services/user.service';

interface AuthenticatedRequest extends Request {
  user?: any;
}

export class AuthMiddleware {
  private jwtService: JWTService;
  private userService: UserService;

  constructor() {
    this.jwtService = new JWTService();
    this.userService = new UserService();
  }

  authenticate = async (req: AuthenticatedRequest, res: Response, next: NextFunction) => {
    try {
      const authHeader = req.headers.authorization;
      
      if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return res.status(401).json({ error: 'Access token required' });
      }

      const token = authHeader.substring(7);
      const decoded = await this.jwtService.verifyAccessToken(token);
      
      // Verify user still exists and is active
      const user = await this.userService.findById(decoded.userId);
      if (!user || !user.isActive) {
        return res.status(401).json({ error: 'User not found or inactive' });
      }

      // Check if token version matches (for token revocation)
      if (user.tokenVersion !== decoded.tokenVersion) {
        return res.status(401).json({ error: 'Token has been revoked' });
      }

      req.user = decoded;
      next();
    } catch (error) {
      return res.status(401).json({ error: 'Invalid token' });
    }
  };

  authorize = (requiredPermissions: string[]) => {
    return (req: AuthenticatedRequest, res: Response, next: NextFunction) => {
      if (!req.user) {
        return res.status(401).json({ error: 'Authentication required' });
      }

      const userPermissions = req.user.permissions || [];
      const hasPermission = requiredPermissions.every(permission => 
        userPermissions.includes(permission)
      );

      if (!hasPermission) {
        return res.status(403).json({ error: 'Insufficient permissions' });
      }

      next();
    };
  };

  rateLimiter = (maxRequests: number, windowMs: number) => {
    const requests = new Map();
    
    return (req: Request, res: Response, next: NextFunction) => {
      const clientId = req.ip || req.connection.remoteAddress;
      const now = Date.now();
      const windowStart = now - windowMs;
      
      if (!requests.has(clientId)) {
        requests.set(clientId, []);
      }
      
      const clientRequests = requests.get(clientId);
      const validRequests = clientRequests.filter((time: number) => time > windowStart);
      
      if (validRequests.length >= maxRequests) {
        return res.status(429).json({ 
          error: 'Too many requests',
          retryAfter: Math.ceil(windowMs / 1000)
        });
      }
      
      validRequests.push(now);
      requests.set(clientId, validRequests);
      
      next();
    };
  };
}
```

### Input Validation & Sanitization
```typescript
// validation/security.validator.ts
import Joi from 'joi';
import DOMPurify from 'isomorphic-dompurify';
import validator from 'validator';

export class SecurityValidator {
  // Password strength validation
  static passwordSchema = Joi.string()
    .min(8)
    .max(128)
    .pattern(new RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]'))
    .required()
    .messages({
      'string.pattern.base': 'Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character',
      'string.min': 'Password must be at least 8 characters long',
      'string.max': 'Password must not exceed 128 characters'
    });

  // Email validation
  static emailSchema = Joi.string()
    .email({ tlds: { allow: false } })
    .max(254)
    .required()
    .custom((value, helpers) => {
      if (!validator.isEmail(value)) {
        return helpers.error('any.invalid');
      }
      return value.toLowerCase().trim();
    });

  // SQL Injection prevention
  static sanitizeInput(input: string): string {
    if (typeof input !== 'string') {
      throw new Error('Input must be a string');
    }
    
    // Remove SQL injection patterns
    const sqlInjectionPattern = /('|(\-\-)|(;)|(\||\|)|(\*|\*))/gi;
    let sanitized = input.replace(sqlInjectionPattern, '');
    
    // HTML encode special characters
    sanitized = validator.escape(sanitized);
    
    return sanitized.trim();
  }

  // XSS prevention
  static sanitizeHtml(html: string): string {
    return DOMPurify.sanitize(html, {
      ALLOWED_TAGS: ['b', 'i', 'em', 'strong', 'a', 'p', 'br'],
      ALLOWED_ATTR: ['href'],
      ALLOW_DATA_ATTR: false,
    });
  }

  // File upload validation
  static validateFileUpload(file: any): { isValid: boolean; error?: string } {
    const allowedMimeTypes = [
      'image/jpeg',
      'image/png',
      'image/gif',
      'application/pdf',
      'text/plain'
    ];
    
    const maxFileSize = 5 * 1024 * 1024; // 5MB
    
    if (!file) {
      return { isValid: false, error: 'No file provided' };
    }
    
    if (!allowedMimeTypes.includes(file.mimetype)) {
      return { isValid: false, error: 'File type not allowed' };
    }
    
    if (file.size > maxFileSize) {
      return { isValid: false, error: 'File size exceeds limit' };
    }
    
    // Check file extension matches MIME type
    const extension = file.originalname.split('.').pop()?.toLowerCase();
    const mimeTypeExtensions: { [key: string]: string[] } = {
      'image/jpeg': ['jpg', 'jpeg'],
      'image/png': ['png'],
      'image/gif': ['gif'],
      'application/pdf': ['pdf'],
      'text/plain': ['txt']
    };
    
    const validExtensions = mimeTypeExtensions[file.mimetype] || [];
    if (!extension || !validExtensions.includes(extension)) {
      return { isValid: false, error: 'File extension does not match content type' };
    }
    
    return { isValid: true };
  }

  // CSRF token validation
  static generateCSRFToken(): string {
    return require('crypto').randomBytes(32).toString('hex');
  }

  static validateCSRFToken(sessionToken: string, requestToken: string): boolean {
    if (!sessionToken || !requestToken) {
      return false;
    }
    
    return require('crypto').timingSafeEqual(
      Buffer.from(sessionToken, 'hex'),
      Buffer.from(requestToken, 'hex')
    );
  }
}

// middleware/security.middleware.ts
import helmet from 'helmet';
import cors from 'cors';
import { Request, Response, NextFunction } from 'express';

export class SecurityMiddleware {
  static setupSecurityHeaders() {
    return helmet({
      contentSecurityPolicy: {
        directives: {
          defaultSrc: ["'self'"],
          styleSrc: ["'self'", "'unsafe-inline'", 'https://fonts.googleapis.com'],
          fontSrc: ["'self'", 'https://fonts.gstatic.com'],
          imgSrc: ["'self'", 'data:', 'https:'],
          scriptSrc: ["'self'"],
          connectSrc: ["'self'"],
          frameSrc: ["'none'"],
          objectSrc: ["'none'"],
          upgradeInsecureRequests: [],
        },
      },
      hsts: {
        maxAge: 31536000,
        includeSubDomains: true,
        preload: true
      },
      noSniff: true,
      xssFilter: true,
      referrerPolicy: { policy: 'same-origin' }
    });
  }

  static setupCORS() {
    return cors({
      origin: process.env.ALLOWED_ORIGINS?.split(',') || ['http://localhost:3000'],
      credentials: true,
      methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
      allowedHeaders: ['Content-Type', 'Authorization', 'X-CSRF-Token'],
      exposedHeaders: ['X-Total-Count'],
      maxAge: 86400 // 24 hours
    });
  }

  static requestLogger = (req: Request, res: Response, next: NextFunction) => {
    const startTime = Date.now();
    
    res.on('finish', () => {
      const duration = Date.now() - startTime;
      const logData = {
        method: req.method,
        url: req.url,
        statusCode: res.statusCode,
        duration,
        userAgent: req.get('User-Agent'),
        ip: req.ip,
        timestamp: new Date().toISOString()
      };
      
      // Log security-relevant events
      if (res.statusCode >= 400) {
        console.warn('Security Event:', logData);
      }
    });
    
    next();
  };

  static detectSuspiciousActivity = (req: Request, res: Response, next: NextFunction) => {
    const suspiciousPatterns = [
      /(<script[^>]*>.*?<\/script>)/gi, // XSS attempts
      /(union|select|insert|delete|update|drop|create|alter)/gi, // SQL injection
      /(\.\.\/|\.\.\\/)/g, // Path traversal
      /(eval\(|javascript:|vbscript:)/gi, // Code injection
    ];
    
    const requestData = JSON.stringify({
      url: req.url,
      query: req.query,
      body: req.body,
      headers: req.headers
    });
    
    for (const pattern of suspiciousPatterns) {
      if (pattern.test(requestData)) {
        console.error('Suspicious activity detected:', {
          ip: req.ip,
          userAgent: req.get('User-Agent'),
          url: req.url,
          pattern: pattern.source
        });
        
        return res.status(400).json({ error: 'Malicious request detected' });
      }
    }
    
    next();
  };
}
```

### Database Security
```typescript
// database/secure-query.service.ts
import { Pool, PoolClient } from 'pg';
import { SecurityValidator } from '../validation/security.validator';

export class SecureQueryService {
  private pool: Pool;

  constructor() {
    this.pool = new Pool({
      host: process.env.DB_HOST,
      port: parseInt(process.env.DB_PORT || '5432'),
      database: process.env.DB_NAME,
      user: process.env.DB_USER,
      password: process.env.DB_PASSWORD,
      ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false,
      max: 20,
      idleTimeoutMillis: 30000,
      connectionTimeoutMillis: 2000,
    });
  }

  async executeQuery<T>(query: string, params: any[] = []): Promise<T[]> {
    const client: PoolClient = await this.pool.connect();
    
    try {
      // Validate and sanitize parameters
      const sanitizedParams = params.map(param => {
        if (typeof param === 'string') {
          return SecurityValidator.sanitizeInput(param);
        }
        return param;
      });
      
      // Log query for audit (without sensitive data)
      console.log('Executing query:', {
        query: query.replace(/\$\d+/g, '?'),
        paramCount: sanitizedParams.length,
        timestamp: new Date().toISOString()
      });
      
      const result = await client.query(query, sanitizedParams);
      return result.rows;
    } catch (error) {
      console.error('Database query error:', {
        error: error.message,
        query: query.replace(/\$\d+/g, '?'),
        timestamp: new Date().toISOString()
      });
      throw error;
    } finally {
      client.release();
    }
  }

  async executeTransaction<T>(queries: Array<{ query: string; params: any[] }>): Promise<T[]> {
    const client: PoolClient = await this.pool.connect();
    
    try {
      await client.query('BEGIN');
      
      const results: T[] = [];
      
      for (const { query, params } of queries) {
        const sanitizedParams = params.map(param => {
          if (typeof param === 'string') {
            return SecurityValidator.sanitizeInput(param);
          }
          return param;
        });
        
        const result = await client.query(query, sanitizedParams);
        results.push(result.rows);
      }
      
      await client.query('COMMIT');
      return results;
    } catch (error) {
      await client.query('ROLLBACK');
      console.error('Transaction error:', error);
      throw error;
    } finally {
      client.release();
    }
  }

  // Prepared statements for common queries
  async findUserByEmail(email: string) {
    const query = `
      SELECT id, email, password_hash, role, is_active, created_at, updated_at
      FROM users 
      WHERE email = $1 AND is_active = true
    `;
    
    const result = await this.executeQuery(query, [email]);
    return result[0] || null;
  }

  async createUser(userData: any) {
    const query = `
      INSERT INTO users (email, password_hash, role, is_active)
      VALUES ($1, $2, $3, $4)
      RETURNING id, email, role, created_at
    `;
    
    const result = await this.executeQuery(query, [
      userData.email,
      userData.passwordHash,
      userData.role || 'user',
      true
    ]);
    
    return result[0];
  }

  async updateUserLastLogin(userId: number) {
    const query = `
      UPDATE users 
      SET last_login = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP
      WHERE id = $1
    `;
    
    await this.executeQuery(query, [userId]);
  }
}
```

### API Security Configuration
```typescript
// config/security.config.ts
export const SecurityConfig = {
  jwt: {
    accessTokenExpiry: process.env.JWT_ACCESS_EXPIRY || '15m',
    refreshTokenExpiry: process.env.JWT_REFRESH_EXPIRY || '7d',
    algorithm: 'HS256' as const,
    issuer: process.env.JWT_ISSUER || 'your-app',
    audience: process.env.JWT_AUDIENCE || 'your-app-users',
  },
  
  rateLimit: {
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 100, // limit each IP to 100 requests per windowMs
    message: 'Too many requests from this IP',
    standardHeaders: true,
    legacyHeaders: false,
  },
  
  cors: {
    origin: process.env.ALLOWED_ORIGINS?.split(',') || ['http://localhost:3000'],
    credentials: true,
    optionsSuccessStatus: 200,
  },
  
  encryption: {
    algorithm: 'aes-256-gcm',
    keyLength: 32,
    ivLength: 16,
    tagLength: 16,
  },
  
  session: {
    secret: process.env.SESSION_SECRET!,
    resave: false,
    saveUninitialized: false,
    cookie: {
      secure: process.env.NODE_ENV === 'production',
      httpOnly: true,
      maxAge: 24 * 60 * 60 * 1000, // 24 hours
      sameSite: 'strict' as const,
    },
  },
  
  fileUpload: {
    maxFileSize: 5 * 1024 * 1024, // 5MB
    allowedMimeTypes: [
      'image/jpeg',
      'image/png',
      'image/gif',
      'application/pdf',
      'text/plain',
    ],
    uploadPath: process.env.UPLOAD_PATH || './uploads',
  },
};

// Environment validation
const requiredEnvVars = [
  'JWT_SECRET_KEY',
  'JWT_REFRESH_SECRET_KEY',
  'SESSION_SECRET',
  'DB_PASSWORD',
];

for (const envVar of requiredEnvVars) {
  if (!process.env[envVar]) {
    throw new Error(`Required environment variable ${envVar} is not set`);
  }
}
```

---

## 🎯 Task Specializations

### High Confidence Tasks (>90%)
- Authentication system implementation
- Authorization and RBAC setup
- Input validation and sanitization
- SQL injection prevention
- XSS protection implementation
- CSRF protection setup
- Security headers configuration
- Password hashing and validation

### Medium Confidence Tasks (75-90%)
- OAuth 2.0 / OpenID Connect integration
- API security implementation
- File upload security
- Session management
- Rate limiting implementation
- Security audit and code review
- Vulnerability assessment

### Collaborative Tasks (<75%)
- Advanced penetration testing (with Security Auditor)
- Compliance implementation (with Compliance Specialist)
- Infrastructure security (with DevOps Agent)
- Mobile app security (with Mobile Agents)

---

## 🔄 Agent Handoff Protocols

### Escalation Triggers
- Advanced penetration testing required
- Compliance-specific requirements (HIPAA, PCI DSS)
- Infrastructure-level security configuration
- Mobile-specific security implementations
- Complex cryptographic implementations

### Handoff Procedures
1. **Security Assessment**: Complete security audit report
2. **Vulnerability Report**: Detailed findings and recommendations
3. **Implementation Guide**: Step-by-step security implementation
4. **Compliance Checklist**: Regulatory compliance requirements

---

## 📊 Quality Assurance

### Security Standards
- **OWASP Compliance**: Follow OWASP Top 10 guidelines
- **Secure Coding**: Implement secure coding practices
- **Encryption**: Use industry-standard encryption methods
- **Authentication**: Multi-factor authentication where applicable

### Security Metrics
- **Vulnerability Count**: Zero critical vulnerabilities
- **Security Test Coverage**: >95% security test coverage
- **Compliance Score**: 100% compliance with applicable standards
- **Incident Response Time**: <1 hour for critical security issues

### Process Standards
- **Security by Design**: Security considerations from project start
- **Regular Audits**: Periodic security assessments
- **Threat Modeling**: Systematic threat analysis
- **Incident Response**: Defined security incident procedures

---

## 🛠️ Security Tools Integration

### Static Analysis
- **SonarQube**: Code quality and security analysis
- **CodeQL**: Semantic code analysis
- **Semgrep**: Static analysis for security bugs
- **ESLint Security**: JavaScript security linting

### Dynamic Analysis
- **OWASP ZAP**: Web application security scanner
- **Burp Suite**: Web vulnerability scanner
- **Nessus**: Vulnerability assessment
- **Nikto**: Web server scanner

### Security Testing
- **OWASP Dependency Check**: Dependency vulnerability scanning
- **Snyk**: Open source vulnerability management
- **npm audit**: Node.js dependency security audit
- **Docker Security Scanning**: Container vulnerability scanning

### Monitoring & Logging
- **SIEM Solutions**: Security information and event management
- **Log Analysis**: Security log monitoring
- **Intrusion Detection**: Network and host-based IDS
- **Security Metrics**: Security KPI tracking

---

## 🔒 Security Best Practices

### Secure Development
- **Principle of Least Privilege**: Minimal access rights
- **Defense in Depth**: Multiple security layers
- **Fail Securely**: Secure failure modes
- **Input Validation**: Validate all inputs

### Authentication & Authorization
- **Strong Password Policies**: Complex password requirements
- **Multi-Factor Authentication**: Additional security layers
- **Session Management**: Secure session handling
- **Token Security**: Secure token implementation

### Data Protection
- **Encryption at Rest**: Encrypt stored data
- **Encryption in Transit**: Secure data transmission
- **Data Minimization**: Collect only necessary data
- **Secure Deletion**: Proper data disposal

---

## 🚀 Continuous Improvement

### Learning Priorities
- Latest security threats and vulnerabilities
- New security frameworks and standards
- Advanced cryptographic techniques
- Cloud security best practices
- Mobile security developments

### Feedback Integration
- Security incident analysis
- Vulnerability assessment results
- Penetration testing findings
- Compliance audit outcomes
- Industry security trends

---

**🛡️ Specialized security expertise with focus on proactive threat prevention, secure development practices, and comprehensive security implementation.**