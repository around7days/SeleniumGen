package selenium.generator.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GenerateUtils {

    /**
     * ファイル一覧の取得<br>
     * 対象ディレクトリ直下のファイルのみ対象<br>
     * 対象ファイルはglob構文で記載
     * @param dir
     * @param fileNmGlob
     * @return ファイル一覧
     * @throws IOException
     */
    public static List<Path> getFileList(Path dir,
                                         String fileNmGlob) throws IOException {
        // 検索結果の格納List
        List<Path> list = new ArrayList<Path>();

        // 検索処理
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, fileNmGlob)) {
            stream.forEach(list::add);
        }

        return list;
    }

    /**
     * ファイルパスから拡張子を除いたファイル名を取得<br>
     * 例）C:\test.txt → test
     * @param filePath
     * @return ファイル名
     */
    public static String getFileNm(Path filePath) {
        String fileNm = filePath.toFile().getName();
        return fileNm.substring(0, fileNm.lastIndexOf("."));
    }

    /**
     * クラスパス配下のファイル名からファイルパスを取得
     * @param fileNm
     * @return path
     */
    public static Path getPath(String fileNm) {
        String path = GeneratePropertyManager.INSTANCE.getClass().getClassLoader().getResource(fileNm).getPath();
        return new File(path).toPath();
    }

    /**
     * 先頭文字大文字化
     * @param value
     * @return value
     */
    public static String firstCharUpper(String value) {
        if (value.length() > 1) {
            return String.valueOf(value.charAt(0)).toUpperCase() + value.substring(1, value.length());
        } else {
            return value.toUpperCase();
        }
    }

    /**
     * キャメルケース化変換（先頭大文字）<br>
     * 例）CAMEL_CASE ⇒ CamelCase
     * @param inputValue 文字列
     * @return キャメルケース項目
     */
    public static String camelCaseUpper(String inputValue) {
        String value = camelCaseLower(inputValue);
        return Character.toUpperCase(value.charAt(0)) + value.substring(1, value.length());
    }

    /**
     * キャメルケース化変換（先頭小文字）<br>
     * 例）CAMEL_CASE ⇒ camelCase
     * @param inputValue 文字列
     * @return キャメルケース項目
     */
    public static String camelCaseLower(String inputValue) {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < inputValue.length(); i++) {
            char token = inputValue.charAt(i);
            if ('_' == token || ' ' == token) {
                // 「_」or「 」の時、次の文字を大文字に変換
                token = inputValue.charAt(++i);
                bf.append(Character.toUpperCase(token));
            } else {
                // 上記以外の時、小文字に変換
                bf.append(Character.toLowerCase(token));
            }
        }
        return bf.toString();
    }

}
