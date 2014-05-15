/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package libdb;

import com.delimce.db.HelperDAO;
import java.sql.SQLException;
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

         HelperDAO hd = new HelperDAO("TST_AUTOMOVIL");
        
        try {
           
            hd.setProcedureName("R_PRUEBA_BULK.r_prueba_ejecuta_prc"); ///nombre del stored
            hd.setInParameter("hola");
            hd.setInParameter("mundo");
            hd.setInParameter(123);
            hd.setOutString();
            hd.executeProcedure();
            hd.getString(4);

        } catch (SQLException ex) {
            Logger.getLogger(pruebaDB.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            hd.closeCall();
            hd.close();
        }

    }
}
