package com.example.hhplusweek2.domain.model

import java.time.Instant

class LectureHistory(
    val id: Long,
    val userId: Long,
    val lectureId: Long,
    val createdAt: Instant
)
