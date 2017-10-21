package logic;

public class Consulta {

	private String nombreConsulta;
	private String query;
	private String descripcion;
	private Dificultad dificultad;
	
	public enum Dificultad {BASICA, INTERMEDIA, AVANZADA}
	
	public Consulta(String nombreConsulta, Dificultad dif ,String query, String descripcion) {
		super();
		this.dificultad = dif;
		this.nombreConsulta = nombreConsulta;
		this.query = query;
		this.descripcion = descripcion;
	}

	public String getNombreConsulta() {
		return nombreConsulta;
	}

	public String getQuery() {
		return query;
	}
	
	public Dificultad getDificultad() {
		return dificultad ;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
}
