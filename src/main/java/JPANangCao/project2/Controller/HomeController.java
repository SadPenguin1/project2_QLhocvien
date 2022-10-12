package JPANangCao.project2.Controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	

	@Autowired
	Environment environment ;
	
	@GetMapping(value ="/home")
	public String home(HttpServletRequest request) {
		request.setAttribute("msg", environment.getProperty("message"));
		return "home";
	}
	

}
