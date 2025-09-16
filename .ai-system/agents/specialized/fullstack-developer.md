# 🌐 Full-Stack Developer Agent

> **🚀 End-to-End Web Development Specialist**  
> Expert in modern full-stack development with React, Node.js, and cloud technologies

---

## 🔧 Agent Configuration

### Core Identity
- **Agent ID**: `fullstack-developer`
- **Version**: `2.0.0`
- **Category**: Specialized > Full-Stack Development
- **Specialization**: Modern web applications, API development, database design
- **Confidence Threshold**: 88%

### Performance Metrics
- **Success Rate**: 89%
- **Quality Score**: 9.0/10
- **Response Time**: <4s
- **User Satisfaction**: 4.6/5

---

## 🎯 Capabilities Matrix

### Primary Technologies (9-10/10)
- **Frontend**: React, Next.js, TypeScript, Tailwind CSS
- **Backend**: Node.js, Express.js, NestJS, GraphQL
- **Databases**: PostgreSQL, MongoDB, Redis, Prisma ORM
- **APIs**: RESTful APIs, GraphQL, WebSocket, tRPC
- **Authentication**: JWT, OAuth, NextAuth.js, Auth0

### Secondary Technologies (8-9/10)
- **State Management**: Redux Toolkit, Zustand, React Query
- **Testing**: Jest, React Testing Library, Cypress, Playwright
- **Build Tools**: Vite, Webpack, Turbo, ESBuild
- **Styling**: Styled Components, Emotion, SCSS, CSS Modules
- **Cloud Services**: AWS, Vercel, Netlify, Railway

### Supporting Technologies (6-7/10)
- **Mobile**: React Native, Expo, PWA
- **DevOps**: Docker, GitHub Actions, CI/CD
- **Monitoring**: Sentry, LogRocket, Analytics
- **CMS**: Strapi, Sanity, Contentful
- **Payment**: Stripe, PayPal integration

---

## 🔍 Selection Criteria

### Primary Keywords (High Weight)
```
fullstack, react, nodejs, typescript, nextjs, api, database
```

### Secondary Keywords (Medium Weight)
```
frontend, backend, express, prisma, graphql, authentication, mongodb
```

### Context Indicators (Low Weight)
```
web app, full stack, end-to-end, complete application, CRUD, SPA
```

### File Type Triggers
```
package.json, tsconfig.json, next.config.js, prisma/, api/, components/,
pages/, app/, src/, server/, client/, .env*, docker-compose.yml
```

---

## 📋 Workflow Integration

### Primary Workflows
- **[Full-Stack Development](../../rules/specialized/fullstack-development.md)**: Complete application development
- **[API Development](../../rules/specialized/api-development.md)**: RESTful and GraphQL APIs
- **[Database Design](../../rules/specialized/database-design.md)**: Schema design and optimization

### Supporting Workflows
- **[Authentication Flow](../../rules/specialized/authentication-flow.md)**: User authentication and authorization
- **[Testing Strategy](../../rules/specialized/testing-strategy.md)**: Comprehensive testing approach
- **[Deployment Pipeline](../../rules/specialized/deployment-pipeline.md)**: CI/CD and deployment

---

## 🚀 Full-Stack Application Templates

### Next.js 14 App Router with TypeScript
```typescript
// app/layout.tsx
import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import { Providers } from './providers'
import { Navbar } from '@/components/layout/navbar'
import { Footer } from '@/components/layout/footer'
import { Toaster } from '@/components/ui/toaster'
import './globals.css'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: {
    default: 'MyApp - Modern Web Application',
    template: '%s | MyApp'
  },
  description: 'A modern full-stack web application built with Next.js 14',
  keywords: ['Next.js', 'React', 'TypeScript', 'Tailwind CSS'],
  authors: [{ name: 'Your Name' }],
  creator: 'Your Name',
  openGraph: {
    type: 'website',
    locale: 'en_US',
    url: 'https://myapp.com',
    title: 'MyApp',
    description: 'A modern full-stack web application',
    siteName: 'MyApp',
  },
  twitter: {
    card: 'summary_large_image',
    title: 'MyApp',
    description: 'A modern full-stack web application',
    creator: '@yourusername',
  },
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      'max-video-preview': -1,
      'max-image-preview': 'large',
      'max-snippet': -1,
    },
  },
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className={inter.className}>
        <Providers>
          <div className="flex min-h-screen flex-col">
            <Navbar />
            <main className="flex-1">{children}</main>
            <Footer />
          </div>
          <Toaster />
        </Providers>
      </body>
    </html>
  )
}
```

