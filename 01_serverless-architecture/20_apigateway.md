# Amazon API Gateway の概要

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



### API Gatewayの特徴

> * サーバをプロビジョニング／管理することなく、APIの作成・管理出来るマネージドサービス。
> * 可用性の担保やスケーラビリティの確保、APIキーの管理と言った作業を、API Gatewayに任せる事ができ、開発者はAPIの開発に注力することが可能。
> * 「REST API」「WebSocket」「HTTP API」に対応
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



## Next

[＜ AWS Lambda ハンズオン① Lambda を単体で使ってみる](./11_lambda_1.md)

[Amazon API Gateway ハンズオン① API Gateway を単体で使ってみる ＞](./21_apigateway_1.md)

