package com.example.hhplusweek2.fake

import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.domain.`interface`.LectureEnrollmentRepository
import com.example.hhplusweek2.domain.model.LectureEnrollment

class FakeLectureEnrollmentRepository : LectureEnrollmentRepository {
    override fun save(command: EnrollUserLectureCommand) {}
    override fun findAllByLectureId(lectureId: Long): List<LectureEnrollment> {
        return emptyList()
    }
}
