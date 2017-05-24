package example.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import example.data.Person;
import example.repository.PersonRepository;
import example.support.RestSupport;

/**
 * 
 * 
 * @author Leonardo Carmona da Silva
 *         <ul>
 *         <li><a href="https://br.linkedin.com/in/l3ocarmona">https://br.linkedin.com/in/l3ocarmona</a></li>
 *         <li><a href="mailto:lcdesenv@gmail.com">lcdesenv@gmail.com</a></li>
 *         </ul>
 *
 */
@RestController
@RequestMapping("/api")
public class PersonController {

	@Autowired
	private ObjectMapper		objectMapper;

	@Autowired
	private PersonRepository	repository;

	@RequestMapping(path = "/person", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> add(@RequestBody Person person) {
		return new ResponseEntity<>(repository.save(person), HttpStatus.CREATED);
	}

	@RequestMapping(path = "/person/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Person> edit(@PathVariable("id") Long id, HttpServletRequest request) throws JsonProcessingException, IOException {
		return new ResponseEntity<>((Person) RestSupport.update(repository, objectMapper, id, request), HttpStatus.OK);
	}

	@RequestMapping(path = "/person/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Cacheable(value = "getPerson", unless = "#result == null", condition = "")
	public Person get(@PathVariable("id") Long id) {
		return repository.findOne(id);
	}

}
