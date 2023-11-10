package com.github.innovationforge.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import com.github.innovationforge.model.TodoItem;
import com.github.innovationforge.service.TodoItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ToDoController {

    private final TodoItemService todoItemService;

    @GetMapping("/")
    public String showCreateTodoItemForm(Model model) {
        model.addAttribute("todoItem", new TodoItem());
        return "createToDoItem";
    }

    @PostMapping("/createToDoItem")
    public String createTodoItem(TodoItem todoItem) {
        // Add validation and error handling as needed
        log.info("Creating to-do item: {}", todoItem);
        // Call your service to save the todo item to your data store
        todoItemService.saveTodoItem(todoItem);

        return "redirect:/todoList";
    }

    @GetMapping("/todoList")
    public String showTodoList(Model model) {
        // Retrieve all todo items from the service
        List<TodoItem> todoItems = todoItemService.getAllTodoItems();

        // Add the list of todo items to the model
        model.addAttribute("todoItems", todoItems);

        // Return the name of the Thymeleaf template for rendering
        return "todoList";
    }

    @GetMapping("/editTodoItem/{id}")
    public String showEditForm(@PathVariable long id, Model model) {
        // Retrieve the todo item by ID and add it to the model
        TodoItem todoItem = todoItemService.getTodoItemById(id);
        log.info("Editing to-do item: {}", todoItem);
        model.addAttribute("todoItem", todoItem);
        return "editTodoItemForm";
    }

    @PostMapping("/updateTodoItem")
    public String updateTodoItem(@ModelAttribute("todoItem") TodoItem updatedTodoItem) {
        log.info("Updating to-do item: {}", updatedTodoItem);
        todoItemService.saveTodoItem(updatedTodoItem);
        return "redirect:/todoList";
    }

    @GetMapping("/deleteTodoItem/{id}")
    public String deleteTodoItem(@PathVariable long id) {
        log.info("Deleting to-do item with ID: {}", id);
        todoItemService.deleteTodoItem(id);
        return "redirect:/todoList";
    }
}