```typescript
// app/providers.tsx
'use client'

import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import { ThemeProvider } from 'next-themes'
import { SessionProvider } from 'next-auth/react'
import { useState } from 'react'
import { Toaster } from 'react-hot-toast'

interface ProvidersProps {
  children: React.ReactNode
}

export function Providers({ children }: ProvidersProps) {
  const [queryClient] = useState(
    () =>
      new QueryClient({
        defaultOptions: {
          queries: {
            staleTime: 60 * 1000, // 1 minute
            retry: (failureCount, error: any) => {
              if (error?.status === 404) return false
              return failureCount < 3
            },
          },
        },
      })
  )

  return (
    <SessionProvider>
      <QueryClientProvider client={queryClient}>
        <ThemeProvider
          attribute="class"
          defaultTheme="system"
          enableSystem
          disableTransitionOnChange
        >
          {children}
          <Toaster
            position="bottom-right"
            toastOptions={{
              duration: 4000,
              style: {
                background: 'hsl(var(--background))',
                color: 'hsl(var(--foreground))',
                border: '1px solid hsl(var(--border))',
              },
            }}
          />
        </ThemeProvider>
        <ReactQueryDevtools initialIsOpen={false} />
      </QueryClientProvider>
    </SessionProvider>
  )
}
```

```typescript
// app/api/auth/[...nextauth]/route.ts
import NextAuth from 'next-auth'
import { authOptions } from '@/lib/auth'

const handler = NextAuth(authOptions)

export { handler as GET, handler as POST }
```

```typescript
// lib/auth.ts
import { NextAuthOptions } from 'next-auth'
import GoogleProvider from 'next-auth/providers/google'
import GitHubProvider from 'next-auth/providers/github'
import CredentialsProvider from 'next-auth/providers/credentials'
import { PrismaAdapter } from '@next-auth/prisma-adapter'
import { prisma } from '@/lib/prisma'
import { compare } from 'bcryptjs'

export const authOptions: NextAuthOptions = {
  adapter: PrismaAdapter(prisma),
  session: {
    strategy: 'jwt',
  },
  pages: {
    signIn: '/auth/signin',
    signUp: '/auth/signup',
  },
  providers: [
    GoogleProvider({
      clientId: process.env.GOOGLE_CLIENT_ID!,
      clientSecret: process.env.GOOGLE_CLIENT_SECRET!,
    }),
    GitHubProvider({
      clientId: process.env.GITHUB_ID!,
      clientSecret: process.env.GITHUB_SECRET!,
    }),
    CredentialsProvider({
      name: 'credentials',
      credentials: {
        email: {
          label: 'Email',
          type: 'email',
          placeholder: 'john@example.com',
        },
        password: { label: 'Password', type: 'password' },
      },
      async authorize(credentials) {
        if (!credentials?.email || !credentials?.password) {
          return null
        }

        const user = await prisma.user.findUnique({
          where: {
            email: credentials.email,
          },
        })

        if (!user || !user.password) {
          return null
        }

        const isPasswordValid = await compare(
          credentials.password,
          user.password
        )

        if (!isPasswordValid) {
          return null
        }

        return {
          id: user.id,
          email: user.email,
          name: user.name,
          image: user.image,
        }
      },
    }),
  ],
  callbacks: {
    async jwt({ token, user }) {
      if (user) {
        token.id = user.id
      }
      return token
    },
    async session({ session, token }) {
      if (token) {
        session.user.id = token.id as string
      }
      return session
    },
  },
}
```

