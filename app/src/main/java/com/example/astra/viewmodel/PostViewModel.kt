package com.example.astra.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astra.data.model.Post
import com.example.astra.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _createPostResult = MutableLiveData<Boolean>()
    val createPostResult: LiveData<Boolean> get() = _createPostResult
    private val _updatePostResult = MutableLiveData<Boolean>()
    val updatePostResult: LiveData<Boolean> get() = _createPostResult

    private val _postDetails = MutableLiveData<Post>()
    val postDetails: LiveData<Post> get() = _postDetails

    fun fetchPosts() {
        _loading.value = true  // Show loading
        viewModelScope.launch {
            val response: Response<List<Post>> = repository.getAllPosts()
            _loading.value = false  // Hide loading after request completes

            if (response.isSuccessful) {
                _posts.value = response.body()
            } else {
                _error.value = "Error: ${response.message()}"
            }
        }
    }

    fun createPost(title: String, message: String, imageFile: File?) {
        if (imageFile == null) return

        _loading.value = true
        viewModelScope.launch {
            val response = repository.createNewPost(title, message, imageFile)
            _loading.value = false

            _createPostResult.value = response.isSuccessful
            if (!response.isSuccessful) {
                _error.value = "Error: ${response.message()}"
            }
        }
    }

    fun deletePost(postId: Int) {
        _loading.value = true
        viewModelScope.launch {
            val response = repository.deletePost(postId)
            _loading.value = false

            if (response.isSuccessful) {
                _error.value = null // Clear any previous errors
            } else {
                _error.value = "Error: ${response.message()}" // Capture the error message
            }
        }
    }

    fun updatePost(postId: Int, title: String, message: String, imageFile: File?) {
        _loading.value = true
        viewModelScope.launch {
            val response = repository.updatePost(postId, title, message, imageFile)
            _loading.value = false

            if (response.isSuccessful) {
                // Handle success, refresh posts if necessary
                _error.value=null
                fetchPosts()
            } else {
                _error.value = "Error: ${response.message()}"
            }
        }
    }

}

