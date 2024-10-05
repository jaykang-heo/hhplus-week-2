package com.example.hhplusweek2.domain

import com.example.hhplusweek2.domain.`interface`.LectureEnrollmentRepository
import com.example.hhplusweek2.domain.`interface`.LectureRepository
import com.example.hhplusweek2.domain.model.Lecture
import com.example.hhplusweek2.domain.model.LectureEnrollment
import com.example.hhplusweek2.domain.query.ListAvailableUserLectureQuery
import com.example.hhplusweek2.stub.StubObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Instant
import kotlin.random.Random

class LectureEnrollmentServiceTest {

    private val mockLectureEnrollmentRepository = mock(LectureEnrollmentRepository::class.java)
    private val mockLectureRepository = mock(LectureRepository::class.java)
    private val sut = LectureEnrollmentService(mockLectureEnrollmentRepository, mockLectureRepository)

    @Test
    @DisplayName("특강이 없으면 빈 배열을 반환한다")
    fun `available-when lecture does not exist, then return empty list`() {
        // given
        val userId = Random.nextLong()
        val dateUtc = Instant.now()
        val query = ListAvailableUserLectureQuery(userId, dateUtc)
        `when`(mockLectureRepository.findAllByInDate(dateUtc)).thenReturn(emptyList())

        // when
        val actual = sut.available(query)

        // then
        assertThat(actual).isEqualTo(emptyList<Lecture>())
    }

    @Test
    @DisplayName("특강 신청 내역이 30개 미만이고 자신이 수강하지 않았으면, 특강을 반환한다")
    fun `available-when lecture enrollment count is under 30 and user is not enrolled, then return lecture`() {
        // given
        val userId = Random.nextLong()
        val teacher = StubObject.generateTeacher()
        val lecture = StubObject.generateLecture(teacher)
        val dateUtc = Instant.now()
        val lectureEnrollment = LectureEnrollment(Random.nextLong(), Random.nextLong(), lecture.id, dateUtc)
        val query = ListAvailableUserLectureQuery(userId, dateUtc)
        `when`(mockLectureRepository.findAllByInDate(dateUtc)).thenReturn(listOf(lecture))
        `when`(mockLectureEnrollmentRepository.findAllByLectureId(lecture.id)).thenReturn(listOf(lectureEnrollment))

        // when
        val actual = sut.available(query)

        // then
        val expected = listOf(lecture)
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
    }

    @Test
    @DisplayName("특강 신청 내역이 30개 미만이고 자신이 수강했으면, 특강을 반환한다")
    fun `available-when lecture enrollment count is under 30 and user is enrolled, then do not return that lecture`() {
        // given
        val userId = Random.nextLong()
        val teacher = StubObject.generateTeacher()
        val lecture = StubObject.generateLecture(teacher)
        val dateUtc = Instant.now()
        val lectureEnrollment = LectureEnrollment(Random.nextLong(), userId, lecture.id, Instant.now())
        val query = ListAvailableUserLectureQuery(userId, dateUtc)
        `when`(mockLectureRepository.findAllByInDate(dateUtc)).thenReturn(listOf(lecture))
        `when`(mockLectureEnrollmentRepository.findAllByLectureId(lecture.id)).thenReturn(listOf(lectureEnrollment))

        // when
        val actual = sut.available(query)

        // then
        assertThat(actual).isEqualTo(emptyList<Lecture>())
    }

    @Test
    @DisplayName("유저가 신청하지 않았지만 특강 신청 내역이 30개 이상이면, 빈 배열을 반환한다")
    fun `available-when lecture enrollment count is over 30, then return empty list`() {
        // given
        val userId = Random.nextLong()
        val teacher = StubObject.generateTeacher()
        val lecture = StubObject.generateLecture(teacher)
        val dateUtc = Instant.now()
        val lectureEnrollments = (1..30).map { LectureEnrollment(Random.nextLong(), Random.nextLong(), lecture.id, Instant.now()) }
        val query = ListAvailableUserLectureQuery(userId, dateUtc)
        `when`(mockLectureRepository.findAllByInDate(dateUtc)).thenReturn(listOf(lecture))
        `when`(mockLectureEnrollmentRepository.findAllByLectureId(lecture.id)).thenReturn(lectureEnrollments)

        // when
        val actual = sut.available(query)

        // then
        assertThat(actual).isEqualTo(emptyList<Lecture>())
    }
}
