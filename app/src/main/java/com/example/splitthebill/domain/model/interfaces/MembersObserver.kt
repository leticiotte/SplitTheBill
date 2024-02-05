package com.example.splitthebill.domain.model.interfaces

import com.example.splitthebill.domain.model.Member

interface MembersObserver {
    fun onMembersChanged(members: List<Member>)
}
