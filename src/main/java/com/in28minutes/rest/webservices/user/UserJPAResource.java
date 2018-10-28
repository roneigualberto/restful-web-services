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
	
	@Autowired
	private PostRepository postRepository;

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
	
	@GetMapping("jpa/users/{id}/posts")
	public List<Post> retrievePosts(@PathVariable int id) {
		
		Optional<User> userOptional = service.findById(id);
		
		if (!userOptional.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}
		
		return userOptional.get().getPosts();

	}
	
	@PostMapping("jpa/users/{id}/posts")
	public ResponseEntity<Object> createPost(@PathVariable int id,@RequestBody Post post) {
		
		Optional<User> userOptional = service.findById(id);
		
		if (!userOptional.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}
		
		User user = userOptional.get();
		
		post.setUser(user);
		
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(user.getId())
				.toUri();
		
		
		
		return ResponseEntity.created(location)
				.build();


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
