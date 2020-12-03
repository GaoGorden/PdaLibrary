package p.gordenyou.pdalibrary.listener;

import java.util.ArrayList;
import java.util.HashMap;

public interface JDBCHelperQueryListener {
    void success(ArrayList<HashMap<String, Object>> result, String queryFlag);

    void fail(String error);
}
