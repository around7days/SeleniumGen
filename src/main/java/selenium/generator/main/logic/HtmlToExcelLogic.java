package selenium.generator.main.logic;

import static selenium.generator.common.Const.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import selenium.generator.common.Const.FindBy;
import selenium.generator.common.Const.HtmlTag;
import selenium.generator.common.Const.ItemAttr;
import selenium.generator.common.Const.ItemAttrType;
import selenium.generator.common.GeneratePropertyManager;
import selenium.generator.common.GenerateUtils;
import selenium.generator.main.bean.ExcelBean;
import selenium.generator.main.bean.ExcelItemBean;
import selenium.generator.main.bean.HtmlBean;
import selenium.generator.main.bean.HtmlItemBean;

public class HtmlToExcelLogic {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(HtmlToExcelLogic.class);

    /** プロパティ（generate） */
    private static final GeneratePropertyManager prop = GeneratePropertyManager.INSTANCE;

    /** 読み込むHTMLタグ情報 */
    private static StringJoiner htmlTags = new StringJoiner(",");
    static {
        for (HtmlTag htmltag : HtmlTag.values()) {
            htmlTags.add(htmltag.name());
        }
    }

    /**
     * HTMLファイルパス一覧の取得
     * @return HTMLファイルパス一覧
     * @throws IOException
     */
    public List<Path> findFile() throws IOException {

        Path dir = Paths.get(prop.getString("html.input.file.dir"));
        String fileNmRegex = prop.getString("html.input.file.name.regex");

        return GenerateUtils.getFileList(dir, fileNmRegex);
    }

    /**
     * HTMLページ解析
     * @param path
     * @return HtmlBean
     * @throws IOException
     */
    public HtmlBean analyze(Path path) throws IOException {

        // HTMLドキュメントの生成
        Document document = Jsoup.parse(path.toFile(), prop.getString("html.encoding"));

        // HtmlBeanの生成
        HtmlBean htmlBean = new HtmlBean();

        // ファイル名の取得
        htmlBean.setFileNm(GenerateUtils.getFileNm(path));

        // HTML項目情報リストの取得
        List<HtmlItemBean> itemList = analyzeHtmlTag(document, htmlTags.toString());
        htmlBean.setItemList(itemList);

        return htmlBean;
    }

    /**
     * Excel情報の生成
     * @param htmlBean
     * @return excelBean
     * @throws ReflectiveOperationException
     */
    public ExcelBean createExcelBean(HtmlBean htmlBean) throws ReflectiveOperationException {

        // Excel情報の生成
        ExcelBean excelBean = new ExcelBean();
        excelBean.setClassNm(htmlBean.getFileNm());

        for (HtmlItemBean htmlItemBean : htmlBean.getItemList()) {
            ExcelItemBean excelItemBean = new ExcelItemBean();

            // Bean情報をコピー
            BeanUtils.copyProperties(excelItemBean, htmlItemBean);

            // FindBy情報を生成
            makeFindByInfo(excelItemBean);
            // Operate情報を生成
            makeOperateInfo(excelItemBean);

            // ExcelBeanに格納
            excelBean.getItemList().add(excelItemBean);
        }

        return excelBean;
    }

