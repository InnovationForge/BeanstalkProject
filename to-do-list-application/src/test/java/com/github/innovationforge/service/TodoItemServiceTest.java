package com.github.innovationforge.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import com.github.innovationforge.model.TodoItem;
import com.github.innovationforge.repository.TodoItemRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoItemServiceTest {

    @Mock
    private TodoItemRepository todoItemRepository;

    @InjectMocks
    private TodoItemService todoItemService;

    private TodoItem todoItem1;
    private TodoItem todoItem2;

    @BeforeEach
    public void setup() {
        todoItem1 = new TodoItem();
        todoItem1.setId(1L);
        todoItem1.setTitle("Test Todo 1");

        todoItem2 = new TodoItem();
        todoItem2.setId(2L);
        todoItem2.setTitle("Test Todo 2");
    }

    @Test
    public void searchReturnsCorrectTodoItems() {
        when(todoItemRepository.search("Test", "user")).thenReturn(Arrays.asList(todoItem1, todoItem2));

        List<TodoItem> result = todoItemService.search("Test", "user");

        assertEquals(2, result.size());
        verify(todoItemRepository, times(1)).search("Test", "user");
    }

    @Test
    public void getAllTodoItemsReturnsCorrectItems() {
        when(todoItemRepository.getAllTodoItems("user")).thenReturn(Arrays.asList(todoItem1, todoItem2));

        List<TodoItem> result = todoItemService.getAllTodoItems("user");

        assertEquals(2, result.size());
        verify(todoItemRepository, times(1)).getAllTodoItems("user");
    }

    @Test
    public void getTodoItemByIdReturnsCorrectItem() {
        when(todoItemRepository.getTodoItemById(1L, "user")).thenReturn(todoItem1);

        TodoItem result = todoItemService.getTodoItemById(1L, "user");

        assertEquals(todoItem1, result);
        verify(todoItemRepository, times(1)).getTodoItemById(1L, "user");
    }

//    @Test
//    public void saveTodoItemCallsCorrectMethod() {
//        todoItemService.saveTodoItem(todoItem1, "user");
//
//        verify(todoItemRepository, times(1)).addTodoItem(todoItem1, "user");
//    }

    @Test
    public void deleteTodoItemCallsCorrectMethod() {
        todoItemService.deleteTodoItem(1L, "user");

        verify(todoItemRepository, times(1)).deleteTodoItem(1L, "user");
    }
}