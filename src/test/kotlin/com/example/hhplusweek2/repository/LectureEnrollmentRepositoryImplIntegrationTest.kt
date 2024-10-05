package com.example.hhplusweek2.repository

import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.repository.jpa.LectureEnrollmentsJpaRepository
import com.example.hhplusweek2.repository.jpa.LectureJpaRepository
import com.example.hhplusweek2.repository.model.LectureEnrollmentEntity
import com.example.hhplusweek2.stub.StubObject
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
class LectureEnrollmentRepositoryImplIntegrationTest(
    @Autowired private val lectureEnrollmentRepository: LectureEnrollmentRepositoryImpl,
    @Autowired private val lectureEnrollmentsJpaRepository: LectureEnrollmentsJpaRepository,
    @Autowired private val lectureJpaRepository: LectureJpaRepository
) {

    @Test
    @DisplayName("특강 신청을 저장할 때, 성공한다")
    fun `save - when saving lecture enrollment, then succeed`() {
        // given
        val userId = Random.nextLong()
        val lectureId = Random.nextLong()
        val teacherId = Random.nextLong()
        lectureJpaRepository.save(StubObject.generateLectureEntity(teacherId))
        val command = EnrollUserLectureCommand(userId, lectureId)

        // when
        lectureEnrollmentRepository.save(command)

        // then
        val enrollments = lectureEnrollmentsJpaRepository.findByUserId(userId)
        assertThat(enrollments).hasSize(1)
        assertThat(enrollments[0].lectureId).isEqualTo(lectureId)
    }

    @Test
    @DisplayName("특강 ID로 신청 내역을 찾을 때, 신청 내역을 반환한다")
    fun `findAllByLectureId - when finding by existing lecture ID, then return enrollments`() {
        // given
        val userId = Random.nextLong()
        val lectureId = Random.nextLong()
        val teacherId = Random.nextLong()
        val lectureEntity = StubObject.generateLectureEntity(teacherId)
        lectureJpaRepository.save(lectureEntity)
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
    @DisplayName("존재하지 않는 특강 ID로 신청 내역을 찾을 때, 빈 리스트를 반환한다")
    fun `findAllByLectureId - when lecture ID does not exist, then return empty list`() {
        // given
        val lectureId = Random.nextLong()

        // when
        val actual = lectureEnrollmentRepository.findAllByLectureId(lectureId)

        // then
        assertThat(actual).isEmpty()
    }
}
