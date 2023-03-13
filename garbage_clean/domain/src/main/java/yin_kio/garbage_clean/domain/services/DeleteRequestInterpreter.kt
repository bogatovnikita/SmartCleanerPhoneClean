package yin_kio.garbage_clean.domain.services

import yin_kio.garbage_clean.domain.entities.DeleteRequest
import yin_kio.garbage_clean.domain.entities.GarbageFiles

internal class DeleteRequestInterpreter(
    private val garbageFiles: GarbageFiles
) {

    fun interpret(deleteRequest: DeleteRequest) : List<String>{
        val res = mutableListOf<String>()
        deleteRequest.forEach {
            garbageFiles[it]?.let { res.addAll(it) }
        }
        return res
    }

}