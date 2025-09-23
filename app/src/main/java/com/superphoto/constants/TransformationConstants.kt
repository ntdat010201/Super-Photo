package com.superphoto.constants

import java.io.Serializable

/**
 * TransformationConstants - Comprehensive AI Photo Transformations
 * Ported from AI Photo reference with 120+ transformations organized by categories
 */
object TransformationConstants {

    // Featured Categories
    const val CATEGORY_FEATURED = "featured"
    const val CATEGORY_BACKGROUND = "background"
    const val CATEGORY_FACE = "face"
    const val CATEGORY_STYLE = "style"
    const val CATEGORY_ENHANCE = "enhance"
    const val CATEGORY_CREATIVE = "creative"
    const val CATEGORY_PROFESSIONAL = "professional"
    const val CATEGORY_VINTAGE = "vintage"

    data class Transformation(
        val id: String,
        val name: String,
        val description: String,
        val category: String,
        val icon: String,
        val isPopular: Boolean = false,
        val isPremium: Boolean = false,
        val processingTime: String = "Fast",
        val geminiPrompt: String
    ) : Serializable

    // Featured Transformations (Most Popular)
    val FEATURED_TRANSFORMATIONS = listOf(
        Transformation(
            id = "celebrity_photo",
            name = "Celebrity Photo",
            description = "Transform into your favorite celebrity",
            category = CATEGORY_FEATURED,
            icon = "⭐",
            isPopular = true,
            geminiPrompt = "Transform this person to look like a celebrity while maintaining facial structure"
        ),
        Transformation(
            id = "background_remover",
            name = "Background Remover",
            description = "Remove background instantly",
            category = CATEGORY_FEATURED,
            icon = "🖼️",
            isPopular = true,
            geminiPrompt = "Remove the background from this image, keep only the main subject"
        ),
        Transformation(
            id = "face_swap",
            name = "Face Swap",
            description = "Swap faces between photos",
            category = CATEGORY_FEATURED,
            icon = "🔄",
            isPopular = true,
            geminiPrompt = "Swap the faces in these two images naturally and realistically"
        ),
        Transformation(
            id = "ai_enhance",
            name = "AI Enhance",
            description = "Enhance photo quality with AI",
            category = CATEGORY_FEATURED,
            icon = "✨",
            isPopular = true,
            geminiPrompt = "Enhance this image quality, improve sharpness, colors, and overall appearance"
        ),
        Transformation(
            id = "style_transfer",
            name = "Style Transfer",
            description = "Apply artistic styles",
            category = CATEGORY_FEATURED,
            icon = "🎨",
            isPopular = true,
            geminiPrompt = "Apply artistic style transfer to this image, make it look like a painting"
        )
    )

