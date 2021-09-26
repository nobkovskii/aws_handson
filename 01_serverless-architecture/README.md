# サーバレスアーキテクチャ

## はじめに

本資料は、AWSが提供している学習コンテンツ「AWS Hands-on for Beginners」より、「[AWS Hands-on for Beginners
Serverless #1サーバーレスアーキテクチャで翻訳 Web API を構築する](https://pages.awscloud.com/event_JAPAN_Hands-on-for-Beginners-Serverless-2019_LP.html?trk=aws_introduction_page)」を参考に記載しています。

## 本ハンズオンのゴール

> • AWS Lambda, Amazon API Gateway, Amazon DynamoDB の基本を学ぶ
>
> • 上記のサービスを組み合わせて、サーバーレスな Web API を作成する

→DynamoDBの代わりに、馴染みのある「Amazon RDS」を使用します。

![architecture](./img/architecture.png)

### 【補足】サーバレスとRDSの組み合わせについて

基本的に、サーバレスとRDSの組み合わせは**アンチパターン**です。

詳細については解説しませんが、Lambdaはリクエスト毎にコンテナを作成（1リクエスト毎に1コンテナを作成して1イベントのみ処理）し、そのコンテナ毎にDBコネクションを張ります。コンテナ間でコネクションプーリングを共有するのは困難であり、最大同時接続数の問題が発生します。

「[Amazon RDS Proxy](https://pages.awscloud.com/rs/112-TZM-766/images/EV_amazon-rds-aws-lambda-update_Jul28-2020_RDS_Proxy.pdf)（言うなれば、Lambda用のコネクションプーリングサービス）」がリリースされた事により、当アンチパターンは解消・解決となりますが、コスト面等の考慮が必要なため「完全解決」には至っていません。

なお、本ハンズオンでは「RDS Proxy」は使用しません。



## 本ハンズオンの前提条件・知識

> • AWS アカウントをお持ちであること
>
> • Java を書いたことがあること（必須ではありません）

→「AWS Hands-on for Beginners」ではPythonを使用していますが、Javaに書き換えています



## Agenda

1. Serverless アーキテクチャの概要
2. AWS Lambda の紹介とハンズオン
   1. AWS Lambda の概要
   2. AWS Lambda ハンズオン① Lambda を単体で使ってみる
   3. AWS Lambda ハンズオン② 他のサービスを呼び出してみる（実施しません）
3. Amazon API Gateway の紹介とハンズオン
   1. Amazon API Gateway の概要
   2. Amazon API Gateway ハンズオン① API Gateway を単体で使ってみる
   3. Amazon API Gateway ハンズオン② API Gateway と Lambda を組み合わせる
4. Amazon DynamoDB の紹介とハンズオン（実施しません）
   1. Amazon DynamoDB の概要（実施しません）
5. Amazon RDSの紹介とハンズオン
   1. Amazon RDSの概要
   2. Amazon RDS ハンズオン① RDSを単体で使ってみる
   3. Amazon RDS ハンズオン②  API Gateway と Lambda と RDS を組み合わせる



# 1.Serverless アーキテクチャの概要

### なぜサーバレスアーキテクチャなのか？

開発者は何をしたいのか？
→エンドユーザに価値を届ける

ビジネスには繋がらない（エンドユーザには見えない）が、必要な作業がある。

* サーバのセットアップ
* ミドルウェアランタイムのセットアップ
* セキュリティパッチの適用
* 耐障害性を確保するためのアーキテクチャ検討

サーバレスアーキテクチャは、これらの作業を**マネージドサービスに任せる**という特徴がある。

サーバレスアーキテクチャの主な特徴

* インフラのプロビジョニングや管理が不要
* 自動でスケール[^1]
* 高い可用性[^1]
* 価値に対する支払い[^2]



### AWSにおけるComputeサービス

![aws-compute-services](./img/aws-compute-service.png)

大きく分けると3種類

* EC2
* Container
* Lambda

優劣があるわけではなく、適材適所、ないしは開発・運用する人のスキルセットに応じて、使い分けることが重要。

* 自由度
* 管理の手間



### サーバレスアーキテクチャでよく使われるAWSのサービス

![aws-serverless-services](./img/aws-serverless-service.png)

computeサービス以外にも、よく使用されるサービスがある。



# 2.AWS Lambda の紹介とハンズオン

## AWS Lambda の概要

### AWS Lambda の特徴

> * サーバーのプロビジョニング/管理なしでプログラムを実⾏できるサービス 
> * コードの実⾏やスケーリングに必要なことは、Lambda 側で実施するので、 開発者の⽅はコードを書くことにより集中できる
> * リクエストベースの料⾦体系
>   * 実行回数＋実行時間
>     * それぞれ無料枠有り

### AWS Lambda におけるコーディングイメージ

* 対応言語
  * Java、Go、PowerShell、Node.js、C#、Python、Ruby
  * サポートされていない言語は、カスタムランタイムを実装することで利用可能

### AWS Lambda で設定できる項⽬ 

* 確保するメモリの量
  * 128MB 〜 3,008MB （64MBごと）
  * CPU 能⼒は確保するメモリの量に⽐例
* タイムアウト値
  * 最⼤で 900秒
* 実⾏ IAM ロール

### Lambdaのイベントソースと呼び出しタイプ

* 非同期呼び出し
  * 「Lambdaへのリクエストが正常に受け付けられたかどうかのみ」を返却
* 同期呼び出し
  * Lambdaの実行完了時にレスポンスを返却

### Lambda Functionのライフサイクル

* Lambdaは呼び出されると、コンテナ上でプログラムが実行する
* 1つのコンテナで同時に実行できるのは、1つのリクエストまで
* コンテナは再利用されるが、利用可能なコンテナが無い時はコールドスタート

![LambdaFunctionのライフサイクル](./img/lambda_func_lifecycle.png)



## AWS Lambda ハンズオン① Lambda を単体で使ってみる

1. Lambdaを検索

   ![handson-01](./img/lambda_handson_01.png)

2. 「関数の作成」を選択![handson-02](./img/lambda_handson_02.png)

3. 「関数の作成」

   1. 関数名：任意（myFunc）
   2. ランタイム：Java8 on Amazon Linux 1
   3. アクセス権限：デフォルト

   ![handson-03](./img/lambda_handson_03.png)

4. 作成後

   ![handson-04](./img/lambda_handson_04.png)
   
5. jarのアップロード

   [ソースコード](./1_lambda-hands-on/)

   1. ソースコードを任意の箇所にダウンロード

   2. ダウンロードしたディレクトリに移動して、`./gradlew build`

      1. Windows（コマンドプロンプト）の場合は「`.\gradlew.bat build`」

         ※すでにファイルがある場合は、上書きされない可能性があるので、削除してから行う

   3. `build/libs/HandsOn-1.0-SNAPSHOT.jar`が出力される

   4. 出力されたjarファイルを使用する

   ![handson-05](./img/lambda_handson_05.png)

6. メソッドの指定

   * ランタイム：Java 8 on Amazon Linux 1
   * ハンドラ：`org.example.LambdaHandler::handleRequest`

   ![handson-06](./img/lambda_handson_06.png)

   ![handson-07](./img/lambda_handson_07.png)

7. テスト実行

   ![handson-08](./img/lambda_handson_08.png)

   ![handson-09](./img/lambda_handson_09.png)



# 3.Amazon API Gateway の紹介とハンズオン

## Amazon API Gateway の概要

### API Gatewayの特徴

> * サーバをプロビジョニング／管理することなく、APIの作成・管理出来るマネージドサービス。
> * 可用性の担保やスケーラビリティの確保、APIキーの管理と言った作業を、API Gatewayに任せる事ができ、開発者はAPIの開発に注力することが可能。
> * 「REST API」と「WebSocket」に対応
>   * 本ハンズオンでは「REST API」を使用
>   * リクエストベースの料金体系
>     * 実行回数（＋キャッシュメモリ量）＋データ転送量



### API Gatewayの使い方（REST APIの場合）

大きく5つのフェーズに分かれる。

1. リソースとメソッドタイプの定義
2. メソッドリクエストの設定
   * 認証の設定、クエリパラメータ、HTTPヘッダの設定など
3. 統合タイプの設定
   * バックエンドの種別を選択する
     * Lambda、HTTP、Mock、AWSサービス、VPCリンク
4. リクエスト／レスポンス変換の定義
   * バックエンドへのInput、バックエンドからのOutputを変換することが可能
   * 「プロキシ統合」を使用すると、変換せずに返すことが可能
5. デプロイ

![apigateway_howto](./img/apigateway_howto.png)



## Amazon API Gateway ハンズオン① API Gateway を単体で使ってみる

### Mockデータを返すAPIを作成する

1. API Gatewayを検索

2. 「APIを作成」を選択

3. APIの作成

   1. プロトコル：REST
   2. 新しいAPIの作成：新しいAPI
   3. 名前と説明
      1. API名：mock-api
      2. 説明：空欄
      3. エンドポイントタイプ：リージョン

4. 「アクション」→「リソースの作成」を選択

   1. リソース名：sample

5. 作成した「sample」を選択した状態で、「アクション」→「メソッドの作成」を選択

   1. プルダウン：GET
   2. チェックボタンを押下

6. セットアップ

   1. 統合タイプ：Mock

7. 統合レスポンスを選択

   1. マッピングテンプレート：`application/json`

   2. json内容

      ```json
      {
          "statusCode":200,
          "body":"hoge"
      }
      ```

8. 「メソッドの実行」→「テスト」→「テスト」を実行

9. デプロイ

   1. 「アクション」→「APIのデプロイ」
      1. デプロイされるステージ：新しいステージ
      2. ステージ名：dev

10. 「ステージ」→「dev」→「URLの呼び出し」を選択





## Amazon API Gateway ハンズオン② API Gateway と Lambda を組み合わせる

### 入力した文字列の末尾に「`-nyan`」とつけるAPIを作成する:cat:

※「この程度ならLambdaいらないじゃん」とか言わないで・・・

[ソースコード](./2_apigateway-hands-on/)

* 依存ライブラリ（`Gson`）もjarに含めるため（`fat jar`作成のため）に、`Gradle Shadow Plugin`を使用しています。

  * `./gradlew shadowJar`を実行することで、`fat jar`が作成されます。

    ※Windowsの場合は、同様に`.\gradlew.bat shadowJar`を実行してください

1. Lambdaを検索
2. 先程作成した「myFunc」を使用する
3. jarのアップロード
   1. 上記で作成した`fat jar`を指定する
4. テスト
   1. 新しいイベント
   2. テンプレート：`apigateway-aws-proxy`
   3. 名前：test-api
   4. 変更を保存→テスト



### API Gatewayの設定

1. API Gatewayを検索

2. 「APIを作成」を選択

3. APIの作成

   1. プロトコル：REST
   2. 新しいAPIの作成：新しいAPI
   3. 名前と説明
      1. API名：test-api
      2. 説明：空欄
      3. エンドポイントタイプ：リージョン

4. 「アクション」→「リソースの作成」を選択

   1. リソース名：test

5. 作成した「test」を選択した状態で、「アクション」→「メソッドの作成」を選択

   1. プルダウン：GET
   2. チェックボタンを押下

6. セットアップ

   1. 統合タイプ：Lambda 関数
   2. Lambda プロキシ統合の使用：チェックを入れる
   3. Lambda関数：myFunc
   4. Lambda関数に権限を追加する：OK

7. メソッドリクエストを選択

   1. URLクエリ文字列パラメータ

      1. 名前：`input_text`

      2. 必須：チェックを入れる

         ※一度作成しないとチェックを行えない

8. 「メソッドの実行」→「テスト」→「テスト」を実行

   1. クエリ文字列

      `input_text=hoge`

9. デプロイ

   1. 「アクション」→「APIのデプロイ」
      1. デプロイされるステージ：dev

10. 「ステージ」→「dev」→「URLの呼び出し」を選択

    1. URLの末尾に`/test?input_text=bar`を追加



# 4.Amazon DynamoDB の紹介とハンズオン（実施しません）

## Amazon DynamoDBの特徴

> * フルマネージド型のNoSQLデータベースサービス
> * 信頼性が高い
>   * 3つのアベイラビリティゾーンに保存される
> * 性能要件に応じて、テーブルごとにスループットキャパシティを定義する
>   * キャパシティのAuto Scaling、オンデマンドキャパシティといった設定も可能
> * ストレージの容量制限が無い
> * 料金体系
>   * 設定したReadキャパシティユニット・Writeキャパシティユニット（無料利用枠有り）＋ストレージ利用料（＋オプション機能料金）



# 5.Amazon RDSの紹介とハンズオン

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





# おわりに

## 落ち穂拾い：例外処理

* 今回作成したソースコードでは例外処理を実装していません。
  * 実際の開発時は、例外ハンドリングを適切に行なってください。



## まとめ

* サーバレスの特徴や利点について学びました。
* AWS Lambda、Amazon API Gateway、Amazon RDSといったAWSサービスの基礎知識を学びました。
* 実際に手を動かして、サーバレスアーキテクチャでWebAPIを作成しました。



## 今後

* 深く学ぶ
  * サーバレスアーキテクチャで、なにかプロダクトを作成する
    * チャットボット、オリジナルWebAPI・・・
  * DynamoDBなど、他のサーバレスアーキテクチャでよく使われるAWSのサービスを使用してみる
  * SpringBootなど、AWSサービス以外の部分をサーバレスアーキテクチャに準拠させてみる
  * 運用してみる
* 広く学ぶ
  * AWSのハンズオンを受講してみる
  * 資格取得のための学習をする



[^1]: オンプレやEC2だと、どのような条件でスケールするのかを検討・設計する必要があるが、サーバレスのサービスでその一部を担保出来る。
[^2]: オンプレの場合はサーバを起動しているだけで費用がかかるが、サーバレスならリクエスト毎に課金。

