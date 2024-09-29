package com.example.hhplusweek2.api.contract.response

import java.time.Instant

data class LectureByDateResponse(
    val dateUtc: Instant,
    val lectures: List<LectureResponse>
)
