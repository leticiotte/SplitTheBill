package com.example.splitthebill.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitthebill.R
import com.example.splitthebill.databinding.FragmentItemsBinding
import com.example.splitthebill.domain.model.Item
import com.example.splitthebill.domain.model.Member
import com.example.splitthebill.domain.model.interfaces.ItemsObserver
import com.example.splitthebill.domain.model.interfaces.OnItemClickListener
import com.example.splitthebill.domain.model.interfaces.ToolbarConfig
import com.example.splitthebill.domain.repository.MemberRepository
import com.example.splitthebill.ui.adapter.ItemAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ItemsFragment : Fragment(), ItemsObserver, OnItemClickListener, ToolbarConfig {

    private var _binding: FragmentItemsBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemsTitleTv: TextView
    private lateinit var itemsRv: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var itemsFab: FloatingActionButton
    private lateinit var toolbar: MaterialToolbar

    private var member: Member = Member()
    private var items: MutableList<Item> = mutableListOf()
    private val memberRepository: MemberRepository = get()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemsTitleTv = binding.itemsTitleTv

        findAndLinkToolbar()
        setupNavigationOnClickListener()
        setupItemsFab()
        setupItemsList()

        arguments?.let {
            member = (it.getSerializable("member") as? Member)!!
            itemsTitleTv.text =
                view.context.getString(R.string.formatted_item_header, member.name)

            if (items != member.items) {
                items = member.items
                itemAdapter.setItems(items)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reloadFragmentData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemsChanged(updatedItems: List<Item>) {
        itemAdapter.setItems(updatedItems)
        if (updatedItems.isNotEmpty()) itemsFab.hide()
        else itemsFab.show()
        itemsRv.adapter?.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("member", member)
        bundle.putSerializable("item", items[position])
        findNavController().navigate(R.id.action_ItemsFragment_to_ItemFormFragment, bundle)
    }

    override fun onItemLongPress(position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Deletar")
        alertDialogBuilder.setMessage("Deseja deletar o item ${items[position].description}?")
        alertDialogBuilder.setPositiveButton("Sim") { _, _ ->
            removeMemberItem(position)
            Snackbar.make(binding.root, "Item deletado com sucesso", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        alertDialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }

    override fun findAndLinkToolbar() {
        toolbar = requireActivity().findViewById(R.id.toolbar)
    }

    override fun setupNavigationOnClickListener() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_ItemsFragment_to_MembersFragment)
        }
    }

    private fun setupItemsList() {
        itemAdapter = ItemAdapter(requireContext().applicationContext, items, this)
        itemAdapter.registerObserver(this)

        itemsRv = binding.itemsRv
        itemsRv.layoutManager = LinearLayoutManager(this.context)
        itemsRv.adapter = itemAdapter
    }

    private fun setupItemsFab() {
        itemsFab = binding.itemsFab

        itemsFab.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("member", member)
            findNavController().navigate(R.id.action_ItemsFragment_to_ItemFormFragment, bundle)
        }
    }

    private fun reloadFragmentData() {
        arguments?.let {
            member = (it.getSerializable("member") as? Member)!!
            itemsTitleTv.text =
                binding.root.context.getString(R.string.formatted_item_header, member.name)

            items = memberRepository.getMemberItems(member.index!!)
            onItemsChanged(items)
        }
    }

    private fun removeMemberItem(itemIndex: Int) {
        memberRepository.removeMemberItemByIndex(member.index!!, itemIndex)
        member = memberRepository.getMemberByIndex(member.index!!)
        items = member.items
        onItemsChanged(items)
    }
}