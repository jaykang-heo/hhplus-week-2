package com.example.hhplusweek2.repository.jpa

import com.example.hhplusweek2.repository.model.LectureEnrollmentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface LectureEnrollmentsJpaRepository : JpaRepository<LectureEnrollmentEntity, Long> {
    fun findByUserId(userId: Long): List<LectureEnrollmentEntity>
    fun findAllByLectureId(lectureId: Long): List<LectureEnrollmentEntity>
}
