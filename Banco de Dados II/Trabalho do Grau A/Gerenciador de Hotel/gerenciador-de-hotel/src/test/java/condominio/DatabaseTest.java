package condominio;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import condominio.model.Consumo;
import condominio.model.Hospedagem;
import condominio.model.Hospede;
import condominio.model.Quarto;

public class DatabaseTest {

	private EntityManager em;

	@Before
	public void setup() {
		em = Persistence.createEntityManagerFactory("bd2_persistence_unit").createEntityManager();
	}

	@Test
	public void saveTest() {
		mock();

		Hospede hospede = em.find(Hospede.class, 1L);

		assertEquals("Leonardo Carmona", hospede.getNome());
		assertEquals("Av. Unisinos", hospede.getRua());

		assertEquals(new Integer(950), hospede.getNumero());
		assertEquals("Cristo Rei", hospede.getBairro());
		assertEquals("São Leopoldo", hospede.getCidade());
		assertEquals("RS", hospede.getEstado());
		assertEquals(Integer.valueOf(51), hospede.getDdd());
		assertEquals(Integer.valueOf(35911112), hospede.getTelefone());

		Quarto quarto = em.find(Quarto.class, 1L);

		assertEquals("Quarto 1", quarto.getDescricao());
		assertEquals(new Double(50), quarto.getDiaria());

		Hospedagem hospedagem = em.find(Hospedagem.class, 1L);

		assertEquals(hospede, hospedagem.getHospede());
		assertEquals(quarto, hospedagem.getQuarto());

		Consumo consumo = em.find(Consumo.class, 1L);

		assertEquals("Coca-Cola Lata", consumo.getProduto());
		assertEquals(new Double(4.5), consumo.getPreco());
		assertEquals(new Integer(3), consumo.getQuantidade());
		assertEquals(hospedagem, consumo.getHospedagem());
	}

	@Test
	public void jsonAndXmlTest() throws JsonProcessingException, FileNotFoundException {
		mock();

		ObjectMapper jsonMapper = new ObjectMapper();
		XmlMapper xmlMapper = new XmlMapper();

		jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
		xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

		Hospede hospede = em.find(Hospede.class, 1L);
		Quarto quarto = em.find(Quarto.class, 1L);
		Hospedagem hospedagem = em.find(Hospedagem.class, 1L);
		Consumo consumo = em.find(Consumo.class, 1L);

		saveText("target/hospede.json", jsonMapper.writeValueAsString(hospede));
		saveText("target/hospede.xml", xmlMapper.writeValueAsString(hospede));

		saveText("target/quarto.json", jsonMapper.writeValueAsString(quarto));
		saveText("target/quarto.xml", xmlMapper.writeValueAsString(quarto));

		saveText("target/hospedagem.json", jsonMapper.writeValueAsString(hospedagem));
		saveText("target/hospedagem.xml", xmlMapper.writeValueAsString(hospedagem));

		saveText("target/consumo.json", jsonMapper.writeValueAsString(consumo));
		saveText("target/consumo.xml", xmlMapper.writeValueAsString(consumo));
	}

	private void saveText(String filename, String text) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(filename)) {
			out.println(text);
		}
	}

	private void mock() {
		em.getTransaction().begin();

		// Hospede

		Hospede hospede = new Hospede();

		hospede.setNome("Leonardo Carmona");
		hospede.setRua("Av. Unisinos");
		hospede.setNumero(950);
		hospede.setBairro("Cristo Rei");
		hospede.setCidade("São Leopoldo");
		hospede.setEstado("RS");
		hospede.setDdd(51);
		hospede.setTelefone(35911112);

		em.persist(hospede);

		// Quarto

		Quarto quarto = new Quarto();

		quarto.setDescricao("Quarto 1");
		quarto.setDiaria(50D);

		em.persist(quarto);

		// Hospedagem

		Hospedagem hospedagem = new Hospedagem();

		hospedagem.setHospede(hospede);
		hospedagem.setQuarto(quarto);

		em.persist(hospedagem);

		// Consumo

		Consumo consumo = new Consumo();

		consumo.setProduto("Coca-Cola Lata");
		consumo.setPreco(4.5);
		consumo.setQuantidade(3);
		consumo.setHospedagem(hospedagem);

		em.persist(consumo);

		em.getTransaction().commit();
	}

}
