web: java -javaagent:dd-java-agent.jar \
          -Ddd.service=farmlog-b \
          -Ddd.env=prod \
          -Ddd.version=1.0 \
          -Ddd.logs.injection=true \
          -Dserver.port=$PORT \
          -jar build/libs/farmlogs-0.0.1-SNAPSHOT.jar