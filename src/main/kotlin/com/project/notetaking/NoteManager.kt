package com.project.notetaking

import java.io.File
import java.time.LocalDateTime
import kotlinx.serialization.*
import kotlinx.serialization.json.*

// Serializable data class used for JSON export/import
@Serializable
data class SerializableNote(
    val id: Int,
    val title: String,
    val content: String,
    val timestamp: String,
    val important: Boolean,
    val tags: List<String>
)

// Main NoteManager class that handles all note operations
class NoteManager {
    private val notes  = mutableListOf<Note>() // Stores notes in memory
    private var nextId = 1                     // Tracks next available ID

    // Add a new note with optional tags
    fun addNote(title: String, content: String, tags: List<String> = emptyList()): Note {
        val note = Note(nextId++, title, content, LocalDateTime.now(), false, tags)
        notes.add(note)
        return note
    }

    // Return all notes
    fun listNotes(): List<Note> = notes

    // Delete a note by ID
    fun deleteNote(id: Int): Boolean {
        return notes.removeIf { it.id == id }
    }

    // Edit a note (update title/content/tags and timestamp)
    fun editNote(id: Int, newTitle: String, newContent: String, newTags: List<String>): Boolean {
        val note = notes.find { it.id == id }
        if (note != null) {
            notes[notes.indexOf(note)] = note.copy(
                title = newTitle,
                content = newContent,
                tags = newTags,
                timestamp = LocalDateTime.now()
            )
            return true
        }
        return false
    }

    // Toggle the important flag for a note
    fun toggleImportant(id: Int): Boolean {
        val note = notes.find { it.id == id }
        if (note != null) {
            notes[notes.indexOf(note)] = note.copy(
                important = !note.important,
                timestamp = LocalDateTime.now()
            )
            return true
        }
        return false
    }

    // Search notes by keyword in the title
    fun searchNotes(keyword: String): List<Note> {
        return notes.filter { it.title.contains(keyword, ignoreCase = true) }
    }

    // Filter notes by tag
    fun filterByTag(tag: String): List<Note> {
        return notes.filter { it.tags.contains(tag) }
    }

    // Sort notes alphabetically by title
    fun sortByTitle(): List<Note> {
        return notes.sortedBy { it.title }
    }

    // Sort notes by chronologically by timestamp
    fun sortByDate(): List<Note> {
        return notes.sortedBy { it.timestamp }
    }

    // Save notes to a text file (CSV-like format)
    fun saveNotesToFile(filename: String) {
        val file = File(filename)
        try {
            val serializable = notes.map { SerializableNote(it.id, it.title, it.content, it.timestamp.toString(), it.important, it.tags) }
            val jsonString = Json { prettyPrint = false }.encodeToString(serializable)
            file.writeText(jsonString)
        } catch (e: Exception) {
            // Fallback to simple CSV if JSON writing fails for some reason
            val csv = notes.joinToString("\n") {
                "${it.id},${it.title},${it.content},${it.timestamp},${it.important},${it.tags.joinToString(",") }"
            }
            file.writeText(csv)
        }
    }

    
    // Load notes from a text file
    fun loadNotesFromFile(filename: String) {
        val file = File(filename)
        if (file.exists()) {
            val text = file.readText()
            // Try JSON first (preferred)
            try {
                val serializableNotes = Json.decodeFromString<List<SerializableNote>>(text)
                notes.clear()
                for (sn in serializableNotes) {
                    val timestamp = try { LocalDateTime.parse(sn.timestamp) } catch (_: Exception) { LocalDateTime.now() }
                    notes.add(Note(sn.id, sn.title, sn.content, timestamp, sn.important, sn.tags))
                    if (sn.id >= nextId) nextId = sn.id + 1
                }
            } catch (je: Exception) {
                // Fallback to legacy CSV parsing if file isn't JSON
                val lines = text.split('\n')
                notes.clear()
                for (line in lines) {
                    if (line.isBlank()) continue
                    // Split into 6 parts only to avoid splitting commas inside content
                    val parts = line.split(',', limit = 6)
                    if (parts.size >= 6) {
                        val id = parts[0].toIntOrNull() ?: continue
                        val title = parts[1]
                        val content = parts[2]
                        val timestamp = try { LocalDateTime.parse(parts[3]) } catch (_: Exception) { LocalDateTime.now() }
                        val important = parts[4].toBoolean()
                        val tags = parts[5].split(',').map { it.trim() }.filter { it.isNotBlank() }
                        notes.add(Note(id, title, content, timestamp, important, tags))
                        if (id >= nextId) nextId = id + 1
                    }
                }
            }
        }
    }

    // Export notes to JSON file
    fun exportToJson(filename: String) {
        val file = File(filename)
        try {
            val SerializableNotes = notes.map {
                SerializableNote(it.id, it.title, it.content, it.timestamp.toString(), it.important, it.tags)
            }
            val jsonString = Json.encodeToString(SerializableNotes)
            file.writeText(jsonString)
        } catch (e: Exception) {
            println("Error exporting to JSON: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    // Import notes from JSON file
    fun importFromJson(filename: String) {
        val file = File(filename)
        if (file.exists()) {
            try {
                val jsonString = file.readText()
                val serializableNotes = Json.decodeFromString<List<SerializableNote>>(jsonString)
                notes.clear()
                for (sn in serializableNotes) {
                    val timestamp = LocalDateTime.parse(sn.timestamp)
                    notes.add(Note(sn.id, sn.title, sn.content, timestamp, sn.important, sn.tags))
                    if (sn.id >= nextId) nextId = sn.id + 1
                }
            } catch (e: Exception) {
                println("Error importing from JSON: ${e.message}")
                throw e
            }
        }
    }
}