select * from user_tables;

select * from sc;
select * from student;
select * from course;

ALTER TABLE student RENAME COLUMN name  TO sname; --修改表列名
ALTER TABLE course RENAME COLUMN id  TO cid; --修改表列名

drop table student;


--选了语文数学的学生
select s.sid,s.sname from student s,
       (select sc.sid from sc,course where sc.cid = course.cid and course.cname in ('Chinese','Math')
       group by sid) x where s.sid = x.sid;

--没有选杰克老师课的学生
select sname from student s where sid not in(select sid from sc,course c where sc.cid = c.cid and c.cteacher = '杰克老师');

select sname from student s where sid not in (select sid from sc join course c on sc.cid = c.cid and c.cteacher = '杰克老师');

--有两门及以上及格的学生
select sname,avg_grade from student s left join      --直接join
       (select sid,trunc(avg(grade),2) avg_grade from sc where grade<60 group by sid having count(DISTINCT cid)>=2) x
on s.sid = x.sid;                                     --group 加 having 加 DISTINCT 厉害 ！！！





