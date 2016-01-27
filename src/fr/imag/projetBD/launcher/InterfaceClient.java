
package fr.imag.projetBD.launcher;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import sun.awt.AWTAccessor.MenuAccessor;
import fr.imag.projetBD.connexionBD.ConnexionBDD;
import fr.imag.projetBD.controller.LectureClavier;

public class InterfaceClient {
	
	private static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima";
	private static final String USER = "serradla";
	private static final String PASSWD = "bd2015";
	private static Connection CONNECTION;
	private  String requete;
	private  Statement statement;
	private  ResultSet resultat;
	private String nomClient="";
	private Scanner scanner=new Scanner(System.in);
	
	public static void main(String[] args) {
		
		InterfaceClient ic = new InterfaceClient();
		ic.initConnexion();
		ic.menuConnexion();
		ic.menuFonctionnel();
	}
	
	/**
	 * Affiche menu
	 * */
	public void menuConnexion () {
		System.out.println("*** Choisir une action a effectuer : ***");
		System.out.println("0 : Quitter");
		System.out.println("1 : Se connecter");
	}
	
	public void menuSuite(){
		System.out.println("2 : Creer un album");
		System.out.println("3 : Uploader des photos personnelles");
		System.out.println("4 : Telecharger des fichiers images partagés ou photos personnelles");
		System.out.println("5 : Supprimer un Fichier Image");
		System.out.println("5 : Modifier un Fichier Image");
		System.out.println("6 : Consulter les informations d'un client");		
		System.out.println("7 : Saisir une commande");	
		System.out.println("8 : Générer un code promo" );
	}
	
	
	public void menuFonctionnel() {
		boolean exit = false;
		int action;
		while(!exit) {
			menuSuite();
			action = LectureClavier.lireEntier("votre choix ?");
			switch(action) {
			case 0 : exit = true; break;
			case 1 : creerClient(); break;
			case 2 : creerAlbum(); break;
			case 3 : uploaderPhotosPersonnelles();break;
			case 4 : associerPhotoAlbum();break;
			case 6 : afficheInfoClient();break;
//			case 4 : modifGardien(); break;
//			case 5 : addMaladie(); break;
//			case 6 : rollback(); break;
//			case 7 : commit(); break;		
//			case 8 : setTransactionReadCommited();break;
//			case 9 : EcritureSale();break;
//			case 10 : lectureMaladie();break;
//			case 11 : deadlock2();break;
//			case 12 : setTransactionSerializable();break;
			default : System.out.println("=> choix incorrect"); menuSuite(); menuFonctionnel();
			}
		} 	    

	}
	
