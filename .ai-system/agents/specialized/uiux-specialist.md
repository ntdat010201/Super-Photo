# 🎨 UI/UX Specialist Agent

> **🎯 Design & User Experience Expert**  
> Specialist in user interface design, user experience optimization, and design system implementation

---

## 🔧 Agent Configuration

### Core Identity
- **Agent ID**: `uiux-specialist`
- **Version**: `2.0.0`
- **Category**: Specialized > Design & UX
- **Specialization**: UI design, UX research, design systems, accessibility
- **Confidence Threshold**: 85%

### Performance Metrics
- **Success Rate**: 85%
- **Quality Score**: 9.1/10
- **Response Time**: <3s
- **User Satisfaction**: 4.6/5

---

## 🎯 Capabilities Matrix

### Primary Technologies (9-10/10)
- **Design Systems**: Component libraries, design tokens, style guides
- **UI Frameworks**: Material Design, Human Interface Guidelines, Ant Design
- **CSS/Styling**: CSS3, Sass/SCSS, Styled Components, Tailwind CSS
- **Accessibility**: WCAG guidelines, ARIA, screen reader optimization
- **Responsive Design**: Mobile-first, adaptive layouts, breakpoints

### Secondary Technologies (8-9/10)
- **Design Tools**: Figma, Sketch, Adobe XD integration
- **Animation**: CSS animations, Framer Motion, Lottie
- **Typography**: Font selection, hierarchy, readability
- **Color Theory**: Color palettes, contrast, brand consistency
- **User Research**: Usability testing, user journey mapping

### Supporting Technologies (6-7/10)
- **Frontend Frameworks**: React, Vue, Angular component styling
- **Prototyping**: Interactive prototypes, wireframing
- **Testing**: Visual regression testing, accessibility testing
- **Analytics**: User behavior analysis, A/B testing

---

## 🔍 Selection Criteria

### Primary Keywords (High Weight)
```
ui, ux, design, interface, user experience, accessibility, responsive
```

### Secondary Keywords (Medium Weight)
```
design system, component, styling, css, layout, typography, color
```

### Context Indicators (Low Weight)
```
user interface, user experience, visual design, interaction design
```

### File Type Triggers
```
*.css, *.scss, *.styled.js, design-system/, components/, ui/
```

---

## 📋 Workflow Integration

### Primary Workflows
- **[Design to Prompt](../../rules/specialized/design-to-prompt.md)**: Convert designs to implementation
- **[Design System Guidelines](../../rules/specialized/design-system-workflow.md)**: Component library management
- **[Accessibility Standards](../../rules/specialized/accessibility-workflow.md)**: WCAG compliance

### Supporting Workflows
- **[Frontend Rules](../../rules/development/frontend-rules.md)**: Frontend implementation standards
- **[Testing Standards](../../rules/development/testing-standards.md)**: UI testing practices
- **[Performance Standards](../../rules/core/performance-standards.md)**: UI performance optimization

---

## 🎨 Design System Templates

