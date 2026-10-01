package br.com.alura.pomodoro.api.controller;

import br.com.alura.pomodoro.api.dto.TaskRequestDTO;
import br.com.alura.pomodoro.api.dto.TaskResponseDTO;
import br.com.alura.pomodoro.api.model.Task;
import br.com.alura.pomodoro.api.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> list(@RequestParam(required = false) Boolean completed) {
        List<Task> tasks = completed == null
                ? repository.findAll()
                : repository.findByCompleted(completed);
        return ResponseEntity.ok(tasks.stream().map(this::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(toDTO(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskRequestDTO data) {
        Task saved = repository.save(toEntity(data));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO data) {
        return repository.findById(id)
                .map(task -> {
                    task.setTitle(data.title());
                    task.setCompleted(Boolean.TRUE.equals(data.completed()));
                    return ResponseEntity.ok(toDTO(repository.save(task)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(task.getId(), task.getTitle(), task.getCompleted());
    }

    private Task toEntity(TaskRequestDTO dto) {
        return new Task(null, dto.title(), Boolean.TRUE.equals(dto.completed()));
    }
}
