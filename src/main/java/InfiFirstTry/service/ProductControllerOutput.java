package InfiFirstTry.service;

import InfiFirstTry.model.Product;

import java.util.Locale;

public class ProductControllerOutput {

    public static String handleProducts(Product[] products) {

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < products.length; i++) {
            Product p = products[i];
            json.append(String.format(
                    Locale.US,
                    "{\"id\":%d,\"name\":\"%s\",\"price\":%.2f}",
                    p.getIdProduct(), p.getName(), p.getPrice()
            ));


            if (i < products.length - 1) json.append(",");
        }
        json.append("]");

        return  json.toString();
    }
}

