package com.techmania.expensemanager.Views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.techmania.expensemanager.Entity.TransactionDatabase
import com.techmania.expensemanager.R
import com.techmania.expensemanager.databinding.LayoutMoreFragmentBinding

class MoreFragment : Fragment() {

    private lateinit var binding: LayoutMoreFragmentBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutMoreFragmentBinding.inflate(inflater, container, false)
        binding.ivBack.setOnClickListener {
            parentFragmentManager.beginTransaction().setCustomAnimations(
                R.anim.anim_slide_start_from_left,
                R.anim.anim_slide_end_to_left,
                R.anim.anim_slide_start_from_left,
                R.anim.anim_slide_end_to_left
            ).remove(this).commit()
        }
        setUpUser()
        initClicks()

        return binding.root
    }

    private fun setUpUser() {
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "")
        val email = sharedPreferences.getString("email", "")
        val isSecured = sharedPreferences.getBoolean("secured",false)
        Log.d("ajit", "isSecured: $isSecured")

        if (isSecured==true){
            binding.menu.cbSecure.isChecked = true
        } else{
            binding.menu.cbSecure.isChecked = false
        }
        binding.tvName.text = name
        binding.tvEmail.text = email
    }

    private fun initClicks() {
        binding.menu.tvHome.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
        binding.menu.tvRestore.setOnClickListener {
            val td = TransactionDatabase.getdatabase(requireContext())
            td.restoreDatabase(requireContext())
        }
        binding.menu.tvExport.setOnClickListener {
            (activity as MainActivity).createFile()

        }
        binding.menu.tvAboutUs.setOnClickListener {
            val url = getString(R.string.linkedin)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        binding.menu.tvDonate.setOnClickListener {
            binding.menu.root.isVisible = false
            binding.qr.isVisible = true
            binding.closeqr.isVisible = true
        }

        binding.menu.tvProtect.setOnClickListener {
            binding.menu.cbSecure.isChecked = !binding.menu.cbSecure.isChecked
            val isSecured = binding.menu.cbSecure.isChecked
            Log.d("ajit", "checkbox: $isSecured")
            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("secured", isSecured)
            editor.apply()

        }

        binding.menu.tvTips.setOnClickListener {
            binding.tips.isVisible = true
            binding.menu.root.isVisible = false
            binding.ivBack.isVisible = false
            binding.ivProfilePhoto.isVisible = false
            binding.tvName.isVisible = false
            binding.tvEmail.isVisible = false
            binding.splitter.isVisible = false
            parentFragmentManager.beginTransaction().replace(binding.tips.id,TipFragment()).commit()
        }

        binding.closeqr.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
            binding.menu.root.isVisible = true
            binding.qr.isVisible = false
            binding.closeqr.isVisible = false
        }

    }
}