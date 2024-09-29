package com.example.hhplusweek2.repository

import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.repository.jpa.LectureEnrollmentsJpaRepository
import com.example.hhplusweek2.repository.jpa.LectureJpaRepository
import com.example.hhplusweek2.repository.model.LectureEnrollmentEntity
import com.example.hhplusweek2.repository.model.LectureEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.random.Random

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LectureEnrollmentRepositoryImplIntegrationTest(
    @Autowired private val lectureEnrollmentRepository: LectureEnrollmentRepositoryImpl,
    @Autowired private val lectureEnrollmentsJpaRepository: LectureEnrollmentsJpaRepository,
    @Autowired private val lectureJpaRepository: LectureJpaRepository
) {

    @Test
    @DisplayName("When saving lecture enrollment, then succeed")
    fun `save - when saving lecture enrollment, then succeed`() {
        // given
        val userId = Random.nextLong()
        val lectureId = Random.nextLong()
        lectureJpaRepository.save(
            LectureEntity(
                id = lectureId,
                title = "Lecture Title",
                teacherId = Random.nextLong(),
                dateUtc = Instant.now()
            )
        )
        val command = EnrollUserLectureCommand(userId, lectureId)

        // when
        lectureEnrollmentRepository.save(command)

        // then
        val enrollments = lectureEnrollmentsJpaRepository.findByUserId(userId)
        assertThat(enrollments).hasSize(1)
        assertThat(enrollments[0].lectureId).isEqualTo(lectureId)
    }

    @Test
    @DisplayName("When finding enrollments by lecture ID, then return enrollments")
    fun `findAllByLectureId - when finding by existing lecture ID, then return enrollments`() {
        // given
        val userId = Random.nextLong()
        val lectureId = Random.nextLong()
        lectureJpaRepository.save(
            LectureEntity(
                id = lectureId,
                title = "Lecture Title",
                teacherId = Random.nextLong(),
                dateUtc = Instant.now()
            )
        )
        val enrollment = lectureEnrollmentsJpaRepository.save(
            LectureEnrollmentEntity(
                id = 0,
                userId = userId,
                lectureId = lectureId,
                enrolledAt = Instant.now()
            )
        )

        // when
        val actual = lectureEnrollmentRepository.findAllByLectureId(lectureId)

        // then
        val expected = listOf(enrollment.toModel())
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }

    @Test
    @DisplayName("When finding enrollments by non-existing lecture ID, then return empty list")
    fun `findAllByLectureId - when lecture ID does not exist, then return empty list`() {
        // given
        val lectureId = Random.nextLong()

        // when
        val actual = lectureEnrollmentRepository.findAllByLectureId(lectureId)

        // then
        assertThat(actual).isEmpty()
    }
}
