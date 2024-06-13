package com.eduardo.todolist.task;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    ITaskRepository taskRepository;

    @PostMapping("/create")
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody TaskModel taskModel) {

        var currentDate = LocalDateTime.now();

        boolean dateValidation = currentDate.isAfter(taskModel.getStartAt())
                || currentDate.isAfter(taskModel.getEndAt())
                || taskModel.getStartAt().isAfter(taskModel.getEndAt());

        if (dateValidation) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("data inválida");
        }

        UUID idUser = (UUID) request.getAttribute("idUser");
        taskModel.setIdUser(idUser);
        TaskModel taskCreated = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }

}
