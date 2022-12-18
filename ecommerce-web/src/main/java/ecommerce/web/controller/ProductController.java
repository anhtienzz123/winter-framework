package ecommerce.web.controller;

import ecommerce.web.entity.Product;
import winter.core.annotation.Controller;
import winter.web.annotation.GetMapping;
import winter.web.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    @GetMapping("/{id}")
    public Product getProductDetail() {
        return new Product(1, "product name 1", 10.0, 100);
    }
}
