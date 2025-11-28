package InfiFirstTry.database.connectionClasses;

import InfiFirstTry.model.Product;
import InfiFirstTry.model.User;
import InfiFirstTry.service.SessionController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class DummyDataConnection {
    private static File externalJson;
    private static Path externalJsonPath;
    private static ObjectMapper mapper = new ObjectMapper();

    private static void fileManager(String item) throws IOException {
        externalJson= new File(System.getenv("JSON_PATH"), item);
        if (externalJson.exists()) {
            externalJsonPath = Paths.get(externalJson.getAbsolutePath());
        } else {
            externalJsonPath = Paths.get("DummyData/"+item);
        }
        externalJson = new File(externalJsonPath.toString());

        mapper = new ObjectMapper();
    }

    public <T> LinkedList<T> getList(String location, Class<T> clazz) {
        LinkedList<T> list = null;
        try {
            fileManager(location);
            mapper.registerModule(new JavaTimeModule());
            list = mapper.readValue(externalJson,
                    mapper.getTypeFactory().constructCollectionType(LinkedList.class, clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> void saveChanges(LinkedList<T> objects, String savingPlace){
        try {
            fileManager(savingPlace);
            mapper.registerModule(new JavaTimeModule());
            mapper.writeValue(externalJson, objects);
            Files.write(
                    Paths.get("DummyData/SessionIDS/" + "debug.txt"),
                    objects.toString().getBytes(StandardCharsets.UTF_8)   // <-- Java 8 braucht byte[]
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
