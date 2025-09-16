# Security Patterns & Standards

> **🔒 Comprehensive Security Framework**  
> Enterprise-grade security patterns for robust, secure system design

## Security Philosophy

**Mission**: Build inherently secure systems with defense-in-depth strategy  
**Approach**: Security by design, zero-trust architecture, continuous validation  
**Principle**: Security is not optional - every component must be secure by default

---

## 🛡️ Core Security Principles

### Fundamental Security Standards

**Zero Trust**: Never trust, always verify - authenticate and authorize every request  
**Defense in Depth**: Multiple layers of security controls and validation  
**Principle of Least Privilege**: Minimum necessary access rights  
**Fail Secure**: System fails to a secure state when errors occur  
**Security by Design**: Security considerations from the beginning of development

### Security Categories

1. **Authentication**: Identity verification and user management
2. **Authorization**: Access control and permission management
3. **Data Protection**: Encryption, hashing, and data integrity
4. **Input Validation**: Sanitization and validation of all inputs
5. **Communication Security**: Secure data transmission and API protection
6. **Infrastructure Security**: System and network security

---

## 🔐 Authentication & Authorization Patterns

### Multi-Factor Authentication System
```typescript
enum AuthenticationFactor {
  KNOWLEDGE = 'knowledge',    // Something you know (password)
  POSSESSION = 'possession',  // Something you have (phone, token)
  INHERENCE = 'inherence',   // Something you are (biometric)
  LOCATION = 'location',     // Somewhere you are (geolocation)
  BEHAVIOR = 'behavior'      // Something you do (typing pattern)
}

enum AuthenticationMethod {
  PASSWORD = 'password',
  SMS_OTP = 'sms_otp',
  EMAIL_OTP = 'email_otp',
  TOTP = 'totp',
  PUSH_NOTIFICATION = 'push_notification',
  BIOMETRIC = 'biometric',
  HARDWARE_TOKEN = 'hardware_token',
  SMART_CARD = 'smart_card'
}

interface AuthenticationConfig {
  requiredFactors: number;
  allowedMethods: AuthenticationMethod[];
  sessionTimeout: number;
  maxFailedAttempts: number;
  lockoutDuration: number;
  passwordPolicy: PasswordPolicy;
  biometricSettings?: BiometricSettings;
}

class SecureAuthenticationManager {
  private authProviders: Map<AuthenticationMethod, AuthProvider> = new Map();
  private sessionManager: SessionManager;
  private auditLogger: SecurityAuditLogger;
  private riskAnalyzer: AuthenticationRiskAnalyzer;
  
  constructor(config: AuthenticationConfig) {
    this.sessionManager = new SessionManager(config.sessionTimeout);
    this.auditLogger = new SecurityAuditLogger();
    this.riskAnalyzer = new AuthenticationRiskAnalyzer();
    
    this.initializeProviders(config);
  }
  
  async authenticate(
    credentials: AuthenticationCredentials,
    context: AuthenticationContext
  ): Promise<AuthenticationResult> {
    const startTime = Date.now();
    
    try {
      // Risk assessment
      const riskScore = await this.riskAnalyzer.assessRisk(credentials, context);
      
      // Determine required authentication factors based on risk
      const requiredFactors = this.determineRequiredFactors(riskScore, context);
      
      // Validate credentials
      const validationResults = await this.validateCredentials(
        credentials,
        requiredFactors
      );
      
      // Check if all required factors are satisfied
      if (!this.areFactorsSatisfied(validationResults, requiredFactors)) {
        await this.auditLogger.logFailedAuthentication({
          userId: credentials.userId,
          reason: 'insufficient_factors',
          riskScore,
          context,
          timestamp: new Date().toISOString()
        });
        
        return {
          success: false,
          reason: 'insufficient_authentication_factors',
          requiredFactors: requiredFactors.filter(
            factor => !validationResults.satisfiedFactors.includes(factor)
          )
        };
      }
      
      // Create secure session
      const session = await this.sessionManager.createSession({
        userId: credentials.userId,
        authenticationMethods: validationResults.usedMethods,
        riskScore,
        context
      });
      
      // Log successful authentication
      await this.auditLogger.logSuccessfulAuthentication({
        userId: credentials.userId,
        sessionId: session.id,
        authenticationMethods: validationResults.usedMethods,
        riskScore,
        context,
        duration: Date.now() - startTime,
        timestamp: new Date().toISOString()
      });
      
      return {
        success: true,
        session,
        user: await this.getUserProfile(credentials.userId),
        permissions: await this.getUserPermissions(credentials.userId)
      };
      
    } catch (error) {
      await this.auditLogger.logAuthenticationError({
        userId: credentials.userId,
        error: error.message,
        context,
        timestamp: new Date().toISOString()
      });
      
      throw new SecurityError('Authentication failed', {
        code: 'AUTHENTICATION_ERROR',
        originalError: error
      });
    }
  }
  
  private async validateCredentials(
    credentials: AuthenticationCredentials,
    requiredFactors: AuthenticationFactor[]
  ): Promise<ValidationResult> {
    const results: FactorValidationResult[] = [];
    const satisfiedFactors: AuthenticationFactor[] = [];
    const usedMethods: AuthenticationMethod[] = [];
    
    // Validate each provided credential
    for (const [method, value] of Object.entries(credentials.factors)) {
      const provider = this.authProviders.get(method as AuthenticationMethod);
      if (!provider) continue;
      
      try {
        const result = await provider.validate(value, {
          userId: credentials.userId,
          timestamp: Date.now()
        });
        
        results.push({
          method: method as AuthenticationMethod,
          factor: provider.getFactor(),
          success: result.valid,
          confidence: result.confidence,
          metadata: result.metadata
        });
        
        if (result.valid) {
          satisfiedFactors.push(provider.getFactor());
          usedMethods.push(method as AuthenticationMethod);
        }
        
      } catch (error) {
        results.push({
          method: method as AuthenticationMethod,
          factor: provider.getFactor(),
          success: false,
          error: error.message
        });
      }
    }
    
    return {
      results,
      satisfiedFactors,
      usedMethods,
      overallSuccess: this.areFactorsSatisfied(
        { satisfiedFactors } as ValidationResult,
        requiredFactors
      )
    };
  }
  
  private determineRequiredFactors(
    riskScore: number,
    context: AuthenticationContext
  ): AuthenticationFactor[] {
    const factors: AuthenticationFactor[] = [AuthenticationFactor.KNOWLEDGE];
    
    // High-risk scenarios require additional factors
    if (riskScore > 0.7 || context.isHighPrivilegeOperation) {
      factors.push(AuthenticationFactor.POSSESSION);
    }
    
    // Very high-risk scenarios require biometric
    if (riskScore > 0.9 || context.isAdministrativeOperation) {
      factors.push(AuthenticationFactor.INHERENCE);
    }
    
    // Location-based requirements
    if (context.isUnknownLocation || context.isHighRiskLocation) {
      factors.push(AuthenticationFactor.LOCATION);
    }
    
    return factors;
  }
}

// Role-Based Access Control (RBAC) with Attribute-Based Access Control (ABAC)
class AccessControlManager {
  private roleManager: RoleManager;
  private permissionManager: PermissionManager;
  private policyEngine: PolicyEngine;
  private auditLogger: SecurityAuditLogger;
  
  constructor() {
    this.roleManager = new RoleManager();
    this.permissionManager = new PermissionManager();
    this.policyEngine = new PolicyEngine();
    this.auditLogger = new SecurityAuditLogger();
  }
  
  async authorize(
    subject: SecuritySubject,
    resource: SecurityResource,
    action: SecurityAction,
    context: AuthorizationContext
  ): Promise<AuthorizationResult> {
    try {
      // Get subject's roles and permissions
      const roles = await this.roleManager.getUserRoles(subject.id);
      const permissions = await this.permissionManager.getUserPermissions(subject.id);
      
      // Evaluate RBAC policies
      const rbacResult = await this.evaluateRBACPolicies({
        subject,
        resource,
        action,
        roles,
        permissions
      });
      
      // Evaluate ABAC policies
      const abacResult = await this.evaluateABACPolicies({
        subject,
        resource,
        action,
        context,
        attributes: {
          subjectAttributes: subject.attributes,
          resourceAttributes: resource.attributes,
          environmentAttributes: context.environment
        }
      });
      
      // Combine results (both must allow)
      const finalDecision = rbacResult.decision === 'ALLOW' && abacResult.decision === 'ALLOW'
        ? 'ALLOW'
        : 'DENY';
      
      const result: AuthorizationResult = {
        decision: finalDecision,
        reason: finalDecision === 'ALLOW' ? 'Access granted' : this.getDenyReason(rbacResult, abacResult),
        rbacResult,
        abacResult,
        appliedPolicies: [...rbacResult.appliedPolicies, ...abacResult.appliedPolicies],
        timestamp: new Date().toISOString()
      };
      
      // Audit the authorization decision
      await this.auditLogger.logAuthorizationDecision({
        subject: subject.id,
        resource: resource.id,
        action: action.name,
        decision: finalDecision,
        reason: result.reason,
        context,
        timestamp: result.timestamp
      });
      
      return result;
      
    } catch (error) {
      await this.auditLogger.logAuthorizationError({
        subject: subject.id,
        resource: resource.id,
        action: action.name,
        error: error.message,
        context,
        timestamp: new Date().toISOString()
      });
      
      // Fail secure - deny access on error
      return {
        decision: 'DENY',
        reason: 'Authorization system error',
        error: error.message,
        timestamp: new Date().toISOString()
      };
    }
  }
  
  private async evaluateRBACPolicies(params: RBACEvaluationParams): Promise<PolicyResult> {
    const { subject, resource, action, roles, permissions } = params;
    
    // Check direct permissions
    const hasDirectPermission = permissions.some(permission => 
      permission.resource === resource.type &&
      permission.actions.includes(action.name)
    );
    
    if (hasDirectPermission) {
      return {
        decision: 'ALLOW',
        reason: 'Direct permission granted',
        appliedPolicies: ['direct_permission']
      };
    }
    
    // Check role-based permissions
    for (const role of roles) {
      const rolePermissions = await this.permissionManager.getRolePermissions(role.id);
      
      const hasRolePermission = rolePermissions.some(permission =>
        permission.resource === resource.type &&
        permission.actions.includes(action.name)
      );
      
      if (hasRolePermission) {
        return {
          decision: 'ALLOW',
          reason: `Access granted through role: ${role.name}`,
          appliedPolicies: [`role_${role.name}`]
        };
      }
    }
    
    return {
      decision: 'DENY',
      reason: 'No matching RBAC permissions',
      appliedPolicies: []
    };
  }
  
  private async evaluateABACPolicies(params: ABACEvaluationParams): Promise<PolicyResult> {
    const policies = await this.policyEngine.getApplicablePolicies(
      params.resource.type,
      params.action.name
    );
    
    for (const policy of policies) {
      const result = await this.policyEngine.evaluatePolicy(policy, params);
      
      if (result.decision === 'DENY') {
        return {
          decision: 'DENY',
          reason: result.reason,
          appliedPolicies: [policy.id]
        };
      }
    }
    
    return {
      decision: 'ALLOW',
      reason: 'All ABAC policies satisfied',
      appliedPolicies: policies.map(p => p.id)
    };
  }
}

// JWT Token Management with Security Best Practices
class SecureTokenManager {
  private signingKey: string;
  private encryptionKey: string;
  private tokenBlacklist: Set<string> = new Set();
  private refreshTokens: Map<string, RefreshTokenInfo> = new Map();
  
  constructor(config: TokenConfig) {
    this.signingKey = config.signingKey;
    this.encryptionKey = config.encryptionKey;
    
    // Start token cleanup process
    this.startTokenCleanup();
  }
  
  async generateTokenPair(
    payload: TokenPayload,
    options: TokenOptions = {}
  ): Promise<TokenPair> {
    // Generate access token
    const accessToken = await this.generateAccessToken(payload, {
      expiresIn: options.accessTokenTTL || '15m',
      audience: options.audience,
      issuer: options.issuer
    });
    
    // Generate refresh token
    const refreshToken = await this.generateRefreshToken(payload, {
      expiresIn: options.refreshTokenTTL || '7d',
      audience: options.audience,
      issuer: options.issuer
    });
    
    // Store refresh token info
    this.refreshTokens.set(refreshToken.jti, {
      userId: payload.sub,
      deviceId: payload.deviceId,
      issuedAt: new Date(),
      expiresAt: new Date(Date.now() + (7 * 24 * 60 * 60 * 1000)), // 7 days
      lastUsed: new Date(),
      ipAddress: options.ipAddress,
      userAgent: options.userAgent
    });
    
    return {
      accessToken: accessToken.token,
      refreshToken: refreshToken.token,
      tokenType: 'Bearer',
      expiresIn: accessToken.expiresIn,
      scope: payload.scope
    };
  }
  
  async validateToken(token: string, options: ValidationOptions = {}): Promise<TokenValidationResult> {
    try {
      // Check if token is blacklisted
      if (this.tokenBlacklist.has(token)) {
        return {
          valid: false,
          reason: 'Token is blacklisted',
          code: 'TOKEN_BLACKLISTED'
        };
      }
      
      // Decrypt and verify token
      const decryptedToken = await this.decryptToken(token);
      const payload = await this.verifyToken(decryptedToken, {
        audience: options.audience,
        issuer: options.issuer,
        clockTolerance: options.clockTolerance || 30
      });
      
      // Additional security checks
      const securityChecks = await this.performSecurityChecks(payload, options);
      if (!securityChecks.passed) {
        return {
          valid: false,
          reason: securityChecks.reason,
          code: securityChecks.code
        };
      }
      
      return {
        valid: true,
        payload,
        claims: payload
      };
      
    } catch (error) {
      return {
        valid: false,
        reason: error.message,
        code: 'TOKEN_VALIDATION_ERROR'
      };
    }
  }
  
  async refreshAccessToken(
    refreshToken: string,
    options: RefreshOptions = {}
  ): Promise<TokenRefreshResult> {
    try {
      // Validate refresh token
      const validation = await this.validateToken(refreshToken, {
        tokenType: 'refresh'
      });
      
      if (!validation.valid) {
        return {
          success: false,
          reason: validation.reason,
          code: validation.code
        };
      }
      
      const payload = validation.payload!;
      
      // Check refresh token info
      const refreshInfo = this.refreshTokens.get(payload.jti);
      if (!refreshInfo) {
        return {
          success: false,
          reason: 'Refresh token not found',
          code: 'REFRESH_TOKEN_NOT_FOUND'
        };
      }
      
      // Check if refresh token is still valid
      if (refreshInfo.expiresAt < new Date()) {
        this.refreshTokens.delete(payload.jti);
        return {
          success: false,
          reason: 'Refresh token expired',
          code: 'REFRESH_TOKEN_EXPIRED'
        };
      }
      
      // Generate new token pair
      const newTokenPair = await this.generateTokenPair({
        sub: payload.sub,
        scope: payload.scope,
        deviceId: payload.deviceId
      }, {
        audience: options.audience,
        issuer: options.issuer,
        ipAddress: options.ipAddress,
        userAgent: options.userAgent
      });
      
      // Invalidate old refresh token
      this.refreshTokens.delete(payload.jti);
      
      // Blacklist old access token if provided
      if (options.oldAccessToken) {
        this.tokenBlacklist.add(options.oldAccessToken);
      }
      
      return {
        success: true,
        tokenPair: newTokenPair
      };
      
    } catch (error) {
      return {
        success: false,
        reason: error.message,
        code: 'TOKEN_REFRESH_ERROR'
      };
    }
  }
  
  async revokeToken(token: string, type: 'access' | 'refresh' = 'access'): Promise<void> {
    if (type === 'access') {
      this.tokenBlacklist.add(token);
    } else {
      const validation = await this.validateToken(token, { tokenType: 'refresh' });
      if (validation.valid && validation.payload) {
        this.refreshTokens.delete(validation.payload.jti);
      }
    }
  }
  
  private async performSecurityChecks(
    payload: any,
    options: ValidationOptions
  ): Promise<SecurityCheckResult> {
    // Check for token replay attacks
    if (options.nonce && payload.nonce !== options.nonce) {
      return {
        passed: false,
        reason: 'Invalid nonce',
        code: 'INVALID_NONCE'
      };
    }
    
    // Check IP address binding
    if (options.bindToIP && payload.ipAddress && payload.ipAddress !== options.clientIP) {
      return {
        passed: false,
        reason: 'IP address mismatch',
        code: 'IP_MISMATCH'
      };
    }
    
    // Check device binding
    if (options.bindToDevice && payload.deviceId && payload.deviceId !== options.deviceId) {
      return {
        passed: false,
        reason: 'Device ID mismatch',
        code: 'DEVICE_MISMATCH'
      };
    }
    
    return { passed: true };
  }
  
  private startTokenCleanup(): void {
    // Clean up expired tokens every hour
    setInterval(() => {
      this.cleanupExpiredTokens();
    }, 3600000);
  }
  
  private cleanupExpiredTokens(): void {
    const now = new Date();
    
    // Clean up expired refresh tokens
    for (const [jti, info] of this.refreshTokens) {
      if (info.expiresAt < now) {
        this.refreshTokens.delete(jti);
      }
    }
    
    // Note: Access token blacklist cleanup would require
    // storing expiration times, which is omitted for brevity
  }
}
```

