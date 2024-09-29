package com.example.hhplusweek2.stub

import com.example.hhplusweek2.domain.model.Lecture
import com.example.hhplusweek2.domain.model.Teacher
import java.time.Instant
import java.util.UUID
import kotlin.random.Random

object StubObject {
    fun generateTeacher(): Teacher {
        return Teacher(Random.nextLong(1, Long.MAX_VALUE), UUID.randomUUID().toString())
    }

    fun generateLecture(teacher: Teacher): Lecture {
        return Lecture(Random.nextLong(), UUID.randomUUID().toString(), teacher, Instant.now())
    }
}
