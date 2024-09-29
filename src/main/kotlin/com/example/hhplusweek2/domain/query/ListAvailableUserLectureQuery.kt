package com.example.hhplusweek2.domain.query

import java.time.Instant

data class ListAvailableUserLectureQuery(
    val userId: Long,
    val dateUtc: Instant
) {
    fun validate() {
        val now = Instant.now()
        if (dateUtc < now) throw RuntimeException("$dateUtc cannot be before the current time $now")
        if (userId < 1) throw RuntimeException("User ID $userId cannot be less than 1.")
    }
}
