package p.gorden.pdalibrary.net;

import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import p.gorden.pdalibrary.config.PdaConfig;
import p.gorden.pdalibrary.net.listener.JDBCHelperExecuteListener;
import p.gorden.pdalibrary.net.listener.JDBCHelperExecuteListener2;
import p.gorden.pdalibrary.net.listener.JDBCHelperExecuteListener3;
import p.gorden.pdalibrary.net.listener.JDBCHelperQueryListener;

public class JDBCHelper {

    private final static String CLASSNAME = "net.sourceforge.jtds.jdbc.Driver";

    public static final String NOT_MATCH_OPERATE_NUMBER = "操作行数不是预计行数";
    public static final String EXECUTE_ERROR = "操作行数有误";

    private static Connection con;
    private static JDBCHelper instance;
    private static String errorMessage;

    private static JDBCHelperExecuteListener executeListener;
    private static JDBCHelperExecuteListener2 executeListener2;
    private static JDBCHelperExecuteListener3 executeListener3;

    private static JDBCHelperQueryListener queryListener;

    private static AlertDialog dialog;


    // 连接实例
    static class JDBCConnect {
        private String[] params;

        private int column;

        private String flag;


        public String[] getParams() {
            return params;
        }

        public void setParams(String[] params) {
            this.params = params;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }

    private static final ArrayList<JDBCConnect> connectPools = new ArrayList<>();


    public static JDBCHelper getJDBCHelper(AlertDialog alertDialog) {
        if (instance == null) {
            synchronized (JDBCHelper.class) {
                if (instance == null) {
                    instance = new JDBCHelper();
                }
            }

        }
        dialog = alertDialog;
        return instance;
    }

    /**
     * 获取操作（增加，删除，更新）实例
     *
     * @param params   sql语句
     * @param column   预计影响行数
     * @param listener 回调接口
     * @return JDBCHelper实例
     */
    @Deprecated
    public static JDBCHelper getExecuteHelper(String[] params, int column, JDBCHelperExecuteListener listener) {
        if (instance == null) {
            synchronized (JDBCHelper.class) {
                if (instance == null) {
                    instance = new JDBCHelper();
                }
            }

        } else {
            JDBCConnect connect = new JDBCConnect();
            connect.setParams(params);
            connect.setColumn(column);
            connectPools.add(connect);

            instance.setExecuteListener(listener);
            instance.setExecuteListener2(null);
        }
        return instance;
    }

    @Deprecated
    public static JDBCHelper getExecuteHelper(String[] params, int column, JDBCHelperExecuteListener2 listener2) {
        if (instance == null) {
            synchronized (JDBCHelper.class) {
                if (instance == null) {
                    instance = new JDBCHelper();
                }
            }

        }
        JDBCConnect connect = new JDBCConnect();
        connect.setParams(params);
        connect.setColumn(column);
        connectPools.add(connect);

        instance.setExecuteListener(null);
        instance.setExecuteListener2(listener2);

        return instance;
    }

    /**
     * 获取操作（增加，删除，更新）实例
     *
     * @param set_sql  sql语句
     * @param column   预计影响行数
     * @param listener 回调接口
     * @return JDBCHelper实例
     */
    @Deprecated
    public static JDBCHelper getExecuteHelper(LinkedList<String> set_sql, int column, JDBCHelperExecuteListener listener) {
        String[] params = set_sql.toArray(new String[0]);
        if (instance == null) {
            synchronized (JDBCHelper.class) {
                if (instance == null) {
                    instance = new JDBCHelper();
                }
            }

        }
        JDBCConnect connect = new JDBCConnect();
        connect.setParams(params);
        connect.setColumn(column);
        connectPools.add(connect);

        instance.setExecuteListener(listener);
        return instance;
    }

    /**
     * 获取查询实例
     *
     * @param params    sql语句
     * @param queryFlag 查询标记
     * @param listener  回调接口
     * @return JDBCHelper实例
     */
    @Deprecated
    public static JDBCHelper getQueryHelper(String[] params, String queryFlag, JDBCHelperQueryListener listener) {
        if (instance == null) {
            synchronized (JDBCHelper.class) {
                if (instance == null) {
                    instance = new JDBCHelper();
                }
            }

        }

        JDBCConnect connect = new JDBCConnect();
        connect.setParams(params);
        connect.setFlag(queryFlag);
        connectPools.add(connect);

        instance.setQueryListener(listener);
        return instance;
    }

    private JDBCHelper() {
        getConnection();
        errorMessage = "";
    }

