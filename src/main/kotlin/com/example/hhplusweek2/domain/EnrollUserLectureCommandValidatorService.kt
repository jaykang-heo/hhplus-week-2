package com.example.hhplusweek2.domain

import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.domain.`interface`.LectureRepository
import org.springframework.stereotype.Service

@Service
class EnrollUserLectureCommandValidatorService(
    private val lectureRepository: LectureRepository
) {
    fun validate(command: EnrollUserLectureCommand) {
        val lecture = lectureRepository.findByIdWithLock(command.lectureId)
            ?: throw RuntimeException("Cannot enroll lecture ${command.lectureId}. ${command.lectureId} does not exist")

        if (lecture.registeredCount >= MAXIMUM_ENROLL_COUNT) {
            throw RuntimeException("Lecture enrollment exceeded maximum limit $MAXIMUM_ENROLL_COUNT of ${command.lectureId}")
        }
    }

    companion object {
        const val MAXIMUM_ENROLL_COUNT = 30
    }
}
