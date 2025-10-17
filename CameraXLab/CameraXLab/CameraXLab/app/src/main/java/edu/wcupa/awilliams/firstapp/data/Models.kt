package edu.wcupa.awilliams.firstapp.data.db

enum class IdeaStatus { Draft, Ready, Shot }

data class Idea(
    val id: Int,
    val title: String,
    val tags: List<String>,
    val status: IdeaStatus
)

data class Shot(
    val id: Int,
    val text: String,
    val done: Boolean
)
