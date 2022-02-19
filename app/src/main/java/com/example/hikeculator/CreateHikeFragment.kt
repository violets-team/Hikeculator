package com.example.hikeculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hikeculator.databinding.FragmentCreateHikeBinding
import com.example.hikeculator.databinding.FragmentMembersBinding

class CreateHikeFragment: Fragment(R.layout.fragment_create_hike) {

    private var _binding: FragmentCreateHikeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCreateHikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}