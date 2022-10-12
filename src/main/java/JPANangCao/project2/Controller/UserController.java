package JPANangCao.project2.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import JPANangCao.project2.Repo.UserJPARepo;
import JPANangCao.project2.Repo.UserRoleJPARepo;
import JPANangCao.project2.entity.User;



@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserJPARepo userJPARepo;


	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("userList", userJPARepo.findAll());
		return "user/create";
	}
  
	@PostMapping("/create")
	public String create(@ModelAttribute User user,@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException  {
		
		if(!file.isEmpty()) {
			final String UPLOAD_FOLDEL ="/C:/uploadFile/";
			String filename = file.getOriginalFilename();
			File newFile = new File(UPLOAD_FOLDEL +filename);
			
			file.transferTo(newFile);
			
		user.setAvatar(filename);		
		}
		userJPARepo.save(user);
		return "redirect:/user/search";
	
	   }	
	
	@GetMapping("/download")
	public void download(@RequestParam("filename") String filename,HttpServletResponse response) throws IOException {
		final String UPLOAD_FOLDEL = "/C:/uploadFile/";
		
		File file = new File(UPLOAD_FOLDEL +filename);
		Files.copy(file.toPath(),response.getOutputStream());
	}
	
//	@PostMapping("/upload-multi")
//	public String createUser(@RequestParam("files") MultipartFile[] files)  throws IllegalStateException, IOException  {
//		
//		System.out.println(files.length);
//		for (MultipartFile file :files)
//		if(!file.isEmpty()) {
//			final String UPLOAD_FOLDEL ="/C:/uploadFile/";
//			String filename = file.getOriginalFilename();
//			File newFile = new File(UPLOAD_FOLDEL +filename);
//			
//			file.transferTo(newFile);
//				
//		}
//		return "redirect:/user/search";	
//	   }	
	
	@GetMapping("/delete")
	public String delete(Model model,@Param("id") int id) {
		userJPARepo.deleteById(id);
		return "redirect:/user/search";		
	}
	@GetMapping("/search")
	public String search(Model model,
			@RequestParam(value = "id",required=false) Integer id,
			@RequestParam(value= "name",required=false) String s,
			
			@RequestParam(value= "uStartDate",required=false)@DateTimeFormat(pattern ="dd/MM/yyyy HH:mm") Date start ,
			@RequestParam(value= "uEndDate",required=false)@DateTimeFormat(pattern ="dd/MM/yyyy HH:mm")  Date end ,
			
			@RequestParam(value= "page",required=false) Integer page,
			@RequestParam(value="size",required=false) Integer size) {
		page = (page==null ? 0:page);
		size = (size==null ? 10:size);
		
		Pageable pageable= PageRequest.of(page, size,Sort.by(Direction.ASC,"id"));
		
		if( id != null ) {
			List<User> users = userJPARepo.findAllById(Arrays.asList(id));
			
			model.addAttribute("count", users.size());
			model.addAttribute("totalPages",1);
			model.addAttribute("userList",users);
		}else{		
			Page<User> pageUser = null;
			
			if(StringUtils.hasText(s) && start != null && end != null) {
				pageUser = userJPARepo.searchByNameAndDate( "%" + s + "%", start, end, pageable);				
			}else if (StringUtils.hasText(s) && start != null ){
				pageUser = userJPARepo.searchByNameAndStartDate( "%" + s + "%", start, pageable);	
			}else if (StringUtils.hasText(s) && end != null) {
				pageUser = userJPARepo.searchByNameAndEndDate( "%" + s + "%", end, pageable);	
			}else if (StringUtils.hasText(s)){
				pageUser = userJPARepo.searchByName( "%" + s + "%",pageable);		
			}else if ( start != null && end != null ){
				pageUser = userJPARepo.searchByDate( start, end, pageable);	
			}else if (start != null) {
				pageUser = userJPARepo.searchByStartDate( start, pageable);	
			}else if (end != null) {
				pageUser = userJPARepo.searchByEndDate( end, pageable);	
			}else {
				pageUser = userJPARepo.findAll(pageable);
			}

			model.addAttribute("count", pageUser.getTotalElements());
			model.addAttribute("totalPages", pageUser.getTotalPages());
			model.addAttribute("userList",pageUser.getContent());
		}
		
		model.addAttribute("id",id);
		model.addAttribute("name",s);	
		model.addAttribute("start",start);	
		model.addAttribute("end",end);
		
		model.addAttribute("size",size);
		model.addAttribute("page",page);
		

		return "user/search";
	}
	@GetMapping("/edit")
	public String edit(Model model,@RequestParam("id")int id) {
		User u = userJPARepo.findById(id).orElse(null);
		model.addAttribute("user",u);
		return "user/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute User editUser) throws IllegalStateException, IOException  {
		User current = userJPARepo.findById(editUser.getId()).orElse(null);
		
		if(current != null) {
			if(!editUser.getFile().isEmpty()) {
				final String UPLOAD_FOLDEL ="/C:/project2/";
				
				String filename = editUser.getFile().getOriginalFilename();
				File newFile = new File(UPLOAD_FOLDEL +filename);
				
				editUser.getFile().transferTo(newFile);
				
				current.setAvatar(filename);
			}
			current.setName(editUser.getName());
			current.setBirthday(editUser.getBirthday());
			current.setUsername(editUser.getUsername());
			current.setPassword(editUser.getPassword());

			userJPARepo.save(current);
		}
		return "redirect:/user/search";		
	}
	}
	
