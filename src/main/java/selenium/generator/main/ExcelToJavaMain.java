package selenium.generator.main;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.TemplateException;
import selenium.generator.main.bean.ExcelBean;
import selenium.generator.main.bean.PageBean;
import selenium.generator.main.logic.ExcelToJavaLogic;

public class ExcelToJavaMain {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(ExcelToJavaMain.class);

    /**
     * 起動
     * @param args
     */
    public static void main(String[] args) {

        logger.info("処理開始---------------------------------------------------------------------------------------");
        try {
            new ExcelToJavaMain().execute();
        } catch (Exception e) {
            logger.error("system error", e);
        }
        logger.info("処理完了---------------------------------------------------------------------------------------");
    }

    /**
     * メイン処理
     * @throws IOException
     * @throws TemplateException
     */
    private void execute() throws IOException, TemplateException {

        // ロジックの生成
        ExcelToJavaLogic logic = new ExcelToJavaLogic();

        // Excelファイルパス一覧の取得
        logger.debug("★findFile");
        List<Path> fileList = logic.findFile();
        if (fileList.isEmpty()) {
            logger.info("対象ファイルなし");
            return;
        }

        for (Path path : fileList) {
            logger.info("対象Excelファイル -> {}", path.toAbsolutePath().normalize());

            // Excelページ解析
            logger.debug("★analyze");
            ExcelBean excelBean = logic.analyze(path);

            // ページ情報の生成
            logger.debug("★createPageBean");
            PageBean pageBean = logic.createPageBean(excelBean);

            // テンプレート生成
            logger.debug("★generate");
            logic.generate(pageBean);
        }
    }
}
