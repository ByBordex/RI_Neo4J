package logic;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.Consulta.Dificultad;

public class JdbcNeo4jConnector {

	String URL, user, password;
	public List<Consulta> consultasPreProgramas;
	
	public JdbcNeo4jConnector()
	{
		consultasPreProgramas = crearConsultasPreprogramadas();
	}
	
	public void setURL(String uRL) {
		URL = uRL;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	private List<Consulta> crearConsultasPreprogramadas()
	{
		List<Consulta> consultas = new ArrayList<Consulta>();
		
		consultas.add( new Consulta("Obtener personajes", Dificultad.BASICA, "match(n:Personaje) return n", "Obtener la lista completa de personajes") );
		consultas.add( new Consulta("Obtener paises", Dificultad.BASICA, "match(n:Pais) return n", "Obtener la lista completa de paises") );
		
		return consultas;
	}
	

	public Connection connect()
	{
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:neo4j:" + URL, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	//Consultas de ejemplo:
	//MATCH (n:Personaje) RETURN n
	//MATCH (n:Personaje) RETURN n.nombre_personaje
	
	public String premadeQuery(String query)
	{
		String result = "";
		try{
			Connection con = connect();;
			PreparedStatement st = con.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			
			ResultSetMetaData metadata = rs.getMetaData();
		    int columnCount = metadata.getColumnCount(); 
			while(rs.next())
			{  
			    for (int i = 1; i <= columnCount; i++) {
			        result += (metadata.getColumnName(i) + ", " + rs.getString(i) );      
			    }
			    result += "\n";
			}
		}catch(Exception e)
		{
			result = e.getMessage();
		}
		
		return result;
	}
	
	public void showPersonajes()
	{
		try{
			Connection con = connect();
			String query = "MATCH (n:Personaje) RETURN n.nombre_personaje";
			PreparedStatement st = con.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			
			while(rs.next())
			{
				System.out.println( rs.getString(1) );
			}
		}catch(Exception e)
		{
			
		}
	}

	
}
