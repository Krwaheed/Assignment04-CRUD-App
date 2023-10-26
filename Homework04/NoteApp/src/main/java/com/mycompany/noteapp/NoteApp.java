package com.mycompany.noteapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * This provides functionalities for a simple note taking application.
 * The application uses a JSON file (notes.json) for storage.
 * Each note consists of an ID and content.
 * 
 * @author Khawaja rohan waheed
 * Csc 340
 */
public class NoteApp {

    // Filename where notes are stored
    private static final String FILENAME = "notes.json";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            // Display the menu options
            System.out.println("1. Create a note");
            System.out.println("2. Display all the notes");
            System.out.println("3. Update a note");
            System.out.println("4. Delete a note");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt(); // Read user's choice
            scanner.nextLine();  // Consume newline
            
            switch (choice) {
                case 1:
                    System.out.print("Enter note: ");
                    String note = scanner.nextLine();
                    createNote(note); // Create a new note
                    break;
                case 2:
                    displayNotes(); // Call method to display all notes
                    break;
                case 3:
                    System.out.print("Enter note ID to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new note: ");
                    String newNote = scanner.nextLine();
                    updateNote(updateId, newNote); // Call method to update an exsiting note
                    break;
                case 4:
                    System.out.print("Enter note ID to delete: ");
                    int deleteId = scanner.nextInt();
                    deleteNote(deleteId); // Call method to delete a note
                    break;
                case 5:
                    return;
            }
        }
    }

    // Read and parse notes from the JSON file
    private static JSONArray getNotes() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILENAME)));
            return new JSONArray(content);
        } catch (IOException e) {
            // If the file doesn't exist or there is an error, return an empty array
            return new JSONArray();
        }
    }

    // Save notes to the JSON file
    private static void saveNotes(JSONArray notes) {
        try (FileWriter file = new FileWriter(FILENAME)) {
            file.write(notes.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create a new note and add it to the JSON array of notes
    private static void createNote(String note) {
        JSONArray notes = getNotes();
        JSONObject newNote = new JSONObject();
        newNote.put("id", notes.length());
        newNote.put("content", note);
        notes.put(newNote);
        saveNotes(notes);
    }

    // Display all existing notes
    private static void displayNotes() {
        JSONArray notes = getNotes();
        for (int i = 0; i < notes.length(); i++) {
            JSONObject note = notes.getJSONObject(i);
            System.out.println("ID: " + note.getInt("id") + ", Note: " + note.getString("content"));
        }
    }

    // Update the content of a note with a specified ID
    private static void updateNote(int id, String newNoteContent) {
        JSONArray notes = getNotes();
        for (int i = 0; i < notes.length(); i++) {
            JSONObject note = notes.getJSONObject(i);
            if (note.getInt("id") == id) {
                note.put("content", newNoteContent);
                saveNotes(notes);
                return;
            }
        }
        System.out.println("Note with ID: " + id + " not found.");
    }

    // Delete a note with a specified ID
    private static void deleteNote(int id) {
        JSONArray notes = getNotes();
        JSONArray newNotes = new JSONArray();
        
        for (int i = 0; i < notes.length(); i++) {
            JSONObject note = notes.getJSONObject(i);
            if (note.getInt("id") != id) {
                newNotes.put(note);
            }
        }
        
        saveNotes(newNotes);
    }
}
