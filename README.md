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


## MySQL設定
resources/application.propertiesで設定


## 認証関連参考サイト
- https://www.bezkoder.com/spring-boot-jwt-authentication/ 


## カバレッジ・テスト
https://littleengineer.jp/%E3%82%B3%E3%83%BC%E3%83%89%E3%81%AE%E3%82%AB%E3%83%90%E3%83%AC%E3%83%83%E3%82%B8%E3%81%AE%E5%8F%96%E5%BE%97%E6%96%B9%E6%B3%95/
```
mvn test jacoco:report
```