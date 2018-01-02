package bengineer.spring.web;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainBean {
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	@RequestMapping("BEmain.do")
	public String main() {return "BEmain";}
}
