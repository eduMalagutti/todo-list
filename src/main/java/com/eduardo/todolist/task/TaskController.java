package com.eduardo.todolist.task;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

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
    public ResponseEntity<TaskModel> create(HttpServletRequest request, @RequestBody TaskModel taskModel) {

        UUID idUser = (UUID) request.getAttribute("idUser");

        taskModel.setIdUser(idUser);

        TaskModel taskCreated = this.taskRepository.save(taskModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }

}
