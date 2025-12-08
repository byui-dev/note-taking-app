package com.project.notetaking

fun main() {
    val manager = NoteManager()
    val filename = "notes.txt"   // Text file persistence
    val jsonFile = "notes.json"  // JSON export/import
    manager.loadNotesFromFile(filename)  // Load notes at startup
    
    // Welcome header
    println("Welcome to the Note-Taking App")

    // Infinite loop until user chooses to exit
    while (true) {
        println(
            """
            Choose an option:
            1) Add
            2) List
            3) Delete
            4) Edit
            5) Search
            6) Filter by Tag
            7) Toggle Important
            8) Sort by Title
            9) Sort by Date
            10) Save
            11) Export to JSON
            12) Import from JSON
            13) Exit
            """.trimIndent()
        )

        // Read user input once per loop; if stdin is closed (readLine() == null)
        // exit gracefully to avoid spinning on `Invalid option.`
        val choice = readLine()
        if (choice == null) {
            println("Input closed. Exiting.")
            return
        }

        when (choice.trim()) {
            "1" -> {
                // Add a new note
                print("Enter title: ")
                val title = readLine() ?: ""
                print("Enter content: ")
                val content = readLine() ?: ""
                print("Enter tags (comma separated): ")
                val tags = readLine()?.split(",")?.map { it.trim() } ?: emptyList()
                val note = manager.addNote(title, content, tags)
                println("Note added:")
                println("${note.id}: ${note.title} - ${note.content} (Tags: ${note.tags.joinToString(", ")}) [Important: ${note.important}] Last updated: ${note.timestamp}")
            }
            "2" -> {
                // List all notes
                println("All Notes:")
                manager.listNotes().forEach {
                    println("${it.id}: ${it.title} - ${it.content} (Tags: ${it.tags.joinToString(", ")}) [Important: ${it.important}] Last updated: ${it.timestamp}")
                }
            }
            "3" -> {
                // Delete a note
                print("Enter note ID to delete: ")
                val id = readLine()?.toIntOrNull()
                if (id != null && manager.deleteNote(id)) {
                    println("Note deleted.")
                } else {
                    println("Note not found.")
                }
            }
            "4" -> {
                // Edit a note
                print("Enter note ID to edit: ")
                val id = readLine()?.toIntOrNull()
                if (id != null) {
                    print("Enter new title: ")
                    val newTitle = readLine() ?: ""
                    print("Enter new content: ")
                    val newContent = readLine() ?: ""
                    print("Enter new tags (comma separated): ")
                    val newTags = readLine()?.split(",")?.map { it.trim() } ?: emptyList()
                    if (manager.editNote(id, newTitle, newContent, newTags)) {
                        println("Note updated.")
                    } else {
                        println("Note not found.")
                    }
                } else {
                    println("Invalid note ID.")
                }
            }
            "5" -> {
                // Search notes
                print("Enter keyword to search: ")
                val keyword = readLine() ?: ""
                val results = manager.searchNotes(keyword)
                if (results.isEmpty()) {
                    println("No notes found.")
                } else {
                    println("Search Results:")
                    results.forEach {
                        println("${it.id}: ${it.title} - ${it.content} (Tags: ${it.tags.joinToString(", ")}) [Important: ${it.important}] Last updated: ${it.timestamp}")
                    }
                }
            }
            "6" -> {
                // Filter by tag
                print("Enter tag to filter: ")
                val tag = readLine() ?: ""
                val results = manager.filterByTag(tag)
                if (results.isEmpty()) {
                    println("No notes found with tag '$tag'.")
                } else {
                    println("Filter Results:")
                    results.forEach {
                        println("${it.id}: ${it.title} - ${it.content} (Tags: ${it.tags.joinToString(", ")}) [Important: ${it.important}] Last updated: ${it.timestamp}")
                    }
                }
            }
            "7" -> {
                // Toggle important flag
                print("Enter note ID to toggle important: ")
                val id = readLine()?.toIntOrNull()
                if (id != null && manager.toggleImportant(id)) {
                   println("Note importance toggled.") 
                } else {
                    println("Note not found.")
                }
            }    
            "8" -> {
                // Sort by title
                println("Notes sorted by title:")
                manager.sortByTitle().forEach {
                    println("${it.id}: ${it.title} - ${it.content} (Tags: ${it.tags.joinToString(", ")}) [Important: ${it.important}] Last updated: ${it.timestamp}")
                }
            }
            "9" -> {
                // Sort by date
                println("Notes sorted by date:")
                manager.sortByDate().forEach {
                    println("${it.id}: ${it.title} - ${it.content} (Tags: ${it.tags.joinToString(", ")}) [Important: ${it.important}] Last updated: ${it.timestamp}")
                }
            }
            "10" -> {
                // Save notes to text file
                manager.saveNotesToFile(filename)
                println("Notes saved to $filename")
            }
            "11" -> {
                // Export notes to JSON
                manager.exportToJson(jsonFile)
                println("Notes exported to $jsonFile")
            }
            "12" -> {
                // Import notes from JSON
                manager.importFromJson(jsonFile)
                println("Notes imported from $jsonFile")
            }
            "13" -> {
                // Exit program (save before exit)
                manager.saveNotesToFile(filename)
                println("Notes saved. Goodbye!")
                return
            }
            else -> println("Invalid option.")
        }
    }
}

