package ru.netology.nmedia.adapter

 import android.view.LayoutInflater
 import android.view.ViewGroup
 import androidx.paging.PagingDataAdapter
 import androidx.recyclerview.widget.RecyclerView
 import ru.netology.nmedia.R
 import ru.netology.nmedia.databinding.CardAdBinding
 import ru.netology.nmedia.databinding.CardPostBinding
 import ru.netology.nmedia.databinding.TimeSeparatorBinding
 import ru.netology.nmedia.dto.Ad
 import ru.netology.nmedia.dto.FeedItem
 import ru.netology.nmedia.dto.Post
 import ru.netology.nmedia.dto.TimeSeparator

class FeedAdapter(private val onInteractionListener: OnInteractionListener) :
    PagingDataAdapter<FeedItem, RecyclerView.ViewHolder>(FeedDiffCallback()) {
    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is Ad -> R.layout.card_ad
            is Post -> R.layout.card_post
            is TimeSeparator -> R.layout.time_separator
            null -> error("Unknown item type")
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {

            R.layout.card_post -> {
                val binding =
                    CardPostBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                PostViewHolder(binding, onInteractionListener)
            }

            R.layout.card_ad -> {
                val binding =
                    CardAdBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                AdViewHolder(binding, onInteractionListener)
            }

            R.layout.time_separator -> {
                val binding =
                    TimeSeparatorBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                TimeSeparatorHolder(binding)
            }

            else -> error("Unknown view type: $viewType")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Ad -> (holder as? AdViewHolder)?.bind(item)
            is Post -> (holder as? PostViewHolder)?.bind(item)
            is TimeSeparator -> (holder as? TimeSeparatorHolder)?.bind(item)
            null -> error("Unknown item type")
        }
    }
}