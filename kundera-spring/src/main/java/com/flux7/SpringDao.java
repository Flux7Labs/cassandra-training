package com.flux7;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


@Service
public class SpringDao
{
    EntityManagerFactory entityManagerFactory;
    public User addUser(User user)
    {
      
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.persist(user);
        entityManager.close();
        return user;
    }

    public User getUserById(String Id)
    {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = entityManager.find(User.class, Id);
        return user;
    }

    @SuppressWarnings("unchecked")
    public List<User> getAllUsers()
    {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT c from User c");
		List<User> list = query.getResultList();
        return list;
    }

    public EntityManagerFactory getEntityManagerFactory()
    {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
    }
    

}