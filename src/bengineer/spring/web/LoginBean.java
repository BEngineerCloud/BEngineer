package bengineer.spring.web;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	@RequestMapping("beLogin.do")
	public String main() {return "beLogin";}
}
