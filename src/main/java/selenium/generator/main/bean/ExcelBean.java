package selenium.generator.main.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel情報
 * @author 7days
 */
public class ExcelBean {

    /** クラス名 */
    private String classNm;

    /** 項目リスト */
    private List<ExcelItemBean> itemList = new ArrayList<>();

    public String getClassNm() {
        return classNm;
    }

    public void setClassNm(String classNm) {
        this.classNm = classNm;
    }

    public List<ExcelItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ExcelItemBean> itemList) {
        this.itemList = itemList;
    }

}
