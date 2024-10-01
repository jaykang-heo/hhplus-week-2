package com.example.hhplusweek2.api

import com.example.hhplusweek2.api.contract.request.EnrollUserLectureRequest
import com.example.hhplusweek2.domain.`interface`.LectureEnrollmentRepository
import com.example.hhplusweek2.repository.jpa.LectureJpaRepository
import com.example.hhplusweek2.repository.jpa.TeacherJpaRepository
import com.example.hhplusweek2.stub.StubObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class UserLectureControllerIntegrationTest(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val lectureEnrollmentRepository: LectureEnrollmentRepository,
    @Autowired private val lectureJpaRepository: LectureJpaRepository,
    @Autowired private val teacherJpaRepository: TeacherJpaRepository
) {

    @Test
    @DisplayName("특강은 최대 30명까지만 신청 할 수 있다")
    fun `when users enroll in lecture exactly 30 times, then all succeed`() {
        // given
        val teacherEntity = StubObject.generateTeacherEntity()
        val teacherId = teacherJpaRepository.save(teacherEntity).id
        val lectureEntity = StubObject.generateLectureEntity(teacherId)
        val lectureId = lectureJpaRepository.save(lectureEntity).id
        val enrollRequests = (1..30).map { userId ->
            EnrollUserLectureRequest(userId.toLong(), lectureId)
        }

        // when, then
        enrollRequests.map { request ->
            webTestClient.post()
                .uri("/lectures/enroll")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk
        }
        val actual = lectureEnrollmentRepository.findAllByLectureId(lectureId)
        assertThat(actual).hasSize(30)
    }

    @Test
    @DisplayName("특강은 최대 30명까지만 신청 할 수 있다 - 31명 시도")
    fun `when users enroll in lecture more than 30 times, then throw error`() {
        // given
        val teacherEntity = StubObject.generateTeacherEntity()
        val teacherId = teacherJpaRepository.save(teacherEntity).id
        val lectureEntity = StubObject.generateLectureEntity(teacherId)
        val lectureId = lectureJpaRepository.save(lectureEntity).id
        val enrollRequests = (1..31).map { userId ->
            EnrollUserLectureRequest(userId.toLong(), lectureId)
        }
        enrollRequests.take(30).forEach { request ->
            webTestClient.post()
                .uri("/lectures/enroll")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk
        }

        // when, then
        webTestClient.post()
            .uri("/lectures//enroll")
            .bodyValue(enrollRequests[30])
            .exchange()
            .expectStatus().is5xxServerError
    }

    @Test
    @DisplayName("동시에 동일한 특강에 40명이 신청하면, 30명만 성공한다")
    fun `when 40 users try to enroll in same lecture, then only 30 succeed`() {
        // given
        val teacherEntity = StubObject.generateTeacherEntity()
        val teacherId = teacherJpaRepository.save(teacherEntity).id
        val lectureEntity = StubObject.generateLectureEntity(teacherId)
        val lectureId = lectureJpaRepository.save(lectureEntity).id
        val enrollRequests = (1..40).map { userId ->
            EnrollUserLectureRequest(userId.toLong(), lectureId)
        }
        val latch = CountDownLatch(40)

        // when
        val futures = enrollRequests.map { request ->
            CompletableFuture.runAsync {
                try {
                    webTestClient.post()
                        .uri("/lectures/enroll")
                        .bodyValue(request)
                        .exchange()
                        .expectStatus()
                } catch (_: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await(10, TimeUnit.SECONDS)

        // then
        CompletableFuture.allOf(*futures.toTypedArray()).join()
        val actualEnrollments = lectureEnrollmentRepository.findAllByLectureId(lectureId)
        assertThat(actualEnrollments).hasSize(30)
    }

    @Test
    @DisplayName("동시에 동일한 특강에 100명이 신청하면, 30명만 성공한다")
    fun `when 100 users try to enroll in same lecture, then only 30 succeed`() {
        // given
        val teacherEntity = StubObject.generateTeacherEntity()
        val teacherId = teacherJpaRepository.save(teacherEntity).id
        val lectureEntity = StubObject.generateLectureEntity(teacherId)
        val lectureId = lectureJpaRepository.save(lectureEntity).id
        val enrollRequests = (1..100).map { userId ->
            EnrollUserLectureRequest(userId.toLong(), lectureId)
        }
        val latch = CountDownLatch(100)

        // when
        val futures = enrollRequests.map { request ->
            CompletableFuture.runAsync {
                try {
                    webTestClient.post()
                        .uri("/lectures/enroll")
                        .bodyValue(request)
                        .exchange()
                        .expectStatus()
                } catch (_: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await(10, TimeUnit.SECONDS)

        // then
        CompletableFuture.allOf(*futures.toTypedArray()).join()
        val actualEnrollments = lectureEnrollmentRepository.findAllByLectureId(lectureId)
        val actualRegisteredCount = lectureJpaRepository.findById(lectureId).get().registeredCount
        assertThat(actualEnrollments).hasSize(30)
        assertThat(actualRegisteredCount).isEqualTo(30)
    }
}
