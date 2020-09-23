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
        company.setId("Implement!");
        company.setName("Implement!");
        company.setCoupons(null);
        company.setEmail("Implement!");
        company.setPassword("Implement!");

        Customer customer = new Customer();
        customer.setFirstName("Implement! ");
        customer.setLastName("Implement!");
        customer.setEmail("Implement!");
        customer.setPassword("Implement!");
        customer.setId("Implement!");


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
            System.out.println(adminFacade.getCompany("Implement!"));


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
            adminFacade.removeCustomer("Implement!");

            /*Get all customers: */
            System.out.println("Method - Get all customers:");
            System.out.println(adminFacade.getAllCustomers());

            /*Get customer: */

            /*Add the ID customer from the DB*/
            System.out.println("Method - Get customer:");
            System.out.println(adminFacade.getCustomer("Implement!"));

        } catch (SystemMalFunctionException | InvalidLoginException | SQLException | NoSuchCustomerException | NoSuchCompanyException | ReturnCouponsException | UpdateCompanyException | CustomerAlreadyExists | CustomerUpdateException e) {
            e.printStackTrace();
        }

    }
}
