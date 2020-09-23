package data.db;

import common.ConnectionPool;
import common.ex.SystemMalFunctionException;
import data.ex.InvalidLoginException;
import data.ex.NoSuchCompanyException;
import data.ex.ReturnCouponsException;
import models.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class CompanyDBDao implements data.dao.CompanyDao {


    @Override
    public long createCompany(Company company) throws SystemMalFunctionException {
        //get a connection.
        Connection connection = ConnectionPool.getInstance().getConnection();
        // get statement
        PreparedStatement ps = null;
        long companyId = -1;
        try {
            ps = connection.prepareStatement(Schema.INSERT_COMPANY, ps.RETURN_GENERATED_KEYS);
            //applyCompanyValueOnStatement
            applyCompanyValueOnStatement(ps, company);
            //executeUpdate
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                companyId = rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new SystemMalFunctionException("There was a problem creating company: " + e.getMessage());
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
        return companyId;
    }

    /**
     * Method that sets the values of the company.
     *
     * @param ps      The prepared statement
     * @param company The Company to create.
     * @throws SQLException If fail to place the values.
     */
    private void applyCompanyValueOnStatement(PreparedStatement ps, Company company) throws SQLException {
        ps.setString(1, company.getName());
        ps.setString(2, company.getEmail());
        ps.setString(3, company.getPassword());
    }

    @Override
    public void removeCompany(long id) throws NoSuchCompanyException, SystemMalFunctionException {
        /*Check if id is valid*/
        if (id <= 0) {
            throw new NoSuchCompanyException("Unable to remove the company" + id);
        }
        /*Get a connection*/
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        try {
            /*prepare a statement*/
            ps = connection.prepareStatement(Schema.DELETE_COMPANY);
            ps.setLong(1, id);
            /*executeUpdate*/
            int affectedRow = ps.executeUpdate();
            if (affectedRow == 0) {
                throw new NoSuchCompanyException("Unable to remove company with id: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    public void updateCompany(Company company) throws SystemMalFunctionException, NoSuchCompanyException {

        long id = company.getId();

        if (id <= 0) {
            throw new NoSuchCompanyException("Unable to update company with id: " + id);
        }

        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(Schema.UPDATE_COMPANY);
            applyCompanyValuesOnStatement(ps, company);
            ps.setLong(4, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new NoSuchCompanyException("Unable to update company with id: " + id);
            }
        } catch (SQLException e) {
            throw new SystemMalFunctionException("There was a problem updating a company: " + e.getMessage());
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

    /**
     * Method that sets the updated values of the company.
     *
     * @param ps      The prepared statement
     * @param company The updated Company .
     * @throws SQLException If fail to place the values.
     */
    private void applyCompanyValuesOnStatement(PreparedStatement ps, Company company) throws SQLException {
        ps.setString(1, company.getName());
        ps.setString(2, company.getEmail());
        ps.setString(3, company.getPassword());
    }

    @Override
    public Company getCompany(long id) throws SystemMalFunctionException, NoSuchCompanyException {

        if (id <= 0) {
            throw new NoSuchCompanyException("Unable to get company with id: " + id);
        }

        Connection connection = ConnectionPool.getInstance().getConnection();
        Company company;
        PreparedStatement ps = null;
        CouponDBDao couponDBDao = new CouponDBDao();
        try {
            ps = connection.prepareStatement(Schema.SELECT_COMPANY_BY_ID);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                company = resultSetToCompany(rs);
                company.setCoupons(couponDBDao.getCoupons(id));

            } else {
                throw new NoSuchCompanyException("Unable to get company with id: " + id);
            }

        } catch (SQLException | ReturnCouponsException e) {
            throw new SystemMalFunctionException("Unable to get company: " + e.getMessage());
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

        return company;
    }

    /**
     * Method that sets the values of the company.
     *
     * @param rs The result Set
     * @return 'Company' object.
     * @throws SQLException If fail to place the values.
     */
    private static Company resultSetToCompany(ResultSet rs) throws SQLException {
        long id = rs.getLong(1);
        String name = rs.getString(2);
        String email = rs.getString(3);
        String password = rs.getString(4);

        return new Company(id, name, email, password);
    }

    @Override
    public Collection<Company> getAllCompanies() throws SystemMalFunctionException, NoSuchCompanyException {
        /*1 Get the ids of all the companies*/
        Connection connection = ConnectionPool.getInstance().getConnection();
        // select id from company
        Collection<Company> companies;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(Schema.SELECT_ID_COMPANIES);

            companies = new ArrayList<>();

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                long id = rs.getLong(1);
                /*2 for each id, call getCompany(id)*/
                Company company = getCompany(id);
                /*3 insert to company into the collection*/
                companies.add(company);
            }
        } catch (SQLException | NoSuchCompanyException e) {
            throw new NoSuchCompanyException("Failed getting all companies: " + e.getMessage());
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

        /*4 return the collection.*/
        return companies;
    }

    @Override
    public Company login(String email, String password) throws SystemMalFunctionException, InvalidLoginException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        /*select * from company where email=? and password=?*/
        PreparedStatement ps = null;
        Company company;
        CouponDBDao couponDBDao = new CouponDBDao();

        try {
            ps = connection.prepareStatement(Schema.SELECT_COMPANY_BY_EMAIL_AND_PASSWORD);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                company = resultSetToCompany(rs);
                company.setCoupons(couponDBDao.getCoupons(company.getId()));
            } else {
                throw new InvalidLoginException(String.format("Invalid login with %s, %s", email, password));
            }
        } catch (SQLException | ReturnCouponsException e) {
            throw new InvalidLoginException(String.format("Invalid login with %s, %s", email, password));
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

        return company;
    }
}
