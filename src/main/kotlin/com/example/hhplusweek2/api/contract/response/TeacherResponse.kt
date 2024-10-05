package com.example.hhplusweek2.api.contract.response

import com.example.hhplusweek2.domain.model.Teacher

data class TeacherResponse(
    val id: Long,
    val name: String
) {
    constructor(teacher: Teacher) : this(
        teacher.id,
        teacher.name
    )
}
