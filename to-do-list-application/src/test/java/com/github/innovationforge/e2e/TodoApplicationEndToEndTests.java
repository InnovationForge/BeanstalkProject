package com.github.innovationforge.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.innovationforge.model.TodoItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class TodoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void endToEndTest() throws Exception {
        // Create a TodoItem
        TodoItem todoItem = new TodoItem(1L, "End-to-End Test Todo", "End-to-End Test Description", LocalDate.now(), false);

        // Create TodoItem
        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/createToDoItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoItem)))
                .andReturn();

        // Ensure the creation was successful and redirected to /todoList
        mockMvc.perform(MockMvcRequestBuilders.get(createResult.getResponse().getRedirectedUrl()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("todoList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("todoItems"));

        // Retrieve the list of TodoItems
        MvcResult listResult = mockMvc.perform(MockMvcRequestBuilders.get("/todoList"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        // Retrieve the model from the MvcResult
        ModelAndView mav = listResult.getModelAndView();
        ModelMap modelMap = mav.getModelMap();

        // Verify that the model contains the "todoItems" attribute
        Assertions.assertTrue(modelMap.containsAttribute("todoItems"));

        // Retrieve the actual list from the model
        List<TodoItem> todoItems = (List<TodoItem>) modelMap.get("todoItems");

        // Now you can perform assertions on the todoItems list
        // For example, you can check if it's not null and has the expected size
        Assertions.assertNotNull(todoItems);
        Assertions.assertEquals(1, todoItems.size());

        // Edit the created TodoItem
        TodoItem updatedTodoItem = new TodoItem();
        updatedTodoItem.setId(todoItems.get(0).getId());
        updatedTodoItem.setTitle("Updated End-to-End Test Todo");
        updatedTodoItem.setDescription("Updated End-to-End Test Description");

        // Perform the request to edit a todo item and get the MvcResult
        MvcResult editResult = mockMvc.perform(MockMvcRequestBuilders.get("/editTodoItem/{id}", updatedTodoItem.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("editTodoItemForm"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("todoItem"))
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/updateTodoItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodoItem)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todoList"));

        // Delete the updated TodoItem
        mockMvc.perform(MockMvcRequestBuilders.get("/deleteTodoItem/{id}", updatedTodoItem.getId()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todoList"));

        // Ensure the updated TodoItem is no longer in the list
        mockMvc.perform(MockMvcRequestBuilders.get("/todoList"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString(updatedTodoItem.getTitle()))))
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString(updatedTodoItem.getDescription()))));
    }
}
