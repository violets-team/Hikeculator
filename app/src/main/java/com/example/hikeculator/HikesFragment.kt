package com.example.hikeculator

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hikeculator.databinding.FragmentHikesBinding
import com.example.hikeculator.databinding.FragmentMembersBinding

class HikesFragment: Fragment(R.layout.fragment_hikes) {

    private var _binding: FragmentHikesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHikesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCreateHike.setOnClickListener {
           findNavController().navigate(R.id.action_hikesFragment_to_createHikeFragment)
        }

        binding.bottomAppBarHikes.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.app_bar_item_profile -> {
                    findNavController().navigate(R.id.action_hikesFragment_to_profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

