package infiSecondTry.database.dummyData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.util.LinkedList;

public class DummyDataConnection {
    private static final String dummyDataSavingPlace = "Data/DummyData/";
    private static File externalJson;
    private static final ObjectMapper mapper = new ObjectMapper();

    private static void fileManager(String item) {

        externalJson = new File(dummyDataSavingPlace + item);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
