# DB設計

## 共通ルール

- PK / FK はすべて `VARCHAR(36)`（UUID形式）
- UUIDはアプリケーション側で `UUID.randomUUID().toString()` により生成
- タイムスタンプは `TIMESTAMP` 型

---

## マスタ系テーブル

### jobs（職業）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| name | VARCHAR | 職業名 |
| description | TEXT | 説明 |
| base_hp | INT | 初期HP |
| base_mp | INT | 初期MP |
| base_attack | INT | 初期攻撃力 |
| base_defense | INT | 初期防御力 |

### skills（スキル・魔法）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| name | VARCHAR | スキル名 |
| description | TEXT | 説明 |
| mp_cost | INT | 消費MP |
| power | INT | ダメージ倍率等 |
| skill_type | VARCHAR | attack / heal / buff |

### job_skills（職業×スキル）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| job_id | VARCHAR(36) FK | jobs.id |
| skill_id | VARCHAR(36) FK | skills.id |
| required_level | INT | 習得に必要なレベル |

### items（アイテム）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| name | VARCHAR | アイテム名 |
| description | TEXT | 説明 |
| item_type | VARCHAR | weapon / armor / helmet / shield / accessory / consumable |
| effect_type | VARCHAR | heal / attack_up 等 |
| effect_value | INT | 効果量 |
| price | INT | 購入価格 |
| slot | VARCHAR | 装備部位（装備品のみ） |

### dungeons（ダンジョン）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| name | VARCHAR | ダンジョン名 |
| description | TEXT | 説明 |
| recommended_level_min | INT | 推奨レベル（下限） |
| recommended_level_max | INT | 推奨レベル（上限） |

### rooms（部屋）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| dungeon_id | VARCHAR(36) FK | dungeons.id |
| floor | INT | 部屋番号 |
| room_type | VARCHAR | normal / boss / treasure / rest |
| is_boss | BOOLEAN | ボス部屋フラグ（デフォルト false） |
| description | TEXT | 説明 |

### enemies（敵）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| dungeon_id | VARCHAR(36) FK | dungeons.id |
| name | VARCHAR | 敵名 |
| hp | INT | HP |
| attack | INT | 攻撃力 |
| defense | INT | 防御力 |
| exp | INT | 獲得経験値 |
| gold | INT | 獲得ゴールド |
| drop_item_id | VARCHAR(36) FK NULL | items.id |
| drop_rate | DECIMAL | ドロップ率 |
| is_boss | BOOLEAN | ボスフラグ（デフォルト false） |

### level_exp_thresholds（レベルアップEXP閾値）

| カラム | 型 | 説明 |
|-------|----|------|
| level | INT PK | レベル |
| required_exp | INT | そのレベルに上がるのに必要な累計EXP |

---

## プレイ系テーブル

### characters（キャラクター）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| name | VARCHAR | キャラクター名 |
| job_id | VARCHAR(36) FK | jobs.id |
| level | INT | レベル（デフォルト 1） |
| exp | INT | 現在EXP（デフォルト 0） |
| stat_points | INT | 未振り分けポイント（デフォルト 0） |
| hp | INT | 現在HP |
| max_hp | INT | 最大HP |
| mp | INT | 現在MP |
| max_mp | INT | 最大MP |
| attack | INT | 攻撃力 |
| defense | INT | 防御力 |
| gold | INT | 所持金（デフォルト 0） |
| status | VARCHAR | alive / dead |
| created_at | TIMESTAMP | 作成日時 |
| updated_at | TIMESTAMP | 更新日時 |

### character_skills（習得済みスキル）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| character_id | VARCHAR(36) FK | characters.id |
| skill_id | VARCHAR(36) FK | skills.id |
| learned_at | TIMESTAMP | 習得日時 |

### inventories（所持アイテム）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| character_id | VARCHAR(36) FK | characters.id |
| item_id | VARCHAR(36) FK | items.id |
| quantity | INT | 所持数 |

### equipments（装備中アイテム）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| character_id | VARCHAR(36) FK | characters.id |
| slot | VARCHAR | weapon / armor / helmet / shield / accessory |
| item_id | VARCHAR(36) FK | items.id |

※ `(character_id, slot)` にユニーク制約

### explore_sessions（探索セッション）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| character_id | VARCHAR(36) FK UNIQUE | characters.id |
| dungeon_id | VARCHAR(36) FK | dungeons.id |
| current_room_id | VARCHAR(36) FK | rooms.id |
| status | VARCHAR | exploring / in_battle / completed |
| created_at | TIMESTAMP | 開始日時 |
| updated_at | TIMESTAMP | 更新日時 |

### battle_sessions（戦闘セッション）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| character_id | VARCHAR(36) FK UNIQUE | characters.id |
| explore_session_id | VARCHAR(36) FK | explore_sessions.id |
| enemy_id | VARCHAR(36) FK | enemies.id |
| enemy_current_hp | INT | 敵の現在HP |
| turn | INT | 現在ターン数 |
| status | VARCHAR | in_progress / win / lose / escaped |
| created_at | TIMESTAMP | 開始日時 |
| updated_at | TIMESTAMP | 更新日時 |

---

## テーブル関係図

```
jobs ──< job_skills >── skills
 │
 └──< characters
          │
          ├──< character_skills >── skills
          ├──< inventories >── items
          ├──< equipments >── items
          │
          ├── explore_sessions ── rooms ──< dungeons
          │                          └──> enemies
          └── battle_sessions ── enemies
                                     └──> items (drop)

level_exp_thresholds（独立マスタ）
```
