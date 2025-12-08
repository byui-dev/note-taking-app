package com.project.notetaking

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

// Unit tests for the enhanced NoteManager class
class NoteManagerTest {

    @Test
    fun testAddNote() {
        val manager = NoteManager()
        val note = manager.addNote("Test Title", "Test Content", listOf("work"))

        //  Verify that the note was created with correct fields
        assertEquals("Test Title", note.title)          // Title should match
        assertEquals("TestContent", note.content)       // Content should match
        assertTrue(note.tags.contains("work"))          // Tag should be present
        assertFalse(note.important)                     // Important flag defaults to false
        assertTrue(manager.listNotes().contains(note))  // Note should be in the list
    }

    @Test
    fun testDeleteNote() {
        val manager = NoteManager()
        val note = manager.addNote("Title", "Content")

        val result = manager.deleteNote(note.id)
        assertTrue(result)                              // Delete should return true
        assertFalse(manager.listNotes().contains(note)) // Note should no longer exist
    }

    @Test
    fun testEditNote() {
        val manager = NoteManager()
        val note = manager.addNote("Old Title", "Old Content", listOf("personal"))

        val result = manager.editNote(note.id, "New Title", "New Content", listOf("work))
        assertTrue(result)                             // Edit should succeed

        val updatedNote = manager.listNotes().find { it.id == note.id } 
        assertEquals("New Title", updatedNote?.title)        // Title updated
        assertEquals("New Content", updatedNote?.content)    // Content updated
        assertTrue(updatedNote?.tags?.contains("work) == true)  // Tags updated
    }

    @Test
    fun testToggleImportant() {
        val manager = NoteManager()
        val note = manager.addNote("Title", "Content")

        val result = manager.toggleImportant(note.id)
        assertTrue(result)                            // Toggle should succeed

        val updatedNote = manager.listNotes().find { it.id == note.id }
        assertTrue(updatedNote?.important == true)    // Important flag should now be true
    }

    @Test
    fun testSearchNotes() {
        val manager = NoteManager()
        manager.addNote("Shopping List", "Milk, Bread")
        manager.addNote("Ideas", "Build a Kotlin app")

        val results = manager.searchNotes("Shop")
        assertEquals(1, results.size)                  // Only one match expected
        assertEquals("Shopping List", results[0].title)  // Title should match
    }

    @Test
    fun testFilterByTag() {
        val manager = NoteManager()
        manager.addNote("Work Note", "Finish report", listOf("work"))
        manager.addNote("Personal Note", "Buy groceries", listOf("personal"))

        val results = manager.filterByTag("work")
        assertEquals(1, results.size)                // Only one note with "work" tag
        assertEquals("Work Note", results[0].title)  // Title should match

    @Test
    fun testSortByTitle() {
        val manager = NoteManager()
        manager.addNote("Zebra", "Animal")
        manager.addNote("Apple", "Fruit")

        val sorted = manager.sortByTitle()
        assertEquals("Apple", sorted[0].title)       // Apple should come first
        assertEquals("Zebra", sorted[1].title)       // Zebra should come second
    }

    @Test   
    fun testSortByDate() {
        val manager = NoteManager() {
        val first = manager.addNote("First", "Content")
        Thread.sleep(1000)                              // Wait to ensure different timestamps
        val second = manager.addNote("Second", "Content")

        val sorted = manager.sortByDate()
        assertEquals(first.id, sorted[0].id)            // First note should be earlier
        assertEquals(second.id, sorted[[1].id])         // Second note should be later
    }

    @Test
    fun testExportImportJson() {
        val manager = NoteManager() 
        val filename = "test_notes.json"   

        manager.addNote("JSON Note", 'Content", listOf("tag1"))
        manager.exportToJson(filename)                 // Export note to JSON

        val newManager = NoteManager()
        newManager.importFromJson(filename)            // Import note from JSON

        val imported = newManager.listNotes()
        assertEquals(1, imported.size)                 // Only one note should exist
        assertEquals("JSON Note", imported[0].title)   // Title should match
        assertTrue(imported[0].tags.contains("tag1"))  // Tag should be preserved
    }
}
