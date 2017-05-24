package example.support;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.repository.CrudRepository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
public final class RestSupport {

	public static <T, ID extends Serializable, S extends T> S update(CrudRepository<T, ID> repository, ObjectMapper objectMapper, S entity, HttpServletRequest request) throws JsonProcessingException, IOException {
		S updatedEntity = objectMapper.readerForUpdating(entity).readValue(request.getReader());

		return repository.save(updatedEntity);
	}
	
	public static <T, ID extends Serializable, S extends T> S update(CrudRepository<T, ID> repository, ObjectMapper objectMapper, ID id, HttpServletRequest request) throws JsonProcessingException, IOException {
		T entity = repository.findOne(id);
		
		if (entity == null)
			return null;
		
		S updatedEntity = objectMapper.readerForUpdating(entity).readValue(request.getReader());

		return repository.save(updatedEntity);
	}

	public static <T> T toJson(Class<T> entity, HttpServletRequest request, ObjectMapper objectMapper) throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(request.getReader(), entity);
	}

	private RestSupport() {

	}

}
