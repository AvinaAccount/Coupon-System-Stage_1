package FacadesTests;

import common.LoginType;
import common.ex.SystemMalFunctionException;
import data.ex.*;
import facade.AbsFacade;
import facade.CustomerFacade;
import models.Customer;

import java.sql.SQLException;

public class MainCustomerFacade {
    public static void main(String[] args) {

        Customer customer = new Customer();
        customer.setFirstName("Implement!");
        customer.setLastName("Implement!");
        customer.setEmail("Implement!");
        customer.setPassword("Implement!");
        customer.setId("Implement!");

        try {
            CustomerFacade customerFacade = (CustomerFacade) AbsFacade.login("Implement!", "Implement!", LoginType.CUSTOMER);
            /**/
            System.out.println("Method - Purchase coupon:");
            customerFacade.purchaseCoupon("Implement!");

            System.out.println("Method - Get my purchased coupon:");
            System.out.println(customerFacade.getMyPurchasedCoupons());

            System.out.println("Method - Get coupons by category:");
            System.out.println(customerFacade.getCouponsByCategory("Implement!"));

            System.out.println("Method - Get coupons lower than price:");
            System.out.println(customerFacade.getCouponsLowerThanPrice("Implement!"));


        } catch (InvalidLoginException | SystemMalFunctionException | SQLException | NoSuchCouponException | NoSuchCustomerException | CouponAlreadyPurchasedException | ZeroCouponAmountException | ReturnCouponsException e) {
            e.printStackTrace();
        }

    }
}
