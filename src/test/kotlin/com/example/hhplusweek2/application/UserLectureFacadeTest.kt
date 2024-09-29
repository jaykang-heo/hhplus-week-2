package com.example.hhplusweek2.application

import com.example.hhplusweek2.domain.LectureEnrollmentService
import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.domain.model.Lecture
import com.example.hhplusweek2.domain.query.ListAvailableUserLectureQuery
import com.example.hhplusweek2.domain.query.ListUserLectureQuery
import com.example.hhplusweek2.fake.FakeEnrollUserLectureCommandValidatorService
import com.example.hhplusweek2.fake.FakeLectureEnrollmentService
import com.example.hhplusweek2.stub.StubObject
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import kotlin.random.Random

class UserLectureFacadeTest {

    private val fakeEnrollUserLectureCommandValidatorService = FakeEnrollUserLectureCommandValidatorService()
    private val fakeLectureService = FakeLectureEnrollmentService()
    private val sut = UserLectureFacade(fakeEnrollUserLectureCommandValidatorService, fakeLectureService)

    @Test
    @DisplayName("특강 번호가 1보다 작으면 에러를 반환한다")
    fun `when lecture id is less than 1, then throw error`() {
        // given
        val userId = Random.nextLong(1, Long.MAX_VALUE)
        val lectureId = -1L
        val command = EnrollUserLectureCommand(userId, lectureId)

        // when, then
        assertThrows<RuntimeException> { sut.enroll(command) }
    }

    @Test
    @DisplayName("유저 아이디가 1보다 작으면 에러를 반환한다")
    fun `when enroll lecture and user id is less than 1, then throw error`() {
        // given
        val userId = -1L
        val lectureId = Random.nextLong(1, Long.MAX_VALUE)
        val command = EnrollUserLectureCommand(userId, lectureId)

        // when, then
        assertThrows<RuntimeException> { sut.enroll(command) }
    }

    @Test
    @DisplayName("유저는 특강을 신청 할 수 있다")
    fun `when user enroll lecture, then succeed`() {
        // given
        val userId = Random.nextLong(1, Long.MAX_VALUE)
        val lectureId = Random.nextLong(1, Long.MAX_VALUE)
        val command = EnrollUserLectureCommand(userId, lectureId)

        // when, then
        assertDoesNotThrow { sut.enroll(command) }
    }

    @Test
    @DisplayName("유저 아이디가 유효하지 않으면 특강 신청 내역을 조회 할 수 없다")
    fun `when user id is invalid then, user cannot find enrolled lecture history`() {
        // given
        val userId = -1L
        val query = ListUserLectureQuery(userId)

        // when, then
        assertThrows<RuntimeException> { sut.list(query) }
    }

    @Test
    @DisplayName("유저가 신청한 특강이 없으면 빈 배열을 반환 받는다")
    fun `when user does not have enrolled lectures, then return empty list`() {
        // given
        val userId = Random.nextLong(1, Long.MAX_VALUE)
        val query = ListUserLectureQuery(userId)
        val mockLectureEnrollmentService = mockk<LectureEnrollmentService>()
        val sut = UserLectureFacade(fakeEnrollUserLectureCommandValidatorService, mockLectureEnrollmentService)
        every { mockLectureEnrollmentService.list(query) } returns emptyList()

        // when
        val actual = sut.list(query)

        // then
        assertThat(actual).isEqualTo(emptyList<Lecture>())
    }

    @Test
    @DisplayName("유저가 신청한 특가이 있으면, 특강 목록을 반환 받는다")
    fun `when user has enrolled lectures, then return lecture list`() {
        // given
        val userId = Random.nextLong(1, Long.MAX_VALUE)
        val query = ListUserLectureQuery(userId)
        val teacher = StubObject.generateTeacher()
        val lecture = StubObject.generateLecture(teacher)
        val mockLectureEnrollmentService = mockk<LectureEnrollmentService>()
        val sut = UserLectureFacade(fakeEnrollUserLectureCommandValidatorService, mockLectureEnrollmentService)
        every { mockLectureEnrollmentService.list(query) } returns listOf(lecture)

        // when
        val actual = sut.list(query)

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(listOf(lecture))
    }

    @Test
    @DisplayName("신청 가능한 특강을 신청할때 유저 아이디가 1보다 작으면, 에러를 반환한다")
    fun `when list available lecture and user id is less than 1, then throw error`() {
        // given
        val userId = -1L
        val query = ListAvailableUserLectureQuery(userId, Instant.now())

        // when, then
        assertThrows<RuntimeException> { sut.available(query) }
    }

    @Test
    @DisplayName("신청 가능한 특강을 조회할때 조회하는 날짜가 과거이면, 에러를 반환한다")
    fun `when list available lectures with past date, then throw error`() {
        // given
        val userId = Random.nextLong(1, Long.MAX_VALUE)
        val pastDate = Instant.now().minusSeconds(1)
        val query = ListAvailableUserLectureQuery(userId, pastDate)

        // when, then
        assertThrows<RuntimeException> { sut.available(query) }
    }
}
