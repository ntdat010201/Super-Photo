# 🔄 Git Workflow Rules - Universal Version Control Standards

> **🚀 Comprehensive Git workflow guidelines for collaborative development**  
> Standardized branching, commit conventions, and collaboration practices

---

## 🎯 Core Git Workflow Strategy

### Git Flow Model (Recommended)
```
master/main     ──●──────●──────●──────●──────●──
                  │      │      │      │      │
release/*         │      ●──────●      │      │
                  │     /        \     │      │
develop          ●──●──●──────────●────●──●───●──
                 │  │  │          │    │  │   │
feature/*        │  ●──●──────────●    │  │   │
                 │     │          │    │  │   │
hotfix/*         │     │          │    ●──●   │
                 │     │          │       │   │
bugfix/*         │     ●──────────●       │   │
                 │                        │   │
chore/*          ●────────────────────────●   │
```

### Branch Types & Naming Conventions
```bash
# Main Branches
master/main           # Production-ready code
develop              # Integration branch for features

# Supporting Branches
feature/TASK-123-user-authentication
feature/add-payment-gateway
feature/improve-search-functionality

bugfix/TASK-456-fix-login-error
bugfix/resolve-memory-leak
bugfix/correct-validation-logic

hotfix/TASK-789-critical-security-patch
hotfix/fix-payment-processing

release/v1.2.0
release/v2.0.0-beta

chore/update-dependencies
chore/setup-ci-pipeline
chore/refactor-database-schema
```

---

## 📝 Commit Message Standards

### Conventional Commits Format
```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

### Commit Types
```bash
# Feature Development
feat: add user authentication system
feat(auth): implement JWT token validation
feat(api): add user profile endpoints

# Bug Fixes
fix: resolve login redirect issue
fix(ui): correct button alignment on mobile
fix(api): handle null pointer exception in user service

# Documentation
docs: update API documentation
docs(readme): add installation instructions
docs(api): document authentication endpoints

# Code Refactoring
refactor: restructure user service layer
refactor(auth): simplify token validation logic
refactor(db): optimize database queries

# Performance Improvements
perf: optimize image loading performance
perf(api): reduce database query execution time
perf(ui): implement lazy loading for components

# Testing
test: add unit tests for user service
test(auth): add integration tests for login flow
test(e2e): add end-to-end tests for checkout process

# Build & CI/CD
build: update webpack configuration
build(deps): upgrade React to version 18
ci: add automated testing pipeline

# Chores & Maintenance
chore: update dependencies to latest versions
chore(lint): fix ESLint warnings
chore(config): update environment variables

# Style Changes
style: fix code formatting issues
style(css): improve responsive design
style(lint): resolve linting errors

# Reverts
revert: revert "add experimental feature"
```

### Commit Message Examples
```bash
# Good Examples
feat(auth): implement OAuth2 integration with Google

Add Google OAuth2 authentication flow:
- Configure OAuth2 client credentials
- Implement authorization code flow
- Add user profile synchronization
- Update login UI with Google sign-in button

Closes #123

fix(api): resolve race condition in user registration

The user registration endpoint was experiencing race conditions
when multiple requests were made simultaneously. This fix:
- Adds proper database transaction handling
- Implements user email uniqueness validation
- Adds retry logic for failed registrations

Fixes #456
Tested-by: QA Team

perf(db): optimize user query performance

Improve database query performance by:
- Adding composite index on (email, status) columns
- Implementing query result caching
- Reducing N+1 query problems

Benchmark results:
- Query time reduced from 250ms to 45ms
- Memory usage decreased by 30%

Breaking-change: Database migration required

# Bad Examples (Avoid)
git commit -m "fix bug"                    # Too vague
git commit -m "WIP"                        # Not descriptive
git commit -m "Fixed the thing"            # Unclear
git commit -m "Updated files"              # No context
git commit -m "asdf"                       # Meaningless
```

---

## 🌿 Branching Strategy

### Feature Development Workflow
```bash
# 1. Start from develop branch
git checkout develop
git pull origin develop

# 2. Create feature branch
git checkout -b feature/TASK-123-user-authentication

