# Validation Rules & Input/Output Standards

> **🛡️ Comprehensive Validation Framework**  
> Standardized validation patterns for secure, reliable, and consistent data handling

## Validation Philosophy

**Mission**: Ensure data integrity, security, and consistency across all system boundaries  
**Approach**: Defense in depth with multiple validation layers  
**Principle**: Validate early, validate often, fail fast with clear messages

---

## 🔍 Input Validation Framework

### Validation Layers

**Layer 1: Syntax Validation**
- Data type checking
- Format validation
- Structure verification
- Encoding validation

**Layer 2: Semantic Validation**
- Business rule compliance
- Logical consistency
- Relationship validation
- Context appropriateness

**Layer 3: Security Validation**
- Injection prevention
- XSS protection
- CSRF validation
- Authorization checks

**Layer 4: Performance Validation**
- Size limits
- Rate limiting
- Resource constraints
- Timeout validation

### Universal Validation Patterns

#### String Validation
```typescript
interface StringValidationRules {
    minLength?: number;
    maxLength?: number;
    pattern?: RegExp;
    allowEmpty?: boolean;
    trimWhitespace?: boolean;
    sanitize?: boolean;
}

class StringValidator {
    static validate(value: any, rules: StringValidationRules): ValidationResult {
        const errors: string[] = [];
        
        // Type check
        if (typeof value !== 'string') {
            return { isValid: false, errors: ['Value must be a string'] };
        }
        
        let processedValue = rules.trimWhitespace ? value.trim() : value;
        
        // Empty check
        if (!rules.allowEmpty && processedValue.length === 0) {
            errors.push('Value cannot be empty');
        }
        
        // Length validation
        if (rules.minLength && processedValue.length < rules.minLength) {
            errors.push(`Value must be at least ${rules.minLength} characters long`);
        }
        
        if (rules.maxLength && processedValue.length > rules.maxLength) {
            errors.push(`Value must not exceed ${rules.maxLength} characters`);
        }
        
        // Pattern validation
        if (rules.pattern && !rules.pattern.test(processedValue)) {
            errors.push('Value does not match required pattern');
        }
        
        // Sanitization
        if (rules.sanitize) {
            processedValue = this.sanitizeString(processedValue);
        }
        
        return {
            isValid: errors.length === 0,
            errors,
            sanitizedValue: processedValue
        };
    }
    
    private static sanitizeString(value: string): string {
        return value
            .replace(/[<>"'&]/g, (match) => {
                const entities: { [key: string]: string } = {
                    '<': '&lt;',
                    '>': '&gt;',
                    '"': '&quot;',
                    "'": '&#x27;',
                    '&': '&amp;'
                };
                return entities[match];
            })
            .replace(/\s+/g, ' ')
            .trim();
    }
}

// Usage examples
const nameValidation = StringValidator.validate(userInput.name, {
    minLength: 2,
    maxLength: 50,
    pattern: /^[a-zA-Z\s]+$/,
    allowEmpty: false,
    trimWhitespace: true,
    sanitize: true
});

const emailValidation = StringValidator.validate(userInput.email, {
    maxLength: 254,
    pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
    allowEmpty: false,
    trimWhitespace: true,
    sanitize: false
});
```

#### Number Validation
```typescript
interface NumberValidationRules {
    min?: number;
    max?: number;
    integer?: boolean;
    positive?: boolean;
    allowZero?: boolean;
    precision?: number;
}

class NumberValidator {
    static validate(value: any, rules: NumberValidationRules): ValidationResult {
        const errors: string[] = [];
        
        // Type conversion and validation
        const numValue = Number(value);
        if (isNaN(numValue) || !isFinite(numValue)) {
            return { isValid: false, errors: ['Value must be a valid number'] };
        }
        
        // Integer check
        if (rules.integer && !Number.isInteger(numValue)) {
            errors.push('Value must be an integer');
        }
        
        // Range validation
        if (rules.min !== undefined && numValue < rules.min) {
            errors.push(`Value must be at least ${rules.min}`);
        }
        
        if (rules.max !== undefined && numValue > rules.max) {
            errors.push(`Value must not exceed ${rules.max}`);
        }
        
        // Positive validation
        if (rules.positive && numValue <= 0) {
            errors.push('Value must be positive');
        }
        
        // Zero validation
        if (!rules.allowZero && numValue === 0) {
            errors.push('Value cannot be zero');
        }
        
        // Precision validation
        if (rules.precision !== undefined) {
            const decimalPlaces = (numValue.toString().split('.')[1] || '').length;
            if (decimalPlaces > rules.precision) {
                errors.push(`Value cannot have more than ${rules.precision} decimal places`);
            }
        }
        
        return {
            isValid: errors.length === 0,
            errors,
            sanitizedValue: numValue
        };
    }
}

// Usage examples
const ageValidation = NumberValidator.validate(userInput.age, {
    min: 0,
    max: 150,
    integer: true,
    positive: false,
    allowZero: true
});

const priceValidation = NumberValidator.validate(userInput.price, {
    min: 0.01,
    positive: true,
    precision: 2
});
```

