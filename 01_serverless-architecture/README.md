# サーバレスアーキテクチャ

## はじめに

本資料は、AWSが提供している学習コンテンツ「AWS Hands-on for Beginners」より、「[AWS Hands-on for Beginners
Serverless #1サーバーレスアーキテクチャで翻訳 Web API を構築する](https://pages.awscloud.com/event_JAPAN_Hands-on-for-Beginners-Serverless-2019_LP.html?trk=aws_introduction_page)」を参考に記載しています。

## 本ハンズオンのゴール

> • AWS Lambda, Amazon API Gateway, Amazon DynamoDB の基本を学ぶ
>
> • 上記のサービスを組み合わせて、サーバーレスな Web API を作成する

→DynamoDBの代わりに、馴染みのある「Amazon RDS」を使用します



## 本ハンズオンの前提条件・知識

> • AWS アカウントをお持ちであること
>
> • Java を書いたことがあること（必須ではありません）
>
> 　※「AWS Hands-on for Beginners」ではPythonを使用していますが、Javaに書き換えています

## Agenda

1. Serverless アーキテクチャの概要
2. AWS Lambda の紹介とハンズオン
   1. AWS Lambda の概要
   2. AWS Lambda ハンズオン① Lambda を単体で使ってみる
   3. AWS Lambda ハンズオン② 他のサービスを呼び出してみる
3. Amazon API Gateway の紹介とハンズオン
   1. Amazon API Gateway の概要
   2. Amazon API Gateway ハンズオン① API Gateway を単体で使ってみる
   3. Amazon API Gateway ハンズオン② API Gateway と Lambda を組み合わせる
4. Amazon DynamoDB の紹介とハンズオン
   1. Amazon DynamoDB の概要
   2. Amazon DynamoDB ハンズオン① テーブルを作ってみる
   3. Amazon DynamoDB ハンズオン② API Gateway と Lambda と DynamoDB を組み合わせる

## なぜサーバレスアーキテクチャなのか？

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

## AWSにおけるComputeサービス

![aws-compute-services](./img/aws-compute-service.png)

大きく分けると3種類

* EC2
* Container
* Lambda

優劣があるわけではなく、適材適所、ないしは開発・運用する人のスキルセットに応じて、使い分けることが重要。

* 自由度
* 管理の手間

## サーバレスアーキテクチャでよく使われるAWSのサービス

![aws-serverless-services](./img/aws-serverless-service.png)

computeサービス以外にも、よく使用されるサービスがある。







[^1]: オンプレやEC2だと、どのような条件でスケールするのかを検討・設計する必要があるが、サーバレスのサービスでその一部を担保出来る。
[^2]: オンプレの場合はサーバを起動しているだけで費用がかかるが、サーバレスならリクエスト毎に課金。