    // Background Transformations
    val BACKGROUND_TRANSFORMATIONS = listOf(
        Transformation(
            id = "bg_beach",
            name = "Beach Paradise",
            description = "Place yourself on a tropical beach",
            category = CATEGORY_BACKGROUND,
            icon = "🏖️",
            geminiPrompt = "Replace background with a beautiful tropical beach scene"
        ),
        Transformation(
            id = "bg_city",
            name = "City Skyline",
            description = "Urban cityscape background",
            category = CATEGORY_BACKGROUND,
            icon = "🏙️",
            geminiPrompt = "Replace background with a modern city skyline"
        ),
        Transformation(
            id = "bg_forest",
            name = "Enchanted Forest",
            description = "Mystical forest setting",
            category = CATEGORY_BACKGROUND,
            icon = "🌲",
            geminiPrompt = "Replace background with an enchanted forest scene"
        ),
        Transformation(
            id = "bg_space",
            name = "Space Adventure",
            description = "Cosmic space background",
            category = CATEGORY_BACKGROUND,
            icon = "🚀",
            geminiPrompt = "Replace background with a cosmic space scene with stars and galaxies"
        ),
        Transformation(
            id = "bg_mountain",
            name = "Mountain Peak",
            description = "Majestic mountain landscape",
            category = CATEGORY_BACKGROUND,
            icon = "⛰️",
            geminiPrompt = "Replace background with majestic mountain peaks"
        ),
        Transformation(
            id = "bg_underwater",
            name = "Underwater World",
            description = "Deep ocean environment",
            category = CATEGORY_BACKGROUND,
            icon = "🌊",
            geminiPrompt = "Replace background with underwater ocean scene with fish and coral"
        ),
        Transformation(
            id = "bg_desert",
            name = "Desert Oasis",
            description = "Sandy desert landscape",
            category = CATEGORY_BACKGROUND,
            icon = "🏜️",
            geminiPrompt = "Replace background with a desert oasis scene"
        ),
        Transformation(
            id = "bg_castle",
            name = "Medieval Castle",
            description = "Ancient castle setting",
            category = CATEGORY_BACKGROUND,
            icon = "🏰",
            geminiPrompt = "Replace background with a medieval castle scene"
        ),
        Transformation(
            id = "bg_garden",
            name = "Flower Garden",
            description = "Beautiful flower garden",
            category = CATEGORY_BACKGROUND,
            icon = "🌺",
            geminiPrompt = "Replace background with a colorful flower garden"
        ),
        Transformation(
            id = "bg_aurora",
            name = "Northern Lights",
            description = "Aurora borealis background",
            category = CATEGORY_BACKGROUND,
            icon = "🌌",
            geminiPrompt = "Replace background with northern lights aurora borealis"
        ),
        Transformation(
            id = "bg_library",
            name = "Ancient Library",
            description = "Scholarly library setting",
            category = CATEGORY_BACKGROUND,
            icon = "📚",
            geminiPrompt = "Replace background with an ancient library with books"
        ),
        Transformation(
            id = "bg_cafe",
            name = "Cozy Cafe",
            description = "Warm coffee shop atmosphere",
            category = CATEGORY_BACKGROUND,
            icon = "☕",
            geminiPrompt = "Replace background with a cozy coffee shop interior"
        ),
        Transformation(
            id = "bg_studio",
            name = "Photo Studio",
            description = "Professional studio backdrop",
            category = CATEGORY_BACKGROUND,
            icon = "📸",
            geminiPrompt = "Replace background with a professional photo studio backdrop"
        ),
        Transformation(
            id = "bg_neon",
            name = "Neon City",
            description = "Cyberpunk neon lights",
            category = CATEGORY_BACKGROUND,
            icon = "🌃",
            geminiPrompt = "Replace background with cyberpunk neon city scene"
        ),
        Transformation(
            id = "bg_vintage",
            name = "Vintage Room",
            description = "Retro vintage interior",
            category = CATEGORY_BACKGROUND,
            icon = "🕰️",
            geminiPrompt = "Replace background with vintage retro room interior"
        )
    )

    // Face Transformations
    val FACE_TRANSFORMATIONS = listOf(
        Transformation(
            id = "face_age_young",
            name = "Youth Filter",
            description = "Make yourself look younger",
            category = CATEGORY_FACE,
            icon = "👶",
            geminiPrompt = "Make this person look younger, reduce wrinkles and age signs"
        ),
        Transformation(
            id = "face_age_old",
            name = "Age Progression",
            description = "See yourself in the future",
            category = CATEGORY_FACE,
            icon = "👴",
            geminiPrompt = "Age this person to show how they might look when older"
        ),
        Transformation(
            id = "face_gender_swap",
            name = "Gender Swap",
            description = "Switch gender appearance",
            category = CATEGORY_FACE,
            icon = "⚧️",
            geminiPrompt = "Transform this person to opposite gender while keeping identity"
        ),
        Transformation(
            id = "face_smile",
            name = "Perfect Smile",
            description = "Add a natural smile",
            category = CATEGORY_FACE,
            icon = "😊",
            geminiPrompt = "Add a natural, beautiful smile to this person's face"
        ),
        Transformation(
            id = "face_makeup",
            name = "AI Makeup",
            description = "Apply professional makeup",
            category = CATEGORY_FACE,
            icon = "💄",
            geminiPrompt = "Apply professional makeup to enhance this person's features"
        ),
        Transformation(
            id = "face_beard",
            name = "Beard Generator",
            description = "Add different beard styles",
            category = CATEGORY_FACE,
            icon = "🧔",
            geminiPrompt = "Add a stylish beard to this person's face"
        ),
        Transformation(
            id = "face_hair_color",
            name = "Hair Color Change",
            description = "Try different hair colors",
            category = CATEGORY_FACE,
            icon = "💇",
            geminiPrompt = "Change the hair color of this person to a different attractive color"
        ),
        Transformation(
            id = "face_glasses",
            name = "Virtual Glasses",
            description = "Try on different glasses",
            category = CATEGORY_FACE,
            icon = "👓",
            geminiPrompt = "Add stylish glasses to this person's face"
        ),
        Transformation(
            id = "face_expression",
            name = "Expression Change",
            description = "Modify facial expressions",
            category = CATEGORY_FACE,
            icon = "😮",
            geminiPrompt = "Change the facial expression to be more expressive and engaging"
        ),
        Transformation(
            id = "face_skin_smooth",
            name = "Skin Smoothing",
            description = "Perfect skin texture",
            category = CATEGORY_FACE,
            icon = "✨",
            geminiPrompt = "Smooth and perfect the skin texture while keeping it natural"
        )
    )

