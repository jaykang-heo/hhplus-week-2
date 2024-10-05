package com.example.hhplusweek2.api.contract.response

import com.example.hhplusweek2.domain.model.Lecture
import java.time.Instant

class LectureResponse(
    val id: Long,
    val title: String,
    val teacher: TeacherResponse,
    val dateUtc: Instant
) {
    companion object {
        fun from(lecture: Lecture) = LectureResponse(
            id = lecture.id,
            title = lecture.title,
            teacher = TeacherResponse(lecture.teacher),
            dateUtc = lecture.dateUtc
        )
    }
}
