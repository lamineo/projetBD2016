

package fr.imag.projetBD.launcher;
import fr.imag.projetBD.connexionBD.ConnexionBDD;
public class ProjetBD {

	public static void main(String[] args) {
		
		ConnexionBDD conn = new ConnexionBDD("serradla","bd2015","jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima");

	}

}
