/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package libdb;

import com.delimce.db.ObjetoDB;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */
public class pruebaDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Cambio");
        ObjetoDB db = new ObjetoDB();
            db.leerConfig("dbconfig.properties");
            db.conectar();
        
        try {
            
            
           
            db.prepararSQL("select * from usuario ");
            db.query();
            
            if(db.getNreg()>0){
            Map resultado = db.simple_db();
            System.out.println("resultado: "+resultado.get("NOMBRE").toString());
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(pruebaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        
        
        
        
        
    }
}