#### Array Validation
```typescript
interface ArrayValidationRules<T> {
    minLength?: number;
    maxLength?: number;
    uniqueItems?: boolean;
    itemValidator?: (item: T) => ValidationResult;
    allowEmpty?: boolean;
}

class ArrayValidator {
    static validate<T>(value: any, rules: ArrayValidationRules<T>): ValidationResult {
        const errors: string[] = [];
        
        // Type check
        if (!Array.isArray(value)) {
            return { isValid: false, errors: ['Value must be an array'] };
        }
        
        // Empty check
        if (!rules.allowEmpty && value.length === 0) {
            errors.push('Array cannot be empty');
        }
        
        // Length validation
        if (rules.minLength !== undefined && value.length < rules.minLength) {
            errors.push(`Array must have at least ${rules.minLength} items`);
        }
        
        if (rules.maxLength !== undefined && value.length > rules.maxLength) {
            errors.push(`Array must not have more than ${rules.maxLength} items`);
        }
        
        // Unique items validation
        if (rules.uniqueItems) {
            const uniqueItems = new Set(value.map(item => JSON.stringify(item)));
            if (uniqueItems.size !== value.length) {
                errors.push('Array items must be unique');
            }
        }
        
        // Item validation
        if (rules.itemValidator) {
            const itemErrors: string[] = [];
            value.forEach((item, index) => {
                const itemResult = rules.itemValidator!(item);
                if (!itemResult.isValid) {
                    itemErrors.push(`Item ${index}: ${itemResult.errors.join(', ')}`);
                }
            });
            errors.push(...itemErrors);
        }
        
        return {
            isValid: errors.length === 0,
            errors,
            sanitizedValue: value
        };
    }
}

// Usage example
const tagsValidation = ArrayValidator.validate(userInput.tags, {
    minLength: 1,
    maxLength: 10,
    uniqueItems: true,
    itemValidator: (tag: string) => StringValidator.validate(tag, {
        minLength: 2,
        maxLength: 20,
        pattern: /^[a-zA-Z0-9-_]+$/
    })
});
```

#### Object Validation
```typescript
interface ObjectValidationSchema {
    [key: string]: {
        required?: boolean;
        validator: (value: any) => ValidationResult;
        defaultValue?: any;
    };
}

class ObjectValidator {
    static validate(value: any, schema: ObjectValidationSchema): ValidationResult {
        const errors: string[] = [];
        const sanitizedObject: any = {};
        
        // Type check
        if (typeof value !== 'object' || value === null || Array.isArray(value)) {
            return { isValid: false, errors: ['Value must be an object'] };
        }
        
        // Validate each field in schema
        for (const [fieldName, fieldRules] of Object.entries(schema)) {
            const fieldValue = value[fieldName];
            
            // Required field check
            if (fieldRules.required && (fieldValue === undefined || fieldValue === null)) {
                errors.push(`Field '${fieldName}' is required`);
                continue;
            }
            
            // Skip validation if field is not present and not required
            if (fieldValue === undefined || fieldValue === null) {
                if (fieldRules.defaultValue !== undefined) {
                    sanitizedObject[fieldName] = fieldRules.defaultValue;
                }
                continue;
            }
            
            // Validate field
            const fieldResult = fieldRules.validator(fieldValue);
            if (!fieldResult.isValid) {
                errors.push(`Field '${fieldName}': ${fieldResult.errors.join(', ')}`);
            } else {
                sanitizedObject[fieldName] = fieldResult.sanitizedValue ?? fieldValue;
            }
        }
        
        // Check for unexpected fields
        const allowedFields = Object.keys(schema);
        const providedFields = Object.keys(value);
        const unexpectedFields = providedFields.filter(field => !allowedFields.includes(field));
        
        if (unexpectedFields.length > 0) {
            errors.push(`Unexpected fields: ${unexpectedFields.join(', ')}`);
        }
        
        return {
            isValid: errors.length === 0,
            errors,
            sanitizedValue: sanitizedObject
        };
    }
}

// Usage example
const userSchema: ObjectValidationSchema = {
    name: {
        required: true,
        validator: (value) => StringValidator.validate(value, {
            minLength: 2,
            maxLength: 50,
            pattern: /^[a-zA-Z\s]+$/
        })
    },
    email: {
        required: true,
        validator: (value) => StringValidator.validate(value, {
            pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        })
    },
    age: {
        required: false,
        validator: (value) => NumberValidator.validate(value, {
            min: 0,
            max: 150,
            integer: true
        }),
        defaultValue: null
    }
};

const userValidation = ObjectValidator.validate(userInput, userSchema);
```

