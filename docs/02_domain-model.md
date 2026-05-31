# ドメインモデル

## エンティティ一覧

### マスタ系（データ追加で拡張できるもの）

| エンティティ | 説明 |
|------------|------|
| `Job` | 職業（戦士・魔法使い・僧侶・盗賊） |
| `JobRequirement` | 転職条件（職業に就くための前提職業） |
| `Skill` | スキル・魔法マスタ |
| `JobSkill` | 職業ごとの習得スキル（レベル条件付き） |
| `Item` | アイテムマスタ（武器・防具・消耗品） |
| `Dungeon` | ダンジョンマスタ（推奨レベル） |
| `Room` | 部屋（ダンジョン内の構造） |
| `Enemy` | 敵マスタ（HP・攻撃力・経験値・ドロップ）※ダンジョン単位で管理 |
| `RoomTreasure` | 宝箱部屋ごとの報酬定義（アイテム・ゴールド・ドロップ率） |
| `EnemySkill` | 敵ごとの使用可能スキルと使用率 |
| `LevelExpThreshold` | レベルアップに必要な累計EXP |

### プレイ系（実際のプレイデータ）

| エンティティ | 説明 |
|------------|------|
| `Character` | キャラクター本体 |
| `CharacterJob` | 転職・習熟状況（経験した職業とマスター状況） |
| `CharacterSkill` | 習得済みスキル |
| `Inventory` | 所持アイテム（Character × Item） |
| `Equipment` | 装備中アイテム（部位ごと） |
| `ExploreSession` | 探索中の状態（現在地） |
| `VisitedRoom` | 探索中にイベント消化済みの部屋 |
| `BattleSession` | 戦闘中の状態（ターン・戦闘結果） |
| `BattleEnemy` | 戦闘中の敵個体（現在HP・位置・撃破状態） |
| `BattleEffect` | 戦闘中のバフ / デバフ効果（対象・効果量・残りターン） |

## エンティティ関係図

```
Job ──< JobRequirement >── Job（自己参照）
 │
 ├──< JobSkill >── Skill
 │                 └──< EnemySkill >── Enemy
 │
 └──< Character ──< CharacterJob >── Job  ※(character_id, job_id) PRIMARY KEY
          │
          ├──< CharacterSkill >── Skill
          ├──< Inventory >── Item
          ├──< Equipment >── Item
          ├── ExploreSession ── Room ──< Dungeon
          │          └──< VisitedRoom >── Room
          └── BattleSession ──< BattleEnemy >── Enemy ──< Dungeon
                         └──< BattleEffect

Room ──< RoomTreasure >── Item

LevelExpThreshold（独立マスタ）
```

> `Character` は speed / skill_points を持ち、作成時に選択職業の初期ステータスを反映する。
> `JobSkill` は required_level に加えて cost を持ち、手動習得時のスキルポイント消費量を表す。
> `BattleSession` は `BattleEnemy` を複数持ち、対象指定付きの戦闘アクションを受け付ける。
> `VisitedRoom` は rest / treasure の 1 回限りイベント管理に使用する。
> `Enemy` はダンジョン単位で管理されるため、`Room` から `Enemy` への直接関連はない。
> 戦闘開始時にダンジョンに紐づく敵の中からランダムに抽選し、normal部屋では 1〜3 体、boss部屋ではボス 1 体 + 取り巻き 0〜2 体を `BattleEnemy` として生成する。
