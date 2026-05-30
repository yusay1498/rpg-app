package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.application.JobApplicationService;
import com.yusay.rpg.api.domain.entity.Job;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobRestController {

    private final JobApplicationService jobApplicationService;

    public JobRestController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Job>> get() {
        return ResponseEntity.ok(jobApplicationService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getById(@PathVariable String id) {
        return ResponseEntity.ok(jobApplicationService.lookup(id));
    }
}
