package com.superphoto.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.superphoto.config.APIConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * FaceSwapProcessor - AI-powered face swapping using Gemini API
 * Handles face detection, alignment, and seamless face replacement
 */
class FaceSwapProcessor(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(APIConfig.API_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(APIConfig.API_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(APIConfig.API_TIMEOUT, TimeUnit.SECONDS)
        .build()

    companion object {
        private const val TAG = "FaceSwapProcessor"
    }

    /**
     * Swap faces between source and target images
     * @param sourceImageUri URI of the source image (face to be replaced)
     * @param targetFaceUri URI of the target face image (face to replace with)
     * @param onSuccess Callback with processed image URI
     * @param onError Callback with error message
     */
    fun swapFaces(
        sourceImageUri: Uri,
        targetFaceUri: Uri,
        onSuccess: (Uri) -> Unit,
        onError: (String) -> Unit
    ) {
        Thread {
            try {
                Log.d(TAG, "Starting face swap process...")

                // Load and process both images
                val sourceBitmap = loadBitmapFromUri(sourceImageUri)
                val targetFaceBitmap = loadBitmapFromUri(targetFaceUri)

                if (sourceBitmap == null || targetFaceBitmap == null) {
                    onError("Failed to load images")
                    return@Thread
                }

                // Convert images to base64
                val sourceBase64 = bitmapToBase64(sourceBitmap)
                val targetFaceBase64 = bitmapToBase64(targetFaceBitmap)

                // Create face swap prompt
                val prompt = createFaceSwapPrompt()

                // Create request body with both images
                val requestBody = createFaceSwapRequestBody(sourceBase64, targetFaceBase64, prompt)

                // Call Gemini API
                val response = callGeminiAPI(requestBody)
                
                if (response != null) {
                    // Parse response and extract processed image
                    val processedImageUri = parseGeminiResponse(response)
                    if (processedImageUri != null) {
                        onSuccess(processedImageUri)
                    } else {
                        onError("Failed to process face swap result")
                    }
                } else {
                    onError("API call failed")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Face swap error", e)
                onError("Face swap failed: ${e.message}")
            }
        }.start()
    }

    private fun loadBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // Resize if too large
            if (bitmap.width > APIConfig.MAX_IMAGE_SIZE || bitmap.height > APIConfig.MAX_IMAGE_SIZE) {
                val ratio = minOf(
                    APIConfig.MAX_IMAGE_SIZE.toFloat() / bitmap.width,
                    APIConfig.MAX_IMAGE_SIZE.toFloat() / bitmap.height
                )
                val newWidth = (bitmap.width * ratio).toInt()
                val newHeight = (bitmap.height * ratio).toInt()
                Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
            } else {
                bitmap
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading bitmap", e)
            null
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, APIConfig.IMAGE_QUALITY, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    private fun createFaceSwapPrompt(): String {
        return """
            Perform a realistic face swap between the two provided images:
            
            1. Detect and analyze faces in both images
            2. Extract the face from the second image (target face)
            3. Replace the face in the first image with the target face
            4. Ensure proper:
               - Face alignment and positioning
               - Skin tone matching and blending
               - Lighting consistency
               - Natural facial expressions
               - Seamless edge blending
            5. Maintain the original image quality and background
            6. Preserve facial features while ensuring realistic integration
            
            Return the result as a high-quality processed image with the face swap completed.
        """.trimIndent()
    }

    private fun createFaceSwapRequestBody(sourceBase64: String, targetFaceBase64: String, prompt: String): RequestBody {
        val jsonObject = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        // Add text prompt
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                        // Add source image
                        put(JSONObject().apply {
                            put("inline_data", JSONObject().apply {
                                put("mime_type", "image/jpeg")
                                put("data", sourceBase64)
                            })
                        })
                        // Add target face image
                        put(JSONObject().apply {
                            put("inline_data", JSONObject().apply {
                                put("mime_type", "image/jpeg")
                                put("data", targetFaceBase64)
                            })
                        })
                    })
                })
            })

            // Generation configuration
            put("generationConfig", JSONObject().apply {
                put("temperature", APIConfig.DEFAULT_TEMPERATURE)
                put("topK", APIConfig.DEFAULT_TOP_K)
                put("topP", APIConfig.DEFAULT_TOP_P)
                put("maxOutputTokens", APIConfig.MAX_OUTPUT_TOKENS)
            })

            // Safety settings
            put("safetySettings", JSONArray().apply {
                APIConfig.SAFETY_SETTINGS.forEach { (category, threshold) ->
                    put(JSONObject().apply {
                        put("category", category)
                        put("threshold", threshold)
                    })
                }
            })
        }

        return jsonObject.toString().toRequestBody("application/json".toMediaType())
    }

    private fun callGeminiAPI(requestBody: RequestBody): String? {
        return try {
            val request = Request.Builder()
                .url(APIConfig.getGeminiUrl())
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.string()
            } else {
                Log.e(TAG, "API call failed: ${response.code} - ${response.message}")
                null
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error", e)
            null
        }
    }

    private fun parseGeminiResponse(response: String): Uri? {
        return try {
            val jsonResponse = JSONObject(response)
            val candidates = jsonResponse.getJSONArray("candidates")
            
            if (candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.getJSONObject("content")
                val parts = content.getJSONArray("parts")
                
                // Look for image data in the response
                for (i in 0 until parts.length()) {
                    val part = parts.getJSONObject(i)
                    if (part.has("inline_data")) {
                        val inlineData = part.getJSONObject("inline_data")
                        val imageData = inlineData.getString("data")
                        
                        // Decode and save the processed image
                        return saveProcessedImage(imageData)
                    }
                }
                
                // If no image data found, try to extract from text response
                val textPart = parts.getJSONObject(0)
                if (textPart.has("text")) {
                    val text = textPart.getString("text")
                    Log.d(TAG, "Gemini response: $text")
                    
                    // For now, return null as we need actual image processing
                    // In a real implementation, this would handle the AI response
                    return null
                }
            }
            
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing response", e)
            null
        }
    }

    private fun saveProcessedImage(base64Data: String): Uri? {
        return try {
            val imageBytes = Base64.decode(base64Data, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            
            // Save to app's cache directory
            val fileName = "face_swap_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, fileName)
            
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, APIConfig.IMAGE_QUALITY, outputStream)
            outputStream.close()
            
            Uri.fromFile(file)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving processed image", e)
            null
        }
    }

    /**
     * Detect faces in an image for preview/validation
     * @param imageUri URI of the image to analyze
     * @param onResult Callback with number of faces detected
     */
    fun detectFaces(
        imageUri: Uri,
        onResult: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        Thread {
            try {
                val bitmap = loadBitmapFromUri(imageUri)
                if (bitmap == null) {
                    onError("Failed to load image")
                    return@Thread
                }

                val base64 = bitmapToBase64(bitmap)
                val prompt = "Analyze this image and count the number of human faces visible. Return only the number."
                
                val requestBody = createSimpleRequestBody(base64, prompt)
                val response = callGeminiAPI(requestBody)
                
                if (response != null) {
                    // Parse face count from response
                    val faceCount = parseFaceCount(response)
                    onResult(faceCount)
                } else {
                    onError("Face detection failed")
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Face detection error", e)
                onError("Face detection failed: ${e.message}")
            }
        }.start()
    }

    private fun createSimpleRequestBody(base64: String, prompt: String): RequestBody {
        val jsonObject = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                        put(JSONObject().apply {
                            put("inline_data", JSONObject().apply {
                                put("mime_type", "image/jpeg")
                                put("data", base64)
                            })
                        })
                    })
                })
            })
        }

        return jsonObject.toString().toRequestBody("application/json".toMediaType())
    }

    private fun parseFaceCount(response: String): Int {
        return try {
            val jsonResponse = JSONObject(response)
            val candidates = jsonResponse.getJSONArray("candidates")
            
            if (candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.getJSONObject("content")
                val parts = content.getJSONArray("parts")
                val textPart = parts.getJSONObject(0)
                val text = textPart.getString("text").trim()
                
                // Extract number from response
                val number = text.filter { it.isDigit() }
                if (number.isNotEmpty()) {
                    number.toInt()
                } else {
                    0
                }
            } else {
                0
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing face count", e)
            0
        }
    }
}