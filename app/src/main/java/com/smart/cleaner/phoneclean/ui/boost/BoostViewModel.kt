package com.smart.cleaner.phoneclean.ui.boost

import androidx.lifecycle.viewModelScope
import com.smart.cleaner.phoneclean.models.BackgroundApp
import com.smart.cleaner.phoneclean.ui.base.BaseViewModel
import com.softcleean.fastcleaner.domain.boost.BoostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoostViewModel @Inject constructor(
    private val boostUseCase: BoostUseCase,
) : BaseViewModel<BoostScreenState>(BoostScreenState()) {

    fun getParams() {
        viewModelScope.launch(Dispatchers.IO) {
            val usedRam = toGb(boostUseCase.getRamUsage())
            val totalRam = toGb(boostUseCase.getTotalRam())
            updateState {
                it.copy(
                    usedRam = usedRam,
                    totalRam = totalRam,
                    isRamBoosted = boostUseCase.isRamBoosted(),
                    listBackgroundApp = mapToBackgroundApp(boostUseCase.getRunningApps()),
                    isLoadUseCase = true
                )
            }
        }
    }

    private fun mapToBackgroundApp(oldList: List<com.softcleean.fastcleaner.domain.models.BackgroundApp>) =
        oldList.map {
            BackgroundApp(name = it.name, icon = it.icon, packageName = it.packageName)
        }

    fun setUsageStatePermission(permissionGiven: Boolean) {
        updateState {
            it.copy(
                permissionGiven = permissionGiven
            )
        }
    }

    private fun toGb(size: Long): Double = size / 1000.0 / 1000.0 / 1000

}