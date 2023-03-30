package yin_kio.garbage_clean.domain.entities

import yin_kio.garbage_clean.domain.garbage_files.GarbageType

internal class DeleteRequest : MutableSet<GarbageType> by mutableSetOf()