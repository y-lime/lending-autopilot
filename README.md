# lending — 備品貸出管理API

AgentCoreハンズオンで、自律実装エージェントに改修させる題材のアプリケーションです。

- Java 21 / Spring Boot 3.5 / MyBatis（アノテーション方式）/ H2（PostgreSQL互換モード）
- 3層構成（`web` → `service` → `mapper`）。依存方向は `ArchitectureTest`（ArchUnit）で固定
- 業務日付は `Clock` から取得する（テストでは `TestClockConfig` で 2026-10-15 に固定）

## ビルドとテスト

```bash
./mvnw -B verify
```

## API

| メソッド | パス | 内容 |
|---|---|---|
| GET | `/api/equipments` | 備品一覧 |
| GET | `/api/equipments/{id}` | 備品詳細 |
| POST | `/api/loans` | 貸出（`{"equipmentId":1,"borrower":"tanaka","days":7}`） |
| POST | `/api/loans/{id}/return` | 返却 |

## 開発ルール（エージェントも同じルールに従う）

- 振る舞いを変えたら、必ずテストを追加・更新する
- テストを無効化（`@Disabled`）したり、削除したりしない
- 依存ライブラリ（`pom.xml`）とCI設定（`.github/`）は変更しない。必要なら提案として書く
