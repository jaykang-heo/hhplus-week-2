package com.example.hhplusweek2.fake

import com.example.hhplusweek2.domain.EnrollUserLectureCommandValidatorService
import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand

class FakeEnrollUserLectureCommandValidatorService : EnrollUserLectureCommandValidatorService(FakeLectureRepository(), FakeLectureEnrollmentRepository()) {
    override fun validate(command: EnrollUserLectureCommand) {}
}
