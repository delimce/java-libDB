/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.delimce.db;

import java.sql.SQLException;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author delimce
 */
public class HelperDAO extends Database {

    protected String EntityTable; ///atributo de tabla fisica a la entidad
    protected String procedureName; ////nombre del procedimiento (puede contener el pkg)
    protected String functionName; ////nombre de la funcion (puede contener el pkg)
    protected List<String> columns; ////conjunto de campos para operaciones crud
    protected List<String> columnsWhere; ///columnas para clausulas where
    protected List<Object> values; ////conjunto de valores de los campos
    protected List<Object> valuesWhere; ////conjunto de valores de los campos where

    public enum typeParam { ///tipo de parametro (IN: entrada, OUT: salida

        IN, OUT
    }

    public static enum sqlType {

        STRING, INTEGER, BIGINT, FLOAT, DOUBLE, DECIMAL
    };

    private List<Parameter> parameters; //////lista de parametros para llamar los stored

    public HelperDAO() {
    }

    public HelperDAO(String EntityTable) {
        this.setEntityTable(EntityTable);
        this.getConfig("dbconfig.properties");
        this.connect();
    }

    public String getEntityTable() {
        return EntityTable;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
        this.parameters = new ArrayList<>();
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
        this.parameters = new ArrayList<>();
    }

    /**
     * realiza el reseteo de las variables de columnas y valores
     */
    public void resetColumns() {

        this.columns = new ArrayList<>(); ///setiando los campos
        this.values = new ArrayList<>();
        this.columnsWhere = new ArrayList<>();
        this.valuesWhere = new ArrayList<>();
    }

    /**
     * hace el set de la tabla para realizar las operaciones crud sobre esta
     *
     * @param EntityTable
     */
    public void setEntityTable(String EntityTable) {
        this.EntityTable = EntityTable;
        resetColumns();

    }

    /**
     * Generate a String by Joining the Elements of Array, seperated By Glue
     *
     * @param glue The String to be placed between two array elements
     * @param Array The Array to be converted to String
     * @return String
     */
    public String Join(String glue, List Array) {
        String res = "";
        int i = 0;
        for (i = 0; i < Array.size() - 1; i++) {
            res = res + Array.get(i) + glue;
        }
        res = res + Array.get(i);
        return res;
    }

    /**
     * Generate a String of "repeat" repeted count times, seperated by glue
     *
     * @param glue The part which comes between the "repeat"
     * @param repeat The string to be repeted Count times
     * @param count The number of times "repeat" String is repeated
     * @return String Repeat(", ", "?", 3) = "?, ?" Repeat(", ", "Repeat", 3) =
     * "Repeat, Repeat, Repeat"
     */
    public String Repeat(String glue, String repeat, Integer count) {
        String res = "";
        int i = 0;
        for (i = 0; i < count - 1; i++) {
            res = res + repeat + glue;
        }
        res = res + repeat;
        return res;
    }

    /**
     * metodo que setea el nombre del campo y el valor (requerido para
     * insert,update,delete), recibe el nombre del campo en tabla y el valor
     * (debe ser objeto)
     *
     * @param name
     * @param obj
     */
    public void setColumn(String name, Object obj) {

        this.columns.add(name);
        this.values.add(obj);

    }

    public void setColumnWhere(String name, Object obj) {

        this.columnsWhere.add(name);
        this.valuesWhere.add(obj);

    }

    /**
     * hace el seting de la posicion del campo y el tipo de objeto a enviar
     * usado para operaciones CRUD
     *
     * @param obj
     * @param pos
     */
    protected void setObject(Object obj, int pos) {

        if (obj.getClass().equals(String.class)) {
            System.out.println("string");
            this.setString(pos, (String) obj);
        } else if (obj.getClass().equals(Integer.class)) {
            System.out.println("int");
            this.setInteger(pos, (Integer) obj);
        } else if (obj.getClass().equals(Short.class)) {
            System.out.println("short");
            this.setInteger(pos, (Integer) obj);
        } else if (obj.getClass().equals(Float.class)) {
            this.setFloat(pos, (Float) obj);
        } else if (obj.getClass().equals(Double.class)) {
            System.out.println("double");
            this.setDoble(pos, (Double) obj);
        }
    }

