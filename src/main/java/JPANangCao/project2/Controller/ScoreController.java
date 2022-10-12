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
import JPANangCao.project2.Repo.ScoreJPARepo;
import JPANangCao.project2.Repo.StudentJPARepo;
import JPANangCao.project2.Repo.UserJPARepo;
import JPANangCao.project2.entity.Course;
import JPANangCao.project2.entity.Score;
import JPANangCao.project2.entity.Student;
import JPANangCao.project2.entity.User;

@Controller
@RequestMapping("/score")
public class ScoreController {
	@Autowired
	StudentJPARepo studentJPARepo;
	@Autowired
	CourseJPARepo courseJPARepo;
	@Autowired
	ScoreJPARepo scoreJPARepo;


	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("studentList", studentJPARepo.findAll());
		model.addAttribute("courseList" , courseJPARepo.findAll());
		return "score/create";
	}
  
	@PostMapping("/create")
	public String create(@ModelAttribute @Valid Score score, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "score/create";
		}
		scoreJPARepo.save(score);
		return "redirect:/score/search";			
	}
		
	@GetMapping("/delete")
	public String delete(Model model,@Param("id") int id) {
		scoreJPARepo.deleteById(id);
		return "redirect:/score/search";		
	}
	@GetMapping("/search")
	public String search(Model model,
			@RequestParam(value = "id",required=false) Integer id,
			@RequestParam(value= "score",required=false) Double s,	
			@RequestParam(value= "courseId",required=false) Integer courseId,	
			@RequestParam(value= "studentId",required=false) Integer studentId,	

			@RequestParam(value= "studentCode",required=false) String studentCode,	
			
			@RequestParam(value= "page",required=false) Integer page,
			@RequestParam(value="size",required=false) Integer size) {
		page = (page==null ? 0:page);
		size = (size==null ? 10:size);
		
		Pageable pageable= PageRequest.of(page, size,Sort.by(Direction.ASC,"id"));
	
		if( id != null ) {
			List<Score> scores = scoreJPARepo.findAllById(Arrays.asList(id));
			
			model.addAttribute("count", scores.size());
			model.addAttribute("totalPages",1);
			model.addAttribute("scoreList",scores);
		}else{		
			Page<Score> pageScore = null;
			
			if (s != null){
				pageScore = scoreJPARepo.searchByScore(s, pageable);	
			}else if (StringUtils.hasText(studentCode)){
				pageScore = scoreJPARepo.searchByStudentCode( studentCode , pageable);	
			}else {
				pageScore = scoreJPARepo.findAll(pageable);
			}

			model.addAttribute("count", pageScore.getTotalElements());
			model.addAttribute("totalPages", pageScore.getTotalPages());
			model.addAttribute("scoreList",pageScore.getContent());
		}
		
		model.addAttribute("id",id);
		model.addAttribute("score",s);	
		model.addAttribute("courseId",courseId);
		model.addAttribute("studentId",studentId);
		model.addAttribute("studentCode", studentCode);
		
		
		model.addAttribute("size",size);
		model.addAttribute("page",page);
		
		model.addAttribute("studentList",studentJPARepo.findAll());
		model.addAttribute("courseList",courseJPARepo.findAll());


		return "score/search";
	} 
	@GetMapping("/edit")
	public String edit(Model model,@RequestParam("id")int id) {
		Score score = scoreJPARepo.findById(id).orElse(null);
		model.addAttribute("score",score);
		model.addAttribute("studentList",studentJPARepo.findAll());
		model.addAttribute("courseList",courseJPARepo.findAll());
		return "score/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute("score") Score score) {
		scoreJPARepo.save(score);
		return "redirect:/score/search";
	}
}
