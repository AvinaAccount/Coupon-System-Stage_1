package FacadesTests;

import common.LoginType;
import common.ex.SystemMalFunctionException;
import data.ex.CouponException;
import data.ex.InvalidLoginException;
import data.ex.NoSuchCouponException;
import data.ex.ReturnCouponsException;
import facade.AbsFacade;
import facade.CompanyFacade;
import models.Coupon;

import java.sql.Date;
import java.sql.SQLException;

public class MainCompanyFacade {
    public static void main(String[] args) {

        Coupon coupon = new Coupon();
        coupon.setId("Implement!");
        coupon.setCompanyId("Implement!");
        coupon.setCategory("Implement!");
        coupon.setTitle(""Implement!"");
        coupon.setStartDate(Date.valueOf("Implement!"));
        coupon.setEndDate(Date.valueOf("Implement!"));
        coupon.setAmount("Implement!");
        coupon.setDescription("Implement!");
        coupon.setPrice("Implement!");
        coupon.setImage("Implement!");

        try {
            CompanyFacade companyFacade = (CompanyFacade) AbsFacade.login("Implement!", "Implement!", LoginType.COMPANY);

            System.out.println("Method - Create coupon:");
            companyFacade.createCoupon(coupon);

            System.out.println("Method - Remove coupon:");
            companyFacade.removeCoupon("Implement!");

            System.out.println("Method - Update coupon: ");
            /*Change the id coupon in line 22*/
            companyFacade.updateCoupon(coupon);

            System.out.println("Method - Get coupon: ");
            System.out.println(companyFacade.getCoupon("Implement!"));

            /*Reminder :this method returning only coupons that belong to her*/
            System.out.println("Method - Get all coupons: ");
            System.out.println(companyFacade.getAllCoupons());

            System.out.println("Method - Get coupons by category: ");
            System.out.println(companyFacade.getCouponsByCategory("Implement!"));

            System.out.println("Method - Get Coupons Lower Than Price... : ");
            /*Insert price*/
            System.out.println(companyFacade.getCouponsLowerThanPrice("Implement!"));

            System.out.println("Method - Get Coupons Before End Date : ");
            System.out.println(companyFacade.getCouponsBeforeEndDate(Date.valueOf("Implement!")));


        } catch (InvalidLoginException | SystemMalFunctionException | SQLException | ReturnCouponsException | CouponException | NoSuchCouponException e) {
            e.printStackTrace();
        }

    }
}
