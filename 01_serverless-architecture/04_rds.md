# 5.Amazon RDSの紹介とハンズオン

## Agenda

1. [Serverless アーキテクチャの概要](./01_serverless.md)
2. [AWS Lambda の紹介とハンズオン](./02_lambda.md)
   1. AWS Lambda の概要
   2. AWS Lambda ハンズオン① Lambda を単体で使ってみる
   3. AWS Lambda ハンズオン② 他のサービスを呼び出してみる（実施しません）
3. [Amazon API Gateway の紹介とハンズオン](./03_apigateway.md)
   1. Amazon API Gateway の概要
   2. Amazon API Gateway ハンズオン① API Gateway を単体で使ってみる
   3. Amazon API Gateway ハンズオン② API Gateway と Lambda を組み合わせる
4. [Amazon DynamoDB の紹介とハンズオン（実施しません）](./10_dynamodb.md)
   1. Amazon DynamoDB の概要（実施しません）
5. [【★】Amazon RDS の紹介とハンズオン](./04_rds.md)
   1. Amazon RDSの概要
   2. Amazon RDS ハンズオン① RDSを単体で使ってみる
   3. Amazon RDS ハンズオン② API Gateway と Lambda と RDS を組み合わせる
6. [終わりに](./99_end.md)



## Amazon RDSの概要

> * フルマネージドなリレーショナルデータベース
> * シンプルかつ迅速にスケール可能
> * 高速、安定したパフォーマンス
> * 低コスト、従量課金
>   * 無料利用枠有り（DBエンジン毎に計算が異なるので割愛）
> * DBエンジン
>   * MySQL、ORACLE、SQL Server、PostgreSQL、Amazon Aurora



### 特徴

> * シンプルな構築
>   * 数クリックでDBが起動
>     * DBエンジン
>     * インスタンスクラス
>     * ディスク種類とサイズ・・・
>   * 選択するだけで高度な機能を実装可能
>     * マルチAZへのデプロイメント
>     * リードレプリカ
>     * バックアップ（スナップショット）
>     * 監視、拡張モニタリング・・・
> * 高い可用性
> * パフォーマンスの向上
> * 運用負荷の軽減
>   * 自動バックアップ（RDS標準機能）
>     * 自動スナップショット＋トランザクションログをS3に保存
>   * スナップショット
>     * 1日1回自動取得（バックアップウィンドウで指定）
>       * DBインスタンスのサイズと同サイズまでストレージコストが無料
>       * DBインスタンス削除と同時に削除
>     * 最大35日分保存（0～35日で設定可能）
>     * 手動スナップショットは任意の時間に実施可能
>   * リストア
>     * リストア：スナップショットを元にDBインスタンスを作成
>     * Point-in-Timeリカバリ：指定した時刻の状態になるようにDBインスタンスを作成
> * セキュリティ
>   * VPC対応
>     * 任意のサプネットで起動可能
>   * アクセス制御
>     * デフォルトではDBインスタンスに対するネットワークアクセスはオフ
>     * セキュリティグループによりアクセス制御
>   * DBインスタンスの暗号化
>     * 保管時のインスタンスとスナップショットの暗号化が可能
>       * 対応するインスタンスタイプがある
>       * DBエンジン毎に暗号化方式が異なる



### 制限事項（例）

> * バージョンが限定される
> * キャパシティに上限がある
> * OSログインやファイルシステムへのアクセスができない
> * IPアドレスの固定が行えない
> * 一部の機能が使えない
> * 個別パッチが適用できない

→トレードオフが許容できない場合は、`On EC2`かオンプレミスで構築する



## Amazon RDS ハンズオン① RDSを単体で使ってみる

1. RDSを検索

2. 「データベースの作成」を選択

