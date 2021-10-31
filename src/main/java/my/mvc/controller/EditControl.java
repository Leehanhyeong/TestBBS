package my.mvc.controller;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.FileRenameUtil;

@Controller
public class EditControl {

	
	@Autowired
	private ServletContext application;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private BbsDAO b_dao;
	
	private String editor_img = "/resources/editor_img";
	private String bbs_upload = "/resources/bbs_upload";
	
	
	@RequestMapping("/edit.inc") //수정본을 가져오기
	public ModelAndView edit(String b_idx) {
		
		ModelAndView mv = new ModelAndView();
		
		BbsVO vo = b_dao.getBbs(b_idx);
		mv.addObject("vo",vo);
		mv.setViewName("edit");
		return mv;
	}
	
	
	
	//수정
		@RequestMapping(value="/edit.inc", method=RequestMethod.POST)
		public ModelAndView edit(BbsVO vo, String cPage) throws Exception{
			
			ModelAndView mv = new ModelAndView(); //반환객체생성
			
			//요청시 파일 첨부된 요청인지 아닌지 구별!
			//파일이첨부된 놈들은 Multipart..로시작 아닌놈들은 application..으로 시작~
			String ctx = request.getContentType();
			
			if(ctx.startsWith("multipart")) { //스타트위드 잊지말자. 시작하는 뜻
				
				MultipartFile mf = vo.getFile();
				
				if(mf.getSize()>0) {
					
					String realPath = application.getRealPath(bbs_upload);
					
					String fname = mf.getOriginalFilename();
					
					fname = FileRenameUtil.checkSameFileName(fname, realPath);
					mf.transferTo(new File(realPath,fname)); //첨부파일 업로드
					
					vo.setFile_name(fname);
					vo.setOri_name(fname); 
					
					//파일처리 끝
				}
				
				vo.setIp(request.getRemoteAddr());
				
				b_dao.edit(vo);//DB저장
				mv.setViewName("redirect:view.inc?b_idx="+vo.getB_idx()+"&cPage="+cPage);
			}else if(ctx.startsWith("application")) {
				//파일 첨부가 안된경우.
				BbsVO bvo = b_dao.getBbs(vo.getB_idx());
				
				mv.addObject("vo", bvo);
				
				mv.setViewName("edit");
			}
			
			return mv;
		}

	}










