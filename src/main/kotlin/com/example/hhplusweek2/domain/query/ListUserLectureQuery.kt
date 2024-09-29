package com.example.hhplusweek2.domain.query

data class ListUserLectureQuery(
    val userId: Long
) {
    fun validate() {
        if (userId < 1) throw RuntimeException("User ID $userId cannot be less than 1.")
    }
}
