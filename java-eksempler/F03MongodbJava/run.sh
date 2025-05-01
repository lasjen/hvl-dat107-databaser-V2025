javac -d bin \
      -cp "lib/bson-5.4.0.jar:lib/mongodb-driver-core-5.4.0.jar:lib/mongodb-driver-sync-5.4.0.jar:lib/slf4j-api-2.0.0-alpha1.jar:lib/slf4j-nop-2.0.0-alpha1.jar" \
      src/no/hvl/dat107/*.java \
      src/no/hvl/dat107/model/*.java \
      src/no/hvl/dat107/config/*.java \
      src/no/hvl/dat107/repository/*.java

java -cp "bin:lib/bson-5.4.0.jar:lib/mongodb-driver-core-5.4.0.jar:lib/mongodb-driver-sync-5.4.0.jar:lib/slf4j-api-2.0.0-alpha1.jar:lib/slf4j-nop-2.0.0-alpha1.jar" \
          no.hvl.dat107.MongoDb_EmbeddingCRUD