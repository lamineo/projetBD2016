package fr.imag.projetBD.launcher;


import java.sql.*;

import fr.imag.projetBD.controller.*;
public class TestZoo {

	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima";

	static final String USER = "plattetl";
	static final String PASSWD = "bd2015";
	static Connection CONN;


	public static void main(String[] args) {

		initConnexion();		
		menuFonctionnel();

	}

	/**
	 * Connexion à la BDD
	 * 
	 * */
	public static void initConnexion() {

		// Enregistrement du driver Oracle

		try {
			System.out.print("Loading Oracle driver... "); 
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			// Chargement du pilote
			System.out.println("loaded");

			// Etablissement de la connection
			System.out.print("Connecting to the database... ");
			TestZoo.CONN = DriverManager.getConnection (CONN_URL,USER,PASSWD);
			// connexion
			System.out.println("connected");

			// Desactivation de l'autocommit
			TestZoo.CONN.setAutoCommit(false);			
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

	/**
	 * Affiche menu
	 * */
	public static void menu () {
		System.out.println("*** Choisir une action a effectuer : ***");
		System.out.println("0 : Quitter");
		System.out.println("1 : Ajouter un nouvel animal");
		System.out.println("2 : Deplacer un animal");
		System.out.println("3 : Affecter un gardien à une cage");
		System.out.println("4 : Modifier l'affectation d'un gardien");
		System.out.println("5 : Declarer une nouvelle maladie pour un animal ");
		System.out.println("6 : Rollback");		
		System.out.println("7 : Commit");	
		System.out.println("8 : setTransactionReadCommited" );
		System.out.println("9 : Ecriture sale" );	
		System.out.println("10 : lectureMaladie" );	
		System.out.println("11 : deadlock2" );	
		System.out.println("12 : setTransactionSerializable" );

	}

	public static void menuFonctionnel() {
		boolean exit = false;
		int action;

		while(!exit) {
			menu();
			action = LectureClavier.lireEntier("votre choix ?");
			switch(action) {
			case 0 : exit = true; break;
			case 1 : addAnimal(); break;
			case 2 : moveAnimal(); break;
			case 3 : affectationGardien(); break;
			case 4 : modifGardien(); break;
			case 5 : addMaladie(); break;
			case 6 : rollback(); break;
			case 7 : commit(); break;		
			case 8 : setTransactionReadCommited();break;
			case 9 : EcritureSale();break;
			case 10 : lectureMaladie();break;
			case 11 : deadlock2();break;
			case 12 : setTransactionSerializable();break;
			default : System.out.println("=> choix incorrect"); menu(); menuFonctionnel();
			}
		} 	    

	}

	public static void addAnimal(){
		String nomAnimal;
		String sexe;
		String typeAnimal;
		String fonctionCage;
		String pays;
		String annNaissance;
		int noCage;
		int nbMaladies;

		System.out.println("Nom de l'animal");
		nomAnimal = LectureClavier.lireChaine();
		System.out.println("Sexe :");
		sexe = LectureClavier.lireChaine();
		System.out.println("type :");
		typeAnimal = LectureClavier.lireChaine();
		System.out.println("fonction de la cage");
		fonctionCage = LectureClavier.lireChaine();
		System.out.println("pays : ");
		pays = LectureClavier.lireChaine();
		System.out.println("Année de naissance : ");
		annNaissance = LectureClavier.lireChaine();
		noCage = LectureClavier.lireEntier("numéro cage : ");
		nbMaladies = LectureClavier.lireEntier("nbMaladies :");

		try {

			String req = "insert into lesAnimaux values ('"+nomAnimal+"','"+sexe+"','"+typeAnimal+"','"+fonctionCage+"','"+pays+"','"+annNaissance+"','"+noCage+"','"+nbMaladies+"')";
			Statement stmt = TestZoo.CONN.createStatement(); // création du descripteur de requête
			ResultSet resultat = stmt.executeQuery(req);
			System.out.println("Animal ajouté !");
			stmt.close();
			resultat.close();
			// fermeture de la connexion

		} catch (SQLException err) { System.out.println(err.getMessage()); } 
		// Attention il faut capturer les exceptions !


	}

	public static void moveAnimal(){
		int noCage = LectureClavier.lireEntier("numéro cage : ");
		System.out.println("nom de l'animal :");
		String nomAnimal = LectureClavier.lireChaine();

		try {

			String req = "update lesAnimaux set noCage="+noCage+" where nomA='"+nomAnimal+"'";
			System.out.println(req);
			Statement stmt = TestZoo.CONN.createStatement(); // création du descripteur de requête
			ResultSet  resultat = stmt.executeQuery(req);
			System.out.println("Animal déplacé !");
			stmt.close();
			resultat.close();

			// fermeture de la connexion

		} catch (SQLException err) { System.out.println(err.getMessage()); } 
		// Attention il faut capturer les exceptions !
	}

	public static void affectationGardien(){
		String nomGardien;
		System.out.println("Entrez le nom du gardien :");
		nomGardien = LectureClavier.lireChaine();
		int noCage = LectureClavier.lireEntier("numéro cage : ");

		try {

			String req = "update lesGardiens set noCage="+noCage+" where nomE='"+nomGardien+"'";
			System.out.println(req);
			Statement stmt = TestZoo.CONN.createStatement(); // création du descripteur de requête
			ResultSet  resultat = stmt.executeQuery(req);
			System.out.println("Gardien affecté !");
			stmt.close();
			resultat.close();
		} catch (SQLException err) { System.out.println(err.getMessage()); }
	}

	public static void modifGardien(){
		String nomGardien;
		System.out.println("Entrez le nom du gardien :");
		nomGardien = LectureClavier.lireChaine();
		int noCage = LectureClavier.lireEntier("numéro cage : ");

		try {

			String req = "update lesGardiens set noCage="+noCage+" where nomE='"+nomGardien+"'";
			System.out.println(req);
			Statement stmt = TestZoo.CONN.createStatement(); // création du descripteur de requête
			ResultSet  resultat = stmt.executeQuery(req);
			System.out.println("Affectation gardien effectué !");
			stmt.close();
			resultat.close();
		} catch (SQLException err) { System.out.println(err.getMessage()); }
	}

	public static void addMaladie(){
		String nomAnimal;
		System.out.println("Entrez le nom de l'animal :");
		nomAnimal = LectureClavier.lireChaine();
		try {

			/* Récupération de la valeur de nb_maladies */ 
			int nb_maladies = -12;
			String req1 = "select nb_maladies from lesAnimaux where nomA='"+nomAnimal+"'";
			Statement stmt1 = TestZoo.CONN.createStatement();
			ResultSet resultat1 = stmt1.executeQuery(req1);
			if(resultat1.next()) {
				nb_maladies = resultat1.getInt("nb_maladies");
			}

			/* Incrémente de la valeur de nb_maladies */
			String req2 = "update lesAnimaux set nb_maladies="+(nb_maladies+1)+" where nomA='"+nomAnimal+"'";
			Statement stmt2 = TestZoo.CONN.createStatement();
			ResultSet resultat2 = stmt2.executeQuery(req2);	

			System.out.println("Une maladie ajoutée");

		} catch (SQLException err) { System.out.println(err.getMessage()); }
		//int nb_maladies = LectureClavier.lireEntier("nb maladies : ");
	}

	public static void rollback(){
		try {
			TestZoo.CONN.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void commit(){
		try {
			TestZoo.CONN.commit();
			System.out.println("Commit.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void EcritureSale(){
		try {

			/* Je lis */ 
			int nb_maladies = -12;
			String req1 = "select nb_maladies from lesAnimaux where nomA='Booby'";
			Statement stmt1 = TestZoo.CONN.createStatement();
			ResultSet resultat1 = stmt1.executeQuery(req1);
			if(resultat1.next()) {
				nb_maladies = resultat1.getInt("nb_maladies");
			}


			/* J'écris */
			String req2 = "update lesAnimaux set nb_maladies="+(nb_maladies+1);
			Statement stmt2 = TestZoo.CONN.createStatement();
			ResultSet resultat2 = stmt2.executeQuery(req2);	

			System.out.println("T1 executed ;-) ");

		} catch (SQLException err) { System.out.println(err.getMessage()); }
	}

	public static void lectureMaladie(){
		
		String nomAnimal;
		System.out.println("Entrez le nom de l'animal :");
		nomAnimal = LectureClavier.lireChaine();
		try {

			/* Récupération de la valeur de nb_maladies */ 
			int nb_maladies = -12;
			String req1 = "select nb_maladies from lesAnimaux where nomA='"+nomAnimal+"'";
			Statement stmt1 = TestZoo.CONN.createStatement();
			ResultSet resultat1 = stmt1.executeQuery(req1);
			if(resultat1.next()) {
				nb_maladies = resultat1.getInt("nb_maladies");
			}

			System.out.println("NombreMaladie : "+ nb_maladies);

		} catch (SQLException err) { System.out.println(err.getMessage()); }
		//int nb_maladies = LectureClavier.lireEntier("nb maladies : ");
	}
	

	public static void deadlock2(){
		try {

			/* J'écris Bobby */ 

			String reqUpdate1 = "update lesAnimaux set nb_maladies=10 where nomA='Bobby'";
			Statement stmt1 = TestZoo.CONN.createStatement();
			ResultSet resultat1 = stmt1.executeQuery(reqUpdate1);				

			System.out.println("Deadlock 2 excuted ;-) ");
		} catch (SQLException err) { System.out.println(err.getMessage()); }
	}


	public static void setTransactionReadCommited(){
		try {
			TestZoo.CONN.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			System.out.println("Isolation en place.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setTransactionSerializable(){
		try {
			TestZoo.CONN.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			System.out.println("Isolation Serializable en place.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getTransactionIsolation(){
		try {
			TestZoo.CONN.getTransactionIsolation();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}