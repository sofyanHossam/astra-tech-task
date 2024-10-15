package com.example.astra.data.remote


import com.example.astra.data.model.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface PostService {
    @GET("getposts")
    suspend fun getPosts(): Response<List<Post>>

    @Multipart
    @POST("create")
    suspend fun createPost(
        @Part("post_title") postTitle: RequestBody,
        @Part("post_message") postMessage: RequestBody,
        @Part postImage: MultipartBody.Part
    ): Response<Void>

    @POST("deletepost")
    suspend fun deletePost(@Body request:Map<String, Int>): Response<Void>

    @Multipart
    @POST("updatepost")
    suspend fun updatePost(
        @Part("id") postId: RequestBody,
        @Part("post_title") postTitle: RequestBody,
        @Part("post_message") postMessage: RequestBody,
        @Part postImage: MultipartBody.Part? // Nullable to allow updates without changing the image
    ): Response<Void>

}
