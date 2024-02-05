package com.example.splitthebill.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitthebill.R
import com.example.splitthebill.databinding.FragmentMembersBinding
import com.example.splitthebill.domain.model.Item
import com.example.splitthebill.domain.model.Member
import com.example.splitthebill.domain.model.interfaces.MembersObserver
import com.example.splitthebill.domain.model.interfaces.OnItemClickListener
import com.example.splitthebill.ui.adapter.MemberAdapter
import com.google.android.material.snackbar.Snackbar

class MembersFragment : Fragment(), MembersObserver, OnItemClickListener {

    private var _binding: FragmentMembersBinding? = null
    private lateinit var membersRecyclerView: RecyclerView
    private val binding get() = _binding!!

    private lateinit var members: MutableList<Member>;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMembersBinding.inflate(inflater, container, false)
        setupMembersList()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_MembersFragment_to_AddMemberFragment)
        }

        setHasOptionsMenu(true)
        return binding.root
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
        members = mutableListOf(
            Member("John Doe", 50.0, mutableListOf(Item("Coca cola", 12.0))),
            Member("Jane Smith", 40.0, mutableListOf()),
            Member("Bob Johnson", 60.0, mutableListOf()),
            Member("John Doe", 50.0, mutableListOf()),
            Member("Jane Smith", 40.0, mutableListOf()),
            Member("Bob Johnson", 60.0, mutableListOf()),
            Member("John Doe", 50.0, mutableListOf()),
            Member("Jane Smith", 40.0, mutableListOf()),
            Member("Bob Johnson", 60.0, mutableListOf()),
            Member("John Doe", 50.0, mutableListOf()),
            Member("Jane Smith", 40.0, mutableListOf()),
            Member("Bob Johnson", 60.0, mutableListOf()),
            Member("John Doe", 50.0, mutableListOf()),
            Member("Jane Smith", 40.0, mutableListOf()),
            Member("Bob Johnson", 60.0, mutableListOf()),
            Member("John Doe", 50.0, mutableListOf()),
            Member("Jane Smith", 40.0, mutableListOf()),
            Member("Bob Johnson", 60.0, mutableListOf()),
        )



        membersRecyclerView = binding.membersRecyclerView
        membersRecyclerView.layoutManager = LinearLayoutManager(this.context)
        val adapter = MemberAdapter(requireContext().applicationContext, members, this)
        adapter.registerObserver(this)
        membersRecyclerView.adapter = adapter
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
        members.clear()
        membersRecyclerView.adapter?.notifyDataSetChanged()
    }


    override fun onMembersChanged(updatedMembers: List<Member>) {
        members.clear()
        members.addAll(updatedMembers)
        membersRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun addMember(member: Member) {
        members.add(member)
        membersRecyclerView.adapter?.notifyItemInserted(members.size - 1)
    }


    private fun removeMember(position: Int) {
        members.removeAt(position)
        membersRecyclerView.adapter?.notifyItemRemoved(position)
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("member", members[position])
        findNavController().navigate(R.id.action_MembersFragment_to_ItemsFragment, bundle)
    }

    override fun onItemLongPress(position: Int) {
        TODO("Not yet implemented")
    }

}