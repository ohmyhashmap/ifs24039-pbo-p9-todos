package org.delcom.app.services;

import java.util.List;
import java.util.UUID;

import org.delcom.app.entities.Todo;
import org.delcom.app.repositories.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public Todo createTodo(String title, String description) {
        Todo todo = new Todo(title, description, false);
        return todoRepository.save(todo);
    }

    public List<Todo> getAllTodos(String search) {
        if (search != null && !search.trim().isEmpty()) {
            return todoRepository.findByKeyword(search);
        }
        return todoRepository.findAll();
    }

    public Todo getTodoById(UUID id) {
        return todoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Todo updateTodo(UUID id, String title, String description, Boolean isFinished) {
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null) {
            todo.setTitle(title);
            todo.setDescription(description);
            todo.setFinished(isFinished);
            return todoRepository.save(todo);
        }
        return null;
    }

    @Transactional
    public boolean deleteTodo(UUID id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}