    /**
     * テンプレート生成
     * @param excelBean
     * @throws IOException
     */
    public void generate(ExcelBean excelBean) throws IOException {

        // テンプレートファイルパスの取得
        Path templateFilePath = GenerateUtils.getPath(prop.getString("excel.template.file"));

        // 出力先パスの取得
        String fileNm = excelBean.getClassNm() + ".xlsx";
        Path outPutFilePath = Paths.get(prop.getString("excel.output.file.dir"), fileNm);

        // コピーしたテンプレートファイルに値を反映
        try (FileInputStream fis = new FileInputStream(templateFilePath.toFile());
             Workbook workbook = new XSSFWorkbook(fis)) {
            // 先にcloseしておく(重要)
            fis.close();

            /*
             * Mainシートの設定
             */
            Sheet sheet = workbook.getSheet(prop.getString("excel.sheet.name"));

            // 値反映行を取得
            int targetRow = prop.getInteger("excel.start.row") - 1;
            // 項目情報単位で設定
            for (int i = 0; i < EXCEL_DETAIL_MAX; i++) {
                // 項目情報の取り出し
                ExcelItemBean itemBean;
                if (excelBean.getItemList().size() > i) {
                    itemBean = excelBean.getItemList().get(i);
                } else {
                    itemBean = new ExcelItemBean(); // 空情報でExcelの項目を上書きする為に使用
                }

                // 出力列情報の取得
                Row row = sheet.getRow(targetRow);

                // HTML画面項目
                setCellValue(row, "excel.item.col", itemBean.getItem()); // 項目名
                setCellValue(row, "excel.item.tag.col", itemBean.getTag()); // tag
                setCellValue(row, "excel.item.input.type.col", itemBean.getType()); // type
                setCellValue(row, "excel.item.id.col", itemBean.getId()); // id
                setCellValue(row, "excel.item.name.col", itemBean.getName()); // name
                setCellValue(row, "excel.item.value.col", itemBean.getValue()); // value
                setCellValue(row, "excel.item.text.col", itemBean.getText()); // text
                // FindBy
                setCellValue(row, "excel.item.findby.col", itemBean.getFindBy()); // 選択方法
                setCellValue(row, "excel.item.findby.val.col", itemBean.getFindByVal()); // 選択値
                setCellValue(row, "excel.item.findby.cnt.col", itemBean.getFindByCnt()); // 取得数
                // Java実装
                setCellValue(row, "excel.java.sendkeys.col", itemBean.getOperateSendKeys()); // 値設定
                setCellValue(row, "excel.java.get.value.col", itemBean.getOperateGetValue()); // 値取得(value)
                setCellValue(row, "excel.java.get.text.col", itemBean.getOperateGetText()); // 値取得(text)
                setCellValue(row, "excel.java.click.col", itemBean.getOperateClick()); // クリック
                setCellValue(row, "excel.java.select.index.col", itemBean.getOperateSelectIndex()); // 選択(index)
                setCellValue(row, "excel.java.select.value.col", itemBean.getOperateSelectValue()); // 選択(value)
                setCellValue(row, "excel.java.select.text.col", itemBean.getOperateSelectText()); // 選択(text)
                setCellValue(row, "excel.java.frame.change.col", itemBean.getOperateFrameChange()); // Frame変更

                targetRow++;
            }

            if (excelBean.getItemList().size() > EXCEL_DETAIL_MAX) {
                logger.warn("Excel明細上限は{}件です。", EXCEL_DETAIL_MAX);
            }

            /*
             * 結果出力
             */
            workbook.write(new FileOutputStream(outPutFilePath.toString()));
            logger.debug("結果出力 -> {}", outPutFilePath.toString());
        }
    }

    /**
     * 項目情報リストの取得
     * @param document
     * @param cssSelector
     * @return list
     */
    private List<HtmlItemBean> analyzeHtmlTag(Document document,
                                              String cssSelector) {
        List<HtmlItemBean> itemBeanList = new ArrayList<>();

        // 属性オブジェクトの取得
        for (Element element : document.select(cssSelector)) {
            logger.debug(element.toString());

            // 項目情報の生成
            HtmlItemBean itemBean = new HtmlItemBean();

            // HTMLを設定
            itemBean.setHtml(element.toString());

            // タグを設定
            itemBean.setTag(element.tagName());

            // type, id, name, valueの設定
            for (Attribute attr : element.attributes()) {
                ItemAttr itemAttr = ItemAttr.getEnum(attr.getKey());
                if (itemAttr == null) {
                    continue;
                }

                switch (itemAttr) {
                case type:
                    itemBean.setType(attr.getValue());
                    break;
                case id:
                    itemBean.setId(attr.getValue());
                    break;
                case name:
                    itemBean.setName(attr.getValue());
                    break;
                case value:
                    itemBean.setValue(attr.getValue());
                    break;
                }
            }

            // textの設定
            if (itemBean.getValue() == null) {
                String text = !element.text().isEmpty() ? element.text() : element.outerHtml();
                itemBean.setText(text);
            }

            // 項目情報リストに含まれているかチェック
            HtmlItemBean existsItemBean = existsItemBean(itemBeanList, itemBean);
            if (existsItemBean != null) {
                // 項目情報リストに含まれている場合

                // 取り出した項目情報に対して件数を+1する。(リストに追加はしない)
                existsItemBean.addCount();
            } else {
                // 項目情報リストに含まれていない場合

                // 取件数を+1する。
                itemBean.addCount();
                // 新規でリストに追加
                itemBeanList.add(itemBean);
            }
        }

        return itemBeanList;
    }

