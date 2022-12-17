package winter.web;

import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();

        Product product = new Product(1, "product name");
        writer.print(new Gson().toJson(product));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Product {
        private int id;

        private String name;
    }
}
