package com.github.innovationforge.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.innovationforge.model.TodoItem;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryTodoItemRepositoryImpl implements TodoItemRepository {

    private static List<TodoItem> todoItemList = new ArrayList<>();
    private static long idCounter = 1;

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        loadInitialData();
    }

    private void loadInitialData() {
        log.info("Loading initial data");
        try (InputStream inputStream = getClass().getResourceAsStream("/data/initial-todo-list.json")) {
            if (inputStream != null) {
                List<TodoItem> initialTodoList = objectMapper.readValue(inputStream, new TypeReference<List<TodoItem>>() {});
                todoItemList.addAll(initialTodoList);
            }
            log.info("Loaded {} todo items", todoItemList.size());
        } catch (IOException e) {
            log.error("Error loading initial data", e);
        }
    }

    public List<TodoItem> search(String query) {
        String lowercaseQuery = query.toLowerCase();

        return todoItemList.stream()
                .filter(item ->
                        item.getTitle().toLowerCase().contains(lowercaseQuery) ||
                                item.getDescription().toLowerCase().contains(lowercaseQuery) ||
                                item.getLabels().stream().anyMatch(label -> label.toLowerCase().contains(lowercaseQuery))
                )
                .collect(Collectors.toList());
    }

    // Retrieve all todo items
    public List<TodoItem> getAllTodoItems() {
        return todoItemList;
    }

    // Retrieve a todo item by ID
    public TodoItem getTodoItemById(long id) {
        for (TodoItem item : todoItemList) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null; // Not found
    }

    // Add a new todo item
    public void addTodoItem(TodoItem todoItem) {
        todoItem.setId(idCounter++);
        todoItemList.add(todoItem);
    }

    // Update an existing todo item
    public void updateTodoItem(TodoItem updatedTodoItem) {
        for (int i = 0; i < todoItemList.size(); i++) {
            TodoItem existingTodoItem = todoItemList.get(i);
            if (existingTodoItem.getId() == updatedTodoItem.getId()) {
                todoItemList.set(i, updatedTodoItem);
                return;
            }
        }
    }

    // Delete a todo item by ID
    public void deleteTodoItem(long id) {
        todoItemList.removeIf(item -> item.getId() == id);
    }
}