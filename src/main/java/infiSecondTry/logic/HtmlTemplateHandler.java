package infiSecondTry.logic;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlTemplateHandler {
    /**Content of the File*/
    private String html;

    /**Constructor for the HtmlTemplateHandler by reading the bytes of the file
     * @param item Path to the Template File*/
    public HtmlTemplateHandler(String item) throws Exception {

        String filePfad = "WebFiles/";

        byte[] bytes = Files.readAllBytes(Paths.get(filePfad + item));

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
        System.out.println("Content-Type: text/html; charset=UTF-8");
        System.out.println();
        System.out.println(html);
    }
}