    // Style Transformations
    val STYLE_TRANSFORMATIONS = listOf(
        Transformation(
            id = "style_anime",
            name = "Anime Style",
            description = "Transform into anime character",
            category = CATEGORY_STYLE,
            icon = "🎌",
            isPopular = true,
            geminiPrompt = "Transform this image into anime/manga art style"
        ),
        Transformation(
            id = "style_cartoon",
            name = "Cartoon Style",
            description = "Cartoon character transformation",
            category = CATEGORY_STYLE,
            icon = "🎭",
            geminiPrompt = "Transform this image into cartoon style illustration"
        ),
        Transformation(
            id = "style_oil_painting",
            name = "Oil Painting",
            description = "Classic oil painting style",
            category = CATEGORY_STYLE,
            icon = "🖼️",
            geminiPrompt = "Transform this image into an oil painting masterpiece"
        ),
        Transformation(
            id = "style_watercolor",
            name = "Watercolor",
            description = "Soft watercolor painting",
            category = CATEGORY_STYLE,
            icon = "🎨",
            geminiPrompt = "Transform this image into watercolor painting style"
        ),
        Transformation(
            id = "style_pencil",
            name = "Pencil Sketch",
            description = "Hand-drawn pencil art",
            category = CATEGORY_STYLE,
            icon = "✏️",
            geminiPrompt = "Transform this image into detailed pencil sketch"
        ),
        Transformation(
            id = "style_pop_art",
            name = "Pop Art",
            description = "Vibrant pop art style",
            category = CATEGORY_STYLE,
            icon = "🎪",
            geminiPrompt = "Transform this image into pop art style with vibrant colors"
        ),
        Transformation(
            id = "style_cyberpunk",
            name = "Cyberpunk",
            description = "Futuristic cyberpunk aesthetic",
            category = CATEGORY_STYLE,
            icon = "🤖",
            geminiPrompt = "Transform this image into cyberpunk futuristic style"
        ),
        Transformation(
            id = "style_gothic",
            name = "Gothic Art",
            description = "Dark gothic style",
            category = CATEGORY_STYLE,
            icon = "🦇",
            geminiPrompt = "Transform this image into gothic art style"
        ),
        Transformation(
            id = "style_impressionist",
            name = "Impressionist",
            description = "Impressionist painting style",
            category = CATEGORY_STYLE,
            icon = "🌅",
            geminiPrompt = "Transform this image into impressionist painting style"
        ),
        Transformation(
            id = "style_pixel_art",
            name = "Pixel Art",
            description = "Retro pixel art style",
            category = CATEGORY_STYLE,
            icon = "🎮",
            geminiPrompt = "Transform this image into pixel art style"
        )
    )

