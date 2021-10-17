# Amazon API Gateway ハンズオン② API Gateway と Lambda を組み合わせる

## Agenda

1. [Serverless アーキテクチャの概要](./01_serverless.md)
2. AWS Lambda の紹介とハンズオン
   1. [AWS Lambda の概要](./10_lambda.md)
   2. [AWS Lambda ハンズオン① Lambda を単体で使ってみる](./11_lambda_1.md)
   3. AWS Lambda ハンズオン② 他のサービスを呼び出してみる（実施しません）
3. Amazon API Gateway の紹介とハンズオン
   1. [Amazon API Gateway の概要](./20_apigateway.md)
   2. [Amazon API Gateway ハンズオン① API Gateway を単体で使ってみる](./21_apigateway_1.md)
   3. [Amazon API Gateway ハンズオン② API Gateway と Lambda を組み合わせる](./22_apigateway_2.md)
4. Amazon DynamoDB の紹介とハンズオン（実施しません）
   1. [Amazon DynamoDB の概要（実施しません）](./30_dynamodb.md)
5. Amazon RDS の紹介とハンズオン
   1. [Amazon RDSの概要](./40_rds.md)
   2. [Amazon RDS ハンズオン① RDSを単体で使ってみる](./41_rds_1.md)
   3. [Amazon RDS ハンズオン② API Gateway と Lambda と RDS を組み合わせる](./42_rds_2.md)
6. [終わりに](./99_end.md)



### 概要

* 入力した文字列の末尾に「`-nyan`」とつけるAPIを作成する:cat:

※「この程度ならLambdaいらないじゃん」とか言わないで・・・



1. lambda用のjarを作成
2. API Gateway から、Lambdaを呼び出す



![img](./img/img_03.jpg)



### 手順（Lambdaの設定）

1. jarの準備（CloudShellにて実施）

   1. 下記コマンドを実行

      ```bash
      cd ~/aws_handson/01_serverless-architecture/2_apigateway-hands-on/
      
      // gradlewの実行権限変更
      chmod 775 ./gradlew
      
      // ビルド（jar）の作成
      // 依存ライブラリ（Gson）もjarに含めるため（fat jar作成のため）に、「Gradle Shadow Plugin」を使用しています。
      // 成功すると「build/libs/HandsOn-1.0-SNAPSHOT-all.jar」が出力される
      ./gradlew shadowJar
      
      ```

2. jarのアップロード

   1. 下記コマンドを実行

      ```bash
      cd ~/aws_handson/01_serverless-architecture/2_apigateway-hands-on/build/libs/
      
      // ハンドラ（＝実行するメソッド）の変更
      aws lambda update-function-configuration --function-name myFunc --handler org.example.handler.LambdaHandler::handleRequest
      
      // jarのアップロード
      aws lambda update-function-code --function-name myFunc --zip-file fileb://HandsOn-1.0-SNAPSHOT-all.jar
      
      ```

3. Lambdaを検索（AWSマネジメントコンソールにて実施）

4. `myFunc`を選択

5. 「テストタブ」＞「新しいイベント」

   1. 新しいイベント

   2. テンプレート：`apigateway-aws-proxy`

      ![api-lambda](./img/api-lambda_02.png)

   3. 名前：test-api

   4. `queryStringParameters`を修正（7～9行目あたり）

      ※引数にて`input_text`として取得しているため

      ```diff
        "queryStringParameters": {
      -    "foo": "bar"
      +    "input_text": "bar"
        },
      ```

      ![api-lambda](./img/api-lambda_03.png)

   5. 変更を保存→テスト

      ![api-lambda](./img/api-lambda_04.png)



### 手順（API Gatewayの設定）

1. API Gatewayを検索

2. 「REST API」＞「構築」を選択

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

      ![api-lambda](./img/api-lambda_05.png)

   4. Lambda関数に権限を追加する：OK

      ![api-lambda](./img/api-lambda_06.png)

7. メソッドリクエストを選択

   ![api-lambda](./img/api-lambda_07.png)

   1. URLクエリ文字列パラメータ

      1. 名前：`input_text`

         ![api-lambda](./img/api-lambda_08.png)

      2. 必須：チェックを入れる

         ※一度作成しないとチェックを行えない

         ![api-lambda](./img/api-lambda_09.png)

8. 「メソッドの実行」→「テスト」→「テスト」を実行

   ![api-lambda](./img/api-lambda_10.png)

   1. クエリ文字列

      `input_text=foo`

      ![api-lambda](./img/api-lambda_11.png)

9. デプロイ

   1. 「アクション」→「APIのデプロイ」
      1. デプロイされるステージ：dev

10. 「ステージ」→「dev」→「URLの呼び出し」を選択

    1. URLの末尾に`/test?input_text=bar`を追加



## Next

[＜ Amazon API Gateway ハンズオン① API Gateway を単体で使ってみる](./21_apigateway_1.md)

[Amazon DynamoDB の概要（実施しません） ＞](./30_dynamodb.md)

