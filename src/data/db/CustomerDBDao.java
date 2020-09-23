package data.db;

import common.ConnectionPool;
import common.ex.SystemMalFunctionException;
import data.dao.CustomerDao;
import data.ex.*;
import models.Coupon;
import models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomerDBDao implements CustomerDao {

    @Override
    public long createCustomer(Customer customer) throws SystemMalFunctionException, SQLException, CustomerAlreadyExists {
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        long customerId = -1;
        Collection<Customer> customerCollection = getAllCustomer();

        for (Customer customerType : customerCollection) {
            String customerEmail = customer.getEmail();
            String customerTypeEmail = customerType.getEmail();
            if (customerEmail.equals(customerTypeEmail)) {
                throw new CustomerAlreadyExists("Unable to create customer that already exists!");
            }
        }

        try {
            ps = connection.prepareStatement(Schema.INSERT_CUSTOMER, ps.RETURN_GENERATED_KEYS);
            applyCustomerValuesOnStatement(customer, ps);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                customerId = rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new SystemMalFunctionException("There was a problem creating customer: " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new SystemMalFunctionException("Unable to close statement " + e.getMessage());
                }
            }
        }
        return customerId;
    }

    /**
     * Method that sets the values of the customer .
     *
     * @param customer The Company to create.
     * @param ps       The prepared statement
     * @throws SQLException If fail to place the values.
     */

    private void applyCustomerValuesOnStatement(Customer customer, PreparedStatement ps) throws SQLException {
        ps.setString(1, customer.getFirstName());
        ps.setString(2, customer.getLastName());
        ps.setString(3, customer.getEmail());
        ps.setString(4, customer.getPassword());
    }

    @Override
    public void removeCustomer(long id) throws NoSuchCustomerException, SystemMalFunctionException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;

        if (id <= 0) {
            throw new NoSuchCustomerException("There's no such customer in the system with ID: " + id);
        }
        try {
            ps = connection.prepareStatement(Schema.DELETE_CUSTOMER_BY_ID);
            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new RemoveCustomerException("No customer has been removed");
            }
        } catch (SQLException | RemoveCustomerException e) {
            throw new SystemMalFunctionException("Unable to remove Customer with ID: " + id + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new SystemMalFunctionException("Unable to close statement " + e.getMessage());
            }
        }
    }

    @Override
    public void updateCustomer(Customer customer) throws SystemMalFunctionException, NoSuchCustomerException {

        long id = customer.getId();
        if (id <= 0) {
            throw new NoSuchCustomerException("No such customer in DB!");
        }
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(Schema.UPDATE_CUSTOMER);
            upDateCustomerValuesOnStatement(customer, ps);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new NoSuchCustomerException("Unable to update customer!");
            }
        } catch (SQLException | NoSuchCustomerException e) {
            throw new SystemMalFunctionException("There was a problem to update the customer. " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new SystemMalFunctionException("Unable to close statement" + e.getMessage());
                }
            }
        }
    }

    /**
     * Method that sets the updated values of the customer .
     *
     * @param customer The updated customer
     * @param ps       The prepared statement
     * @throws SQLException If fail to place the values.
     */

    public void upDateCustomerValuesOnStatement(Customer customer, PreparedStatement ps) throws SQLException {
        ps.setString(1, customer.getFirstName());
        ps.setString(2, customer.getLastName());
        ps.setString(3, customer.getEmail());
        ps.setString(4, customer.getPassword());
        ps.setLong(5, customer.getId());
    }

    @Override
    public Customer getCustomer(long id) throws SystemMalFunctionException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        Customer customer = null;
        try {
            ps = connection.prepareStatement(Schema.SELECT_CUSTOMER_BY_ID);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                customer = resultSetToCustomer(rs);
                customer.setCoupons(getCoupons(rs.getInt(1)));
            } else {
                throw new NoSuchCustomerException("No customer in DB!");
            }
        } catch (SQLException | NoSuchCustomerException e) {
            throw new SystemMalFunctionException("No customer has been found with this id: " + id + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new SystemMalFunctionException("Unable to close Prepared Statement" + e.getMessage());
                }
            }

        }
        return customer;
    }

    /**
     * Method that sets the values of the customer .
     *
     * @param rs The result Set
     * @return 'Customer' object.
     * @throws SQLException If fail to place the values.
     */

    public Customer resultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong(1));
        customer.setFirstName(rs.getString(2));
        customer.setLastName(rs.getString(3));
        customer.setEmail(rs.getString(4));
        customer.setPassword(rs.getString(5));
        return customer;
    }

    @Override
    public Collection<Customer> getAllCustomer() throws SystemMalFunctionException, SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        Collection<Customer> customerCollection;

        try {
            ps = connection.prepareStatement(Schema.SELECT_ALL_CUSTOMER_ID);
            customerCollection = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long id = rs.getLong(1);
                Customer customer = getCustomer(id);
                customerCollection.add(customer);
            }
        } catch (SystemMalFunctionException e) {
            throw new SystemMalFunctionException("Unable to create Collection of Customers " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ps.close();
        }
        return customerCollection;
    }

    @Override
    public Set<Coupon> getCoupons(long customerId) throws SystemMalFunctionException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        Set<Coupon> coupons = new HashSet<>();

        try {
            ps = connection.prepareStatement(Schema.SELECT_COUPON_BY_CUSTOMER_ID);
            ps.setLong(1, customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                coupons.add(getCustomerCoupon(rs));
            }
        } catch (SQLException e) {
            throw new SystemMalFunctionException("Unable to return coupons of customer " + customerId + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new SystemMalFunctionException("Unable to close Prepared Statement!" + e.getMessage());
                }
            }
        }
        return coupons;
    }

    public Coupon getCustomerCoupon(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(rs.getLong(1));
        coupon.setCompanyId(rs.getInt(2));
        coupon.setCategory(rs.getInt(3));
        coupon.setTitle(rs.getString(4));
        coupon.setStartDate(rs.getDate(5));
        coupon.setEndDate(rs.getDate(6));
        coupon.setAmount(rs.getInt(7));
        coupon.setDescription(rs.getString(8));
        coupon.setPrice(rs.getDouble(9));
        coupon.setImage(rs.getString(10));
        return coupon;
    }

    @Override
    public void insertCustomerCoupon(long couponId, long customerId) throws SystemMalFunctionException,
            NoSuchCouponException,
            NoSuchCustomerException, ReturnCouponsException {

        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        CouponDBDao couponDBDao = new CouponDBDao();

        if (couponDBDao.getCoupon(couponId) == null) {
            throw new NoSuchCouponException("There is no such coupon with id: " + couponId);
        }

        if (getCustomer(customerId) == null) {
            throw new NoSuchCustomerException("There is no such customer with id: " + customerId);
        }

        try {
            ps = connection.prepareStatement(Schema.INSERT_CUSTOMER_AND_COUPON_BY_ID);
            ps.setLong(1, couponId);
            ps.setLong(2, customerId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new CouponAlreadyPurchasedException("Coupon Already Purchased! ");
            }


        } catch (SQLException | CouponAlreadyPurchasedException e) {
            throw new SystemMalFunctionException("Unable to insert detail to table customer_coupon!" + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new SystemMalFunctionException("Un able to close Prepared Statement" + e.getMessage());
            }
        }
    }

    @Override
    public Customer login(String email, String password) throws SystemMalFunctionException, InvalidLoginException, SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement(Schema.LOGIN_CUSTOMER_BY_EMAIL_AND_PASSWORD);
        Customer customer;
        try {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                customer = resultSetToCustomer(rs);
            } else {
                throw new InvalidLoginException(String.format("Invalid login with email: %s and password: %s", email, password));
            }
        } catch (InvalidLoginException | SQLException e) {
            throw new InvalidLoginException(String.format("Invalid login with email: %s and password: %s", email, password));
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new SystemMalFunctionException("Un able to close Prepared Statement" + e.getMessage());
            }
        }
        return customer;
    }
}
