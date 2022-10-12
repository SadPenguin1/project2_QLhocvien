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
import JPANangCao.project2.entity.Course;

@Controller
@RequestMapping("/course")
public class CourseController {
	@Autowired
	CourseJPARepo courseJPARepo;


	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("course" ,new Course());
		return "course/create";
	}
  
	@PostMapping("/create")
	public String create(@ModelAttribute @Valid Course course,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "course/create";
		}
		courseJPARepo.save(course);
		return "redirect:/course/search";			
	}
		
	@GetMapping("/delete")
	public String delete(Model model,@Param("id") int id) {
		courseJPARepo.deleteById(id);
		return "redirect:/course/search";		
	}
	@GetMapping("/search")
	public String searchByName(Model model,
			@RequestParam(value = "id",required=false) Integer id,
			@RequestParam(value= "name",required=false) String s,
			
			@RequestParam(value= "page",required=false) Integer page,
			@RequestParam(value="size",required=false) Integer size) {
		page = (page==null ? 0:page);
		size = (size==null ? 10:size);
		
		Pageable pageable= PageRequest.of(page, size,Sort.by(Direction.ASC,"id"));
		
		if( id != null ) {
			List<Course> courses = courseJPARepo.findAllById(Arrays.asList(id));
			
			model.addAttribute("count", courses.size());
			model.addAttribute("totalPages",1);
			model.addAttribute("courseList",courses);
		}else{		
			Page<Course> pageUser = null;
			
			if (StringUtils.hasText(s)){
				pageUser = courseJPARepo.searchByName( "%" + s + "%",pageable);					
			}else {
				pageUser = courseJPARepo.findAll(pageable);
			}

			model.addAttribute("count", pageUser.getTotalElements());
			model.addAttribute("totalPages", pageUser.getTotalPages());
			model.addAttribute("courseList",pageUser.getContent());
		}
		
		model.addAttribute("id",id);
		model.addAttribute("name",s);	
	
		model.addAttribute("size",size);
		model.addAttribute("page",page);

		return "course/search";
	}
	@GetMapping("/edit")
	public String edit(Model model,@RequestParam("id")int id) {
		Course course = courseJPARepo.findById(id).orElse(null);
		model.addAttribute("course",course);
		return "course/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute("course") Course course) {
		courseJPARepo.save(course);
		return "redirect:/course/search";
	}
}
