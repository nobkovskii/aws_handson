# 2 API Gateway HandsOn（Amazon API Gateway ハンズオン② API Gateway と Lambda を組み合わせる）

jarファイル作成方法

1. 当ディレクトリに移動

2. `./gradlew shadowJar`を実行

   1. コマンドプロンプトの場合は、`.\gradlew.bat shadowJar`

      ※すでにファイルがある場合は、上書きされないこともあるので、jarファイルを削除か`./gradlew clean`してからの実施を推奨

3. `build/libs/HandsOn-1.0-SNAPSHOT-all.jar`が出力される

4. 出力されたjarファイルを、Lambdaにアップロードする

