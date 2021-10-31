package my.mvc.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.FileRenameUtil;
import spring.vo.ImgVO;

@Controller
public class WriteControl {

	//에디터에서 이미지가 들어갈 때 해당 이미지를 받아서
	//저장할 위치 
	private String editor_img = "/resources/editor_img";
	private String bbs_upload = "/resources/bbs_upload";
	

	
	@Autowired
	private ServletContext application;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private BbsDAO b_dao;
	
	
	@RequestMapping("/write.inc")
	public String write() {
		
		return "write";
	}
	
	
	@RequestMapping(value="/saveImg.inc", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> saveImg(ImgVO ivo){
		//반환객체 생성
		Map<String, String> map = new HashMap<String, String>();
		
		String fname = null; // 반환할 때 필요함!
		
		MultipartFile f = ivo.getS_file(); //넘어온 이미지 파일 있나 확인
		
		if(f.getSize()>0) {
			//이미지파일이 있는경우
			String realPath = application.getRealPath(editor_img);//절대경로 설정
			
			
			fname = f.getOriginalFilename(); //이름얻어내기.
			
			fname = FileRenameUtil.checkSameFileName(fname, realPath);//이름중복뒤에1
			
			try {
				f.transferTo(new File(realPath,fname));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//파일이 업로드가 되었으므로 이제 정확한 경로를 반환해야 한다.(JSON)
			String c_path = request.getContextPath();
			
			map.put("url", c_path+editor_img);
			map.put("fname", fname);
			
			
		}
		return map;
	}
	
	
	@RequestMapping(value="/write.inc", method=RequestMethod.POST)
	public ModelAndView write(BbsVO vo)throws Exception{
		
		//1.multipartfile 첨부파일을  vo로부터 가져오고
		MultipartFile mf = vo.getFile();
		
		
		//파일첨부를 안할수도 있으니 
		if(mf.getSize()>0) {
			//값이 들어오는지 안들어오는지는 포스트맨으로 확인이 가능하닷
			//2.파일 존재여부 확인하고
			
			
			//3.파일이 있다면 절대경로를 설정해주고
			String realPath = application.getRealPath(bbs_upload);
			
			//4.설정해준후 파일이름을 가져오고
			String fname = mf.getOriginalFilename();
			
			//5.파일이름이 겹칠 수 있으니 겹치지않도록 파일명뒤 숫자를 붙혀준다.
			fname = FileRenameUtil.checkSameFileName(fname, realPath);
			
			//fileinputstream없이 transferto로 아주쉽게 파일을 저장가능	
			//예외처리는 위에 throws로 처리했다.
			//6.첨부파일 업로드
			mf.transferTo(new File(realPath,fname));
			
			//7.첨부된 파일명을 DB에저장하기 vo안에있는 file_name에 저장
			vo.setFile_name(fname);
			vo.setOri_name(fname);  //요놈
		}
		//7.1 사용자 IP저장
		vo.setBname("BBS"); //요놈...
		vo.setIp(request.getRemoteAddr()); 
		
		//8.이모든걸 DB에 저장으로 마무리
		b_dao.add(vo);
		
	
		
		//반환객체로 반환해준다.
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/list.inc");
		return mv;
		
	}

	}



















