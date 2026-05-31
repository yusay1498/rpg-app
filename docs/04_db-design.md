# DB設計

## 共通ルール

- PK / FK はすべて `VARCHAR(36)`（UUID形式）
  - 例外：`level_exp_thresholds.level` は意味のある整数値のため `INT PK` とする
  - 例外：区分マスタは `VARCHAR(20) PK`（コード値そのもの）
- UUIDはアプリケーション側で `UUID.randomUUID().toString()` により生成
- タイムスタンプは `TIMESTAMP` 型
- 拡張が想定される固定値セットは区分マスタテーブル + FK で管理する
- 状態遷移のように値が固定的なものは CHECK 制約で管理する

---

## 区分マスタ

| テーブル | 値 | 用途 |
|---------|----|----- |
| skill_types | attack / heal / buff | スキル種別 |
| item_types | weapon / armor / helmet / shield / accessory / consumable | アイテム種別 |
| effect_types | heal / attack_up | 効果種別 |
| slot_types | weapon / armor / helmet / shield / accessory | 装備部位 |
| room_types | normal / boss / treasure / rest | 部屋種別 |

> 値の追加は各区分マスタへの `INSERT` のみで完了する。DDL変更不要。

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
| base_speed | INT | 初期素早さ |
| hp_per_level | INT | レベルアップ1回あたりのHP増加量 |
| mp_per_level | INT | レベルアップ1回あたりのMP増加量 |
| attack_per_level | INT | レベルアップ1回あたりの攻撃力増加量 |
| defense_per_level | INT | レベルアップ1回あたりの防御力増加量 |
| speed_per_level | INT | レベルアップ1回あたりの素早さ増加量 |
| rank | VARCHAR(20) | 職業ランク（beginner / intermediate / advanced / master） |
| master_level | INT | この職業をマスターするのに必要なレベル |

### job_requirements（転職条件）

| カラム | 型 | 説明 |
|--------|-----|------|
| job_id | VARCHAR(36) FK | jobs.id（ON DELETE CASCADE） |
| required_job_id | VARCHAR(36) FK | jobs.id（ON DELETE RESTRICT）前提職業 |

※ `(job_id, required_job_id)` が PRIMARY KEY
※ `CHECK (job_id <> required_job_id)` — 自己参照（自分自身を前提職業に設定）を禁止する制約

### skills（スキル・魔法）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| name | VARCHAR | スキル名 |
| description | TEXT | 説明 |
| mp_cost | INT | 消費MP |
| power | INT | ダメージ倍率等 |
| skill_type | VARCHAR FK | skill_types.code |

### job_skills（職業×スキル）

| カラム | 型 | 説明 |
|-------|----|------|
| job_id | VARCHAR(36) FK | jobs.id（ON DELETE CASCADE） |
| skill_id | VARCHAR(36) FK | skills.id（ON DELETE CASCADE） |
| required_level | INT | 習得に必要なレベル |
| cost | INT | 習得に必要なスキルポイント |

※ `(job_id, skill_id)` が PRIMARY KEY

### items（アイテム）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| name | VARCHAR | アイテム名 |
| description | TEXT | 説明 |
| item_type | VARCHAR FK | item_types.code |
| effect_type | VARCHAR FK NULL | effect_types.code |
| effect_value | INT | 効果量 |
| price | INT | 購入価格 |
| sell_price | INT NULL | 売却価格（NULLなら売却不可） |
| is_sellable | BOOLEAN | 売却可否（デフォルト true） |
| slot | VARCHAR FK NULL | slot_types.code（装備品のみ） |

※ `effect_type` と `effect_value` は両方NULLまたは両方NOT NULLとするCHECK制約あり
※ 売却専用アイテムは `price = 0`、`sell_price` に売却価格を設定する

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
| dungeon_id | VARCHAR(36) FK | dungeons.id（ON DELETE CASCADE） |
| floor | INT | 部屋番号 |
| room_type | VARCHAR FK | room_types.code |
| is_boss | BOOLEAN | ボス部屋フラグ（デフォルト false） |
| description | TEXT | 説明 |

