package juniqe.com.model;

import juniqe.com.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {
    private String accessToken;
    private UserEntity userEntity;
}
