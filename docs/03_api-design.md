# API設計

## キャラクター

| メソッド | エンドポイント | 説明           |
|---------|--------------|--------------|
| `POST` | `/api/characters` | キャラ作成        |
| `GET` | `/api/characters/{id}` | キャラ情報取得      |
| `GET` | `/api/characters/{id}/status` | ステータス詳細      |
| `PATCH` | `/api/characters/{id}` | キャラ名変更（部分更新） |
| `DELETE` | `/api/characters/{id}` | キャラクター削除     |

## ダンジョン探索

| メソッド | エンドポイント | 説明 |
|---------|--------------|------|
| `GET` | `/api/dungeons` | ダンジョン一覧 |
| `POST` | `/api/characters/{id}/explore/start` | 探索開始 |
| `GET` | `/api/characters/{id}/explore/current` | 現在地・部屋情報 |
| `POST` | `/api/characters/{id}/explore/move` | 次の部屋へ移動（戦闘自動開始） |
| `POST` | `/api/characters/{id}/explore/escape` | ダンジョン脱出 |

### 探索移動レスポンス追加項目

- rest部屋到達時は `event: "rest"` と回復量を返す
- treasure部屋到達時は `event: "treasure"` と獲得報酬を返す

## 戦闘

| メソッド | エンドポイント | 説明 |
|---------|--------------|------|
| `GET` | `/api/characters/{id}/battle/current` | 戦闘状況取得 |
| `POST` | `/api/characters/{id}/battle/action` | アクション実行（attack / skill / item / run） |

### 戦闘アクションリクエスト例

```json
{
  "action": "skill",
  "skillId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "targetIndex": 0
}
```

> `targetIndex` は複数敵戦闘で対象を指定するための項目。`attack` / `skill` / `item` で使用する。

### 戦闘アクションレスポンス例（勝利）

```json
{
  "battleResult": "win",
  "turn": 3,
  "battleLog": [
    {
      "turn": 1,
      "actor": "character",
      "action": "skill",
      "targetIndex": 0,
      "result": "52 damage"
    }
  ],
  "enemies": [
    { "index": 0, "name": "スライム", "currentHp": 0, "maxHp": 20, "isDefeated": true },
    { "index": 1, "name": "コウモリ", "currentHp": 0, "maxHp": 15, "isDefeated": true }
  ],
  "expGained": 120,
  "levelUp": {
    "occurred": true,
    "newLevel": 5,
    "statsGained": {
      "hp": 10,
      "mp": 4,
      "attack": 3,
      "defense": 2,
      "speed": 1
    },
    "skillPointsGained": 1
  }
}
```

### 戦闘アクションレスポンス例（敗北）

```json
{
  "battleResult": "lose",
  "penalty": {
    "goldHalved": true,
    "hpRestoredTo": 1
  }
}
```

> 敗北時は `explore_session` を削除し、`characters` の hp を 1・gold を半減に更新して返す。

## アイテム・装備

| メソッド | エンドポイント | 説明 |
|---------|--------------|------|
| `GET` | `/api/characters/{id}/inventory` | 所持アイテム一覧 |
| `POST` | `/api/characters/{id}/inventory/{itemId}/use` | アイテム使用 |
| `PUT` | `/api/characters/{id}/equipment` | 装備一括変更 |
| `DELETE` | `/api/characters/{id}/equipment/{slot}/unequip` | 装備解除 |

## スキル

| メソッド | エンドポイント | 説明 |
|---------|--------------|------|
| `GET` | `/api/characters/{id}/skills` | 習得済みスキル一覧 |
| `POST` | `/api/characters/{id}/skills/{skillId}` | スキルポイントを消費してスキル習得 |

### スキル習得レスポンス仕様

- レベル条件未達、スキルポイント不足、習得済みの場合は 400 系エラーを返す
- 成功時は `201 Created` を返し、`/api/characters/{id}/skills` を `Location` ヘッダに設定する

## ショップ

| メソッド | エンドポイント | 説明 |
|---------|--------------|------|
| `GET` | `/api/shop` | 商品一覧 |
| `POST` | `/api/characters/{id}/shop/buy` | 購入（キャラクターの所持金・インベントリを更新） |
| `POST` | `/api/characters/{id}/shop/sell` | 売却（売却価格に応じて所持金・インベントリを更新） |

### 売却リクエスト例

```json
{
  "itemId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "quantity": 1
}
```

## マスタ系

| メソッド | エンドポイント | 説明 |
|---------|--------------|------|
| `GET` | `/api/items` | アイテムマスタ一覧 |
| `GET` | `/api/enemies` | 敵マスタ一覧 |
| `GET` | `/api/jobs/{id}` | 職業個別取得 |

## 転職

| メソッド | エンドポイント | 説明 |
|---------|--------------|------|
| `GET` | `/api/jobs` | 職業一覧（ランク・転職条件含む） |
| `GET` | `/api/characters/{id}/jobs` | キャラクターの転職・習熟状況一覧 |
| `POST` | `/api/characters/{id}/job/change` | 転職（前提条件チェック・レベルリセット） |

### 転職リクエスト例

POST /api/characters/{id}/job/change

```json
{
  "jobId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```

### 転職レスポンス例（成功）

```json
{
  "characterId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "newJob": {
    "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "name": "魔剣士",
    "rank": "intermediate"
  },
  "level": 1
}
```

### 転職レスポンス例（失敗：前提条件未達）

```json
{
  "error": "JOB_REQUIREMENT_NOT_MET",
  "message": "転職条件を満たしていません",
  "requiredJobs": [
    { "id": "...", "name": "戦士", "mastered": true },
    { "id": "...", "name": "魔法使い", "mastered": false }
  ]
}
```

> 転職時は `characters.level` を 1 にリセットし、`character_jobs` に転職履歴を記録する。前提職業がすべて `mastered = true` でない場合は 400 エラーを返す。

## HTTPメソッドの使い分け

| メソッド | 用途 |
|---------|------|
| `GET` | リソース取得（副作用なし） |
| `POST` | リソース作成・アクション実行 |
| `PUT` | リソース全体の置き換え（装備一括） |
| `PATCH` | リソースの部分更新（キャラ名変更） |
| `DELETE` | リソース削除 |

## enumの表記ルール

- APIレスポンス・DB保存値ともに **lower_case** に統一する
- 例：`win` / `lose` / `escaped` / `in_progress`
