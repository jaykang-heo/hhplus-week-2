package com.example.hhplusweek2.domain.command

import com.example.hhplusweek2.api.contract.request.EnrollUserLectureRequest

data class EnrollUserLectureCommand(
    val userId: Long,
    val lectureId: Long
) {
    constructor(request: EnrollUserLectureRequest) : this(
        request.userId,
        request.lectureId
    )

    fun validate() {
        if (userId < 1) throw RuntimeException("User ID must be greater than 0.")
        if (lectureId < 1) throw RuntimeException("Lecture ID must be greater than 0.")
    }
}