    // Enhancement Transformations
    val ENHANCE_TRANSFORMATIONS = listOf(
        Transformation(
            id = "enhance_hdr",
            name = "HDR Enhancement",
            description = "High dynamic range processing",
            category = CATEGORY_ENHANCE,
            icon = "🌈",
            geminiPrompt = "Apply HDR enhancement to improve dynamic range and colors"
        ),
        Transformation(
            id = "enhance_sharpen",
            name = "Smart Sharpen",
            description = "Intelligent sharpening",
            category = CATEGORY_ENHANCE,
            icon = "🔍",
            geminiPrompt = "Apply smart sharpening to enhance image details"
        ),
        Transformation(
            id = "enhance_denoise",
            name = "Noise Reduction",
            description = "Remove image noise",
            category = CATEGORY_ENHANCE,
            icon = "🧹",
            geminiPrompt = "Remove noise and grain from this image while preserving details"
        ),
        Transformation(
            id = "enhance_upscale",
            name = "AI Upscale",
            description = "Increase resolution with AI",
            category = CATEGORY_ENHANCE,
            icon = "📈",
            isPremium = true,
            geminiPrompt = "Upscale this image to higher resolution using AI enhancement"
        ),
        Transformation(
            id = "enhance_colorize",
            name = "AI Colorize",
            description = "Add color to black & white",
            category = CATEGORY_ENHANCE,
            icon = "🌈",
            geminiPrompt = "Colorize this black and white image with realistic colors"
        ),
        Transformation(
            id = "enhance_restore",
            name = "Photo Restoration",
            description = "Restore old damaged photos",
            category = CATEGORY_ENHANCE,
            icon = "🔧",
            isPremium = true,
            geminiPrompt = "Restore this old or damaged photo, fix scratches and improve quality"
        ),
        Transformation(
            id = "enhance_lighting",
            name = "Lighting Fix",
            description = "Improve lighting conditions",
            category = CATEGORY_ENHANCE,
            icon = "💡",
            geminiPrompt = "Improve the lighting and exposure of this image"
        ),
        Transformation(
            id = "enhance_contrast",
            name = "Smart Contrast",
            description = "Optimize contrast levels",
            category = CATEGORY_ENHANCE,
            icon = "⚡",
            geminiPrompt = "Optimize contrast and brightness for better visual impact"
        ),
        Transformation(
            id = "enhance_saturation",
            name = "Color Boost",
            description = "Enhance color vibrancy",
            category = CATEGORY_ENHANCE,
            icon = "🎨",
            geminiPrompt = "Boost color saturation and vibrancy naturally"
        ),
        Transformation(
            id = "enhance_clarity",
            name = "Clarity Boost",
            description = "Improve overall clarity",
            category = CATEGORY_ENHANCE,
            icon = "✨",
            geminiPrompt = "Improve overall image clarity and definition"
        )
    )

    // Creative Transformations
    val CREATIVE_TRANSFORMATIONS = listOf(
        Transformation(
            id = "creative_double_exposure",
            name = "Double Exposure",
            description = "Artistic double exposure effect",
            category = CATEGORY_CREATIVE,
            icon = "👥",
            geminiPrompt = "Create artistic double exposure effect combining two images"
        ),
        Transformation(
            id = "creative_mirror",
            name = "Mirror Effect",
            description = "Symmetrical mirror reflection",
            category = CATEGORY_CREATIVE,
            icon = "🪞",
            geminiPrompt = "Create mirror effect with symmetrical reflection"
        ),
        Transformation(
            id = "creative_kaleidoscope",
            name = "Kaleidoscope",
            description = "Kaleidoscope pattern effect",
            category = CATEGORY_CREATIVE,
            icon = "🔮",
            geminiPrompt = "Transform image into kaleidoscope pattern"
        ),
        Transformation(
            id = "creative_mosaic",
            name = "Photo Mosaic",
            description = "Create mosaic from photos",
            category = CATEGORY_CREATIVE,
            icon = "🧩",
            geminiPrompt = "Create photo mosaic effect using multiple small images"
        ),
        Transformation(
            id = "creative_collage",
            name = "AI Collage",
            description = "Intelligent photo collage",
            category = CATEGORY_CREATIVE,
            icon = "📷",
            geminiPrompt = "Create artistic collage combining multiple photos"
        ),
        Transformation(
            id = "creative_surreal",
            name = "Surreal Art",
            description = "Surrealistic transformation",
            category = CATEGORY_CREATIVE,
            icon = "🌀",
            geminiPrompt = "Transform into surreal artistic composition"
        ),
        Transformation(
            id = "creative_fractal",
            name = "Fractal Art",
            description = "Mathematical fractal patterns",
            category = CATEGORY_CREATIVE,
            icon = "🌸",
            geminiPrompt = "Apply fractal art patterns to the image"
        ),
        Transformation(
            id = "creative_glitch",
            name = "Glitch Effect",
            description = "Digital glitch aesthetic",
            category = CATEGORY_CREATIVE,
            icon = "📺",
            geminiPrompt = "Apply digital glitch effect for modern aesthetic"
        ),
        Transformation(
            id = "creative_hologram",
            name = "Hologram Effect",
            description = "Futuristic hologram look",
            category = CATEGORY_CREATIVE,
            icon = "🔮",
            geminiPrompt = "Transform into futuristic hologram effect"
        ),
        Transformation(
            id = "creative_neon",
            name = "Neon Glow",
            description = "Vibrant neon lighting",
            category = CATEGORY_CREATIVE,
            icon = "💫",
            geminiPrompt = "Add vibrant neon glow effects to the image"
        )
    )

