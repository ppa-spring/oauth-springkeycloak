export const environment = {
  production: false,
  serverUrl: '/api',
  keycloak: {
    // provider
    issuer: 'http://localhost:8080/auth',
    // realm
    realm: 'extranet-realm',
    clientId: 'angular-front'
  }
}
