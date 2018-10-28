package com.in28minutes.rest.webservices.filter;


import static com.in28minutes.rest.webservices.MappingJacksonValueBuilder.newMapping;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.in28minutes.rest.webservices.MappingJacksonValueBuilder;

@RestController
public class FilteringController {

	@GetMapping("/filtering")
	public MappingJacksonValue retrieveSomeBean() {

		SomeBean someBean = new SomeBean("v1", "v2", "v3");

		return  newMapping()
				.value(someBean)
				.property("field1")
				.property("field2")
				.build();
	}

	@GetMapping("/filtering-list")
	public MappingJacksonValue retrieveSomeBeanList() {

		List<SomeBean> list = Arrays.asList(new SomeBean("v1", "v2", "v3"));

		return   newMapping()
				.collection(list, SomeBean.class)
				.property("field2")
				.property("field3")
				.build();
	}

	public MappingJacksonValue filter(String filterName, Object value, String... properties) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(properties);

		FilterProvider filters = new SimpleFilterProvider().addFilter(filterName, filter);

		MappingJacksonValue mapping = new MappingJacksonValue(value);

		mapping.setFilters(filters);

		return mapping;
	}

}
