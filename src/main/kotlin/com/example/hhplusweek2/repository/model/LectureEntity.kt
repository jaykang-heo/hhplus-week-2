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
    val dateUtc: Instant,
    var registeredCount: Int
) {
    fun toModel(teacher: Teacher): Lecture {
        return Lecture(
            id,
            title,
            teacher,
            dateUtc,
            registeredCount
        )
    }

    constructor(lecture: Lecture) : this(
        lecture.id,
        lecture.title,
        lecture.teacher.id,
        lecture.dateUtc,
        lecture.registeredCount
    )
}
