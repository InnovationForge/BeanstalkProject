package com.github.innovationforge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItem {
    private long id;           // Unique identifier for the to-do item
    @Size(max = 24, message = "The title must have a maximum length of 24 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "The title must not contain special characters.")
    private String title;      // Title or name of the to-do item
    @Size(max = 240, message = "The description must have a maximum length of 240 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "The description must not contain special characters.")
    private String description; // Detailed description of the to-do item
    @FutureOrPresent(message = "Date must be in the future or present.")
    private LocalDate dueDate; // Due date for the to-do item
    private boolean completed; // Flag to mark if the to-do item is completeds
    @Size(max = 3, message = "Labels set can have a maximum of 3 labels.")
    private Set<@Size(max = 6, message = "Each label can have a maximum of 6 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Each label must not contain special characters.")
            String> labels = new HashSet<>();
    private String username;
}