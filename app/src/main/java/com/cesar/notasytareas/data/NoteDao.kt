package com.cesar.notasytareas.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cesar.notasytareas.model.Note

@Dao
interface NoteDao {

    @Insert
    fun insert(note: Note)

    @Query("SELECT * FROM Note WHERE type=1 ORDER BY date DESC")
    fun getAllTasks(): List<Note>

    @Query("SELECT * FROM Note WHERE type=2 ORDER BY id DESC")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM Note WHERE id= :id")
    fun getById(id: Int) : List<Note>

    @Query("SELECT * FROM Note WHERE title LIKE :title AND type=:type OR description LIKE :description AND type=:type")
    fun getByTitleDescription(title: String, description: String, type:Int) : List<Note>


    @Query("UPDATE Note set title = :title, description = :description, date = :date, hour = :hour, completed = :completed WHERE id = :id")
    fun updateTask(title: String,description: String,date: String,hour: String,completed: Boolean,id: Int)

    @Query("UPDATE Note set title = :title, description = :description WHERE id = :id")
    fun updateNote(title: String,description:String, id: Int)


    @Delete
    fun deleteNote(note: Note)

}