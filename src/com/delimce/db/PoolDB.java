/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.delimce.db;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Administrador
 */
public class PoolDB extends ObjetoDB {
    
    
    private DataSource ds; ///conexion obtenida del pool
    
    /**
     * Metodo que obtiene el recurso datasource creado en context.xml para el manejo del pool de conexiones.
     * @param datasource 
     */
    
    public PoolDB(String datasource) throws NamingException {

        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        this.ds = (DataSource) envContext.lookup(datasource);

    }
    
    /**
     * obtengo la variable de conexion del pool
     */
    
    @Override
    public void conectar(){
        try {
            this.dbc = this.ds.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(PoolDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
      
    
}
