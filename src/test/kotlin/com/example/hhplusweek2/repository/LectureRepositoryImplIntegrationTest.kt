package com.example.hhplusweek2.repository

import com.example.hhplusweek2.domain.`interface`.LectureRepository
import com.example.hhplusweek2.repository.jpa.LectureJpaRepository
import com.example.hhplusweek2.repository.jpa.TeacherJpaRepository
import com.example.hhplusweek2.repository.model.LectureEntity
import com.example.hhplusweek2.repository.model.TeacherEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.random.Random

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LectureRepositoryImplIntegrationTest(
    @Autowired private val lectureRepository: LectureRepository,
    @Autowired private val lectureJpaRepository: LectureJpaRepository,
    @Autowired private val teacherJpaRepository: TeacherJpaRepository
) {

    @Test
    @DisplayName("When lecture does not exist, then return null")
    fun `findById - when lecture does not exist, then return null`() {
        // given
        val lectureId = Random.nextLong()

        // when
        val actual = lectureRepository.findById(lectureId)

        // then
        assertThat(actual).isNull()
    }

    @Test
    @DisplayName("When lecture exists, then return lecture")
    fun `findById - when lecture exists, then return lecture`() {
        // given
        val teacher = teacherJpaRepository.save(
            TeacherEntity(
                id = 0,
                name = "Teacher Name"
            )
        )
        val lecture = lectureJpaRepository.save(
            LectureEntity(
                id = 0,
                title = "Lecture Title",
                teacherId = teacher.id,
                dateUtc = Instant.now()
            )
        )

        // when
        val actual = lectureRepository.findById(lecture.id)

        // then
        val expected = lecture.toModel(teacher.toModel())
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }

    @Test
    @DisplayName("When no lectures exist, then return empty list")
    fun `findAll - when no lectures exist, then return empty list`() {
        // when
        val actual = lectureRepository.findAllByInDate(Instant.now())

        // then
        assertThat(actual).isEmpty()
    }

    @Test
    @DisplayName("When lectures exist, then return list of lectures")
    fun `findAll - when lectures exist, then return list of lectures`() {
        // given
        val teacher = teacherJpaRepository.save(
            TeacherEntity(
                id = 0,
                name = "Teacher Name"
            )
        )
        val lecture = lectureJpaRepository.save(
            LectureEntity(
                id = 0,
                title = "Lecture Title",
                teacherId = teacher.id,
                dateUtc = Instant.now()
            )
        )

        // when
        val actual = lectureRepository.findAllByInDate(Instant.now())

        // then
        val expected = listOf(lecture.toModel(teacher.toModel()))
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }
}
