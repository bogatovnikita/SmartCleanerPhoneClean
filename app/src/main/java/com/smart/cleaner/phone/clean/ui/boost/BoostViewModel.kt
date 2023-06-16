package com.smart.cleaner.phone.clean.ui.boost

import androidx.lifecycle.viewModelScope
import com.smart.cleaner.phone.clean.models.BackgroundApp
import com.smart.cleaner.phone.clean.ui.base.BaseViewModel
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
        updateState { it.copy(isLoadUseCase = false) }
        viewModelScope.launch(Dispatchers.IO) {
            val usedRam = toGb(boostUseCase.getRamUsage())
            val totalRam = toGb(boostUseCase.getTotalRam())
            val apps = boostUseCase.getRunningApps()
            updateState {
                it.copy(
                    usedRam = usedRam,
                    totalRam = totalRam,
                    isRamBoosted = boostUseCase.isRamBoosted(),
                    listBackgroundApp = mapToBackgroundApp(apps),
                    isNothingToKill = apps.isEmpty() && screenState.value.isPermissionGiven,
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
                isPermissionGiven = permissionGiven
            )
        }
    }

    private fun toGb(size: Long): Double = size / 1000.0 / 1000.0 / 1000

}