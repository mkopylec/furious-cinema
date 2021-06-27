package com.github.mkopylec.furiouscinema.rest

import com.github.mkopylec.furiouscinema.core.RepertoireAddingResult
import com.github.mkopylec.furiouscinema.core.RepertoireLoadingResult
import com.github.mkopylec.furiouscinema.core.ScreeningAddingResult
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.DayOfWeek

@RestController
@RequestMapping(path = ["/repertoires"], produces = [APPLICATION_JSON_VALUE])
class RepertoireEndpoint {

    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    suspend fun addRepertoire(
        @RequestHeader(AUTHORIZATION) authorizationHeader: String,
        @RequestBody request: NewRepertoireRequest
    ): ResponseEntity<RepertoireAddingResult> {
        TODO()
    }

    @PutMapping(path = ["/{day}/screenings"], consumes = [APPLICATION_JSON_VALUE])
    suspend fun addScreening(
        @RequestHeader(AUTHORIZATION) authorizationHeader: String,
        @PathVariable day: DayOfWeek,
        @RequestBody request: NewScreeningRequest
    ): ResponseEntity<ScreeningAddingResult> {
        TODO()
    }

    @GetMapping("/{day}")
    suspend fun loadRepertoire(
        @PathVariable day: DayOfWeek
    ): ResponseEntity<RepertoireLoadingResult> {
        TODO()
    }
}
