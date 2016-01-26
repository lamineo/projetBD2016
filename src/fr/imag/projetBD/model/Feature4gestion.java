package fr.imag.projetBD.model;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.imag.projetBD.connexionBD.ConnexionBDD;
import fr.imag.projetBD.controller.LectureClavier;
import fr.imag.projetBD.launcher.TestZoo;
public class Feature4gestion {
	
	private int numImageAsupprimer;
	
	public Feature4gestion(){
	}
	
	public int supprimerImage(){
		int numImage;		
		numImage = LectureClavier.lireEntier("Entrer le numéro de l'image à supprimer pour des raisons juridiques: ");
		
		try {
			String reqLectureFichierImage = "select * from fichierimage where numimage="+numImage;
			Statement stmt1 = ConnexionBDD.CONNEXION.createStatement();
			ResultSet listeFichierImage = stmt1.executeQuery(reqLectureFichierImage);
			
			
			if(listeFichierImage.next()){
				Statement stmt2 = ConnexionBDD.CONNEXION.createStatement();
				String reqGetCommandes = "select numcommande from commande where STATUTCOMMANDE='en cours' AND numcommande in ( select numcommande as commandeLieeAImage from tirage where numalbum in (select numalbum from photo where numimage=4))";
				ResultSet commandeContenantImage = stmt2.executeQuery(reqGetCommandes);	
				
				while(commandeContenantImage.next()){
					int numeroCommande = commandeContenantImage.getInt(1);
					
					String reqUpdateCommandeToAnnulee = "update commande set statutcommande='annulee' where numcommande="+numeroCommande;
					Statement stmt3 = ConnexionBDD.CONNEXION.createStatement();
					stmt3.executeQuery(reqUpdateCommandeToAnnulee);
					System.out.println("la commande numéro "+numeroCommande+" a été annulée");
					
				}
				
				String reqDelImage = "DELETE FROM fichierimage WHERE numimage="+numImage;
				Statement stmt4 = ConnexionBDD.CONNEXION.createStatement();
				stmt4.executeQuery(reqDelImage);
				
				ConnexionBDD.CONNEXION.commit();
			}
			
			
			stmt1.close();
			listeFichierImage.close();

		} catch (SQLException err) {
			System.out.println(err.getMessage()); 
			
		}
		return 0;
	}
	
}
