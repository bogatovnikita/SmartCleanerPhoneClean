package com.softcleean.fastcleaner.ads

import androidx.lifecycle.ViewModel

class AdsViewModel: ViewModel() {

    private var isCanShowInter: Boolean = false

    fun canShowInter() {
        isCanShowInter = true
    }

    fun cantShowInter() {
        isCanShowInter = false
    }

    fun isCanShowInter() = isCanShowInter

}