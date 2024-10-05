package com.example.hhplusweek2.fake

import com.example.hhplusweek2.domain.`interface`.LectureRepository
import com.example.hhplusweek2.domain.model.Lecture
import java.time.Instant

class FakeLectureRepository : LectureRepository {
    override fun save(lecture: Lecture) {
    }

    override fun getById(lectureId: Long): Lecture {
        TODO()
    }

    override fun findByIdWithLock(lectureId: Long): Lecture? {
        return null
    }

    override fun findByUserId(userId: Long): List<Lecture> = listOf()
    override fun findAllByInDate(date: Instant): List<Lecture> {
        return emptyList()
    }
}
