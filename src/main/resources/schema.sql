-- ============================================================
-- 区分マスタ
-- ============================================================

CREATE TABLE IF NOT EXISTS skill_types (
    code VARCHAR(20) PRIMARY KEY
);
INSERT INTO skill_types (code) VALUES ('attack'), ('heal'), ('buff') ON CONFLICT DO NOTHING;

CREATE TABLE IF NOT EXISTS item_types (
    code VARCHAR(20) PRIMARY KEY
);
INSERT INTO item_types (code) VALUES ('weapon'), ('armor'), ('helmet'), ('shield'), ('accessory'), ('consumable') ON CONFLICT DO NOTHING;

CREATE TABLE IF NOT EXISTS effect_types (
    code VARCHAR(20) PRIMARY KEY
);
INSERT INTO effect_types (code) VALUES ('heal'), ('attack_up') ON CONFLICT DO NOTHING;

CREATE TABLE IF NOT EXISTS slot_types (
    code VARCHAR(20) PRIMARY KEY
);
INSERT INTO slot_types (code) VALUES ('weapon'), ('armor'), ('helmet'), ('shield'), ('accessory') ON CONFLICT DO NOTHING;

CREATE TABLE IF NOT EXISTS room_types (
    code VARCHAR(20) PRIMARY KEY
);
INSERT INTO room_types (code) VALUES ('normal'), ('boss'), ('treasure'), ('rest') ON CONFLICT DO NOTHING;

-- ============================================================
-- マスタ系テーブル
-- ============================================================

CREATE TABLE IF NOT EXISTS jobs (
    id          VARCHAR(36)  PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    base_hp     INT          NOT NULL,
    base_mp     INT          NOT NULL,
    base_attack INT          NOT NULL,
    base_defense INT         NOT NULL
);

CREATE TABLE IF NOT EXISTS skills (
    id          VARCHAR(36)  PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    mp_cost     INT          NOT NULL DEFAULT 0,
    power       INT          NOT NULL DEFAULT 0,
    skill_type  VARCHAR(20)  NOT NULL REFERENCES skill_types(code)
);

CREATE TABLE IF NOT EXISTS job_skills (
    id              VARCHAR(36) PRIMARY KEY,
    job_id          VARCHAR(36) NOT NULL REFERENCES jobs(id) ON DELETE CASCADE,
    skill_id        VARCHAR(36) NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    required_level  INT         NOT NULL DEFAULT 1,
    UNIQUE (job_id, skill_id)
);

CREATE TABLE IF NOT EXISTS items (
    id           VARCHAR(36)  PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    item_type    VARCHAR(20)  NOT NULL REFERENCES item_types(code),
    effect_type  VARCHAR(20)           REFERENCES effect_types(code),
    effect_value INT,
    price        INT          NOT NULL DEFAULT 0,
    slot         VARCHAR(20)           REFERENCES slot_types(code),
    CHECK (
        (effect_type IS NULL AND effect_value IS NULL)
        OR (effect_type IS NOT NULL AND effect_value IS NOT NULL)
    )
);

CREATE TABLE IF NOT EXISTS dungeons (
    id                      VARCHAR(36)  PRIMARY KEY,
    name                    VARCHAR(100) NOT NULL,
    description             TEXT,
    recommended_level_min   INT          NOT NULL DEFAULT 1,
    recommended_level_max   INT          NOT NULL DEFAULT 99
);