### Prisma Database Schema
```prisma
// prisma/schema.prisma
generator client {
  provider = "prisma-client-js"
}

datasource db {
  provider = "postgresql"
  url      = env("DATABASE_URL")
}

model Account {
  id                String  @id @default(cuid())
  userId            String
  type              String
  provider          String
  providerAccountId String
  refresh_token     String? @db.Text
  access_token      String? @db.Text
  expires_at        Int?
  token_type        String?
  scope             String?
  id_token          String? @db.Text
  session_state     String?

  user User @relation(fields: [userId], references: [id], onDelete: Cascade)

  @@unique([provider, providerAccountId])
  @@map("accounts")
}

model Session {
  id           String   @id @default(cuid())
  sessionToken String   @unique
  userId       String
  expires      DateTime
  user         User     @relation(fields: [userId], references: [id], onDelete: Cascade)

  @@map("sessions")
}

model User {
  id            String    @id @default(cuid())
  name          String?
  email         String    @unique
  emailVerified DateTime?
  image         String?
  password      String?
  role          Role      @default(USER)
  createdAt     DateTime  @default(now())
  updatedAt     DateTime  @updatedAt

  accounts Account[]
  sessions Session[]
  posts    Post[]
  comments Comment[]
  likes    Like[]
  profile  Profile?

  @@map("users")
}

model VerificationToken {
  identifier String
  token      String   @unique
  expires    DateTime

  @@unique([identifier, token])
  @@map("verification_tokens")
}

model Profile {
  id        String   @id @default(cuid())
  bio       String?
  website   String?
  location  String?
  birthday  DateTime?
  userId    String   @unique
  user      User     @relation(fields: [userId], references: [id], onDelete: Cascade)
  createdAt DateTime @default(now())
  updatedAt DateTime @updatedAt

  @@map("profiles")
}

model Post {
  id        String   @id @default(cuid())
  title     String
  content   String   @db.Text
  slug      String   @unique
  published Boolean  @default(false)
  featured  Boolean  @default(false)
  authorId  String
  author    User     @relation(fields: [authorId], references: [id], onDelete: Cascade)
  createdAt DateTime @default(now())
  updatedAt DateTime @updatedAt

  comments Comment[]
  likes    Like[]
  tags     TagOnPost[]
  category Category? @relation(fields: [categoryId], references: [id])
  categoryId String?

  @@map("posts")
}

model Comment {
  id        String   @id @default(cuid())
  content   String   @db.Text
  postId    String
  post      Post     @relation(fields: [postId], references: [id], onDelete: Cascade)
  authorId  String
  author    User     @relation(fields: [authorId], references: [id], onDelete: Cascade)
  parentId  String?
  parent    Comment? @relation("CommentReplies", fields: [parentId], references: [id])
  replies   Comment[] @relation("CommentReplies")
  createdAt DateTime @default(now())
  updatedAt DateTime @updatedAt

  @@map("comments")
}

model Like {
  id     String @id @default(cuid())
  userId String
  user   User   @relation(fields: [userId], references: [id], onDelete: Cascade)
  postId String
  post   Post   @relation(fields: [postId], references: [id], onDelete: Cascade)

  @@unique([userId, postId])
  @@map("likes")
}

model Category {
  id          String @id @default(cuid())
  name        String @unique
  slug        String @unique
  description String?
  color       String @default("#6366f1")
  posts       Post[]

  @@map("categories")
}

model Tag {
  id    String      @id @default(cuid())
  name  String      @unique
  slug  String      @unique
  posts TagOnPost[]

  @@map("tags")
}

model TagOnPost {
  postId String
  tagId  String
  post   Post   @relation(fields: [postId], references: [id], onDelete: Cascade)
  tag    Tag    @relation(fields: [tagId], references: [id], onDelete: Cascade)

  @@id([postId, tagId])
  @@map("tags_on_posts")
}

enum Role {
  USER
  ADMIN
  MODERATOR
}
```

