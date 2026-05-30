# ドメインモデル

## エンティティ一覧

### マスタ系（データ追加で拡張できるもの）

| エンティティ | 説明 |
|------------|------|
| `Job` | 職業（戦士・魔法使い・僧侶・盗賊） |
| `Skill` | スキル・魔法マスタ |
| `JobSkill` | 職業ごとの習得スキル（レベル条件付き） |
| `Item` | アイテムマスタ（武器・防具・消耗品） |
| `Dungeon` | ダンジョンマスタ（推奨レベル） |
| `Room` | 部屋（ダンジョン内の構造） |
| `Enemy` | 敵マスタ（HP・攻撃力・経験値・ドロップ）※ダンジョン単位で管理 |
| `LevelExpThreshold` | レベルアップに必要な累計EXP |

### プレイ系（実際のプレイデータ）

| エンティティ | 説明 |
|------------|------|
| `Character` | キャラクター本体 |
| `CharacterSkill` | 習得済みスキル |
| `Inventory` | 所持アイテム（Character × Item） |
| `Equipment` | 装備中アイテム（部位ごと） |
| `ExploreSession` | 探索中の状態（現在地） |
| `BattleSession` | 戦闘中の状態（ターン・敵HP） |

## エンティティ関係図

```
Job ──< JobSkill >── Skill
 │
 └──< Character ──< CharacterSkill
          │
          ├──< Inventory >── Item
          ├──< Equipment >── Item
          ├── ExploreSession ── Room ──< Dungeon
          └── BattleSession ── Enemy ──< Dungeon
                                  └──> Item (drop)

LevelExpThreshold（独立マスタ）
```

> `Enemy` はダンジョン単位で管理されるため、`Room` から `Enemy` への直接関連はない。
> 戦闘開始時にダンジョンに紐づく敵の中からランダムに抽選する。
