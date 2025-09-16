# 🔒 Security Standards - Universal Application Security Guidelines

> **🛡️ Comprehensive security framework for secure software development**  
> Defense-in-depth security practices across all platforms and technologies

---

## 🎯 Security Philosophy

### Core Security Principles
```
🔐 Defense in Depth
┌─────────────────────────────────────┐
│  Application Layer Security         │
├─────────────────────────────────────┤
│  API & Service Security             │
├─────────────────────────────────────┤
│  Data Layer Security                │
├─────────────────────────────────────┤
│  Infrastructure Security            │
├─────────────────────────────────────┤
│  Network Security                   │
└─────────────────────────────────────┘
```

### Security by Design
- **Principle of Least Privilege**: Minimal access rights
- **Zero Trust Architecture**: Never trust, always verify
- **Fail Secure**: Default to secure state on failure
- **Security in Depth**: Multiple layers of protection
- **Privacy by Design**: Data protection from the start

---

## 🔐 Authentication & Authorization

### JWT Implementation Standards
```javascript
// JWT Configuration
const jwtConfig = {
  secret: process.env.JWT_SECRET, // 256-bit random key
  algorithm: 'HS256',
  expiresIn: '15m', // Short-lived access tokens
  refreshTokenExpiry: '7d',
  issuer: 'your-app-name',
  audience: 'your-app-users'
};

// Secure JWT Generation
const generateTokens = (user) => {
  const payload = {
    userId: user.id,
    email: user.email,
    role: user.role,
    permissions: user.permissions
  };
  
  const accessToken = jwt.sign(payload, jwtConfig.secret, {
    algorithm: jwtConfig.algorithm,
    expiresIn: jwtConfig.expiresIn,
    issuer: jwtConfig.issuer,
    audience: jwtConfig.audience
  });
  
  const refreshToken = jwt.sign(
    { userId: user.id, tokenType: 'refresh' },
    process.env.REFRESH_TOKEN_SECRET,
    {
      algorithm: 'HS256',
      expiresIn: jwtConfig.refreshTokenExpiry,
      issuer: jwtConfig.issuer,
      audience: jwtConfig.audience
    }
  );
  
  return { accessToken, refreshToken };
};

// JWT Validation Middleware
const validateJWT = (req, res, next) => {
  try {
    const authHeader = req.headers.authorization;
    
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(401).json({ error: 'Missing or invalid authorization header' });
    }
    
    const token = authHeader.substring(7);
    
    const decoded = jwt.verify(token, jwtConfig.secret, {
      algorithms: [jwtConfig.algorithm],
      issuer: jwtConfig.issuer,
      audience: jwtConfig.audience
    });
    
    req.user = decoded;
    next();
  } catch (error) {
    if (error.name === 'TokenExpiredError') {
      return res.status(401).json({ error: 'Token expired' });
    }
    if (error.name === 'JsonWebTokenError') {
      return res.status(401).json({ error: 'Invalid token' });
    }
    return res.status(500).json({ error: 'Token validation failed' });
  }
};

// Role-Based Access Control
const requireRole = (roles) => {
  return (req, res, next) => {
    if (!req.user) {
      return res.status(401).json({ error: 'Authentication required' });
    }
    
    if (!roles.includes(req.user.role)) {
      return res.status(403).json({ error: 'Insufficient permissions' });
    }
    
    next();
  };
};

// Permission-Based Access Control
const requirePermission = (permission) => {
  return (req, res, next) => {
    if (!req.user) {
      return res.status(401).json({ error: 'Authentication required' });
    }
    
    if (!req.user.permissions.includes(permission)) {
      return res.status(403).json({ error: `Permission '${permission}' required` });
    }
    
    next();
  };
};
```

