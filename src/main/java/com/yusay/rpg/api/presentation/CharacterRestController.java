package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.domain.entity.Character;
import com.yusay.rpg.api.application.CharacterApplicationService;
import com.yusay.rpg.api.domain.entity.CharacterJob;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterRestController {

    private final CharacterApplicationService characterApplicationService;

    public CharacterRestController(CharacterApplicationService characterApplicationService) {
        this.characterApplicationService = characterApplicationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(CharacterResponse.from(characterApplicationService.lookup(id)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid Character character) {
        CharacterResponse newCharacter = CharacterResponse
                .from(characterApplicationService.create(character));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCharacter.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchName(
            @PathVariable String id,
            @RequestBody @Valid CharacterRenameRequest request
    ) {
        characterApplicationService.rename(id, request.name());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        characterApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/jobs")
    public ResponseEntity<List<CharacterJobResponse>> getJobs(@PathVariable String id) {
        return ResponseEntity.ok(
                characterApplicationService.listJobs(id).stream()
                        .map(CharacterJobResponse::from)
                        .toList()
        );
    }

    @PostMapping("/{id}/job/change")
    public ResponseEntity<Void> postChangeJob(
            @PathVariable String id,
            @RequestBody @Valid CharacterJob request
    ) {
        CharacterJob newCharacterJob = characterApplicationService
                .changeJob(id, request.getJob() != null ? request.getJob().getId() : null);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build()
                .toUri()
                .resolve("../jobs");

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}/skills")
    public ResponseEntity<List<CharacterSkillResponse>> getSkills(@PathVariable String id) {
        return ResponseEntity.ok(
                characterApplicationService.listSkills(id).stream()
                        .map(CharacterSkillResponse::from)
                        .toList()
        );
    }

    @PostMapping("/{id}/skills/{skillId}")
    public ResponseEntity<Void> learnSkill(
            @PathVariable String id,
            @PathVariable String skillId
    ) {
        characterApplicationService.learnSkill(id, skillId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build()
                .toUri()
                .resolve("../skills");

        return ResponseEntity.created(location).build();
    }
}
