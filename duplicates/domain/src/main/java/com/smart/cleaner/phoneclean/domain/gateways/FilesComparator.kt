package com.smart.cleaner.phoneclean.domain.gateways

import com.smart.cleaner.phoneclean.domain.models.File


interface FilesComparator:  (File, File) -> Boolean