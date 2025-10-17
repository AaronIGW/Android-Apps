package com.example.dessertrelease.ui

enum class SortOrder { AZ, ZA }

data class DessertReleaseUiState(
    val isLinearLayout: Boolean = true,
    val sortOrder: SortOrder = SortOrder.AZ,
    val dynamicColor: Boolean = true,
    val favorites: Set<String> = emptySet(),
    val searchQuery: String = ""
)
