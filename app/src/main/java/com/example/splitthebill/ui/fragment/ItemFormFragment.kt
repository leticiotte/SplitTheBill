package com.example.splitthebill.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.splitthebill.R
import com.example.splitthebill.databinding.FragmentItemFormBinding
import com.example.splitthebill.domain.model.Item
import com.example.splitthebill.domain.model.Member
import com.example.splitthebill.domain.model.interfaces.ToolbarConfig
import com.example.splitthebill.domain.repository.MemberRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get

class ItemFormFragment : Fragment(), ToolbarConfig {
    private var _binding: FragmentItemFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var formItemDescriptionEt: EditText
    private lateinit var formItemValueEt: EditText
    private lateinit var saveBtn: Button
    private lateinit var toolbar: MaterialToolbar

    private val memberRepository: MemberRepository = get()

    private lateinit var member: Member
    private lateinit var item: Item
    private var isEdit: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemFormBinding.inflate(inflater, container, false)

        setupInputs()
        setupSaveBtn()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findAndLinkToolbar()
        setupNavigationOnClickListener()

        arguments?.let {
            member = it.getSerializable("member") as Member
            if (it.getSerializable("item") != null) {
                item = it.getSerializable("item") as Item
                isEdit = true
                formItemDescriptionEt.text = Editable.Factory.getInstance().newEditable(item.description)
                formItemValueEt.text = Editable.Factory.getInstance().newEditable(item.value.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun findAndLinkToolbar() {
        toolbar = requireActivity().findViewById(R.id.toolbar)
    }

    override fun setupNavigationOnClickListener() {
        toolbar.setNavigationOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("member", member)
            findNavController().navigate(R.id.action_ItemFormFragment_to_ItemsFragment, bundle)
        }
    }

    private fun setupInputs() {
        formItemValueEt = binding.formItemValueEt
        formItemDescriptionEt = binding.formItemDescriptionEt
        formItemValueEt = binding.formItemValueEt
    }

    private fun setupSaveBtn() {
        saveBtn = binding.saveBtn
        saveBtn.setOnClickListener {
            if (!isValidInputs()) {
                Snackbar.make(
                    binding.root,
                    "Preencha o formulário corretamente",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Action", null).show()
                return@setOnClickListener
            }
            if (isEdit) {
                editItemWithFormValues()
            } else {
                createItemWithFormValues()
            }
        }
    }

    private fun isValidInputs(): Boolean {
        if (formItemDescriptionEt.text.toString().isNullOrEmpty()) return false
        if (formItemValueEt.text.toString().isNullOrEmpty()) return false
        return true
    }

    private fun createItemWithFormValues() {
        var newItem = Item()
        newItem.description = formItemDescriptionEt.text.toString()
        newItem.value = formItemValueEt.text.toString().toDouble()
        memberRepository.addMemberItem(member.index!!, newItem)
        Snackbar.make(
            binding.root,
            "Item adicionado com sucesso",
            Snackbar.LENGTH_LONG
        )
            .setAction("Action", null).show()
        val bundle = Bundle()
        bundle.putSerializable("member", member)
        findNavController().navigate(R.id.action_ItemFormFragment_to_ItemsFragment, bundle)
    }

    private fun editItemWithFormValues() {
        var newItem = Item()
        newItem.index = item.index
        newItem.description = formItemDescriptionEt.text.toString()
        newItem.value = formItemValueEt.text.toString().toDouble()
        memberRepository.editMemberItem(member.index!!, newItem)
        Snackbar.make(
            binding.root,
            "Item editado com sucesso",
            Snackbar.LENGTH_LONG
        )
            .setAction("Action", null).show()

        val bundle = Bundle()
        bundle.putSerializable("member", member)
        findNavController().navigate(R.id.action_ItemFormFragment_to_ItemsFragment, bundle)
    }
}