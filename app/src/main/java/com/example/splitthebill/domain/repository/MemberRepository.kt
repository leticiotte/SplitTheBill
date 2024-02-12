package com.example.splitthebill.domain.repository

import com.example.splitthebill.domain.model.Item
import com.example.splitthebill.domain.model.Member

interface MemberRepository {
    fun getAllMembers(): MutableList<Member>
    fun addMember(member: Member)
    fun removeMemberByIndex(index: Int)
    fun getMemberByIndex(index: Int): Member
    fun clearAllMembers()
    fun getMemberItems(memberIndex: Int): MutableList<Item>
    fun addMemberItem(memberIndex: Int, item: Item)
    fun removeMemberItemByIndex(memberIndex: Int, itemIndex: Int)
}