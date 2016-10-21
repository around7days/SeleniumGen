package selenium.generator.main.logic;

import static selenium.generator.common.Const.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import selenium.generator.common.GeneratePropertyManager;
import selenium.generator.common.GenerateUtils;
import selenium.generator.main.bean.ExcelBean;
import selenium.generator.main.bean.ExcelItemBean;
import selenium.generator.main.bean.PageBean;

public class ExcelToJavaLogic {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(ExcelToJavaLogic.class);

    /** プロパティ */
    private static final GeneratePropertyManager prop = GeneratePropertyManager.INSTANCE;

    /**
     * Excelファイルパス一覧の取得
     * @return ファイル一覧
     * @throws IOException
     */
    public List<Path> findFile() throws IOException {

        Path dir = Paths.get(prop.getString("excel.input.file.dir"));
        String fileNmRegex = prop.getString("excel.input.file.extension");

        return GenerateUtils.getFileList(dir, fileNmRegex);
    }

    /**
     * Excelページ解析
     * @param path
     * @return ExcelBean
     * @throws IOException
     */
    public ExcelBean analyze(Path path) throws IOException {

        ExcelBean excelBean = new ExcelBean();

        // クラス名の取得
        String classNm = GenerateUtils.getFileNm(path);
        classNm = GenerateUtils.firstCharUpper(classNm);
        excelBean.setClassNm(classNm); // TODO 最終的にはExcelファイルの中に項目を作って取得する

        // Excelファイルの読込
        try (FileInputStream fis = new FileInputStream(path.toFile()); Workbook workbook = new XSSFWorkbook(fis)) {
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
                ExcelItemBean itemBean = new ExcelItemBean();

                // 出力列情報の取得
                Row row = sheet.getRow(targetRow);

                // HTML画面項目
                itemBean.setItem(getCellValue(row, "excel.item.col")); // 項目名
                itemBean.setTag(getCellValue(row, "excel.item.tag.col")); // tag
                itemBean.setType(getCellValue(row, "excel.item.input.type.col")); // type
                itemBean.setId(getCellValue(row, "excel.item.id.col")); // id
                itemBean.setName(getCellValue(row, "excel.item.name.col")); // name
                itemBean.setValue(getCellValue(row, "excel.item.value.col")); // value
                itemBean.setText(getCellValue(row, "excel.item.text.col")); // text
                // FindBy
                itemBean.setFindBy(getCellValue(row, "excel.item.findby.col")); // 選択方法
                itemBean.setFindByVal(getCellValue(row, "excel.item.findby.val.col")); // 選択値
                itemBean.setFindByCnt(getCellValue(row, "excel.item.findby.cnt.col")); // 取得数

                targetRow++;

                // リストに追加
                if (!itemBean.getItem().isEmpty()) {
                    excelBean.getItemList().add(itemBean);
                }
            }
        }

        return excelBean;
    }

    /**
     * ページ情報の生成
     * @param excelBean
     * @return
     */
    public PageBean createPageBean(ExcelBean excelBean) {
        PageBean pageBean = new PageBean();

        // クラス名
        pageBean.setClassNm(excelBean.getClassNm() + prop.getString("java.class.name.suffix"));
        // パッケージ
        pageBean.setPackageNm(prop.getString("java.package"));
        // 継承クラス名
        pageBean.setExtendsClassNm(prop.getString("java.extends.class"));
        // 項目リスト
        pageBean.setItemList(excelBean.getItemList());

        return pageBean;
    }

    /**
     * テンプレート生成
     * @param pageBean
     * @throws IOException
     * @throws TemplateException
     */
    public void generate(PageBean pageBean) throws IOException, TemplateException {

        // テンプレートファイル名・ファイルパス・文字コードの取得
        String templateFileNm = prop.getString("freemaker.template.file");
        Path templateFilePath = GenerateUtils.getPath(templateFileNm);
        String templateFileEncoding = prop.getString("freemaker.template.encoding");

        // Freemarkerの初期化
        Configuration cfg = new Configuration(Configuration.getVersion());
        cfg.setDirectoryForTemplateLoading(templateFilePath.toFile().getParentFile());
        cfg.setDefaultEncoding(templateFileEncoding);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);

        // テンプレートの読込
        Template template = cfg.getTemplate(templateFileNm);

        // データモデルを定義
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("pageBean", pageBean);

        // 出力ファイル情報の生成
        String javaFileNm = pageBean.getClassNm() + ".java";
        Path javaFilePath = Paths.get(prop.getString("java.output.file.dir"), javaFileNm);
        String javaEncoding = prop.getString("java.encoding");

        // テンプレートのマージ、出力処理
        try (Writer writer = Files.newBufferedWriter(javaFilePath, Charset.forName(javaEncoding))) {
            template.process(dataModel, writer);
        }

        logger.debug("結果出力 -> {}", javaFilePath);
    }

    /**
     * Cellから値取得
     * @param row
     * @param propKey
     * @return 値
     */
    private String getCellValue(Row row,
                                String colPropKey) {
        int col = prop.getInteger(colPropKey) - 1;
        Cell cell = row.getCell(col);
        if (cell == null) {
            return "";
        }
        return cell.getStringCellValue();
    }
}
