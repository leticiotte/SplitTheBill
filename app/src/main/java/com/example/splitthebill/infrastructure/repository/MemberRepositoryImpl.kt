package com.example.splitthebill.infrastructure.repository

import com.example.splitthebill.domain.model.Item
import com.example.splitthebill.domain.model.Member
import com.example.splitthebill.domain.repository.MemberRepository

class MemberRepositoryImpl : MemberRepository {
    private var members: MutableList<Member> = mutableListOf(
        Member(0, "John Doe", 50.0, mutableListOf(Item(0, "Coca cola", 12.0))),
        Member(1, "Jane Smith", 40.0, mutableListOf()),
        Member(2, "Bob Johnson", 60.0, mutableListOf()),
        Member(3, "Letícia Gonçalves", 0.0, mutableListOf()),
        Member(4, "Carlos Fernando", 0.0, mutableListOf())
    )

    override fun getAllMembers(): MutableList<Member> {
        return members
    }

    override fun addMember(member: Member) {
        member.index = members.size
        members.add(member)
    }

    override fun editMember(member: Member) {
        members[member.index!!] = member
    }

    override fun removeMemberByIndex(index: Int) {
        members.removeAt(index)
    }

    override fun getMemberByIndex(index: Int): Member {
        return members[index]
    }

    override fun clearAllMembers() {
        members.clear()
    }

    override fun getMemberItems(memberIndex: Int): MutableList<Item> {
        return members[memberIndex].items
    }

    override fun addMemberItem(memberIndex: Int, item: Item) {
        item.index = members[memberIndex].items.size
        members[memberIndex].items.add(item)
    }

    override fun removeMemberItemByIndex(memberIndex: Int, itemIndex: Int) {
        members[memberIndex].items.removeAt(itemIndex)
    }
}