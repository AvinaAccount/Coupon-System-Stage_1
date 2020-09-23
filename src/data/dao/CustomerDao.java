package data.dao;

import common.ex.SystemMalFunctionException;
import data.ex.*;
import models.Coupon;
import models.Customer;

import java.sql.SQLException;
import java.util.Collection;


/**
 * Basic customer interface that designed to perform basic operations on the DB
 */

public interface CustomerDao {
    /**
     * Method that create customer in DB by getting object from type Customer.
     *
     * @param customer Object type 'Customer' to insert into DB.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws SQLException               If there was a problem creating customer.
     * @throws CustomerAlreadyExists      If customer already  exists.
     */
    long createCustomer(Customer customer) throws SystemMalFunctionException, SQLException, CustomerAlreadyExists;

    /**
     * Method that remove customer from th DB by getting only the ID of the customer.
     *
     * @param id To identify the customer.
     * @throws NoSuchCustomerException    If there's no such customer.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws SQLException               If there a problem with SQL syntax or prepareStatement operation.
     */
    void removeCustomer(long id) throws NoSuchCustomerException, SystemMalFunctionException, SQLException;

    /**
     * Method that updating customer by getting "Customer" object
     *
     * @param customer Update customer by object type 'Customer.
     * @throws NoSuchCustomerException    If there is no such customer
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws SQLException               If there was a system error.
     */

    void updateCustomer(Customer customer) throws NoSuchCustomerException, SQLException, SystemMalFunctionException;

    /**
     * Method to get customer by his ID only.
     *
     * @param id To identify the customer
     * @return Object type 'Customer'
     * @throws NoSuchCustomerException    If there is no such customer in the DB.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws SQLException               If there a problem with SQL syntax or prepareStatement operation.
     */

    Customer getCustomer(long id) throws NoSuchCustomerException, SQLException, SystemMalFunctionException;

    /**
     * Method that return all the customers
     *
     * @return Object type 'Collection' of all customers.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws SQLException               If there a problem with SQL syntax or prepareStatement operation.
     */

    Collection<Customer> getAllCustomer() throws SystemMalFunctionException, SQLException;

    /**
     * Method that return all the coupons of the customer
     *
     * @param customerId To identify the customer
     * @return Object type 'Collection' of coupons.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     */

    Collection<Coupon> getCoupons(long customerId) throws SystemMalFunctionException;

    /**
     * Method that insert into the table ,the coupon id and the customer id .
     *
     * @param couponId   To identify the coupon.
     * @param customerId To identify the customer.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     */

    void insertCustomerCoupon(long couponId, long customerId) throws SystemMalFunctionException,
            CouponAlreadyPurchasedException, NoSuchCouponException, NoSuchCustomerException, SQLException, ReturnCouponsException;

    /**
     * Login method as a customer by email and password
     *
     * @param email    Login argument
     * @param password Login argument
     * @return Object type 'Customer'.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws InvalidLoginException      If the login password or username is incorrect.
     */
    Customer login(String email, String password) throws SystemMalFunctionException, SQLException, InvalidLoginException;


}