    private void getConnection() {
        try {
            if (con == null || con.isClosed()) {
                // 使用Class加载驱动程序
                Class.forName(CLASSNAME);
                //连接数据库
                con = DriverManager.getConnection(PdaConfig.getDatabaseUrl(), PdaConfig.SQLUSERNAME, PdaConfig.SQLPWS);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("加载驱动程序出错");
            errorMessage = e.getMessage();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorMessage = e.getMessage();
        }
    }

    @Deprecated
    public void setExecuteListener(JDBCHelperExecuteListener executeListener) {
        JDBCHelper.executeListener = executeListener;
    }

    @Deprecated
    public void setExecuteListener2(JDBCHelperExecuteListener2 executeListener2) {
        JDBCHelper.executeListener2 = executeListener2;
    }

    public void setExecuteListener3(JDBCHelperExecuteListener3 executeListener3) {
        JDBCHelper.executeListener3 = executeListener3;
    }


    public void setQueryListener(JDBCHelperQueryListener queryListener) {
        JDBCHelper.queryListener = queryListener;
    }

    public void addQuerySql(String queryFlag, String[] params) {
        JDBCConnect connect = new JDBCConnect();
        connect.setFlag(queryFlag);
        connect.setParams(params);
        connectPools.add(connect);
        sqlQuery();
    }

    public void addExecuteSql(int column, String[] params) {
        JDBCConnect connect = new JDBCConnect();
        connect.setColumn(column);
        connect.setParams(params);
        connectPools.add(connect);
        sqlExecute();
    }

    public void addExecuteSqlN(String executeFlag, String[] params) {
        JDBCConnect connect = new JDBCConnect();
        connect.setFlag(executeFlag);
        connect.setParams(params);
        connectPools.add(connect);
        sqlExecuteN();
    }

    public void sqlExecute() {
//        for (JDBCConnect connect : connectPools) {
//            new SQLServerExecute().execute(connect);
//        }

        Iterator<JDBCConnect> iterator = connectPools.iterator();
        if(iterator.hasNext()){
            new SQLServerExecute().execute(iterator.next());
            iterator.remove();
        }
    }

    public void sqlExecuteN() {
//        for (JDBCConnect connect : connectPools) {
//            new SQLServerExecuteN().execute(connect);
//        }

        Iterator<JDBCConnect> iterator = connectPools.iterator();
        if(iterator.hasNext()){
            new SQLServerExecuteN().execute(iterator.next());
            iterator.remove();
        }
    }

    public void sqlQuery() {
//        for (JDBCConnect connect : connectPools) {
//            new SQLServerQuery().execute(connect);
//        }

        Iterator<JDBCConnect> iterator = connectPools.iterator();
        if(iterator.hasNext()){
            new SQLServerQuery().execute(iterator.next());
            iterator.remove();
        }
    }

    public void release() {
        if (dialog != null) {
            dialog = null;
        }
    }

    private static class SQLServerExecute extends AsyncTask<JDBCConnect, Integer, Boolean> {
        boolean result = false;
        int executeColumn = 0;

        @Override
        protected void onPreExecute() {
            if (dialog != null) {
                dialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(JDBCConnect... connects) {


            JDBCConnect connect = connects[0];
            try {
                errorMessage = "";

                PreparedStatement state;
                if (con == null || con.isClosed()) {
                    instance.getConnection();
                }

                if(con == null){
                    return false;
                }

                String[] params = connect.params;
                for (String param : params) {
                    state = con.prepareStatement(param);
                    if (state != null) {
                        //executeUpdate 返回的是影响的行数
                        con.setAutoCommit(false);
                        int testColumn = state.executeUpdate();
                        executeColumn = testColumn;
                        if (testColumn != connect.column) {
                            result = false;
                            errorMessage = NOT_MATCH_OPERATE_NUMBER;
                            con.rollback();
                            state.close();

                            con.close();
                            connectPools.remove(connect);
                            return false;
                        }
                        state.close();
                    }
                }
                result = true;
                con.commit();
                con.setAutoCommit(true);

                //回收资源
                con.close();
                connectPools.remove(connect);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                try {
                    con.close();
                    connectPools.remove(connect);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                errorMessage = e.getMessage();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {

            if (dialog != null) {
                dialog.dismiss();
            }

            if (executeListener != null) {
                executeListener.result(result, errorMessage);
            }

            if (executeListener2 != null) {
                executeListener2.result(result, errorMessage, executeColumn);
            }

            if (executeListener == null && executeListener2 == null) {
                throw new IllegalArgumentException("请先设置执行回调setExecuteListener()！");
            }
        }
    }

    private static class SQLServerExecuteN extends AsyncTask<JDBCConnect, Integer, String> {
        boolean result = false;
        int executeColumn = 0;

        @Override
        protected void onPreExecute() {
            if (dialog != null) {
                dialog.show();
            }
        }

        @Override
        protected String doInBackground(JDBCConnect... connects) {

            JDBCConnect connect = connects[0];

            try {
                errorMessage = "";

                PreparedStatement state;
                if (con == null || con.isClosed()) {
                    instance.getConnection();
                }

                if(con == null){
                    result = false;
                    return connect.flag;
                }

                String[] params = connect.params;
                for (String param : params) {
                    state = con.prepareStatement(param);
                    if (state != null) {
                        //executeUpdate 返回的是影响的行数
                        con.setAutoCommit(false);
                            /*
                            用于执行给定 SQL 语句，该语句可能为 INSERT、UPDATE 或 DELETE 语句，或者不返回任何内容的 SQL                           语句（如 SQL DDL 语句）。
                            例如 ：CREATE TABLE 和 DROP TABLE。INSERT、UPDATE 或 DELETE 语句的效果是修改表中零行或                            多行中的一列或多列。

                            使用executeUpdate方法是因为在 createTableCoffees 中的 SQL 语句是 DDL （数据定义语言）语                            句。创建表，改变表，删除表都是 DDL 语句的例子，要用 executeUpdate 方法来执行。

                            也可以从它的名字里看出，方法 executeUpdate 也被用于执行更新表 SQL 语句。实际上，相对于创建表                            来说，executeUpdate 用于更新表的时间更多，因为表只需要创建一次，但经常被更新。

                            若没有影响任何数据： executeColumn = 0
                             */
                        if ((executeColumn = state.executeUpdate()) < 0) {
                            errorMessage = EXECUTE_ERROR;
                            con.rollback();

                            state.close();
                            con.close();
                            connectPools.remove(connect);

                            result = false;
                            return connect.flag;
                        }
                        state.close();
                    }
                }

                con.commit();
                con.setAutoCommit(true);

                //回收资源
                con.close();
                connectPools.remove(connect);

                result = true;
                return connect.flag;
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
                try {
                    con.close();
                    connectPools.remove(connect);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                result = false;
                return connect.flag;
            }
        }

        @Override
        protected void onPostExecute(String flag) {

            if (dialog != null) {
                dialog.dismiss();
            }

            if (executeListener != null) {
                executeListener.result(result, errorMessage);
            }

            if (executeListener2 != null) {
                executeListener2.result(result, errorMessage, executeColumn);
            }

            if (executeListener3 != null) {
                executeListener3.result(flag, result, errorMessage, executeColumn);
            }

            if (executeListener == null && executeListener2 == null && executeListener3 == null) {
                throw new IllegalArgumentException("请先设置执行回调setExecuteListener()！");
            }
        }
    }

    private static class SQLServerQuery extends AsyncTask<JDBCConnect, Integer, String> {
        ArrayList<LinkedHashMap<String, Object>> list = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            if (dialog != null) {
                dialog.show();
            }
        }

        @Override
        protected String doInBackground(JDBCConnect... connects) {

            JDBCConnect connect = connects[0];
            try {
//                String sql = "Select * from TCcang where wei = '113'";
                errorMessage = "";
                PreparedStatement state;
                if (con == null || con.isClosed()) {
                    instance.getConnection();
                }

                if(con == null){
                    return connect.flag;
                }

                String[] params = connect.params;
                state = con.prepareStatement(params[0]);
                list.clear();
                if (state != null) {
                    ResultSet resultData = state.executeQuery();
                    list = convertList(resultData);
//                        if (list.size() == 0)
//                            errorMessage = "经查询，没有数据";
                    state.close();
                }
                con.close();
                connectPools.remove(connect);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
                try {
                    con.close();
                    connectPools.remove(connect);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return connect.flag;
        }

        @Override
        protected void onPostExecute(String queryFlag) {

            if (dialog != null) {
                dialog.dismiss();
            }

            if (queryListener != null) {
                if (errorMessage.equals("")) {
                    queryListener.success(list, queryFlag);
                } else {
                    queryListener.fail(errorMessage);
                }
            } else {
                throw new IllegalArgumentException("请先设置查询回调setQueryListener()！");
            }
        }
    }

    /**
     * ResultSet 转化为ArrayList<HashMap<String, Object>>
     *
     * @param resultSet 数据源
     * @return ArrayList对象
     * @throws SQLException SQL操作异常
     */
    private static ArrayList<LinkedHashMap<String, Object>> convertList(ResultSet resultSet) throws SQLException {

        ArrayList<LinkedHashMap<String, Object>> list = new ArrayList<>();
        ResultSetMetaData md = resultSet.getMetaData();//获取键名
        int columnCount = md.getColumnCount();//获取行的数量
        while (resultSet.next()) {
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();//声明Map
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), resultSet.getObject(i));//获取键名及值
            }
            list.add(rowData);
        }
        return list;
    }
}