# 3. Work on feature with atomic commits
git add .
git commit -m "feat(auth): add user model and validation"
git commit -m "feat(auth): implement password hashing"
git commit -m "feat(auth): add login endpoint"
git commit -m "test(auth): add unit tests for authentication"

# 4. Keep branch updated with develop
git fetch origin
git rebase origin/develop

# 5. Push feature branch
git push origin feature/TASK-123-user-authentication

# 6. Create Pull Request
# - Target: develop
# - Reviewers: Team members
# - Labels: feature, needs-review

# 7. After approval and merge, cleanup
git checkout develop
git pull origin develop
git branch -d feature/TASK-123-user-authentication
git push origin --delete feature/TASK-123-user-authentication
```

### Hotfix Workflow
```bash
# 1. Start from master/main
git checkout main
git pull origin main

# 2. Create hotfix branch
git checkout -b hotfix/TASK-789-critical-security-patch

# 3. Implement fix
git add .
git commit -m "fix(security): patch SQL injection vulnerability"
git commit -m "test(security): add regression tests"

# 4. Push and create PR to main
git push origin hotfix/TASK-789-critical-security-patch

# 5. After merge to main, also merge to develop
git checkout develop
git pull origin develop
git merge main
git push origin develop

# 6. Cleanup
git branch -d hotfix/TASK-789-critical-security-patch
git push origin --delete hotfix/TASK-789-critical-security-patch
```

### Release Workflow
```bash
# 1. Create release branch from develop
git checkout develop
git pull origin develop
git checkout -b release/v1.2.0

# 2. Prepare release
git commit -m "chore(release): bump version to 1.2.0"
git commit -m "docs(release): update CHANGELOG.md"
git commit -m "fix(release): resolve minor UI issues"

# 3. Merge to main
git checkout main
git pull origin main
git merge --no-ff release/v1.2.0
git tag -a v1.2.0 -m "Release version 1.2.0"
git push origin main --tags

# 4. Merge back to develop
git checkout develop
git merge --no-ff release/v1.2.0
git push origin develop

# 5. Cleanup
git branch -d release/v1.2.0
git push origin --delete release/v1.2.0
```

---

## 🔍 Code Review Standards

### Pull Request Template
```markdown
## 📋 Pull Request Description

### 🎯 What does this PR do?
- [ ] Adds new feature
- [ ] Fixes bug
- [ ] Improves performance
- [ ] Refactors code
- [ ] Updates documentation
- [ ] Other: ___________

### 📝 Description
Brief description of changes and motivation.

### 🔗 Related Issues
Closes #123
Related to #456

### 🧪 Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing completed
- [ ] All tests passing

### 📸 Screenshots (if applicable)
| Before | After |
|--------|-------|
| ![before](url) | ![after](url) |

### ✅ Checklist
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] No breaking changes (or documented)
- [ ] Performance impact considered
- [ ] Security implications reviewed

### 🚀 Deployment Notes
Any special deployment considerations or migration steps.
```

### Review Guidelines
```markdown
## Code Review Checklist

### 🔍 Code Quality
- [ ] Code is readable and well-documented
- [ ] Functions are small and focused
- [ ] Variable and function names are descriptive
- [ ] No code duplication
- [ ] Error handling is appropriate
- [ ] Security best practices followed

### 🏗️ Architecture
- [ ] Changes align with project architecture
- [ ] Dependencies are justified
- [ ] Performance implications considered
- [ ] Scalability impact assessed

### 🧪 Testing
- [ ] Adequate test coverage
- [ ] Tests are meaningful and not just for coverage
- [ ] Edge cases are tested
- [ ] Integration points are tested

### 📚 Documentation
- [ ] Code is self-documenting
- [ ] Complex logic is commented
- [ ] API changes are documented
- [ ] README updated if needed

### 🔒 Security
- [ ] Input validation implemented
- [ ] Authentication/authorization checked
- [ ] Sensitive data handling reviewed
- [ ] No secrets in code
```

---

## 🏷️ Tagging & Release Management

### Semantic Versioning
```bash
# Version Format: MAJOR.MINOR.PATCH
# Example: 1.2.3

