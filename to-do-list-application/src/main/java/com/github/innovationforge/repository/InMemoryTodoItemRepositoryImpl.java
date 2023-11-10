package com.github.innovationforge.repository;

import java.util.ArrayList;
import java.util.List;

import com.github.innovationforge.model.TodoItem;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryTodoItemRepositoryImpl implements TodoItemRepository {

    private static List<TodoItem> todoItemList = new ArrayList<>();
    private static long idCounter = 1;

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
