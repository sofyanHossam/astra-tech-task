package com.example.astra.data.repository

import com.example.astra.data.model.Post
import com.example.astra.data.remote.PostService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class PostRepository @Inject constructor(private val postService: PostService) {

    suspend fun getAllPosts(): Response<List<Post>> = postService.getPosts()

    suspend fun createNewPost(title: String, message: String, imageFile: File): Response<Void> {
        val postTitle = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val postMessage = message.toRequestBody("text/plain".toMediaTypeOrNull())
        val postImage = MultipartBody.Part.createFormData(
            "post_image", imageFile.name, imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        return postService.createPost(postTitle, postMessage, postImage)
    }

    suspend fun deletePost(postId: Int): Response<Void> {
        val requestBody = mapOf("id" to postId) // Convert the ID to the required format
        return postService.deletePost(requestBody)
    }

    suspend fun updatePost(postId: Int, title: String, message: String, imageFile: File?): Response<Void> {
        // Create RequestBody for ID
        val postIdBody = postId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val postTitle = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val postMessage = message.toRequestBody("text/plain".toMediaTypeOrNull())
        val postImagePart = imageFile?.let {
            MultipartBody.Part.createFormData(
                "post_image", it.name, it.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )
        }

        return postService.updatePost(postIdBody, postTitle, postMessage, postImagePart)
    }


}
