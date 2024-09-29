package com.example.hhplusweek2.domain

import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.domain.`interface`.LectureRepository
import org.springframework.stereotype.Service

@Service
class EnrollUserLectureCommandValidatorService(
    private val lectureRepository: LectureRepository
) {
    fun validate(command: EnrollUserLectureCommand) {
        lectureRepository.findById(command.lectureId)
            ?: throw RuntimeException("Cannot enroll lecture ${command.lectureId}. ${command.lectureId} does not exist")
    }
}
