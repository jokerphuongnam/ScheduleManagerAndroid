package com.pnam.schedulemanager.ui.main.userinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pnam.schedulemanager.databinding.BottomSheetControlImageBinding
import com.pnam.schedulemanager.ui.setting.SettingActivity

class ControlImageBottomSheet(private val controlImageCallBack: ControlImageCallBack): BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetControlImageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        when(requireContext()){
            is SettingActivity ->{
                binding = BottomSheetControlImageBinding.inflate(inflater, container, false)
//        binding.chooseImage.setOnClickListener { controlImageCallBack.chooseImage() }
                binding.openCamera.setOnClickListener { controlImageCallBack.openCamera() }
//        binding.deleteImage.setOnClickListener { controlImageCallBack.deleteImage() }
            }
        }
        return binding.root
    }

    interface ControlImageCallBack{
        fun chooseImage()
        fun openCamera()
        fun deleteImage()
    }
}