# MAJOR: Breaking changes
v2.0.0  # API changes, major refactoring
v3.0.0  # New architecture, breaking changes

# MINOR: New features (backward compatible)
v1.1.0  # New endpoints, features
v1.2.0  # Enhanced functionality

# PATCH: Bug fixes (backward compatible)
v1.2.1  # Bug fixes, security patches
v1.2.2  # Minor improvements

# Pre-release versions
v2.0.0-alpha.1    # Alpha release
v2.0.0-beta.1     # Beta release
v2.0.0-rc.1       # Release candidate
```

### Release Tagging
```bash
# Create annotated tag
git tag -a v1.2.0 -m "Release version 1.2.0

Features:
- User authentication system
- Payment gateway integration
- Enhanced search functionality

Bug Fixes:
- Fixed login redirect issue
- Resolved memory leak in image processing
- Corrected validation logic

Breaking Changes:
- API endpoint /users now requires authentication
- Database schema updated (migration required)"

# Push tags
git push origin --tags

# List tags
git tag -l

# Delete tag (if needed)
git tag -d v1.2.0
git push origin --delete v1.2.0
```

---

## 🔧 Git Configuration

### Global Git Setup
```bash
# User Configuration
git config --global user.name "Your Name"
git config --global user.email "your.email@company.com"

# Editor Configuration
git config --global core.editor "code --wait"  # VS Code
git config --global core.editor "vim"          # Vim

# Line Ending Configuration
git config --global core.autocrlf input        # macOS/Linux
git config --global core.autocrlf true         # Windows

# Default Branch
git config --global init.defaultBranch main

# Pull Strategy
git config --global pull.rebase true

# Push Strategy
git config --global push.default simple

# Merge Tool
git config --global merge.tool vscode
git config --global mergetool.vscode.cmd 'code --wait $MERGED'

# Diff Tool
git config --global diff.tool vscode
git config --global difftool.vscode.cmd 'code --wait --diff $LOCAL $REMOTE'
```

### Project-Specific Git Hooks
```bash
# .git/hooks/pre-commit
#!/bin/sh
# Pre-commit hook for code quality checks

echo "Running pre-commit checks..."

# Run linting
npm run lint
if [ $? -ne 0 ]; then
  echo "❌ Linting failed. Please fix errors before committing."
  exit 1
fi

# Run tests
npm run test
if [ $? -ne 0 ]; then
  echo "❌ Tests failed. Please fix failing tests before committing."
  exit 1
fi

# Check commit message format
commit_regex='^(feat|fix|docs|style|refactor|test|chore|perf|ci|build|revert)(\(.+\))?: .{1,50}'

if ! grep -qE "$commit_regex" "$1"; then
  echo "❌ Invalid commit message format."
  echo "Format: <type>[optional scope]: <description>"
  echo "Example: feat(auth): add user authentication"
  exit 1
fi

echo "✅ Pre-commit checks passed!"

# .git/hooks/commit-msg
#!/bin/sh
# Commit message validation

commit_regex='^(feat|fix|docs|style|refactor|test|chore|perf|ci|build|revert)(\(.+\))?: .{1,50}'

if ! grep -qE "$commit_regex" "$1"; then
  echo "❌ Invalid commit message format."
  echo "Format: <type>[optional scope]: <description>"
  echo "Example: feat(auth): add user authentication"
  exit 1
fi

# .git/hooks/pre-push
#!/bin/sh
# Pre-push hook for additional checks

echo "Running pre-push checks..."

# Run full test suite
npm run test:full
if [ $? -ne 0 ]; then
  echo "❌ Full test suite failed. Please fix before pushing."
  exit 1
fi

# Check for TODO/FIXME comments
if git diff --cached --name-only | xargs grep -l "TODO\|FIXME" > /dev/null; then
  echo "⚠️  Warning: Found TODO/FIXME comments in staged files."
  echo "Consider resolving them before pushing."
fi

echo "✅ Pre-push checks passed!"
```

---

## 🚀 Advanced Git Techniques

### Interactive Rebase for Clean History
```bash
# Squash multiple commits into one
git rebase -i HEAD~3

