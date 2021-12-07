package com.greatlearning.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.greatlearning.entities.Student;
import com.greatlearning.services.StudentService;

@Repository
public class StudentServiceImpl implements StudentService{
	
	private SessionFactory sessionFactory;
	private Session session;

	@Autowired
	StudentServiceImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (HibernateException ex) {
			session = sessionFactory.openSession();
		}
	}
	
	@Override
	@Transactional
	public void saveStudent(Student student) {
		
		if(student!=null && student.getName()!=null && student.getCountry()!=null && student.getDepartment()!=null){
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(student);		
			transaction.commit();
		}
	}

	@Override
	@Transactional
	public void updateStudent(Student student , Integer studentId) {
		if(studentId!=null){
			
			Student studentFromDb = getStudentById(studentId);
			if(studentFromDb!=null){
				student.setStudentId(studentId);							
				Transaction transaction = session.beginTransaction();		
				session.flush();
				session.merge(student);				
				transaction.commit();
			}
		}
	}

	@Override
	@Transactional
	public List<Student> getAllStudents() {
		@SuppressWarnings("unchecked")
		List<Student> theStudents = session.createQuery("from Student").list();
		return theStudents;
		
	}

	@Override
	@Transactional
	public Student getStudentById(Integer studentId) {
		Student theStudent = (Student) session.createQuery("from Student where studentId =:studentId").setParameter("studentId",studentId).uniqueResult();
		return theStudent;
	}

	@Override
	@Transactional
	public void deleteStudent(Integer studentId) {
		if(studentId!=null){
			Student student = getStudentById(studentId);
			if(student!=null){
				Transaction transaction = session.beginTransaction();
				session.delete(student);
				transaction.commit();
			}
		}
	}

}