### OAuth2 Implementation
```javascript
// OAuth2 Configuration
const oauth2Config = {
  google: {
    clientId: process.env.GOOGLE_CLIENT_ID,
    clientSecret: process.env.GOOGLE_CLIENT_SECRET,
    redirectUri: process.env.GOOGLE_REDIRECT_URI,
    scope: ['openid', 'email', 'profile']
  },
  github: {
    clientId: process.env.GITHUB_CLIENT_ID,
    clientSecret: process.env.GITHUB_CLIENT_SECRET,
    redirectUri: process.env.GITHUB_REDIRECT_URI,
    scope: ['user:email']
  }
};

// OAuth2 Flow Implementation
const initiateOAuth2Flow = (provider) => {
  return (req, res) => {
    const config = oauth2Config[provider];
    if (!config) {
      return res.status(400).json({ error: 'Unsupported OAuth provider' });
    }
    
    const state = crypto.randomBytes(32).toString('hex');
    req.session.oauthState = state;
    
    const authUrl = new URL(`https://accounts.${provider}.com/oauth/authorize`);
    authUrl.searchParams.set('client_id', config.clientId);
    authUrl.searchParams.set('redirect_uri', config.redirectUri);
    authUrl.searchParams.set('scope', config.scope.join(' '));
    authUrl.searchParams.set('response_type', 'code');
    authUrl.searchParams.set('state', state);
    
    res.redirect(authUrl.toString());
  };
};

// OAuth2 Callback Handler
const handleOAuth2Callback = (provider) => {
  return async (req, res) => {
    try {
      const { code, state } = req.query;
      
      // Validate state parameter
      if (!state || state !== req.session.oauthState) {
        return res.status(400).json({ error: 'Invalid state parameter' });
      }
      
      // Exchange code for access token
      const tokenResponse = await exchangeCodeForToken(provider, code);
      
      // Get user info
      const userInfo = await getUserInfo(provider, tokenResponse.access_token);
      
      // Create or update user
      const user = await createOrUpdateOAuthUser(provider, userInfo);
      
      // Generate JWT tokens
      const tokens = generateTokens(user);
      
      res.json({
        user: {
          id: user.id,
          email: user.email,
          name: user.name
        },
        tokens
      });
    } catch (error) {
      console.error('OAuth2 callback error:', error);
      res.status(500).json({ error: 'OAuth2 authentication failed' });
    }
  };
};
```

---

## 🔒 Password Security

### Password Hashing Standards
```javascript
const bcrypt = require('bcrypt');
const argon2 = require('argon2');

// Bcrypt Configuration (Recommended for most cases)
const BCRYPT_ROUNDS = 12; // Adjust based on performance requirements

const hashPasswordBcrypt = async (password) => {
  try {
    const salt = await bcrypt.genSalt(BCRYPT_ROUNDS);
    const hashedPassword = await bcrypt.hash(password, salt);
    return hashedPassword;
  } catch (error) {
    throw new Error('Password hashing failed');
  }
};

const verifyPasswordBcrypt = async (password, hashedPassword) => {
  try {
    return await bcrypt.compare(password, hashedPassword);
  } catch (error) {
    throw new Error('Password verification failed');
  }
};

// Argon2 Configuration (More secure, higher resource usage)
const argon2Config = {
  type: argon2.argon2id,
  memoryCost: 2 ** 16, // 64 MB
  timeCost: 3,
  parallelism: 1,
  hashLength: 32
};

const hashPasswordArgon2 = async (password) => {
  try {
    return await argon2.hash(password, argon2Config);
  } catch (error) {
    throw new Error('Password hashing failed');
  }
};

const verifyPasswordArgon2 = async (password, hashedPassword) => {
  try {
    return await argon2.verify(hashedPassword, password);
  } catch (error) {
    throw new Error('Password verification failed');
  }
};

