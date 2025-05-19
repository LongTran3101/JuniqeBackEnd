package juniqe.com.service;

import juniqe.com.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserEntity> getAllUsers(Pageable pageable);
    UserEntity getUserById(Integer id);
    UserEntity getUserByUsername(String username);
    UserEntity createUser(UserEntity user);
    UserEntity updateUser(Integer id, UserEntity user);
    void deleteUser(Integer id);
}
