package com.example.hhplusweek2.domain.model

import java.time.Instant

class LectureEnrollment(
    val id: Long,
    val userId: Long,
    val lectureId: Long,
    val enrolledAt: Instant
)