3. データベースの作成（設定する箇所は`★`を記載）

   1. データベース作成方法の選択

      1. データベース作成方法：標準作成

   2. エンジンのオプション

      1. ★エンジンのタイプ：`MySQL`
      2. バージョン：`MySQL 8.0.23`

   3. テンプレート

      1. ★テンプレート：**無料利用枠**

   4. 設定

      1. DBインスタンス識別子：`database-1`（任意）

      2. マスターユーザ名：`admin`（任意）

      3. ★マスターパスワード：任意の文字列

         ※8文字以上。スラッシュ、シングルクォート、ダブルクォート、アットマークは使用不可能

      4. ★パスワードを確認：任意の文字列

   5. DBインスタンスクラス

      1. DBインスタンスクラス：`db.t2.micro`（無料利用枠なので、そのまま）

   6. ストレージ

      1. ストレージタイプ：汎用SSD（gp2）
      2. ストレージ割当：20
      3. ★ストレージの自動スケーリングを有効にする：チェックを外す

   7. 接続

      1. VPC：デフォルト
      2. サブネットグループ：デフォルト
      3. ★パブリックアクセス：あり
      4. ★VPCセキュリティグループ：新規作成
      5. ★新しいVPCセキュリティグループ名：`rds_sg`
      6. ★アベイラビリティゾーン：`ap-northeast-1a`

   8. データベース認証

      1. データベース認証オプション：パスワード認証

   9. 追加設定

      1. データベースの選択肢
         1. ★最初のデータベース名：`rds_test`
         2. DBパラメータグループ：`default:mysql8.0`
         3. オプショングループ：`default:mysql-8.0`
      2. バックアップ
         1. ★自動バックアップを有効にします：チェックを外す
      3. メンテナンス
         1. ★マイナーバージョン自動アップグレードの有効化：チェックを外す

   10. 「データベースの作成」を選択

       作成まで数分かかります。

4. エンドポイントを確認する

   1. 作成したデータベースを選択
   2. 「接続とセキュリティ」の「エンドポイント（`database-1.xxxx.ap-northeast-1.rds.amazonaws.com`）」をコピー

5. 「AWS CloudShell」のIPを確認する

   1. グローバルIPの確認
      1. `curl https://ifconfig.io`

6. セキュリティグループの設定

   1. 「セキュリティグループ（VPCの機能）」を検索
   2. セキュリティグループ名：`rds_sg`を選択
   3. インバウンドルール→「編集（Edit inbound rules）」
   4. インバウンドルール
      1. ルールを追加
      2. タイプ：`MYSQL/Aurora`
      3. リソースタイプ：カスタム
      4. ソース：AWS CloudShellのグローバルIP
      5. ルールを保存

7. 「AWS CloudShell」からTableを作成する

   1. `mysql --version`

      1. 入っていなければ`sudo yum install mysql`を実施

   2. DBアクセス

      1. `mysql -u admin -p -h <RDS-エンドポイント> rds_test`

         ※コピーしたエンドポイントを指定する

   3. DB一覧

      1. `show databases;`

   4. Table作成

      ```sql
      create table test (
          id          int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
          input_text  varchar(255),
          output_text varchar(255)
      );
      ```

   5. Table確認

      1. `show tables;`
      2. `desc test;`



## Amazon RDS ハンズオン②  API Gateway と Lambda と RDS を組み合わせる

先程作成したRDSと、LambdaやAPI Gatewayをつなげます。



Lambda

[ソースコード](./3_rds-hands-on/)

* 依存ライブラリ（`jdbc`）もjarに含めるため（`fat jar`作成のため）に、`Gradle Shadow Plugin`を使用しています。

  * `./gradlew shadowJar`を実行することで、`fat jar`が作成されます。

    ※Windowsの場合は、同様に`.\gradlew.bat shadowJar`を実行してください

1. Lambdaを検索
2. 「myFunc2」を新規作成する
   1. 
3. jarのアップロード
   1. 上記で作成した`fat jar`を指定する
4. ハンドラ
   1. `org.example.handler.LambdaHandler::handleRequest`
5. テスト
   1. 新しいイベント
   2. テンプレート：`apigateway-aws-proxy`
   3. 名前：test-api
   4. 変更を保存→テスト



## Next

[＜ Amazon API Gateway の紹介とハンズオン](./03_apigateway.md)

[終わりに ＞](./99_end.md)

