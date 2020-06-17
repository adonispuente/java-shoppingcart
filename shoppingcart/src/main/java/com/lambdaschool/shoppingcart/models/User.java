package com.lambdaschool.shoppingcart.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User
        extends Auditable
{
    @ApiModelProperty(name = "user id",
            value = "primary key for User",
            required = true,
            example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userid;


    /**
     * The username (String). Cannot be null and must be unique
     */
    @ApiModelProperty(name = "User Name",
            value = "Actual user name for sign on",
            required = true,
            example = "Some Name")
    @Size(min = 2,
            max = 30,
            message = "User Name must be between 2 and 30 characters")
    @NotNull
    @Column(nullable = false,
            unique = true)
    private String username;


    /**
     * The password (String) for this user. Cannot be null. Never get displayed
     */
    @ApiModelProperty(name = "password",
            value = "The password for this user",
            required = true,
            example = "ILuvM4th!")
    @Size(min = 4,
            message = "Password must 4 or more characters")
//    @NotNull
    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    private String comments;



    @ApiModelProperty(name = "cart",
            value = "The cart a user has")
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "user",
            allowSetters = true)
    private List<Cart> carts = new ArrayList<>();


    /**
     * Part of the join relationship between user and role
     * connects users to the user role combination
     */
    @ApiModelProperty(name = "roles",
            value = "List of user roles for this users")
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "user",
            allowSetters = true)
    private List<UserRoles> roles = new ArrayList<>();

    public User()
    {

    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public long getUserid()
    {
        return userid;
    }

    public void setUserid(long userid)
    {
        this.userid = userid;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    public List<Cart> getCarts()
    {
        return carts;
    }

    public void setCarts(List<Cart> carts)
    {
        this.carts = carts;
    }

    @JsonIgnore
    public List<SimpleGrantedAuthority> getAuthority()
    {
        List<SimpleGrantedAuthority> rtnList = new ArrayList<>();

        for (UserRoles r : this.roles)
        {
            String myRole = "ROLE_" + r.getRole()
                    .getName()
                    .toUpperCase();
            rtnList.add(new SimpleGrantedAuthority(myRole));
        }

        return rtnList;
    }
}
