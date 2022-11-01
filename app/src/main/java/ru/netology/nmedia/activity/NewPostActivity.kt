package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_post.*
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edit.requestFocus()

        val postContent = intent?.getStringExtra(Intent.EXTRA_TEXT)
        edit.setText(postContent)

        binding.ok.setOnClickListener {
            val content = binding.edit.text.toString()
            val intent = Intent()
            if (content.isEmpty()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }
}