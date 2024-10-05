package com.example.hhplusweek2.repository.model

import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.domain.model.LectureEnrollment
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "lecture_enrollments")
data class LectureEnrollmentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val userId: Long,
    val lectureId: Long,
    val enrolledAt: Instant
) {
    fun toModel(): LectureEnrollment {
        return LectureEnrollment(
            id,
            userId,
            lectureId,
            enrolledAt
        )
    }

    constructor(command: EnrollUserLectureCommand) : this(
        0,
        command.userId,
        command.lectureId,
        Instant.now()
    )
}
