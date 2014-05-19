/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package libdb;

import com.delimce.db.HelperDAO;
import java.sql.SQLException;

/**
 *
 * @author luis
 */

public class pruebaDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {

        HelperDAO hd = new HelperDAO("TST_AUTOMOVIL");
       
        
        ////********funcion
//        hd.setFunctionName("R_PRUEBA_BULK.busca_texto");    
//        hd.setOutString(); ////de primero el parametro de entrada
//        hd.setInParameter("epale ");
//        hd.setInParameter("funcionó");
//        hd.setInParameter(123);
//        hd.executeFunction();
//        ////*******stored 
//        hd.setProcedureName("R_PRUEBA_BULK.r_prueba_ejecuta_prc");
//        hd.setInParameter("hola");
//        hd.setInParameter("señor");
//        hd.setInParameter(123);
//        hd.setOutString();
//        hd.executeProcedure();
        
        
        
        hd.setColumn("modelo", "prueba2");
        hd.setColumnWhere("id", 8);
        hd.setColumnWhere("marca", "Volkswagen");
        hd.update();
        
        
        
        

    }
}
