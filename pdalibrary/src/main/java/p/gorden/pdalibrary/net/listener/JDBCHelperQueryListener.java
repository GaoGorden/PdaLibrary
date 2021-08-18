package p.gorden.pdalibrary.net.listener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface JDBCHelperQueryListener {
    void success(ArrayList<LinkedHashMap<String, Object>> result, String queryFlag);

    void fail(String errorMessage);
}
