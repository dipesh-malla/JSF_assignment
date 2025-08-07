package com.dipesh.javaintern.service;

import com.dipesh.javaintern.model.Student;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class StudentService {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public List<Student> getAllStudents() {
        return em.createQuery("SELECT s FROM Student s", Student.class).getResultList();
    }

    public void saveStudent(Student student) {
        em.persist(student);
    }

    public void updateStudent(Student student) {
        em.merge(student);
    }

    public void deleteStudent(Student student) {
        Student s = em.find(Student.class, student.getId());
        if (s != null) {
            em.remove(s);
        }
    }
}

