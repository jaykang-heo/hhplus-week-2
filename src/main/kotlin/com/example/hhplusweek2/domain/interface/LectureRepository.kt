package com.example.hhplusweek2.domain.`interface`

import com.example.hhplusweek2.domain.model.Lecture
import java.time.Instant

interface LectureRepository {
    fun save(lecture: Lecture)
    fun getById(lectureId: Long): Lecture
    fun findByIdWithLock(lectureId: Long): Lecture?
    fun findByUserId(userId: Long): List<Lecture>
    fun findAllByInDate(dateUtc: Instant): List<Lecture>
}
