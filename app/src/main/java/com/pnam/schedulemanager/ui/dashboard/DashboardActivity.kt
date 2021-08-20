package com.pnam.schedulemanager.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityDashboardBinding
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.login.LoginActivity
import com.pnam.schedulemanager.ui.scheduleInfo.ScheduleInfoActivity
import com.pnam.schedulemanager.ui.setting.SettingActivity
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DashboardActivity :
    BaseActivity<ActivityDashboardBinding, DashboardViewModel>(R.layout.activity_dashboard) {
    private val schedulesAdapter: SchedulesAdapter by lazy {
        SchedulesAdapter(selectedItem, deleteItem)
    }

    private val selectedItem: (Schedule, List<View>) -> Unit by lazy {
        { note, sharedElements ->
            intent.putExtra(ScheduleInfoActivity.NOTE, note.scheduleId)
            addContent.launch(intent, makeSceneTransitionAnimation(*sharedElements.toTypedArray()))
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

    val addContent: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.scheduleLiveData
            }
        }


    private fun setupViewMode() {
        viewModel.apply {
            scheduleLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        lifecycleScope.launch {
                            schedulesAdapter.submitList(resource.data)
                        }
                        binding.schedulesRefresh.isRefreshing = false
                    }
                    is Resource.Error -> {

                    }
                }
            }
            deleteLiveData.observe { resource ->
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
            userLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        toolbar.loading()
                    }
                    is Resource.Success -> {
                        if (resource.data == null) {
                            toolbar.cancelAnimation()
                            logout()
                        } else {
                            toolbar.setUser(resource.data)
                        }
                    }
                    is Resource.Error -> {
                        toolbar.emptyAvatar()
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            addBtn.setOnClickListener {
                addContent.launch(Intent(this@DashboardActivity, ScheduleInfoActivity::class.java))
            }
            schedulesRefresh.setOnRefreshListener { viewModel.getSchedules() }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        toolbar.onCreate()
    }

    private val toolbar: DashboardToolbar by lazy {
        DashboardToolbar()
    }

    private var _searchItem: MenuItem? = null
    private val searchItem: MenuItem get() = _searchItem!!
    private val searchItemId: Int by lazy { R.id.search }
    private var _searchView: SearchView? = null
    private val searchView: SearchView
        get() = _searchView ?: synchronized(this) {
            _searchView ?: (searchItem.actionView as SearchView).also {
                _searchView = it
            }
        }


    private var _profileItem: MenuItem? = null
    private val profileItem: MenuItem get() = _profileItem!!

    private fun setupUser() {
        toolbar.setBinding(profileItem.actionView.findViewById(R.id.container))
        toolbar.avatarClick = View.OnClickListener {
            slideSecondActivity(
                Intent(this@DashboardActivity, SettingActivity::class.java).apply {
                    when (val resoruce = viewModel.userLiveData.value) {
                        is Resource.Success -> {
                            putExtra(USER, resoruce.data)
                        }
                    }
                },
                makeSceneTransitionAnimation(toolbar.binding.avatar).toBundle()
            )
        }
    }

    private val loginIntent by lazy {
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    private val searchQuery: SearchView.OnQueryTextListener by lazy {
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return if (query.isEmpty()) {
                    false
                } else {
                    searchView.clearFocus()
                    true
                }
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {

                } else {

                }
                return true
            }
        }
    }

    private val searchExpandItem: MenuItem.OnActionExpandListener by lazy {
        object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                searchView.setQuery("", false)
                searchView.setOnQueryTextListener(searchQuery)
                searchView.queryHint = "${getString(R.string.search_schedules)}…"
                searchView.requestFocus()
                toolbar.setVisible(false)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                toolbar.setVisible(true)
                return true
            }
        }
    }

    private fun setUpSearchItem() {
        searchItem.setOnActionExpandListener(searchExpandItem)
    }

    fun logout() {
        slideSecondActivity(loginIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        _searchItem = menu.findItem(searchItemId)
        _profileItem = menu.findItem(R.id.profile)
        setUpSearchItem()
        setupUser()
        return true
    }

    override fun createUI() {
        setupToolbar()
        initNotesRecycler()
        setupViewMode()
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
    }

    override val viewModel: DashboardViewModel by viewModels()

    companion object {
        const val USER: String = "user"
    }
}