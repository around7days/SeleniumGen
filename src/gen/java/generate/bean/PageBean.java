package generate.bean;

import java.util.ArrayList;
import java.util.List;

import generate.com.GenerateUtils;

/**
 * ページ情報
 * @author 7days
 */
public class PageBean {

    /** ページ名称 */
    private String name;

    /** パッケージ */
    private String packageNm;

    /** 項目リスト */
    private List<ItemBean> itemList = new ArrayList<>();

    /**
     * ページ名称を取得します。
     * @return ページ名称
     */
    public String getName() {
        return name;
    }

    /**
     * ページ名称を取得します。<br>
     * キャメルケース（先頭大文字）
     * @return ページ名称
     */
    public String getNameToUpperCamel() {
        return GenerateUtils.camelCaseUpper(name);
    }

    /**
     * ページ名称を設定します。
     * @param name ページ名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * パッケージを取得します。
     * @return パッケージ
     */
    public String getPackageNm() {
        return packageNm;
    }

    /**
     * パッケージを設定します。
     * @param packageNm パッケージ
     */
    public void setPackageNm(String packageNm) {
        this.packageNm = packageNm;
    }

    /**
     * 項目リストを取得します。
     * @return 項目リスト
     */
    public List<ItemBean> getItemList() {
        return itemList;
    }

    /**
     * 項目リストを設定します。
     * @param itemList 項目リスト
     */
    public void setItemList(List<ItemBean> itemList) {
        this.itemList = itemList;
    }

}
