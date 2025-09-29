package com.superphoto.ai

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.superphoto.config.APIConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class GeminiApiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateContent(request: GeminiRequest): AIResult<GeminiResponse> = withContext(Dispatchers.IO) {
        try {
            val jsonRequest = buildGeminiJsonRequest(request)
            // Sử dụng đúng URL format cho Gemini API
            val url = "https://generativelanguage.googleapis.com/v1beta/models/${APIConfig.GEMINI_MODEL}:generateContent?key=${APIConfig.GEMINI_API_KEY}"
            
            Log.d("GeminiApiService", "Request URL: $url")
            Log.d("GeminiApiService", "Request Body: ${jsonRequest.toString()}")
            
            val requestBody = jsonRequest.toString().toRequestBody("application/json".toMediaType())
            val httpRequest = Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(httpRequest).execute()
            val responseBody = response.body?.string()

            Log.d("GeminiApiService", "Response Code: ${response.code}")
            Log.d("GeminiApiService", "Response Body: $responseBody")

            if (response.isSuccessful && responseBody != null) {
                val geminiResponse = parseGeminiResponse(responseBody)
                AIResult.Success(geminiResponse)
            } else {
                val errorMessage = responseBody ?: "Unknown error occurred"
                Log.e("GeminiApiService", "API Error: Code ${response.code}, Message: $errorMessage")
                AIResult.Error("Gemini API error: $errorMessage")
            }
        } catch (e: Exception) {
            Log.e("GeminiApiService", "Exception in generateContent", e)
            AIResult.Error("Failed to generate content: ${e.message}", e)
        }
    }

    private fun buildGeminiJsonRequest(request: GeminiRequest): JSONObject {
        val jsonRequest = JSONObject()
        val contentsArray = JSONArray()

        request.contents.forEach { content ->
            val contentObj = JSONObject()
            val partsArray = JSONArray()

            content.parts.forEach { part ->
                val partObj = JSONObject()
                part.text?.let { partObj.put("text", it) }
                part.inlineData?.let { inlineData ->
                    val inlineDataObj = JSONObject()
                    inlineDataObj.put("mimeType", inlineData.mimeType)
                    inlineDataObj.put("data", inlineData.data)
                    partObj.put("inlineData", inlineDataObj)
                }
                partsArray.put(partObj)
            }

            contentObj.put("parts", partsArray)
            contentsArray.put(contentObj)
        }

        jsonRequest.put("contents", contentsArray)

        // Add generation config
        request.generationConfig?.let { config ->
            val configObj = JSONObject()
            configObj.put("temperature", config.temperature.toDouble())
            configObj.put("topK", config.topK.toLong())
            configObj.put("topP", config.topP.toDouble())
            configObj.put("maxOutputTokens", config.maxOutputTokens.toLong())
            jsonRequest.put("generationConfig", configObj)
        }

        // Add safety settings
        request.safetySettings?.let { settings ->
            val safetyArray = JSONArray()
            settings.forEach { setting ->
                val safetyObj = JSONObject()
                safetyObj.put("category", setting.category)
                safetyObj.put("threshold", setting.threshold)
                safetyArray.put(safetyObj)
            }
            jsonRequest.put("safetySettings", safetyArray)
        }

        return jsonRequest
    }

    private fun parseGeminiResponse(responseBody: String): GeminiResponse {
        val jsonResponse = JSONObject(responseBody)
        
        if (jsonResponse.has("error")) {
            val errorObj = jsonResponse.getJSONObject("error")
            val error = GeminiError(
                code = errorObj.getInt("code"),
                message = errorObj.getString("message"),
                status = errorObj.getString("status")
            )
            return GeminiResponse(error = error)
        }

        val candidates = mutableListOf<GeminiCandidate>()
        if (jsonResponse.has("candidates")) {
            val candidatesArray = jsonResponse.getJSONArray("candidates")
            for (i in 0 until candidatesArray.length()) {
                val candidateObj = candidatesArray.getJSONObject(i)
                
                // Check if content exists and has parts
                val parts = mutableListOf<GeminiPart>()
                if (candidateObj.has("content")) {
                    val contentObj = candidateObj.getJSONObject("content")
                    if (contentObj.has("parts")) {
                        val partsArray = contentObj.getJSONArray("parts")
                        for (j in 0 until partsArray.length()) {
                            val partObj = partsArray.getJSONObject(j)
                            val text = if (partObj.has("text")) partObj.getString("text") else null
                            parts.add(GeminiPart(text = text))
                        }
                    } else {
                        // Handle case where content exists but no parts (like MAX_TOKENS response)
                        Log.w("GeminiApiService", "Content object exists but no parts found. FinishReason might be MAX_TOKENS")
                    }
                }
                
                // If no parts found, create empty content
                if (parts.isEmpty()) {
                    parts.add(GeminiPart(text = "Response truncated due to token limit"))
                }

                val content = GeminiContent(parts = parts)
                val finishReason = if (candidateObj.has("finishReason")) 
                    candidateObj.getString("finishReason") else null

                candidates.add(GeminiCandidate(
                    content = content,
                    finishReason = finishReason
                ))
            }
        }

        return GeminiResponse(candidates = candidates)
    }
}

class PollinationsApiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS) // Longer timeout for image generation
        .build()

    suspend fun generateImage(request: PollinationsImageRequest): AIResult<Bitmap> = withContext(Dispatchers.IO) {
        try {
            val encodedPrompt = URLEncoder.encode(request.prompt, "UTF-8")
            val url = buildPollinationsUrl(encodedPrompt, request.width, request.height, request.seed, request.model, request.enhance)
            
            Log.d("PollinationsApiService", "Generating image with URL: $url")
            
            val httpRequest = Request.Builder()
                .url(url)
                .get()
                .build()

            val response = client.newCall(httpRequest).execute()
            
            if (response.isSuccessful) {
                val inputStream = response.body?.byteStream()
                if (inputStream != null) {
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    if (bitmap != null) {
                        AIResult.Success(bitmap)
                    } else {
                        AIResult.Error("Failed to decode image from response")
                    }
                } else {
                    AIResult.Error("Empty response body")
                }
            } else {
                val errorMessage = response.body?.string() ?: "Unknown error"
                Log.e("PollinationsApiService", "API Error: ${response.code} - $errorMessage")
                AIResult.Error("Pollinations API error: ${response.code} - $errorMessage")
            }
        } catch (e: IOException) {
            Log.e("PollinationsApiService", "Network error in generateImage", e)
            AIResult.Error("Network error: ${e.message}", e)
        } catch (e: Exception) {
            Log.e("PollinationsApiService", "Exception in generateImage", e)
            AIResult.Error("Failed to generate image: ${e.message}", e)
        }
    }

    private fun buildPollinationsUrl(
        encodedPrompt: String,
        width: Int,
        height: Int,
        seed: Int?,
        model: String,
        enhance: Boolean
    ): String {
        var url = "${APIConfig.POLLINATIONS_BASE_URL}/prompt/$encodedPrompt"
        
        val params = mutableListOf<String>()
        params.add("width=$width")
        params.add("height=$height")
        params.add("model=$model")
        params.add("enhance=$enhance")
        
        seed?.let { params.add("seed=$it") }
        
        if (params.isNotEmpty()) {
            url += "?" + params.joinToString("&")
        }
        
        return url
    }
}