---

## 🔒 Security Validation Patterns

### SQL Injection Prevention
```typescript
class SQLInjectionValidator {
    private static readonly DANGEROUS_PATTERNS = [
        /('|(\-\-)|(;)|(\||\|)|(\*|\*))/i,
        /(exec(\s|\+)+(s|x)p\w+)/i,
        /((select|insert|update|delete|drop|create|alter|exec|union|script)\s)/i
    ];
    
    static validate(value: string): ValidationResult {
        const errors: string[] = [];
        
        for (const pattern of this.DANGEROUS_PATTERNS) {
            if (pattern.test(value)) {
                errors.push('Input contains potentially dangerous SQL patterns');
                break;
            }
        }
        
        return {
            isValid: errors.length === 0,
            errors,
            sanitizedValue: value
        };
    }
    
    static sanitize(value: string): string {
        return value
            .replace(/'/g, "''")
            .replace(/\\/g, '\\\\')
            .replace(/;/g, '\\;')
            .replace(/--/g, '\\--');
    }
}
```

### XSS Prevention
```typescript
class XSSValidator {
    private static readonly XSS_PATTERNS = [
        /<script[^>]*>.*?<\/script>/gi,
        /<iframe[^>]*>.*?<\/iframe>/gi,
        /javascript:/gi,
        /on\w+\s*=/gi,
        /<\s*\w.*?>/gi
    ];
    
    static validate(value: string): ValidationResult {
        const errors: string[] = [];
        
        for (const pattern of this.XSS_PATTERNS) {
            if (pattern.test(value)) {
                errors.push('Input contains potentially dangerous HTML/JavaScript');
                break;
            }
        }
        
        return {
            isValid: errors.length === 0,
            errors,
            sanitizedValue: this.sanitize(value)
        };
    }
    
    static sanitize(value: string): string {
        return value
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#x27;')
            .replace(/\//g, '&#x2F;');
    }
}
```

### File Upload Validation
```typescript
interface FileValidationRules {
    allowedTypes: string[];
    maxSize: number; // in bytes
    allowedExtensions: string[];
    scanForMalware?: boolean;
}

class FileValidator {
    static validate(file: File, rules: FileValidationRules): ValidationResult {
        const errors: string[] = [];
        
        // File type validation
        if (!rules.allowedTypes.includes(file.type)) {
            errors.push(`File type '${file.type}' is not allowed`);
        }
        
        // File size validation
        if (file.size > rules.maxSize) {
            errors.push(`File size exceeds maximum allowed size of ${rules.maxSize} bytes`);
        }
        
        // File extension validation
        const extension = file.name.split('.').pop()?.toLowerCase();
        if (!extension || !rules.allowedExtensions.includes(extension)) {
            errors.push(`File extension '${extension}' is not allowed`);
        }
        
        // File name validation
        const fileNameValidation = StringValidator.validate(file.name, {
            maxLength: 255,
            pattern: /^[a-zA-Z0-9._-]+$/
        });
        
        if (!fileNameValidation.isValid) {
            errors.push('Invalid file name: ' + fileNameValidation.errors.join(', '));
        }
        
        return {
            isValid: errors.length === 0,
            errors,
            sanitizedValue: file
        };
    }
    
    static async scanFileContent(file: File): Promise<ValidationResult> {
        // Basic content validation
        const buffer = await file.arrayBuffer();
        const uint8Array = new Uint8Array(buffer);
        
        // Check for executable signatures
        const executableSignatures = [
            [0x4D, 0x5A], // PE executable
            [0x7F, 0x45, 0x4C, 0x46], // ELF executable
            [0xCA, 0xFE, 0xBA, 0xBE], // Mach-O executable
        ];
        
        for (const signature of executableSignatures) {
            if (this.hasSignature(uint8Array, signature)) {
                return {
                    isValid: false,
                    errors: ['File appears to be an executable'],
                    sanitizedValue: null
                };
            }
        }
        
        return {
            isValid: true,
            errors: [],
            sanitizedValue: file
        };
    }
    
    private static hasSignature(data: Uint8Array, signature: number[]): boolean {
        if (data.length < signature.length) return false;
        
        for (let i = 0; i < signature.length; i++) {
            if (data[i] !== signature[i]) return false;
        }
        
        return true;
    }
}
```

