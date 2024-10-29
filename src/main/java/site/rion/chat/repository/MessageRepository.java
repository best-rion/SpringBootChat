package site.rion.chat.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import site.rion.chat.model.Message;


public interface MessageRepository extends CrudRepository<Message, Integer> 
{
	@Query(value = "SELECT * FROM message WHERE (sender = :person1 AND receiver = :person2)"
			+ " OR (sender = :person2 AND receiver = :person1)", nativeQuery = true)
	List<Message> findByPeople(String person1, String person2);
	
	@Query(value = "SELECT COUNT(*) FROM message WHERE sender = :sender AND receiver = :me AND seen=false", nativeQuery = true)
	int numOfUnseenMessageBySender(String me, String sender);
	
	@Query(value = "SELECT * FROM message WHERE sender = :sender AND receiver = :me AND seen=false", nativeQuery = true)
	List<Message> unseenMessageBySender(String me, String sender);
	
	Message findById(int id);
}