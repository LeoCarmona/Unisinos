package condominio.model;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

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
@Table(name = "HOSPEDAGEM")
public class Hospedagem {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HOSPEDAGEM")
	@SequenceGenerator(name = "SEQ_HOSPEDAGEM", sequenceName = "S_HOSPEDAGEM", allocationSize = 1)
	private Long		id;

	@NotNull
	@Temporal(TIMESTAMP)
	@Column(name = "DATA_DE_ENTRADA", nullable = false)
	private Calendar	dataDeEntrada	= Calendar.getInstance();

	@Temporal(TIMESTAMP)
	@Column(name = "DATA_DE_SAIDA")
	private Calendar	dataDeSaida;

	@NotNull
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "HOSPEDE_ID")
	private Hospede		hospede;

	@NotNull
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "QUARTO_ID")
	private Quarto		quarto;

}
