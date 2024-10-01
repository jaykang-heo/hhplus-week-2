package com.example.hhplusweek2.domain.model

import java.time.Instant

data class Lecture(
    val id: Long,
    val title: String,
    val teacher: Teacher,
    val dateUtc: Instant,
    var registeredCount: Int
) {
    fun increaseRegisterCount() {
        registeredCount += 1
    }
}
