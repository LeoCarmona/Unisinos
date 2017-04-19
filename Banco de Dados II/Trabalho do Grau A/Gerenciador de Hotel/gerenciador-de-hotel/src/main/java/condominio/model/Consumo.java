package condominio.model;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
@Table(name = "CONSUMO")
@XmlType
public class Consumo {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONSUMO")
	@SequenceGenerator(name = "SEQ_CONSUMO", sequenceName = "S_CONSUMO", allocationSize = 1)
	private Long		id;

	@NotBlank
	@Column(name = "PRODUTO", nullable = false)
	private String		produto;

	@NotNull
	@DecimalMin(value = "0")
	@Column(name = "PRECO", nullable = false)
	private Double		preco;

	@NotNull
	@Min(value = 1L)
	@Column(name = "QUANTIDADE", nullable = false)
	private Integer		quantidade;

	@NotNull
	@Column(name = "DATA_DO_CONSUMO", nullable = false)
	@Temporal(TIMESTAMP)
	private Calendar	dataDoConsumo	= Calendar.getInstance();

	@NotNull
	@JoinColumn(name = "HOSPEDAGEM_ID", nullable = false, updatable = false)
	@ManyToOne(optional = false)
	private Hospedagem	hospedagem;

}
