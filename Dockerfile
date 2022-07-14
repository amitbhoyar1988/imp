# Alpine Linux with OpenJDK JRE
FROM openjdk:13-alpine

# Copy war file
COPY /target/deliziahr-authentication-service-0.0.1-SNAPSHOT.jar /echo.jar
COPY /keystore.p12 /keystore.p12
COPY /ca_bundle.crt /ca_bundle.crt
COPY /certificate.crt /certificate.crt
# Copy public-privat-key

COPY private_key.der /private_key.der
COPY public_key.der /public_key.der

#RUN yes changeit | keytool -import -v -trustcacerts -alias keyAlias  -file certificate.crt -keystore /opt/openjdk-13/lib/security/cacerts  -keypass changeit
RUN yes | keytool -import -v -trustcacerts -alias keyAlias  -file certificate.crt -keystore /opt/openjdk-13/lib/security/cacerts  -storepass changeit 

# run the app
CMD ["/opt/openjdk-13/bin/java", "-jar", "/echo.jar"]





