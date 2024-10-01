package com.example.hhplusweek2.repository

import com.example.hhplusweek2.repository.jpa.LectureEnrollmentsJpaRepository
import com.example.hhplusweek2.repository.jpa.LectureJpaRepository
import com.example.hhplusweek2.repository.model.LectureEnrollmentEntity
import com.example.hhplusweek2.repository.model.LectureEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Instant
import java.util.Optional
import java.util.UUID
import kotlin.random.Random

class LectureRepositoryImplTest {

    private val lectureEnrollmentsJpaRepository = mock(LectureEnrollmentsJpaRepository::class.java)
    private val lectureJpaRepository = mock(LectureJpaRepository::class.java)
    private val sut = LectureRepositoryImpl(lectureJpaRepository, lectureEnrollmentsJpaRepository)

    @Test
    @DisplayName("강의가 존재하지 않으면 반환하지 않는다")
    fun `findById-when lecture does not exist, then return null`() {
        // given
        val lectureId = Random.nextLong()
        `when`(lectureJpaRepository.findById(lectureId)).thenReturn(Optional.empty())

        // when
        val actual = sut.findById(lectureId)

        // then
        assertThat(actual).isNull()
    }

    @Test
    @DisplayName("특강이 존재하면, 특강을 반환한다")
    fun `findById-when lecture exists, then return lecture`() {
        // given
        val lectureId = Random.nextLong()
        val lecture = LectureEntity(lectureId, UUID.randomUUID().toString(), UUID.randomUUID().toString(), 0)
        `when`(lectureJpaRepository.findById(lectureId)).thenReturn(Optional.of(lecture))

        // when
        val actual = sut.findById(lectureId)

        // then
        val expected = lecture.toModel()
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }

    @Test
    @DisplayName("유저의 특강 내역이 존재하지 않으면, 빈 배열을 반환한다")
    fun `findByUserId-when lecture history does not exist, then return empty list`() {
        // given
        val userId = Random.nextLong()
        `when`(lectureEnrollmentsJpaRepository.findByUserId(userId)).thenReturn(emptyList())
        `when`(lectureJpaRepository.findAllById(emptyList())).thenReturn(emptyList())

        // when
        val actual = sut.findByUserId(userId)

        // then
        assertThat(actual).isEmpty()
    }

    @Test
    @DisplayName("유저의 특강 신청 내역이 있으면, 특강을 반환한다")
    fun `findByUserId-when user has enrolled lecture histories, then return lectures`() {
        // given
        val userId = Random.nextLong()
        val lectureId = Random.nextLong()
        val lectureEnrollmentEntity = LectureEnrollmentEntity(0, userId, lectureId, Instant.now())
        val lecture = LectureEntity(0, UUID.randomUUID().toString(), UUID.randomUUID().toString(), 0)
        `when`(lectureEnrollmentsJpaRepository.findByUserId(userId)).thenReturn(listOf(lectureEnrollmentEntity))
        `when`(lectureJpaRepository.findAllById(listOf(lectureEnrollmentEntity.lectureId))).thenReturn(listOf(lecture))

        // when
        val actual = sut.findByUserId(userId)

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(listOf(lecture.toModel()))
    }

    @Test
    @DisplayName("특강 전체 목록을 찾을때 특강이 없으면 빈 배열을 반환한다")
    fun `findAll-when lecture does not exist, then return empty list`() {
        // given
        `when`(lectureJpaRepository.findAll()).thenReturn(emptyList())

        // when
        val actual = sut.findAll()

        // then
        assertThat(actual).isEmpty()
    }

    @Test
    @DisplayName("특강이 존재하면, 모든 강의를 반환한다")
    fun `findAll-when lecture exists, then return list of lectures`() {
        // given
        val lectureEntity = LectureEntity(0, UUID.randomUUID().toString(), UUID.randomUUID().toString(), 0)
        `when`(lectureJpaRepository.findAll()).thenReturn(listOf(lectureEntity))

        // when
        val actual = sut.findAll()

        // then
        val expected = listOf(lectureEntity.toModel())
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }
}
