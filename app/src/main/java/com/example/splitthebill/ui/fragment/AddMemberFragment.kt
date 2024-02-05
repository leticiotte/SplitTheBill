package com.example.splitthebill.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.splitthebill.R
import com.example.splitthebill.databinding.FragmentAddMemberBinding

class AddMemberFragment : Fragment() {
    private var _binding: FragmentAddMemberBinding? = null
    private val binding get() = _binding!!

    private lateinit var addMemberHasItemCb: CheckBox
    private lateinit var addMemberItemDescriptionEt: EditText
    private lateinit var addMemberItemValueEt: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddMemberBinding.inflate(inflater, container, false)

        setupHasItemCb()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupHasItemCb(){
        addMemberHasItemCb = binding.addMemberHasItemCb
        addMemberItemDescriptionEt = binding.addMemberItemDescriptionEt
        addMemberItemValueEt = binding.addMemberItemValueEt

        addMemberHasItemCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                addMemberItemDescriptionEt.isEnabled = true
                addMemberItemDescriptionEt.background = null
                addMemberItemValueEt.isEnabled = true
                addMemberItemValueEt.background = null
            } else {
                addMemberItemDescriptionEt.isEnabled = false
                addMemberItemDescriptionEt.background = ContextCompat.getDrawable(binding.root.context, R.drawable.edittext_background_disabled)!!
                addMemberItemValueEt.isEnabled = false
                addMemberItemValueEt.background = ContextCompat.getDrawable(binding.root.context, R.drawable.edittext_background_disabled)!!
            }
        }
    }

}