    // Professional Transformations
    val PROFESSIONAL_TRANSFORMATIONS = listOf(
        Transformation(
            id = "pro_headshot",
            name = "Professional Headshot",
            description = "Corporate headshot style",
            category = CATEGORY_PROFESSIONAL,
            icon = "👔",
            geminiPrompt = "Transform into professional corporate headshot"
        ),
        Transformation(
            id = "pro_linkedin",
            name = "LinkedIn Profile",
            description = "Perfect LinkedIn photo",
            category = CATEGORY_PROFESSIONAL,
            icon = "💼",
            geminiPrompt = "Optimize for professional LinkedIn profile photo"
        ),
        Transformation(
            id = "pro_passport",
            name = "Passport Photo",
            description = "Official document photo",
            category = CATEGORY_PROFESSIONAL,
            icon = "📋",
            geminiPrompt = "Format as official passport/ID photo with proper background"
        ),
        Transformation(
            id = "pro_resume",
            name = "Resume Photo",
            description = "Professional resume picture",
            category = CATEGORY_PROFESSIONAL,
            icon = "📄",
            geminiPrompt = "Create professional resume photo with clean background"
        ),
        Transformation(
            id = "pro_business",
            name = "Business Portrait",
            description = "Executive business portrait",
            category = CATEGORY_PROFESSIONAL,
            icon = "🏢",
            geminiPrompt = "Transform into executive business portrait"
        ),
        Transformation(
            id = "pro_academic",
            name = "Academic Photo",
            description = "Scholarly professional look",
            category = CATEGORY_PROFESSIONAL,
            icon = "🎓",
            geminiPrompt = "Create academic professional photo for scholarly purposes"
        ),
        Transformation(
            id = "pro_medical",
            name = "Medical Professional",
            description = "Healthcare professional look",
            category = CATEGORY_PROFESSIONAL,
            icon = "⚕️",
            geminiPrompt = "Transform into medical professional appearance"
        ),
        Transformation(
            id = "pro_lawyer",
            name = "Legal Professional",
            description = "Attorney professional style",
            category = CATEGORY_PROFESSIONAL,
            icon = "⚖️",
            geminiPrompt = "Create legal professional appearance for attorney profile"
        ),
        Transformation(
            id = "pro_teacher",
            name = "Educator Style",
            description = "Professional educator look",
            category = CATEGORY_PROFESSIONAL,
            icon = "👨‍🏫",
            geminiPrompt = "Transform into professional educator appearance"
        ),
        Transformation(
            id = "pro_consultant",
            name = "Consultant Style",
            description = "Professional consultant look",
            category = CATEGORY_PROFESSIONAL,
            icon = "📊",
            geminiPrompt = "Create professional consultant appearance"
        )
    )

