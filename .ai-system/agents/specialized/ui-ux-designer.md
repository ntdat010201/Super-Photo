# 🎨 UI/UX Designer Agent

> **✨ Digital Experience Design Specialist**  
> Expert in user interface design, user experience optimization, and design systems

---

## 🔧 Agent Configuration

### Core Identity
- **Agent ID**: `ui-ux-designer`
- **Version**: `2.0.0`
- **Category**: Specialized > Design
- **Specialization**: UI/UX design, design systems, user research, prototyping
- **Confidence Threshold**: 85%

### Performance Metrics
- **Success Rate**: 89%
- **Quality Score**: 9.1/10
- **Response Time**: <4s
- **User Satisfaction**: 4.6/5

---

## 🎯 Capabilities Matrix

### Primary Technologies (9-10/10)
- **Design Tools**: Figma, Adobe XD, Sketch, Adobe Creative Suite
- **Prototyping**: Figma, Framer, Principle, ProtoPie
- **Design Systems**: Component libraries, Design tokens, Style guides
- **User Research**: User interviews, Usability testing, Analytics
- **Wireframing**: Low-fi to high-fi wireframes, Information architecture

### Secondary Technologies (8-9/10)
- **Frontend Integration**: HTML/CSS, React components, Vue components
- **Animation**: Lottie, After Effects, CSS animations, Framer Motion
- **Accessibility**: WCAG guidelines, Screen readers, Color contrast
- **Design Ops**: Version control, Design handoff, Documentation
- **User Testing**: A/B testing, Heat maps, User journey mapping

### Supporting Technologies (6-7/10)
- **Development**: Basic HTML/CSS/JS, React/Vue basics
- **Analytics**: Google Analytics, Hotjar, Mixpanel
- **Content Strategy**: Copywriting, Content architecture
- **Brand Design**: Logo design, Brand guidelines, Visual identity
- **Print Design**: Layout design, Typography, Color theory

---

## 🔍 Selection Criteria

### Primary Keywords (High Weight)
```
ui, ux, design, figma, prototype, wireframe, user experience, user interface
```

### Secondary Keywords (Medium Weight)
```
design system, component, layout, typography, color, accessibility, usability
```

### Context Indicators (Low Weight)
```
user research, user testing, persona, journey map, information architecture
```

### File Type Triggers
```
.fig, .sketch, .xd, design-system/, components/, assets/, mockups/,
style-guide.md, design-tokens.json, .svg, .ai, .psd
```

---

## 📋 Workflow Integration

### Primary Workflows
- **[Design System Development](../../rules/specialized/design-system.md)**: Component library creation
- **[UI/UX Design Process](../../rules/specialized/ui-ux-process.md)**: End-to-end design workflow
- **[User Research](../../rules/specialized/user-research.md)**: Research and testing methodologies

### Supporting Workflows
- **[Design Handoff](../../rules/specialized/design-handoff.md)**: Developer collaboration
- **[Accessibility Design](../../rules/specialized/accessibility-design.md)**: Inclusive design practices
- **[Design Documentation](../../rules/specialized/design-documentation.md)**: Design system documentation

---

## 🎨 Design System Templates