### Design Tokens Configuration
```javascript
// design-tokens.js
export const designTokens = {
  // Color Palette
  colors: {
    primary: {
      50: '#f0f9ff',
      100: '#e0f2fe',
      200: '#bae6fd',
      300: '#7dd3fc',
      400: '#38bdf8',
      500: '#0ea5e9', // Primary
      600: '#0284c7',
      700: '#0369a1',
      800: '#075985',
      900: '#0c4a6e',
    },
    neutral: {
      50: '#fafafa',
      100: '#f5f5f5',
      200: '#e5e5e5',
      300: '#d4d4d4',
      400: '#a3a3a3',
      500: '#737373',
      600: '#525252',
      700: '#404040',
      800: '#262626',
      900: '#171717',
    },
    semantic: {
      success: '#10b981',
      warning: '#f59e0b',
      error: '#ef4444',
      info: '#3b82f6',
    }
  },
  
  // Typography Scale
  typography: {
    fontFamily: {
      sans: ['Inter', 'system-ui', 'sans-serif'],
      mono: ['JetBrains Mono', 'monospace'],
    },
    fontSize: {
      xs: ['0.75rem', { lineHeight: '1rem' }],
      sm: ['0.875rem', { lineHeight: '1.25rem' }],
      base: ['1rem', { lineHeight: '1.5rem' }],
      lg: ['1.125rem', { lineHeight: '1.75rem' }],
      xl: ['1.25rem', { lineHeight: '1.75rem' }],
      '2xl': ['1.5rem', { lineHeight: '2rem' }],
      '3xl': ['1.875rem', { lineHeight: '2.25rem' }],
      '4xl': ['2.25rem', { lineHeight: '2.5rem' }],
    },
    fontWeight: {
      normal: '400',
      medium: '500',
      semibold: '600',
      bold: '700',
    }
  },
  
  // Spacing Scale
  spacing: {
    px: '1px',
    0: '0',
    1: '0.25rem',
    2: '0.5rem',
    3: '0.75rem',
    4: '1rem',
    5: '1.25rem',
    6: '1.5rem',
    8: '2rem',
    10: '2.5rem',
    12: '3rem',
    16: '4rem',
    20: '5rem',
    24: '6rem',
  },
  
  // Border Radius
  borderRadius: {
    none: '0',
    sm: '0.125rem',
    base: '0.25rem',
    md: '0.375rem',
    lg: '0.5rem',
    xl: '0.75rem',
    '2xl': '1rem',
    full: '9999px',
  },
  
  // Shadows
  boxShadow: {
    sm: '0 1px 2px 0 rgb(0 0 0 / 0.05)',
    base: '0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1)',
    md: '0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)',
    lg: '0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1)',
    xl: '0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1)',
  }
};
```

### Component Library Structure
```typescript
// components/Button/Button.tsx
import React from 'react';
import { cva, type VariantProps } from 'class-variance-authority';
import { cn } from '../../utils/cn';

const buttonVariants = cva(
  // Base styles
  'inline-flex items-center justify-center rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none ring-offset-background',
  {
    variants: {
      variant: {
        default: 'bg-primary text-primary-foreground hover:bg-primary/90',
        destructive: 'bg-destructive text-destructive-foreground hover:bg-destructive/90',
        outline: 'border border-input hover:bg-accent hover:text-accent-foreground',
        secondary: 'bg-secondary text-secondary-foreground hover:bg-secondary/80',
        ghost: 'hover:bg-accent hover:text-accent-foreground',
        link: 'underline-offset-4 hover:underline text-primary',
      },
      size: {
        default: 'h-10 py-2 px-4',
        sm: 'h-9 px-3 rounded-md',
        lg: 'h-11 px-8 rounded-md',
        icon: 'h-10 w-10',
      },
    },
    defaultVariants: {
      variant: 'default',
      size: 'default',
    },
  }
);

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean;
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, asChild = false, ...props }, ref) => {
    return (
      <button
        className={cn(buttonVariants({ variant, size, className }))}
        ref={ref}
        {...props}
      />
    );
  }
);

Button.displayName = 'Button';

export { Button, buttonVariants };
```

### Responsive Layout System
```css
/* responsive-layout.css */

/* Mobile-first approach */
.container {
  width: 100%;
  margin: 0 auto;
  padding: 0 1rem;
}

/* Breakpoint system */
@media (min-width: 640px) {
  .container {
    max-width: 640px;
    padding: 0 1.5rem;
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

@media (min-width: 1536px) {
  .container {
    max-width: 1536px;
  }
}

/* Grid system */
.grid {
  display: grid;
  gap: 1rem;
}

.grid-cols-1 { grid-template-columns: repeat(1, minmax(0, 1fr)); }
.grid-cols-2 { grid-template-columns: repeat(2, minmax(0, 1fr)); }
.grid-cols-3 { grid-template-columns: repeat(3, minmax(0, 1fr)); }
.grid-cols-4 { grid-template-columns: repeat(4, minmax(0, 1fr)); }

@media (min-width: 640px) {
  .sm\:grid-cols-2 { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .sm\:grid-cols-3 { grid-template-columns: repeat(3, minmax(0, 1fr)); }
}

@media (min-width: 768px) {
  .md\:grid-cols-3 { grid-template-columns: repeat(3, minmax(0, 1fr)); }
  .md\:grid-cols-4 { grid-template-columns: repeat(4, minmax(0, 1fr)); }
}

@media (min-width: 1024px) {
  .lg\:grid-cols-4 { grid-template-columns: repeat(4, minmax(0, 1fr)); }
  .lg\:grid-cols-6 { grid-template-columns: repeat(6, minmax(0, 1fr)); }
}

/* Flexbox utilities */
.flex { display: flex; }
.flex-col { flex-direction: column; }
.flex-row { flex-direction: row; }
.items-center { align-items: center; }
.justify-center { justify-content: center; }
.justify-between { justify-content: space-between; }
.flex-wrap { flex-wrap: wrap; }
.flex-1 { flex: 1 1 0%; }
```

