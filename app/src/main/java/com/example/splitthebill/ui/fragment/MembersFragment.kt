package com.example.splitthebill.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitthebill.R
import com.example.splitthebill.databinding.FragmentMembersBinding
import com.example.splitthebill.domain.model.Member
import com.example.splitthebill.domain.model.interfaces.MembersObserver
import com.example.splitthebill.domain.model.interfaces.OnItemClickListener
import com.example.splitthebill.domain.repository.MemberRepository
import com.example.splitthebill.ui.adapter.MemberAdapter
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get

class MembersFragment : Fragment(), MembersObserver, OnItemClickListener {

    private var _binding: FragmentMembersBinding? = null
    private val binding get() = _binding!!
    private lateinit var membersRv: RecyclerView
    private lateinit var memberAdapter: MemberAdapter

    private lateinit var members: MutableList<Member>
    private val memberRepository: MemberRepository = get()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMembersBinding.inflate(inflater, container, false)
        setupMembersList()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_MembersFragment_to_MemberFormFragment)
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        members = memberRepository.getAllMembers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_members_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_calculator -> {
                splitValueCalculator()
                return true
            }
            R.id.action_clean_data -> {
                showCleanDataConfirmationDialog()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupMembersList() {
        members = memberRepository.getAllMembers()

        memberAdapter = MemberAdapter(requireContext().applicationContext, members, this)
        memberAdapter.registerObserver(this)

        membersRv = binding.membersRecyclerView
        membersRv.layoutManager = LinearLayoutManager(this.context)
        membersRv.adapter = memberAdapter
    }

    private fun splitValueCalculator() {
        Snackbar.make(binding.root, "Calcular", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    private fun showCleanDataConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Limpar dados")
        builder.setMessage("Tem certeza de que deseja limpar todos os dados?")

        builder.setPositiveButton("Sim") { _, _ ->
            resetEntries()
            Snackbar.make(binding.root, "Dados limpados com sucesso", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        builder.setNegativeButton("Não") { _, _ ->
            Snackbar.make(binding.root, "Limpar dados cancelado", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun resetEntries() {
        memberRepository.clearAllMembers()
        onMembersChanged(memberRepository.getAllMembers())
        membersRv.adapter?.notifyDataSetChanged()
    }


    override fun onMembersChanged(updatedMembers: List<Member>) {
        memberAdapter.setMembers(updatedMembers)
        membersRv.adapter?.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("member", members[position])
        findNavController().navigate(R.id.action_MembersFragment_to_ItemsFragment, bundle)
    }

    override fun onItemLongPress(position: Int) {
        val itemView = membersRv.findViewHolderForAdapterPosition(position)?.itemView
        if (itemView != null) {
            val popupMenu = PopupMenu(requireContext(), itemView)
            popupMenu.menuInflater.inflate(R.menu.menu_member_options, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        val bundle = Bundle()
                        bundle.putSerializable("member", members[position])
                        findNavController().navigate(R.id.action_MembersFragment_to_MemberFormFragment, bundle)
                        true
                    }
                    R.id.action_delete -> {
                        val alertDialogBuilder = AlertDialog.Builder(requireContext())
                        alertDialogBuilder.setTitle("Deletar")
                        alertDialogBuilder.setMessage("Deseja deletar o membro ${members[position].name}?")
                        alertDialogBuilder.setPositiveButton("Sim") { _, _ ->
                            removeMember(position)
                            Snackbar.make(
                                binding.root,
                                "Membro deletado com sucesso",
                                Snackbar.LENGTH_LONG
                            )
                                .setAction("Action", null).show()
                        }
                        alertDialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        alertDialogBuilder.create().show()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    private fun removeMember(index: Int){
        memberRepository.removeMemberByIndex(index)
        onMembersChanged(memberRepository.getAllMembers())
    }
}