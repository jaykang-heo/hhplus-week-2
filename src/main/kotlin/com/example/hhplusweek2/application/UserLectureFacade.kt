package com.example.hhplusweek2.application

import com.example.hhplusweek2.domain.EnrollUserLectureCommandValidatorService
import com.example.hhplusweek2.domain.LectureEnrollmentService
import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.domain.model.Lecture
import com.example.hhplusweek2.domain.query.ListAvailableUserLectureQuery
import com.example.hhplusweek2.domain.query.ListUserLectureQuery
import org.springframework.stereotype.Service

@Service
class UserLectureFacade(
    private val enrollUserLectureCommandValidatorService: EnrollUserLectureCommandValidatorService,
    private val lectureEnrollmentService: LectureEnrollmentService
) {
    fun enroll(command: EnrollUserLectureCommand) {
        command.validate()
        enrollUserLectureCommandValidatorService.validate(command)
        lectureEnrollmentService.enroll(command)
    }

    fun list(query: ListUserLectureQuery): List<Lecture> {
        query.validate()
        return lectureEnrollmentService.list(query)
    }

    fun available(query: ListAvailableUserLectureQuery): List<Lecture> {
        query.validate()
        return lectureEnrollmentService.available(query)
    }
}
