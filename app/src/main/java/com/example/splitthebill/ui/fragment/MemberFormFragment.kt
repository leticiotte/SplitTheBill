package com.example.splitthebill.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.splitthebill.R
import com.example.splitthebill.databinding.FragmentMemberFormBinding
import com.example.splitthebill.domain.model.Item
import com.example.splitthebill.domain.model.Member
import com.example.splitthebill.domain.model.interfaces.ToolbarConfig
import com.example.splitthebill.domain.repository.MemberRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get

class MemberFormFragment : Fragment(), ToolbarConfig {
    private var _binding: FragmentMemberFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var formMemberNameEt: EditText
    private lateinit var formMemberHasItemCb: CheckBox
    private lateinit var formMemberItemDescriptionEt: EditText
    private lateinit var formMemberItemValueEt: EditText
    private lateinit var saveBtn: Button
    private lateinit var toolbar: MaterialToolbar

    private val memberRepository: MemberRepository = get()

    private lateinit var member: Member
    private var isEdit: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMemberFormBinding.inflate(inflater, container, false)

        setupInputs()
        setupHasItemCb()
        setupSaveBtnOnClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findAndLinkToolbar()
        setupNavigationOnClickListener()

        arguments?.let {
            if (it.getSerializable("member") != null) {
                member = it.getSerializable("member") as Member
                isEdit = true
                formMemberNameEt.text = Editable.Factory.getInstance().newEditable(member.name)
                if (member.items.size != 0) {
                    formMemberHasItemCb.isChecked = true

                    formMemberItemDescriptionEt.isEnabled = true
                    formMemberItemDescriptionEt.background = null
                    formMemberItemDescriptionEt.text =
                        Editable.Factory.getInstance().newEditable(member.items[0].description)

                    formMemberItemValueEt.isEnabled = true
                    formMemberItemValueEt.background = null
                    formMemberItemValueEt.text =
                        Editable.Factory.getInstance().newEditable(member.items[0].value.toString())
                }
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
            findNavController().navigate(R.id.action_MemberFormFragment_to_MembersFragment)
        }
    }

    private fun setupInputs() {
        formMemberNameEt = binding.formMemberNameEt
        formMemberHasItemCb = binding.formMemberHasItemCb
        formMemberItemDescriptionEt = binding.formMemberItemDescriptionEt
        formMemberItemValueEt = binding.formMemberItemValueEt
        saveBtn = binding.saveBtn
    }

    private fun setupHasItemCb() {
        formMemberHasItemCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                formMemberItemDescriptionEt.isEnabled = true
                formMemberItemDescriptionEt.background = null
                formMemberItemValueEt.isEnabled = true
                formMemberItemValueEt.background = null
            } else {
                formMemberItemDescriptionEt.isEnabled = false
                formMemberItemDescriptionEt.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.edittext_background_disabled
                )!!
                formMemberItemValueEt.isEnabled = false
                formMemberItemValueEt.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.edittext_background_disabled
                )!!
            }
        }
    }

    private fun setupSaveBtnOnClickListener() {
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
                editMemberWithFormValues()
            } else {
                createMemberWithFormValues()
            }
        }
    }

    private fun isValidInputs(): Boolean {
        if (formMemberNameEt.text.toString().isNullOrEmpty()) return false
        if (formMemberHasItemCb.isChecked) {
            if (formMemberItemDescriptionEt.text.toString().isNullOrEmpty()) return false
            if (formMemberItemValueEt.text.toString().isNullOrEmpty()) return false
        }
        return true
    }

    private fun createMemberWithFormValues() {
        var newMember = Member()
        newMember.name = formMemberNameEt.text.toString()
        if (formMemberHasItemCb.isChecked) {
            var item = Item(
                0,
                formMemberItemDescriptionEt.text.toString(),
                formMemberItemValueEt.text.toString().toDouble()
            )
            newMember.items.add(item)
            newMember.amountPaid = item.value
        }
        memberRepository.addMember(newMember)
        Snackbar.make(
            binding.root,
            "Membro salvo com sucesso",
            Snackbar.LENGTH_LONG
        )
            .setAction("Action", null).show()

        findNavController().navigate(R.id.action_MemberFormFragment_to_MembersFragment)
    }

    private fun editMemberWithFormValues() {
        var newMember = Member()
        newMember.index = member.index
        newMember.name = formMemberNameEt.text.toString()
        if (formMemberHasItemCb.isChecked) {
            var item = Item(
                0,
                formMemberItemDescriptionEt.text.toString(),
                formMemberItemValueEt.text.toString().toDouble()
            )
            newMember.items.add(item)
            newMember.amountPaid = item.value
        }
        memberRepository.editMember(newMember)
        Snackbar.make(
            binding.root,
            "Membro editado com sucesso",
            Snackbar.LENGTH_LONG
        )
            .setAction("Action", null).show()
        findNavController().navigate(R.id.action_MemberFormFragment_to_MembersFragment)
    }
}