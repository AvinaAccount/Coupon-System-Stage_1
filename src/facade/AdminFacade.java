package facade;

import common.ex.SystemMalFunctionException;
import data.dao.CompanyDao;
import data.dao.CouponDao;
import data.dao.CustomerDao;
import data.db.CompanyDBDao;
import data.db.CouponDBDao;
import data.db.CustomerDBDao;
import data.ex.*;
import models.Company;
import models.Coupon;
import models.Customer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

/**
 * Class to control all the companies,customers and coupons by the admin only.
 */

public class AdminFacade extends AbsFacade {
    /**
     * User permissions to the database by the Dao class.
     */
    private CouponDao couponDao;
    private CompanyDao companyDao;
    private CustomerDao customerDao;

    private AdminFacade() {
        couponDao = new CouponDBDao();
        companyDao = new CompanyDBDao();
        customerDao = new CustomerDBDao();
    }

    /**
     * Method that gives access to the DB as an administrator by username and password.
     *
     * @param userName To identify the user.
     * @param password To identify the user.
     * @return 'AdminFacade' object.
     * @throws InvalidLoginException If the login password or username is incorrect.
     */
    public static AbsFacade performLogin(String userName, String password) throws InvalidLoginException {
        if (userName.equals("admin") && password.equals("1234"))
            return new AdminFacade();
        throw new InvalidLoginException("Unable to login with provided credentials as admin.");
    }

    /**
     * Method to create new company in the DB.
     *
     * @param company Object type 'Company' to insert into DB.
     * @return Company id (long)
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning..
     */

    public long createCompany(Company company) throws SystemMalFunctionException {
        return companyDao.createCompany(company);
    }

    /**
     * Method to remove company from DB.
     * To remove a company must previously delete the coupons that belong to the company
     *
     * @param companyId To identify the company.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws NoSuchCompanyException     If the company not exist.
     */
    public void removeCompany(long companyId) throws SystemMalFunctionException,
            NoSuchCompanyException, ReturnCouponsException {

        /*Check if the company exists*/
        if (companyDao.getCompany(companyId) == null) {
            throw new NoSuchCompanyException("This company doesn't exist");
        }

        Set<Coupon> coupons = couponDao.getCoupons(companyId);

        for (Coupon coupon : coupons) {
            try {
                /* 1.Remove the coupons of the company*/
                couponDao.removeCoupon(coupon.getId());
            } catch (NoSuchCouponException e) {
                // Ignore since we know for sure that all the coupons belong to the specified company
            }
        }
        /* 2.Remove the company .*/
        companyDao.removeCompany(companyId);
    }

    /**
     * Method that update the company .
     * The method can not change the name of a company.
     *
     * @param company Update company by object type 'Company'.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws NoSuchCompanyException     If there's no such company in the DB.
     * @throws UpdateCompanyException     If unable to update the company .
     */

    public void updateCompany(Company company) throws SystemMalFunctionException, NoSuchCompanyException, UpdateCompanyException {

        Company companyBeforeUpdating = companyDao.getCompany(company.getId());
        String name = company.getName();
        String nameBeforeUpdating = companyBeforeUpdating.getName();

        /*Compare the resulting name and the existing name of the company*/
        if (name.equals(nameBeforeUpdating)) {
            companyDao.updateCompany(company);
        } else {
            throw new UpdateCompanyException("Unable to update the company name.");
        }
    }

    /**
     * Method that return all the companies from DB.
     *
     * @return Collection of all companies.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws NoSuchCompanyException     If failed getting all companies.
     */
    public Collection<Company> getAllCompanies() throws SystemMalFunctionException, NoSuchCompanyException {
        return companyDao.getAllCompanies();

    }

    /**
     * Method that return company object by getting the company ID number.
     *
     * @param companyId To identify the company.
     * @return Company object.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws NoSuchCompanyException     If the argument "companyId" is less than 0 or equal.
     */

    public Company getCompany(long companyId) throws SystemMalFunctionException, NoSuchCompanyException {
        return companyDao.getCompany(companyId);

    }


    /**
     * Method that create customer in DB by getting object from type Customer.
     * Unable to create a customer with an email that is already exists.
     *
     * @param customer Object type 'Customer' to insert into DB.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws SQLException               If there was a problem creating customer.
     * @throws CustomerAlreadyExists      If customer already  exists.
     */
    public void createCustomer(Customer customer) throws SystemMalFunctionException, SQLException, CustomerAlreadyExists {
        customerDao.createCustomer(customer);
    }

    /**
     * Method that updating customer by getting "Customer" object
     * Unable to change the name of a customer
     *
     * @param customer Update customer by object type 'Customer.
     * @throws CustomerUpdateException    If unable to update name of the customer
     * @throws NoSuchCustomerException    If there is no such customer
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws SQLException               If there was a system error.
     */

    public void updateCustomer(Customer customer) throws CustomerUpdateException, NoSuchCustomerException,
            SystemMalFunctionException, SQLException {

        /*Getting the customer before updating */
        long customerId = customer.getId();
        Customer oldCustomer = customerDao.getCustomer(customerId);

        /* First name and last name from insert customer*/
        String insertFirstName = customer.getFirstName();
        String insertLastName = customer.getLastName();

        /* First name and last name written in the DB*/
        String firstNameOld = oldCustomer.getFirstName();
        String lastNameOld = oldCustomer.getLastName();

        if (insertFirstName.equals(firstNameOld) && insertLastName.equals(lastNameOld)) {
            customerDao.updateCustomer(customer);
        } else {
            throw new CustomerUpdateException("Unable to update name of the customer (Only email and password)");
        }
    }

    /**
     * Method that sets the values.
     *
     * @param ps       The prepared statement
     * @param customer The updated customer
     * @throws SQLException If fail to place the values.
     */

    private void setCustomerUpdatedValues(PreparedStatement ps, Customer customer) throws SQLException {
        ps.setString(1, customer.getEmail());
        ps.setString(2, customer.getPassword());
        ps.setLong(3, customer.getId());

    }

    /**
     * Method that remove customer from th DB by getting only the ID of the customer.
     *
     * @param customerId To identify the customer.
     * @throws NoSuchCustomerException    If there's no such customer.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws SQLException               If there a problem with SQL syntax or prepareStatement operation.
     */

    public void removeCustomer(long customerId) throws NoSuchCustomerException, SystemMalFunctionException, SQLException {
        customerDao.removeCustomer(customerId);
    }

    /**
     * Method that return all the customers
     *
     * @return Object type 'Collection' of all customers.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws SQLException               If there a problem with SQL syntax or prepareStatement operation.
     */

    public Collection<Customer> getAllCustomers() throws SystemMalFunctionException, SQLException {
        return customerDao.getAllCustomer();
    }

    /**
     * Method that return one customer by his ID.
     *
     * @param customerId To identify the customer.
     * @return Object type 'Customer'
     * @throws NoSuchCustomerException    If there is no such customer in the DB.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws SQLException               If there a problem with SQL syntax or prepareStatement operation.
     */

    public Customer getCustomer(long customerId) throws NoSuchCustomerException, SystemMalFunctionException, SQLException {
        return customerDao.getCustomer(customerId);
    }

}
