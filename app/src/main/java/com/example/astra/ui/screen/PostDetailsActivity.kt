package com.example.astra.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.astra.R
import com.example.astra.data.model.Post
import com.example.astra.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class PostDetailsActivity : AppCompatActivity() {
    private val postViewModel: PostViewModel by viewModels()

    private lateinit var postDetailImage: ImageView
    private lateinit var postDelete: ImageView
    private lateinit var postDetailTitle: TextView
    private lateinit var postDetailEssay: TextView
    private lateinit var pb: ProgressBar
    private lateinit var editButton: ImageView
    private var selectedImageUri: Uri? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        // Initialize views
        postDetailImage = findViewById(R.id.post_detail_image)
        postDelete = findViewById(R.id.post_detail_delete)
        postDetailTitle = findViewById(R.id.post_detail_title)
        postDetailEssay = findViewById(R.id.post_detail_essay)
        pb = findViewById(R.id.progress_bar)
        editButton = findViewById(R.id.post_detail_edit)

        val post: Post? = intent.getParcelableExtra("post")

        if (post != null) {
            postDetailTitle.text = post.post_title
            postDetailEssay.text = post.post_message
            Glide.with(this)
                .load(post.post_image)
                .placeholder(R.drawable.placeholder)
                .into(postDetailImage)

            postDelete.setOnClickListener {
                deletePost(post.id)
            }

            editButton.setOnClickListener {
                showEditPostDialog(post)
            }
        } else {
            Toast.makeText(this, "Error retrieving post", Toast.LENGTH_SHORT).show()
        }

        // Image picker setup
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = uri
                Log.e("SelectedImage", uri.toString())
            } ?: run {
                Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show()
            }
        }

        observeViewModel()
    }

    private fun deletePost(postId: Int) {
        postViewModel.loading.observe(this) { isLoading ->
            pb.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        postViewModel.deletePost(postId)
        postViewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Post deleted successfully!", Toast.LENGTH_SHORT).show()
                // Navigate back to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showEditPostDialog(post: Post) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_post, null)
        val editTextTitle = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val editTextMessage = dialogView.findViewById<EditText>(R.id.editTextMessage)
        val imageButton = dialogView.findViewById<Button>(R.id.buttonChooseImage)

        // Set existing data in the dialog
        editTextTitle.setText(post.post_title)
        editTextMessage.setText(post.post_message)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Edit Post")
            .setPositiveButton("Update") { _, _ ->

                val title = editTextTitle.text.toString().trim()
                val message = editTextMessage.text.toString().trim()
                val postId = post.id

                if (title.isEmpty() || message.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                postViewModel.loading.observe(this) { isLoading ->
                    pb.visibility = if (isLoading) View.VISIBLE else View.GONE
                }

                postViewModel.updatePost(postId, title, message, getFileFromUri(selectedImageUri))
                postViewModel.error.observe(this) { errorMessage ->
                    if (errorMessage != null) {
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Post updated successfully!", Toast.LENGTH_SHORT).show()
                        // Navigate back to MainActivity to refresh
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Close the dialog and return to main activity
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        imageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        dialog.show()
    }

    private fun getFileFromUri(uri: Uri?): File? {
        return uri?.let {
            val inputStream = contentResolver.openInputStream(it)
            val file = File(cacheDir, "temp_image.jpg")
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file
        }
    }

    private fun observeViewModel() {
        postViewModel.loading.observe(this) { isLoading ->
            pb.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        postViewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

