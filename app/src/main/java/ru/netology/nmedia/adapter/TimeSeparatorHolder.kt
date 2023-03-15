package ru.netology.nmedia.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.TimeSeparatorBinding
import ru.netology.nmedia.dto.TimeSeparator
import ru.netology.nmedia.enumeration.TimeSeparatorType

class TimeSeparatorHolder(
    private val binding: TimeSeparatorBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(timeSeparator: TimeSeparator) {
        binding.postTime.text = dateFormatter(timeSeparator)
    }

}

private fun dateFormatter(timeSeparator: TimeSeparator) =
    when(timeSeparator.type) {
       TimeSeparatorType.TODAY -> "Today"
       TimeSeparatorType.YESTERDAY -> "Yesterday"
       TimeSeparatorType.LAST_WEEK -> "Last week"
   }


