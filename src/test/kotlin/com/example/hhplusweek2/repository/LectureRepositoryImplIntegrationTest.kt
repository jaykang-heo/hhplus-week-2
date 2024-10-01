package com.example.hhplusweek2.repository

import com.example.hhplusweek2.domain.`interface`.LectureRepository
import com.example.hhplusweek2.repository.jpa.LectureJpaRepository
import com.example.hhplusweek2.repository.jpa.TeacherJpaRepository
import com.example.hhplusweek2.repository.model.TeacherEntity
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
class LectureRepositoryImplIntegrationTest(
    @Autowired private val lectureRepository: LectureRepository,
    @Autowired private val lectureJpaRepository: LectureJpaRepository,
    @Autowired private val teacherJpaRepository: TeacherJpaRepository
) {

    @Test
    @DisplayName("특강이 존재하지 않으면, null을 반환한다")
    fun `findById - when lecture does not exist, then return null`() {
        // given
        val lectureId = Random.nextLong()

        // when
        val actual = lectureRepository.findByIdWithLock(lectureId)

        // then
        assertThat(actual).isNull()
    }

    @Test
    @DisplayName("특강이 존재하면, 특강을 반환한다")
    fun `findById - when lecture exists, then return lecture`() {
        // given
        val teacher = teacherJpaRepository.save(
            TeacherEntity(
                id = 0,
                name = "Teacher Name"
            )
        )
        val lecture = lectureJpaRepository.save(StubObject.generateLectureEntity(teacher.id))

        // when
        val actual = lectureRepository.findByIdWithLock(lecture.id)

        // then
        val expected = lecture.toModel(teacher.toModel())
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }

    @Test
    @DisplayName("특강이 존재하지 않으면, 빈 리스트를 반환한다")
    fun `findAll - when no lectures exist, then return empty list`() {
        // when
        val actual = lectureRepository.findAllByInDate(Instant.now())

        // then
        assertThat(actual).isEmpty()
    }

    @Test
    @DisplayName("특강이 존재하면, 특강 리스트를 반환한다")
    fun `findAll - when lectures exist, then return list of lectures`() {
        // given
        val teacher = teacherJpaRepository.save(
            TeacherEntity(
                id = 0,
                name = "Teacher Name"
            )
        )
        val lecture = lectureJpaRepository.save(StubObject.generateLectureEntity(teacher.id))

        // when
        val actual = lectureRepository.findAllByInDate(Instant.now())

        // then
        val expected = listOf(lecture.toModel(teacher.toModel()))
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }
}
