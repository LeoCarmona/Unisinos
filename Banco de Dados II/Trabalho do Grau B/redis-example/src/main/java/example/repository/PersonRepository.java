package example.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import example.data.Person;

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
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

}
