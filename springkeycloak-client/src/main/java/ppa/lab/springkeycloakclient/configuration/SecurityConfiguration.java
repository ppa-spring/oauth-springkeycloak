package ppa.lab.springkeycloakclient.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;

import java.util.function.Consumer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorize -> authorize
						.anyRequest().authenticated()
				)
				.oauth2Login(oauth2 -> oauth2
						.authorizationEndpoint(authorization -> authorization
								.authorizationRequestResolver(
										authorizationRequestResolver(this.clientRegistrationRepository)
								)
						)
				);
		return http.build();
	}

	private OAuth2AuthorizationRequestResolver authorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {

		DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
				new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
		authorizationRequestResolver.setAuthorizationRequestCustomizer(authorizationRequestCustomizer());

		return  authorizationRequestResolver;
	}

	private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
		return customizer -> customizer
				.additionalParameters(params -> params.put("cmroc", "oc4631"));
	}

//	@Bean
//	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
//		http
//				.oauth2Client()
//				.and()
//				.oauth2Login()
//				.tokenEndpoint()
//				.and()
//				.userInfoEndpoint();
//
//		http
//				.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
//
//		http
//				.authorizeHttpRequests()
//				.requestMatchers("/unauthenticated", "/oauth2/**", "/login/**").permitAll()
//				.anyRequest()
//				.fullyAuthenticated()
//				.and()
//				.logout()
//				.logoutSuccessUrl("http://localhost:8080/realms/T1SpringBootKeycloak/protocol/openid-connect/logout?redirect_uri=http://localhost:8081/");
//
//		return http.build();
//	}
}
