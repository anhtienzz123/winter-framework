package ecommerce.web;

import winter.core.annotation.WinterApplication;
import winter.web.WinterWebApplication;

@WinterApplication
public class EcommerceWebApplication {

    public static void main(String[] args) {
        WinterWebApplication.run(EcommerceWebApplication.class);
    }
}
