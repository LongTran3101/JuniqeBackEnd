package juniqe.com.Controller;

import juniqe.com.config.JwtUtil;
import juniqe.com.model.LoginDTO;
import juniqe.com.entity.UserEntity;
import juniqe.com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity user){
        return  success(userService.registerUser(user.getUsername(),user.getPassword(),user.getEmail()));
    }
    // Demo login đơn giản, user/pass cố định
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user, HttpServletResponse response) {
        String username = user.get("username");
        String password = user.get("password");

        // Thay bằng service kiểm tra user/pass thật
        UserEntity userinfo= userService.getUserByUsername(username);
        if(userinfo!=null  && userinfo.getId()!=null)
        {
            if (userinfo != null && passwordEncoder.matches(password, userinfo.getPassword()) && userinfo.getStatus()==1 && userinfo.getEnabled()) {
                String accessToken = jwtUtil.generateAccessToken(username);
                String refreshToken = jwtUtil.generateRefreshToken(username);

                // Set refresh token vào HttpOnly cookie
                ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                        .httpOnly(true)
                        .path("/api/auth/refresh")
                        .maxAge(24 * 60 * 60)//24 * 60 * 60
                        .build();
                response.addHeader("Set-Cookie", cookie.toString());
                LoginDTO dto = LoginDTO.builder()
                        .accessToken(accessToken)
                        .userEntity(userinfo)
                        .build();
                return ResponseEntity.ok(dto);
            }

        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body("Refresh token invalid");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);

        return ResponseEntity.ok(tokens);
    }
}