    // Vintage Transformations
    val VINTAGE_TRANSFORMATIONS = listOf(
        Transformation(
            id = "vintage_1920s",
            name = "1920s Glamour",
            description = "Roaring twenties style",
            category = CATEGORY_VINTAGE,
            icon = "🎭",
            geminiPrompt = "Transform into 1920s glamour style with period appropriate look"
        ),
        Transformation(
            id = "vintage_1950s",
            name = "1950s Classic",
            description = "Mid-century classic look",
            category = CATEGORY_VINTAGE,
            icon = "🕺",
            geminiPrompt = "Transform into 1950s classic style"
        ),
        Transformation(
            id = "vintage_1960s",
            name = "1960s Mod",
            description = "Swinging sixties style",
            category = CATEGORY_VINTAGE,
            icon = "🌈",
            geminiPrompt = "Transform into 1960s mod style"
        ),
        Transformation(
            id = "vintage_1970s",
            name = "1970s Disco",
            description = "Groovy disco era",
            category = CATEGORY_VINTAGE,
            icon = "🕺",
            geminiPrompt = "Transform into 1970s disco era style"
        ),
        Transformation(
            id = "vintage_1980s",
            name = "1980s Retro",
            description = "Neon eighties vibe",
            category = CATEGORY_VINTAGE,
            icon = "📼",
            geminiPrompt = "Transform into 1980s retro style with neon colors"
        ),
        Transformation(
            id = "vintage_sepia",
            name = "Sepia Tone",
            description = "Classic sepia photography",
            category = CATEGORY_VINTAGE,
            icon = "📸",
            geminiPrompt = "Apply classic sepia tone effect"
        ),
        Transformation(
            id = "vintage_film",
            name = "Film Photography",
            description = "Analog film camera look",
            category = CATEGORY_VINTAGE,
            icon = "🎞️",
            geminiPrompt = "Apply vintage film photography aesthetic"
        ),
        Transformation(
            id = "vintage_polaroid",
            name = "Polaroid Style",
            description = "Instant camera aesthetic",
            category = CATEGORY_VINTAGE,
            icon = "📷",
            geminiPrompt = "Transform into polaroid instant photo style"
        ),
        Transformation(
            id = "vintage_daguerreotype",
            name = "Daguerreotype",
            description = "Early photography style",
            category = CATEGORY_VINTAGE,
            icon = "🖼️",
            geminiPrompt = "Transform into daguerreotype early photography style"
        ),
        Transformation(
            id = "vintage_tintype",
            name = "Tintype Photo",
            description = "Civil war era photography",
            category = CATEGORY_VINTAGE,
            icon = "⚔️",
            geminiPrompt = "Transform into tintype civil war era photography"
        )
    )

    // All transformations combined
    val ALL_TRANSFORMATIONS = FEATURED_TRANSFORMATIONS + 
                             BACKGROUND_TRANSFORMATIONS + 
                             FACE_TRANSFORMATIONS + 
                             STYLE_TRANSFORMATIONS + 
                             ENHANCE_TRANSFORMATIONS + 
                             CREATIVE_TRANSFORMATIONS + 
                             PROFESSIONAL_TRANSFORMATIONS + 
                             VINTAGE_TRANSFORMATIONS

    // Category mappings
    val TRANSFORMATIONS_BY_CATEGORY = mapOf(
        CATEGORY_FEATURED to FEATURED_TRANSFORMATIONS,
        CATEGORY_BACKGROUND to BACKGROUND_TRANSFORMATIONS,
        CATEGORY_FACE to FACE_TRANSFORMATIONS,
        CATEGORY_STYLE to STYLE_TRANSFORMATIONS,
        CATEGORY_ENHANCE to ENHANCE_TRANSFORMATIONS,
        CATEGORY_CREATIVE to CREATIVE_TRANSFORMATIONS,
        CATEGORY_PROFESSIONAL to PROFESSIONAL_TRANSFORMATIONS,
        CATEGORY_VINTAGE to VINTAGE_TRANSFORMATIONS
    )

    // Popular transformations
    val POPULAR_TRANSFORMATIONS = ALL_TRANSFORMATIONS.filter { it.isPopular }

    // Premium transformations
    val PREMIUM_TRANSFORMATIONS = ALL_TRANSFORMATIONS.filter { it.isPremium }

    // Get transformation by ID
    fun getTransformationById(id: String): Transformation? {
        return ALL_TRANSFORMATIONS.find { it.id == id }
    }

    // Get transformations by category
    fun getTransformationsByCategory(category: String): List<Transformation> {
        return TRANSFORMATIONS_BY_CATEGORY[category] ?: emptyList()
    }

    // Search transformations
    fun searchTransformations(query: String): List<Transformation> {
        return ALL_TRANSFORMATIONS.filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.description.contains(query, ignoreCase = true)
        }
    }

    // Get category display names
    val CATEGORY_DISPLAY_NAMES = mapOf(
        CATEGORY_FEATURED to "Featured",
        CATEGORY_BACKGROUND to "Backgrounds",
        CATEGORY_FACE to "Face Effects",
        CATEGORY_STYLE to "Art Styles",
        CATEGORY_ENHANCE to "Enhance",
        CATEGORY_CREATIVE to "Creative",
        CATEGORY_PROFESSIONAL to "Professional",
        CATEGORY_VINTAGE to "Vintage"
    )
}