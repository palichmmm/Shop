import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.*;

public class JsonDataProvider {
    private StringBuilder sb = new StringBuilder();

    public JsonDataProvider(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception err) {
            System.out.println(err);
        }
    }

    public List<Product> pars() {
        List<Product> objectList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(sb.toString());
            JSONArray jsonArray = (JSONArray) object;
            for (Object obj : jsonArray) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Product employee = gson.fromJson(String.valueOf(obj), Product.class);
                objectList.add(employee);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return objectList;
    }
}
