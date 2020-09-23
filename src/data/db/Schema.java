package data.db;

/**
 * This class defines the DB , and executing SQL queries.
 */

public class Schema {

    //Tables.
    private static final String TABLE_NAME_COMPANY = "company";
    private static final String TABLE_NAME_COUPON = "coupon";
    private static final String TABLE_NAME_CUSTOMER = "customer";
    private static final String TABLE_NAME_CUSTOMER_COUPON = "customer_coupon";
    //Company columns.
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    //Coupon columns
    public static final String COL_COUPON_ID = "id";
    private static final String COL_COMPANY_ID = "company_id";
    private static final String COL_CATEGORY = "category";
    private static final String COL_TITLE = "title";
    private static final String COL_START_DATE = "start_date";
    private static final String COL_END_DATE = "end_date";
    private static final String COL_AMOUNT = "amount";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_PRICE = "price";
    private static final String COL_IMAGE = "image";
    //Customer columns
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";
    private static final String COL_CUSTOMER_EMAIL = "email";
    private static final String COL_CUSTOMER_PASSWORD = "password";
    //Customer_coupon columns
    public static final String COL_COUPON_IN_TABLE_CUSTOMER_COUPON_ID = "coupon_id";
    public static final String COL_CUSTOMER_ID = "customer_id";


    public static final String INSERT_COMPANY = "INSERT INTO "
            + TABLE_NAME_COMPANY + "("
            + COL_NAME + ","
            + COL_EMAIL + ","
            + COL_PASSWORD
            + ") VALUES (?,?,?)";


    public static final String INSERT_COUPON = "INSERT INTO "
            + TABLE_NAME_COUPON
            + " (" + COL_COMPANY_ID
            + "," + COL_CATEGORY
            + "," + COL_TITLE
            + "," + COL_START_DATE
            + "," + COL_END_DATE
            + "," + COL_AMOUNT
            + "," + COL_DESCRIPTION
            + "," + COL_PRICE
            + "," + COL_IMAGE
            + ") VALUES (?,?,?,?,?,?,?,?,?)";

    public static final String INSERT_CUSTOMER = "INSERT INTO "
            + TABLE_NAME_CUSTOMER + "("
            + COL_FIRST_NAME + ","
            + COL_LAST_NAME + ","
            + COL_CUSTOMER_EMAIL + ","
            + COL_CUSTOMER_PASSWORD
            + ") VALUES (?,?,?,?)";

    public static final String DELETE_COMPANY = "DELETE FROM "
            + TABLE_NAME_COMPANY + " WHERE " + COL_ID
            + " =?";

    public static final String UPDATE_CUSTOMER = "UPDATE " +
            TABLE_NAME_CUSTOMER + " "
            + "SET "
            + COL_FIRST_NAME + " = ?,"
            + COL_LAST_NAME + " = ?,"
            + COL_EMAIL + " = ?,"
            + COL_PASSWORD + " = ? "
            + "WHERE " + COL_ID + " = ?";

    public static final String SELECT_CUSTOMER_BY_ID = "SELECT * FROM "
            + TABLE_NAME_CUSTOMER
            + " WHERE id = ?";

    public static final String SELECT_ALL_CUSTOMER_ID = "SELECT * FROM " + TABLE_NAME_CUSTOMER;

    public static final String SELECT_COUPON_BY_CUSTOMER_ID = "SELECT * FROM "
            + TABLE_NAME_COUPON
            + " INNER JOIN " + TABLE_NAME_CUSTOMER_COUPON +
            " ON " + TABLE_NAME_COUPON + "." + COL_ID + " = " + TABLE_NAME_CUSTOMER_COUPON + ".coupon_id "
            + " where " + TABLE_NAME_CUSTOMER_COUPON + ".customer_id = ?";

    public static final String INSERT_CUSTOMER_AND_COUPON_BY_ID = "INSERT INTO "
            + TABLE_NAME_CUSTOMER_COUPON
            + " (" + COL_COUPON_IN_TABLE_CUSTOMER_COUPON_ID
            + "," + COL_CUSTOMER_ID
            + ")" +
            " VALUES (?,?)";

    public static final String LOGIN_CUSTOMER_BY_EMAIL_AND_PASSWORD = "SELECT * FROM " + TABLE_NAME_CUSTOMER
            + " WHERE " + COL_EMAIL + " = ? "
            + "AND " + COL_PASSWORD + " = ? ";

