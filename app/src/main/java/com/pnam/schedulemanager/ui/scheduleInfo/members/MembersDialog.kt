package com.pnam.schedulemanager.ui.scheduleInfo.members

import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.view.setPadding
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.DialogMembersBinding
import com.pnam.schedulemanager.model.database.domain.Member
import com.pnam.schedulemanager.ui.base.BaseDialogFragment
import com.pnam.schedulemanager.ui.scheduleInfo.ScheduleInfoActivity
import com.pnam.schedulemanager.ui.scheduleInfo.ScheduleInfoViewModel
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MembersDialog : BaseDialogFragment<DialogMembersBinding, MembersViewModel>(
    R.layout.dialog_members
) {
    private fun setupSize() {
        dialog?.window?.let { window ->
            resources.displayMetrics.let { metrics ->
                val width = (metrics.widthPixels * 0.80).toInt()
                val height = (metrics.heightPixels * 0.70).toInt()
                window.setLayout(width, height)
            }
        }
    }

    private val searchItem: MenuItem by lazy {
        binding.toolbar.menu.findItem(R.id.search_member)
    }

    private val searchView: SearchView by lazy {
        searchItem.actionView as SearchView
    }

    private fun setupSearchView() {
        searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button).apply {
            setImageResource(R.drawable.ic_add)
            adjustViewBounds = true
            scaleType = ImageView.ScaleType.FIT_CENTER
            setPadding(36)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return if (query.isNotEmpty()) {
                    searchView.clearFocus()
                    viewModel.submitSearch(query)
                    true
                } else {
                    false
                }
            }

            override fun onQueryTextChange(newText: String): Boolean {
                binding.type = ShowTypeMember.SEARCH
                return if (newText.isNotEmpty()) {
                    viewModel.searchUser(newText)
                    true
                } else {
                    searchesAdapter.submitList(mutableListOf())
                    false
                }
            }
        })
        searchView.setOnCloseListener {
            binding.type = ShowTypeMember.MEMBERS
            return@setOnCloseListener false
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            inflateMenu(R.menu.members_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.leave -> {
                        activityViewModel.newSchedule.value?.let { schedule ->
                            if (schedule.members.size > 1) {
                                viewModel.leaveGroup(schedule)
                            } else {
                                viewModel.deleteSchedule(schedule)
                            }
                            requireActivity().onBackPressed()
                        }
                    }
                    R.id.close -> {
                        dismiss()
                    }
                }
                true
            }
        }
        setupSearchView()
    }

    private val membersAdapter: MembersAdapter by lazy {
        MembersAdapter()
    }

    private val searchesAdapter: SearchesAdapter by lazy {
        SearchesAdapter({ search ->
            search.searchId?.let { searchId ->
                viewModel.deleteSearch(searchId)
            }
        }, { search ->
            searchView.setQuery(search.word, true)
        })
    }

    private val searchResultsAdapter: SearchResultsAdapter by lazy {
        SearchResultsAdapter { search ->
            search.userId?.let { userId ->
                activityViewModel.newSchedule.value?.let { schedule ->
                    viewModel.addMember(userId, schedule.scheduleId)
                }
            }
        }
    }

    private fun setupBinding() {
        binding.apply {
            type = ShowTypeMember.MEMBERS
            members.adapter = membersAdapter
            members.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            search.adapter = searchesAdapter
            search.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            searchResults.adapter = searchResultsAdapter
            searchResults.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun setupViewModel() {
        viewModel.apply {
            searchUserLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        searchesAdapter.submitList(
                            resource.data.sortedWith(
                                compareBy { search ->
                                    if (search.searchId.isNullOrBlank()) {
                                        search.word
                                    } else {
                                        " ${search.word}"
                                    }
                                }
                            )
                        )
                    }
                    is Resource.Error -> {

                    }
                }
            }
            searchResultsLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        binding.type = ShowTypeMember.SEARCH_RESULTS
                        searchResultsAdapter.submitList(resource.data.sortedBy { it.word })
                    }
                    is Resource.Error -> {

                    }
                }
            }
            deleteSearchLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        searchView.setQuery(searchView.query, false)
                    }
                    is Resource.Error -> {

                    }
                }
            }
            addMemberLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        activityViewModel.getScheduleInfo()
                        dismiss()
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }
    }

    override fun createUI() {
        setupSize()
        setupToolbar()
        setupBinding()
        setupViewModel()
    }

    override fun onResume() {
        super.onResume()
        (arguments?.getParcelableArray(ScheduleInfoActivity.MEMBERS) as Array<Member>).let { members ->
            membersAdapter.submitList(members.toMutableList())
        }
    }

    override val viewModel: MembersViewModel by viewModels()
    private val activityViewModel: ScheduleInfoViewModel by activityViewModels()
}