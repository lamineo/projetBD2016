package fr.imag.projetBD.connexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnexionBDD {
	
	public static Connection CONNEXION;
	
	private String user;
	private String mdp;
	private String adresseBDDServeur;

	
	public  ConnexionBDD(String user, String mdp, String adresseServeur){
		this.user = user;
		this.mdp = mdp;
		this.adresseBDDServeur = adresseServeur;
		
	}
	
	public void connectToServer(){

				try {
					System.out.print("Connexion à la base de donnée..."); 
					
					DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());					

					CONNEXION = DriverManager.getConnection (this.adresseBDDServeur,this.user,this.mdp);
					System.out.println("Connecté.");

					CONNEXION.setAutoCommit(false);			
					System.out.println("Autocommit disabled");
					
				} catch (SQLException e) {
					System.err.println("failed");
					System.out.println("Affichage de la pile d'erreur");
					e.printStackTrace(System.err);
					System.out.println("Affichage du message d'erreur");
					System.out.println(e.getMessage());
					System.out.println("Affichage du code d'erreur");
					System.out.println(e.getErrorCode());	
				} 
		
	}
}