### enemies（敵）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| dungeon_id | VARCHAR(36) FK | dungeons.id（ON DELETE CASCADE） |
| name | VARCHAR | 敵名 |
| hp | INT | HP |
| attack | INT | 攻撃力 |
| defense | INT | 防御力 |
| speed | INT | 素早さ |
| exp | INT | 獲得経験値 |
| gold | INT | 獲得ゴールド |
| drop_item_id | VARCHAR(36) FK NULL | items.id（ON DELETE SET NULL） |
| drop_rate | DECIMAL | ドロップ率 |
| is_boss | BOOLEAN | ボスフラグ（デフォルト false） |

> `enemies` はダンジョン単位で管理する。戦闘開始時にダンジョンに紐づく敵の中からランダムに抽選する。
> `Room` と `Enemy` の直接関連はない。

### level_exp_thresholds（レベルアップEXP閾値）

| カラム | 型 | 説明 |
|-------|----|------|
| level | INT PK | レベル（意味のある整数値のためINT PKを使用。共通ルールの例外） |
| required_exp | INT | そのレベルに上がるのに必要な累計EXP |

---

## プレイ系テーブル

### characters（キャラクター）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| name | VARCHAR | キャラクター名 |
| job_id | VARCHAR(36) FK | jobs.id（ON DELETE RESTRICT） |
| level | INT | レベル（デフォルト 1） |
| exp | INT | 現在EXP（デフォルト 0） |
| hp | INT | 現在HP |
| max_hp | INT | 最大HP |
| mp | INT | 現在MP |
| max_mp | INT | 最大MP |
| attack | INT | 攻撃力 |
| defense | INT | 防御力 |
| speed | INT | 素早さ |
| skill_points | INT | 所持スキルポイント（デフォルト 0） |
| gold | INT | 所持金（デフォルト 0） |
| status | VARCHAR | alive / dead ※CHECK制約 |
| created_at | TIMESTAMP | 作成日時 |
| updated_at | TIMESTAMP | 更新日時 |

> キャラクター作成時、選択職業の `base_hp` / `base_mp` / `base_attack` / `base_defense` / `base_speed` を反映する。
> 併せて `character_jobs` に初期職業を 1 件登録する。

### character_jobs（転職・習熟状況）

| カラム | 型 | 説明 |
|--------|-----|------|
| character_id | VARCHAR(36) FK | characters.id（ON DELETE CASCADE） |
| job_id | VARCHAR(36) FK | jobs.id（ON DELETE RESTRICT） |
| mastered | BOOLEAN | マスター済みフラグ（デフォルト false） |
| max_level | INT | その職業で到達した最大レベル（デフォルト 1） |

※ `(character_id, job_id)` が PRIMARY KEY

### character_skills（習得済みスキル）

| カラム | 型 | 説明 |
|-------|----|------|
| character_id | VARCHAR(36) FK | characters.id（ON DELETE CASCADE） |
| skill_id | VARCHAR(36) FK | skills.id（ON DELETE RESTRICT） |
| learned_at | TIMESTAMP | 習得日時 |

※ `(character_id, skill_id)` が PRIMARY KEY

### inventories（所持アイテム）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| character_id | VARCHAR(36) FK | characters.id（ON DELETE CASCADE） |
| item_id | VARCHAR(36) FK | items.id（ON DELETE RESTRICT） |
| quantity | INT | 所持数 |

※ `(character_id, item_id)` にユニーク制約

### equipments（装備中アイテム）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| character_id | VARCHAR(36) FK | characters.id（ON DELETE CASCADE） |
| slot | VARCHAR FK | slot_types.code |
| item_id | VARCHAR(36) FK | items.id（ON DELETE RESTRICT） |

※ `(character_id, slot)` にユニーク制約

