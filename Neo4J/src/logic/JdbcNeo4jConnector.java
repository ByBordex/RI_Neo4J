package logic;
import java.sql.Array;
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
		consultas.add( new Consulta("Miembros nacidos en Novigrado", Dificultad.INTERMEDIA,"MATCH (c:Ciudad{name_ciudad:'Novigrado'})-[:BORN]-(p:Personaje)-[:PERTENECE]-(f:Faccion) WITH f, count(p) AS numero ORDER BY numero DESC RETURN f.nombre_faccion AS Faccion, numero", "Muestra el numero de miembros de una facción nacidos en Novigrado") );
		consultas.add(new Consulta("Personajes que no pertenecen a facción", Dificultad.INTERMEDIA, "MATCH (p:Personaje)-[:BORN]->(c:Ciudad)-[:CAPITAL_DE]->(pa:Pais)-[:FORMA]->(f:Faccion) WHERE NOT (p)-[:PERTENECE]-(f) RETURN p.nombre_personaje as Nombre", "Muestra el nombre de los personajes que nacieron en un país que formó una facción, a la cual dicho personaje no pertenece"));
		
		//Consultas avanzadas.
		consultas.add( new Consulta("Facciones que defienden en una batalla", Dificultad.AVANZADA, "MATCH (f:Faccion)-[p:TOMA_PARTE]->(a:Armisticio)<-[p1:TOMA_PARTE]-(f1:Faccion)<-[forma:FORMA]-(pais:Pais),(a:Armisticio)-[:TOMA_LUGAR_EN]->(pais:Pais), (f:Faccion)<-[fo:FORMA]-(pa:Pais) WHERE p.postura = 'Defensor' AND p1.postura = 'Atacante' RETURN DISTINCT f.nombre_faccion AS defensor,pa.nombre_pais AS paisesQueFormanFaccionDefensora,a.nombre_armisticio AS guerra,pais.nombre_pais AS paisDondeSeSituaGuerra,f1.nombre_faccion AS atacante,pais.nombre_pais AS paisQueFormaFaccionAtacante", 
							"Muestra el nombre de facciones defensivas en armisticio, los paises que pertenecen a la acción defensiva, el país en el que tiene lugar y la facción atacante junto con los países que la forman") );
		consultas.add(new Consulta("Gobernantes atacantes en armisticio", Dificultad.AVANZADA, "MATCH (p:Personaje)-[:GOBIERNA]->(pais:Pais)-[:FORMA]->(f:Faccion)-[parte:TOMA_PARTE]->(a:Armisticio) WHERE parte.postura='Atacante' RETURN DISTINCT p.nombre_personaje as Gobernante_atacante, a.nombre_armisticio as Guerra", "Muestra el nombre de los gobernantes de países atacantes en algún armisticio y el nombre de ese armisticio"));
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
			    	result += metadata.getColumnName(i) + ": ";
			    	//2003 es el tipo de los collect()
			    	if( metadata.getColumnType(i) == 2003 )
			    	{
			    		String[] array = ( String[] ) rs.getArray(i).getArray();
			    		for(int j = 0; j < array.length; j++)
			    		{
			    			result += array[j];
			    			if( j != columnCount)
					        	result +=  ", ";
			    		}
			    	}
			    	else
			    	{
			    		result += rs.getString(i);
			    	}
			    	
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
