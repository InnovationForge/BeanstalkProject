package com.github.innovationforge.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.github.innovationforge.model.Notification;
import com.github.innovationforge.model.TodoItem;
import com.github.innovationforge.service.TodoItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ToDoController {

    private final TodoItemService todoItemService;
    private final HttpSession session;

    @GetMapping("/searchForm")
    public String showSearchForm(Model model) {
//        model.addAttribute("todoItem", new TodoItem());
        return "searchForm";
    }
    @GetMapping("/search")
    public String search(@RequestParam String query, Model model, Principal principal) {
        List<TodoItem> searchResults = todoItemService.search(query, principal.getName());
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("query", query);
        return "searchResults";
    }

    @GetMapping("/")
    public String showCreateTodoItemForm(Model model) {
        model.addAttribute("todoItem", new TodoItem());
        return "createToDoItem";
    }

    @PostMapping("/createToDoItem")
    public String createTodoItem(TodoItem todoItem, Principal principal) {
        // Add validation and error handling as needed
        log.info("Creating to-do item: {}", todoItem);
        // Call your service to save the todo item to your data store
        todoItemService.saveTodoItem(todoItem, principal.getName());
        // Create and store a success notification
        Notification notification = new Notification("To-do item created successfully", "success");
        session.setAttribute("notification", notification);
        return "redirect:/todoList";
    }


    @GetMapping("/todoList")
    public String showTodoList(Model model,
            @RequestParam(defaultValue = "all") String filter,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            Principal principal) {
        log.info("logged in user: {}", principal.getName());
        // Retrieve all todo items from the service
        List<TodoItem> todoItems = todoItemService.getAllTodoItems(principal.getName());

        // Apply filtering logic
        List<TodoItem> filteredTodoItems;
        if ("completed".equals(filter)) {
            filteredTodoItems = todoItems.stream().filter(TodoItem::isCompleted).collect(Collectors.toList());
        } else if ("incomplete".equals(filter)) {
            filteredTodoItems = todoItems.stream().filter(item -> !item.isCompleted()).collect(Collectors.toList());
        } else {
            filteredTodoItems = todoItems;
        }

        // Apply sorting logic
        if ("dueDate".equals(sortBy)) {
            filteredTodoItems.sort(Comparator.comparing(TodoItem::getDueDate));
        } else if ("title".equals(sortBy)) {
            filteredTodoItems.sort(Comparator.comparing(TodoItem::getTitle));
        }
        // Add the list of todo items to the model
        model.addAttribute("filteredTodoItems", filteredTodoItems);
        // Return the name of the Thymeleaf template for rendering
        return "todoList";
    }


    @GetMapping("/editTodoItem/{id}")
    public String showEditForm(@PathVariable long id, Model model, Principal principal) {
        // Retrieve the todo item by ID and add it to the model
        TodoItem todoItem = todoItemService.getTodoItemById(id, principal.getName());
        log.info("Editing to-do item: {}", todoItem);
        model.addAttribute("todoItem", todoItem);
        // Return the name of the Thymeleaf template for rendering§
        return "editTodoItemForm";
    }

    @PostMapping("/updateTodoItem")
    public String updateTodoItem(@ModelAttribute("todoItem") TodoItem updatedTodoItem,Principal principal) {
        log.info("Updating to-do item: {}", updatedTodoItem);
        todoItemService.saveTodoItem(updatedTodoItem, principal.getName());
        Notification notification = new Notification("To-do item updated successfully", "info");
        session.setAttribute("notification", notification);
        return "redirect:/todoList";
    }

    @GetMapping("/deleteTodoItem/{id}")
    public String deleteTodoItem(@PathVariable long id, Principal principal) {
        log.info("Deleting to-do item with ID: {}", id);
        todoItemService.deleteTodoItem(id, principal.getName());
        Notification notification = new Notification("To-do item deleted successfully", "danger");
        session.setAttribute("notification", notification);
        return "redirect:/todoList";
    }
}
