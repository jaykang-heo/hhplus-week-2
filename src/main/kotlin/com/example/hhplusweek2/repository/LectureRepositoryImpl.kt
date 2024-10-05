package com.example.hhplusweek2.repository

import com.example.hhplusweek2.domain.`interface`.LectureRepository
import com.example.hhplusweek2.domain.model.Lecture
import com.example.hhplusweek2.repository.jpa.LectureEnrollmentsJpaRepository
import com.example.hhplusweek2.repository.jpa.LectureJpaRepository
import com.example.hhplusweek2.repository.jpa.TeacherJpaRepository
import com.example.hhplusweek2.repository.model.LectureEntity
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.ZoneOffset

@Repository
class LectureRepositoryImpl(
    private val lectureJpaRepository: LectureJpaRepository,
    private val teacherJpaRepository: TeacherJpaRepository,
    private val lectureEnrollmentsJpaRepository: LectureEnrollmentsJpaRepository
) : LectureRepository {
    override fun save(lecture: Lecture) {
        val entity = LectureEntity(lecture)
        lectureJpaRepository.save(entity)
    }

    override fun getById(lectureId: Long): Lecture {
        val lectureEntity = lectureJpaRepository.findById(lectureId).get()
        val teacherEntity = teacherJpaRepository.findById(lectureEntity.teacherId).get()
        return lectureEntity.toModel(teacherEntity.toModel())
    }

    override fun findByIdWithLock(lectureId: Long): Lecture? {
        val lectureEntity = lectureJpaRepository.findById(lectureId).orElse(null) ?: return null
        val teacherEntity = teacherJpaRepository.findById(lectureEntity.teacherId).get()
        return lectureEntity.toModel(teacherEntity.toModel())
    }

    override fun findByUserId(userId: Long): List<Lecture> {
        val lectureIds = lectureEnrollmentsJpaRepository.findByUserId(userId).map { it.lectureId }.toSet()
        val lectures = lectureJpaRepository.findAllById(lectureIds)
        val teacherIds = lectures.map { it.teacherId }.toSet()
        val teachers = teacherJpaRepository.findAllById(teacherIds).associateBy { it.id }
        return lectures.mapNotNull { lectureEntity ->
            val teacherEntity = teachers[lectureEntity.teacherId]!!
            val teacherModel = teacherEntity.toModel()
            lectureEntity.toModel(teacherModel)
        }
    }

    override fun findAllByInDate(dateUtc: Instant): List<Lecture> {
        val zoneId = ZoneOffset.UTC
        val localDate = dateUtc.atZone(zoneId).toLocalDate()
        val startOfDay = localDate.atStartOfDay(zoneId).toInstant()
        val endOfDay = localDate.plusDays(1).atStartOfDay(zoneId).toInstant()
        val lectures = lectureJpaRepository.findAllByDateUtcBetween(startOfDay, endOfDay)
        val teacherIds = lectures.map { it.teacherId }.toSet()
        val teachers = teacherJpaRepository.findAllById(teacherIds).associateBy { it.id }
        return lectures.mapNotNull { lectureEntity ->
            val teacherEntity = teachers[lectureEntity.teacherId]
            val teacherModel = teacherEntity?.toModel()
            teacherModel?.let { lectureEntity.toModel(it) }
        }
    }
}
