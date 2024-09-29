package com.example.hhplusweek2.repository.jpa

import com.example.hhplusweek2.repository.model.TeacherEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TeacherJpaRepository : JpaRepository<TeacherEntity, Long>
