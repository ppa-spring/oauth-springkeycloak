package ppa.lab.securityservice.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {

	private JwtEncoder jwtEncoder;
	private AuthenticationManager authenticationManager;

	public AuthController(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager) {
		this.jwtEncoder = jwtEncoder;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/token")
	public Map<String, String> jwtToken(Authentication authentication) {
		return setJwtTokens(authentication, false);
	}

	@PostMapping("/token-authmanager")
	public Map<String, String> jwtToken(String username, String password, boolean withRefreshToken) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		return setJwtTokens(authentication, withRefreshToken);
	}

	private Map<String, String> setJwtTokens(Authentication authentication, boolean withRefreshToken) {
		Map<String, String> idToken = new HashMap<>();
		Instant instant = Instant.now();
		Instant expireAt = instant.plus(withRefreshToken ? 30 : 120, ChronoUnit.SECONDS);

		setJwtAccessToken(authentication, idToken, instant, expireAt);
		if (withRefreshToken) {
			setJwtRefreshToken(authentication, idToken, instant);
		}
		return idToken;
	}

	private void setJwtAccessToken(Authentication authentication, Map<String, String> idToken, Instant instant, Instant expireAt) {
		String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
		JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
				.subject(authentication.getName())
				.issuedAt(instant)
				.expiresAt(expireAt)
				.issuer("security-service")
				.claim("scope", scope)
				.build();

		String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
		idToken.put("accessToken", jwtAccessToken);
	}

	private void setJwtRefreshToken(Authentication authentication, Map<String, String> idToken, Instant instant) {
		JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
				.subject(authentication.getName())
				.issuedAt(instant)
				.expiresAt(instant.plus(5, ChronoUnit.MINUTES))
				.issuer("security-service")
				.build();

		String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
		idToken.put("refreshToken", jwtAccessToken);
	}
}
