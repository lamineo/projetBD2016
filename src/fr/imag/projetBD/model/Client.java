package fr.imag.projetBD.model;

public class Client {
	int numClient;
	String mail;
	String nom;
	String prenom;
	String motDePasse;
	String adresse;
	int actif;
	
	
	public Client(int numCient,String mail, String nom,String prenom,String mdp, String adr, int actif){
		this.numClient = numClient;
		this.mail = mail;
		this.nom=nom;
		this.prenom = prenom;
		this.motDePasse = mdp;
		this.adresse=adr;
		this.actif= 1;
	}
	
	public Client(){
		
	}
	
	
	
}