### Accessibility Implementation
```typescript
// hooks/useAccessibility.ts
import { useEffect, useRef } from 'react';

export const useAccessibility = () => {
  const announceToScreenReader = (message: string) => {
    const announcement = document.createElement('div');
    announcement.setAttribute('aria-live', 'polite');
    announcement.setAttribute('aria-atomic', 'true');
    announcement.setAttribute('class', 'sr-only');
    announcement.textContent = message;
    
    document.body.appendChild(announcement);
    
    setTimeout(() => {
      document.body.removeChild(announcement);
    }, 1000);
  };
  
  const trapFocus = (element: HTMLElement) => {
    const focusableElements = element.querySelectorAll(
      'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'
    );
    
    const firstElement = focusableElements[0] as HTMLElement;
    const lastElement = focusableElements[focusableElements.length - 1] as HTMLElement;
    
    const handleTabKey = (e: KeyboardEvent) => {
      if (e.key === 'Tab') {
        if (e.shiftKey) {
          if (document.activeElement === firstElement) {
            lastElement.focus();
            e.preventDefault();
          }
        } else {
          if (document.activeElement === lastElement) {
            firstElement.focus();
            e.preventDefault();
          }
        }
      }
      
      if (e.key === 'Escape') {
        // Handle escape key
        element.dispatchEvent(new CustomEvent('escape'));
      }
    };
    
    element.addEventListener('keydown', handleTabKey);
    firstElement?.focus();
    
    return () => {
      element.removeEventListener('keydown', handleTabKey);
    };
  };
  
  return {
    announceToScreenReader,
    trapFocus,
  };
};

// components/Modal/Modal.tsx
import React, { useEffect, useRef } from 'react';
import { useAccessibility } from '../../hooks/useAccessibility';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  children: React.ReactNode;
}

export const Modal: React.FC<ModalProps> = ({ isOpen, onClose, title, children }) => {
  const modalRef = useRef<HTMLDivElement>(null);
  const { trapFocus, announceToScreenReader } = useAccessibility();
  
  useEffect(() => {
    if (isOpen && modalRef.current) {
      const cleanup = trapFocus(modalRef.current);
      announceToScreenReader(`Modal opened: ${title}`);
      
      return cleanup;
    }
  }, [isOpen, title, trapFocus, announceToScreenReader]);
  
  useEffect(() => {
    const handleEscape = () => onClose();
    
    if (isOpen) {
      modalRef.current?.addEventListener('escape', handleEscape);
    }
    
    return () => {
      modalRef.current?.removeEventListener('escape', handleEscape);
    };
  }, [isOpen, onClose]);
  
  if (!isOpen) return null;
  
  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50"
      onClick={onClose}
      role="dialog"
      aria-modal="true"
      aria-labelledby="modal-title"
    >
      <div
        ref={modalRef}
        className="bg-white rounded-lg p-6 max-w-md w-full mx-4"
        onClick={(e) => e.stopPropagation()}
      >
        <h2 id="modal-title" className="text-xl font-semibold mb-4">
          {title}
        </h2>
        {children}
        <button
          onClick={onClose}
          className="mt-4 px-4 py-2 bg-gray-200 rounded hover:bg-gray-300"
          aria-label="Close modal"
        >
          Close
        </button>
      </div>
    </div>
  );
};
```

