package selenium.generator.main;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import selenium.generator.main.bean.ExcelBean;
import selenium.generator.main.bean.HtmlBean;
import selenium.generator.main.logic.HtmlToExcelLogic;

public class HtmlToExcelMain {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(HtmlToExcelMain.class);

    /**
     * 起動
     * @param args
     */
    public static void main(String[] args) {

        logger.info("処理開始---------------------------------------------------------------------------------------");
        try {
            new HtmlToExcelMain().execute();
        } catch (Exception e) {
            logger.error("system error", e);
        }
        logger.info("処理完了---------------------------------------------------------------------------------------");
    }

    /**
     * メイン処理
     * @throws IOException
     * @throws ReflectiveOperationException
     */
    private void execute() throws IOException, ReflectiveOperationException {

        // ロジックの生成
        HtmlToExcelLogic logic = new HtmlToExcelLogic();

        // HTMLファイルパス一覧の取得
        logger.debug("★findFile");
        List<Path> fileList = logic.findFile();
        if (fileList.isEmpty()) {
            logger.info("対象ファイルなし");
            return;
        }

        for (Path path : fileList) {
            logger.info("対象HTMLファイル -> {}", path.toAbsolutePath().normalize());

            // HTMLページ解析
            logger.debug("★analyze");
            HtmlBean htmlBean = logic.analyze(path);

            // Excel情報の生成
            logger.debug("★createExcelBean");
            ExcelBean excelBean = logic.createExcelBean(htmlBean);

            // テンプレート生成
            logger.debug("★generate");
            logic.generate(excelBean);
        }
    }
}