---

## 🔒 Data Protection Patterns

### Encryption and Hashing System
```typescript
enum EncryptionAlgorithm {
  AES_256_GCM = 'aes-256-gcm',
  AES_256_CBC = 'aes-256-cbc',
  CHACHA20_POLY1305 = 'chacha20-poly1305'
}

enum HashingAlgorithm {
  ARGON2ID = 'argon2id',
  BCRYPT = 'bcrypt',
  SCRYPT = 'scrypt',
  PBKDF2 = 'pbkdf2'
}

class CryptographyManager {
  private encryptionKeys: Map<string, CryptoKey> = new Map();
  private keyRotationSchedule: Map<string, KeyRotationInfo> = new Map();
  
  constructor(private config: CryptographyConfig) {
    this.initializeKeys();
    this.startKeyRotation();
  }
  
  async encryptData(
    data: string | Buffer,
    keyId: string,
    algorithm: EncryptionAlgorithm = EncryptionAlgorithm.AES_256_GCM
  ): Promise<EncryptionResult> {
    try {
      const key = this.encryptionKeys.get(keyId);
      if (!key) {
        throw new Error(`Encryption key '${keyId}' not found`);
      }
      
      const iv = crypto.randomBytes(16);
      const cipher = crypto.createCipher(algorithm, key);
      cipher.setAAD(Buffer.from(keyId)); // Additional authenticated data
      
      let encrypted = cipher.update(data, 'utf8', 'hex');
      encrypted += cipher.final('hex');
      
      const authTag = cipher.getAuthTag();
      
      return {
        encryptedData: encrypted,
        iv: iv.toString('hex'),
        authTag: authTag.toString('hex'),
        algorithm,
        keyId,
        timestamp: new Date().toISOString()
      };
      
    } catch (error) {
      throw new SecurityError('Encryption failed', {
        code: 'ENCRYPTION_ERROR',
        keyId,
        algorithm,
        originalError: error
      });
    }
  }
  
  async decryptData(
    encryptionResult: EncryptionResult
  ): Promise<string> {
    try {
      const key = this.encryptionKeys.get(encryptionResult.keyId);
      if (!key) {
        throw new Error(`Decryption key '${encryptionResult.keyId}' not found`);
      }
      
      const decipher = crypto.createDecipher(encryptionResult.algorithm, key);
      decipher.setAAD(Buffer.from(encryptionResult.keyId));
      decipher.setAuthTag(Buffer.from(encryptionResult.authTag, 'hex'));
      
      let decrypted = decipher.update(encryptionResult.encryptedData, 'hex', 'utf8');
      decrypted += decipher.final('utf8');
      
      return decrypted;
      
    } catch (error) {
      throw new SecurityError('Decryption failed', {
        code: 'DECRYPTION_ERROR',
        keyId: encryptionResult.keyId,
        algorithm: encryptionResult.algorithm,
        originalError: error
      });
    }
  }
  
  async hashPassword(
    password: string,
    algorithm: HashingAlgorithm = HashingAlgorithm.ARGON2ID
  ): Promise<PasswordHash> {
    try {
      let hash: string;
      let salt: string;
      let params: any;
      
      switch (algorithm) {
        case HashingAlgorithm.ARGON2ID:
          salt = crypto.randomBytes(32).toString('hex');
          hash = await argon2.hash(password, {
            type: argon2.argon2id,
            memoryCost: 2 ** 16, // 64 MB
            timeCost: 3,
            parallelism: 1,
            salt: Buffer.from(salt, 'hex')
          });
          params = { memoryCost: 2 ** 16, timeCost: 3, parallelism: 1 };
          break;
          
        case HashingAlgorithm.BCRYPT:
          const saltRounds = 12;
          salt = await bcrypt.genSalt(saltRounds);
          hash = await bcrypt.hash(password, salt);
          params = { saltRounds };
          break;
          
        case HashingAlgorithm.SCRYPT:
          salt = crypto.randomBytes(32).toString('hex');
          hash = await new Promise((resolve, reject) => {
            crypto.scrypt(password, salt, 64, { N: 2**14, r: 8, p: 1 }, (err, derivedKey) => {
              if (err) reject(err);
              else resolve(derivedKey.toString('hex'));
            });
          }) as string;
          params = { N: 2**14, r: 8, p: 1 };
          break;
          
        default:
          throw new Error(`Unsupported hashing algorithm: ${algorithm}`);
      }
      
      return {
        hash,
        salt,
        algorithm,
        params,
        timestamp: new Date().toISOString()
      };
      
    } catch (error) {
      throw new SecurityError('Password hashing failed', {
        code: 'HASHING_ERROR',
        algorithm,
        originalError: error
      });
    }
  }
  
  async verifyPassword(
    password: string,
    passwordHash: PasswordHash
  ): Promise<boolean> {
    try {
      switch (passwordHash.algorithm) {
        case HashingAlgorithm.ARGON2ID:
          return await argon2.verify(passwordHash.hash, password);
          
        case HashingAlgorithm.BCRYPT:
          return await bcrypt.compare(password, passwordHash.hash);
          
        case HashingAlgorithm.SCRYPT:
          const derivedKey = await new Promise((resolve, reject) => {
            crypto.scrypt(
              password,
              passwordHash.salt,
              64,
              passwordHash.params,
              (err, key) => {
                if (err) reject(err);
                else resolve(key.toString('hex'));
              }
            );
          }) as string;
          return derivedKey === passwordHash.hash;
          
        default:
          throw new Error(`Unsupported hashing algorithm: ${passwordHash.algorithm}`);
      }
    } catch (error) {
      throw new SecurityError('Password verification failed', {
        code: 'VERIFICATION_ERROR',
        algorithm: passwordHash.algorithm,
        originalError: error
      });
    }
  }
  
  private async initializeKeys(): Promise<void> {
    // Initialize encryption keys from secure key management system
    for (const keyConfig of this.config.keys) {
      const key = await this.loadOrGenerateKey(keyConfig);
      this.encryptionKeys.set(keyConfig.id, key);
      
      // Set up key rotation schedule
      this.keyRotationSchedule.set(keyConfig.id, {
        keyId: keyConfig.id,
        rotationInterval: keyConfig.rotationInterval,
        lastRotation: new Date(),
        nextRotation: new Date(Date.now() + keyConfig.rotationInterval)
      });
    }
  }
  
  private startKeyRotation(): void {
    // Check for key rotation every hour
    setInterval(() => {
      this.performKeyRotation();
    }, 3600000);
  }
  
  private async performKeyRotation(): Promise<void> {
    const now = new Date();
    
    for (const [keyId, rotationInfo] of this.keyRotationSchedule) {
      if (now >= rotationInfo.nextRotation) {
        await this.rotateKey(keyId);
        
        // Update rotation schedule
        rotationInfo.lastRotation = now;
        rotationInfo.nextRotation = new Date(now.getTime() + rotationInfo.rotationInterval);
      }
    }
  }
  
  private async rotateKey(keyId: string): Promise<void> {
    // Generate new key
    const newKey = await this.generateKey();
    
    // Store old key for decryption of existing data
    const oldKey = this.encryptionKeys.get(keyId);
    if (oldKey) {
      this.encryptionKeys.set(`${keyId}_old_${Date.now()}`, oldKey);
    }
    
    // Replace with new key
    this.encryptionKeys.set(keyId, newKey);
    
    console.log(`Key rotation completed for key: ${keyId}`);
  }
}

// Data Loss Prevention (DLP) System
class DataLossPreventionManager {
  private sensitiveDataPatterns: Map<string, RegExp> = new Map();
  private classificationRules: DataClassificationRule[] = [];
  private auditLogger: SecurityAuditLogger;
  
  constructor() {
    this.auditLogger = new SecurityAuditLogger();
    this.initializeSensitiveDataPatterns();
    this.initializeClassificationRules();
  }
  
  async scanData(data: any, context: ScanContext): Promise<DLPScanResult> {
    const findings: DLPFinding[] = [];
    const dataString = typeof data === 'string' ? data : JSON.stringify(data);
    
    // Scan for sensitive data patterns
    for (const [type, pattern] of this.sensitiveDataPatterns) {
      const matches = dataString.match(pattern);
      if (matches) {
        findings.push({
          type: 'sensitive_data',
          category: type,
          matches: matches.length,
          confidence: this.calculateConfidence(type, matches),
          severity: this.getSeverity(type),
          locations: this.findMatchLocations(dataString, pattern)
        });
      }
    }
    
    // Apply classification rules
    const classification = await this.classifyData(data, context);
    
    // Determine if data should be blocked
    const shouldBlock = this.shouldBlockData(findings, classification, context);
    
    const result: DLPScanResult = {
      findings,
      classification,
      shouldBlock,
      riskScore: this.calculateRiskScore(findings, classification),
      recommendations: this.generateRecommendations(findings, classification),
      timestamp: new Date().toISOString()
    };
    
    // Log scan result
    await this.auditLogger.logDLPScan({
      context,
      result,
      timestamp: result.timestamp
    });
    
    return result;
  }
  
  private initializeSensitiveDataPatterns(): void {
    // Credit card numbers
    this.sensitiveDataPatterns.set('credit_card', /\b(?:\d{4}[\s-]?){3}\d{4}\b/g);
    
    // Social Security Numbers
    this.sensitiveDataPatterns.set('ssn', /\b\d{3}-\d{2}-\d{4}\b/g);
    
    // Email addresses
    this.sensitiveDataPatterns.set('email', /\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b/g);
    
    // Phone numbers
    this.sensitiveDataPatterns.set('phone', /\b(?:\+?1[-.]?)?\(?\d{3}\)?[-.]?\d{3}[-.]?\d{4}\b/g);
    
    // IP addresses
    this.sensitiveDataPatterns.set('ip_address', /\b(?:\d{1,3}\.){3}\d{1,3}\b/g);
    
    // API keys (generic pattern)
    this.sensitiveDataPatterns.set('api_key', /\b[A-Za-z0-9]{32,}\b/g);
    
    // Passwords (common patterns)
    this.sensitiveDataPatterns.set('password', /(?:password|pwd|pass)\s*[:=]\s*["']?([^\s"']+)/gi);
  }
  
  private initializeClassificationRules(): void {
    this.classificationRules = [
      {
        name: 'financial_data',
        patterns: ['credit_card', 'bank_account', 'routing_number'],
        classification: 'HIGHLY_SENSITIVE',
        weight: 1.0
      },
      {
        name: 'personal_data',
        patterns: ['ssn', 'passport', 'drivers_license'],
        classification: 'SENSITIVE',
        weight: 0.8
      },
      {
        name: 'contact_data',
        patterns: ['email', 'phone', 'address'],
        classification: 'INTERNAL',
        weight: 0.6
      },
      {
        name: 'technical_data',
        patterns: ['api_key', 'password', 'token'],
        classification: 'CONFIDENTIAL',
        weight: 0.9
      }
    ];
  }
}
```

