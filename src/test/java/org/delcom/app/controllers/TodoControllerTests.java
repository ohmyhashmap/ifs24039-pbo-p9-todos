package org.delcom.app.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.delcom.app.configs.ApiResponse;
import org.delcom.app.entities.Todo;
import org.delcom.app.services.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TodoControllerTests {
    @Test
    @DisplayName("Pengujian untuk controller Todo")
    void testTodoController() throws Exception {
        // Buat random UUID
        UUID todoId = UUID.randomUUID();
        UUID nonexistentTodoId = UUID.randomUUID();

        // Membuat dummy data
        Todo todo = new Todo("Belajar Spring Boot", "Belajar mock repository di unit test", false);
        todo.setId(todoId);

        // Membuat mock ServiceRepository
        // Buat mock
        TodoService todoService = Mockito.mock(TodoService.class);

        // Atur perilaku mock
        when(todoService.createTodo(any(String.class), any(String.class))).thenReturn(todo);

        // Membuat instance controller
        TodoController todoController = new TodoController(todoService);
        assert (todoController != null);

        // Menguji create todo dengan data valid
        {
            ApiResponse<Map<String, UUID>> result = todoController.createTodo(todo);
            assert (result != null);
            assert (result.getStatus().equals("success"));
            assert (result.getData().get("id").equals(todoId));
        }

        // Menguji create todo dengan data tidak valid
        {
            // judul kosong
            Todo invalidTodo = new Todo("", "Deskripsi tanpa judul", false);
            ApiResponse<Map<String, UUID>> result = todoController.createTodo(invalidTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));

            // deskripsi kosong
            invalidTodo = new Todo("Judul tanpa deskripsi", "", false);
            result = todoController.createTodo(invalidTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));

            // keduanya kosong
            invalidTodo = new Todo("", "", false);
            result = todoController.createTodo(invalidTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));

            // keduanya null
            invalidTodo = new Todo(null, null, false);
            result = todoController.createTodo(invalidTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));

            // judul null
            invalidTodo = new Todo(null, "Deskripsi dengan judul null", false);
            result = todoController.createTodo(invalidTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));

            // deskripsi null
            invalidTodo = new Todo("Judul dengan deskripsi null", null, false);
            result = todoController.createTodo(invalidTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));
        }

        // Menguji getAllTodos
        {
            List<Todo> dummyResponse = List.of(todo);
            when(todoService.getAllTodos("testing")).thenReturn(dummyResponse);
            ApiResponse<Map<String, List<Todo>>> result = todoController.getAllTodos(null);
            assert (result != null);
            assert (result.getStatus().equals("success"));
            assert (result.getData().size() == 1);
        }

        // Menguji getTodoById dengan ID yang ada
        {
            when(todoService.getTodoById(todoId)).thenReturn(todo);
            ApiResponse<Map<String, Todo>> result = todoController.getTodoById(todoId);
            assert (result != null);
            assert (result.getStatus().equals("success"));
            assert (result.getData().get("todo").getId().equals(todoId));
        }

        // Menguji getTodoById dengan ID yang tidak ada
        {
            when(todoService.getTodoById(nonexistentTodoId)).thenReturn(null);
            ApiResponse<Map<String, Todo>> result = todoController.getTodoById(nonexistentTodoId);
            assert (result != null);
            assert (result.getStatus().equals("fail"));
        }

        // Menguji updateTodo dengan data valid
        {
            Todo updatedTodo = new Todo("Belajar Spring Boot - Updated", "Deskripsi updated", true);
            updatedTodo.setId(todoId);
            when(todoService.updateTodo(any(UUID.class), any(String.class), any(String.class), any(Boolean.class)))
                    .thenReturn(updatedTodo);

            ApiResponse<Todo> result = todoController.updateTodo(todoId, updatedTodo);
            assert (result != null);
            assert (result.getStatus().equals("success"));
        }

        // Menguji updateTodo dengan data tidak valid
        {
            // judul kosong
            Todo invalidTodo = new Todo("", "Deskripsi tanpa judul", false);
            invalidTodo.setId(todoId);
            ApiResponse<Todo> result = todoController.updateTodo(todoId, invalidTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));

            // deskripsi kosong
            invalidTodo = new Todo("Judul tanpa deskripsi", "", false);
            invalidTodo.setId(todoId);
            result = todoController.updateTodo(todoId, invalidTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));

            // keduanya kosong
            invalidTodo = new Todo("", "", false);
            invalidTodo.setId(todoId);
            result = todoController.updateTodo(todoId, invalidTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));

            // keduanya null
            invalidTodo = new Todo(null, null, false);
            invalidTodo.setId(todoId);
            result = todoController.updateTodo(todoId, invalidTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));

            // judul null
            invalidTodo = new Todo(null, "Deskripsi dengan judul null", false);
            invalidTodo.setId(todoId);
            result = todoController.updateTodo(todoId, invalidTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));

            // deskripsi null
            invalidTodo = new Todo("Judul dengan deskripsi null", null, false);
            invalidTodo.setId(todoId);
            result = todoController.updateTodo(todoId, invalidTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));
        }

        // Menguji updateTodo dengan ID yang tidak ada
        {
            when(todoService.updateTodo(any(UUID.class), any(String.class), any(String.class), any(Boolean.class)))
                    .thenReturn(null);
            Todo updatedTodo = new Todo("Belajar Spring Boot - Updated", "Deskripsi updated", true);
            updatedTodo.setId(nonexistentTodoId);

            ApiResponse<Todo> result = todoController.updateTodo(nonexistentTodoId, updatedTodo);
            assert (result != null);
            assert (result.getStatus().equals("fail"));
        }

        // Menguji deleteTodo dengan ID yang ada
        {
            when(todoService.deleteTodo(todoId)).thenReturn(true);
            ApiResponse<String> result = todoController.deleteTodo(todoId);
            assert (result != null);
            assert (result.getStatus().equals("success"));
        }

        // Menguji deleteTodo dengan ID yang tidak ada
        {
            when(todoService.deleteTodo(nonexistentTodoId)).thenReturn(false);
            ApiResponse<String> result = todoController.deleteTodo(nonexistentTodoId);
            assert (result != null);
            assert (result.getStatus().equals("fail"));
        }
    }
}