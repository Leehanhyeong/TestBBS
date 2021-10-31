package my.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.Paging;

@Controller
public class ListControl {

	@Autowired
	private BbsDAO b_dao;
	
	int nowPage;
	int rowTotal;   //총 게시물 수
	int blockList = 10; //한 페이지당 표현될 게시물 수
	int blockPage = 5; //한 블럭당 표현될 페이지 수.
	
	
	//Map은 json으로 반환할때 쓴다.
	//뭔가 저장할 땐 ModelAndView
	//저장할게 없을땐 String
	@RequestMapping("/list.inc")
	public ModelAndView list(String cPage, String bname){
		
		ModelAndView mv = new ModelAndView();
			
		if(cPage == null)
			nowPage = 1;
		else
			nowPage = Integer.parseInt(cPage);
		
		if(bname == null)
			bname = "BBS"; //일반 게시판
		
		rowTotal = b_dao.getTotalCount(bname); //전체 게시물 수를 구했다.
		
		//페이징 처리를 위한 객체 생성
		Paging page = new Paging(nowPage, rowTotal, blockList, blockPage);
		
		int begin = page.getBegin();
		int end = page.getEnd();
		
		BbsVO[] ar = b_dao.getList(String.valueOf(begin), String.valueOf(end), bname);
				
		
		//JSP에서 표현해야 하므로 ar을 mb에 저장한다.
		
		mv.addObject("ar", ar);
		mv.addObject("nowPage", nowPage);
		mv.addObject("blockList", blockList);
		mv.addObject("rowTotal", rowTotal);
		mv.addObject("pageCode", page.getSb().toString());
		
		
		mv.setViewName("list"); //views/list.jsp
		return mv;
	}
}




