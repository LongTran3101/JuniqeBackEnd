package juniqe.com.service;

import juniqe.com.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserEntity> getAllUsers(Pageable pageable, String username, String email);
    UserEntity getUserById(Integer id);
    UserEntity getUserByUsername(String username);
    UserEntity createUser(UserEntity user);
    UserEntity registerUser(String username,String password,String email);
    UserEntity updateUser(Integer id, UserEntity user);
    void deleteUser(Integer id);
}