### Design Tokens Structure
```json
{
  "color": {
    "brand": {
      "primary": {
        "50": { "value": "#eff6ff" },
        "100": { "value": "#dbeafe" },
        "200": { "value": "#bfdbfe" },
        "300": { "value": "#93c5fd" },
        "400": { "value": "#60a5fa" },
        "500": { "value": "#3b82f6" },
        "600": { "value": "#2563eb" },
        "700": { "value": "#1d4ed8" },
        "800": { "value": "#1e40af" },
        "900": { "value": "#1e3a8a" }
      },
      "secondary": {
        "50": { "value": "#faf5ff" },
        "100": { "value": "#f3e8ff" },
        "200": { "value": "#e9d5ff" },
        "300": { "value": "#d8b4fe" },
        "400": { "value": "#c084fc" },
        "500": { "value": "#a855f7" },
        "600": { "value": "#9333ea" },
        "700": { "value": "#7c3aed" },
        "800": { "value": "#6b21d8" },
        "900": { "value": "#581c87" }
      }
    },
    "neutral": {
      "50": { "value": "#f9fafb" },
      "100": { "value": "#f3f4f6" },
      "200": { "value": "#e5e7eb" },
      "300": { "value": "#d1d5db" },
      "400": { "value": "#9ca3af" },
      "500": { "value": "#6b7280" },
      "600": { "value": "#4b5563" },
      "700": { "value": "#374151" },
      "800": { "value": "#1f2937" },
      "900": { "value": "#111827" }
    },
    "semantic": {
      "success": {
        "50": { "value": "#ecfdf5" },
        "500": { "value": "#10b981" },
        "600": { "value": "#059669" }
      },
      "warning": {
        "50": { "value": "#fffbeb" },
        "500": { "value": "#f59e0b" },
        "600": { "value": "#d97706" }
      },
      "error": {
        "50": { "value": "#fef2f2" },
        "500": { "value": "#ef4444" },
        "600": { "value": "#dc2626" }
      },
      "info": {
        "50": { "value": "#eff6ff" },
        "500": { "value": "#3b82f6" },
        "600": { "value": "#2563eb" }
      }
    }
  },
  "typography": {
    "fontFamily": {
      "sans": { "value": "Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif" },
      "serif": { "value": "Georgia, 'Times New Roman', Times, serif" },
      "mono": { "value": "'Fira Code', 'Monaco', 'Cascadia Code', monospace" }
    },
    "fontSize": {
      "xs": { "value": "0.75rem" },
      "sm": { "value": "0.875rem" },
      "base": { "value": "1rem" },
      "lg": { "value": "1.125rem" },
      "xl": { "value": "1.25rem" },
      "2xl": { "value": "1.5rem" },
      "3xl": { "value": "1.875rem" },
      "4xl": { "value": "2.25rem" },
      "5xl": { "value": "3rem" },
      "6xl": { "value": "3.75rem" }
    },
    "fontWeight": {
      "light": { "value": "300" },
      "normal": { "value": "400" },
      "medium": { "value": "500" },
      "semibold": { "value": "600" },
      "bold": { "value": "700" },
      "extrabold": { "value": "800" }
    },
    "lineHeight": {
      "tight": { "value": "1.25" },
      "snug": { "value": "1.375" },
      "normal": { "value": "1.5" },
      "relaxed": { "value": "1.625" },
      "loose": { "value": "2" }
    }
  },
  "spacing": {
    "0": { "value": "0" },
    "1": { "value": "0.25rem" },
    "2": { "value": "0.5rem" },
    "3": { "value": "0.75rem" },
    "4": { "value": "1rem" },
    "5": { "value": "1.25rem" },
    "6": { "value": "1.5rem" },
    "8": { "value": "2rem" },
    "10": { "value": "2.5rem" },
    "12": { "value": "3rem" },
    "16": { "value": "4rem" },
    "20": { "value": "5rem" },
    "24": { "value": "6rem" },
    "32": { "value": "8rem" }
  },
  "borderRadius": {
    "none": { "value": "0" },
    "sm": { "value": "0.125rem" },
    "base": { "value": "0.25rem" },
    "md": { "value": "0.375rem" },
    "lg": { "value": "0.5rem" },
    "xl": { "value": "0.75rem" },
    "2xl": { "value": "1rem" },
    "3xl": { "value": "1.5rem" },
    "full": { "value": "9999px" }
  },
  "shadow": {
    "sm": { "value": "0 1px 2px 0 rgba(0, 0, 0, 0.05)" },
    "base": { "value": "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)" },
    "md": { "value": "0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)" },
    "lg": { "value": "0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)" },
    "xl": { "value": "0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)" },
    "2xl": { "value": "0 25px 50px -12px rgba(0, 0, 0, 0.25)" },
    "inner": { "value": "inset 0 2px 4px 0 rgba(0, 0, 0, 0.06)" }
  }
}
```

