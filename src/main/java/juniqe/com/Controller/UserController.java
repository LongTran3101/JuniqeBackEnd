package juniqe.com.Controller;

import jakarta.validation.Valid;
import juniqe.com.entity.UserEntity;
import juniqe.com.model.DomainCode;
import juniqe.com.model.ResponseDTO;
import juniqe.com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController extends  BaseController{

    private final UserService userService;

    // Lấy danh sách user có phân trang
    @GetMapping
    public ResponseEntity<Page<UserEntity>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return pageSuccess(userService.getAllUsers(pageable));
    }

    // Lấy user theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        return success(userService.getUserById(id));
    }

    // Lấy user theo username
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return success(userService.getUserByUsername(username));
    }

    // Tạo user mới
    @PostMapping
    public ResponseEntity<ResponseDTO<UserEntity>> createUser(@Valid @RequestBody UserEntity user) {
        return success(userService.createUser(user));

    }

    // Cập nhật user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserEntity updatedUser) {
        return success();
    }

    // Xóa user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
            userService.deleteUser(id);
            return success();
    }
}
