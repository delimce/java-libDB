/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.delimce.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author delimce
 * Clase para usar operaciones en la base de datos de modo seguro, hereda de Database.java
 */
public class SecureDB extends Database {

    protected PreparedStatement pstmt;

    /** query usado solo para INSERT, update y delete, devuelve el id del ult registro modificado
     *
     * @param query
     */
    public void prepararTSQL(String query) {

        try {

            this.pstmt = this.dbc.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ///trata de guardar el ultimo id insertado

        } catch (SQLException ex) {
            Logger.getLogger(SecureDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** usado para SELECT y operaciones que no devuelvan id
     *
     * @param query
     * @param select
     */
    public void prepararSQL(String query) {

        try {

            this.pstmt = this.dbc.prepareStatement(query,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);


        } catch (SQLException ex) {
            Logger.getLogger(SecureDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * metodos para setiar los valores
     *
     */
    public void setCadena(Integer pos, String Cadena) {
        try {
            this.pstmt.setString(pos, Cadena);
          } catch (SQLException ex) {
            Logger.getLogger(SecureDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    public void setTimestamp(Integer pos, Date fecha){
        try {
            this.pstmt.setTimestamp(pos, (fecha==null ? null : new java.sql.Timestamp(fecha.getTime())));
        } catch (SQLException ex) {
            Logger.getLogger(SecureDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setEntero(Integer pos, Integer Numero) {
        try {
            this.pstmt.setInt(pos, Numero);
        } catch (SQLException ex) {
            Logger.getLogger(SecureDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setLong(Integer pos, long Numero) {
        try {
            this.pstmt.setLong(pos, Numero);
        } catch (SQLException ex) {
            Logger.getLogger(SecureDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public void setFloat(Integer pos, float Numero) {
        try {
            this.pstmt.setFloat(pos, Numero);
        } catch (SQLException ex) {
            Logger.getLogger(SecureDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setDoble(Integer pos, double Numero) {
        try {
            this.pstmt.setDouble(pos, Numero);
        } catch (SQLException ex) {
            Logger.getLogger(SecureDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setFecha(Integer pos, Date Fecha) {
        try {
            //psInsertar.setDate(pos, (java.sql.Date) Fecha);
            if(Fecha==null)
            this.pstmt.setDate(pos, null); 
            else
            this.pstmt.setDate(pos, new java.sql.Date(Fecha.getTime()));
        } catch (SQLException ex) {
            Logger.getLogger(SecureDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void tquery() throws SQLException {
      
            this.pstmt.executeUpdate();
            this.result = this.pstmt.getGeneratedKeys();

            if (this.result != null && result.next()) {
                this.ultimoID = this.result.getLong(1);
            }


     
    }

    public void query() throws SQLException {

       
            this.pstmt.executeQuery();
            ///numero de registros
            this.result = this.pstmt.getResultSet();
            this.result.last();
            this.nreg = this.result.getRow();

            this.result.beforeFirst();


  

    }

    /** cierra un objeto de tipo Pstmt
     * 
     */
    
   public void cerrarp() {
        try {
            this.pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(SecureDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


 
}
