/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package libdb;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author luis
 */
public class PruebaStored1 {

    private static final String DB_CONN_URL
            = "jdbc:oracle:thin:@localhost:1521:ORA10G";
    private static final String DB_USER
            = "beta";
    private static final String DB_PASSWORD
            = "beta";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {

        Connection conn = null;
        CallableStatement st = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(DB_CONN_URL,
                    DB_USER, DB_PASSWORD);

//            st = conn.prepareCall("{call R_PRUEBA_BULK.r_prueba_ejecuta_prc(?, ?, ?, ?)}");
//            st.setString(1, "xxx");
//            st.setString(2, "hola mundo");
//            st.setInt(3,123);
//            st.registerOutParameter(4, Types.VARCHAR);
            ///funcion
            st = conn.prepareCall("{? = call R_PRUEBA_BULK.busca_texto(?, ?, ?)}");
            st.setString(2, "xxx");
            st.setString(3, "hola mundo");
            st.setInt(4, 123);

            st.registerOutParameter(1, Types.VARCHAR);
            st.execute();

            System.out.println("Received greeting from DB session "
                    + "mensaje  : " + st.getString(1));

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Test failed: " + e.getMessage());
        } finally {
            if (st != null) {
                st.close();
            }

        }

    }
}
