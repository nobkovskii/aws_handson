# 4.Amazon DynamoDB の紹介とハンズオン（実施しません）

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



## Amazon DynamoDBの特徴

> * フルマネージド型のNoSQLデータベースサービス
> * 信頼性が高い
>   * 3つのアベイラビリティゾーンに保存される
> * 性能要件に応じて、テーブルごとにスループットキャパシティを定義する
>   * キャパシティのAuto Scaling、オンデマンドキャパシティといった設定も可能
> * ストレージの容量制限が無い
> * 料金体系
>   * 設定したReadキャパシティユニット・Writeキャパシティユニット（無料利用枠有り）＋ストレージ利用料（＋オプション機能料金）



## Next

[＜ Amazon API Gateway ハンズオン② API Gateway と Lambda を組み合わせる](./22_apigateway_2.md)

[Amazon RDSの概要 ＞](./40_rds.md)