### explore_sessions（探索セッション）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| character_id | VARCHAR(36) FK UNIQUE | characters.id（ON DELETE CASCADE） |
| dungeon_id | VARCHAR(36) FK | dungeons.id（ON DELETE RESTRICT） |
| current_room_id | VARCHAR(36) FK | rooms.id（ON DELETE RESTRICT） |
| status | VARCHAR | exploring / in_battle / completed ※CHECK制約 |
| created_at | TIMESTAMP | 開始日時 |
| updated_at | TIMESTAMP | 更新日時 |

### visited_rooms（訪問済み部屋）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| explore_session_id | VARCHAR(36) FK | explore_sessions.id（ON DELETE CASCADE） |
| room_id | VARCHAR(36) FK | rooms.id（ON DELETE CASCADE） |

※ `(explore_session_id, room_id)` にユニーク制約

### battle_sessions（戦闘セッション）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| character_id | VARCHAR(36) FK UNIQUE | characters.id（ON DELETE CASCADE） |
| explore_session_id | VARCHAR(36) FK | explore_sessions.id（ON DELETE CASCADE） |
| turn | INT | 現在ターン数 |
| status | VARCHAR | in_progress / win / lose / escaped ※CHECK制約 |
| created_at | TIMESTAMP | 開始日時 |
| updated_at | TIMESTAMP | 更新日時 |

### battle_enemies（戦闘中の敵個体）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| battle_session_id | VARCHAR(36) FK | battle_sessions.id（ON DELETE CASCADE） |
| enemy_id | VARCHAR(36) FK | enemies.id（ON DELETE RESTRICT） |
| current_hp | INT | 現在HP |
| position | INT | 表示順・targetIndex |
| is_defeated | BOOLEAN | 撃破済みフラグ（デフォルト false） |

### room_treasures（宝箱報酬）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| room_id | VARCHAR(36) FK | rooms.id（ON DELETE CASCADE） |
| item_id | VARCHAR(36) FK NULL | items.id（ON DELETE SET NULL） |
| gold | INT | 獲得ゴールド（デフォルト 0） |
| drop_rate | DECIMAL(5,2) | 抽選率（デフォルト 1.00） |

### battle_effects（戦闘中効果・追加検討）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| battle_session_id | VARCHAR(36) FK | battle_sessions.id（ON DELETE CASCADE） |
| character_or_enemy | VARCHAR(20) | 効果対象（character / enemy） |
| effect_type | VARCHAR(20) FK | effect_types.code |
| value | INT | 効果量 |
| remaining_turns | INT | 残りターン数 |

### enemy_skills（敵スキル・追加検討）

| カラム | 型 | 説明 |
|-------|----|------|
| enemy_id | VARCHAR(36) FK | enemies.id（ON DELETE CASCADE） |
| skill_id | VARCHAR(36) FK | skills.id（ON DELETE CASCADE） |
| use_rate | DECIMAL(5,2) | 使用率 |

※ `(enemy_id, skill_id)` が PRIMARY KEY

---

## テーブル関係図

```
[区分マスタ]
skill_types, item_types, effect_types, slot_types, room_types

[業務テーブル]
jobs ──< job_requirements >── jobs（自己参照）
 │
 ├──< job_skills >── skills
 │                  └──< enemy_skills >── enemies
 │
 └──< characters
          │
          ├──< character_jobs >── jobs  ※(character_id, job_id) PRIMARY KEY
          ├──< character_skills >── skills  ※(character_id, skill_id) PRIMARY KEY
          ├──< inventories >── items  ※(character_id, item_id) ユニーク
          ├──< equipments >── items   ※(character_id, slot) ユニーク
          │
          ├── explore_sessions ── rooms ──< dungeons
          │         └──< visited_rooms >── rooms
          └── battle_sessions ──< battle_enemies >── enemies ──< dungeons
                         └──< battle_effects

rooms ──< room_treasures >── items

level_exp_thresholds（独立マスタ）
```
