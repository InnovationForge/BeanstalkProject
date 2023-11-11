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

    public List<TodoItem> getAllTodoItems() {
        return todoItemRepository.getAllTodoItems();
    }

    public TodoItem getTodoItemById(long id) {
        return todoItemRepository.getTodoItemById(id);
    }

    public void saveTodoItem(TodoItem todoItem) {
        if (todoItem.getId() == 0) {
            todoItemRepository.addTodoItem(todoItem);
        } else {
            todoItemRepository.updateTodoItem(todoItem);
        }
    }

    public void deleteTodoItem(long id) {
        todoItemRepository.deleteTodoItem(id);
    }
}