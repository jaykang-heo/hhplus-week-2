package com.example.hhplusweek2.domain

import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.domain.`interface`.LectureEnrollmentRepository
import com.example.hhplusweek2.domain.`interface`.LectureRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.random.Random
import kotlin.test.Test

class EnrollUserLectureCommandValidatorServiceTest {

    private val lectureRepository = mock(LectureRepository::class.java)
    private val lectureEnrollmentRepository = mock(LectureEnrollmentRepository::class.java)
    private val sut = EnrollUserLectureCommandValidatorService(lectureRepository, lectureEnrollmentRepository)

    @Test
    @DisplayName("특강이 존재하지 않으면 에러를 반환한다")
    fun `when lecture does not exist, then user cannot enroll`() {
        // given
        val userId = Random.nextLong(1, Long.MAX_VALUE)
        val lectureId = Random.nextLong()
        `when`(lectureRepository.findByIdWithLock(lectureId)).thenReturn(null)
        val command = EnrollUserLectureCommand(userId, lectureId)

        // when, then
        assertThrows<RuntimeException> { sut.validate(command) }
    }
}
