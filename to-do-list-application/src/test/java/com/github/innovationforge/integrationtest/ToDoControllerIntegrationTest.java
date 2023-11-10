package com.github.innovationforge.integrationtest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.innovationforge.service.TodoItemService;
import com.github.innovationforge.web.ToDoController;
import com.github.innovationforge.model.TodoItem;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ToDoController.class)
public class ToDoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoItemService todoItemService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void showCreateTodoItemForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("createToDoItem"));
    }

    @Test
    void createTodoItem() throws Exception {
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle("Test Todo");
        todoItem.setDescription("Test description");

        mockMvc.perform(MockMvcRequestBuilders.post("/createToDoItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoItem)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todoList"));

        Mockito.verify(todoItemService, Mockito.times(1)).saveTodoItem(Mockito.any());
    }

    @Test
    void showTodoList() throws Exception {
        List<TodoItem> todoItems = Arrays.asList(
                new TodoItem(1L, "Task 1", "Description 1", LocalDate.now(), false),
                new TodoItem(2L, "Task 2", "Description 2", LocalDate.now(), false),
                new TodoItem(3L, "Task 3", "Description 3", LocalDate.now(), false)
        );

        Mockito.when(todoItemService.getAllTodoItems()).thenReturn(todoItems);

        mockMvc.perform(MockMvcRequestBuilders.get("/todoList"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("todoList"))
                .andExpect(MockMvcResultMatchers.model().attribute("todoItems", todoItems));
    }

    @Test
    void showEditForm() throws Exception {
        TodoItem todoItem = new TodoItem(1L, "Task 1", "Description 1", LocalDate.now(), false);

        Mockito.when(todoItemService.getTodoItemById(1L)).thenReturn(todoItem);

        mockMvc.perform(MockMvcRequestBuilders.get("/editTodoItem/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("editTodoItemForm"))
                .andExpect(MockMvcResultMatchers.model().attribute("todoItem", todoItem));
    }

    @Test
    void updateTodoItem() throws Exception {
        TodoItem updatedTodoItem = new TodoItem(1L, "Task 1", "Description 1", LocalDate.now(), false);

        mockMvc.perform(MockMvcRequestBuilders.post("/updateTodoItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodoItem)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todoList"));

        Mockito.verify(todoItemService, Mockito.times(1)).saveTodoItem(Mockito.any());
    }

    @Test
    void deleteTodoItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/deleteTodoItem/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todoList"));

        Mockito.verify(todoItemService, Mockito.times(1)).deleteTodoItem(1L);
    }
}
