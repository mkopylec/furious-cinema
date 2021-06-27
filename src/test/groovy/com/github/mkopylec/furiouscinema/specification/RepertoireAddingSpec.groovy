package com.github.mkopylec.furiouscinema.specification

import com.github.mkopylec.furiouscinema.rest.NewRepertoireRequest

import static com.github.mkopylec.furiouscinema.core.RepertoireAddingViolation.NOT_AN_OWNER
import static com.github.mkopylec.furiouscinema.core.RepertoireAddingViolation.NOT_AUTHENTICATED
import static com.github.mkopylec.furiouscinema.core.RepertoireAddingViolation.REPERTOIRE_ALREADY_EXISTS
import static com.github.mkopylec.furiouscinema.utils.AuthenticationTokens.INVALID_AUTHENTICATION_TOKEN
import static com.github.mkopylec.furiouscinema.utils.AuthenticationTokens.MOVIEGOER_AUTHENTICATION_TOKEN
import static com.github.mkopylec.furiouscinema.utils.AuthenticationTokens.OWNER_AUTHENTICATION_TOKEN
import static java.time.DayOfWeek.SATURDAY
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNAUTHORIZED
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

class RepertoireAddingSpec extends BasicSpec {

    def "Should add a daily repertoire"() {
        given:
        def request = new NewRepertoireRequest(SATURDAY)

        when:
        def response = cinema.addRepertoire(OWNER_AUTHENTICATION_TOKEN, request)

        then:
        with(response) {
            statusCode == OK
            with(body) {
                value == null
                violation == null
            }
        }
    }

    def "Should deny to add a daily repertoire when user is not authenticated"() {
        given:
        def request = new NewRepertoireRequest(SATURDAY)

        when:
        def response = cinema.addRepertoire(INVALID_AUTHENTICATION_TOKEN, request)

        then:
        with(response) {
            statusCode == UNAUTHORIZED
            with(body) {
                value == null
                violation == NOT_AUTHENTICATED
            }
        }
    }

    def "Should deny to add a daily repertoire when user is not an owner"() {
        given:
        def request = new NewRepertoireRequest(SATURDAY)

        when:
        def response = cinema.addRepertoire(MOVIEGOER_AUTHENTICATION_TOKEN, request)

        then:
        with(response) {
            statusCode == FORBIDDEN
            with(body) {
                value == null
                violation == NOT_AN_OWNER
            }
        }
    }

    def "Should not to add a daily repertoire when it already exists"() {
        given:
        def request = new NewRepertoireRequest(SATURDAY)
        cinema.addRepertoire(OWNER_AUTHENTICATION_TOKEN, request)

        when:
        def response = cinema.addRepertoire(OWNER_AUTHENTICATION_TOKEN, request)

        then:
        with(response) {
            statusCode == UNPROCESSABLE_ENTITY
            with(body) {
                value == null
                violation == REPERTOIRE_ALREADY_EXISTS
            }
        }
    }
}
