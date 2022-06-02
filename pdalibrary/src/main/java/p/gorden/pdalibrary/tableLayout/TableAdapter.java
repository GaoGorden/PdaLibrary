package p.gorden.pdalibrary.tableLayout;

/**
 * Created by Smartown on 2017/7/19.
 */
public interface TableAdapter {

    int getColumnCount();

    String[] getColumnContent(int position);

    String[] getHeader();

    int getPage();

}
