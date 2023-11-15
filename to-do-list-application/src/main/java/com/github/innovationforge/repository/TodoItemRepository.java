package com.github.innovationforge.repository;

import java.util.List;

import com.github.innovationforge.model.TodoItem;

public interface TodoItemRepository {

    List<TodoItem> search(String query, String username);

    List<TodoItem> getAllTodoItems(String username);

    TodoItem getTodoItemById(long id, String username);

    void addTodoItem(TodoItem todoItem, String username);

    void updateTodoItem(TodoItem todoItem, String username);

    void deleteTodoItem(long id, String username);
}