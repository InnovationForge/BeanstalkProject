package com.github.innovationforge.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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

    private final AtomicReference<List<TodoItem>> todoItemRef = new AtomicReference<>(Collections.emptyList());
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
                todoItemRef.set(new ArrayList<>(initialTodoList));
            }
            idCounter = todoItemRef.get().stream().mapToLong(TodoItem::getId).max().orElse(0) + 1;
            log.info("Loaded {} todo items", todoItemRef.get().size());
        } catch (IOException e) {
            log.error("Error loading initial data", e);
        }
    }

    public List<TodoItem> search(String query, String username) {
        String lowercaseQuery = query.toLowerCase();

        return todoItemRef.get().stream()
                .filter(item ->
                        item.getUsername().equals(username) && (
                                item.getTitle().toLowerCase().contains(lowercaseQuery) ||
                                        item.getDescription().toLowerCase().contains(lowercaseQuery) ||
                                        item.getLabels().stream().anyMatch(label -> label.toLowerCase().contains(lowercaseQuery))
                        )
                )
                .collect(Collectors.toList());
    }

    public List<TodoItem> getAllTodoItems(String username) {
        return todoItemRef.get().stream()
                .filter(todoItem -> todoItem.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public TodoItem getTodoItemById(long id, String username) {
        return todoItemRef.get().stream()
                .filter(todoItem -> todoItem.getId() == id && todoItem.getUsername().equals(username))
                .findFirst().orElse(null);
    }

    public synchronized void addTodoItem(TodoItem todoItem, String username) {
        List<TodoItem> mutableList = new ArrayList<>(todoItemRef.get());
        todoItem.setId(idCounter++);
        todoItem.setUsername(username);
        mutableList.add(todoItem);
        todoItemRef.set(Collections.unmodifiableList(mutableList));
    }

    public synchronized void updateTodoItem(TodoItem updatedTodoItem, String username) {
        updatedTodoItem.setUsername(username);

        List<TodoItem> mutableList = new ArrayList<>(todoItemRef.get());

        mutableList = mutableList.stream()
                .map(existingTodoItem ->
                        existingTodoItem.getId() == updatedTodoItem.getId() &&
                                existingTodoItem.getUsername().equals(username) ? updatedTodoItem : existingTodoItem)
                .collect(Collectors.toList());

        todoItemRef.set(Collections.unmodifiableList(mutableList));
    }

    public synchronized void deleteTodoItem(long id, String username) {
        List<TodoItem> mutableList = new ArrayList<>(todoItemRef.get());

        mutableList = mutableList.stream()
                .filter(item -> !(item.getId() == id && item.getUsername().equals(username)))
                .collect(Collectors.toList());

        todoItemRef.set(Collections.unmodifiableList(mutableList));
    }
}