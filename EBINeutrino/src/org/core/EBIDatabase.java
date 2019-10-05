package org.core;

import org.core.database.DBCALLBACK;
import org.core.database.TABLE;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.interfaces.IEBIDatabase;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;


public class EBIDatabase implements IEBIDatabase {

    public Connection conn = null;
    public Statement stmt = null;
    private String connectionUrl = null;
    private String user = null;
    private String password = null;
    private String driver = null;
    public static boolean toUpperCase = true;
    private boolean isAutocommit = false;
    public static String INSERT = "INSERT";
    public static String UPDATE = "UPDATE";
    public static String DELETE = "DELETE";
    public static String SELECT = "SELECT";

    /**
     * connect to a database system
     *
     * @param driver
     * @param host
     * @param db
     * @param password
     * @param user
     * @param dbType
     * @param SID
     * @return
     */

    @Override
	public boolean connect(final String driver, final String host, String db, final String password, final String user, final String dbType, final String SID, final String toUpper) {
        try {
            this.user = user.trim();
            this.password = password.trim();
            this.driver = driver;
            Class.forName(this.driver);
            connectionUrl = null;

            if ("yes".equals(toUpper.toLowerCase())) {
                toUpperCase = true;
                db = db.toUpperCase();
            } else {
                db = db.toLowerCase();
            }

            if ("mysql".equals(dbType)) {
                connectionUrl = "jdbc:" + dbType + "://" + host + "/" + db.trim() + "?useUnicode=true&connectTimeout=0&socketTimeout=0"
                        + "&interactiveClient=true&reconnectAtTxEnd=true&autoReconnect=true&tcpKeepAlive=true"
                        + "&characterEncoding=utf8&jdbcCompliantTruncation=false&zeroDateTimeBehavior=round&serverTimezone=UTC";

            } else if ("oracle".equals(dbType)) {
                connectionUrl = "jdbc:" + dbType + ":thin:@" + host + ":" + SID.trim();
            } else {
                return false;
            }
            
            System.out.println(connectionUrl);

            conn = DriverManager.getConnection(connectionUrl, this.user, this.password);

        } catch (final SQLException ex) {
            ex.printStackTrace();
            EBISystem.logger.error("Error connection to the database", ex.fillInStackTrace());
            return false;
        } catch (final Exception ex) {
            ex.printStackTrace();
            EBISystem.logger.error("Error connection to the database", ex.fillInStackTrace());
            return false;
        }
        return true;
    }

    /**
     * reconnect to a database
     *
     * @return
     */
    private boolean reconnect() {
        try {
            Class.forName(this.driver).newInstance();
            conn = DriverManager.getConnection(connectionUrl, this.user, this.password);
            EBISystem.getInstance().setIEBIDatabase(this);

        } catch (final SQLException ex) {
            exceptionHandle(ex);
            EBISystem.logger.error("Error connection to the database", ex.fillInStackTrace());
            return false;
        } catch (final Exception ex) {
            ex.printStackTrace();
            EBISystem.logger.error("Error connection to the database", ex.fillInStackTrace());
            return false;
        }
        return true;
    }

    /**
     * Execute SQL Query
     *
     * @param query
     * @return SQL ResultSet
     */
    @Override
	public ResultSet execute(final String query) throws SQLException {
        ResultSet rs = null;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if (stmt.execute(query)) {
                rs = stmt.getResultSet();
            }

        } catch (final SQLException ex) {
            exceptionHandle(ex);
        }