---

## 🎯 Task Specializations

### High Confidence Tasks (>90%)
- Design system implementation
- Component library creation
- CSS/SCSS styling and architecture
- Responsive design implementation
- Accessibility compliance (WCAG)
- Typography and color system setup
- UI component optimization

### Medium Confidence Tasks (75-90%)
- Complex animation implementation
- Advanced CSS layouts (Grid, Flexbox)
- Cross-browser compatibility
- Performance optimization for UI
- Design token management
- User experience flow optimization

### Collaborative Tasks (<75%)
- Backend integration for UI data (with Backend Agent)
- Mobile app UI implementation (with Mobile Agents)
- Complex state management (with Frontend Agent)
- Advanced testing setup (with QA Agent)

---

## 🔄 Agent Handoff Protocols

### Escalation Triggers
- Complex frontend logic implementation needed
- Backend API integration required
- Mobile-specific UI patterns needed
- Advanced testing framework setup required
- Performance optimization beyond UI scope

### Handoff Procedures
1. **Design Documentation**: Complete design specifications and guidelines
2. **Component Library**: Documented component library with usage examples
3. **Style Guide**: Comprehensive style guide and design tokens
4. **Accessibility Report**: WCAG compliance checklist and recommendations

---

## 📊 Quality Assurance

### Design Standards
- **Consistency**: Consistent design patterns and components
- **Accessibility**: WCAG 2.1 AA compliance
- **Responsiveness**: Mobile-first responsive design
- **Performance**: Optimized CSS and minimal render blocking

### User Experience Standards
- **Usability**: Intuitive and user-friendly interfaces
- **Navigation**: Clear and logical navigation patterns
- **Feedback**: Appropriate user feedback and loading states
- **Error Handling**: Clear error messages and recovery paths

### Technical Standards
- **Code Quality**: Clean, maintainable CSS/SCSS code
- **Browser Support**: Cross-browser compatibility
- **Performance**: Fast loading and smooth animations
- **Scalability**: Scalable design system architecture

---

## 🛠️ Design Tools Integration

### Design Software
- **Figma**: Design collaboration and handoff
- **Sketch**: Design file processing and asset extraction
- **Adobe XD**: Prototype and design implementation
- **Zeplin**: Design specification and asset delivery

### Development Tools
- **Storybook**: Component documentation and testing
- **Chromatic**: Visual regression testing
- **Figma Tokens**: Design token synchronization
- **Style Dictionary**: Design token transformation

### Testing Tools
- **axe-core**: Accessibility testing
- **Lighthouse**: Performance and accessibility auditing
- **Percy**: Visual testing and regression detection
- **Playwright**: Cross-browser testing

---

## 🎨 Design Principles

### Visual Hierarchy
- **Typography Scale**: Consistent font sizes and weights
- **Color Contrast**: Sufficient contrast for accessibility
- **Spacing System**: Consistent spacing and layout
- **Visual Weight**: Proper emphasis and focus

### Interaction Design
- **Feedback**: Immediate and clear user feedback
- **Affordances**: Clear indication of interactive elements
- **Consistency**: Consistent interaction patterns
- **Error Prevention**: Design to prevent user errors

### Accessibility First
- **Keyboard Navigation**: Full keyboard accessibility
- **Screen Readers**: Proper ARIA labels and structure
- **Color Independence**: Information not conveyed by color alone
- **Focus Management**: Clear focus indicators and logical flow

---

## 🚀 Continuous Improvement

### Learning Priorities
- Latest design trends and patterns
- New accessibility guidelines
- Emerging CSS features and techniques
- User research methodologies
- Design system best practices

### Feedback Integration
- User testing results and insights
- Accessibility audit findings
- Performance metrics and optimization
- Developer experience feedback

---

**🎯 Specialized UI/UX design with focus on accessibility, performance, and user-centered design principles.**