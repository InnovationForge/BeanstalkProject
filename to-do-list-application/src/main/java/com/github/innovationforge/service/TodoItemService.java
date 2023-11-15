package com.github.innovationforge.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import com.github.innovationforge.repository.TodoItemRepository;
import com.github.innovationforge.model.TodoItem;

@Service
@RequiredArgsConstructor
public class TodoItemService {

    private final TodoItemRepository todoItemRepository;

    public List<TodoItem> search(String query, String username) {
        return todoItemRepository.search(query, username);
    }

    public List<TodoItem> getAllTodoItems(String username) {
        return todoItemRepository.getAllTodoItems(username);
    }

    public TodoItem getTodoItemById(long id, String username) {
        return todoItemRepository.getTodoItemById(id, username);
    }

    public void saveTodoItem(TodoItem todoItem, String username) {
        if (todoItem.getId() == 0) {
            todoItemRepository.addTodoItem(todoItem, username);
        } else {
            todoItemRepository.updateTodoItem(todoItem, username);
        }
    }

    public void deleteTodoItem(long id, String username) {
        todoItemRepository.deleteTodoItem(id, username);
    }
}