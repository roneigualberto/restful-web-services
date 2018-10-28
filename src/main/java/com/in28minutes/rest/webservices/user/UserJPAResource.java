package com.in28minutes.rest.webservices.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserJPAResource {

	@Autowired
	private UserRepository service;

	@GetMapping("jpa/users")
	public List<User> retrieveAllUsers() {
		return service.findAll();
	}

	@GetMapping("jpa/users/{id}")
	public Resource<User> retrieveUser(@PathVariable int id) {
		User user = service.findById(id)
				.orElseThrow(() -> new UserNotFoundException("id-" + id));

		Resource<User> resource = new Resource<User>(user);

		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());

		resource.add(linkTo.withRel("all-users"));

		return resource;
	}

	@DeleteMapping("jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		service.deleteById(id);

	}

	@PostMapping("jpa/users")
	public ResponseEntity<Object> createUser(@RequestBody @Valid User user) {

		User savedUser = service.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location)
				.build();

	}

}