---

## 📤 Output Validation & Formatting

### Response Formatting Standards
```typescript
interface APIResponse<T> {
    success: boolean;
    data?: T;
    error?: {
        code: string;
        message: string;
        details?: any;
    };
    metadata?: {
        timestamp: string;
        requestId: string;
        version: string;
    };
}

class ResponseFormatter {
    static success<T>(data: T, requestId?: string): APIResponse<T> {
        return {
            success: true,
            data: this.sanitizeOutput(data),
            metadata: {
                timestamp: new Date().toISOString(),
                requestId: requestId || this.generateRequestId(),
                version: '1.0'
            }
        };
    }
    
    static error(code: string, message: string, details?: any, requestId?: string): APIResponse<null> {
        return {
            success: false,
            error: {
                code,
                message: this.sanitizeErrorMessage(message),
                details: details ? this.sanitizeOutput(details) : undefined
            },
            metadata: {
                timestamp: new Date().toISOString(),
                requestId: requestId || this.generateRequestId(),
                version: '1.0'
            }
        };
    }
    
    private static sanitizeOutput(data: any): any {
        if (typeof data === 'string') {
            return XSSValidator.sanitize(data);
        }
        
        if (Array.isArray(data)) {
            return data.map(item => this.sanitizeOutput(item));
        }
        
        if (typeof data === 'object' && data !== null) {
            const sanitized: any = {};
            for (const [key, value] of Object.entries(data)) {
                // Skip sensitive fields
                if (this.isSensitiveField(key)) {
                    continue;
                }
                sanitized[key] = this.sanitizeOutput(value);
            }
            return sanitized;
        }
        
        return data;
    }
    
    private static sanitizeErrorMessage(message: string): string {
        // Remove sensitive information from error messages
        return message
            .replace(/password[^\s]*/gi, '[REDACTED]')
            .replace(/token[^\s]*/gi, '[REDACTED]')
            .replace(/key[^\s]*/gi, '[REDACTED]')
            .replace(/secret[^\s]*/gi, '[REDACTED]');
    }
    
    private static isSensitiveField(fieldName: string): boolean {
        const sensitiveFields = [
            'password', 'token', 'secret', 'key', 'auth',
            'credential', 'private', 'confidential'
        ];
        
        return sensitiveFields.some(sensitive => 
            fieldName.toLowerCase().includes(sensitive)
        );
    }
    
    private static generateRequestId(): string {
        return `req_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
    }
}
```

### Data Serialization Standards
```typescript
class DataSerializer {
    static serialize(data: any): string {
        try {
            return JSON.stringify(data, this.replacer, 2);
        } catch (error) {
            throw new Error(`Serialization failed: ${error.message}`);
        }
    }
    
    static deserialize<T>(json: string): T {
        try {
            return JSON.parse(json, this.reviver);
        } catch (error) {
            throw new Error(`Deserialization failed: ${error.message}`);
        }
    }
    
    private static replacer(key: string, value: any): any {
        // Handle special types
        if (value instanceof Date) {
            return { __type: 'Date', value: value.toISOString() };
        }
        
        if (value instanceof RegExp) {
            return { __type: 'RegExp', value: value.toString() };
        }
        
        // Remove sensitive data
        if (typeof key === 'string' && key.toLowerCase().includes('password')) {
            return '[REDACTED]';
        }
        
        return value;
    }
    
    private static reviver(key: string, value: any): any {
        // Restore special types
        if (typeof value === 'object' && value !== null && value.__type) {
            switch (value.__type) {
                case 'Date':
                    return new Date(value.value);
                case 'RegExp':
                    const match = value.value.match(/^\/(.*)\/([gimuy]*)$/);
                    return match ? new RegExp(match[1], match[2]) : value.value;
            }
        }
        
        return value;
    }
}
```

---

## 🎯 Validation Result Interface

```typescript
interface ValidationResult {
    isValid: boolean;
    errors: string[];
    warnings?: string[];
    sanitizedValue?: any;
    metadata?: {
        validatedAt: string;
        validatorVersion: string;
        performanceMetrics?: {
            validationTime: number;
            rulesApplied: number;
        };
    };
}

class ValidationResultBuilder {
    private result: ValidationResult;
    
    constructor() {
        this.result = {
            isValid: true,
            errors: [],
            warnings: [],
            metadata: {
                validatedAt: new Date().toISOString(),
                validatorVersion: '1.0.0'
            }
        };
    }
    
    addError(error: string): this {
        this.result.errors.push(error);
        this.result.isValid = false;
        return this;
    }
    
