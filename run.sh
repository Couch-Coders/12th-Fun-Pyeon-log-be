echo ${SECRET_FILE} >> env.json
sed 's/*/"/g' env.json >> secureFile.json
java -Dserver.port=$PORT $JAVA_OPTS -Dspring.profiles.actice=prod -jar ./build/libs/demo-0.0.1-SNAPSHOT.jar