package JPANangCao.project2.Repo;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import JPANangCao.project2.entity.User;

public interface UserJPARepo extends JpaRepository<User, Integer>{
	 @Query("SELECT u FROM User u WHERE u.name LIKE :x")
	  Page<User> searchByName(@Param("x") String s ,Pageable pageable);
	  
	  @Query("SELECT u FROM User u "+"WHERE u.birthday >= :start and u.birthday <= :end")
	  Page<User> searchByDate(@Param("start") Date start ,@Param("end") Date end,Pageable pageable);
	  
	  @Query("SELECT u FROM User u "+"WHERE u.birthday >= :start")
	  Page<User> searchByStartDate(@Param("start") Date start ,Pageable pageable);
	  
	  @Query("SELECT u FROM User u "+"WHERE u.birthday >= :end")
	  Page<User> searchByEndDate(@Param("end") Date end ,Pageable pageable);
	  
	  @Query( "SELECT u FROM User u WHERE u.name LIKE :x AND u.birthday >= :start AND u.birthday <= :end")
	  Page<User> searchByNameAndDate(@Param("x") String s ,@Param("start") Date start ,@Param("end") Date end ,
			  Pageable pageable);
	  
	  @Query( "SELECT u FROM User u WHERE u.name LIKE :x AND u.birthday >= :start ")
	  Page<User> searchByNameAndStartDate(@Param("x") String s ,@Param("start") Date start ,  Pageable pageable);
	  
	  @Query( "SELECT u FROM User u WHERE u.name LIKE :x AND  u.birthday <= :end")
	  Page<User> searchByNameAndEndDate(@Param("x") String s ,@Param("end") Date end , Pageable pageable);
	  
	  
	  

}
