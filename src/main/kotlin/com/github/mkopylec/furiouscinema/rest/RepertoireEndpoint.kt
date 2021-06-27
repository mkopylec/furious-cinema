package com.github.mkopylec.furiouscinema.rest

import com.github.mkopylec.furiouscinema.core.FuriousCinema
import com.github.mkopylec.furiouscinema.core.NewRepertoire
import com.github.mkopylec.furiouscinema.core.NewScreening
import com.github.mkopylec.furiouscinema.core.RepertoireAddingResult
import com.github.mkopylec.furiouscinema.core.RepertoireAddingViolation
import com.github.mkopylec.furiouscinema.core.RepertoireLoadingResult
import com.github.mkopylec.furiouscinema.core.RepertoireLoadingViolation
import com.github.mkopylec.furiouscinema.core.ScreeningAddingResult
import com.github.mkopylec.furiouscinema.core.ScreeningAddingViolation
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
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
class RepertoireEndpoint(
    private val cinema: FuriousCinema
) {

    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    suspend fun addRepertoire(
        @RequestHeader(AUTHORIZATION) authorizationHeader: String,
        @RequestBody request: NewRepertoireRequest
    ): ResponseEntity<RepertoireAddingResult> {
        val repertoire = NewRepertoire(authorizationHeader.extractAuthenticationToken(), request.day)
        val result = cinema.addRepertoire(repertoire)
        return status(
            when (result.violation) {
                RepertoireAddingViolation.NOT_AUTHENTICATED -> NOT_FOUND
                RepertoireAddingViolation.NOT_AN_OWNER -> FORBIDDEN
                RepertoireAddingViolation.REPERTOIRE_ALREADY_EXISTS -> UNPROCESSABLE_ENTITY
                null -> OK
            }
        ).body(result)
    }

    @PutMapping(path = ["/{day}/screenings"], consumes = [APPLICATION_JSON_VALUE])
    suspend fun addScreening(
        @RequestHeader(AUTHORIZATION) authorizationHeader: String,
        @PathVariable day: DayOfWeek,
        @RequestBody request: NewScreeningRequest
    ): ResponseEntity<ScreeningAddingResult> {
        val screening = NewScreening(authorizationHeader.extractAuthenticationToken(), day, request.startTime, request.movieId, request.price)
        val result = cinema.addScreening(screening)
        return status(
            when (result.violation) {
                ScreeningAddingViolation.NOT_AUTHENTICATED -> UNAUTHORIZED
                ScreeningAddingViolation.NOT_AN_OWNER -> FORBIDDEN
                ScreeningAddingViolation.REPERTOIRE_NOT_FOUND,
                ScreeningAddingViolation.SCREENINGS_CLASH,
                ScreeningAddingViolation.INVALID_PRICE -> UNPROCESSABLE_ENTITY
                null -> OK
            }
        ).body(result)
    }

    @GetMapping("/{day}")
    suspend fun loadRepertoire(
        @PathVariable day: DayOfWeek
    ): ResponseEntity<RepertoireLoadingResult> {
        val result = cinema.loadRepertoire(day)
        return status(
            when (result.violation) {
                RepertoireLoadingViolation.REPERTOIRE_NOT_FOUND -> NOT_FOUND
                null -> OK
            }
        ).body(result)
    }
}