    /**
     * para setiar parametros de entrada de sp o funcion
     *
     * @param obj
     * @param pos
     */
    protected void setObjectParamIn(Object obj, int pos) {

        if (obj.getClass().equals(String.class)) {
            System.out.println("parametro entrada string");
            this.setStringInParam(pos, (String) obj);
        } else if (obj.getClass().equals(Integer.class)) {
            System.out.println("parametro entrada int");
            this.setIntegerInParam(pos, (Integer) obj);
        } else if (obj.getClass().equals(Short.class)) {
            System.out.println("parametro entrada short");
            this.setIntegerInParam(pos, (Integer) obj);
        } else if (obj.getClass().equals(Float.class)) {
            System.out.println("parametro entrada float");
            this.setFloatInParam(pos, (Float) obj);
        } else if (obj.getClass().equals(Double.class)) {
            System.out.println("parametro entrada double");
            this.setDobleInParam(pos, (Double) obj);
        }
    }

    protected void setObjectParamOut(sqlType type, int pos) {

        if (type == sqlType.STRING) {
            System.out.println("parametro salida string");
            this.setStringOutParam(pos);
        } else if (type == sqlType.INTEGER) {
            System.out.println("parametro salida int");
            this.setIntegerOutParam(pos);
        } else if (type == sqlType.FLOAT) {
            System.out.println("parametro salida float");
            this.setFloatOutParam(pos);
        } else if (type == sqlType.DOUBLE) {
            System.out.println("parametro salida double");
            this.setDobleOutParam(pos);
        }
    }

    /**
     * hace el seting del paso de parametros al sp o la funcion
     *
     * @param p
     */
    protected void setObject(Parameter p) {
        if (p.getType() == typeParam.IN) { //parametro de entrada
            this.setObjectParamIn(p.getValue(), p.getPosition());

        } else {
            this.setObjectParamOut(p.getTypeSql(), p.getPosition());

        }

    }

