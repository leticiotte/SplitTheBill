package com.example.splitthebill.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitthebill.R
import com.example.splitthebill.databinding.FragmentSplitBillBinding
import com.example.splitthebill.domain.model.SplitBillInfos
import com.example.splitthebill.domain.model.interfaces.ToolbarConfig
import com.example.splitthebill.ui.adapter.SplitBillResultAdapter
import com.example.splitthebill.ui.holder.SplitBillResultHolder
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SplitBillFragment : Fragment(), ToolbarConfig {
    private var _binding: FragmentSplitBillBinding? = null
    private val binding get() = _binding!!
    private lateinit var splitBillRv: RecyclerView
    private lateinit var splitBillResultAdapter: SplitBillResultAdapter
    private lateinit var toolbar: MaterialToolbar

    private var splitBillResultList: MutableList<SplitBillInfos> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplitBillBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findAndLinkToolbar()
        setupNavigationOnClickListener()

        arguments?.let {
            val json = it.getString("splitBillResultList")
            val type = object : TypeToken<List<SplitBillInfos>>() {}.type
            splitBillResultList = Gson().fromJson(json, type) ?: mutableListOf()
        }

        setupSplitBillResultList()
        splitBillRv.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTextViewColors()
        }
    }


    private fun setupSplitBillResultList() {
        splitBillResultAdapter =
            SplitBillResultAdapter(requireContext().applicationContext, splitBillResultList)

        splitBillRv = binding.splitBillRv
        splitBillRv.layoutManager = LinearLayoutManager(this.context)
        splitBillRv.adapter = splitBillResultAdapter
    }

    override fun findAndLinkToolbar() {
        toolbar = requireActivity().findViewById(R.id.toolbar)
    }

    override fun setupNavigationOnClickListener() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_SplitBillFragment_to_MembersFragment)
        }
    }

    private fun updateTextViewColors() {
        for (i in 0 until splitBillResultAdapter.itemCount) {
            val viewHolder =
                splitBillRv.findViewHolderForAdapterPosition(i) as? SplitBillResultHolder
            viewHolder?.let {
                val textColor = if (splitBillResultList[i].hasToPay) {
                    ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
                } else {
                    ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
                }
                it.name.setTextColor(textColor)
                it.value.setTextColor(textColor)
            }
        }
    }

}