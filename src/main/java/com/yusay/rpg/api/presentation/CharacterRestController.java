package com.yusay.rpg.api.presentation;

import com.yusay.rpg.api.application.CharacterApplicationService;
import com.yusay.rpg.api.application.CharacterJobService;
import com.yusay.rpg.api.application.CharacterSkillService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/characters")
@Validated
public class CharacterRestController {

    private final CharacterApplicationService characterApplicationService;
    private final CharacterJobService characterJobService;
    private final CharacterSkillService characterSkillService;

    public CharacterRestController(
            CharacterApplicationService characterApplicationService,
            CharacterJobService characterJobService,
            CharacterSkillService characterSkillService
    ) {
        this.characterApplicationService = characterApplicationService;
        this.characterJobService = characterJobService;
        this.characterSkillService = characterSkillService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> getById(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id) {
        return ResponseEntity.ok(CharacterResponse.from(characterApplicationService.lookup(id)));
    }

    @PostMapping
    public ResponseEntity<Void> post(@RequestBody @Valid CharacterCreateRequest request) {
        CharacterResponse newCharacter = CharacterResponse
                .from(characterApplicationService.create(request.name(), request.jobId()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCharacter.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchName(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id,
            @RequestBody @Valid CharacterRenameRequest request
    ) {
        characterApplicationService.rename(id, request.name());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id) {
        characterApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/jobs")
    public ResponseEntity<List<CharacterJobResponse>> getJobs(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id) {
        return ResponseEntity.ok(
                characterJobService.list(id).stream()
                        .map(CharacterJobResponse::from)
                        .toList()
        );
    }

    @PostMapping("/{id}/jobs")
    public ResponseEntity<Void> postJob(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id,
            @RequestBody @Valid JobChangeRequest request
    ) {
        characterJobService.changeJob(id, request.jobId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/characters/{id}/jobs")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}/skills")
    public ResponseEntity<List<CharacterSkillResponse>> getSkills(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id) {
        return ResponseEntity.ok(
                characterSkillService.listSkills(id).stream()
                        .map(CharacterSkillResponse::from)
                        .toList()
        );
    }

    @PostMapping("/{id}/skills/{skillId}")
    public ResponseEntity<Void> postSkill(
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String id,
            @PathVariable @Pattern(regexp = "^[0-9a-f\\-]{36}$") String skillId
    ) {
        characterSkillService.learnSkill(id, skillId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/characters/{id}/skills")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