---

## 🛡️ Input Validation & Sanitization

### Comprehensive Input Validation System
```typescript
enum ValidationType {
  STRING = 'string',
  NUMBER = 'number',
  EMAIL = 'email',
  URL = 'url',
  JSON = 'json',
  SQL = 'sql',
  HTML = 'html',
  JAVASCRIPT = 'javascript'
}

enum SanitizationType {
  HTML_ESCAPE = 'html_escape',
  SQL_ESCAPE = 'sql_escape',
  JS_ESCAPE = 'js_escape',
  URL_ENCODE = 'url_encode',
  STRIP_TAGS = 'strip_tags',
  WHITELIST = 'whitelist'
}

class InputValidationManager {
  private validators: Map<ValidationType, InputValidator> = new Map();
  private sanitizers: Map<SanitizationType, InputSanitizer> = new Map();
  private auditLogger: SecurityAuditLogger;
  
  constructor() {
    this.auditLogger = new SecurityAuditLogger();
    this.initializeValidators();
    this.initializeSanitizers();
  }
  
  async validateAndSanitize(
    input: any,
    rules: ValidationRule[],
    context: ValidationContext
  ): Promise<ValidationResult> {
    const results: FieldValidationResult[] = [];
    const sanitizedData: any = {};
    let overallValid = true;
    
    for (const rule of rules) {
      const fieldValue = this.getFieldValue(input, rule.field);
      
      // Validate field
      const validationResult = await this.validateField(
        fieldValue,
        rule,
        context
      );
      
      results.push(validationResult);
      
      if (!validationResult.valid) {
        overallValid = false;
      }
      
      // Sanitize field if validation passed
      if (validationResult.valid && rule.sanitization) {
        sanitizedData[rule.field] = await this.sanitizeField(
          fieldValue,
          rule.sanitization,
          context
        );
      } else if (validationResult.valid) {
        sanitizedData[rule.field] = fieldValue;
      }
    }
    
    const result: ValidationResult = {
      valid: overallValid,
      results,
      sanitizedData: overallValid ? sanitizedData : null,
      errors: results.filter(r => !r.valid).map(r => r.error!),
      timestamp: new Date().toISOString()
    };
    
    // Log validation attempt
    await this.auditLogger.logValidationAttempt({
      context,
      result,
      timestamp: result.timestamp
    });
    
    return result;
  }
  
  private async validateField(
    value: any,
    rule: ValidationRule,
    context: ValidationContext
  ): Promise<FieldValidationResult> {
    try {
      // Check required fields
      if (rule.required && (value === undefined || value === null || value === '')) {
        return {
          field: rule.field,
          valid: false,
          error: {
            code: 'REQUIRED_FIELD',
            message: `Field '${rule.field}' is required`,
            field: rule.field
          }
        };
      }
      
      // Skip validation for optional empty fields
      if (!rule.required && (value === undefined || value === null || value === '')) {
        return {
          field: rule.field,
          valid: true
        };
      }
      
      // Type validation
      for (const validationType of rule.types) {
        const validator = this.validators.get(validationType);
        if (!validator) {
          throw new Error(`Validator for type '${validationType}' not found`);
        }
        
        const typeResult = await validator.validate(value, rule.options);
        if (!typeResult.valid) {
          return {
            field: rule.field,
            valid: false,
            error: {
              code: 'TYPE_VALIDATION_FAILED',
              message: typeResult.error || `Invalid ${validationType}`,
              field: rule.field,
              validationType
            }
          };
        }
      }
      
      // Custom validation
      if (rule.customValidator) {
        const customResult = await rule.customValidator(value, context);
        if (!customResult.valid) {
          return {
            field: rule.field,
            valid: false,
            error: {
              code: 'CUSTOM_VALIDATION_FAILED',
              message: customResult.error || 'Custom validation failed',
              field: rule.field
            }
          };
        }
      }
      
      // Security validation (XSS, SQL injection, etc.)
      const securityResult = await this.performSecurityValidation(value, rule, context);
      if (!securityResult.valid) {
        return {
          field: rule.field,
          valid: false,
          error: {
            code: 'SECURITY_VALIDATION_FAILED',
            message: securityResult.error || 'Security validation failed',
            field: rule.field,
            securityThreat: securityResult.threat
          }
        };
      }
      
      return {
        field: rule.field,
        valid: true
      };
      
    } catch (error) {
      return {
        field: rule.field,
        valid: false,
        error: {
          code: 'VALIDATION_ERROR',
          message: error.message,
          field: rule.field
        }
      };
    }
  }
  
  private async performSecurityValidation(
    value: any,
    rule: ValidationRule,
    context: ValidationContext
  ): Promise<SecurityValidationResult> {
    const valueString = String(value);
    
    // XSS detection
    if (this.detectXSS(valueString)) {
      return {
        valid: false,
        error: 'Potential XSS attack detected',
        threat: 'XSS'
      };
    }
    
    // SQL injection detection
    if (this.detectSQLInjection(valueString)) {
      return {
        valid: false,
        error: 'Potential SQL injection detected',
        threat: 'SQL_INJECTION'
      };
    }
    
    // Command injection detection
    if (this.detectCommandInjection(valueString)) {
      return {
        valid: false,
        error: 'Potential command injection detected',
        threat: 'COMMAND_INJECTION'
      };
    }
    
    // Path traversal detection
    if (this.detectPathTraversal(valueString)) {
      return {
        valid: false,
        error: 'Potential path traversal detected',
        threat: 'PATH_TRAVERSAL'
      };
    }
    
    return { valid: true };
  }
  
  private detectXSS(input: string): boolean {
    const xssPatterns = [
      /<script[^>]*>.*?<\/script>/gi,
      /javascript:/gi,
      /on\w+\s*=/gi,
      /<iframe[^>]*>.*?<\/iframe>/gi,
      /<object[^>]*>.*?<\/object>/gi,
      /<embed[^>]*>/gi,
      /expression\s*\(/gi
    ];
    
    return xssPatterns.some(pattern => pattern.test(input));
  }
  
  private detectSQLInjection(input: string): boolean {
    const sqlPatterns = [
      /('|(\-\-)|(;)|(\||\|)|(\*|\*))/gi,
      /(union|select|insert|delete|update|drop|create|alter|exec|execute)/gi,
      /\b(or|and)\s+\w+\s*=\s*\w+/gi,
      /\b\d+\s*=\s*\d+/gi,
      /'\s*(or|and)\s*'\w+'/gi
    ];
    
    return sqlPatterns.some(pattern => pattern.test(input));
  }
  
  private detectCommandInjection(input: string): boolean {
    const commandPatterns = [
      /[;&|`$(){}\[\]]/g,
      /\b(cat|ls|pwd|whoami|id|uname|ps|netstat|ifconfig|ping|wget|curl|nc|telnet|ssh|ftp)\b/gi,
      /\.\.\//g,
      /\\x[0-9a-f]{2}/gi
    ];
    
    return commandPatterns.some(pattern => pattern.test(input));
  }
  
  private detectPathTraversal(input: string): boolean {
    const pathPatterns = [
      /\.\.[\/\\]/g,
      /[\/\\]\.\.[\/\\]/g,
      /%2e%2e%2f/gi,
      /%2e%2e%5c/gi,
      /\.\.%2f/gi,
      /\.\.%5c/gi
    ];
    
    return pathPatterns.some(pattern => pattern.test(input));
  }
  
  private initializeValidators(): void {
    // String validator
    this.validators.set(ValidationType.STRING, {
      validate: async (value: any, options: any = {}) => {
        if (typeof value !== 'string') {
          return { valid: false, error: 'Value must be a string' };
        }
        
        if (options.minLength && value.length < options.minLength) {
          return { valid: false, error: `String must be at least ${options.minLength} characters` };
        }
        
        if (options.maxLength && value.length > options.maxLength) {
          return { valid: false, error: `String must be at most ${options.maxLength} characters` };
        }
        
        if (options.pattern && !options.pattern.test(value)) {
          return { valid: false, error: 'String does not match required pattern' };
        }
        
        return { valid: true };
      }
    });
    
    // Email validator
    this.validators.set(ValidationType.EMAIL, {
      validate: async (value: any) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
          return { valid: false, error: 'Invalid email format' };
        }
        return { valid: true };
      }
    });
    
    // URL validator
    this.validators.set(ValidationType.URL, {
      validate: async (value: any) => {
        try {
          new URL(value);
          return { valid: true };
        } catch {
          return { valid: false, error: 'Invalid URL format' };
        }
      }
    });
    
    // Add more validators as needed...
  }
  
  private initializeSanitizers(): void {
    // HTML escape sanitizer
    this.sanitizers.set(SanitizationType.HTML_ESCAPE, {
      sanitize: async (value: string) => {
        return value
          .replace(/&/g, '&amp;')
          .replace(/</g, '&lt;')
          .replace(/>/g, '&gt;')
          .replace(/"/g, '&quot;')
          .replace(/'/g, '&#x27;')
          .replace(/\//g, '&#x2F;');
      }
    });
    
    // SQL escape sanitizer
    this.sanitizers.set(SanitizationType.SQL_ESCAPE, {
      sanitize: async (value: string) => {
        return value.replace(/'/g, "''");
      }
    });
    
    // Strip tags sanitizer
    this.sanitizers.set(SanitizationType.STRIP_TAGS, {
      sanitize: async (value: string) => {
        return value.replace(/<[^>]*>/g, '');
      }
    });
    
    // Add more sanitizers as needed...
  }
}
```

---

## Integration with .god Ecosystem

### Security Sub-Agent Integration
- **Security Auditor**: Automated security scanning and vulnerability assessment
- **Bug Hunter**: Security-focused bug detection and analysis
- **Test Executor**: Security testing and penetration testing
- **Performance Analyzer**: Security performance impact assessment
- **Context Optimizer**: Security context optimization

### Security Workflow Integration
- **TSDDR 2.0**: Security-driven test design and validation
- **Kiro Workflow**: Security checkpoints in task execution
- **Agent Coordination**: Security-aware task distribution

### Security Monitoring Dashboard
```typescript
class SecurityDashboard {
  private securityMetrics: SecurityMetrics;
  private threats: SecurityThreat[];
  private vulnerabilities: SecurityVulnerability[];
  
  generateSecurityReport(): SecurityReport {
    return {
      summary: this.generateSecuritySummary(),
      metrics: this.securityMetrics,
      threats: this.threats.filter(threat => threat.active),
      vulnerabilities: this.vulnerabilities.filter(vuln => !vuln.resolved),
      recommendations: this.generateSecurityRecommendations(),
      compliance: this.assessCompliance()
    };
  }
  
  private generateSecuritySummary(): SecuritySummary {
    return {
      overallSecurityScore: this.calculateSecurityScore(),
      authenticationGrade: this.gradeAuthentication(),
      authorizationGrade: this.gradeAuthorization(),
      dataProtectionGrade: this.gradeDataProtection(),
      inputValidationGrade: this.gradeInputValidation(),
      criticalVulnerabilities: this.countCriticalVulnerabilities()
    };
  }
}
```

---

**Usage**: All system components requiring security  
**Enforcement**: Automated security testing in CI/CD  
**Monitoring**: Real-time security monitoring and alerting  
**Evolution**: Continuous security improvement based on threat intelligence and security assessments