    public static final String DELETE_COUPON = "DELETE FROM "
            + TABLE_NAME_COUPON + " WHERE " + COL_ID
            + " = ? ";

    public static final String DELETE_CUSTOMER_BY_ID = "DELETE FROM " + TABLE_NAME_CUSTOMER
            + " WHERE " + COL_ID + " = ?";

    public static final String UPDATE_COUPON = "UPDATE "
            + TABLE_NAME_COUPON
            + " SET "
            + COL_CATEGORY + "=?,"
            + COL_TITLE + "=?,"
            + COL_START_DATE + "=?,"
            + COL_END_DATE + "=?,"
            + COL_AMOUNT + "=?,"
            + COL_DESCRIPTION + "=?,"
            + COL_PRICE + "=?,"
            + COL_IMAGE + "=? "
            + "WHERE " + COL_COUPON_ID + " = ?";

    public static final String UPDATE_COMPANY = "UPDATE " + TABLE_NAME_COMPANY +
            " SET "
            + COL_NAME + " = ?, "
            + COL_EMAIL + " = ? , "
            + COL_PASSWORD + " = ? "
            + "WHERE "
            + COL_ID + " = ? ";

    public static final String UPDATE_NEW_AMOUNT_OF_COUPON_BY_ID = "UPDATE "
            + TABLE_NAME_COUPON + " SET "
            + TABLE_NAME_COUPON + "." + COL_AMOUNT + " = "
            + TABLE_NAME_COUPON + "." + COL_AMOUNT + " -1 "
            + "WHERE " + COL_COUPON_ID + " = ?";

    public static final String SELECT_COUPON_BY_ID = "SELECT * FROM "
            + TABLE_NAME_COUPON
            + " WHERE " + COL_COUPON_ID + " = ?";

    public static final String SELECT_ALL_COUPONS = "SELECT * FROM " + TABLE_NAME_COUPON;

    public static final String SELECT_COUPONS_BY_CATEGORY = "SELECT * FROM "
            + TABLE_NAME_COUPON
            + " WHERE " + COL_CATEGORY + " = ?";

    public static final String SELECT_COUPONS_BY_COMPANY_ID = "SELECT * FROM " + TABLE_NAME_COUPON
            + " WHERE " + COL_COMPANY_ID + " = ? ";
    ;

    public static final String SELECT_COMPANY_BY_ID = "SELECT * FROM " + TABLE_NAME_COMPANY
            + " WHERE " + COL_ID + " = ? ";

    public static final String SELECT_ID_COMPANIES = "SELECT " + COL_ID + " FROM " + TABLE_NAME_COMPANY;

    public static final String SELECT_COMPANY_BY_EMAIL_AND_PASSWORD = "SELECT * FROM "
            + TABLE_NAME_COMPANY
            + " WHERE "
            + COL_EMAIL + "=?" +
            " AND " +
            COL_PASSWORD + "=?";

    public static final String UPDATE_COMPANY_AS_ADMIN = "UPDATE "
            + TABLE_NAME_COMPANY + " SET "
            + TABLE_NAME_COMPANY + "." + COL_EMAIL + " = ?, "
            + TABLE_NAME_COMPANY + "." + COL_PASSWORD + "= ?"
            + "WHERE " + TABLE_NAME_COMPANY + "." + COL_ID + "=?";

    public static final String UPDATE_CUSTOMER_BY_ADMIN_FACADE = "UPDATE "
            + TABLE_NAME_CUSTOMER + " SET "
            + TABLE_NAME_CUSTOMER + "." + COL_EMAIL + " = ? ,"
            + TABLE_NAME_CUSTOMER + "." + COL_PASSWORD + " = ?"
            + " WHERE " + COL_ID + " = ?";

    public static final String UPDATE_COUPON_BY_COMPANY_FACADE = "UPDATE "
            + TABLE_NAME_COUPON
            + " SET "
            + COL_CATEGORY + "=?,"
            + COL_START_DATE + "=?,"
            + COL_END_DATE + "=?,"
            + COL_AMOUNT + "=?,"
            + COL_DESCRIPTION + "=?,"
            + COL_PRICE + "=?,"
            + COL_IMAGE + "=? "
            + "WHERE " + COL_COUPON_ID + " = ?";
}
