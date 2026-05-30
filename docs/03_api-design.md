# API設計

## キャラクター

| メソッド | エンドポイント | 説明 |
|---------|--------------|------|
| `POST` | `/api/characters` | キャラ作成 |
| `GET` | `/api/characters/{id}` | キャラ情報取得 |
| `GET` | `/api/characters/{id}/status` | ステータス詳細 |
| `PATCH` | `/api/characters/{id}` | キャラ名変更（部分更新） |
| `PUT` | `/api/characters/{id}/stats` | ステータスポイント配分（全体更新） |
| `DELETE` | `/api/characters/{id}` | キャラクター削除 |

## ダンジョン探索

| メソッド | エンドポイント | 説明 |
|---------|--------------|------|
| `GET` | `/api/dungeons` | ダンジョン一覧 |
| `POST` | `/api/characters/{id}/explore/start` | 探索開始 |
| `GET` | `/api/characters/{id}/explore/current` | 現在地・部屋情報 |
| `POST` | `/api/characters/{id}/explore/move` | 次の部屋へ移動（戦闘自動開始） |
| `POST` | `/api/characters/{id}/explore/escape` | ダンジョン脱出 |

## 戦闘

| メソッド | エンドポイント | 説明 |
|---------|--------------|------|
| `GET` | `/api/characters/{id}/battle/current` | 戦闘状況取得 |
| `POST` | `/api/characters/{id}/battle/action` | アクション実行（attack / skill / item / run） |

### 戦闘アクションレスポンス例（勝利）

```json
{
  "battleResult": "win",
  "expGained": 120,
  "levelUp": {
    "occurred": true,
    "newLevel": 5,
    "statsGained": {
      "hp": 10,
      "attack": 3,
      "defense": 2
    },
    "skillsLearned": ["ファイアボール"]
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

## ショップ

| メソッド | エンドポイント | 説明 |
|---------|--------------|------|
| `GET` | `/api/shop` | 商品一覧 |
| `POST` | `/api/characters/{id}/shop/buy` | 購入（キャラクターの所持金・インベントリを更新） |

## マスタ系

| メソッド | エンドポイント | 説明 |
|---------|--------------|------|
| `GET` | `/api/jobs` | 職業一覧 |
| `GET` | `/api/items` | アイテムマスタ一覧 |
| `GET` | `/api/enemies` | 敵マスタ一覧 |

## HTTPメソッドの使い分け

| メソッド | 用途 |
|---------|------|
| `GET` | リソース取得（副作用なし） |
| `POST` | リソース作成・アクション実行 |
| `PUT` | リソース全体の置き換え（ステータス配分・装備一括） |
| `PATCH` | リソースの部分更新（キャラ名変更） |
| `DELETE` | リソース削除 |

## enumの表記ルール

- APIレスポンス・DB保存値ともに **lower_case** に統一する
- 例：`win` / `lose` / `escaped` / `in_progress`
