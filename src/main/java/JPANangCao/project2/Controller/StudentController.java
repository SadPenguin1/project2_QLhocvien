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

import JPANangCao.project2.Repo.CourseJPARepo;
import JPANangCao.project2.Repo.StudentJPARepo;
import JPANangCao.project2.Repo.UserJPARepo;
import JPANangCao.project2.entity.Course;
import JPANangCao.project2.entity.Student;
import JPANangCao.project2.entity.User;

@Controller
@RequestMapping("/student")
public class StudentController {
	@Autowired
	StudentJPARepo studentJPARepo;
	@Autowired
	UserJPARepo userJPARepo;


	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("userList", userJPARepo.findAll());
		return "student/create";
	}
  
	@PostMapping("/create")
	public String create(@ModelAttribute @Valid Student student, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "student/create";
		}
		studentJPARepo.save(student);
		return "redirect:/student/search";			
	}
		
	@GetMapping("/delete")
	public String delete(Model model,@Param("id") int id) {
		studentJPARepo.deleteById(id);
		return "redirect:/student/search";		
	}
	@GetMapping("/search")
	public String search(Model model,
			@RequestParam(value = "id",required=false) Integer id,
			@RequestParam(value= "studentCode",required=false) String s,
			
			@RequestParam(value= "page",required=false) Integer page,
			@RequestParam(value="size",required=false) Integer size) {
		page = (page==null ? 0:page);
		size = (size==null ? 10:size);
		
		Pageable pageable= PageRequest.of(page, size,Sort.by(Direction.ASC,"id"));
		
		if( id != null ) {
			List<Student> students = studentJPARepo.findAllById(Arrays.asList(id));
			
			model.addAttribute("count", students.size());
			model.addAttribute("totalPages",1);
			model.addAttribute("studentList",students);
		}else{		
			Page<Student> pageStudent = null;
			
			if (StringUtils.hasText(s)){
				pageStudent = studentJPARepo.searchByStudentCode("%" + s +"%",pageable);					
			}else {
				pageStudent = studentJPARepo.findAll(pageable);
			}

			model.addAttribute("count", pageStudent.getTotalElements());
			model.addAttribute("totalPages", pageStudent.getTotalPages());
			model.addAttribute("studentList",pageStudent.getContent());
		}
		
		model.addAttribute("id",id);
		model.addAttribute("studentCode",s);	
	
		model.addAttribute("size",size);
		model.addAttribute("page",page);
		model.addAttribute("userList",userJPARepo.findAll());


		return "student/search";
	}
	@GetMapping("/edit")
	public String edit(Model model,@RequestParam("id")int id) {
		Student student = studentJPARepo.findById(id).orElse(null);
		model.addAttribute("student",student);
		model.addAttribute("userList", userJPARepo.findAll());

		return "student/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute("student") Student student) {
		studentJPARepo.save(student);
		return "redirect:/student/search";
	}
}
