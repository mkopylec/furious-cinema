package com.github.mkopylec.furiouscinema.core

import org.springframework.stereotype.Service
import java.time.DayOfWeek

@Service
class FuriousCinema {

    suspend fun loadMovies(): MoviesLoadingResult {
        TODO()
    }

    suspend fun loadMovie(movieId: String): MovieLoadingResult {
        TODO()
    }

    suspend fun vote(vote: NewVote): VotingResult {
        TODO()
    }

    suspend fun addRepertoire(repertoire: NewRepertoire): RepertoireAddingResult {
        TODO()
    }

    suspend fun addScreening(screening: NewScreening): ScreeningAddingResult {
        TODO()
    }

    suspend fun loadRepertoire(day: DayOfWeek): RepertoireLoadingResult {
        TODO()
    }
}
