package site.rion.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import site.rion.chat.model.ChatUser;


public interface UserRepository extends CrudRepository<ChatUser, Integer>
{
	ChatUser findByUsername(String username);
	ChatUser findById(int id);
	
	@Query(value = "SELECT * FROM user WHERE username <> :username", nativeQuery = true)
	List<ChatUser> findAllExcept(String username);
}
