package com.example.hhplusweek2.repository

import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.domain.`interface`.LectureEnrollmentRepository
import com.example.hhplusweek2.domain.model.LectureEnrollment
import com.example.hhplusweek2.repository.jpa.LectureEnrollmentsJpaRepository
import com.example.hhplusweek2.repository.model.LectureEnrollmentEntity
import org.springframework.stereotype.Repository

@Repository
class LectureEnrollmentRepositoryImpl(
    private val lectureEnrollmentsJpaRepository: LectureEnrollmentsJpaRepository
) : LectureEnrollmentRepository {
    override fun save(command: EnrollUserLectureCommand) {
        val entity = LectureEnrollmentEntity(command)
        lectureEnrollmentsJpaRepository.save(entity)
    }

    override fun findAllByLectureId(lectureId: Long): List<LectureEnrollment> {
        return lectureEnrollmentsJpaRepository.findAllByLectureId(lectureId).map { it.toModel() }
    }
}
