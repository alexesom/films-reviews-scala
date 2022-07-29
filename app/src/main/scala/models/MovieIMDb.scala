package models

import java.time.LocalDate

case class Ratings(Source: String,
                   Value: String)


case class MovieIMDb(Title: String,
                     Year: LocalDate,
                     Rated: String,
                     Released: LocalDate,
                     Runtime: String,
                     Genre: String,
                     Director: String,
                     Writer: String,
                     Actors: String,
                     Plot: String,
                     Language: String,
                     Country: String,
                     Awards: String,
                     Poster: String,
                     Ratings: Seq[Ratings],
                     Metascore: String,
                     imdbRating: String,
                     imdbVotes: String,
                     imdbID: String,
                     Type: String,
                     DVD: String,
                     BoxOffice: String,
                     Production: String,
                     Website: String,
                     Response: String)