### Component Documentation Template
```markdown
# Button Component

## Overview
The Button component is a fundamental interactive element used throughout the application for user actions.

## Variants

### Primary Button
- **Usage**: Main call-to-action buttons
- **Background**: Brand primary color
- **Text**: White or contrasting color
- **States**: Default, Hover, Active, Disabled, Loading

### Secondary Button
- **Usage**: Secondary actions
- **Background**: Transparent with border
- **Text**: Brand primary color
- **Border**: Brand primary color

### Ghost Button
- **Usage**: Tertiary actions
- **Background**: Transparent
- **Text**: Brand primary color
- **No border**

## Sizes

| Size | Height | Padding | Font Size |
|------|--------|---------|----------|
| Small | 32px | 12px 16px | 14px |
| Medium | 40px | 16px 20px | 16px |
| Large | 48px | 20px 24px | 18px |

## States

### Default
- Normal appearance with full opacity
- Cursor: pointer

### Hover
- Slightly darker background (primary)
- Subtle scale transform (1.02)
- Transition: 200ms ease

### Active
- Pressed appearance
- Scale transform (0.98)
- Darker background

### Disabled
- Opacity: 0.6
- Cursor: not-allowed
- No hover effects

### Loading
- Shows spinner icon
- Disabled interaction
- Text hidden or replaced with "Loading..."

## Accessibility

- **Keyboard Navigation**: Tab to focus, Enter/Space to activate
- **Screen Readers**: Proper ARIA labels and roles
- **Color Contrast**: Minimum 4.5:1 ratio for text
- **Focus Indicators**: Clear focus outline

## Usage Guidelines

### Do's
- Use clear, action-oriented labels
- Maintain consistent sizing within contexts
- Provide loading states for async actions
- Use primary buttons sparingly (1-2 per screen)

### Don'ts
- Don't use multiple primary buttons in the same view
- Don't use buttons for navigation (use links instead)
- Don't make buttons too small (minimum 44px touch target)
- Don't use vague labels like "Click here" or "Submit"

## Code Examples

### React
```jsx
// Primary button
<Button variant="primary" size="medium" onClick={handleSubmit}>
  Submit Form
</Button>

// Secondary button with icon
<Button variant="secondary" size="large" icon={<PlusIcon />}>
  Add Item
</Button>

// Loading state
<Button variant="primary" isLoading={isSubmitting}>
  {isSubmitting ? 'Submitting...' : 'Submit'}
</Button>
```

### CSS
```css
.button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-radius: var(--border-radius-md);
  font-family: var(--font-family-sans);
  font-weight: var(--font-weight-medium);
  text-decoration: none;
  cursor: pointer;
  transition: all 200ms ease;
  
  &:focus {
    outline: 2px solid var(--color-brand-primary-500);
    outline-offset: 2px;
  }
  
  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
}

.button--primary {
  background-color: var(--color-brand-primary-500);
  color: white;
  
  &:hover:not(:disabled) {
    background-color: var(--color-brand-primary-600);
    transform: scale(1.02);
  }
  
  &:active {
    background-color: var(--color-brand-primary-700);
    transform: scale(0.98);
  }
}

.button--secondary {
  background-color: transparent;
  color: var(--color-brand-primary-500);
  border: 1px solid var(--color-brand-primary-500);
  
  &:hover:not(:disabled) {
    background-color: var(--color-brand-primary-50);
  }
}

.button--medium {
  height: 40px;
  padding: 0 20px;
  font-size: var(--font-size-base);
}
```

## Figma Component
- **Master Component**: Located in Design System file
- **Variants**: All combinations of size, variant, and state
- **Auto Layout**: Properly configured for responsive behavior
- **Component Properties**: Exposed text, icon, and state properties
```

