import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TisWillActiallyRun {

    @RequestMapping("/")
    String home() {
        return "Hello World!"
    }
}