package juniqe.com.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import juniqe.com.config.JwtUtil;
import juniqe.com.entity.UserEntity;
import juniqe.com.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Lớp filter này được chạy 1 lần trên mỗi request.
 * Nó kiểm tra nếu có JWT Bearer token trong header thì xác thực người dùng.
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil; // Tiện ích để xử lý token: validate, extract username, etc.
    private final UserService userService; // Service để lấy thông tin user từ database


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Lấy giá trị của header Authorization từ HTTP request
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Kiểm tra header có tồn tại và bắt đầu bằng "Bearer "
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            // Cắt bỏ tiền tố "Bearer " để lấy token thật sự
            token = header.substring(7);

            // Nếu token hợp lệ thì trích xuất username từ token
            if (jwtUtil.validateToken(token)) {
                username = jwtUtil.extractUsername(token);
            }
        }

        // Nếu có username và chưa có Authentication trong context (nghĩa là chưa xác thực)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Truy xuất thông tin người dùng từ database
            UserEntity userinfo = userService.getUserByUsername(username);
            UserDetails userDetails=null;
            if(userinfo.getEnabled() && userinfo.getStatus()==1)
            {
                userDetails = new UserDetailsImpl(userinfo);
            }
            // Gói thông tin người dùng thành đối tượng UserDetails để Spring Security hiểu được
            // Kiểm tra token lại một lần nữa trước khi cấp quyền truy cập
            if (jwtUtil.validateToken(token) && userDetails !=null) {
                // Tạo Authentication token cho Spring Security sử dụng
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, // thông tin người dùng
                        null, // không cần password trong lúc này
                        userDetails.getAuthorities() // danh sách quyền (roles)
                );

                // Gán thông tin thêm cho token từ HTTP request (ví dụ IP, session ID...)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Gán Authentication vào SecurityContext để hệ thống biết người dùng đã đăng nhập
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Tiếp tục chuỗi filter tiếp theo (có thể là controller nếu xác thực thành công)
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();

        // Bỏ qua filter nếu là login, refresh, version, hoặc là OPTIONS (preflight CORS)
        return path.equals("/api/auth/login")
                || path.equals("/api/auth/refresh")
                || path.equals("/api/auth/register")
                || path.equals("/version")
                || "OPTIONS".equalsIgnoreCase(method);
    }

}
