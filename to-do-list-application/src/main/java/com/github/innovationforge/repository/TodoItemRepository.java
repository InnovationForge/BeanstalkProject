package com.github.innovationforge.repository;

import java.util.List;

import com.github.innovationforge.model.TodoItem;

public interface TodoItemRepository {

    List<TodoItem> getAllTodoItems();

    TodoItem getTodoItemById(long id);

    void addTodoItem(TodoItem todoItem);

    void updateTodoItem(TodoItem todoItem);

    void deleteTodoItem(long id);
}