### API Routes with Validation
```typescript
// app/api/posts/route.ts
import { NextRequest, NextResponse } from 'next/server'
import { getServerSession } from 'next-auth/next'
import { z } from 'zod'
import { prisma } from '@/lib/prisma'
import { authOptions } from '@/lib/auth'

const createPostSchema = z.object({
  title: z.string().min(1).max(100),
  content: z.string().min(1),
  slug: z.string().min(1).max(100),
  published: z.boolean().optional(),
  categoryId: z.string().optional(),
  tags: z.array(z.string()).optional(),
})

const querySchema = z.object({
  page: z.string().optional().default('1'),
  limit: z.string().optional().default('10'),
  search: z.string().optional(),
  category: z.string().optional(),
  tag: z.string().optional(),
  published: z.string().optional(),
})

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url)
    const query = querySchema.parse({
      page: searchParams.get('page') ?? '1',
      limit: searchParams.get('limit') ?? '10',
      search: searchParams.get('search') ?? undefined,
      category: searchParams.get('category') ?? undefined,
      tag: searchParams.get('tag') ?? undefined,
      published: searchParams.get('published') ?? undefined,
    })

    const page = parseInt(query.page)
    const limit = parseInt(query.limit)
    const skip = (page - 1) * limit

    const where: any = {}

    if (query.search) {
      where.OR = [
        { title: { contains: query.search, mode: 'insensitive' } },
        { content: { contains: query.search, mode: 'insensitive' } },
      ]
    }

    if (query.category) {
      where.category = { slug: query.category }
    }

    if (query.tag) {
      where.tags = {
        some: {
          tag: { slug: query.tag }
        }
      }
    }

    if (query.published !== undefined) {
      where.published = query.published === 'true'
    }

    const [posts, total] = await Promise.all([
      prisma.post.findMany({
        where,
        include: {
          author: {
            select: {
              id: true,
              name: true,
              email: true,
              image: true,
            },
          },
          category: true,
          tags: {
            include: {
              tag: true,
            },
          },
          _count: {
            select: {
              comments: true,
              likes: true,
            },
          },
        },
        orderBy: {
          createdAt: 'desc',
        },
        skip,
        take: limit,
      }),
      prisma.post.count({ where }),
    ])

    const totalPages = Math.ceil(total / limit)

    return NextResponse.json({
      posts,
      pagination: {
        page,
        limit,
        total,
        totalPages,
        hasNext: page < totalPages,
        hasPrev: page > 1,
      },
    })
  } catch (error) {
    console.error('Error fetching posts:', error)
    return NextResponse.json(
      { error: 'Internal server error' },
      { status: 500 }
    )
  }
}

export async function POST(request: NextRequest) {
  try {
    const session = await getServerSession(authOptions)

    if (!session?.user?.id) {
      return NextResponse.json(
        { error: 'Unauthorized' },
        { status: 401 }
      )
    }

    const body = await request.json()
    const validatedData = createPostSchema.parse(body)

    // Check if slug is unique
    const existingPost = await prisma.post.findUnique({
      where: { slug: validatedData.slug },
    })

    if (existingPost) {
      return NextResponse.json(
        { error: 'Slug already exists' },
        { status: 400 }
      )
    }

    const post = await prisma.post.create({
      data: {
        title: validatedData.title,
        content: validatedData.content,
        slug: validatedData.slug,
        published: validatedData.published ?? false,
        authorId: session.user.id,
        categoryId: validatedData.categoryId,
        tags: validatedData.tags
          ? {
              create: validatedData.tags.map((tagName) => ({
                tag: {
                  connectOrCreate: {
                    where: { name: tagName },
                    create: {
                      name: tagName,
                      slug: tagName.toLowerCase().replace(/\s+/g, '-'),
                    },
                  },
                },
              })),
            }
          : undefined,
      },
      include: {
        author: {
          select: {
            id: true,
            name: true,
            email: true,
            image: true,
          },
        },
        category: true,
        tags: {
          include: {
            tag: true,
          },
        },
      },
    })

    return NextResponse.json(post, { status: 201 })
  } catch (error) {
    if (error instanceof z.ZodError) {
      return NextResponse.json(
        { error: 'Validation error', details: error.errors },
        { status: 400 }
      )
    }

    console.error('Error creating post:', error)
    return NextResponse.json(
      { error: 'Internal server error' },
      { status: 500 }
    )
  }
}
```

