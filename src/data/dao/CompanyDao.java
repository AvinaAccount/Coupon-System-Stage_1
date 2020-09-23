package data.dao;

import common.ex.SystemMalFunctionException;
import data.ex.InvalidLoginException;
import data.ex.NoSuchCompanyException;
import models.Company;

import java.util.Collection;

/**
 * Basic company interface that designed to perform basic operations on the DB
 */
public interface CompanyDao {
    /**
     * Method that create new company in DB.
     *
     * @param company Object type 'Company' to insert into DB.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     */
    long createCompany(Company company) throws SystemMalFunctionException;

    /**
     * Method that remove new company in DB.
     *
     * @param id To identify the company.
     * @throws NoSuchCompanyException     If the company not exist.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     */
    void removeCompany(long id) throws NoSuchCompanyException, SystemMalFunctionException;

    /**
     * Method that update the company by getting object type 'Company'.
     *
     * @param company Update company by object type 'Company'.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws NoSuchCompanyException     If unable to update the company .
     */
    void updateCompany(Company company) throws SystemMalFunctionException, NoSuchCompanyException;

    /**
     * Method that return company.
     *
     * @param id To identify the company.
     * @return Object type 'Company'.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws NoSuchCompanyException     If the argument "companyId" is less than 0 or equal.
     */
    Company getCompany(long id) throws SystemMalFunctionException, NoSuchCompanyException;

    /**
     * Method that return all the companies from DB.
     *
     * @return Object type 'Collection' of all companies.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws NoSuchCompanyException     If failed getting all companies.
     */
    Collection<Company> getAllCompanies() throws SystemMalFunctionException, NoSuchCompanyException;

    /**
     * Login method as a company by email and password
     *
     * @param email    To identify the company.
     * @param password To identify the company.
     * @return Object type 'Company'.
     * @throws SystemMalFunctionException If there's a general problem with the system's functioning.
     * @throws InvalidLoginException      If the login password or username is incorrect.
     */
    Company login(String email, String password) throws SystemMalFunctionException, InvalidLoginException;
}