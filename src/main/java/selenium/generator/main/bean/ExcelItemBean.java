package selenium.generator.main.bean;

/**
 * ExcelItem情報
 * @author 7days
 */
public class ExcelItemBean {

    /** HTML */
    private String html;

    /* ------------------------------- */
    /** 項目名 */
    private String item;
    /** tag */
    private String tag;
    /** type */
    private String type;
    /** id */
    private String id;
    /** name */
    private String name;
    /** value */
    private String value;
    /** text */
    private String text;
    /** 件数 */
    private int count = 0;

    /* ------------------------------- */
    /** 選択方法 */
    private String findBy;
    /** 選択方法の値 */
    private String findByVal;
    /** 選択方法の取得数 */
    private String findByCnt;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFindBy() {
        return findBy;
    }

    public void setFindBy(String findBy) {
        this.findBy = findBy;
    }

    public String getFindByVal() {
        return findByVal;
    }

    public void setFindByVal(String findByVal) {
        this.findByVal = findByVal;
    }

    public String getFindByCnt() {
        return findByCnt;
    }

    public void setFindByCnt(String findByCnt) {
        this.findByCnt = findByCnt;
    }

}
