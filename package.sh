# 直接打包pom项目，生成其他需要模块的jar包
mvn clean
mvn package -pl dzzhyk-springframework-pom -am