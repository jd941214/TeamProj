package team.Dproject.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import team.Dproject.main.model.MemberDTO;

import team.Dproject.main.service.MemberMapper;

/**
 * Handles requests for the application home page.
 */
@Controller
public class MemberController {
   @Autowired
   private MemberMapper memberMapper;

   @Resource(name = "upLoadPath")
   private String upLoadPath;

   @RequestMapping(value = "/index.do")
   public String main() {
      return "index";

   }

   @RequestMapping(value = "/member_login.do")
   public String MemberLogin(HttpServletRequest req) {
      Cookie[] cks = req.getCookies();
      String value = null;
      if (cks != null && cks.length != 0) {
         for (int i = 0; i < cks.length; ++i) {
            String name = cks[i].getName();
            if (name.equals("id")) {
               value = cks[i].getValue();
               break;

            }

         }

      }
      req.setAttribute("value", value);
      return "member/member_login";

   }

   @RequestMapping(value = "/member_login_ok.do")
   public String MemberLoginOk(HttpServletRequest req, HttpServletResponse resp) {
      String id = req.getParameter("id");
      String passwd = req.getParameter("passwd");
      String saveId = req.getParameter("saveId");
      int res = memberMapper.memberLogin(id, passwd);
      String msg = null, url = null;
      switch (res) {
      case 0:
         MemberDTO dto = memberMapper.getMember(id);
         HttpSession session = req.getSession();
         Cookie ck = new Cookie("id", id);
         if (saveId != null) {
            ck.setMaxAge(10 * 60);

         } else {
            ck.setMaxAge(0);

         }
         resp.addCookie(ck);
         session.setAttribute("sedto", dto);
         session.setAttribute("MNUM", dto.getMember_no());
         msg = dto.getName() + "�� ȯ���մϴ�. ������������ �̵��մϴ�.";
         url = "index.do";
         break;

      case 1:
         msg = "��й�ȣ�� �߸� �Է��ϼ̽��ϴ�. �ٽ� �Է��� �ּ���";
         url = "member_login.do";
         break;

      case 2:
         msg = "���� ���̵� �Դϴ�. �ٽ� Ȯ���Ͻð� �Է��� �ּ���";
         url = "member_login.do";
         break;

      }
      req.setAttribute("msg", msg);
      req.setAttribute("url", url);
      return "message";

   }

   @RequestMapping(value = "/member_logout.do")
   public String MemberLogout(HttpServletRequest req) {
      HttpSession session = req.getSession();
      session.removeAttribute("sedto");
      session.removeAttribute("MNUM");
      req.setAttribute("msg", "�α׾ƿ� �Ǿ����ϴ�. ������������ �̵��մϴ�.");
      req.setAttribute("url", "index.do");
      return "message";

   }

   @RequestMapping(value = "/member_input.do")
   public String MemberInput(HttpServletRequest req) {
      req.setAttribute("idck", 0);
      return "member/member_input";

   }
   
    @RequestMapping("/idcheck.do")
       @ResponseBody
       public boolean idcheck(@RequestBody String id, HttpServletRequest req) {
           boolean data = memberMapper.idcheck(id);
           req.setAttribute("idck", 1);
           return data;
           
       }

   @RequestMapping(value = "/member_input_ok.do")
   public String MemberInputOk(HttpServletRequest req, MemberDTO dto, BindingResult result) {
      boolean checkMember = memberMapper.checkMember(dto);
      String msg = null, url = null;
      if (checkMember) {
         String filename = "";
         int filesize = 0;
         MultipartHttpServletRequest mr = (MultipartHttpServletRequest) req;
         MultipartFile file = mr.getFile("filename");
         File target = new File(upLoadPath, file.getOriginalFilename());
         if (file.getSize() > 0) {
            try {
               file.transferTo(target);
            } catch (IOException e) {
            }
            filename = file.getOriginalFilename();
            filesize = (int) file.getSize();
         }else{
            if(dto.getSex() == 0){
               filename = "male.jpg";
               
            }else{
               filename = "female.jpg";
               
            }
            
         }
         dto.setFilename(filename);
         dto.setFilesize(filesize);
         int res = memberMapper.insertMember(dto);
         if (res > 0) {
            msg = "ȸ�����Լ���! �α��� �������� �̵��մϴ�.";
            url = "member_login.do";

         } else {
            msg = "ȸ�����Խ���! ������������ �̵��մϴ�.";
            url = "index.do";

         }
         
      } else {
         msg = "���̵� �ʹ� �����ϴ�. �α��� ���ּ���.";
         url = "member_login.do";

      }
      req.setAttribute("msg", msg);
      req.setAttribute("url", url);
      return "message";

   }

   @RequestMapping(value = "/member_list.do")
   public String MemberList(HttpServletRequest req) {
      HttpSession session = req.getSession();
      MemberDTO dto = (MemberDTO) session.getAttribute("sedto");
      String mode = req.getParameter("mode");
      List<MemberDTO> list = null;
      if(dto.getPosition() == 0){
         if (mode == null) {
            mode = "all";

         }
         if (mode.equals("all")) {
            list = memberMapper.memberList();

         } else {
            String search = req.getParameter("search");
            String searchString = req.getParameter("searchString");
            if (search == null) {
               search = "id";

            }
            if (searchString == null) {
               searchString = "";

            }
            list = memberMapper.findMember(search, searchString);

         }
         req.setAttribute("memberList", list);
         req.setAttribute("mode", mode);
         return "member/member_list";
         
      }else{
         req.setAttribute("msg", "�����ڸ� �� �� �ִ� ������ �Դϴ�");
         req.setAttribute("url", "index.do");
         return "message";
         
      }

   }

