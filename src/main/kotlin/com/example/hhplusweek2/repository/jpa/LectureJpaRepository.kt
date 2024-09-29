package com.example.hhplusweek2.repository.jpa

import com.example.hhplusweek2.repository.model.LectureEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface LectureJpaRepository : JpaRepository<LectureEntity, Long> {

    fun findAllByDateUtcBetween(startDateUtc: Instant, endDateUtc: Instant): List<LectureEntity>
}
