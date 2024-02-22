FROM azul/zulu-openjdk:17

ENV GEERA_DB_USER=$GEERA_DB_USER
ENV GEERA_DB_PASSWORD=$GEERA_DB_PASSWORD
ENV GEERA_JWT_SECRET=$GEERA_JWT_SECRET
ENV JAVA_OPTS="-Dcom.sun.management.jmxremote \
-Dcom.sun.management.jmxremote.port=9010 \
-Dcom.sun.management.jmxremote.rmi.port=9010 \
-Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.ssl=false \
-Djava.rmi.server.hostname=101.101.216.182 \"


WORKDIR /app

COPY build/libs/geera-0.0.1-SNAPSHOT.jar .

CMD ["java $JAVA_OPTS",  "-jar", "geera-0.0.1-SNAPSHOT.jar"]