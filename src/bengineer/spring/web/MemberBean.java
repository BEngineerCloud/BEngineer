package bengineer.spring.web;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/beMember/*")
public class MemberBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	@RequestMapping("beLogin.do")
	public String beLogin() {return "/BEngineer/beMember/beLogin";}
	
	@RequestMapping("beCollback.do")
	public String beCollback() {return "/BEngineer/beMember/beLogin";}
	
	@RequestMapping("beAddinfo.do")
	public String beAddinfo() {return "/BEngineer/beMember/beAddinfo";}
	
	@RequestMapping("beChecknickname.do")
	public String beChecknickname() {return "/BEngineer/beMember/beChecknickname";}
}
