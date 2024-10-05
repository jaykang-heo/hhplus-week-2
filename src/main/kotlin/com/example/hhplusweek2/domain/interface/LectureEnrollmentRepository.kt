package com.example.hhplusweek2.domain.`interface`

import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.domain.model.LectureEnrollment

interface LectureEnrollmentRepository {
    fun save(command: EnrollUserLectureCommand)
    fun findAllByLectureId(lectureId: Long): List<LectureEnrollment>
    fun findByLectureIdAndUserId(lectureId: Long, userId: Long): LectureEnrollment?
}
