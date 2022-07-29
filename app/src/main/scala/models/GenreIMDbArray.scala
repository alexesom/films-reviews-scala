package models

case class GenreIMDbEntity(id: Long, name: String)

case class GenreIMDbArray(genres: Seq[GenreIMDbEntity])
