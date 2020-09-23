package data.db;

import common.ConnectionPool;
import common.ex.SystemMalFunctionException;
import data.dao.CouponDao;
import data.ex.CouponException;
import data.ex.NoSuchCouponException;
import data.ex.ReturnCouponsException;
import data.ex.ZeroCouponAmountException;
import models.Coupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CouponDBDao implements CouponDao {
    @Override
    public long createCoupon(Coupon coupon) throws SystemMalFunctionException, CouponException, ReturnCouponsException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        long couponId = -1;

        Collection<Coupon> coupons = getAllCoupon();
        for (Coupon couponType : coupons) {
            String couponTypeTitle = couponType.getTitle();
            String couponTitle = coupon.getTitle();

            if (couponTypeTitle.equals(couponTitle)) {
                throw new CouponException("Cannot create a coupon with the same title!");
            }
        }

        try {
            ps = connection.prepareStatement(Schema.INSERT_COUPON, ps.RETURN_GENERATED_KEYS);
            applyCouponValuesOnStatement(ps, coupon);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                couponId = rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new SystemMalFunctionException("There was a problem creating coupon: " + e.getMessage());
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
        return couponId;
    }

    /**
     * Method that sets the values of the coupon.
     *
     * @param ps     The prepared statement
     * @param coupon The Coupon to create.
     * @throws SQLException If fail to place the values.
     */

    private void applyCouponValuesOnStatement(PreparedStatement ps, Coupon coupon) throws SQLException {

        ps.setInt(1, coupon.getCompanyId());
        ps.setInt(2, coupon.getCategory());
        ps.setString(3, coupon.getTitle());
        ps.setDate(4, coupon.getStartDate());
        ps.setDate(5, coupon.getEndDate());
        ps.setInt(6, coupon.getAmount());
        ps.setString(7, coupon.getDescription());
        ps.setDouble(8, coupon.getPrice());
        ps.setString(9, coupon.getImage());

    }

    @Override
    public void removeCoupon(long id) throws SystemMalFunctionException, NoSuchCouponException {

        if (id <= 0) {
            throw new NoSuchCouponException("No such coupon exception with id: " + id);
        }

        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(Schema.DELETE_COUPON);
            ps.setLong(1, id);
            int affectedRow = ps.executeUpdate();
            if (affectedRow == 0) {
                throw new NoSuchCouponException("No such coupon in the DB");
            }
        } catch (SQLException e) {
            throw new SystemMalFunctionException("Unable to remove coupon from DB" + e.getMessage());
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
    public void updateCoupon(Coupon coupon) throws NoSuchCouponException, SystemMalFunctionException {

        if (coupon.getId() <= 0) {
            throw new NoSuchCouponException("No such coupon in DB!");
        }

        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(Schema.UPDATE_COUPON);
            upDateCouponOnStatement(coupon, ps);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SystemMalFunctionException("Unable to update coupon");
            }
        } catch (SQLException e) {
            throw new SystemMalFunctionException("Unable to update coupon!" + e.getMessage());
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
     * Method that sets the update values of the coupon.
     *
     * @param coupon The updated Coupon .
     * @param ps     The prepared statement.
     * @throws SQLException If fail to place the values.
     */

    private void upDateCouponOnStatement(Coupon coupon, PreparedStatement ps) throws SQLException {
        ps.setInt(1, coupon.getCategory());
        ps.setString(2, coupon.getTitle());
        ps.setDate(3, coupon.getStartDate());
        ps.setDate(4, coupon.getEndDate());
        ps.setInt(5, coupon.getAmount());
        ps.setString(6, coupon.getDescription());
        ps.setDouble(7, coupon.getPrice());
        ps.setString(8, coupon.getImage());
        ps.setLong(9, coupon.getId());

    }

    @Override
    public void decrementCouponAmount(long id) throws SystemMalFunctionException, ZeroCouponAmountException, NoSuchCouponException, ReturnCouponsException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;

        Coupon coupon = getCoupon(id);
        int amount = coupon.getAmount();
        if (amount <= 0) {
            throw new ZeroCouponAmountException("Coupon stock is over!");
        }

        try {
            ps = connection.prepareStatement(Schema.UPDATE_NEW_AMOUNT_OF_COUPON_BY_ID);
            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new ZeroCouponAmountException("Unable to decrement coupon amount! " + e.getMessage());
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
    public Coupon getCoupon(long id) throws SystemMalFunctionException, ReturnCouponsException, NoSuchCouponException {

        if (id <= 0) {
            throw new ReturnCouponsException("Invalid coupon id:" + id);
        }

        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        Coupon coupon = null;

        try {
            ps = connection.prepareStatement(Schema.SELECT_COUPON_BY_ID);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                coupon = setCouponValues(rs);
            }

            if (coupon == null) {
                throw new NoSuchCouponException("The coupon not exist in DB");
            }

        } catch (SQLException e) {
            throw new SystemMalFunctionException("Unable to get the coupon from DB!" + e.getMessage());
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

        return coupon;
    }

    /**
     * Method that sets the values of the coupon.
     *
     * @param rs The result Set
     * @return 'Coupon' object.
     * @throws SQLException If fail to place the values.
     */

    private Coupon setCouponValues(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(rs.getInt(1));
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
    public Set<Coupon> getAllCoupon() throws SystemMalFunctionException, ReturnCouponsException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        Set<Coupon> coupons = new HashSet<>();

        try {
            ps = connection.prepareStatement(Schema.SELECT_ALL_COUPONS);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                coupons.add(getCoupon(rs.getInt(1)));
            }
        } catch (SQLException | NoSuchCouponException e) {
            throw new ReturnCouponsException("Unable to get all coupons" + e.getMessage());
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

        return coupons;
    }

    @Override
    public Collection<Coupon> getCouponsByCategory(int category) throws SystemMalFunctionException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = null;
        Set<Coupon> coupons = new HashSet<>();

        try {
            ps = connection.prepareStatement(Schema.SELECT_COUPONS_BY_CATEGORY);
            ps.setInt(1, category);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                coupons.add(getCoupon(rs.getInt(1)));
            }
        } catch (SQLException | ReturnCouponsException | NoSuchCouponException e) {
            throw new SystemMalFunctionException("Unable to get coupons by category" + e.getMessage());
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
        return coupons;
    }

    @Override
    public Set<Coupon> getCoupons(long companyId) throws SystemMalFunctionException, ReturnCouponsException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Set<Coupon> coupons = new HashSet<>();
        PreparedStatement ps = null;
        try {

            ps = connection.prepareStatement(Schema.SELECT_COUPONS_BY_COMPANY_ID);
            ps.setLong(1, companyId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                coupons.add(resultSetToCoupon(rs));
            }
        } catch (SQLException e) {
            throw new ReturnCouponsException("Unable to get company's coupons:" + e.getMessage());
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
        return coupons;
    }

    /**
     * Method that sets the values of the coupon.
     *
     * @param rs The result Set
     * @return 'Coupon' object.
     * @throws SQLException If fail to place the values.
     */

    private static Coupon resultSetToCoupon(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(rs.getInt(1));
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
}
