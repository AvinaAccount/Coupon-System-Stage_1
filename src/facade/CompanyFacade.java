package facade;

import common.ex.SystemMalFunctionException;
import data.dao.CompanyDao;
import data.dao.CouponDao;
import data.db.CompanyDBDao;
import data.db.CouponDBDao;
import data.ex.CouponException;
import data.ex.InvalidLoginException;
import data.ex.NoSuchCouponException;
import data.ex.ReturnCouponsException;
import models.Company;
import models.Coupon;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class CompanyFacade extends AbsFacade {
    /**
     * User permissions to the database by the Dao class.
     */
    private Company company;
    private CompanyDao companyDao;
    private CouponDao couponDBDao;


    private CompanyFacade(String email, String password) throws SystemMalFunctionException, InvalidLoginException {
        companyDao = new CompanyDBDao();
        couponDBDao = new CouponDBDao();
        company = companyDao.login(email, password);
    }

    /**
     * Login method.
     *
     * @param email    To identify the user.
     * @param password To identify the user.
     * @return 'CompanyFacade' Object.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws InvalidLoginException      If one or more details is incorrect in the login user.
     */
    public static AbsFacade performLogin(String email, String password)
            throws SystemMalFunctionException, InvalidLoginException {
        CompanyFacade companyFacade = new CompanyFacade(email, password);
        return companyFacade;
    }

    /**
     * Method that creating coupon
     * Creation of a coupon with the same title, is not allowed
     *
     * @param coupon Object type 'Coupon' to insert into DB.
     * @return Coupon id (long)
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws CouponException            If thar's problem creating coupon with the same title
     * @throws ReturnCouponsException     If thar's a problem to get 'Collection' object with all coupons.
     */

    public long createCoupon(Coupon coupon) throws SystemMalFunctionException, CouponException, ReturnCouponsException {
        return couponDBDao.createCoupon(coupon);
    }

    /**
     * Method that remove coupon from th DB by getting only the ID of the coupon.
     *
     * @param couponId To identify the coupon.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws NoSuchCouponException      If there's no such coupon.
     */

    public void removeCoupon(long couponId) throws SystemMalFunctionException, NoSuchCouponException, ReturnCouponsException {
        /* We can remove only coupons that belong to this company */

        long companyId = company.getId();
        int couponCompanyId = getCoupon(couponId).getCompanyId();

        if (companyId == couponCompanyId) {
            couponDBDao.removeCoupon(couponId);
        } else {
            throw new NoSuchCouponException("There's no coupon in our company with id : " + couponId);
        }
    }

    /**
     * Method that updating coupon in the DB ,by getting "Coupon" object .
     * Not possible to update coupon's title.
     *
     * @param coupon Update coupon by object type 'Coupon'.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws CouponException            If thar's a problem updating coupon title.
     * @throws NoSuchCouponException      If thar's no such coupon in DB
     */

    public void updateCoupon(Coupon coupon) throws SystemMalFunctionException, CouponException, NoSuchCouponException, ReturnCouponsException {
        /*Getting coupon before updating*/
        long couponId = coupon.getId();
        Coupon originalCoupon = couponDBDao.getCoupon(couponId);

        if (coupon.getTitle().equals(originalCoupon.getTitle())) {
            couponDBDao.updateCoupon(coupon);
        } else {
            throw new CouponException("Unable to update title!");
        }
    }

    /**
     * Method that sets the values.
     *
     * @param ps     The prepared statement
     * @param coupon The updated Coupon
     * @throws SQLException If there a problem with SQL syntax or 'prepareStatement' operation.
     */

    private void updateCouponByCompany(PreparedStatement ps, Coupon coupon) throws SQLException {
        ps.setInt(1, coupon.getCategory());
        ps.setDate(2, coupon.getStartDate());
        ps.setDate(3, coupon.getEndDate());
        ps.setInt(4, coupon.getAmount());
        ps.setString(5, coupon.getDescription());
        ps.setDouble(6, coupon.getPrice());
        ps.setString(7, coupon.getImage());
        ps.setLong(8, coupon.getId());
    }

    /**
     * Method that return Only one coupon by getting his ID.
     *
     * @param couponId To identify the coupon.
     * @return Coupon object
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     */

    public Coupon getCoupon(long couponId) throws SystemMalFunctionException, NoSuchCouponException, ReturnCouponsException {
        return couponDBDao.getCoupon(couponId);
    }

    /**
     * Method that return all the coupons of the company from the DB.
     *
     * @return 'Collection' object of all the coupons of the company in the DB.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws ReturnCouponsException     If thar's a problem to get 'Collection' object with all the company coupons.
     */

    public Collection<Coupon> getAllCoupons() throws SystemMalFunctionException, ReturnCouponsException {
        return couponDBDao.getCoupons(company.getId());

    }

    /**
     * Method that return all the coupons according to their category.
     * The method returns only coupons that belong to her (The company).
     *
     * @param category To identify the category.
     * @return 'Collection' object of all the coupons with the same category.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     */

    public Collection<Coupon> getCouponsByCategory(int category) throws SystemMalFunctionException {
        Collection<Coupon> couponsByCategory = couponDBDao.getCouponsByCategory(category);
        Collection<Coupon> couponCollection = new HashSet<>();
        for (Coupon coupon : couponsByCategory) {
            if (coupon.getCompanyId() == company.getId()) {
                couponCollection.add(coupon);
            }
        }
        return couponCollection;

    }

    /**
     * Method that return all the coupons that lower than price that received.
     *
     * @param price The price limit
     * @return 'Collection' object of all the coupons that lower than price.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws ReturnCouponsException     If thar's a problem to return 'Collection' object with all coupons.
     */

    public Collection<Coupon> getCouponsLowerThanPrice(double price) throws SystemMalFunctionException, ReturnCouponsException {

        Collection<Coupon> couponCollection = couponDBDao.getCoupons(company.getId());
        Collection<Coupon> couponsLowerPrice = new HashSet<>();
        for (Coupon coupon : couponCollection) {
            if (coupon.getPrice() < price) {
                couponsLowerPrice.add(coupon);
            }
        }
        return couponsLowerPrice;
    }

    /**
     * Method that return all the coupon before specific date.
     *
     * @param endDate The date limit
     * @return 'Collection' object of all the coupons before the end date.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws ReturnCouponsException     If thar's a problem to return 'Collection' object with all coupons.
     */

    public Collection<Coupon> getCouponsBeforeEndDate(Date endDate) throws SystemMalFunctionException, ReturnCouponsException {

        Collection<Coupon> couponCollection = couponDBDao.getCoupons(company.getId());
        Collection<Coupon> couponsBeforeEndDate = new HashSet<>();

        for (Coupon coupon : couponCollection) {
            Date couponEndDate = coupon.getEndDate();
            if (couponEndDate.compareTo(endDate) > 0) {
                couponsBeforeEndDate.add(coupon);
            }
        }
        return couponsBeforeEndDate;
    }
}
