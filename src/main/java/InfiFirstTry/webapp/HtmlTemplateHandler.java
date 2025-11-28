package InfiFirstTry.webapp;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class HtmlTemplateHandler {
    /**Text of the File*/
    private String html;
    private File externalHtml;
    private Path externalHtmlPath;

    /**Constructor for the HtmlTemplateHandler by reading the bytes of the file
     * @param item Path to the Template File*/
    public HtmlTemplateHandler(String item) throws Exception {
        externalHtml= new File(System.getenv("WEBFILES_PATH"), item);
        if (externalHtml.exists()) {
            externalHtmlPath = Paths.get(externalHtml.getAbsolutePath());
        } else {
            externalHtmlPath = Paths.get("WebFiles/"+item);
        }
        byte[] bytes = Files.readAllBytes(externalHtmlPath);
        html = new String(bytes, StandardCharsets.UTF_8);
    }

    /**Replaces the placeholder with the given value
     * @param placeholder Placeholder to replace
     * @param value Value to replace the placeholder with*/
    public void replace(String placeholder, String value) {
        html = html.replace("{{" + placeholder + "}}", value);
    }

    /**Prints the HTML to the console*/
    public void printHtml() {
        System.out.println(html);
    }
}
