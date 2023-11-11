package com.github.innovationforge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItem {
    private long id;           // Unique identifier for the to-do item
    private String title;      // Title or name of the to-do item
    private String description; // Detailed description of the to-do item
    private LocalDate dueDate; // Due date for the to-do item
    private boolean completed; // Flag to mark if the to-do item is completeds
    private Set<String> labels = new HashSet<>();
}