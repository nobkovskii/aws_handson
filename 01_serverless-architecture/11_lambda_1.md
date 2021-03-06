# AWS Lambda ハンズオン① Lambda を単体で使ってみる

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

* Inputで渡した値を含んだ、Jsonを返すLambdaを作成

  ※詳細は、ソースコードをご確認ください

  * Input

  ```json
  {
    "key1": "value1",
    "key2": "value2",
    "key3": "value3"
  }
  ```

  * Output

  ```json
  {
    "input": {
      "key1": "value1",
      "key2": "value2",
      "key3": "value3"
    },
    "context": {
      "awsRequestId": "xxx",
      "logGroupName": "/aws/lambda/myFunc",
      "logStreamName": "YYYY/MM/DD/xxx",
      "functionName": "myFunc",
      "functionVersion": "$LATEST",
      "invokedFunctionArn": "arn:aws:lambda:ap-northeast-1:xxx:function:myFunc",
      "logger": {},
      "remainingTimeInMillis": 15000,
      "memoryLimitInMB": 512
    },
    "body": "test",
    "statusCode": "200"
  }
  ```

![img](./img/img_01.jpg)



### 手順

1. Lambdaを検索

   ![handson-01](./img/lambda_handson_01.png)

2. 「関数の作成」を選択![handson-02](./img/lambda_handson_02.png)

3. 「関数の作成」

   1. オプション：「一から作成」

   2. 関数名：任意（myFunc）

   3. ランタイム：Java8 on Amazon Linux 2

      Q：Amazon Linux1と2の違いは？

      A：2は、EC2上のパフォーマンスが最適化されるように、カーネルが調整されています。

      　　1は、2020年12月31日でEOL（End of Life）となっているので、基本的には「2」を使いましょう。

   4. アーキテクチャ：x86_64

   5. アクセス権限：デフォルト

   ![handson-03](./img/lambda_handson_03.png)

4. 作成後

   ![handson-04](./img/lambda_handson_04.png)
   
5. jarのアップロード（CLIで実施）

   1. jarの準備（CloudShellにて実施）

      1. 下記コマンドを実行

         ```bash
         cd ~/aws_handson/01_serverless-architecture/1_lambda-hands-on/
         
         // gradlewの実行権限変更
         chmod 775 ./gradlew
         
         // ビルド（jar）の作成
         // 成功すると「build/libs/HandsOn-1.0-SNAPSHOT.jar」が出力される
         ./gradlew build
         
         ```

   2. jarのアップロード

      1. 下記コマンドを実行

         ```bash
         cd ~/aws_handson/01_serverless-architecture/1_lambda-hands-on/build/libs/
         
         // ハンドラ（＝実行するメソッド）の変更
         aws lambda update-function-configuration --function-name myFunc --handler  org.example.LambdaHandler::handleRequest
         
         // jarのアップロード
         aws lambda update-function-code --function-name myFunc --zip-file fileb://HandsOn-1.0-SNAPSHOT.jar
         
         ```

         ▼参考

         * https://docs.aws.amazon.com/ja_jp/lambda/latest/dg/java-package.html
         * https://docs.aws.amazon.com/cli/latest/reference/lambda/update-function-configuration.html

7. テスト実行

   1. 「テストタブ」＞「新しいイベント」

   ![handson-08](./img/lambda_handson_08.png)

   ![handson-09](./img/lambda_handson_09.png)



## Next

[＜ AWS Lambda の概要](./10_lambda.md)

[Amazon API Gateway の概要 ＞](./20_apigateway.md)

