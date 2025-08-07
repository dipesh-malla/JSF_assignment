    package com.dipesh.javaintern.controller;

    import com.dipesh.javaintern.model.Student;
    import com.dipesh.javaintern.service.StudentService;

    import javax.annotation.PostConstruct;
    import javax.ejb.EJB;
    import javax.faces.application.FacesMessage;
    import javax.faces.bean.ManagedBean;
    import javax.faces.bean.ViewScoped;
    import javax.faces.context.FacesContext;
    import java.io.Serializable;
    import java.util.*;
    import java.util.logging.Logger;

    @ManagedBean(name = "studentBean")
    @ViewScoped
    public class StudentBean implements Serializable {

        private static final long serialVersionUID = 1L;

        private static final Logger logger = Logger.getLogger(StudentBean.class.getName());

        private Student student;

        private List<Student> students;

        private Student selectedStudent;

        private List<String> availableSubjects;

        private List<String> editAvailableSubjects;

        private Map<String, List<String>> subjectsByClass;

        @EJB
        private StudentService studentService;

        @PostConstruct
        public void init() {
            student = new Student();
            loadStudents();

            subjectsByClass = new HashMap<>();
            subjectsByClass.put("Class 1", Arrays.asList("Math", "Science", "English"));
            subjectsByClass.put("Class 2", Arrays.asList("Biology", "Chemistry", "History"));
            subjectsByClass.put("Class 3", Arrays.asList("Physics", "Geography", "Economics"));

            availableSubjects = new ArrayList<>();
        }

        public void loadStudents() {
            students = studentService.getAllStudents();
        }

        public void onClassChange() {
            if (student.getStudentClass() != null && subjectsByClass.containsKey(student.getStudentClass())) {
                availableSubjects = subjectsByClass.get(student.getStudentClass());
            } else {
                availableSubjects = new ArrayList<>();
            }

        }
        public void onCreateClassChange() {
            String selectedClass = student.getStudentClass();

            if (selectedClass != null && !selectedClass.isEmpty()) {
                List<String> subjects = subjectsByClass.get(selectedClass);
                if (subjects != null) {
                    availableSubjects = new ArrayList<>(subjects);
                } else {
                    availableSubjects = new ArrayList<>();
                }
            } else {
                availableSubjects = new ArrayList<>();
            }

            student.setSubject(null);
        }



        public void save() {
            try {
                studentService.saveStudent(student);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Student added successfully"));
                loadStudents();
                student = new Student();
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error saving student", e.getMessage()));
            }
        }

        public void delete(Student s) {
            try {
                studentService.deleteStudent(s);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Student deleted"));
                loadStudents();
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error deleting student", e.getMessage()));
            }
        }

        public void edit(Student s) {
            selectedStudent = s;

            if (s.getStudentClass() != null && subjectsByClass.containsKey(s.getStudentClass())) {
                availableSubjects = subjectsByClass.get(s.getStudentClass());
            } else {
                availableSubjects = new ArrayList<>();
            }
        }

        public void update() {
            if (selectedStudent != null) {
                try {
                    studentService.updateStudent(selectedStudent);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Student updated"));
                    loadStudents();
                    selectedStudent = null;
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating student", e.getMessage()));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "No student selected for update", null));
            }

        }

        public List<String> getEditAvailableSubjects() {
            if(editAvailableSubjects == null) {
                editAvailableSubjects = new ArrayList<>();
            }
            return editAvailableSubjects;
        }

        public void onEditClassChange() {
            if(selectedStudent != null && selectedStudent.getStudentClass() != null) {
                List<String> subjects = subjectsByClass.get(selectedStudent.getStudentClass());
                if(subjects != null) {
                    editAvailableSubjects = subjects;
                } else {
                    editAvailableSubjects = new ArrayList<>();
                }
            } else {
                editAvailableSubjects = new ArrayList<>();
            }
            if(selectedStudent != null) {
                selectedStudent.setSubject(null);
            }
        }


        // Getters and Setters

        public Student getStudent() {
            if (student == null) {
                student = new Student();
            }
            return student;
        }

        public void setStudent(Student student) {
            this.student = student;
        }

        public List<Student> getStudents() {
            return students;
        }

        public Student getSelectedStudent() {
            if (selectedStudent == null) {
                selectedStudent = new Student();
            }
            return selectedStudent;
        }

        public void setSelectedStudent(Student selectedStudent) {
            this.selectedStudent = selectedStudent;
        }

        public Map<String, List<String>> getSubjectsByClass() {
            return subjectsByClass;
        }

        public List<String> getAvailableSubjects() {
            return availableSubjects;
        }
    }