    /**
     * 項目情報の存在チェック
     * @param itemBeanList
     * @param itemBean
     * @return [存在する:項目情報 存在しない:null]
     */
    private HtmlItemBean existsItemBean(List<HtmlItemBean> itemBeanList,
                                        HtmlItemBean itemBean) {
        for (HtmlItemBean tempBean : itemBeanList) {
            if (itemBean.toString().equals(tempBean.toString())) { // TODO 比較方法がちょっと手抜き・・・
                return tempBean;
            }
        }
        return null;
    }

    /**
     * FindBy情報を生成
     * @param itemBean
     */
    private void makeFindByInfo(ExcelItemBean itemBean) {
        String findBy = "";
        String findByVal = "";
        if (!StringUtils.isEmpty(itemBean.getId())) {
            // id
            findBy = FindBy.id.name();
            findByVal = itemBean.getId();
        } else if (!StringUtils.isEmpty(itemBean.getName())) {
            // name
            findBy = FindBy.name.name();
            findByVal = itemBean.getName();
        } else if (!StringUtils.isEmpty(itemBean.getValue())) {
            // value
            findBy = FindBy.css.name();
            if (HtmlTag.input == HtmlTag.getEnum(itemBean.getTag())) {
                // inputタグ
                findByVal = "input[type='" + itemBean.getType() + "'][value='" + itemBean.getValue() + "']";
            } else {
                // その他タグ
                findByVal = "input[value='" + itemBean.getValue() + "']";
            }
        } else if (!StringUtils.isEmpty(itemBean.getText())) {
            // text
            findBy = FindBy.partialLinkText.name();
            findByVal = itemBean.getText();
        }
        itemBean.setFindBy(findBy);
        itemBean.setFindByVal(findByVal);

        if (itemBean.getCount() > 1) {
            itemBean.setFindByCnt(CNT_MULTI);
        }
    }

    /**
     * Operate情報を生成
     * @param itemBean
     */
    private void makeOperateInfo(ExcelItemBean itemBean) {

        // タグ情報で判断
        HtmlTag tag = HtmlTag.getEnum(itemBean.getTag());
        if (tag != null) {
            switch (tag) {
            case a:
            case button:
                itemBean.setOperateClick(ON);
                break;
            case select:
                itemBean.setOperateSelectIndex(ON);
                itemBean.setOperateSelectText(ON);
                itemBean.setOperateSelectValue(ON);
                break;
            case textarea:
                itemBean.setOperateSendKeys(ON);
                break;
            case input:
                break;
            case frame:
                itemBean.setOperateFrameChange(ON);
                break;
            // case form:
            // break;
            // case label:
            // break;
            default:
                break;
            }

        }

        // input type情報で判断
        ItemAttrType type = ItemAttrType.getEnum(itemBean.getType());
        if (type != null) {
            switch (type) {
            case checkbox:
            case button:
            case submit:
            case radio:
            case imgage:
                itemBean.setOperateClick(ON);
                break;
            case text:
            case password:
                itemBean.setOperateSendKeys(ON);
                break;
            case file:
                break;
            }
        }
    }

    /**
     * Cellへの値反映
     * @param row
     * @param propKey
     * @param value
     */
    private void setCellValue(Row row,
                              String colPropKey,
                              String value) {
        int col = prop.getInteger(colPropKey) - 1;
        Cell cell = row.getCell(col);
        cell.setCellValue(value);
    }

}
