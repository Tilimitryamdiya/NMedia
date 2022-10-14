package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        subscribe()
        setupListeners()
    }

    private fun subscribe() {
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
                likeCount.text = getFormattedNumber(post.likes)
                shareCount.text = getFormattedNumber(post.shared)
            }
        }
    }

    private fun setupListeners() {
        binding.like.setOnClickListener {
            viewModel.like()
        }
        binding.share.setOnClickListener{
            viewModel.share()
        }
    }

    private fun getFormattedNumber(count: Int): String {
        val formatter = java.text.DecimalFormat("###.#")
        formatter.roundingMode = RoundingMode.DOWN

        return when (count) {
            in 0..999 -> count.toString()
            in 1_000..9_999 -> formatter.format(count / 1000.0) + "K"
            in 10_000..999_999 -> (count / 1000).toString() + "K"
            else -> formatter.format(count / 1_000_000.0) + "M"
        }
    }
}

