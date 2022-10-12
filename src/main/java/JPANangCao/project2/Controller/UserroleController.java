package JPANangCao.project2.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import JPANangCao.project2.Repo.UserJPARepo;
import JPANangCao.project2.Repo.UserRoleJPARepo;
import JPANangCao.project2.entity.User;
import JPANangCao.project2.entity.UserRole;

@Controller
@RequestMapping("/userrole")

public class UserroleController {
	@Autowired
	UserRoleJPARepo userRoleJPARepo;
	@Autowired
	UserJPARepo userJPARepo;


	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("userList", userJPARepo.findAll());
//		model.addAttribute("userrole" ,new UserRole());
		
		return "userrole/create";
	}
  
	@PostMapping("/create")
	public String create(@ModelAttribute @Valid UserRole userrole,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "userrole/create";
		}
		userRoleJPARepo.save(userrole);
		return "redirect:/userrole/search";			
	}
		
	@GetMapping("/delete")
	public String delete(Model model,@Param("id") int id) {
		userRoleJPARepo.deleteById(id);
		return "redirect:/userrole/search";		
	}
	@GetMapping("/search")
	public String search(Model model,
			@RequestParam(value = "id",required=false) Integer id,
			@RequestParam(value= "role",required=false) String s,
			
//			@RequestParam(value= "userId",required=false) int userId,
			
			@RequestParam(value= "page",required=false) Integer page,
			@RequestParam(value="size",required=false) Integer size) {
		page = (page==null ? 0:page);
		size = (size==null ? 10:size);
		
		
		Pageable pageable= PageRequest.of(page, size,Sort.by(Direction.ASC,"id"));
		
		if( id != null ) {
			List<UserRole> userroles = userRoleJPARepo.findAllById(Arrays.asList(id));
			
			model.addAttribute("count", userroles.size());
			model.addAttribute("totalPages",1);
			model.addAttribute("userroleList",userroles);
		}else{		
			Page<UserRole> pageUser = null;
			
			if (StringUtils.hasText(s)){
				pageUser = userRoleJPARepo.searchByRole( "%" + s + "%",pageable);	
//			}else if(userId != null) {
//				pageUser = userRoleJPARepo.searchByUserId(userId, pageable);
			}else {
				pageUser = userRoleJPARepo.findAll(pageable);
			}

			model.addAttribute("count", pageUser.getTotalElements());
			model.addAttribute("totalPages", pageUser.getTotalPages());
			model.addAttribute("userroleList",pageUser.getContent());
		}
		
		model.addAttribute("id",id);
		model.addAttribute("role",s);
		
//		model.addAttribute("userId",userId);
	
		model.addAttribute("size",size);
		model.addAttribute("page",page);
		
		model.addAttribute("userList",userRoleJPARepo.findAll());

		return "userrole/search";
	}
	@GetMapping("/edit")
	public String edit(Model model,@RequestParam("id")int id) {
		UserRole userrole = userRoleJPARepo.findById(id).orElse(null);
		model.addAttribute("userrole",userrole);
		return "userrole/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute("userrole") UserRole userrole) {
		userRoleJPARepo.save(userrole);
		return "redirect:/userrole/search";
	}
}
