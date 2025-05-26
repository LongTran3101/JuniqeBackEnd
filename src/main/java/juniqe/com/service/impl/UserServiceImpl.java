package juniqe.com.service.impl;

import juniqe.com.entity.UserEntity;
import juniqe.com.repositories.UserEntityRepository;
import juniqe.com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserEntityRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserEntity> getAllUsers(Pageable pageable, String username, String email) {
        // Nếu username hoặc email null, gán chuỗi rỗng để tìm all
        String usernameFilter = (username == null) ? "" : username;
        String emailFilter = (email == null) ? "" : email;
        Page<UserEntity> usersPage = userRepository
                .findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(usernameFilter, emailFilter, pageable);
        // Set password về rỗng trước khi trả về
        usersPage.getContent().forEach(user -> user.setPassword(""));
        return usersPage;
    }

    @Override
    public UserEntity getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserEntity registerUser(String username, String password, String email) {
        UserEntity user = new UserEntity();
        user.setEnabled(false);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("user");
        user.setStatus(1);
        return userRepository.save(user);
    }

    @Override
    public UserEntity updateUser(Integer id, UserEntity user) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        existingUser.setEnabled(user.getEnabled());
        existingUser.setVipExpiredAt(user.getVipExpiredAt());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
