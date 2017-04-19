package condominio.model;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Leonardo Carmona da Silva
 *         <ul>
 *         <li><a href="https://br.linkedin.com/in/l3ocarmona">https://br.linkedin.com/in/l3ocarmona</a></li>
 *         <li><a href="mailto:lcdesenv@gmail.com">lcdesenv@gmail.com</a></li>
 *         </ul>
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "HOSPEDE")
@XmlType
public class Hospede {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HOSPEDE")
	@SequenceGenerator(name = "SEQ_HOSPEDE", sequenceName = "S_HOSPEDE", allocationSize = 1)
	private Long		id;

	@NotNull
	@Column(name = "NOME", nullable = false)
	private String		nome;

	@NotBlank
	@Column(name = "RUA", nullable = false)
	private String		rua;

	@NotNull
	@Min(value = 1L)
	@Column(name = "NUMERO", nullable = false)
	private Integer		numero;

	@NotBlank
	@Column(name = "BAIRRO", nullable = false)
	private String		bairro;

	@NotBlank
	@Column(name = "CIDADE", nullable = false)
	private String		cidade;

	@NotBlank
	@Size(min = 2, max = 2)
	@Column(name = "ESTADO", nullable = false, length = 2)
	private String		estado;

	@NotNull
	@Min(value = 1L)
	@Max(value = 99L)
	@Column(name = "DDD", nullable = false)
	private Integer		ddd;

	@NotBlank
	@Size(min = 10000000, max = 999999999)
	@Column(name = "TELEFONE", nullable = false)
	private Integer		telefone;

	@NotNull
	@Temporal(TIMESTAMP)
	@Column(name = "DATA_DE_REGISTRO", nullable = false)
	private Calendar	dataDeRegistro	= Calendar.getInstance();

}
