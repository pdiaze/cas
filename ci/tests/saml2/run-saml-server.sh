#!/bin/bash

if [[ -z "${SP_SLO_SERVICE}" ]]; then
  export SP_SLO_SERVICE="https://localhost:8443/cas/login?client_name=SAML2Client&logoutendpoint=true"
else
  echo -e "Found existing SLO service at ${SP_SLO_SERVICE}"
fi

if [[ -z "${SP_ACS_SERVICE}" ]]; then
  export SP_ACS_SERVICE="https://localhost:8443/cas/login?client_name=SAML2Client"
else
  echo -e "Found existing ACS service at ${SP_ACS_SERVICE}"
fi

if [[ -z "${SP_ENTITY_ID}" ]]; then
  export SP_ENTITY_ID="cas:apereo:pac4j:saml"
else
  echo -e "Found existing SP entity id at ${SP_ENTITY_ID}"
fi

docker stop simplesamlphp-idp || true && docker rm simplesamlphp-idp || true
echo -e "Running SAML2 IdP with SP entity id ${SP_ENTITY_ID} and SP ACS service ${SP_ACS_SERVICE} on port 9443"

openssl req -newkey rsa:3072 -new -x509 -days 365 \
  -nodes -out /tmp/saml.crt -keyout /tmp/saml.pem \
  -subj "/C=PE/ST=Lima/L=Lima/O=Acme Inc. /OU=IT Department/CN=acme.com"

if [[ ! -z "${IDP_ENTITYID}" ]]; then
echo "Using IDP entity ID ${IDP_ENTITYID}"
fi

docker run --name=simplesamlphp-idp -p 9443:8080 \
  -e SIMPLESAMLPHP_SP_ENTITY_ID="${SP_ENTITY_ID}" \
  -e SIMPLESAMLPHP_SP_ASSERTION_CONSUMER_SERVICE="${SP_ACS_SERVICE}" \
  -e SIMPLESAMLPHP_SP_SINGLE_LOGOUT_SERVICE="${SP_SLO_SERVICE}" \
  -e IDP_ENCRYPTION_CERTIFICATE="${IDP_ENCRYPTION_CERTIFICATE}" \
  -e IDP_SIGNING_CERTIFICATE="${IDP_SIGNING_CERTIFICATE}" \
  -e IDP_ENTITYID="${IDP_ENTITYID}" \
  -v /tmp/saml.crt:/var/www/simplesamlphp/cert/saml.crt \
  -v /tmp/saml.pem:/var/www/simplesamlphp/cert/saml.pem \
  -v $PWD/ci/tests/saml2/saml20-idp-remote.php:/var/www/simplesamlphp/metadata/saml20-idp-remote.php \
  -v $PWD/ci/tests/saml2/authsources.php:/var/www/simplesamlphp/config/authsources.php \
  -v $PWD/ci/tests/saml2/config.php:/var/www/simplesamlphp/config/config.php \
  -d kenchan0130/simplesamlphp