	/**
	 * Affiche les infos relativent à un client
	 * Ses albums et contenus
	 * Ses photos
	 * Ses commandes et leur status
	 * Ses données perso avec les codes promos non utilisés
	 **/
	private void afficheInfoClient() {
		String numeroClient = this.getNumClient();
		this.requete = "select * from client where numclient="+numeroClient;
		try {
			this.executerTransaction();
			if(this.resultat.next()){				
				System.out.println("--------------Information relative aux clients--------------");
				System.out.println("Numero Client	|	Nom	|	Prenom	|	Mail		|		Adresse  ");				
				System.out.println("\t"+this.resultat.getInt(1)+"\t\t"+this.resultat.getString(3)+"\t\t"+
						this.resultat.getString(4)+"\t\t\t"+this.resultat.getString(2)+"\t\t\t"+this.resultat.getString(6));
				System.out.println("-------------- Liste Code promo --------------");
				this.requete = "select * from codepromo where numclient="+numeroClient+"and utilise=1";
				this.executerTransaction();
				if(this.resultat.next()){
					System.out.println("numero code promo "+this.resultat.getInt(1)+" avec une reduction de "+this.resultat.getFloat(3));
				
				}else{
					System.out.println("Aucun code promo");
					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
	}

	private void associerPhotoAlbum() {
		String numFichierImage;
		
		verifierConnection();
		numFichierImage=selectionnerFichiersImage();
		
		requete = "select count(numalbum) as nb from album where numclient in (select numclient from client where nom='"+nomClient+"')";
		try {
			executerTransaction();
			if(resultat.next()){
				String res = resultat.getString("nb");
				int n = Integer.parseInt(res);
				if (n ==0){
					System.out.println("Vous n'avez créé aucun aucun album");
					menuSuite();
				}
				else{
					String numAlbum = selectionnerAlbum();
					requete = "insert into Photo values('0','"+numAlbum+"','"+numFichierImage+"','40','titreParDefaut','CommentaireDefaut')";
					executerTransaction();
					commit();
					requete = "select numphoto from photo where numalbum='"+numAlbum+"' and numimage ='"+numFichierImage+"'";
					executerTransaction();
					if(resultat.next()){
						String numPhoto = resultat.getString("numphoto");
						trouverDestinationAlbum(numAlbum,numPhoto);
					}
				}
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void trouverDestinationAlbum(String numalbum,String numPhoto){
		/*check Calendrier*/
		requete="select count(numalbum) as nb from Calendrier where numclient in (select numClient from client where  nom='"+nomClient+"')";
		try {
			executerTransaction();
			if(resultat.next()){
				String res = resultat.getString("nb");
				if(Integer.parseInt(res) > 0){
					requete = "update Calendrier set numphoto='"+numPhoto+"' where numalbum='"+numalbum+"'";
					executerTransaction();
					commit();
					System.out.println("Photo ajoutée dans Calendrier");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*check Agenda*/
		requete="select count(numalbum) as nb from Agenda where numclient in (select numClient from client where  nom='"+nomClient+"')";
		try {
			executerTransaction();
			if(resultat.next()){
				String res = resultat.getString("nb");
				if(Integer.parseInt(res) > 0){
					requete = "update Agenda set numphoto='"+numPhoto+"' where numalbum='"+numalbum+"'";
					executerTransaction();
					commit();
					System.out.println("Photo ajoutée dans Agenda");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*check Livre*/
		requete="select count(numalbum) as nb from Livre where numclient in (select numClient from client where  nom='"+nomClient+"')";
		try {
			executerTransaction();
			if(resultat.next()){
				String res = resultat.getString("nb");
				if(Integer.parseInt(res) > 0){
					requete = "update Livre set numphoto='"+numPhoto+"' where numalbum='"+numalbum+"'";
					executerTransaction();
					commit();
					System.out.println("Photo ajoutée dans Livre");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void uploaderPhotosPersonnelles() {
		verifierConnection();
		System.out.println("Combien de photos souhaitez-vous uploader ?");
		int numPhotATelech = LectureClavier.lireEntier("un entier :");
		String numeroclient = getNumClient();
		for (int i=0;i<numPhotATelech;i++){
			Random r = new Random();
			int rand =r.nextInt()%100;
			String s = Integer.toString(rand);
			String chemin = "C : BDD/TEST/PHOTO"+s;
			requete = "insert into fichierimage values ('0','"+numeroclient+"','"+chemin+"',to_date(sysdate,'yyyy/mm/dd'),'1920','1')";
			try {
				executerTransaction();
				commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	public void verifierConnection(){
		if(nomClient == ""){
			System.out.println("Veuillez vous connectez avant !");
			menuConnexion();
		}
	}
	
	private String getNumClient(){
		String nClient="";
		requete = "select numclient from client where nom='"+nomClient+"'";
		try {
			executerTransaction();
				if(resultat.next())
				nClient=resultat.getString("numclient");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nClient;
	}

	private  void creerAlbum(){
		verifierConnection();
		String nClient="";
		String numFormat = "";String numalbum="";
		requete = "select numclient from client where nom='"+nomClient+"'";
		try {
			executerTransaction();
				if(resultat.next())
				nClient=resultat.getString("numclient");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Que souhaitez-vous créer ?");
		System.out.println("||1.un album");
		System.out.println("||2.un calendrier");
		System.out.println("||3.un agenda");System.out.println("||4.un livre");
		int choixAlbum = LectureClavier.lireEntier("--------------");
		System.out.println("Saisir un format :");
		afficherFormat();
		String choixFormat = LectureClavier.lireChaine();
		requete = " select distinct numformat from format where libelle = '"+choixFormat+"'";
		try {
			executerTransaction();
			if (resultat.next())
			numFormat = resultat.getString("numFormat");
		} catch (Exception e) {
			// TODO: handle exception
		}
		requete="insert into Album values('0','"+numFormat+"','"+nClient+"')";
		try {
			executerTransaction();
			commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//num album dont le client est proprietaire
		requete = "select numalbum from album where numclient= '"+nClient+"'";
		try {
			executerTransaction();
			if (resultat.next())
			numalbum = resultat.getString("numAlbum");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Erreur numAlbum");
		}
		switch(choixAlbum) {
		case 1:System.out.println("Album pour le client "+nomClient+" de numéro "+nClient+ " créé");
			commit();
			break;
		case 2:System.out.println("Veuillez renseigner un type de calendrier [Bureau] ou [Mural]");
				String typeCalend =  LectureClavier.lireChaine();
				requete = "insert into Calendrier values('"+numalbum+"',NULL,'"+numFormat+"','"+nClient+"','"+typeCalend+"')";
			try {
				executerTransaction();
				commit();
				System.out.println("Calendrier pour le client "+nomClient+" de numéro "+nClient+ " créé");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}break;
		case 3:System.out.println("Saisir type agenda : [52s] ou [365j]");
		String typeAgenda = LectureClavier.lireChaine(); 
			requete = "insert into Agenda values('"+numalbum+"','"+numFormat+"','"+nClient+"','"+typeAgenda+"')";
			try {
				executerTransaction();
				commit();
				System.out.println("Agenda pour le client "+nomClient+" de numéro "+nClient+ " créé");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:
		Random r = new Random();
		String s =Integer.toString(r.nextInt()%100);
		String postface = "postfacedefaut"+s;
		String preface = "prefacedefaut"+s;
		String titre = "titredefaut"+s;
			requete="insert into Livre values('"+numalbum+"',NULL,'"+numFormat+"','"+nClient+"','"+preface+"','"+postface+"','"+titre+"')";
			try {
				executerTransaction();
				commit();
				System.out.println("Livre pour le client "+nomClient+" de numéro "+nClient+ " créé");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:System.out.println("Cette commande n'existe pas, recommencez !");
				menuSuite();
			break;
		}
	}


	public void commit(){
		requete="commit";
		try {
			executerTransaction();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void creerClient(){
		String mail;
		String prenom;
		String motDePasse;
		String adresse;
		int choix;
		
		choix = LectureClavier.lireEntier("Créer un client : 0.automatique | 1.manuellement ?");
		switch (choix) {
		case 0:
			
			try {
				Random r = new Random();
				String s =Integer.toString(r.nextInt()%100);
				String sMail = "mailpardefault"+s+"@live.fr";
				nomClient="nomParDefaut"+s;
				String sPrenom = "prenomDefaut"+s;
				requete= "insert into client values ('0','"+sMail+"','"+nomClient+"','"+sPrenom+"','mdpDefaut','adresse','1')";
				executerTransaction();
				commit();
				System.out.println("Client "+nomClient+" créé!");
			} catch (Exception e) {	
				System.out.println(e.getMessage());
			}
			break;
		case 1:
			System.out.println("Votre mail :");
			mail = LectureClavier.lireChaine();
			mail.toLowerCase();
			System.out.println("Votre nom :");
			nomClient = LectureClavier.lireChaine();
			System.out.println("Votre prenom :");
			prenom = LectureClavier.lireChaine();
			System.out.println("Votre mot de passe :");
			motDePasse = LectureClavier.lireChaine();
			System.out.println("Votre adresse :");
			adresse = LectureClavier.lireChaine();
			
			try {
				requete = "insert into client values ('0','"+mail+"','"+nomClient+"','"+prenom+"','"+motDePasse+"','"+adresse+"','1')";
				executerTransaction();
				commit();
				System.out.println("Client "+nomClient+" créé!");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			break;
		default:
			System.out.println("[Mauvaise valeur saisie]");
			menuSuite();menuFonctionnel();
			break;
		}	
	}
	private void executerTransaction() throws SQLException {
			statement = CONNECTION.createStatement();
			resultat = statement.executeQuery(requete);
	}
	
	private String selectionnerAlbum(){
		Map<String, String> albums = new HashMap<String, String>();
		String requete;
		Statement statement;
		ResultSet resultat;
		String numalbum=null;
		try {
			statement = CONNECTION.createStatement();
			requete = "select numalbum,numformat from album where numclient in (select numclient from client where nom='"+nomClient+"')";
			resultat = statement.executeQuery(requete);
			System.out.println("Liste des numeros d'album:");
			while(resultat.next()) {
				albums.put(resultat.getString("numalbum"), resultat.getString("numformat"));
				System.out.println("N° Album :"+resultat.getString("numalbum")+": numero format : "+resultat.getString("numformat"));
			}
			if (albums.isEmpty())
				System.out.println("Aucun fichier image enregistré");
			else {
				System.out.println("Sélectionner un numero d'album :");
				numalbum=scanner.nextLine();

				while (!albums.containsKey(numalbum)) {
					System.out.println("Numero d'album non valide, recommencez.");
					numalbum=scanner.nextLine();
				}
			}
		} catch (SQLException err) { 
			System.out.println(err.getMessage()); 
		}
		return numalbum;
	}
	private String selectionnerFichiersImage() {
		Map<String, String> fichiersImage = new HashMap<String, String>();
		String requete;
		Statement statement;
		ResultSet resultat;
		String numFichierImage=null;
		try {
			statement = CONNECTION.createStatement();
			requete = "select f.NUMIMAGE,f.chemin from fichierimage f join client c on(f.NUMCLIENT=c.NUMCLIENT) where f.partage=1 or f.numclient in (select numclient from client where nom='"+nomClient+"')";
			resultat = statement.executeQuery(requete);
			System.out.println("Liste des fichiers images:\n");
			while(resultat.next()) {
				fichiersImage.put(resultat.getString("numImage"), resultat.getString("chemin"));
				System.out.println(resultat.getString("numImage")+": "+resultat.getString("chemin"));
			}
			if (fichiersImage.isEmpty())
				System.out.println("Aucun fichier image enregistré");
			else {
				System.out.println("Sélectionner un fichier image :");
				numFichierImage=scanner.nextLine();

				while (!fichiersImage.containsKey(numFichierImage)) {
					System.out.println("Fichier image non valide, recommencez.");
					numFichierImage=scanner.nextLine();
				}
			}
		} catch (SQLException err) { 
			System.out.println(err.getMessage()); 
		}
		return numFichierImage;
	}
	
	
	
	
	private void afficherFormat() {
		Map<String, String> formats = new HashMap<String, String>();
		requete = "select numFormat,libelle from format";
		try {
			executerTransaction();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		System.out.println("Liste des formats :\n");
		try {
			while(resultat.next()) {
				formats.put(resultat.getString("numFormat"), resultat.getString("libelle"));
				System.out.println(resultat.getString("numFormat")+": "+resultat.getString("libelle"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void initConnexion() {
		try {
			System.out.print("Loading Oracle driver... "); 
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			System.out.println("loaded");

			System.out.print("Connecting to the database... ");
			CONNECTION = DriverManager.getConnection (CONN_URL,USER,PASSWD);
			System.out.println("connected");
			CONNECTION.setAutoCommit(false);			
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

