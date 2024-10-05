package com.example.hhplusweek2.domain.model

import java.time.Instant

data class LecturesByDate(
    val date: Instant,
    val lectures: List<Lecture>
)