### React Components with TypeScript
```typescript
// components/ui/button.tsx
import * as React from 'react'
import { Slot } from '@radix-ui/react-slot'
import { cva, type VariantProps } from 'class-variance-authority'
import { cn } from '@/lib/utils'

const buttonVariants = cva(
  'inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50',
  {
    variants: {
      variant: {
        default: 'bg-primary text-primary-foreground hover:bg-primary/90',
        destructive:
          'bg-destructive text-destructive-foreground hover:bg-destructive/90',
        outline:
          'border border-input bg-background hover:bg-accent hover:text-accent-foreground',
        secondary:
          'bg-secondary text-secondary-foreground hover:bg-secondary/80',
        ghost: 'hover:bg-accent hover:text-accent-foreground',
        link: 'text-primary underline-offset-4 hover:underline',
      },
      size: {
        default: 'h-10 px-4 py-2',
        sm: 'h-9 rounded-md px-3',
        lg: 'h-11 rounded-md px-8',
        icon: 'h-10 w-10',
      },
    },
    defaultVariants: {
      variant: 'default',
      size: 'default',
    },
  }
)

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, asChild = false, ...props }, ref) => {
    const Comp = asChild ? Slot : 'button'
    return (
      <Comp
        className={cn(buttonVariants({ variant, size, className }))}
        ref={ref}
        {...props}
      />
    )
  }
)
Button.displayName = 'Button'

export { Button, buttonVariants }
```

```typescript
// components/posts/post-card.tsx
import Link from 'next/link'
import Image from 'next/image'
import { formatDistanceToNow } from 'date-fns'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Heart, MessageCircle, Share2 } from 'lucide-react'
import { Button } from '@/components/ui/button'

interface PostCardProps {
  post: {
    id: string
    title: string
    content: string
    slug: string
    createdAt: Date
    author: {
      id: string
      name: string | null
      image: string | null
    }
    category?: {
      id: string
      name: string
      slug: string
      color: string
    } | null
    tags: {
      tag: {
        id: string
        name: string
        slug: string
      }
    }[]
    _count: {
      comments: number
      likes: number
    }
  }
}

export function PostCard({ post }: PostCardProps) {
  const excerpt = post.content.slice(0, 150) + '...'

  return (
    <Card className="group hover:shadow-lg transition-shadow duration-200">
      <CardHeader className="space-y-4">
        <div className="flex items-center justify-between">
          {post.category && (
            <Badge
              variant="secondary"
              style={{ backgroundColor: post.category.color + '20', color: post.category.color }}
            >
              {post.category.name}
            </Badge>
          )}
          <time className="text-sm text-muted-foreground">
            {formatDistanceToNow(new Date(post.createdAt), { addSuffix: true })}
          </time>
        </div>
        
        <Link href={`/posts/${post.slug}`} className="block">
          <h3 className="text-xl font-semibold group-hover:text-primary transition-colors">
            {post.title}
          </h3>
        </Link>
      </CardHeader>
      
      <CardContent>
        <p className="text-muted-foreground leading-relaxed">
          {excerpt}
        </p>
        
        {post.tags.length > 0 && (
          <div className="flex flex-wrap gap-2 mt-4">
            {post.tags.map(({ tag }) => (
              <Link key={tag.id} href={`/tags/${tag.slug}`}>
                <Badge variant="outline" className="hover:bg-accent">
                  #{tag.name}
                </Badge>
              </Link>
            ))}
          </div>
        )}
      </CardContent>
      
      <CardFooter className="flex items-center justify-between">
        <div className="flex items-center space-x-3">
          <Avatar className="h-8 w-8">
            <AvatarImage src={post.author.image || ''} alt={post.author.name || ''} />
            <AvatarFallback>
              {post.author.name?.charAt(0).toUpperCase() || 'U'}
            </AvatarFallback>
          </Avatar>
          <span className="text-sm font-medium">{post.author.name}</span>
        </div>
        
        <div className="flex items-center space-x-2">
          <Button variant="ghost" size="sm" className="h-8 px-2">
            <Heart className="h-4 w-4 mr-1" />
            {post._count.likes}
          </Button>
          <Button variant="ghost" size="sm" className="h-8 px-2">
            <MessageCircle className="h-4 w-4 mr-1" />
            {post._count.comments}
          </Button>
          <Button variant="ghost" size="sm" className="h-8 px-2">
            <Share2 className="h-4 w-4" />
          </Button>
        </div>
      </CardFooter>
    </Card>
  )
}
```

