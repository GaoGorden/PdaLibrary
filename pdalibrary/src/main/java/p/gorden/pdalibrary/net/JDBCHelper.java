package p.gorden.pdalibrary.net;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import p.gorden.pdalibrary.net.listener.JDBCHelperExecuteListener;
import p.gorden.pdalibrary.net.listener.JDBCHelperExecuteListener2;
import p.gorden.pdalibrary.net.listener.JDBCHelperQueryListener;

public class JDBCHelper {
    public static String USERNAME = "sa";
    public static String PASSWORD = "yin0755!";
    private final static String CLASSNAME = "net.sourceforge.jtds.jdbc.Driver";
    public static String URL = "jdbc:jtds:sqlserver://39.99.34.177;DatabaseName=LianHe";

    public static final String NOT_MATCH_OPERATE_NUMBER = "操作行数不是预计行数";
    public static final String EXECUTE_ERROR = "执行有误";

    private static Connection con;
    private static JDBCHelper instance;

    //********所需参数***************
    private String[] params;

    private static int column;
    private static JDBCHelperExecuteListener executeListener;
    private static JDBCHelperExecuteListener2 executeListener2;

    private static String queryFlag;
    private static JDBCHelperQueryListener queryListener;
    //********所需参数***************

    /**
     * 获取操作（增加，删除，更新）实例
     *
     * @param params   sql语句
     * @param column   预计影响行数
     * @param listener 回调接口
     * @return JDBCHelper实例
     */
    public static JDBCHelper getExecuteHelper(String[] params, int column, JDBCHelperExecuteListener listener) {
        if (instance == null) {
            synchronized (JDBCHelper.class) {
                if (instance == null) {
                    instance = new JDBCHelper(params, column, null, null);
                }
            }

        } else {
            instance.setParams(params);
            instance.setColumn(column);
            instance.setExecuteListener(listener);
            instance.setExecuteListener2(null);
        }
        return instance;
    }

