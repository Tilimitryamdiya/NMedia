package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.netology.nmedia.databinding.ActivityMainBinding
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fillThePost(post)
        likedByMe(post)
        shared(post)
    }

    private val post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась " +
                "с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, " +
                "разработке, аналитике и управлению. Мы растём сами и помогаем расти " +
                "студентам: от новичков до уверенных профессионалов. Но самое важное " +
                "остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет " +
                "хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на " +
                "путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likedByMe = false,
        likes = 9_999_999
    )

    private fun fillThePost(post: Post) = with(binding) {
        author.text = post.author
        published.text = post.published
        content.text = post.content
        likeCount.text = getFormattedNumber(post.likes)
        shareCount.text = getFormattedNumber(post.shared)
    }

    private fun likedByMe(post: Post) = with(binding) {
        like.setOnClickListener {
            Log.d("stuff", "Click listener: like")
            post.likedByMe = !post.likedByMe
            like.setImageResource(
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            )
            if (post.likedByMe) post.likes++ else post.likes--
            likeCount.text = getFormattedNumber(post.likes)
        }
    }

    private fun shared(post: Post) = with(binding) {
        share.setOnClickListener {
            Log.d("stuff", "Click listener: share")
            post.shared++
            shareCount.text = getFormattedNumber(post.shared)
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

