
### 安装本地 jar 到本地 maven 仓库

groupId="com.github.cuinipeng"
artifactId="xxx"
version="xxx"
jarfile="xxx"
mvn install:install-file -DgroupId=${groupId} -DartifactId=${artifactId} -Dversion=${version} -Dpackaging=jar -Dfile=${jarfile}