    public static JDBCHelper getExecuteHelper(String[] params, int column, JDBCHelperExecuteListener2 listener2) {
        if (instance == null) {
            synchronized (JDBCHelper.class) {
                if (instance == null) {
                    instance = new JDBCHelper(params, column, null, null);
                }
            }

        } else {
            instance.setParams(params);
            instance.setColumn(column);
            instance.setExecuteListener(null);
            instance.setExecuteListener2(listener2);
        }
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
    public static JDBCHelper getExecuteHelper(LinkedList<String> set_sql, int column, JDBCHelperExecuteListener listener) {
        String[] params = set_sql.toArray(new String[0]);
        if (instance == null) {
            synchronized (JDBCHelper.class) {
                if (instance == null) {
                    instance = new JDBCHelper(params, column, null, null);
                }
            }

        } else {
            instance.setParams(params);
            instance.setColumn(column);
        }
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
    public static JDBCHelper getQueryHelper(String[] params, String queryFlag, JDBCHelperQueryListener listener) {
        if (instance == null) {
            synchronized (JDBCHelper.class) {
                if (instance == null) {
                    instance = new JDBCHelper(params, 0, queryFlag, listener);
                }
            }

        } else {
            instance.setParams(params);
            instance.setQueryFlag(queryFlag);
        }
            instance.setQueryListener(listener);
        return instance;
    }

    private JDBCHelper(String[] params, int column, String queryFlag, JDBCHelperQueryListener queryListener) {
        this.params = params;
        JDBCHelper.column = column;
        JDBCHelper.queryFlag = queryFlag;
        JDBCHelper.queryListener = queryListener;
    }

    private void setParams(String[] params) {
        this.params = params;
    }

    private void setColumn(int column) {
        JDBCHelper.column = column;
    }

    private void setExecuteListener(JDBCHelperExecuteListener executeListener) {
        JDBCHelper.executeListener = executeListener;
    }

    private void setExecuteListener2(JDBCHelperExecuteListener2 executeListener2) {
        JDBCHelper.executeListener2 = executeListener2;
    }

    private void setQueryFlag(String queryFlag) {
        JDBCHelper.queryFlag = queryFlag;
    }

    private void setQueryListener(JDBCHelperQueryListener queryListener) {
        JDBCHelper.queryListener = queryListener;
    }

    public void sqlExecute() {
        new SQLServerExecute().execute(params);
    }

    public void sqlExecuteN() {
        new SQLServerExecuteN().execute(params);
    }

    public void sqlQuery() {
        new SQLServerQuery().execute(params);
    }

    private static class SQLServerExecute extends AsyncTask<String, Integer, Boolean> {
        boolean result = false;
        String errorMessage = "";
        int executeColumn = 0;

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                if (con == null || con.isClosed()) {
                    // 使用Class加载驱动程序
                    Class.forName(CLASSNAME);
                    //连接数据库
                    con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("加载驱动程序出错");
                errorMessage = e.getMessage();
                return false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                errorMessage = e.getMessage();
                return false;
            }

            try {
//                String sql = "Update TCcang set rong = 1.0 where cang = '备件仓'";
                //创建Statement,操作数据
                PreparedStatement state;
                if (con != null && params.length != 0) {
                    for (String param : params) {
                        state = con.prepareStatement(param);
                        if (state != null) {
                            //executeUpdate 返回的是影响的行数
                            con.setAutoCommit(false);
                            int testColumn = state.executeUpdate();
                            executeColumn = testColumn;
                            if (testColumn < 0) {
                                result = false;
                                errorMessage = NOT_MATCH_OPERATE_NUMBER;
                                con.rollback();
                                state.close();
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
                }
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                try {
                    con.close();
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
            super.onPostExecute(aVoid);
            if (executeListener != null) {
                executeListener.result(result, errorMessage);
            }

            if (executeListener2 != null) {
                executeListener2.result(result, errorMessage, executeColumn);
            }
        }
    }

    private static class SQLServerExecuteN extends AsyncTask<String, Integer, Boolean> {
        boolean result = false;
        String errorMessage = "";

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                if (con == null || con.isClosed()) {
                    // 使用Class加载驱动程序
                    Class.forName(CLASSNAME);
                    //连接数据库
                    con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("加载驱动程序出错");
                errorMessage = e.getMessage();
                return false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                errorMessage = e.getMessage();
                return false;
            }

            try {
//                String sql = "Update TCcang set rong = 1.0 where cang = '备件仓'";
                //创建Statement,操作数据
                PreparedStatement state;
                if (con != null && params.length != 0) {
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
                             */
                            if (state.executeUpdate() < 0) {
                                result = false;
                                errorMessage = EXECUTE_ERROR;
                                con.rollback();
                                state.close();
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
                }
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                try {
                    con.close();
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
            super.onPostExecute(aVoid);
            executeListener.result(result, errorMessage);
        }
    }

    private static class SQLServerQuery extends AsyncTask<String, Integer, Void> {
        PreparedStatement state = null;
        ArrayList<LinkedHashMap<String, Object>> list;
        String errorMessage = "";

        @Override
        protected Void doInBackground(String... params) {

            try {
                if (con == null || con.isClosed()) {
                    // 使用Class加载驱动程序
                    Class.forName(CLASSNAME);
                    //连接数据库
                    con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("加载驱动程序出错");
                errorMessage = e.getMessage();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                errorMessage = e.getMessage();
            }

            try {
//                String sql = "Select * from TCcang where wei = '113'";
                if (con != null && params.length != 0) {
                    state = con.prepareStatement(params[0]);
                    if (state != null) {
                        ResultSet resultData = state.executeQuery();
                        list = convertList(resultData);
//                        if (list.size() == 0)
//                            errorMessage = "经查询，没有数据";
                        state.close();
                    }
                    con.close();
                }
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (errorMessage.equals("")) {
                queryListener.success(list, queryFlag);
            } else {
                queryListener.fail(errorMessage);
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
