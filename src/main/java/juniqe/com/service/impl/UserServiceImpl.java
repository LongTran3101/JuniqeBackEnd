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
    public Page<UserEntity> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
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
    public UserEntity updateUser(Integer id, UserEntity user) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        existingUser.setEnabled(user.getEnabled());
        existingUser.setCreatedBy(user.getCreatedBy());
        existingUser.setVipExpiredAt(user.getVipExpiredAt());
        existingUser.setRefreshToken(user.getRefreshToken());
        existingUser.setLastLogin(user.getLastLogin());

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
