package com.example.hhplusweek2.stub

import com.example.hhplusweek2.domain.model.Lecture
import com.example.hhplusweek2.domain.model.Teacher
import com.example.hhplusweek2.repository.model.LectureEntity
import com.example.hhplusweek2.repository.model.TeacherEntity
import java.time.Instant
import java.util.UUID
import kotlin.random.Random

object StubObject {
    fun generateTeacher(): Teacher {
        return Teacher(Random.nextLong(1, Long.MAX_VALUE), UUID.randomUUID().toString())
    }

    fun generateLecture(teacher: Teacher): Lecture {
        return Lecture(Random.nextLong(), UUID.randomUUID().toString(), teacher, Instant.now(), 0)
    }

    fun generateLectureEntity(teacherId: Long): LectureEntity {
        return LectureEntity(0, UUID.randomUUID().toString(), teacherId, Instant.now(), 0)
    }

    fun generateTeacherEntity(): TeacherEntity {
        return TeacherEntity(0, UUID.randomUUID().toString())
    }
}
