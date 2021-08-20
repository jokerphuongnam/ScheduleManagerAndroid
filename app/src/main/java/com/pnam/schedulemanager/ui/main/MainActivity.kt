package com.pnam.schedulemanager.ui.main

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.databinding.ActivityMainBinding
import com.pnam.schedulemanager.model.database.domain.Reference
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.ui.base.BaseActivity
import com.pnam.schedulemanager.ui.login.LoginActivity
import com.pnam.schedulemanager.ui.main.schedules.SchedulesFragment
import com.pnam.schedulemanager.ui.main.setting.SettingFragment
import com.pnam.schedulemanager.ui.main.userinfo.UserInfoFragment
import com.pnam.schedulemanager.ui.scheduleInfo.InsertType
import com.pnam.schedulemanager.ui.scheduleInfo.ScheduleInfoActivity
import com.pnam.schedulemanager.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    @Inject
    lateinit var reference: Reference

    /**
     * set adapter for view pager 2
     * */
    private val mainAdapter: MainFragmentAdapter by lazy {
        MainFragmentAdapter(this).apply {
            addFragment(SchedulesFragment())
            addFragment(UserInfoFragment())
            addFragment(SettingFragment())
        }
    }

    /**
     * intent for login activity
     * */
    private val loginIntent by lazy {
        Intent(this, LoginActivity::class.java)
    }

    /**
     * intent start activity note (add note, edit note)
     * */
    private val addIntent: Intent by lazy {
        Intent(this, ScheduleInfoActivity::class.java)
    }

    /**
     * intent will start activity note (add note, edit note) will create list have 1 task
     * */
    private val addIntentWithTask: Intent by lazy {
        Intent(this, ScheduleInfoActivity::class.java).apply {
            putParcelableExtra(ScheduleInfoActivity.INSERT_TYPE, InsertType.CHECK_BOX)
        }
    }

    var refreshCallback: (()-> Unit)? = null

    /**
     * add content use for start activity for result
     * */
    val addContent: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                refreshCallback?.invoke()
            }
        }

    /**
     * event click for item bottom appbar
     * */
    private val bottomAppBarItemClick: OnMenuItemClickListener by lazy {
        OnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.checkbox -> {
                    addContent.launch(addIntentWithTask)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private val tabSelectedCallBack: TabLayout.OnTabSelectedListener by lazy {
        object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.mainViewPager.setCurrentItem(tab!!.position, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        }
    }

    /**
     * when user selected first tab ui will change:
     * - show fab add
     * - show bottom appbar
     * - allow exit appbar
     * */
    private fun changeUIWhenSelectedFirstTab() {
        binding.apply {
            tabWrap.animate().translationY(0F)
            actionBar.apply {
                setScrollFlag(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED)
                animate().translationY(0F)
                visibility = View.VISIBLE
            }
            addBtn.show()
        }
    }

    /**
     * when user selected other first tab ui will change:
     * - hide fab add
     * - hide bottom appbar
     * - don't allow exit appbar
     * */
    private fun changeUIWhenSelectedOtherTab() {
        binding.apply {
            tabWrap.animate()
                .translationY(tabWrap.height.toFloat())
            actionBar.apply {
                setScrollFlag(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL)
                animate().translationY(-actionBar.height.toFloat())
                visibility = View.GONE
            }
            addBtn.hide()
        }
    }

    /**
     * action when click floating action button add
     * */
    private val addAction: View.OnClickListener by lazy {
        View.OnClickListener {
            addContent.launch(addIntent)
        }
    }

    /**
     * if user launch app first time
     * make splashy (launcher) screen after success load splashy screen when setting for ui
     * */
    private fun splashyScreen() {
//        Splashy(this)
//            .setLogo(R.drawable.ic_logo)
//            .setTitle(R.string.app_name)
//            .setFullScreen(true)
//            .setAnimation(Splashy.Animation.SLIDE_IN_TOP_BOTTOM)
//            .setDuration(2000)
//            .show()
    }

    var cancelCallback: (()->Unit)? = null

    /**
     * if have data will set event to link pager with tabs
     * */
    private fun setUpUI() {
        viewModel.userLiveData.observe(userObservable)
        binding.apply {
            mainViewPager.apply {
                currentItem = 0
                adapter = mainAdapter
                registerOnPageChangeCallback(
                    object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            tabs.selectTab(tabs.getTabAt(position))
                            when (position) {
                                0 -> {

                                }
                                1 -> {
//                                    cancelCallback?.invoke()
                                }
                                2 -> {

                                }
                            }
                            if (position != 0) {
                                changeUIWhenSelectedOtherTab()
                            } else {
                                changeUIWhenSelectedFirstTab()
                            }
                        }
                    }
                )
            }
            tabs.addOnTabSelectedListener(tabSelectedCallBack)
            addBtn.setOnClickListener(addAction)
            tabWrap.setOnMenuItemClickListener(bottomAppBarItemClick)
        }
    }

    /**
     * init action (logout, show view) when get Long id
     * */
    private fun initAction(resource: Resource<String>) {
        when (resource) {
            is Resource.Loading -> {

            }
            is Resource.Success -> {
                setUpUI()
            }
            is Resource.Error -> {
                logout()
            }
        }
    }

    /**
     * logout
     * */
    fun logout() {
        slideSecondActivity(loginIntent)
    }

    /**
     * observer for get image avatar
     * */
    private val userObservable: Observer<Resource<User>> by lazy {
        Observer<Resource<User>> { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.avatar.setImageResource(R.drawable.ic_loading)
                }
                is Resource.Success -> {
                    binding.user = resource.data
                }
                is Resource.Error -> {
                    binding.avatar.setImageResource(R.drawable.ic_empty)
                }
            }
        }
    }

    override fun createUI() {
        viewModel.uidLiveData.observe { resource ->
            initAction(resource)
        }
    }

    /**
     * when back if in editable user info will cancel this mode
     * if have fragment in back stack will close this fragment
     * else will click first time will warning if continues click will kill app
     * */
    override fun onBackPressed() {
        if (
            binding.mainViewPager.currentItem == 1
            && (mainAdapter.getItem(1) as UserInfoFragment).isEditUse
        ) {
            cancelCallback?.invoke()
        } else if (isEmptyFragmentBackStack) {
            twiceTimeToExit()
        } else {
            mainAdapter.fragmentActivity.supportFragmentManager.popBackStack()
        }
    }

    override val viewModel: MainViewModel by viewModels()
}