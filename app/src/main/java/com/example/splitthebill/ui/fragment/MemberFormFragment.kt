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
import com.example.splitthebill.domain.repository.MemberRepository
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get

class MemberFormFragment : Fragment() {
    private var _binding: FragmentMemberFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var addMemberNameEt: EditText
    private lateinit var addMemberHasItemCb: CheckBox
    private lateinit var addMemberItemDescriptionEt: EditText
    private lateinit var addMemberItemValueEt: EditText
    private lateinit var saveBtn: Button

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

        arguments?.let {
            member = it.getSerializable("member") as Member
            if (member != null) {
                isEdit = true
                addMemberNameEt.text = Editable.Factory.getInstance().newEditable(member.name)
                if (member.items.size != 0) {
                    addMemberHasItemCb.isChecked = true

                    addMemberItemDescriptionEt.isEnabled = true
                    addMemberItemDescriptionEt.background = null
                    addMemberItemDescriptionEt.text =
                        Editable.Factory.getInstance().newEditable(member.items[0].description)

                    addMemberItemValueEt.isEnabled = true
                    addMemberItemValueEt.background = null
                    addMemberItemValueEt.text =
                        Editable.Factory.getInstance().newEditable(member.items[0].value.toString())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupInputs() {
        addMemberNameEt = binding.addMemberNameEt
        addMemberHasItemCb = binding.addMemberHasItemCb
        addMemberItemDescriptionEt = binding.addMemberItemDescriptionEt
        addMemberItemValueEt = binding.addMemberItemValueEt
        saveBtn = binding.saveBtn
    }

    private fun setupHasItemCb() {
        addMemberHasItemCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                addMemberItemDescriptionEt.isEnabled = true
                addMemberItemDescriptionEt.background = null
                addMemberItemValueEt.isEnabled = true
                addMemberItemValueEt.background = null
            } else {
                addMemberItemDescriptionEt.isEnabled = false
                addMemberItemDescriptionEt.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.edittext_background_disabled
                )!!
                addMemberItemValueEt.isEnabled = false
                addMemberItemValueEt.background = ContextCompat.getDrawable(
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
        if (addMemberNameEt.text.toString().isNullOrEmpty()) return false
        if (addMemberHasItemCb.isChecked) {
            if (addMemberItemDescriptionEt.text.toString().isNullOrEmpty()) return false
            if (addMemberItemValueEt.text.toString().isNullOrEmpty()) return false
        }
        return true
    }

    private fun createMemberWithFormValues() {
        var newMember = Member()
        newMember.name = addMemberNameEt.text.toString()
        if (addMemberHasItemCb.isChecked) {
            var item = Item(
                0,
                addMemberItemDescriptionEt.text.toString(),
                addMemberItemValueEt.text.toString().toDouble()
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
        findNavController().navigate(R.id.action_AddMemberFragment_to_MembersFragment)
    }

    private fun editMemberWithFormValues() {
        var newMember = Member()
        newMember.index = member.index
        newMember.name = addMemberNameEt.text.toString()
        if (addMemberHasItemCb.isChecked) {
            var item = Item(
                0,
                addMemberItemDescriptionEt.text.toString(),
                addMemberItemValueEt.text.toString().toDouble()
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
        findNavController().navigate(R.id.action_AddMemberFragment_to_MembersFragment)
    }
}