package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.application.DungeonApplicationService;
import com.yusay.rpg.api.domain.entity.Dungeon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dungeons")
public class DungeonRestController {

    private final DungeonApplicationService dungeonApplicationService;

    public DungeonRestController(DungeonApplicationService dungeonApplicationService) {
        this.dungeonApplicationService = dungeonApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<Dungeon>> get() {
        return ResponseEntity.ok(dungeonApplicationService.list());
    }
}
