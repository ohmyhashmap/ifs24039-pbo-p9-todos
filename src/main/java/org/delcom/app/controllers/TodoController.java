package org.delcom.app.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.delcom.app.configs.ApiResponse;
import org.delcom.app.entities.Todo;
import org.delcom.app.services.TodoService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // Menambahkan todo baru
    // -------------------------------
    @PostMapping
    public ApiResponse<Map<String, UUID>> createTodo(@RequestBody Todo todo) {
        if (todo.getTitle() == null || todo.getTitle().isEmpty() ||
                todo.getDescription() == null || todo.getDescription().isEmpty()) {
            return new ApiResponse<>("fail", "Data tidak valid", null);
        }

        Todo newTodo = todoService.createTodo(todo.getTitle(), todo.getDescription());

        return new ApiResponse<Map<String, UUID>>(
                "success",
                "Todo berhasil dibuat",
                Map.of("id", newTodo.getId()));
    }

    // Mendapatkan semua todo dengan opsi pencarian
    // -------------------------------
    @GetMapping
    public ApiResponse<Map<String, List<Todo>>> getAllTodos(@RequestParam(required = false) String search) {
        List<Todo> todos = todoService.getAllTodos(search);
        return new ApiResponse<>(
                "success",
                "Daftar todo berhasil diambil",
                Map.of("todos", todos));
    }

    // Mendapatkan todo berdasarkan ID
    // -------------------------------
    @GetMapping("/{id}")
    public ApiResponse<Map<String, Todo>> getTodoById(@PathVariable UUID id) {
        Todo todo = todoService.getTodoById(id);

        if (todo == null) {
            return new ApiResponse<>(
                    "fail",
                    "Data todo tidak ditemukan",
                    null);
        }

        return new ApiResponse<>(
                "success",
                "Data todo berhasil diambil",
                Map.of("todo", todo));
    }

    // Memperbarui todo berdasarkan ID
    // -------------------------------
    @PutMapping("/{id}")
    public ApiResponse<Todo> updateTodo(@PathVariable UUID id, @RequestBody Todo todo) {

        if (todo.getTitle() == null || todo.getTitle().isEmpty() ||
                todo.getDescription() == null || todo.getDescription().isEmpty()) {
            return new ApiResponse<>("fail", "Data tidak valid", null);
        }

        Todo updatedTodo = todoService.updateTodo(id, todo.getTitle(), todo.getDescription(), todo.isFinished());

        if (updatedTodo == null) {
            return new ApiResponse<>("fail", "Data todo tidak ditemukan", null);
        }

        return new ApiResponse<>("success", "Data todo berhasil diperbarui", null);
    }

    // Menghapus todo berdasarkan ID
    // -------------------------------
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteTodo(@PathVariable UUID id) {
        boolean status = todoService.deleteTodo(id);

        if (!status) {
            return new ApiResponse<>(
                    "fail",
                    "Data todo tidak ditemukan",
                    null);
        }

        return new ApiResponse<>(
                "success",
                "Data todo berhasil dihapus",
                null);
    }
}