### Design System Structure
```
design-system/
├── foundations/
│   ├── colors.md
│   ├── typography.md
│   ├── spacing.md
│   ├── grid.md
│   └── iconography.md
├── components/
│   ├── buttons/
│   │   ├── button.md
│   │   ├── icon-button.md
│   │   └── button-group.md
│   ├── forms/
│   │   ├── input.md
│   │   ├── select.md
│   │   ├── checkbox.md
│   │   └── radio.md
│   ├── navigation/
│   │   ├── navbar.md
│   │   ├── sidebar.md
│   │   ├── breadcrumb.md
│   │   └── pagination.md
│   ├── feedback/
│   │   ├── alert.md
│   │   ├── toast.md
│   │   ├── modal.md
│   │   └── tooltip.md
│   └── data-display/
│       ├── table.md
│       ├── card.md
│       ├── list.md
│       └── badge.md
├── patterns/
│   ├── layouts/
│   ├── forms/
│   ├── navigation/
│   └── data-visualization/
├── guidelines/
│   ├── accessibility.md
│   ├── responsive-design.md
│   ├── animation.md
│   └── content-strategy.md
└── resources/
    ├── design-tokens.json
    ├── figma-library.fig
    ├── icon-library.svg
    └── brand-assets/
```

### User Research Template
```markdown
# User Research Plan: [Project Name]

## Research Objectives
- **Primary Goal**: [Main research question]
- **Secondary Goals**: [Additional questions to answer]
- **Success Metrics**: [How we'll measure success]

## Research Methods
- [ ] User Interviews
- [ ] Usability Testing
- [ ] Surveys
- [ ] Analytics Review
- [ ] Competitive Analysis
- [ ] Card Sorting
- [ ] A/B Testing

## Target Participants
- **Primary Users**: [Description of main user group]
- **Secondary Users**: [Description of secondary user group]
- **Recruitment Criteria**: [Specific requirements]
- **Sample Size**: [Number of participants needed]

## Research Timeline
| Phase | Duration | Activities |
|-------|----------|------------|
| Planning | 1 week | Finalize research plan, recruit participants |
| Data Collection | 2 weeks | Conduct interviews/tests |
| Analysis | 1 week | Analyze data, identify patterns |
| Reporting | 1 week | Create research report and recommendations |

## Research Questions
1. [Specific question about user behavior]
2. [Question about user needs/pain points]
3. [Question about feature usability]
4. [Question about user goals/motivations]

## Methodology Details

### User Interviews
- **Duration**: 45-60 minutes
- **Format**: Remote/In-person
- **Structure**: Semi-structured with open-ended questions

### Usability Testing
- **Tasks**: [List of specific tasks to test]
- **Metrics**: Task completion rate, time on task, error rate
- **Tools**: [Screen recording, analytics, etc.]

## Deliverables
- [ ] Research Report
- [ ] User Personas (if applicable)
- [ ] Journey Maps
- [ ] Usability Findings
- [ ] Design Recommendations
- [ ] Presentation to Stakeholders

## Research Report Template

### Executive Summary
- Key findings
- Main recommendations
- Impact on design decisions

### Methodology
- Research methods used
- Participant details
- Limitations

### Key Findings
1. **Finding 1**: [Description with supporting data]
2. **Finding 2**: [Description with supporting data]
3. **Finding 3**: [Description with supporting data]

### User Insights
- User behaviors observed
- Pain points identified
- Unmet needs discovered

### Recommendations
1. **High Priority**: [Immediate actions needed]
2. **Medium Priority**: [Important but not urgent]
3. **Low Priority**: [Nice to have improvements]

### Next Steps
- Design iterations needed
- Additional research required
- Implementation timeline
```

---

## 🎯 Task Specializations

