package com.yusay.rpg.api.domain.repository;

import com.yusay.rpg.api.domain.entity.Dungeon;

import java.util.List;

public interface DungeonRepository {
    List<Dungeon> findAll();
}
