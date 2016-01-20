

package fr.imag.projetBD.launcher;
import java.sql.ResultSet;
import java.sql.Statement;

import sun.awt.AWTAccessor.MenuAccessor;
import fr.imag.projetBD.connexionBD.ConnexionBDD;
import fr.imag.projetBD.controller.LectureClavier;
public class ProjetBD {

	public static void main(String[] args) {
		
		ConnexionBDD conn = new ConnexionBDD("ouarglin","bd2015","jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima");
		conn.connectToServer();
		menuAffichage();
		menuFonctionnel();
	}
	
	/**
	 * Affiche menu
	 * */
	public static void menuAffichage () {
		System.out.println("*** Choisir une action a effectuer : ***");
		System.out.println("0 : Quitter");
		System.out.println("1 : Creer un nouveau client");
		System.out.println("2 : Creer un album");
		System.out.println("3 : Creer un livre");
		System.out.println("4 : Creer un calendrier");
		System.out.println("5 : Supprimer un Fichier Image");
		System.out.println("5 : Modifier un Fichier Image");
		System.out.println("6 : Consulter les informations d'un client");		
		System.out.println("7 : Saisir une commande");	
		System.out.println("8 : Générer un code promo" );	
	}
	
	public static void menuFonctionnel() {
		boolean exit = false;
		int action;

		while(!exit) {
			menuAffichage();
			action = LectureClavier.lireEntier("votre choix ?");
			switch(action) {
			case 0 : exit = true; break;
			case 1 : creerClient(); break;
//			case 2 : moveAnimal(); break;
//			case 3 : affectationGardien(); break;
//			case 4 : modifGardien(); break;
//			case 5 : addMaladie(); break;
//			case 6 : rollback(); break;
//			case 7 : commit(); break;		
//			case 8 : setTransactionReadCommited();break;
//			case 9 : EcritureSale();break;
//			case 10 : lectureMaladie();break;
//			case 11 : deadlock2();break;
//			case 12 : setTransactionSerializable();break;
			default : System.out.println("=> choix incorrect"); menuAffichage(); menuFonctionnel();
			}
		} 	    

	}
	
	public static void creerClient(){
		int numClient;
		String mail;
		String nom;
		String prenom;
		String motDePasse;
		String adresse;
		int choix;
		
		choix = LectureClavier.lireEntier("Créer un client : 0.automatique | 1.manuellement ?");
		switch (choix) {
		case 0:
			
			try {
				String req = "insert into client values ('0','mailpardefault@live.fr','nomParDefaut','prenomDefaut','mdpDefaut','adresse','1')";
				Statement stmt = ConnexionBDD.CONNEXION.createStatement(); // crÃ©ation du descripteur de requÃªte
				ResultSet resultat = stmt.executeQuery(req);
				ConnexionBDD.CONNEXION.commit();
				System.out.println("Client créé !");
				stmt.close();
				resultat.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			break;
		case 1:
			System.out.println("Votre mail :");
			mail = LectureClavier.lireChaine();
			System.out.println("Votre nom :");
			nom = LectureClavier.lireChaine();
			System.out.println("Votre prenom :");
			prenom = LectureClavier.lireChaine();
			System.out.println("Votre mot de passe :");
			motDePasse = LectureClavier.lireChaine();
			System.out.println("Votre adresse :");
			adresse = LectureClavier.lireChaine();
			
			try {
				String req = "insert into client values ('0','"+mail+"','"+nom+"','"+prenom+"','"+motDePasse+"','"+adresse+"')";
				Statement stmt = ConnexionBDD.CONNEXION.createStatement(); // crÃ©ation du descripteur de requÃªte
				ResultSet resultat = stmt.executeQuery(req);
				System.out.println("Client créé!");
				stmt.close();
				resultat.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			break;
		default:
			System.out.println("[Mauvaise valeur saisie]");
			menuAffichage();menuFonctionnel();
			break;
			
		}	
		
	}
	
	

}
