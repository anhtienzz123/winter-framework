package ecommerce.web.controller;

import ecommerce.web.entity.Product;
import winter.core.annotation.Controller;
import winter.web.annotation.GetMapping;
import winter.web.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    @GetMapping("/detail1")
    public Product getProductDetail1() {
        return new Product(1, "product name 1", 10.0, 100);
    }

    @GetMapping("/detail2")
    public Product getProductDetail2() {
        return new Product(2, "product name 2", 20.0, 200);
    }
}
