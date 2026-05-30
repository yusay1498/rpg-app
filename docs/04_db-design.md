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
| id | VARCHAR(36) PK | UUID |
| job_id | VARCHAR(36) FK | jobs.id（ON DELETE CASCADE） |
| skill_id | VARCHAR(36) FK | skills.id（ON DELETE CASCADE） |
| required_level | INT | 習得に必要なレベル |

※ `(job_id, skill_id)` にユニーク制約

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
| slot | VARCHAR FK NULL | slot_types.code（装備品のみ） |

※ `effect_type` と `effect_value` は両方NULLまたは両方NOT NULLとするCHECK制約あり

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
| stat_points | INT | 未振り分けポイント（デフォルト 0） |
| hp | INT | 現在HP |
| max_hp | INT | 最大HP |
| mp | INT | 現在MP |
| max_mp | INT | 最大MP |
| attack | INT | 攻撃力 |
| defense | INT | 防御力 |
| gold | INT | 所持金（デフォルト 0） |
| status | VARCHAR | alive / dead ※CHECK制約 |
| created_at | TIMESTAMP | 作成日時 |
| updated_at | TIMESTAMP | 更新日時 |

### character_skills（習得済みスキル）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| character_id | VARCHAR(36) FK | characters.id（ON DELETE CASCADE） |
| skill_id | VARCHAR(36) FK | skills.id（ON DELETE RESTRICT） |
| learned_at | TIMESTAMP | 習得日時 |

※ `(character_id, skill_id)` にユニーク制約

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

### battle_sessions（戦闘セッション）

| カラム | 型 | 説明 |
|-------|----|------|
| id | VARCHAR(36) PK | UUID |
| character_id | VARCHAR(36) FK UNIQUE | characters.id（ON DELETE CASCADE） |
| explore_session_id | VARCHAR(36) FK | explore_sessions.id（ON DELETE CASCADE） |
| enemy_id | VARCHAR(36) FK | enemies.id（ON DELETE RESTRICT） |
| enemy_current_hp | INT | 敵の現在HP |
| turn | INT | 現在ターン数 |
| status | VARCHAR | in_progress / win / lose / escaped ※CHECK制約 |
| created_at | TIMESTAMP | 開始日時 |
| updated_at | TIMESTAMP | 更新日時 |

---

## テーブル関係図

```
[区分マスタ]
skill_types, item_types, effect_types, slot_types, room_types

[業務テーブル]
jobs ──< job_skills >── skills
 │
 └──< characters
          │
          ├──< character_skills >── skills  ※(character_id, skill_id) ユニーク
          ├──< inventories >── items  ※(character_id, item_id) ユニーク
          ├──< equipments >── items   ※(character_id, slot) ユニーク
          │
          ├── explore_sessions ── rooms ──< dungeons
          └── battle_sessions ── enemies ──< dungeons
                                     └──> items (drop)

level_exp_thresholds（独立マスタ）
```
