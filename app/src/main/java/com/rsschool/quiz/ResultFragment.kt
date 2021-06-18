package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.rsschool.quiz.databinding.FragmentResultBinding
import java.lang.IllegalStateException

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var score = 0
    private var clickListener: MainActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        clickListener = context as MainActivity

    }

    override fun onDetach() {
        super.onDetach()
        clickListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var arg: Bundle? = null
        try {
            arg = requireArguments()

        } catch (e: IllegalStateException) {

        }
        if (arg != null) {
            score = arg.getInt(RESULT)
        }
        uiUpdate()

        binding.shareIv.setOnClickListener {

        }

        binding.resetIv.setOnClickListener {
            clickListener?.reset(true)
        }

        binding.closeIv.setOnClickListener {
            clickListener?.close()
        }
    }

    fun uiUpdate() {
        binding.resultTv.text = "Your result $score %"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val RESULT = "result"

        fun newInstance(result: Int): ResultFragment {
            val fragment = ResultFragment()
            val args = bundleOf(
                RESULT to result
            )
            fragment.arguments = args
            return fragment
        }

    }
}

