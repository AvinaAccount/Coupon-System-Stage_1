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
        customer.setFirstName("Customer Facade Test");
        customer.setLastName("Customer Facade Test");
        customer.setEmail("Customer_Facade_Test@gmail.com");
        customer.setPassword("CustomerFacade_Test_Password");
        customer.setId(188);

        try {
            CustomerFacade customerFacade = (CustomerFacade) AbsFacade.login("Rany@gmail.com", "987234983", LoginType.CUSTOMER);
            /**/
            System.out.println("Method - Purchase coupon:");
            customerFacade.purchaseCoupon(73);

            System.out.println("Method - Get my purchased coupon:");
            System.out.println(customerFacade.getMyPurchasedCoupons());

            System.out.println("Method - Get coupons by category:");
            System.out.println(customerFacade.getCouponsByCategory(1));

            System.out.println("Method - Get coupons lower than price:");
            System.out.println(customerFacade.getCouponsLowerThanPrice(1000));


        } catch (InvalidLoginException | SystemMalFunctionException | SQLException | NoSuchCouponException | NoSuchCustomerException | CouponAlreadyPurchasedException | ZeroCouponAmountException | ReturnCouponsException e) {
            e.printStackTrace();
        }

    }
}