# In the editor, change 'pick' to 'squash' for commits to combine
pick abc1234 feat(auth): add user model
squash def5678 feat(auth): add validation
squash ghi9012 feat(auth): fix typos

# Result: One clean commit with all changes

# Reorder commits
git rebase -i HEAD~5
# Reorder lines in the editor to change commit order

# Edit commit messages
git rebase -i HEAD~2
# Change 'pick' to 'reword' to edit commit message
```

### Cherry-picking Commits
```bash
# Apply specific commit from another branch
git cherry-pick abc1234

# Cherry-pick multiple commits
git cherry-pick abc1234 def5678

# Cherry-pick a range of commits
git cherry-pick abc1234..def5678

# Cherry-pick without committing (for review)
git cherry-pick --no-commit abc1234
```

### Stashing Work
```bash
# Stash current changes
git stash push -m "WIP: working on feature X"

# Stash specific files
git stash push -m "Stash config changes" config/

# List stashes
git stash list

# Apply stash
git stash apply stash@{0}

# Apply and remove stash
git stash pop

# Create branch from stash
git stash branch feature/new-work stash@{0}
```

### Bisect for Bug Hunting
```bash
# Start bisect session
git bisect start

# Mark current commit as bad
git bisect bad

# Mark known good commit
git bisect good v1.0.0

# Git will checkout middle commit
# Test and mark as good or bad
git bisect good  # or git bisect bad

# Continue until bug is found
# Reset when done
git bisect reset
```

---

## 📊 Git Analytics & Monitoring

### Useful Git Commands for Analysis
```bash
# Show commit statistics
git shortlog -sn --all

# Show file change frequency
git log --pretty=format: --name-only | sort | uniq -c | sort -rg

# Show commits by author
git log --author="John Doe" --oneline

# Show commits in date range
git log --since="2023-01-01" --until="2023-12-31" --oneline

# Show branch merge history
git log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit

# Show file history
git log -p filename

# Show who changed what in a file
git blame filename

# Show changes between branches
git diff main..develop

# Show files changed between commits
git diff --name-only abc1234..def5678
```

### Git Aliases for Productivity
```bash
# Add to ~/.gitconfig
[alias]
    st = status
    co = checkout
    br = branch
    ci = commit
    ca = commit -a
    cm = commit -m
    cam = commit -am
    cp = cherry-pick
    rb = rebase
    rbi = rebase -i
    lg = log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit
    lga = log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit --all
    unstage = reset HEAD --
    last = log -1 HEAD
    visual = !gitk
    type = cat-file -t
    dump = cat-file -p
    find = !git ls-files | grep -i
    grep = grep -Ii
    ll = log --oneline
    lla = log --oneline --all
    graph = log --graph --all --decorate --stat --date=iso
```

---

## ✅ Git Workflow Checklist

### Daily Development
- [ ] Start day with `git pull origin develop`
- [ ] Create feature branch with descriptive name
- [ ] Make atomic commits with conventional messages
- [ ] Regularly rebase with develop to stay updated
- [ ] Run tests before committing
- [ ] Push feature branch and create PR
- [ ] Request code review from team members
- [ ] Address review feedback promptly
- [ ] Merge after approval and cleanup branch

### Release Preparation
- [ ] Create release branch from develop
- [ ] Update version numbers
- [ ] Update CHANGELOG.md
- [ ] Run full test suite
- [ ] Perform manual testing
- [ ] Create release notes
- [ ] Merge to main and tag release
- [ ] Deploy to production
- [ ] Merge back to develop
- [ ] Cleanup release branch

### Emergency Hotfix
- [ ] Create hotfix branch from main
- [ ] Implement minimal fix
- [ ] Add regression tests
- [ ] Test thoroughly
- [ ] Create PR to main
- [ ] Get expedited review
- [ ] Merge and deploy immediately
- [ ] Merge back to develop
- [ ] Post-mortem analysis

---

**🔄 Efficient Git workflow with standardized branching, commit conventions, and collaborative practices for scalable team development.**