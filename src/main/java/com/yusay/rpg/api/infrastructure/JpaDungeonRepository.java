package com.yusay.rpg.api.infrastructure;

import com.yusay.rpg.api.domain.entity.Dungeon;
import com.yusay.rpg.api.domain.repository.DungeonRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDungeonRepository extends DungeonRepository, JpaRepository<Dungeon, String> {
}
