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
		
		//Consultas básicas
		consultas.add( new Consulta("Miembros escuela del lobo", Dificultad.BASICA,"MATCH (m:EscuelaBrujos {nombre_escuela: 'Lobo'})<-[:ADIESTRADO_EN]-(a:Personaje) RETURN a.nombre_personaje as Nombre","Muestra los personajes que han sido adiestrados en la escuela del lobo.")  );
		consultas.add( new Consulta("Bestias de Oxenfurt", Dificultad.BASICA,"MATCH (m:Ciudad {name_ciudad: 'Oxenfurt'})<-[:HABITA_EN]-(a:Bestia) RETURN count(*) as NumeroBestias","Muestra las bestias que habitan en la ciudad de Oxenfurt.")  );
		consultas.add( new Consulta("Miembros por facción", Dificultad.BASICA,"MATCH (f:Faccion)<-[:PERTENECE]-(a:Personaje) RETURN f.nombre_faccion as Faccion, count(*) as Miembros","Muestra el número de personajes que componen cada facción.")  );
		
		//Consultas intermedias.
		consultas.add( new Consulta("Personajes nacidos en habitat de moira", Dificultad.INTERMEDIA,"MATCH (c:Ciudad)<-[:BORN]-(p:Personaje) WHERE (:Bestia {nombre_bestia:'Moira'})-[:HABITA_EN]->(c) RETURN DISTINCT p.nombre_personaje as Nombre, c.name_ciudad as Ciudad","Muestra el nombre delos personajes que han nacido en ciudades donde habita una moira.")  );
		consultas.add( new Consulta("Personajes no nacidos en capital.", Dificultad.INTERMEDIA,"MATCH (c:Ciudad)<-[:BORN]-(p:Personaje) WHERE NOT (:Pais)<-[:CAPITAL_DE]-(c) RETURN DISTINCT p.nombre_personaje as Nombre, c.name_ciudad as Ciudad","Muestra los personajes que no han nacido en la capital de un pais.")  );

		//Consultas avanzadas.
		
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
			    	
			        result += (metadata.getColumnName(i) + ": " + rs.getString(i));     
			        if( i != columnCount)
			        	result +=  ",  ";
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