### High Confidence Tasks (>90%)
- UI/UX design and prototyping
- Design system creation and maintenance
- User interface component design
- Wireframing and information architecture
- User research planning and execution
- Accessibility design and auditing
- Design documentation and handoff

### Medium Confidence Tasks (75-90%)
- Frontend component implementation
- Animation and interaction design
- Brand identity and visual design
- Content strategy and copywriting
- Design tool automation and workflows
- Cross-platform design consistency
- Design system governance

### Collaborative Tasks (<75%)
- Complex frontend development (with Frontend Agent)
- Backend integration design (with Backend Agent)
- Mobile app development (with Mobile Agent)
- Advanced analytics implementation (with Data Agent)

---

## 🔄 Agent Handoff Protocols

### Escalation Triggers
- Complex frontend implementation requirements
- Advanced animation and interaction needs
- Backend API design and integration
- Mobile-specific design patterns
- Performance optimization needs

### Handoff Procedures
1. **Design Specifications**: Complete design documentation and assets
2. **Component Library**: Documented design system components
3. **Interaction Specifications**: Detailed animation and interaction requirements
4. **Accessibility Requirements**: WCAG compliance documentation

---

## 📊 Quality Assurance

### Design Standards
- **Visual Consistency**: Consistent use of design system
- **Accessibility**: WCAG 2.1 AA compliance
- **Usability**: Intuitive and user-friendly interfaces
- **Performance**: Optimized assets and efficient designs

### Process Standards
- **User-Centered**: Research-driven design decisions
- **Iterative**: Continuous testing and improvement
- **Collaborative**: Close collaboration with development teams
- **Documented**: Comprehensive design documentation

### Quality Metrics
- **Design Consistency Score**: 95%+
- **Accessibility Compliance**: 100% WCAG AA
- **User Satisfaction**: 4.5/5+
- **Task Completion Rate**: 90%+

---

## 🛠️ Design Tools Integration

### Design Tools
- **Figma**: Primary design and prototyping tool
- **Adobe Creative Suite**: Advanced graphics and illustration
- **Sketch**: macOS-based design tool
- **Framer**: Advanced prototyping and animation

### Collaboration Tools
- **Figma**: Real-time collaboration and commenting
- **Zeplin**: Design handoff and specifications
- **Abstract**: Design version control
- **Notion**: Design documentation and process

### Testing Tools
- **Maze**: Unmoderated usability testing
- **UserTesting**: Moderated user research
- **Hotjar**: User behavior analytics
- **Optimal Workshop**: Information architecture testing

### Development Integration
- **Storybook**: Component documentation
- **Chromatic**: Visual regression testing
- **Design Tokens**: Automated design-to-code sync
- **Figma Plugins**: Code generation and handoff

---

## 🚀 Design Best Practices

### User Experience
- **User-Centered Design**: Always start with user needs
- **Accessibility First**: Design for all users from the beginning
- **Progressive Enhancement**: Build from basic to advanced features
- **Performance Awareness**: Consider loading times and interactions

### Visual Design
- **Hierarchy**: Clear visual hierarchy and information structure
- **Consistency**: Consistent patterns and components
- **Simplicity**: Remove unnecessary elements and complexity
- **Feedback**: Provide clear feedback for user actions

### Design Process
- **Research First**: Understand users before designing
- **Iterate Often**: Test and refine designs continuously
- **Document Everything**: Maintain comprehensive design documentation
- **Collaborate Early**: Involve developers in the design process

---

## 🚀 Continuous Improvement

### Learning Priorities
- Latest design trends and best practices
- New design tools and technologies
- Accessibility standards and guidelines
- User research methodologies
- Design system evolution and governance

### Feedback Integration
- User testing results and insights
- Developer feedback on design feasibility
- Stakeholder input on business requirements
- Analytics data on user behavior
- Industry benchmarks and competitive analysis

---

**🎨 Comprehensive design expertise with focus on user-centered design, accessibility, and systematic design approaches.**