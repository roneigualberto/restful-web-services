package com.in28minutes.rest.webservices;

import static com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter.filterOutAllExcept;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.http.converter.json.MappingJacksonValue;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class MappingJacksonValueBuilder {

	private Set<String> properties = new HashSet<>();

	private Object value;

	private String filterName;

	private MappingJacksonValueBuilder() {
	}
	
	
	public static MappingJacksonValueBuilder newMapping() {
		return new MappingJacksonValueBuilder();
	}

	public MappingJacksonValueBuilder property(String property) {

		this.properties.add(property);

		return this;
	}
	
	
	public <T> MappingJacksonValueBuilder value(T value) {

		return value(value, value.getClass());
	}

	public <T> MappingJacksonValueBuilder collection(Collection<T> value, Class<T> clazz) {

		return value(value, clazz);
	}

	private <T> MappingJacksonValueBuilder value(Object value, Class<T> clazz) {

		requireNonNull(value);

		JsonFilter annotation = clazz.getAnnotation(JsonFilter.class);

		if (annotation == null) {
			throw new IllegalArgumentException("Annotation @JsonFilter not present!");
		}

		this.filterName = annotation.value();

		this.value = value;

		return this;

	}

	public MappingJacksonValue build() {

		Objects.requireNonNull(this.value);

		SimpleBeanPropertyFilter filter = filterOutAllExcept(properties);

		FilterProvider filters = new SimpleFilterProvider().addFilter(filterName, filter);

		MappingJacksonValue mapping = new MappingJacksonValue(value);

		mapping.setFilters(filters);

		return mapping;
	}

}
