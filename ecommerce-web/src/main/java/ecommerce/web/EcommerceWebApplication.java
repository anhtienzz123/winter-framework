package ecommerce.web;

import winter.core.WinterRunner;
import winter.core.annotation.WinterApplication;

@WinterApplication(scanBasePackages = {"ecommerce.web.configuration", "ecommerce.web.controller",
        "ecommerce.web.entity", "ecommerce.web.repository", "ecommerce.web.service.impl"})
public class EcommerceWebApplication {

    public static void main(String[] args) {
        WinterRunner.run(EcommerceWebApplication.class);
    }
}
