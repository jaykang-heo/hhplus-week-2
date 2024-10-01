package com.example.hhplusweek2.repository.jpa

import com.example.hhplusweek2.repository.model.LectureEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.time.Instant
import java.util.Optional

interface LectureJpaRepository : JpaRepository<LectureEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    override fun findById(id: Long): Optional<LectureEntity>
    fun findAllByDateUtcBetween(startDateUtc: Instant, endDateUtc: Instant): List<LectureEntity>
}