CREATE TABLE IF NOT EXISTS rooms (
    id          VARCHAR(36) PRIMARY KEY,
    dungeon_id  VARCHAR(36) NOT NULL REFERENCES dungeons(id) ON DELETE CASCADE,
    floor       INT         NOT NULL,
    room_type   VARCHAR(20) NOT NULL REFERENCES room_types(code),
    is_boss     BOOLEAN     NOT NULL DEFAULT FALSE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS enemies (
    id           VARCHAR(36)  PRIMARY KEY,
    dungeon_id   VARCHAR(36)  NOT NULL REFERENCES dungeons(id) ON DELETE CASCADE,
    name         VARCHAR(100) NOT NULL,
    hp           INT          NOT NULL,
    attack       INT          NOT NULL,
    defense      INT          NOT NULL,
    exp          INT          NOT NULL DEFAULT 0,
    gold         INT          NOT NULL DEFAULT 0,
    drop_item_id VARCHAR(36)  REFERENCES items(id) ON DELETE SET NULL,
    drop_rate    DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    is_boss      BOOLEAN      NOT NULL DEFAULT FALSE
);

-- level_exp_thresholds: レベルは意味のある整数値のため INT PK（共通ルールの例外）
CREATE TABLE IF NOT EXISTS level_exp_thresholds (
    level        INT PRIMARY KEY,
    required_exp INT NOT NULL
);

-- ============================================================
-- プレイ系テーブル
-- ============================================================

CREATE TABLE IF NOT EXISTS characters (
    id          VARCHAR(36)  PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    job_id      VARCHAR(36)  NOT NULL REFERENCES jobs(id) ON DELETE RESTRICT,
    level       INT          NOT NULL DEFAULT 1,
    exp         INT          NOT NULL DEFAULT 0,
    stat_points INT          NOT NULL DEFAULT 0,
    hp          INT          NOT NULL,
    max_hp      INT          NOT NULL,
    mp          INT          NOT NULL,
    max_mp      INT          NOT NULL,
    attack      INT          NOT NULL,
    defense     INT          NOT NULL,
    gold        INT          NOT NULL DEFAULT 0,
    status      VARCHAR(10)  NOT NULL DEFAULT 'alive' CHECK (status IN ('alive', 'dead')),
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS character_skills (
    id           VARCHAR(36) PRIMARY KEY,
    character_id VARCHAR(36) NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    skill_id     VARCHAR(36) NOT NULL REFERENCES skills(id) ON DELETE RESTRICT,
    learned_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (character_id, skill_id)
);

CREATE TABLE IF NOT EXISTS inventories (
    id           VARCHAR(36) PRIMARY KEY,
    character_id VARCHAR(36) NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    item_id      VARCHAR(36) NOT NULL REFERENCES items(id) ON DELETE RESTRICT,
    quantity     INT         NOT NULL DEFAULT 1,
    UNIQUE (character_id, item_id)
);

CREATE TABLE IF NOT EXISTS equipments (
    id           VARCHAR(36) PRIMARY KEY,
    character_id VARCHAR(36) NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    slot         VARCHAR(20) NOT NULL REFERENCES slot_types(code),
    item_id      VARCHAR(36) NOT NULL REFERENCES items(id) ON DELETE RESTRICT,
    UNIQUE (character_id, slot)
);

CREATE TABLE IF NOT EXISTS explore_sessions (
    id              VARCHAR(36) PRIMARY KEY,
    character_id    VARCHAR(36) NOT NULL UNIQUE REFERENCES characters(id) ON DELETE CASCADE,
    dungeon_id      VARCHAR(36) NOT NULL REFERENCES dungeons(id) ON DELETE RESTRICT,
    current_room_id VARCHAR(36) NOT NULL REFERENCES rooms(id) ON DELETE RESTRICT,
    status          VARCHAR(20) NOT NULL DEFAULT 'exploring' CHECK (status IN ('exploring', 'in_battle', 'completed')),
    created_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS battle_sessions (
    id                 VARCHAR(36) PRIMARY KEY,
    character_id       VARCHAR(36) NOT NULL UNIQUE REFERENCES characters(id) ON DELETE CASCADE,
    explore_session_id VARCHAR(36) NOT NULL REFERENCES explore_sessions(id) ON DELETE CASCADE,
    enemy_id           VARCHAR(36) NOT NULL REFERENCES enemies(id) ON DELETE RESTRICT,
    enemy_current_hp   INT         NOT NULL,
    turn               INT         NOT NULL DEFAULT 1,
    status             VARCHAR(20) NOT NULL DEFAULT 'in_progress' CHECK (status IN ('in_progress', 'win', 'lose', 'escaped')),
    created_at         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- インデックス
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_job_skills_job_id ON job_skills(job_id);
CREATE INDEX IF NOT EXISTS idx_job_skills_skill_id ON job_skills(skill_id);
CREATE INDEX IF NOT EXISTS idx_rooms_dungeon_id ON rooms(dungeon_id);
CREATE INDEX IF NOT EXISTS idx_enemies_dungeon_id ON enemies(dungeon_id);
CREATE INDEX IF NOT EXISTS idx_enemies_drop_item_id ON enemies(drop_item_id);
CREATE INDEX IF NOT EXISTS idx_characters_job_id ON characters(job_id);
CREATE INDEX IF NOT EXISTS idx_character_skills_skill_id ON character_skills(skill_id);
CREATE INDEX IF NOT EXISTS idx_inventories_character_id ON inventories(character_id);
CREATE INDEX IF NOT EXISTS idx_inventories_item_id ON inventories(item_id);
CREATE INDEX IF NOT EXISTS idx_equipments_character_id ON equipments(character_id);
CREATE INDEX IF NOT EXISTS idx_equipments_item_id ON equipments(item_id);
CREATE INDEX IF NOT EXISTS idx_explore_sessions_dungeon_id ON explore_sessions(dungeon_id);
CREATE INDEX IF NOT EXISTS idx_explore_sessions_current_room_id ON explore_sessions(current_room_id);
CREATE INDEX IF NOT EXISTS idx_battle_sessions_explore_session_id ON battle_sessions(explore_session_id);
CREATE INDEX IF NOT EXISTS idx_battle_sessions_enemy_id ON battle_sessions(enemy_id);
