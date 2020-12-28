package sui.cloud.user.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String role;

    public Role(String role){
        this.role=role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
