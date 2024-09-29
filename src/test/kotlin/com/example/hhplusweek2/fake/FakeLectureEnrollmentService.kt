package com.example.hhplusweek2.fake

import com.example.hhplusweek2.domain.LectureEnrollmentService
import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand

class FakeLectureEnrollmentService : LectureEnrollmentService(FakeLectureEnrollmentRepository(), FakeLectureRepository()) {
    override fun enroll(command: EnrollUserLectureCommand) {
    }
}
