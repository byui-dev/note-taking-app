package com.project.notetaking

import java.time.LocalDateTime

// Data class to represent a Note in the system
// Includes ID, title, content, timestamp, importance flag, and tags
data class Note(
    val id: Int,                 // Unique identifier for the note
    val title: String,           // Short title of the note
    val content: String,         // The actual text/content of the note
    val timestamp: LocalDateTime, //  When the note was created or last edited
    val important: Boolean = false, // Flag to mark note as important (default false)
    val tags: List<String> = emptyList() // Tags for categorization (e.g., "work", "personal")
)