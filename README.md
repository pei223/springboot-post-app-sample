# postapp
ユーザーごとに記事を作成するサンプルアプリ

## Spring boot
以下のサイトで構築

https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.6.1&packaging=jar&jvmVersion=11&groupId=com.example&artifactId=postapp&name=postapp&description=Demo%20project%20for%20Spring%20Boot&packageName=com.example.postapp&dependencies=devtools,web,session,thymeleaf,security,data-jpa,validation

https://start.spring.io/ 
- spring data jpa
- spring security
- mysql driver
- thymeleaf
- spring boot devtools
- spring web
- validation
- spring session


## アプリ実行
### デバッグ動作確認
```
mvn spring-boot:run -D"spring-boot.run.profiles"=dev,common
```

## プロジェクトビルド
オンプレ用
```
mvn package spring-boot:repackage -D"spring-boot.run.profiles"=onpre,common
docker-compose up -d --build
```

## docker-compose関連
消去
```
docker-compose rm -s
```

止める
```
docker-compose stop
```



## 認証関連参考サイト
- https://www.bezkoder.com/spring-boot-jwt-authentication/ 


## カバレッジ・テスト
https://littleengineer.jp/%E3%82%B3%E3%83%BC%E3%83%89%E3%81%AE%E3%82%AB%E3%83%90%E3%83%AC%E3%83%83%E3%82%B8%E3%81%AE%E5%8F%96%E5%BE%97%E6%96%B9%E6%B3%95/

```
mvn test jacoco:report -D"spring-boot.run.profiles"=dev,common
```


## PostgreSQL Docker
```
docker rm postgreDB --force

docker build -t postgre_db_image -f dockerfiles/PostgreDBDockerfile .
docker run -d --name postgre_db -p 15432:5432  --restart always postgre_db_image
```