    addWarning(warning: string): this {
        if (!this.result.warnings) this.result.warnings = [];
        this.result.warnings.push(warning);
        return this;
    }
    
    setSanitizedValue(value: any): this {
        this.result.sanitizedValue = value;
        return this;
    }
    
    setPerformanceMetrics(time: number, rulesCount: number): this {
        if (!this.result.metadata) this.result.metadata = { validatedAt: '', validatorVersion: '' };
        this.result.metadata.performanceMetrics = {
            validationTime: time,
            rulesApplied: rulesCount
        };
        return this;
    }
    
    build(): ValidationResult {
        return this.result;
    }
}
```

---

## 🚀 Performance Optimization

### Validation Caching
```typescript
class ValidationCache {
    private static cache = new Map<string, ValidationResult>();
    private static readonly MAX_CACHE_SIZE = 1000;
    private static readonly CACHE_TTL = 5 * 60 * 1000; // 5 minutes
    
    static getCachedResult(input: any, rules: any): ValidationResult | null {
        const key = this.generateCacheKey(input, rules);
        const cached = this.cache.get(key);
        
        if (cached && this.isCacheValid(cached)) {
            return cached;
        }
        
        return null;
    }
    
    static setCachedResult(input: any, rules: any, result: ValidationResult): void {
        if (this.cache.size >= this.MAX_CACHE_SIZE) {
            this.evictOldestEntries();
        }
        
        const key = this.generateCacheKey(input, rules);
        this.cache.set(key, {
            ...result,
            metadata: {
                ...result.metadata,
                cachedAt: new Date().toISOString()
            }
        });
    }
    
    private static generateCacheKey(input: any, rules: any): string {
        return btoa(JSON.stringify({ input, rules }));
    }
    
    private static isCacheValid(result: ValidationResult): boolean {
        const cachedAt = result.metadata?.cachedAt;
        if (!cachedAt) return false;
        
        const cacheTime = new Date(cachedAt).getTime();
        return Date.now() - cacheTime < this.CACHE_TTL;
    }
    
    private static evictOldestEntries(): void {
        const entries = Array.from(this.cache.entries());
        entries.sort((a, b) => {
            const timeA = new Date(a[1].metadata?.cachedAt || 0).getTime();
            const timeB = new Date(b[1].metadata?.cachedAt || 0).getTime();
            return timeA - timeB;
        });
        
        // Remove oldest 20% of entries
        const toRemove = Math.floor(entries.length * 0.2);
        for (let i = 0; i < toRemove; i++) {
            this.cache.delete(entries[i][0]);
        }
    }
}
```

---

## 📊 Validation Metrics & Monitoring

```typescript
class ValidationMetrics {
    private static metrics = {
        totalValidations: 0,
        successfulValidations: 0,
        failedValidations: 0,
        averageValidationTime: 0,
        commonErrors: new Map<string, number>()
    };
    
    static recordValidation(result: ValidationResult, duration: number): void {
        this.metrics.totalValidations++;
        
        if (result.isValid) {
            this.metrics.successfulValidations++;
        } else {
            this.metrics.failedValidations++;
            
            // Track common errors
            result.errors.forEach(error => {
                const count = this.metrics.commonErrors.get(error) || 0;
                this.metrics.commonErrors.set(error, count + 1);
            });
        }
        
        // Update average validation time
        this.metrics.averageValidationTime = 
            (this.metrics.averageValidationTime * (this.metrics.totalValidations - 1) + duration) / 
            this.metrics.totalValidations;
    }
    
    static getMetrics() {
        return {
            ...this.metrics,
            successRate: this.metrics.successfulValidations / this.metrics.totalValidations,
            failureRate: this.metrics.failedValidations / this.metrics.totalValidations,
            topErrors: Array.from(this.metrics.commonErrors.entries())
                .sort((a, b) => b[1] - a[1])
                .slice(0, 10)
        };
    }
}
```

---

## Integration with .god Ecosystem

### Sub-Agent Integration
- **Security Auditor**: Security validation pattern compliance
- **Bug Hunter**: Validation failure pattern analysis
- **Test Executor**: Validation test generation and execution
- **Performance Analyzer**: Validation performance optimization

### Workflow Integration
- **TSDDR 2.0**: Validation-driven test design
- **Kiro Workflow**: Validation checkpoints in task execution
- **Code Review**: Validation pattern adherence verification

---

**Usage**: All input/output boundaries and data transformations  
**Enforcement**: Automated validation in CI/CD pipeline  
**Monitoring**: Real-time validation metrics and alerting  
**Evolution**: Continuous pattern refinement based on security threats and performance data