package selenium.generator.main.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * テンプレート出力情報
 * @author 7days
 */
public class PageBean {

    /** クラス名 */
    private String classNm;

    /** パッケージ名 */
    private String packageNm;

    /** 継承クラス名 */
    private String extendsClassNm;

    /** 項目リスト */
    private List<ExcelItemBean> itemList = new ArrayList<>();

    public String getClassNm() {
        return classNm;
    }

    public void setClassNm(String classNm) {
        this.classNm = classNm;
    }

    public String getPackageNm() {
        return packageNm;
    }

    public void setPackageNm(String packageNm) {
        this.packageNm = packageNm;
    }

    public String getExtendsClassNm() {
        return extendsClassNm;
    }

    public void setExtendsClassNm(String extendsClassNm) {
        this.extendsClassNm = extendsClassNm;
    }

    public List<ExcelItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ExcelItemBean> itemList) {
        this.itemList = itemList;
    }
}
