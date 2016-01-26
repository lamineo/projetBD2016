

package fr.imag.projetBD.launcher;
import fr.imag.projetBD.connexionBD.ConnexionBDD;
import fr.imag.projetBD.model.Feature4gestion;
public class ProjetBD {

	public static void main(String[] args) {
		
		ConnexionBDD conn = new ConnexionBDD("serradla","bd2015","jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima");

		conn.connectToServer();
		int f4 = new Feature4gestion().supprimerImage();
		

	}

}