        return rs;
    }

    /**
     * Execute SQL Query
     *
     * @param query
     * @return return true if query is executed or otherwise false
     */

    @Override
	public boolean exec(final String query) throws SQLException {
        boolean ret = true;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(query);
            stmt.close();
        } catch (final SQLException ex) {
            exceptionHandle(ex);
            ret = false;
        }

        return ret;
    }

    /**
     * Execute SQL Query
     *
     * @param query
     * @return String Exception as string otherwise empty string
     */

    @Override
	public void execExt(final String query) throws SQLException {
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(query);
            stmt.close();
        } catch (final SQLException ex) {
            exceptionHandle(ex);
        }
    }

    /**
     * SQL mapped functionality
     * @param Table
     * @param Type
     * @param fields Hashmap pair field value
     * @param where Hashmap pair field value
     * @param dbclb
     * @return void
     */

    @Override
	public TABLE ebiSQL(final String Table, final String Type, final HashMap<String, Object> fields, final HashMap<String, Object> where, final DBCALLBACK dbclb){

       TABLE table=null;

       try{
            //todo implement a query cache!
            final StringBuilder queryBuilder = new StringBuilder();
            boolean iterateField=false;
            switch(Type){

                case "INSERT":
                    queryBuilder.append(EBIDatabase.INSERT);
                    queryBuilder.append(" INTO ");
                    queryBuilder.append(Table);
                    queryBuilder.append(" ( ");
                    final String values= iterateFields(queryBuilder, fields);
                    queryBuilder.append(" ) ");
                    queryBuilder.append(" VALUES ( ");
                    queryBuilder.append(values);
                    queryBuilder.append(" ) ");
                    iterateField=true;
                break;

                case "UPDATE":
                    queryBuilder.append(EBIDatabase.UPDATE);
                    queryBuilder.append(" ");
                    queryBuilder.append(Table);
                    queryBuilder.append(" SET ");
                    iterateFieldsP(queryBuilder, fields);
                    if(where.size() >0){
                        queryBuilder.append(" WHERE ");
                        iterateFieldsP(queryBuilder, where);
                    }
                    iterateField=true;
                break;

                case "DELETE":
                    queryBuilder.append(EBIDatabase.DELETE);
                    queryBuilder.append(" FROM ");
                    queryBuilder.append(Table);
                    if(where.size() >0){
                        queryBuilder.append(" WHERE ");
                        iterateFieldsP(queryBuilder, where);
                    }
                break;

                case "SELECT":
                    queryBuilder.append(EBIDatabase.SELECT);
                    queryBuilder.append(" FROM ");
                    queryBuilder.append(Table);
                    iterateFields(queryBuilder, fields);
                break;

                default:
                    //exception :(=
                    throw new SQLException("Query type not valid");

            }

            table = new TABLE();
            final PreparedStatement ps = conn.prepareStatement(queryBuilder.toString(),
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, Statement.RETURN_GENERATED_KEYS);

            if(iterateField) {
                iterateValues(ps, fields);
            }

            if(where.size() >0){
                iterateValues(ps,where);
            }

            final ResultSet exRes = ps.executeQuery();
            exRes.beforeFirst();
            if(exRes.next()){
                iterateResult(exRes, table);
            }else if(iterateField){
                table.NEW_ROW(1).ADD_COLUMN(1,"INSERTED_KEY",ps.getGeneratedKeys().getLong(1));
            }

            if(dbclb != null){
                dbclb.callback(table);
            }

        }catch(final SQLException EX){
            exceptionHandle(EX);
        }

        return table;
    }


    /**
     * Iterate hashmap of fields return a string like FIELD=?,FIELD1=? .. ..
     * @param builder
     * @param flds
     * @return
     */
    private final void iterateFieldsP(final StringBuilder builder, final HashMap<String,Object> flds){
        int i=0;
        final int size = flds.size();
        final Iterator<String> fields = flds.keySet().iterator();

        while(fields.hasNext()){
            builder.append(fields.next());
            builder.append("=?");
            if(i<size-1){
                builder.append(",");
            }
            i++;
        }
    }


    /**
     * Iterate hashmap of fields return a string like ( ?,?,?,? )
     * @param builder
     * @param flds
     * @return
     */
    private final String iterateFields(final StringBuilder builder, final HashMap<String,Object> flds){
        int i=0;
        final int size = flds.size();
        final Iterator<String> fields = flds.keySet().iterator();
        String values="";
        while(fields.hasNext()){
            builder.append(fields.next());
            if(i<size-1){
                values+="?,";
                builder.append(",");
            }else{
                values+="?";
            }
            i++;
        }
        return values;
    }


    /**
     * Iterate hashmap of fields values
     * @param ps
     * @param flds
     * @return
     */
    private final void iterateValues(final PreparedStatement ps, final HashMap<String,Object> flds) throws SQLException{
        int i=0;
        final Iterator<String> fields = flds.keySet().iterator();
        while(fields.hasNext()){
            ps.setObject(i,flds.get(fields.next()));
            i++;
        }

    }

    /**
     * Iterate a ResultSet
     * @param set
     * @param table
     * @return
     */
    private final void iterateResult(final ResultSet set, final TABLE table) throws SQLException{
        int rowNr=0;
        set.beforeFirst();
        final ResultSetMetaData rsmd = set.getMetaData();
        final int cCnt = rsmd.getColumnCount();
        while(set.next()){
            table.NEW_ROW(rowNr);
            for(int i=0; i<cCnt; i++){
                final String colName = rsmd.getColumnName(i);
                table.ADD_COLUMN(rowNr, colName,set.getObject(colName));
            }
           rowNr++;
        }
    }



    /**
     * create new PreparedStatement
     *
     * @param query
     * @return Preparedstatement
     */
    @Override
	public PreparedStatement initPreparedStatement(final String query) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (final SQLException ex) {
            exceptionHandle(ex);
        }
        return ps;
    }

    @Override
	public boolean isValidConnection() {
        try {
            if (!conn.isClosed()) {
                return true;
            }
        } catch (final SQLException e) {
            exceptionHandle(e);
        }
        return false;
    }

    /**
     * Execute preparedstatement
     *
     * @param ps
     * @return generated key (id)
     */
    @Override
	public String executePreparedStmtGetKey(final PreparedStatement ps) {
        ResultSet key;
        String gkey = "";
        try {
            ps.execute();
            key = ps.getGeneratedKeys();
            key.next();
            gkey = key.getString(1);
            ps.close();
        } catch (final SQLException ex) {
            exceptionHandle(ex);
        }
        return gkey;
    }

    /**
     * Execute preparedstatement
     *
     * @param ps
     * @return return true if the preparedstatement is successfully executed
     */

    @Override
	public boolean executePreparedStmt(final PreparedStatement ps) {
        try {
            ps.execute();
            ps.close();
        } catch (final SQLException ex) {
            exceptionHandle(ex);
            return false;
        }

        return true;

    }

    /**
     * Execute a query with a PreperadStatment
     *
     * @param ps Preparedstatement
     * @return return ResultSet if successfully or null
     */
    @Override
	public ResultSet executePreparedQuery(final PreparedStatement ps) {
        ResultSet set = null;
        try {
            set = ps.executeQuery();
        } catch (final SQLException ex) {
            exceptionHandle(ex);
        }
        return set;
    }

    /**
     * Close a ResultSet
     *
     * @param set
     */
    @Override
	public void closeResultSet(final ResultSet set) {
        try {
            stmt.close();
            set.close();

        } catch (final SQLException ex) {
            exceptionHandle(ex);
        }
    }

    /**
     * Enable autocommit
     */
    @Override
	public void setAutoCommit(final boolean autocommit) {
        try {
            conn.setAutoCommit(autocommit);
            isAutocommit = autocommit;
        } catch (final SQLException ex) {
            exceptionHandle(ex);
        }
    }

    @Override
	public boolean isAutoCommit() {
        return isAutocommit;
    }

    /**
     * Return an active connection
     *
     * @return
     */
    @Override
	public Connection getActiveConnection() {
        return conn;
    }

    /**
     * Set an active database connection
     *
     * @param con
     */
    @Override
	public void setActiveConnection(final Connection con) {
        this.conn = con;
    }

    /**
     * Handle an Exception to reconnect
     *
     * @param ex
     */
    private void exceptionHandle(final SQLException ex) {

        String ext = "\nSQLException: " + ex.getMessage();
        ext += "\nSQLState: " + ex.getSQLState();
        ext += "\nVendorError: " + ex.getErrorCode();
        ext += "\nSQL: " + ex.getSQLState();
        ex.printStackTrace();
        EBIExceptionDialog.getInstance(ext).Show(EBIMessage.ERROR_MESSAGE);
    }

}