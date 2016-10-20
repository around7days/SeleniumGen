package selenium.generator.main.bean;

import java.util.StringJoiner;

/**
 * HTMLItem情報
 * @author 7days
 */
public class HtmlItemBean {

    /** HTML */
    private String html;

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

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
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

    /**
     * 件数に＋１する
     */
    public void addCount() {
        this.count++;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(",");
        sj.add("tag:" + tag);
        sj.add("type:" + type);
        sj.add("id:" + id);
        sj.add("name:" + name);
        sj.add("value:" + value);
        sj.add("text:" + text);
        return sj.toString();
    }

}
