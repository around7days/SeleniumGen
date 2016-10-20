package selenium.generator.main.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * HTML情報
 * @author 7days
 */
public class HtmlBean {

    /** ファイル名 */
    private String fileNm;

    /** 項目リスト */
    private List<HtmlItemBean> itemList = new ArrayList<>();

    public String getFileNm() {
        return fileNm;
    }

    public void setFileNm(String fileNm) {
        this.fileNm = fileNm;
    }

    public List<HtmlItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<HtmlItemBean> itemList) {
        this.itemList = itemList;
    }

}
