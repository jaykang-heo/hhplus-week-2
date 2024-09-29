package com.example.hhplusweek2.api

import com.example.hhplusweek2.api.contract.request.EnrollUserLectureRequest
import com.example.hhplusweek2.api.contract.response.GetAvailableLectureResponse
import com.example.hhplusweek2.api.contract.response.LectureResponse
import com.example.hhplusweek2.api.contract.response.ListUserLectureResponse
import com.example.hhplusweek2.application.UserLectureFacade
import com.example.hhplusweek2.domain.command.EnrollUserLectureCommand
import com.example.hhplusweek2.domain.query.ListAvailableUserLectureQuery
import com.example.hhplusweek2.domain.query.ListUserLectureQuery
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/lectures")
class UserLectureController(
    private val userLectureFacade: UserLectureFacade
) {

    @PostMapping("/enroll")
    fun enrollLecture(
        @RequestBody request: EnrollUserLectureRequest
    ) {
        val command = EnrollUserLectureCommand(request)
        userLectureFacade.enroll(command)
    }

    @GetMapping("/enrollments/{userId}")
    fun listEnrollments(
        @PathVariable("userId") userId: Long
    ): ListUserLectureResponse {
        val query = ListUserLectureQuery(userId)
        val lectures = userLectureFacade.list(query)
        return ListUserLectureResponse(lectures.map { LectureResponse.from(it) })
    }

    @GetMapping("/available/{userId}")
    fun listAvailableLectures(
        @PathVariable userId: Long,
        @RequestParam("dateUtc") dateUtc: Instant
    ): GetAvailableLectureResponse {
        val query = ListAvailableUserLectureQuery(userId, dateUtc)
        val lectures = userLectureFacade.available(query)
        val lectureResponses = lectures.map { LectureResponse.from(it) }
        return GetAvailableLectureResponse(lectureResponses)
    }
}
