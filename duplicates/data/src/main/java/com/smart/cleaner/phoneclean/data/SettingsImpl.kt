package com.smart.cleaner.phoneclean.data

import com.smart.cleaner.phoneclean.domain.gateways.DuplicatesSettings
import com.smart.cleaner.phoneclean.settings.Settings
import javax.inject.Inject

class SettingsImpl @Inject constructor(
    private val settings: Settings
): DuplicatesSettings {

    override fun saveDuplicatesDeleteTime() = settings.saveTimeDuplicateDelete()

}