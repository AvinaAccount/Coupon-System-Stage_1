package facade;

import common.ex.SystemMalFunctionException;
import data.dao.CouponDao;
import data.dao.CustomerDao;
import data.db.CouponDBDao;
import data.db.CustomerDBDao;
import data.ex.*;
import models.Coupon;
import models.Customer;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class CustomerFacade extends AbsFacade {
    /**
     * User permissions to the database by the Dao class.
     */
    private Customer customer;
    private CustomerDao customerDao;


    private CustomerFacade(String email, String password)
            throws InvalidLoginException, SQLException, SystemMalFunctionException {

        customerDao = new CustomerDBDao();
        this.customer = customerDao.login(email, password);
    }

    /**
     * Method that gives access to the DB as an administrator by username and password.
     *
     * @param email    To identify the user.
     * @param password To identify the user.
     * @return CustomerFacade object
     * @throws InvalidLoginException if the login password or username is incorrect.
     */

    public static AbsFacade performLogin(String email, String password)
            throws SystemMalFunctionException, SQLException, InvalidLoginException {

        CustomerFacade customerFacade = new CustomerFacade(email, password);
        return customerFacade;
    }

    /**
     * Method that documents the registration of the coupons purchased by the customers
     * A customer can not hold the same coupons more than once.
     *
     * @param couponId To identify the coupon
     * @throws SystemMalFunctionException      If there's a general problem with the system's functioning.
     * @throws NoSuchCouponException           If there is no such coupon in the DB.
     * @throws NoSuchCustomerException         If there is no such customer in the DB.
     * @throws SQLException                    If there a problem with SQL syntax or prepareStatement operation.
     * @throws CouponAlreadyPurchasedException If coupon already purchased by the customer.
     * @throws ZeroCouponAmountException       The coupon supply is over (Amount = 0).
     */

    public void purchaseCoupon(long couponId)
            throws SystemMalFunctionException, NoSuchCouponException,
            NoSuchCustomerException, SQLException, CouponAlreadyPurchasedException, ZeroCouponAmountException, ReturnCouponsException {

        CouponDao couponDao = new CouponDBDao();
        customerDao.insertCustomerCoupon(couponId, customer.getId());
        couponDao.decrementCouponAmount(couponId);
    }

    /**
     * Method that return all the coupons that purchased by the customer.
     *
     * @return 'Collection' type 'Coupon'
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     */

    public Collection<Coupon> getMyPurchasedCoupons() throws SystemMalFunctionException {
        return customerDao.getCoupons(customer.getId());

    }

    /**
     * Method that return all the coupons that purchased by the customer according to their category.
     *
     * @param category Category number
     * @return 'Collection' type 'Coupon'.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     */

    public Collection<Coupon> getCouponsByCategory(int category) throws SystemMalFunctionException {

        Collection<Coupon> allCustomerCoupons = customerDao.getCoupons(customer.getId());
        Collection<Coupon> couponsByCategory = new HashSet<>();
        for (Coupon coupon : allCustomerCoupons) {
            if (coupon.getCategory() == category) {
                couponsByCategory.add(coupon);
            }
        }
        return couponsByCategory;
    }

    /**
     * Method that return all the coupons under the given price ,that purchased by the customer
     *
     * @param price The price limit
     * @return 'Collection' type 'Coupon'.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     */

    public Collection<Coupon> getCouponsLowerThanPrice(double price) throws SystemMalFunctionException {

        Collection<Coupon> coupons = customerDao.getCoupons(customer.getId());
        Collection<Coupon> couponsLowerThanPrice = new HashSet<>();
        for (Coupon coupon : coupons) {
            if (coupon.getPrice() < price) {
                couponsLowerThanPrice.add(coupon);
            }
        }
        return couponsLowerThanPrice;
    }
}
