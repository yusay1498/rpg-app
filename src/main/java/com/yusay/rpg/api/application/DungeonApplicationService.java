package com.yusay.rpg.api.application;

import com.yusay.rpg.api.domain.entity.Dungeon;
import com.yusay.rpg.api.domain.repository.DungeonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DungeonApplicationService {

    private final DungeonRepository dungeonRepository;

    public DungeonApplicationService(DungeonRepository dungeonRepository) {
        this.dungeonRepository = dungeonRepository;
    }

    public List<Dungeon> list() {
        return dungeonRepository.findAll();
    }
}
