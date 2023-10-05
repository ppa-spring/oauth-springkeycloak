package ppa.lab.securityservice.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TestController {

	@GetMapping("/datatest")
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	public Map<String, Object> dataTest(Authentication authentication) {
		return Map.of(
				"message", "datatest",
				"username", authentication.getName(),
				"authorities", authentication.getAuthorities());
	}

	@PostMapping(value = "/savedata", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public Map<String, Object> saveData(Authentication authentication, @RequestParam String data) {
		return Map.of(
				"saveData", data,
				"username", authentication.getName(),
				"authorities", authentication.getAuthorities());
	}
}
