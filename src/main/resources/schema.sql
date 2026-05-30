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
    skill_type  VARCHAR(20)  NOT NULL -- attack / heal / buff
);

CREATE TABLE IF NOT EXISTS job_skills (
    id              VARCHAR(36) PRIMARY KEY,
    job_id          VARCHAR(36) NOT NULL REFERENCES jobs(id),
    skill_id        VARCHAR(36) NOT NULL REFERENCES skills(id),
    required_level  INT         NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS items (
    id           VARCHAR(36)  PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    item_type    VARCHAR(20)  NOT NULL, -- weapon / armor / helmet / shield / accessory / consumable
    effect_type  VARCHAR(20),           -- heal / attack_up 等
    effect_value INT,
    price        INT          NOT NULL DEFAULT 0,
    slot         VARCHAR(20)            -- 装備部位（装備品のみ）
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
    dungeon_id  VARCHAR(36) NOT NULL REFERENCES dungeons(id),
    floor       INT         NOT NULL,
    room_type   VARCHAR(20) NOT NULL, -- normal / boss / treasure / rest
    is_boss     BOOLEAN     NOT NULL DEFAULT FALSE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS enemies (
    id           VARCHAR(36)  PRIMARY KEY,
    dungeon_id   VARCHAR(36)  NOT NULL REFERENCES dungeons(id),
    name         VARCHAR(100) NOT NULL,
    hp           INT          NOT NULL,
    attack       INT          NOT NULL,
    defense      INT          NOT NULL,
    exp          INT          NOT NULL DEFAULT 0,
    gold         INT          NOT NULL DEFAULT 0,
    drop_item_id VARCHAR(36)  REFERENCES items(id),
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
    job_id      VARCHAR(36)  NOT NULL REFERENCES jobs(id),
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
    status      VARCHAR(10)  NOT NULL DEFAULT 'alive', -- alive / dead
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS character_skills (
    id           VARCHAR(36) PRIMARY KEY,
    character_id VARCHAR(36) NOT NULL REFERENCES characters(id),
    skill_id     VARCHAR(36) NOT NULL REFERENCES skills(id),
    learned_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS inventories (
    id           VARCHAR(36) PRIMARY KEY,
    character_id VARCHAR(36) NOT NULL REFERENCES characters(id),
    item_id      VARCHAR(36) NOT NULL REFERENCES items(id),
    quantity     INT         NOT NULL DEFAULT 1,
    UNIQUE (character_id, item_id)
);

CREATE TABLE IF NOT EXISTS equipments (
    id           VARCHAR(36) PRIMARY KEY,
    character_id VARCHAR(36) NOT NULL REFERENCES characters(id),
    slot         VARCHAR(20) NOT NULL, -- weapon / armor / helmet / shield / accessory
    item_id      VARCHAR(36) NOT NULL REFERENCES items(id),
    UNIQUE (character_id, slot)
);

CREATE TABLE IF NOT EXISTS explore_sessions (
    id              VARCHAR(36) PRIMARY KEY,
    character_id    VARCHAR(36) NOT NULL UNIQUE REFERENCES characters(id),
    dungeon_id      VARCHAR(36) NOT NULL REFERENCES dungeons(id),
    current_room_id VARCHAR(36) NOT NULL REFERENCES rooms(id),
    status          VARCHAR(20) NOT NULL DEFAULT 'exploring', -- exploring / in_battle / completed
    created_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS battle_sessions (
    id                 VARCHAR(36) PRIMARY KEY,
    character_id       VARCHAR(36) NOT NULL UNIQUE REFERENCES characters(id),
    explore_session_id VARCHAR(36) NOT NULL REFERENCES explore_sessions(id),
    enemy_id           VARCHAR(36) NOT NULL REFERENCES enemies(id),
    enemy_current_hp   INT         NOT NULL,
    turn               INT         NOT NULL DEFAULT 1,
    status             VARCHAR(20) NOT NULL DEFAULT 'in_progress', -- in_progress / win / lose / escaped
    created_at         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);
