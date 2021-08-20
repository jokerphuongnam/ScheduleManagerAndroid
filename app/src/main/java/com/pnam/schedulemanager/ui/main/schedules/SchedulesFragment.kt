package com.pnam.schedulemanager.ui.main.schedules

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.FragmentSchedulesBinding
import com.pnam.schedulemanager.ui.base.BaseFragment
import com.pnam.schedulemanager.ui.main.MainActivity
import com.pnam.schedulemanager.ui.scheduleInfo.ScheduleInfoActivity
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SchedulesFragment : BaseFragment<FragmentSchedulesBinding, SchedulesViewModel>(R.layout.fragment_schedules) {
    private val schedulesAdapter: SchedulesAdapter by lazy {
        SchedulesAdapter(selectedItem, deleteItem)
    }

    private val selectedItem: (Schedule, List<View>) -> Unit by lazy {
        { note, sharedElements ->
            val context: Context = requireActivity()
            val intent = Intent(context, ScheduleInfoActivity::class.java)
            intent.putExtra(ScheduleInfoActivity.NOTE, note.scheduleId)
            when (context) {
                is MainActivity -> {
                    context.addContent.launch(intent, makeSceneTransitionAnimation(*sharedElements.toTypedArray()))
                }
            }
        }
    }

    private val deleteItem: (Schedule) -> Unit by lazy {
        { note ->
            viewModel.delete(note)
        }
    }

    private fun initNotesRecycler() {
        binding.notesRecycler.apply {
            adapter = schedulesAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }
    }

    override fun createUI() {
        initNotesRecycler()
        binding.notesRefresh.apply {
            setOnRefreshListener { viewModel.getSchedules() }
            isRefreshing = false
        }
        viewModel.apply {
            scheduleLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        lifecycleScope.launch {
                            schedulesAdapter.submitList(resource.data!!)
                        }
                        binding.notesRefresh.isRefreshing = false
                    }
                    is Resource.Error -> {

                    }
                }
            }
            deleteLiveData.observe{resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        showToast(getString(R.string.delete_success))
                    }
                    is Resource.Error -> {
                        showToast(getString(R.string.delete_error))
                    }
                }
            }
        }
    }

    override val viewModel: SchedulesViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is MainActivity -> {
                context.refreshCallback =  {
                    viewModel.scheduleLiveData
                }
            }
        }
    }
}