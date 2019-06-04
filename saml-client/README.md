# SAML Client Server
Small server which works as an SAML client.

You can either run it directly with maven with ``mvn spring-boot:run`` and edit the relevant proerties in the ``src/main/resources/application.properties`` file. In this case you also have to place the idp metadata in the ``src/main/resources/idp.xml`` file.

Or you can build the contained ``Dockerfile`` and start it with following commands:
```
docker build -t samlclient .
docker run --rm -it -e SAML_SPENTITYID=samltest -e SERVER_PORT=8080 -p 8086:8080 -v /my/idp.xml:/config/idp.xml samlclient
```
In this case you have to map the idp.xml file into the container to ``/config/idp.xml`` and give the other parameters as environment variables.

Available configs:
* saml.spEntityId: saml SP entity id
* saml.assertionConsumerUrl: url for the redirect back where the saml assertion is consumed. has to be `http(s)://<host>(:<port>)/samllogin`
* server.port: port server listens to (from spring-boot)