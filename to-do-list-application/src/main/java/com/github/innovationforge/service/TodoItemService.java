package com.github.innovationforge.service;

import org.springframework.stereotype.Service;

import java.util.List;

import com.github.innovationforge.repository.TodoItemRepository;
import com.github.innovationforge.web.TodoItem;

@Service
public class TodoItemService {

    // Other dependencies, if any

    // Your existing service methods

    // Example usage of TodoItemRepository in the service
    public List<TodoItem> getAllTodoItems() {
        return TodoItemRepository.getAllTodoItems();
    }

    public TodoItem getTodoItemById(long id) {
        return TodoItemRepository.getTodoItemById(id);
    }

    public void saveTodoItem(TodoItem todoItem) {
        if (todoItem.getId() == 0) {
            TodoItemRepository.addTodoItem(todoItem);
        } else {
            TodoItemRepository.updateTodoItem(todoItem);
        }
    }

    public void deleteTodoItem(long id) {
        TodoItemRepository.deleteTodoItem(id);
    }
}