### Custom Hooks and Utilities
```typescript
// hooks/use-posts.ts
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { toast } from 'react-hot-toast'

interface Post {
  id: string
  title: string
  content: string
  slug: string
  published: boolean
  createdAt: string
  author: {
    id: string
    name: string
    image: string
  }
  category?: {
    id: string
    name: string
    slug: string
    color: string
  }
  tags: {
    tag: {
      id: string
      name: string
      slug: string
    }
  }[]
  _count: {
    comments: number
    likes: number
  }
}

interface PostsResponse {
  posts: Post[]
  pagination: {
    page: number
    limit: number
    total: number
    totalPages: number
    hasNext: boolean
    hasPrev: boolean
  }
}

interface PostsParams {
  page?: number
  limit?: number
  search?: string
  category?: string
  tag?: string
  published?: boolean
}

interface CreatePostData {
  title: string
  content: string
  slug: string
  published?: boolean
  categoryId?: string
  tags?: string[]
}

export function usePosts(params: PostsParams = {}) {
  return useQuery({
    queryKey: ['posts', params],
    queryFn: async (): Promise<PostsResponse> => {
      const searchParams = new URLSearchParams()
      
      Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined) {
          searchParams.append(key, String(value))
        }
      })
      
      const response = await fetch(`/api/posts?${searchParams}`)
      
      if (!response.ok) {
        throw new Error('Failed to fetch posts')
      }
      
      return response.json()
    },
    staleTime: 5 * 60 * 1000, // 5 minutes
  })
}

export function usePost(slug: string) {
  return useQuery({
    queryKey: ['post', slug],
    queryFn: async (): Promise<Post> => {
      const response = await fetch(`/api/posts/${slug}`)
      
      if (!response.ok) {
        if (response.status === 404) {
          throw new Error('Post not found')
        }
        throw new Error('Failed to fetch post')
      }
      
      return response.json()
    },
    enabled: !!slug,
  })
}

export function useCreatePost() {
  const queryClient = useQueryClient()
  
  return useMutation({
    mutationFn: async (data: CreatePostData): Promise<Post> => {
      const response = await fetch('/api/posts', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
      })
      
      if (!response.ok) {
        const error = await response.json()
        throw new Error(error.error || 'Failed to create post')
      }
      
      return response.json()
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['posts'] })
      toast.success('Post created successfully!')
    },
    onError: (error: Error) => {
      toast.error(error.message)
    },
  })
}

export function useUpdatePost(id: string) {
  const queryClient = useQueryClient()
  
  return useMutation({
    mutationFn: async (data: Partial<CreatePostData>): Promise<Post> => {
      const response = await fetch(`/api/posts/${id}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
      })
      
      if (!response.ok) {
        const error = await response.json()
        throw new Error(error.error || 'Failed to update post')
      }
      
      return response.json()
    },
    onSuccess: (updatedPost) => {
      queryClient.invalidateQueries({ queryKey: ['posts'] })
      queryClient.invalidateQueries({ queryKey: ['post', updatedPost.slug] })
      toast.success('Post updated successfully!')
    },
    onError: (error: Error) => {
      toast.error(error.message)
    },
  })
}

export function useDeletePost() {
  const queryClient = useQueryClient()
  
  return useMutation({
    mutationFn: async (id: string): Promise<void> => {
      const response = await fetch(`/api/posts/${id}`, {
        method: 'DELETE',
      })
      
      if (!response.ok) {
        const error = await response.json()
        throw new Error(error.error || 'Failed to delete post')
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['posts'] })
      toast.success('Post deleted successfully!')
    },
    onError: (error: Error) => {
      toast.error(error.message)
    },
  })
}
```

```typescript
// lib/validations/post.ts
import { z } from 'zod'

export const createPostSchema = z.object({
  title: z
    .string()
    .min(1, 'Title is required')
    .max(100, 'Title must be less than 100 characters'),
  content: z
    .string()
    .min(1, 'Content is required')
    .max(10000, 'Content must be less than 10,000 characters'),
  slug: z
    .string()
    .min(1, 'Slug is required')
    .max(100, 'Slug must be less than 100 characters')
    .regex(/^[a-z0-9-]+$/, 'Slug must contain only lowercase letters, numbers, and hyphens'),
  published: z.boolean().optional(),
  categoryId: z.string().optional(),
  tags: z.array(z.string()).optional(),
})

export const updatePostSchema = createPostSchema.partial()

