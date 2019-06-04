# OpenId Client Server
Small server which works as an OpenId client.

You can either run it directly with maven with ``mvn spring-boot:run`` and edit the relevant proerties in the ``src/main/resources/application.properties`` file.

Or you can build the contained ``Dockerfile`` and start it with following commands:
```
docker build -t openidclient .
docker run --rm -it -e OPENID_CLIENTID=<clienid> -e SERVER_PORT=8080 -p 8086:8080 openidclient
```
In this case you have to specify the parameters as environment variables.

Available configs:
* openid.clientId: openid client id
* openid.clientSecret: openid client secret
* openid.accessTokenUri: url for the openid token endpoint
* openid.userAuthorizationUri: url for the openid authorization page
* openid.redirectUri: url for the redirect back to the openid client, has to be `http(s)://<host>(:<port>)/openidlogin`
* openid.additionalScopes: additional scopes (scope openid is always included)
* server.port: port server listens to (from spring-boot)