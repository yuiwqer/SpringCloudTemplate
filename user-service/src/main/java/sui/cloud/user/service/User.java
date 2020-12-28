package sui.cloud.user.service;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Data
@Entity
public class User implements UserDetails {
    //必须
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true,nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    //可选
    private String nickname;
    @Column(columnDefinition = "mediumtext")
    private String avatar;
    //security基本
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)//热更新,级联保存
    private List<Role> roles;
    @Column(nullable = false)
    private Boolean AccountNonExpired=true;
    @Column(nullable = false)
    private Boolean AccountNonLocked=true;
    @Column(nullable = false)
    private Boolean CredentialsNonExpired=true;
    @Column(nullable = false)
    private Boolean Enabled=true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return AccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return AccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return CredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return Enabled;
    }
}
