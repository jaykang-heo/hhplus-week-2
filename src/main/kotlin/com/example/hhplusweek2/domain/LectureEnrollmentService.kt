package com.example.hhplusweek2.domain

import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.domain.`interface`.LectureEnrollmentRepository
import com.example.hhplusweek2.domain.`interface`.LectureRepository
import com.example.hhplusweek2.domain.model.Lecture
import com.example.hhplusweek2.domain.query.ListAvailableUserLectureQuery
import com.example.hhplusweek2.domain.query.ListUserLectureQuery
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class LectureEnrollmentService(
    private val lectureEnrollmentRepository: LectureEnrollmentRepository,
    private val lectureRepository: LectureRepository
) {

    fun enroll(command: EnrollUserLectureCommand) {
        lectureEnrollmentRepository.save(command)
    }

    fun list(query: ListUserLectureQuery): List<Lecture> {
        return lectureRepository.findByUserId(query.userId)
    }

    fun available(query: ListAvailableUserLectureQuery): List<Lecture> {
        val dateUtc: Instant = query.dateUtc
        val lectures = lectureRepository.findAllByInDate(dateUtc)
        return lectures.filter { lecture ->
            val totalEnrolled = lectureEnrollmentRepository.findAllByLectureId(lecture.id)
            val userEnrolled = totalEnrolled.any { it.userId == query.userId }
            !userEnrolled && totalEnrolled.size < MAXIMUM_ENROLL_COUNT
        }
    }

    companion object {
        private const val MAXIMUM_ENROLL_COUNT = 30
    }
}
