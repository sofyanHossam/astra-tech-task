package com.example.astra.ui.screen

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.app.AlertDialog
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astra.R
import com.example.astra.ui.adapter.PostAdapter
import com.example.astra.viewmodel.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val postViewModel: PostViewModel by viewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var fab: FloatingActionButton
    private var selectedImageUri: Uri? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        progressBar = findViewById(R.id.progress_bar)
        fab = findViewById(R.id.fab_add_post)
        recyclerView.layoutManager = LinearLayoutManager(this)

        postViewModel.posts.observe(this, Observer { posts ->
            recyclerView.adapter = PostAdapter(posts,this)
        })

        // Observe the loading state
        postViewModel.loading.observe(this, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        // Observe the error message
        postViewModel.error.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        postViewModel.createPostResult.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Post created successfully!", Toast.LENGTH_SHORT).show()
                postViewModel.fetchPosts() // Refresh posts after creating new one
            }
        })

        postViewModel.fetchPosts()

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = uri
                Log.e("SelectedImage", uri.toString())
            } ?: run {
                Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show()
            }
        }

        fab.setOnClickListener {
            showCreatePostDialog()
        }

    }

    private fun showCreatePostDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_post, null)
        val editTextTitle = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val editTextMessage = dialogView.findViewById<EditText>(R.id.editTextMessage)
        val imageButton = dialogView.findViewById<Button>(R.id.buttonChooseImage)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Create Post")
            .setPositiveButton("Post") { _, _ ->
                val title = editTextTitle.text.toString().trim()
                val message = editTextMessage.text.toString().trim()

                if (title.isEmpty() || message.isEmpty() || selectedImageUri == null) {
                    Toast.makeText(this, "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                postViewModel.createPost(title, message, getFileFromUri(selectedImageUri))
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

}
