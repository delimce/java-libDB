/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.delimce.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author delimce
 */
public class ObjetoDB extends SecureDB {

    private ResultSetMetaData rsmd;

    /** Se trae un vector[] tipo: String con todos los registros del valor solicitado
     *
     * @return String[]
     */
    public ObjetoDB() {
    }

    ////constructor con conexion a la base de datos
    public ObjetoDB(String conexion) {

        this.conectar();

    }

    public String[] array_db() {

        String[] data = new String[this.nreg];

        int i = 0;
        try {

            this.rsmd = (ResultSetMetaData) this.result.getMetaData();

            while (this.result.next()) {
                data[i] = this.result.getString(this.rsmd.getColumnName(1));
                i++;
            }

            this.liberar(); ///cierra el objeto result (no puede volver a usarse)
            this.cerrarp();

        } catch (SQLException ex) {
            Logger.getLogger(ObjetoDB.class.getName()).log(Level.SEVERE, null, ex);
        }


        return data;

    }

    /** crea un vector asociativo con los elementos de un query de 1 solo registro como resultado
     *  @return Map
     */
    public Map simple_db() {

        Map data = new HashMap();

        try {

            this.result.next(); ///si se trajo al menos 1 registro
            this.rsmd = (ResultSetMetaData) this.result.getMetaData();
            for (int i = 0; i < this.rsmd.getColumnCount(); i++) {

                data.put(this.rsmd.getColumnName(i + 1), this.result.getString(this.rsmd.getColumnName(i + 1)));

            }

            this.liberar(); ///cierra el objeto result (no puede volver a usarse)
            this.cerrarp();

        } catch (SQLException ex) {
            Logger.getLogger(ObjetoDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!data.isEmpty()) {
            return data;
        } else {
            return null;
        }

    }

    /**
     * crea una matriz NxM con los resultados de una consulta
     * @return ArrayList 
     */
    public ArrayList estructura_db() {


        ArrayList lista = new ArrayList();

        try {
            this.rsmd = (ResultSetMetaData) this.result.getMetaData();


            if (this.getNreg() > 0) {

                while (this.result.next()) {

                    Map fila = new HashMap(); ///fila temp que guarda la data de cada registro

                    for (int i = 0; i < this.rsmd.getColumnCount(); i++) {

                        fila.put(this.rsmd.getColumnName(i + 1), this.result.getString(this.rsmd.getColumnName(i + 1)));

                    }

                    lista.add(fila); ///voy agregando fila por fila al arraylist
                    fila = null; //reseteo el objeto

                }

            } else {

                return null;
            }


        } catch (SQLException ex) {
            Logger.getLogger(ObjetoDB.class.getName()).log(Level.SEVERE, null, ex);
        }


        return lista;

    }
}