   @RequestMapping(value = "/member_edit.do")
   public String MemberEdit(HttpServletRequest req) {
      String id = req.getParameter("id");
      MemberDTO dto = memberMapper.getMember(id);
      if(dto == null){
         req.setAttribute("msg", "ȸ�� ������ �����ϴ�");
         req.setAttribute("url", "member/member_list");
         return "message";
         
      }
      req.setAttribute("dto", dto);
      return "member/member_edit";

   }

   @RequestMapping(value = "/member_edit_ok.do")
   public String MemberEditOk(HttpServletRequest req, MemberDTO dto, BindingResult result) {
      String msg = null, url = null, mode = req.getParameter("mode");
      HttpSession session = req.getSession();
      String filename = dto.getFilename();
      int filesize =dto.getFilesize();
      
      MultipartHttpServletRequest mr = (MultipartHttpServletRequest)req;
      MultipartFile file = mr.getFile("new_filename");
      File target = new File(upLoadPath, file.getOriginalFilename());
      if (file.getSize() > 0){
         try{
            file.transferTo(target);
            
         }catch(IOException e){}
         filename = file.getOriginalFilename();
         filesize = (int)file.getSize();
         dto.setFilename(filename);
         dto.setFilesize(filesize);
      }else if(dto.getFilename() == null){
         dto.setFilename("���Ͼ���");
         dto.setFilesize(0);
         
      }
      int res = memberMapper.editMember(dto);
      if (res > 0) {
         session.removeAttribute("sedto");
         session.setAttribute("sedto", dto);
         if (mode.equals("mypage")) {
            msg = "ȸ����������! ������������ �̵��մϴ�.";
            url = "member_mypage.do";

         } else {
            msg = "ȸ����������! ȸ��������� �̵��մϴ�.";
            url = "member_list.do";

         }

      } else {
         if (mode.equals("mypage")) {
            msg = "ȸ����������! ������������ �̵��մϴ�.";
            url = "member_mypage.do";

         } else {
            msg = "ȸ����������! ȸ�������������� �̵��մϴ�.";
            url = "member_edit.do?id=" + dto.getId();

         }

      }
      req.setAttribute("msg", msg);
      req.setAttribute("url", url);
      return "message";

   }

   @RequestMapping(value = "/member_mypage.do")
   public String MemberMypage(HttpServletRequest req) {
      if((MemberDTO) req.getSession().getAttribute("sedto") == null){
         req.setAttribute("msg", "�α��� �� �̿� ������ �������Դϴ�.");
         req.setAttribute("url", "member_login.do");
         return "message";
         
      }
      return "member/mypage";

   }
   
   @RequestMapping(value = "/member_search.do")
   public String MemberSearch(HttpServletRequest req) {
      MemberDTO dto = (MemberDTO) req.getSession().getAttribute("sedto");
      if(dto.getPosition() == 0){
         String mode = req.getParameter("mode");
         req.setAttribute("mode", mode);
         return "member/member_search";
         
      }else{
         req.setAttribute("msg", "�����ڸ� �� �� �ִ� ������ �Դϴ�");
         req.setAttribute("url", "index.do");
         return "message";
         
      }

   }

   @RequestMapping(value = "/member_search_ok.do")
   public String MemberSearchOk(HttpServletRequest req) {
      String mode = req.getParameter("mode");
      String searchString = req.getParameter("searchString");
      String ssn1 = req.getParameter("ssn1");
      String ssn2 = req.getParameter("ssn2");
      List<MemberDTO> list = null;
      if (mode.equals("id")) {
         list = memberMapper.searchMemberId(searchString, ssn1, ssn2);

      }
      if (mode.equals("passwd")) {
         list = memberMapper.searchMemberPasswd(searchString, ssn1, ssn2);

      }
      req.setAttribute("searchList", list);
      req.setAttribute("mode", mode);
      return "member/searchResult";

   }

   @RequestMapping(value = "/member_delete.do")
   public String MemberDelete(HttpServletRequest req) {
      String mode = req.getParameter("mode");
      if(mode == null){
         mode = "";
         
      }
      HttpSession session = req.getSession();
      MemberDTO dto = (MemberDTO) session.getAttribute("sedto");
      int res = 0;
      String msg = null, url = null;
      if(mode.equals("admin")){
         res = memberMapper.deleteMember(Integer.parseInt(req.getParameter("member_no")));
         if(res > 0){
            msg = "ȸ�� ���� ����! ȸ��������� �̵��մϴ�.";
            url = "member_list.do";
            
         }else{
            msg = "ȸ�� ���� ����! ȸ��������� �̵��մϴ�.";
            url = "member_list.do";
            
         }
         
      }else{
         String passwd = req.getParameter("passwd");
         if(passwd == null){
            msg = "��й�ȣ�� �Է����ּ���.";
            url = "member_mypage.do";
            
         }else if(passwd.equals(dto.getPasswd())){
            msg = "ȸ�� Ż�� ����! ���� �������� �̵��մϴ�.";
            url = "index.do";
            
         }else{
            msg = "��й�ȣ�� �߸� �Է��ϼ̽��ϴ�. �ٽ� �Է����ּ���.";
            url = "member_mypage.do";
            
         }
         res = memberMapper.deleteMember(dto.getMember_no());
         if (res > 0) {
            session.removeAttribute("sedto");
            msg = "ȸ��Ż�𼺰�! ������������ �̵��մϴ�.";
            url = "index.do";

         } else {
            msg = "ȸ��Ż�����! ������������ �̵��մϴ�.";
            url = "index.do";

         }
         
      }
      req.setAttribute("msg", msg);
      req.setAttribute("url", url);
      return "message";

   }


   


}