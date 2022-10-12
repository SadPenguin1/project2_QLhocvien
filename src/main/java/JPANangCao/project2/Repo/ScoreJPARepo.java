package JPANangCao.project2.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import JPANangCao.project2.entity.Score;
import JPANangCao.project2.entity.Student;

public interface ScoreJPARepo extends JpaRepository<Score, Integer>{
	 @Query("SELECT u FROM Score u WHERE u.score = :x")
	  Page<Score> searchByScore(@Param("x") Double s ,Pageable pageable);
	 @Query("SELECT sc FROM Score sc JOIN sc.student stu WHERE stu.studentCode = :x")
	  Page<Score> searchByStudentCode(@Param("x") String studentCode ,Pageable pageable);   

}
