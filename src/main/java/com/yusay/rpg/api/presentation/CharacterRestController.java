package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.application.CharacterApplicationService;
import com.yusay.rpg.api.domain.entity.Character;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/characters")
public class CharacterRestController {

    private final CharacterApplicationService characterApplicationService;

    public CharacterRestController(CharacterApplicationService characterApplicationService) {
        this.characterApplicationService = characterApplicationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Character> getById(@PathVariable String id) {
        return ResponseEntity.ok(characterApplicationService.lookup(id));
    }
}
