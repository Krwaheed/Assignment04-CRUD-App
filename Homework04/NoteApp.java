
package com.mycompany.noteapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class NoteApp {

    private static final String FILENAME = "notes.json";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("1. Create note");
            System.out.println("2. Display notes");
            System.out.println("3. Update note");
            System.out.println("4. Delete note");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            
            switch (choice) {
                case 1:
                    System.out.print("Enter note: ");
                    String note = scanner.nextLine();
                    createNote(note);
                    break;
                case 2:
                    displayNotes();
                    break;
                case 3:
                    System.out.print("Enter note ID to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new note: ");
                    String newNote = scanner.nextLine();
                    updateNote(updateId, newNote);
                    break;
                case 4:
                    System.out.print("Enter note ID to delete: ");
                    int deleteId = scanner.nextInt();
                    deleteNote(deleteId);
                    break;
                case 5:
                    return;
            }
        }
    }

    private static JSONArray getNotes() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILENAME)));
            return new JSONArray(content);
        } catch (IOException e) {
            return new JSONArray();
        }
    }

    private static void saveNotes(JSONArray notes) {
        try (FileWriter file = new FileWriter(FILENAME)) {
            file.write(notes.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createNote(String note) {
        JSONArray notes = getNotes();
        JSONObject newNote = new JSONObject();
        newNote.put("id", notes.length());
        newNote.put("content", note);
        notes.put(newNote);
        saveNotes(notes);
    }

    private static void displayNotes() {
        JSONArray notes = getNotes();
        for (int i = 0; i < notes.length(); i++) {
            JSONObject note = notes.getJSONObject(i);
            System.out.println("ID: " + note.getInt("id") + ", Note: " + note.getString("content"));
        }
    }

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

