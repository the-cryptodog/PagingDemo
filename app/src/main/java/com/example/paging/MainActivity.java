package com.example.paging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btn_add,btn_clear;
    StudentDao studentDao;
    StudentsDatabase studentsDatabase;
    MyPageAdapter myPageAdapter;
    LiveData<PagedList<Student>> allStudentsLivePaged;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Book book = new Book ("TheDog",80258);
        Gson gson = new Gson();
        String jsonBook = gson.toJson(book);
        Log.d("myLog",jsonBook);

        recyclerView = findViewById(R.id.recyclerview);
        myPageAdapter = new MyPageAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(myPageAdapter);

        studentsDatabase = StudentsDatabase.getInstance(this);
        studentDao = studentsDatabase.getStudentDao();

        allStudentsLivePaged = new LivePagedListBuilder<>(studentDao.getAllStudents(),2).build();
        allStudentsLivePaged.observe(this, new Observer<PagedList<Student>>() {
            @Override
            public void onChanged(PagedList<Student> students) {
                myPageAdapter.submitList(students);
            }
        });

        btn_add = findViewById(R.id.btn_add);
        btn_clear = findViewById(R.id.btn_clear);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student[] students= new Student[1000];
                for(int i= 0 ; i <1000; i++){
                    Student student = new Student();
                    student.setStudentNumber(i);
                    students[i]=student;
                }
                new InsertAsyncTask(studentDao).execute(students);
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearAsyncTask(studentDao).execute();
            }
        });
    }

    static class InsertAsyncTask extends AsyncTask<Student,Void,Void>{
        StudentDao studentDao;

        public InsertAsyncTask(StudentDao studentDao) {
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDao.insertStudents(students);
            return null;
        }
    }
    static class ClearAsyncTask extends AsyncTask<Void,Void,Void>{
        StudentDao studentDao;

        public ClearAsyncTask(StudentDao studentDao) {
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Void...voids) {
            studentDao.deleteAllStudent();
            return null;
        }
    }
}