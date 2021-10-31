package my.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;


@Controller
public class DelControl {

	
	@Autowired
	private BbsDAO b_dao;
	
	
	@RequestMapping("/delete.inc")
	public String del(String b_idx, String cPage) {
		
		//이것저것 따지기 싫으면 그냥 모델앤뷰 쓰면 됩니당.
		b_dao.del(b_idx);
		
		
		
		return "redirect:/list.inc?cPage="+cPage;
	}
	
}