    /////metodos para CRUD
    public void dataInsert() {

        try {
            String tablename = this.getEntityTable();
            String sql = "insert into " + tablename + " (" + Join(", ", columns) + ") values (" + Repeat(", ", "?", columns.size()) + ")";
            System.out.println(sql);
            prepareTSQL(sql);

            for (int i = 0; i < columns.size(); i++) {
                setObject(values.get(i), i + 1);
            }

            tquery();
        } catch (SQLException ex) {
            Logger.getLogger(HelperDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closePrepare();
        }

    }

    public void delete() {
        try {
            if (columnsWhere.size() < 1) { ///debe haber un criterio al menos
                System.out.println("IMPOSIBLE APLICAR OPERACION SIN UN CRITERIO");
                return;
            }

            String tablename = this.getEntityTable();
            String sql = "delete from " + tablename + " where ";
            int i;
            for (i = 0; i < columnsWhere.size() - 1; i++) {
                sql = sql + columnsWhere.get(i) + "=? and "; ///todo: hacer que agrupe por and/or
            }
            sql = sql + columnsWhere.get(i) + "=?";
            prepareTSQL(sql);
            for (i = 0; i < columnsWhere.size(); i++) {
                setObject(valuesWhere.get(i), i + 1);
            }
            tquery();
        } catch (SQLException ex) {
            Logger.getLogger(HelperDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closePrepare();
        }

    }

    public void update() {

        try {
            if (columnsWhere.size() < 1) { ///debe haber un criterio al menos
                System.out.println("IMPOSIBLE APLICAR OPERACION SIN UN CRITERIO");
                return;
            }
            String tablename = this.getEntityTable();
            String sql = "update " + tablename + " set ";
            int i;
            for (i = 0; i < columns.size() - 1; i++) {
                sql += columns.get(i) + "=?,";
            }
            sql += columns.get(i) + "=? where ";
            for (i = 0; i < columnsWhere.size() - 1; i++) {
                sql += columnsWhere.get(i) + "=? and "; ///todo: hacer que agrupe por and/or
            }
            sql += columnsWhere.get(i) + "=?";

            System.out.println(sql);
            prepareTSQL(sql);
            /////mexclando vectores de valores para pasar los parametros
            List<List<Object>> mexcla = asList(this.values, this.valuesWhere);
            for (i = 0; i < mexcla.size(); i++) {
                System.out.println(mexcla.get(i));
                setObject(mexcla.get(i), i + 1);
            }
            tquery();
        } catch (SQLException ex) {
            Logger.getLogger(HelperDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closePrepare();
        }

    }

    /**
     * ejecucion de un storedProcedure (debe setiarse el nombre previamente y
     * colocar los parametros de entrada y salida correspondientes
     */
    public void executeProcedure() {

        String sql = "{call " + this.getProcedureName() + "(" + Repeat(", ", "?", this.parameters.size()) + ")}";
        this.prepareCall(sql);
        for (Parameter parameter : parameters) {
            setObject(parameter);
        }
        this.executeCall();

    }

    /**
     * metodo que setea un parametro de ENTRADA para un sp o funcion la llamada
     * de este metodo despues del setProcedureName determina la posicion de los
     * parametros, se asigna el valor del parametro debe ser de tipo Objeto
     *
     * @param value
     */
    public void setInParameter(Object value) {
        int pos = this.parameters.size() + 1;
        Parameter p = new Parameter();
        p.setPosition(pos);
        p.setType(typeParam.IN);
        p.setValue(value);

        this.parameters.add(p);///agregando a la lista de parametros para ejecutar el SP o funcion

    }

    /**
     * metodo que setea un parametro de ENTRADA para un sp o funcion la llamada
     * necesita la posicion del parametro en el sp o funcion y el valor
     *
     * @param pos
     * @param value
     */
    public void setInParameter(int pos, Object value) {
        Parameter p = new Parameter();
        p.setPosition(pos);
        p.setType(typeParam.IN);
        p.setValue(value);

        this.parameters.add(p);///agregando a la lista de parametros para ejecutar el SP o funcion

    }

    /**
     * metodo que setea un parametro de SALIDA para un sp o funcion la llamada
     * de este metodo despues del setProcedureName determina la posicion de los
     * parametros, se asigna el tipo de valor (java.sql.types)
     *
     * @param t
     */
    private void setOutParameter(sqlType t) {
        int pos = this.parameters.size() + 1;
        Parameter p = new Parameter();
        p.setPosition(pos);
        p.setType(typeParam.OUT);
        p.setTypeSql(t);
        this.parameters.add(p);///agregando a la lista de parametros para ejecutar el SP o funcion

    }

    public void setOutString() {
        setOutParameter(sqlType.STRING);
    }

    public void setOutInteger() {

        setOutParameter(sqlType.INTEGER);
    }

    public void setOutFloat() {

        setOutParameter(sqlType.FLOAT);
    }

    public void setOutDouble() {
        setOutParameter(sqlType.DOUBLE);
    }

    /////clase interna para el uso de parametros (procedimientos y funciones) 
    public class Parameter {

        private String name; ///parametro nombre
        private int position; ///posicion
        private typeParam type; ////tipo de parametro
        private Object value; ////valor
        private sqlType typeSql; ////tipos de parametros de salida (java.sql.Types)

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public typeParam getType() {
            return type;
        }

        public void setType(typeParam type) {
            this.type = type;
        }

        public sqlType getTypeSql() {
            return typeSql;
        }

        public void setTypeSql(sqlType typeSql) {
            this.typeSql = typeSql;
        }

    }

}
