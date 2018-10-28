package com.in28minutes.rest.webservices.versoning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonVersoningController {

	@GetMapping("v1/person")
	public PersonV1 personV1() {
		return new PersonV1("Bog Charlie");
	}

	@GetMapping("v2/person")
	public PersonV2 personV2() {
		return new PersonV2(new Name("Bog", "Charlie"));
	}

	@GetMapping(value = "/person/param", params = "version=1")
	public PersonV1 paramV1() {
		return new PersonV1("Bog Charlie");
	}

	@GetMapping(value = "/person/param", params = "version=2")
	public PersonV2 paramv2() {
		return new PersonV2(new Name("Bog", "Charlie"));
	}
	
	

	@GetMapping(value = "/person/header", headers = "X-VERSION-API=1")
	public PersonV1 headerV1() {
		return new PersonV1("Bog Charlie");
	}

	@GetMapping(value = "/person/header", headers = "X-VERSION-API=2")
	public PersonV2 headerV2() {
		return new PersonV2(new Name("Bog", "Charlie"));
	}
	
	@GetMapping(value = "/person/produces", produces = "application/vnd.company.app-v1+json")
	public PersonV1 producesV1() {
		return new PersonV1("Bog Charlie");
	}

	@GetMapping(value = "/person/produces", produces = "application/vnd.company.app-v2+json")
	public PersonV2 producesV2() {
		return new PersonV2(new Name("Bog", "Charlie"));
	}

}
