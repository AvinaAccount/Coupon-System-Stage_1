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
        coupon.setId(78);
        coupon.setCompanyId(24);
        coupon.setCategory(5);
        coupon.setTitle("Company_Facade_Last Test");
        coupon.setStartDate(Date.valueOf("2020-02-01"));
        coupon.setEndDate(Date.valueOf("2020-02-02"));
        coupon.setAmount(33);
        coupon.setDescription("Company_Facade_Test - Last Test---V2");
        coupon.setPrice(33);
        coupon.setImage("Company_Facade_Last Test---V2");

        try {
            CompanyFacade companyFacade = (CompanyFacade) AbsFacade.login("Intel@support.com", "Intel1234", LoginType.COMPANY);

            System.out.println("Method - Create coupon:");
            companyFacade.createCoupon(coupon);

            System.out.println("Method - Remove coupon:");
            companyFacade.removeCoupon(80);

            System.out.println("Method - Update coupon: ");
            /*Change the id coupon in line 22*/
            companyFacade.updateCoupon(coupon);

            System.out.println("Method - Get coupon: ");
            System.out.println(companyFacade.getCoupon(78));

            /*Reminder :this method returning only coupons that belong to her*/
            System.out.println("Method - Get all coupons: ");
            System.out.println(companyFacade.getAllCoupons());

            System.out.println("Method - Get coupons by category: ");
            System.out.println(companyFacade.getCouponsByCategory(1));

            System.out.println("Method - Get Coupons Lower Than Price... : ");
            /*Insert price*/
            System.out.println(companyFacade.getCouponsLowerThanPrice(1000));

            System.out.println("Method - Get Coupons Before End Date : ");
            System.out.println(companyFacade.getCouponsBeforeEndDate(Date.valueOf("2020-02-02")));


        } catch (InvalidLoginException | SystemMalFunctionException | SQLException | ReturnCouponsException | CouponException | NoSuchCouponException e) {
            e.printStackTrace();
        }

    }
}