// Password Strength Validation
const validatePasswordStrength = (password) => {
  const errors = [];
  
  if (password.length < 8) {
    errors.push('Password must be at least 8 characters long');
  }
  
  if (password.length > 128) {
    errors.push('Password must be less than 128 characters');
  }
  
  if (!/[a-z]/.test(password)) {
    errors.push('Password must contain at least one lowercase letter');
  }
  
  if (!/[A-Z]/.test(password)) {
    errors.push('Password must contain at least one uppercase letter');
  }
  
  if (!/\d/.test(password)) {
    errors.push('Password must contain at least one number');
  }
  
  if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)) {
    errors.push('Password must contain at least one special character');
  }
  
  // Check against common passwords
  const commonPasswords = [
    'password', '123456', 'password123', 'admin', 'qwerty',
    'letmein', 'welcome', 'monkey', '1234567890'
  ];
  
  if (commonPasswords.includes(password.toLowerCase())) {
    errors.push('Password is too common');
  }
  
  return {
    isValid: errors.length === 0,
    errors
  };
};

// Password Reset Security
const generatePasswordResetToken = () => {
  return {
    token: crypto.randomBytes(32).toString('hex'),
    expires: new Date(Date.now() + 15 * 60 * 1000) // 15 minutes
  };
};

const validatePasswordResetToken = async (token, userId) => {
  const resetRequest = await PasswordReset.findOne({
    token,
    userId,
    expires: { $gt: new Date() },
    used: false
  });
  
  return resetRequest !== null;
};
```

---

## 🛡️ Input Validation & Sanitization

### Input Validation Framework
```javascript
const Joi = require('joi');
const DOMPurify = require('isomorphic-dompurify');
const validator = require('validator');

// Validation Schemas
const userRegistrationSchema = Joi.object({
  email: Joi.string()
    .email({ tlds: { allow: false } })
    .required()
    .max(254)
    .messages({
      'string.email': 'Invalid email format',
      'any.required': 'Email is required',
      'string.max': 'Email too long'
    }),
  
  password: Joi.string()
    .min(8)
    .max(128)
    .pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?])/)
    .required()
    .messages({
      'string.min': 'Password must be at least 8 characters',
      'string.max': 'Password too long',
      'string.pattern.base': 'Password must contain uppercase, lowercase, number, and special character',
      'any.required': 'Password is required'
    }),
  
  name: Joi.string()
    .min(1)
    .max(100)
    .pattern(/^[a-zA-Z\s'-]+$/)
    .required()
    .messages({
      'string.min': 'Name is required',
      'string.max': 'Name too long',
      'string.pattern.base': 'Name contains invalid characters',
      'any.required': 'Name is required'
    }),
  
  phone: Joi.string()
    .pattern(/^\+?[1-9]\d{1,14}$/)
    .optional()
    .messages({
      'string.pattern.base': 'Invalid phone number format'
    }),
  
  dateOfBirth: Joi.date()
    .max('now')
    .min('1900-01-01')
    .optional()
    .messages({
      'date.max': 'Date of birth cannot be in the future',
      'date.min': 'Invalid date of birth'
    })
});

// Validation Middleware
const validateInput = (schema) => {
  return (req, res, next) => {
    const { error, value } = schema.validate(req.body, {
      abortEarly: false,
      stripUnknown: true
    });
    
    if (error) {
      const errors = error.details.map(detail => ({
        field: detail.path.join('.'),
        message: detail.message,
        value: detail.context.value
      }));
      
      return res.status(400).json({
        error: 'Validation failed',
        errors
      });
    }
    
    req.validatedData = value;
    next();
  };
};

// HTML Sanitization
const sanitizeHtml = (input) => {
  if (typeof input !== 'string') {
    return input;
  }
  
  return DOMPurify.sanitize(input, {
    ALLOWED_TAGS: ['b', 'i', 'em', 'strong', 'p', 'br'],
    ALLOWED_ATTR: [],
    KEEP_CONTENT: true
  });
};

