package com.example.hhplusweek2.repository

import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.repository.jpa.LectureEnrollmentsJpaRepository
import com.example.hhplusweek2.repository.model.LectureEnrollmentEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.random.Random

class LectureEnrollmentRepositoryImplTest {

    private val lectureEnrollmentsJpaRepository = mock(LectureEnrollmentsJpaRepository::class.java)
    private val sut = LectureEnrollmentRepositoryImpl(lectureEnrollmentsJpaRepository)

    @Test
    @DisplayName("특강 신청 내역을 정상적으로 저장한다")
    fun `save-when save lecture history, then succeed`() {
        // given
        val userId = Random.nextLong()
        val lectureId = Random.nextLong()
        val command = EnrollUserLectureCommand(userId, lectureId)
        val entity = LectureEnrollmentEntity(command)
        `when`(lectureEnrollmentsJpaRepository.save(entity)).thenReturn(entity)

        // when, then
        assertDoesNotThrow { sut.save(command) }
    }

    @Test
    @DisplayName("존재하는 특강의 아이디로 특강 내역을 찾으면, 특강 신청 내역을 반환한다")
    fun `findAllByLectureId-when find with existing lecture id, then return lecture enrollments`() {
        // given
        val userId = Random.nextLong()
        val lectureId = Random.nextLong()
        val command = EnrollUserLectureCommand(userId, lectureId)
        val entity = LectureEnrollmentEntity(command)
        `when`(lectureEnrollmentsJpaRepository.findAllByLectureId(lectureId)).thenReturn(listOf(entity))

        // when
        val actual = sut.findAllByLectureId(lectureId)

        // then
        val expected = listOf(entity.toModel())
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }

    @Test
    @DisplayName("존재하지 않는 특강 아이디로 특강 내역을 찾으면, 빈 배열을 반환한다")
    fun `findAllByLectureId-when`() {
        // given
        val userId = Random.nextLong()
        val lectureId = Random.nextLong()
        val command = EnrollUserLectureCommand(userId, lectureId)
        val entity = LectureEnrollmentEntity(command)
        `when`(lectureEnrollmentsJpaRepository.findAllByLectureId(lectureId)).thenReturn(listOf())

        // when
        val actual = sut.findAllByLectureId(lectureId)

        // then
        val expected = emptyList<LectureEnrollmentEntity>()
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }
}
