package com.example.hhplusweek2.repository.model

import com.example.hhplusweek2.domain.model.Lecture
import com.example.hhplusweek2.domain.model.Teacher
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "lectures")
data class LectureEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val title: String,
    val teacherId: Long,
    val dateUtc: Instant
) {
    fun toModel(teacher: Teacher): Lecture {
        return Lecture(
            id,
            title,
            teacher,
            dateUtc
        )
    }
}