// SQL Injection Prevention
const sanitizeForSQL = (input) => {
  if (typeof input !== 'string') {
    return input;
  }
  
  // Remove or escape dangerous SQL characters
  return input
    .replace(/'/g, "''")
    .replace(/"/g, '""')
    .replace(/;/g, '')
    .replace(/--/g, '')
    .replace(/\/\*/g, '')
    .replace(/\*\//g, '');
};

// XSS Prevention
const preventXSS = (req, res, next) => {
  const sanitizeObject = (obj) => {
    for (const key in obj) {
      if (typeof obj[key] === 'string') {
        obj[key] = sanitizeHtml(obj[key]);
      } else if (typeof obj[key] === 'object' && obj[key] !== null) {
        sanitizeObject(obj[key]);
      }
    }
  };
  
  if (req.body) {
    sanitizeObject(req.body);
  }
  
  if (req.query) {
    sanitizeObject(req.query);
  }
  
  if (req.params) {
    sanitizeObject(req.params);
  }
  
  next();
};

// File Upload Validation
const validateFileUpload = {
  allowedMimeTypes: [
    'image/jpeg',
    'image/png',
    'image/gif',
    'image/webp',
    'application/pdf',
    'text/plain'
  ],
  
  maxFileSize: 5 * 1024 * 1024, // 5MB
  
  validate: (file) => {
    const errors = [];
    
    if (!validateFileUpload.allowedMimeTypes.includes(file.mimetype)) {
      errors.push('File type not allowed');
    }
    
    if (file.size > validateFileUpload.maxFileSize) {
      errors.push('File size too large');
    }
    
    // Check file signature (magic numbers)
    const fileSignatures = {
      'image/jpeg': [0xFF, 0xD8, 0xFF],
      'image/png': [0x89, 0x50, 0x4E, 0x47],
      'application/pdf': [0x25, 0x50, 0x44, 0x46]
    };
    
    const signature = fileSignatures[file.mimetype];
    if (signature) {
      const fileHeader = Array.from(file.buffer.slice(0, signature.length));
      if (!signature.every((byte, index) => byte === fileHeader[index])) {
        errors.push('File signature mismatch');
      }
    }
    
    return {
      isValid: errors.length === 0,
      errors
    };
  }
};
```

---

## 🔐 Data Protection & Encryption

### Encryption Standards
```javascript
const crypto = require('crypto');
const CryptoJS = require('crypto-js');

// AES Encryption Configuration
const encryptionConfig = {
  algorithm: 'aes-256-gcm',
  keyLength: 32,
  ivLength: 16,
  tagLength: 16
};

// Generate Encryption Key
const generateEncryptionKey = () => {
  return crypto.randomBytes(encryptionConfig.keyLength);
};

// Encrypt Sensitive Data
const encryptData = (data, key) => {
  try {
    const iv = crypto.randomBytes(encryptionConfig.ivLength);
    const cipher = crypto.createCipher(encryptionConfig.algorithm, key, iv);
    
    let encrypted = cipher.update(JSON.stringify(data), 'utf8', 'hex');
    encrypted += cipher.final('hex');
    
    const tag = cipher.getAuthTag();
    
    return {
      encrypted,
      iv: iv.toString('hex'),
      tag: tag.toString('hex')
    };
  } catch (error) {
    throw new Error('Encryption failed');
  }
};

// Decrypt Sensitive Data
const decryptData = (encryptedData, key) => {
  try {
    const { encrypted, iv, tag } = encryptedData;
    
    const decipher = crypto.createDecipher(
      encryptionConfig.algorithm,
      key,
      Buffer.from(iv, 'hex')
    );
    
    decipher.setAuthTag(Buffer.from(tag, 'hex'));
    
    let decrypted = decipher.update(encrypted, 'hex', 'utf8');
    decrypted += decipher.final('utf8');
    
    return JSON.parse(decrypted);
  } catch (error) {
    throw new Error('Decryption failed');
  }
};

// PII Data Encryption
class PIIEncryption {
  constructor(encryptionKey) {
    this.key = encryptionKey;
  }
  
  encryptPII(data) {
    const piiFields = ['ssn', 'creditCard', 'bankAccount', 'passport'];
    const encryptedData = { ...data };
    
    piiFields.forEach(field => {
      if (encryptedData[field]) {
        encryptedData[field] = encryptData(encryptedData[field], this.key);
      }
    });
    
    return encryptedData;
  }
  
  decryptPII(data) {
    const piiFields = ['ssn', 'creditCard', 'bankAccount', 'passport'];
    const decryptedData = { ...data };
    
    piiFields.forEach(field => {
      if (decryptedData[field] && typeof decryptedData[field] === 'object') {
        decryptedData[field] = decryptData(decryptedData[field], this.key);
      }
    });
    
    return decryptedData;
  }
}

// Database Field Encryption
const encryptDatabaseField = (value, fieldKey) => {
  if (!value) return value;
  
  const key = crypto.pbkdf2Sync(fieldKey, 'salt', 10000, 32, 'sha256');
  return encryptData(value, key);
};

const decryptDatabaseField = (encryptedValue, fieldKey) => {
  if (!encryptedValue) return encryptedValue;
  
  const key = crypto.pbkdf2Sync(fieldKey, 'salt', 10000, 32, 'sha256');
  return decryptData(encryptedValue, key);
};
```

---

## 🚫 Rate Limiting & DDoS Protection

### Rate Limiting Implementation
```javascript
const rateLimit = require('express-rate-limit');
const RedisStore = require('rate-limit-redis');
const redis = require('redis');

// Redis Client for Rate Limiting
const redisClient = redis.createClient({
  host: process.env.REDIS_HOST,
  port: process.env.REDIS_PORT,
  password: process.env.REDIS_PASSWORD
});

// General API Rate Limiting
const generalRateLimit = rateLimit({
  store: new RedisStore({
    client: redisClient,
    prefix: 'rl:general:'
  }),
  windowMs: 15 * 60 * 1000, // 15 minutes
  max: 100, // 100 requests per window
  message: {
    error: 'Too many requests',
    retryAfter: '15 minutes'
  },
  standardHeaders: true,
  legacyHeaders: false,
  keyGenerator: (req) => {
    return req.ip + ':' + (req.user?.id || 'anonymous');
  }
});

// Authentication Rate Limiting
const authRateLimit = rateLimit({
  store: new RedisStore({
    client: redisClient,
    prefix: 'rl:auth:'
  }),
  windowMs: 15 * 60 * 1000, // 15 minutes
  max: 5, // 5 login attempts per window
  message: {
    error: 'Too many login attempts',
    retryAfter: '15 minutes'
  },
  skipSuccessfulRequests: true,
  keyGenerator: (req) => {
    return req.ip + ':' + (req.body.email || 'unknown');
  }
});

// Password Reset Rate Limiting
const passwordResetRateLimit = rateLimit({
  store: new RedisStore({
    client: redisClient,
    prefix: 'rl:password-reset:'
  }),
  windowMs: 60 * 60 * 1000, // 1 hour
  max: 3, // 3 password reset attempts per hour
  message: {
    error: 'Too many password reset attempts',
    retryAfter: '1 hour'
  },
  keyGenerator: (req) => {
    return req.body.email || req.ip;
  }
});

// File Upload Rate Limiting
const fileUploadRateLimit = rateLimit({
  store: new RedisStore({
    client: redisClient,
    prefix: 'rl:upload:'
  }),
  windowMs: 60 * 60 * 1000, // 1 hour
  max: 10, // 10 file uploads per hour
  message: {
    error: 'Too many file uploads',
    retryAfter: '1 hour'
  },
  keyGenerator: (req) => {
    return req.user?.id || req.ip;
  }
});

// Advanced Rate Limiting with Sliding Window
class SlidingWindowRateLimit {
  constructor(redisClient, options) {
    this.redis = redisClient;
    this.windowSize = options.windowMs;
    this.maxRequests = options.max;
    this.keyPrefix = options.keyPrefix || 'rl:sliding:';
  }
  
  async isAllowed(key) {
    const now = Date.now();
    const window = Math.floor(now / this.windowSize);
    const redisKey = `${this.keyPrefix}${key}:${window}`;
    
    const current = await this.redis.incr(redisKey);
    
    if (current === 1) {
      await this.redis.expire(redisKey, Math.ceil(this.windowSize / 1000));
    }
    
    return current <= this.maxRequests;
  }
  
  middleware() {
    return async (req, res, next) => {
      const key = req.ip + ':' + (req.user?.id || 'anonymous');
      const allowed = await this.isAllowed(key);
      
      if (!allowed) {
        return res.status(429).json({
          error: 'Rate limit exceeded',
          retryAfter: Math.ceil(this.windowSize / 1000)
        });
      }
      
      next();
    };
  }
}
```

---

## 🔍 Security Headers & CORS

### Security Headers Configuration
```javascript
const helmet = require('helmet');
const cors = require('cors');

// Comprehensive Security Headers
const securityHeaders = helmet({
  // Content Security Policy
  contentSecurityPolicy: {
    directives: {
      defaultSrc: ["'self'"],
      styleSrc: ["'self'", "'unsafe-inline'", 'https://fonts.googleapis.com'],
      fontSrc: ["'self'", 'https://fonts.gstatic.com'],
      imgSrc: ["'self'", 'data:', 'https:'],
      scriptSrc: ["'self'"],
      objectSrc: ["'none'"],
      mediaSrc: ["'self'"],
      frameSrc: ["'none'"],
      connectSrc: ["'self'", 'https://api.example.com']
    }
  },
  
  // HTTP Strict Transport Security
  hsts: {
    maxAge: 31536000, // 1 year
    includeSubDomains: true,
    preload: true
  },
  
  // X-Frame-Options
  frameguard: {
    action: 'deny'
  },
  
  // X-Content-Type-Options
  noSniff: true,
  
  // X-XSS-Protection
  xssFilter: true,
  
  // Referrer Policy
  referrerPolicy: {
    policy: 'strict-origin-when-cross-origin'
  },
  
  // Permissions Policy
  permissionsPolicy: {
    features: {
      camera: ['self'],
      microphone: ['self'],
      geolocation: ['self'],
      payment: ['self']
    }
  }
});

// CORS Configuration
const corsOptions = {
  origin: (origin, callback) => {
    const allowedOrigins = [
      'https://yourdomain.com',
      'https://www.yourdomain.com',
      'https://app.yourdomain.com'
    ];
    
    // Allow requests with no origin (mobile apps, etc.)
    if (!origin) return callback(null, true);
    
    if (allowedOrigins.includes(origin)) {
      callback(null, true);
    } else {
      callback(new Error('Not allowed by CORS'));
    }
  },
  credentials: true,
  methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
  allowedHeaders: [
    'Origin',
    'X-Requested-With',
    'Content-Type',
    'Accept',
    'Authorization',
    'X-API-Key'
  ],
  exposedHeaders: ['X-Total-Count', 'X-Page-Count'],
  maxAge: 86400 // 24 hours
};

// Custom Security Middleware
const customSecurityHeaders = (req, res, next) => {
  // Remove server information
  res.removeHeader('X-Powered-By');
  
  // Add custom security headers
  res.setHeader('X-API-Version', '1.0');
  res.setHeader('X-Request-ID', req.id || crypto.randomUUID());
  
  // Prevent MIME type sniffing
  res.setHeader('X-Content-Type-Options', 'nosniff');
  
  // Prevent clickjacking
  res.setHeader('X-Frame-Options', 'DENY');
  
  // Enable XSS protection
  res.setHeader('X-XSS-Protection', '1; mode=block');
  
  next();
};
```

---

## 🔐 API Security Standards

### API Key Management
```javascript
// API Key Generation
const generateAPIKey = () => {
  const prefix = 'ak_'; // API key prefix
  const randomPart = crypto.randomBytes(32).toString('hex');
  return prefix + randomPart;
};

// API Key Validation
const validateAPIKey = async (req, res, next) => {
  try {
    const apiKey = req.headers['x-api-key'] || req.query.api_key;
    
    if (!apiKey) {
      return res.status(401).json({ error: 'API key required' });
    }
    
    // Validate API key format
    if (!apiKey.startsWith('ak_') || apiKey.length !== 67) {
      return res.status(401).json({ error: 'Invalid API key format' });
    }
    
    // Check API key in database
    const apiKeyRecord = await APIKey.findOne({
      key: apiKey,
      isActive: true,
      expiresAt: { $gt: new Date() }
    });
    
    if (!apiKeyRecord) {
      return res.status(401).json({ error: 'Invalid or expired API key' });
    }
    
    // Update last used timestamp
    await APIKey.updateOne(
      { _id: apiKeyRecord._id },
      { lastUsedAt: new Date() }
    );
    
    req.apiKey = apiKeyRecord;
    next();
  } catch (error) {
    console.error('API key validation error:', error);
    res.status(500).json({ error: 'API key validation failed' });
  }
};

// API Request Signing
const signAPIRequest = (method, url, body, secret) => {
  const timestamp = Date.now();
  const payload = `${method}${url}${JSON.stringify(body || {})}${timestamp}`;
  const signature = crypto
    .createHmac('sha256', secret)
    .update(payload)
    .digest('hex');
  
  return {
    timestamp,
    signature
  };
};

// API Request Verification
const verifyAPIRequest = (req, res, next) => {
  try {
    const signature = req.headers['x-signature'];
    const timestamp = req.headers['x-timestamp'];
    
    if (!signature || !timestamp) {
      return res.status(401).json({ error: 'Missing signature or timestamp' });
    }
    
    // Check timestamp (prevent replay attacks)
    const now = Date.now();
    const requestTime = parseInt(timestamp);
    const timeDiff = Math.abs(now - requestTime);
    
    if (timeDiff > 300000) { // 5 minutes
      return res.status(401).json({ error: 'Request timestamp too old' });
    }
    
    // Verify signature
    const payload = `${req.method}${req.originalUrl}${JSON.stringify(req.body || {})}${timestamp}`;
    const expectedSignature = crypto
      .createHmac('sha256', req.apiKey.secret)
      .update(payload)
      .digest('hex');
    
    if (signature !== expectedSignature) {
      return res.status(401).json({ error: 'Invalid signature' });
    }
    
    next();
  } catch (error) {
    console.error('API request verification error:', error);
    res.status(500).json({ error: 'Request verification failed' });
  }
};
```

---

## 🔍 Security Monitoring & Logging

### Security Event Logging
```javascript
const winston = require('winston');
const { ElasticsearchTransport } = require('winston-elasticsearch');

// Security Logger Configuration
const securityLogger = winston.createLogger({
  level: 'info',
  format: winston.format.combine(
    winston.format.timestamp(),
    winston.format.errors({ stack: true }),
    winston.format.json()
  ),
  transports: [
    new winston.transports.File({
      filename: 'logs/security.log',
      level: 'warn'
    }),
    new winston.transports.File({
      filename: 'logs/security-error.log',
      level: 'error'
    }),
    new ElasticsearchTransport({
      level: 'info',
      clientOpts: {
        node: process.env.ELASTICSEARCH_URL
      },
      index: 'security-logs'
    })
  ]
});

// Security Event Types
const SecurityEvents = {
  LOGIN_SUCCESS: 'login_success',
  LOGIN_FAILURE: 'login_failure',
  LOGIN_BLOCKED: 'login_blocked',
  PASSWORD_RESET: 'password_reset',
  ACCOUNT_LOCKED: 'account_locked',
  SUSPICIOUS_ACTIVITY: 'suspicious_activity',
  API_KEY_USED: 'api_key_used',
  RATE_LIMIT_EXCEEDED: 'rate_limit_exceeded',
  UNAUTHORIZED_ACCESS: 'unauthorized_access',
  DATA_BREACH_ATTEMPT: 'data_breach_attempt'
};

// Security Event Logger
const logSecurityEvent = (eventType, details, req) => {
  const logData = {
    event: eventType,
    timestamp: new Date().toISOString(),
    ip: req.ip,
    userAgent: req.get('User-Agent'),
    userId: req.user?.id,
    sessionId: req.sessionID,
    ...details
  };
  
  securityLogger.info('Security Event', logData);
  
  // Send alerts for critical events
  const criticalEvents = [
    SecurityEvents.DATA_BREACH_ATTEMPT,
    SecurityEvents.SUSPICIOUS_ACTIVITY,
    SecurityEvents.ACCOUNT_LOCKED
  ];
  
  if (criticalEvents.includes(eventType)) {
    sendSecurityAlert(logData);
  }
};

// Suspicious Activity Detection
const detectSuspiciousActivity = async (req, res, next) => {
  try {
    const userId = req.user?.id;
    const ip = req.ip;
    const userAgent = req.get('User-Agent');
    
    // Check for multiple IPs for same user
    if (userId) {
      const recentIPs = await getRecentIPsForUser(userId, 24); // Last 24 hours
      if (recentIPs.length > 5) {
        logSecurityEvent(SecurityEvents.SUSPICIOUS_ACTIVITY, {
          reason: 'Multiple IPs detected',
          userId,
          ipCount: recentIPs.length,
          ips: recentIPs
        }, req);
      }
    }
    
    // Check for unusual user agent
    if (userAgent && isUnusualUserAgent(userAgent)) {
      logSecurityEvent(SecurityEvents.SUSPICIOUS_ACTIVITY, {
        reason: 'Unusual user agent',
        userAgent
      }, req);
    }
    
    // Check for rapid requests
    const requestCount = await getRequestCountForIP(ip, 60); // Last minute
    if (requestCount > 100) {
      logSecurityEvent(SecurityEvents.SUSPICIOUS_ACTIVITY, {
        reason: 'Rapid requests detected',
        requestCount,
        timeWindow: '1 minute'
      }, req);
    }
    
    next();
  } catch (error) {
    console.error('Suspicious activity detection error:', error);
    next();
  }
};

// Security Alert System
const sendSecurityAlert = async (eventData) => {
  try {
    // Send email alert
    await sendEmail({
      to: process.env.SECURITY_TEAM_EMAIL,
      subject: `Security Alert: ${eventData.event}`,
      template: 'security-alert',
      data: eventData
    });
    
    // Send Slack notification
    await sendSlackNotification({
      channel: '#security-alerts',
      message: `🚨 Security Alert: ${eventData.event}`,
      details: eventData
    });
    
    // Log to security incident system
    await createSecurityIncident(eventData);
  } catch (error) {
    console.error('Failed to send security alert:', error);
  }
};
```

---

## ✅ Security Checklist

### Pre-Development Security
- [ ] Security requirements defined
- [ ] Threat modeling completed
- [ ] Security architecture reviewed
- [ ] Secure coding guidelines established
- [ ] Security testing strategy planned

### Authentication & Authorization
- [ ] Strong password policy implemented
- [ ] Multi-factor authentication available
- [ ] JWT tokens properly configured
- [ ] Session management secure
- [ ] Role-based access control implemented
- [ ] OAuth2 flows properly secured

### Input Validation & Sanitization
- [ ] All inputs validated and sanitized
- [ ] SQL injection prevention implemented
- [ ] XSS protection in place
- [ ] File upload security configured
- [ ] CSRF protection enabled

### Data Protection
- [ ] Sensitive data encrypted at rest
- [ ] Data encrypted in transit (HTTPS)
- [ ] PII data properly protected
- [ ] Database access secured
- [ ] Backup encryption implemented

### API Security
- [ ] API authentication implemented
- [ ] Rate limiting configured
- [ ] API versioning strategy
- [ ] Request/response validation
- [ ] API documentation security reviewed

### Infrastructure Security
- [ ] Security headers configured
- [ ] CORS properly configured
- [ ] Server hardening completed
- [ ] Network security implemented
- [ ] Monitoring and logging active

### Compliance & Governance
- [ ] GDPR compliance verified
- [ ] Security policies documented
- [ ] Incident response plan ready
- [ ] Security training completed
- [ ] Regular security audits scheduled

---

**🛡️ Comprehensive security framework ensuring robust protection across all application layers and development phases.**