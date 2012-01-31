/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.delimce.db;


import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.Properties;


/**
 *
 * @author Administrador
 */
public class Database {

    //////////////////////VARIABLES PARA LA CONEXION
//    private String dbserver = "10.16.44.139";
//    private String dbuser = "postgres";
//    private String dbpass = "12345";
//    
    private String dbserver = "localhost";
    private String dbuser = "postgres";
    private String dbpass = "delimce";
    
    private String dbname = "cip_corpoelec";
    private String dbport = "5432"; //CASO PG, oracle mssql
    private String dbms = "PGSQL";
    private String dbservice = "";
    //////////////////////////////////////////////////
    protected int nreg;
    protected long ultimoID;
    protected Connection dbc;
    private Statement stmt;
    public ResultSet result;
    private static int nconexiones; ////numero de conexiones existentes a la base de datos

    
    /**
     * lectura de los parametros de conexion de un archivo externo 
     * el archivo debe existir en la raiz del proyecto y ser .property, si no existe toma los parametros por defecto
     * 
     */
    
    public void leerConfig(String archivo) {

        try {
            //se crea una instancia a la clase Properties
            Properties propiedades = new Properties();
            //se leen el archivo .properties
            
            try{
                
             propiedades.load(new FileInputStream(archivo));
             
             
            }catch (NullPointerException e2){
                
                System.out.println("No es posible leer el archivo de propiedades de conexion \n " + e2);
                
            }
             
            //si el archivo de propiedades NO esta vacio retornan las propiedes leidas
            if (!propiedades.isEmpty()) {
                
                this.dbms = propiedades.getProperty("dbms").trim(); ///motor de base de datos a usar Mysql, Postgres u Oracle
                this.dbserver = propiedades.getProperty("dbserver").trim(); ////nombre o ip del servidor
                this.dbname = propiedades.getProperty("dbname").trim(); ///nombre de la base de datos
                this.dbuser = propiedades.getProperty("dbuser").trim(); ///usuario de la db
                this.dbpass = propiedades.getProperty("dbpass").trim(); ///password de la db
                
                  
                if(!propiedades.getProperty("dbport").trim().isEmpty())
                    this.dbport = propiedades.getProperty("dbport").trim(); ///puerto
                
                if(!propiedades.getProperty("dbservice").trim().isEmpty())
                    this.dbservice = propiedades.getProperty("dbservice").trim(); ///SID oracle
                
            } else {//sino  retornara NULL
                System.out.print("el archivo esta vacio: " + archivo);
            }
        } catch (IOException ex) {
            System.out.println("No es posible leer el archivo de propiedades de conexion " + archivo);
        }
    }

    
    
    /**
     * metodo que realiza la conexion a la base de datos
     * hasta la fecha la conexion es con mysql,oracle y por defecto postgres
     */
    
    public void conectar() {

        this.dbc = null;
        
   
        
        try {
            
            if (this.dbms.toLowerCase().equals("oracle")) ///oracle
            Class.forName ("oracle.jdbc.OracleDriver");
            else if(this.dbms.toLowerCase().equals("mysql")) ///mysql
            Class.forName("com.mysql.jdbc.Driver");
            else if(this.dbms.toLowerCase().equals("mssql")) ///sql server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            else ///postgresql
            Class.forName("org.postgresql.Driver");    
                
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, "No se encuentra el driver de conexion necesario: "+ex);
        }
        
        
        try {

            ////estableciendo conexion a la base de datos
            if (this.dbms.toLowerCase().equals("mysql")) {
                this.dbc = DriverManager.getConnection("jdbc:mysql://" + this.dbserver + "/" + this.dbname, this.dbuser, this.dbpass);
            } else if(this.dbms.toLowerCase().equals("oracle")) {
               
                this.dbc =  DriverManager.getConnection ("jdbc:oracle:oci8:@BDDESA","providencia","providencia");
             } else if(this.dbms.toLowerCase().equals("mssql")) {
                
                this.dbc = DriverManager.getConnection("jdbc:sqlserver://" + this.dbserver + ":" + this.dbport + ";databaseName=" + this.dbname + ";user=" + this.dbuser + ";password=" + this.dbpass);
                
            } else {
                this.dbc = DriverManager.getConnection("jdbc:postgresql://" + this.dbserver + ":" + this.dbport + "/" + this.dbname, this.dbuser, this.dbpass);
            }

            ///////////crea el objeto statement para realizar querys
            this.stmt = this.dbc.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
            //////setea las transacciones para que sean por sentencia
            this.dbc.setAutoCommit(true);


        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    /**
     * devuelve el numero de conexiones hechas con la clase Database
     * @return
     */

    public static int getNconexiones() {
        return nconexiones;
    }


    public void cerrar() {
        try {
            this.dbc.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    /** metodo que ejecuta un query T eje: insert, update delete ..
     *
     */
    public void tquery(String sql) throws SQLException {
      
         this.stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

        this.result = this.stmt.getGeneratedKeys();

        if (this.result != null && result.next()) {
            this.ultimoID = this.result.getLong(1);
        }


   

    }

    /** metodo que ejecuta un query sencillo ejemplo: select ..
     *
     */
    public void query(String sql) throws SQLException {
       
            this.stmt.executeQuery(sql);
            ///numero de registros
            this.result = this.stmt.getResultSet();
            this.result.last();
            this.nreg = this.result.getRow();

            this.result.beforeFirst();



     



    }



    /**
     * Metodo que ejecuta una consulta sql diferente a insert,select,update
     *
     */

    public void ejecutar(String sql) throws SQLException{
       
            this.stmt.execute(sql);
      

    }

    /**trae el numero de filas q devuelve el query
     *
     * @return
     */
    public int getNreg() {

        return this.nreg;
    }

    public long getUltimoID() {
        return ultimoID;
    }

    /**libera una conexion tanto stmt y resulset
     *
     */
    public void liberar() {

        try {
            this.result.close();

        } catch (SQLException e) {

            System.out.println("fallo liberando resultset");
            return;

        }


    }

    /** cierra un objeto de tipo statement
     * 
     */
    public void cerrars() {
        try {
            this.stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(SecureDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** cierra todos los elementos de una conexion
     *  result, stmt, dbc
     */
    public void cerrarTodo() {
        try {
            this.result.close();
            this.stmt.close();
            this.dbc.close();
        } /////////////////////////////////////////metodos para transacciones
        catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

/////////////////////////////////////////metodos para transacciones
/////abrir transaccion
    public void abrir_transaccion() {
        try {
            this.dbc.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

////pasa el parametro booleano confirmando o no la aplicar cambios o no
    public void cerrar_transaccion(boolean confirm) {

        try {
            if (confirm) {
                this.dbc.commit();
            } else {
                this.dbc.rollback();
            }
            
           this.dbc.setAutoCommit(true); ////setiando autocommit
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
