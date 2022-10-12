package JPANangCao.project2.entity;



import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name="student")
public class Student {
	
	@Id
	
	private Integer id;
	@NotBlank
	private String studentCode;

	//@JoinColumn(name ="studnet_id")
	@OneToOne
	private User user;
	
//	@ManyToMany
//	@JoinTable(name="score", 
//				joinColumns = @JoinColumn(name="student_id"), 
//				inverseJoinColumns = @JoinColumn(name="course_id"))
//	@JsonManagedReference
//	private List<Course> courses;
}
