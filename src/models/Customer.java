package models;

import java.util.Set;

public class Customer {
    private long id = -1;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Set<Coupon> coupons;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(Set<Coupon> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}' + "\n";
    }

//    public boolean equals(Coupon coupon) {
//        Boolean couponStatus = false;
//        long couponId = coupon.getId();
//        for (int i = 0; i < coupons.size(); i++) {
//            if (coupons.iterator().next().getId() == couponId) {
//                couponStatus = true;
//            }
//        }
//        return couponStatus;
//    }


}
