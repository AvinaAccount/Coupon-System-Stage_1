package data.dao;

import common.ex.SystemMalFunctionException;
import data.ex.CouponException;
import data.ex.NoSuchCouponException;
import data.ex.ReturnCouponsException;
import data.ex.ZeroCouponAmountException;
import models.Coupon;

import java.util.Collection;
import java.util.Set;

/**
 * Basic coupon interface that designed to perform basic operations on the DB
 */
public interface CouponDao {

    /**
     * Method to create a new coupon in the DB.
     *
     * @param coupon Object type 'Coupon' to insert into DB.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning..
     * @throws CouponException            If coupons have the same title
     * @throws ReturnCouponsException     If there's a problem with getting all the coupons.
     */
    long createCoupon(Coupon coupon) throws SystemMalFunctionException, CouponException, ReturnCouponsException;


    /**
     * Method that remove coupon from DB by his id.
     *
     * @param id To identify the coupon.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws NoSuchCouponException      If thar's no coupon.
     */
    void removeCoupon(long id) throws SystemMalFunctionException, NoSuchCouponException;

    /**
     * Method that updating coupon by getting 'Coupon' object.
     *
     * @param coupon Update company by object type 'Coupon'.
     * @throws NoSuchCouponException      If thar's no coupon.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     */

    void updateCoupon(Coupon coupon) throws NoSuchCouponException, SystemMalFunctionException;

    /**
     * Method that subtracting the Amount column of the coupon
     *
     * @param id To identify the coupon.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws ZeroCouponAmountException  If the value of amount equals 0.
     */

    void decrementCouponAmount(long id) throws SystemMalFunctionException, ZeroCouponAmountException, NoSuchCouponException, ReturnCouponsException;

    /**
     * Method that return coupon by getting id.
     *
     * @param id To identify the coupon.
     * @return 'Coupon' object.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws NoSuchCouponException      If There's no coupon with the id argument
     */

    Coupon getCoupon(long id) throws SystemMalFunctionException, ReturnCouponsException, NoSuchCouponException;

    /**
     * Method that returning all the Coupons.
     *
     * @return 'Collection' object.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     */

    Collection<Coupon> getAllCoupon() throws SystemMalFunctionException, ReturnCouponsException;

    /**
     * Method that returning all the Coupons from the same category.
     *
     * @param category To identify the category .
     * @return 'Collection' object .
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     */
    Collection<Coupon> getCouponsByCategory(int category) throws SystemMalFunctionException;

    /**
     * Method that returning all the coupons of one company.
     *
     * @param id To identify the company.
     * @return Object type 'Set<Coupon>' with all the coupons of one company
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws ReturnCouponsException     If unable to get the company coupons
     */

    Set<Coupon> getCoupons(long id) throws SystemMalFunctionException, ReturnCouponsException;
}
