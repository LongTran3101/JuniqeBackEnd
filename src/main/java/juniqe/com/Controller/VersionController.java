package juniqe.com.Controller;

import juniqe.com.model.Version;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class VersionController extends BaseController {
    @Value("${Apllication.version}")
    private String version;
    @GetMapping("/version")
    ResponseEntity<?> Version(){
        return success(new Version(version));
    }
}
