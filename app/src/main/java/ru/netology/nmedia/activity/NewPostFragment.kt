package ru.netology.nmedia.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_new_post.*
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)
    private lateinit var binding: FragmentNewPostBinding
    private val preferences by lazy { this.activity?.getPreferences(Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            preferences?.edit()?.apply {
                if (arguments?.textArg == edit.text.toString()) {
                    return@apply
                } else {
                    putString(SAVE_KEY, edit.text.toString())
                    apply()
                }
            }
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edit.requestFocus()
        arguments?.textArg?.let(binding.edit::setText) ?: preferences?.getString(SAVE_KEY, "")
            .let(binding.edit::setText)

        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            if (text.isNotBlank()) {
                viewModel.changeContent(text)
                viewModel.save()
            }
            preferences?.edit {
                clear()
                commit()
            }
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
    }

    companion object {
        const val SAVE_KEY = "save"
        var Bundle.textArg by StringArg
    }
}