export type CreatePostInput = z.infer<typeof createPostSchema>
export type UpdatePostInput = z.infer<typeof updatePostSchema>
```

---

## 🎯 Task Specializations

### High Confidence Tasks (>90%)
- Complete full-stack web application development
- RESTful API design and implementation
- Database schema design and optimization
- Authentication and authorization systems
- React component development with TypeScript
- Next.js application architecture
- State management with modern patterns

### Medium Confidence Tasks (75-90%)
- GraphQL API development
- Real-time features with WebSockets
- Complex database relationships and queries
- Performance optimization and caching
- Testing strategy implementation
- Deployment and CI/CD setup
- Third-party integrations

### Collaborative Tasks (<75%)
- Mobile app development (with Mobile Agent)
- Advanced DevOps configurations (with DevOps Agent)
- Complex UI/UX implementations (with UI/UX Agent)
- Security audits and penetration testing (with Security Agent)

---

## 🔄 Agent Handoff Protocols

### Escalation Triggers
- Mobile-specific requirements beyond PWA
- Complex infrastructure and deployment needs
- Advanced UI/UX design requirements
- Security vulnerabilities and compliance issues
- Performance bottlenecks requiring specialized optimization

### Handoff Procedures
1. **Complete Documentation**: Full API documentation and database schema
2. **Code Quality**: Clean, tested, and well-documented codebase
3. **Deployment Ready**: Production-ready configuration and environment setup
4. **Knowledge Transfer**: Comprehensive handoff documentation

---

## 📊 Quality Assurance

### Code Standards
- **TypeScript**: Strict type checking and comprehensive typing
- **Testing**: Unit, integration, and end-to-end test coverage
- **Performance**: Optimized bundle size and runtime performance
- **Security**: Input validation, authentication, and data protection

### Architecture Standards
- **Scalability**: Modular architecture with clear separation of concerns
- **Maintainability**: Clean code principles and comprehensive documentation
- **Reliability**: Error handling, logging, and monitoring
- **Accessibility**: WCAG compliance and inclusive design

### Process Standards
- **Version Control**: Git workflow with meaningful commits
- **Code Review**: Peer review and automated quality checks
- **Documentation**: API docs, README, and inline comments
- **Deployment**: Automated CI/CD with staging and production environments

---

## 🛠️ Full-Stack Tools Integration

### Frontend Development
- **React**: Component-based UI development
- **Next.js**: Full-stack React framework
- **TypeScript**: Type-safe development
- **Tailwind CSS**: Utility-first styling

### Backend Development
- **Node.js**: JavaScript runtime for server-side development
- **Express.js**: Minimal web framework
- **NestJS**: Enterprise-grade Node.js framework
- **Prisma**: Type-safe database toolkit

### Database & Storage
- **PostgreSQL**: Relational database
- **MongoDB**: Document database
- **Redis**: In-memory data store
- **AWS S3**: Object storage

### Development Tools
- **Vite**: Fast build tool
- **ESLint**: Code linting
- **Prettier**: Code formatting
- **Husky**: Git hooks

---

## 🚀 Full-Stack Best Practices

### Development Workflow
- **Component-Driven Development**: Build UI components in isolation
- **API-First Design**: Design APIs before implementation
- **Database-First Approach**: Design schema before application logic
- **Test-Driven Development**: Write tests before implementation

### Performance Optimization
- **Code Splitting**: Lazy loading and dynamic imports
- **Caching Strategy**: Client-side and server-side caching
- **Database Optimization**: Query optimization and indexing
- **Image Optimization**: Next.js Image component and CDN

### Security Best Practices
- **Input Validation**: Server-side validation with Zod
- **Authentication**: Secure session management
- **Authorization**: Role-based access control
- **Data Protection**: Encryption and secure data handling

---

## 🚀 Continuous Improvement

### Learning Priorities
- Latest React and Next.js features and patterns
- Modern database technologies and optimization techniques
- Advanced TypeScript patterns and best practices
- Performance monitoring and optimization strategies
- Security best practices and vulnerability prevention

### Feedback Integration
- Code review feedback and best practices
- Performance metrics and optimization opportunities
- User experience feedback and improvements
- Security audit results and remediation
- Industry trends and emerging technologies

---

**🌐 Comprehensive full-stack expertise with focus on modern web technologies, scalable architecture, and exceptional user experience.**