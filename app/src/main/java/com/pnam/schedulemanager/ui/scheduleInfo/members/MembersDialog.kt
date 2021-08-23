package com.pnam.schedulemanager.ui.scheduleInfo.members

import android.util.Log
import androidx.fragment.app.viewModels
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.DialogMembersBinding
import com.pnam.schedulemanager.model.database.domain.Member
import com.pnam.schedulemanager.ui.base.BaseDialogFragment
import com.pnam.schedulemanager.ui.scheduleInfo.ScheduleInfoActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MembersDialog : BaseDialogFragment<DialogMembersBinding, MembersViewModel>(
    R.layout.dialog_members
) {
    override fun createUI() {
        (arguments?.getParcelableArray(ScheduleInfoActivity.MEMBERS) to Array<Member>::class).let { members ->
            Log.e("cccccccccccccccccc", members.toString())
        }
    }

    override val viewModel: MembersViewModel by viewModels()
}