package com.example.hhplusweek2.repository.model

import com.example.hhplusweek2.domain.model.Teacher
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class TeacherEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String
) {
    fun toModel(): Teacher {
        return Teacher(id, name)
    }
}
