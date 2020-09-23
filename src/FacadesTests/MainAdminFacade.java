package FacadesTests;

import common.LoginType;
import common.ex.SystemMalFunctionException;
import data.ex.*;
import facade.AbsFacade;
import facade.AdminFacade;
import models.Company;
import models.Customer;

import java.sql.SQLException;

public class MainAdminFacade {
    public static void main(String[] args) {

        Company company = new Company();
        company.setId(66);
        company.setName("Admin Facade ---V");
        company.setCoupons(null);
        company.setEmail("Admin_Facade_---V222@gmail.com");
        company.setPassword("Admin_Password ---V222");

        Customer customer = new Customer();
        customer.setFirstName("First Name---V ");
        customer.setLastName("Last Name---V");
        customer.setEmail("Admin_Facade2233@gmail.com---V2");
        customer.setPassword("Admin_Facade_Password2233---V2");
        customer.setId(198);


        try {
            AdminFacade adminFacade = (AdminFacade) AbsFacade.login("admin", "1234", LoginType.ADMIN);

            /*Companies methods*/
            System.out.println("Method - Create company:");
            adminFacade.createCompany(company);


            /* Add the ID company from the DB. */
            System.out.println("Method - Remove company:");
            adminFacade.removeCompany(65);


            System.out.println("Method - Update company:");
            /*Change the company id (Line 17)*/
            adminFacade.updateCompany(company);


            System.out.println("Method - Get all companies:");
            System.out.println(adminFacade.getAllCompanies());


            /*Add the ID company from the DB*/
            System.out.println("Method - Get company:");
            System.out.println(adminFacade.getCompany(66));


            /*Customer methods*/

            /*Create customer: */
            System.out.println("Method - Create customer:");
            adminFacade.createCustomer(customer);


            /*Update customer: */
            /*Change the company id (Line 28)*/
            System.out.println("Method - Update customer:");
            adminFacade.updateCustomer(customer);

            /*Remove customer: */
            /*Add the ID customer from the DB to the requested company*/
            System.out.println("Method - Remove customer:");
            adminFacade.removeCustomer(196);

            /*Get all customers: */
            System.out.println("Method - Get all customers:");
            System.out.println(adminFacade.getAllCustomers());

            /*Get customer: */

            /*Add the ID customer from the DB*/
            System.out.println("Method - Get customer:");
            System.out.println(adminFacade.getCustomer(198));

        } catch (SystemMalFunctionException | InvalidLoginException | SQLException | NoSuchCustomerException | NoSuchCompanyException | ReturnCouponsException | UpdateCompanyException | CustomerAlreadyExists | CustomerUpdateException e) {
            e.printStackTrace();
        }

    }
}
