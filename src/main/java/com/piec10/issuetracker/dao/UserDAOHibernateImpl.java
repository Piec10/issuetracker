package com.piec10.issuetracker.dao;

import com.piec10.issuetracker.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserDAOHibernateImpl implements UserDAO{

    @Autowired
    private EntityManager entityManager;


    @Override
    @Transactional
    public List<User> findAll() {

        Session currentSession = entityManager.unwrap(Session.class);

        Query<User> query = currentSession.createQuery("from User", User.class);

        List<User> users = query.getResultList();

        return users;
    }
}
