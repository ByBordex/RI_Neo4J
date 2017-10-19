package logic;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class JdbcNeo4jConnector {

	public static Connection connect()
	{
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:neo4j:http://127.0.0.1:7474", "neo4j", "g1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	//Consultas de ejemplo:
	//MATCH (n:Personaje) RETURN n
	//MATCH (n:Personaje) RETURN n.nombre_personaje
	
	public static String premadeQuery(String query)
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
	
	public static void showPersonajes()
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
