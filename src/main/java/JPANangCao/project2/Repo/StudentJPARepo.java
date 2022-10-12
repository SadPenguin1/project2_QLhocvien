package JPANangCao.project2.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import JPANangCao.project2.entity.Student;

public interface StudentJPARepo extends JpaRepository<Student, Integer>{
	 @Query("SELECT u FROM Student u WHERE u.studentCode LIKE :x")
	  Page<Student> searchByStudentCode(@Param("x") String s ,Pageable pageable);
	 

}
