package com.example.preatec

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.preatec.databinding.FragmentCallFragmentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_COLOR_RES =  "ARG_COLOR_RES"

class CallFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var colorRes: Int? = null
    private val binding: FragmentCallFragmentBinding by lazy {
        FragmentCallFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            colorRes = it.getInt(ARG_COLOR_RES)
            binding.root.setBackgroundResource(colorRes!!)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(colorRes: Int) =
            CallFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLOR_RES, colorRes)

                }
            }
    }
}