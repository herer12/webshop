package infiSecondTry.database.dummyData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.util.LinkedList;

public class DummyDataConnection {

    private static final String dummyDataSavingPlace = "Data/DummyData/";
    private final ObjectMapper mapper;

    public DummyDataConnection() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    private File getFile(String item) {
        return new File(dummyDataSavingPlace + item);
    }

    public <T> LinkedList<T> getList(String location, Class<T> clazz) {
        try {
            File jsonFile = getFile(location);
            return mapper.readValue(
                    jsonFile,
                    mapper.getTypeFactory().constructCollectionType(LinkedList.class, clazz)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedList<>();  // wichtig!
        }
    }

    public static <T> void saveChanges(LinkedList<T> objects, String location) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.writeValue(new File(dummyDataSavingPlace + location), objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
