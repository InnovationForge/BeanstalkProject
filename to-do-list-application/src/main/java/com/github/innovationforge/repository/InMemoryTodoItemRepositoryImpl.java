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

    private List<TodoItem> todoItemList = new ArrayList<>();
    private long idCounter = 1;

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
            idCounter = todoItemList.stream().mapToLong(TodoItem::getId).max().orElse(0) + 1;
            log.info("Loaded {} todo items", todoItemList.size());
        } catch (IOException e) {
            log.error("Error loading initial data", e);
        }
    }

    public List<TodoItem> search(String query, String username) {
        String lowercaseQuery = query.toLowerCase();

        return todoItemList.stream()
                .filter(item ->
                        item.getUsername().equals(username) && (
                                item.getTitle().toLowerCase().contains(lowercaseQuery) ||
                                        item.getDescription().toLowerCase().contains(lowercaseQuery) ||
                                        item.getLabels().stream().anyMatch(label -> label.toLowerCase().contains(lowercaseQuery))
                        )
                )
                .collect(Collectors.toList());
    }

    // Retrieve all todo items
    public List<TodoItem> getAllTodoItems(String username) {
        return todoItemList.stream()
                .filter(todoItem -> todoItem.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    // Retrieve a todo item by ID
    public TodoItem getTodoItemById(long id, String username) {
        return todoItemList.stream()
                .filter(todoItem -> todoItem.getId() == id && todoItem.getUsername().equals(username))
                .findFirst().orElse(null);
    }

    // Add a new todo item
    public synchronized void addTodoItem(TodoItem todoItem, String username) {
        // Create a mutable ArrayList containing the elements of todoItemList
        List<TodoItem> mutableList = new ArrayList<>(todoItemList);

        // Now you can add elements to the mutable list
        todoItem.setId(idCounter++);
        todoItem.setUsername(username);
        mutableList.add(todoItem);

        // After adding all necessary elements, update todoItemList with the new list
        todoItemList = mutableList;
    }

    // Update an existing todo item
    public synchronized void updateTodoItem(TodoItem updatedTodoItem, String username) {
        updatedTodoItem.setUsername(username);

        // Create a mutable ArrayList containing the elements of todoItemList
        List<TodoItem> mutableList = new ArrayList<>(todoItemList);

        mutableList = mutableList.stream()
                .map(existingTodoItem ->
                        existingTodoItem.getId() == updatedTodoItem.getId() &&
                                existingTodoItem.getUsername().equals(username) ? updatedTodoItem : existingTodoItem)
                .toList();
        // After adding all necessary elements, update todoItemList with the new list
        todoItemList = mutableList;
    }

    // Delete a todo item by ID
    public void deleteTodoItem(long id, String username) {
        // Create a mutable ArrayList containing the elements of todoItemList
        List<TodoItem> mutableList = new ArrayList<>(todoItemList);

        mutableList = mutableList.stream()
                .filter(item -> !(item.getId() == id && item.getUsername().equals(username)))
                .collect(Collectors.toList());

        // After adding all necessary elements, update todoItemList with the new list
        todoItemList = mutableList;
    }
}