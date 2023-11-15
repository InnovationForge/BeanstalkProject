package com.github.innovationforge.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Notification {
    private String message;
    private String type; // e.g., "success", "info", "warning", "danger"
}
