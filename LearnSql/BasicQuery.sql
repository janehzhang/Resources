select * from user_tables;

select * from sc;
select * from student;
select * from course;

ALTER TABLE student RENAME COLUMN name  TO sname; --�޸ı�����
ALTER TABLE course RENAME COLUMN id  TO cid; --�޸ı�����

drop table student;


--ѡ��������ѧ��ѧ��
select s.sid,s.sname from student s,
       (select sc.sid from sc,course where sc.cid = course.cid and course.cname in ('Chinese','Math')
       group by sid) x where s.sid = x.sid;

--û��ѡ�ܿ���ʦ�ε�ѧ��
select sname from student s where sid not in(select sid from sc,course c where sc.cid = c.cid and c.cteacher = '�ܿ���ʦ');

select sname from student s where sid not in (select sid from sc join course c on sc.cid = c.cid and c.cteacher = '�ܿ���ʦ');

--�����ż����ϼ����ѧ��
select sname,avg_grade from student s left join      --ֱ��join
       (select sid,trunc(avg(grade),2) avg_grade from sc where grade<60 group by sid having count(DISTINCT cid)>=2) x
on s.sid = x.sid;                                     --group �� having �� DISTINCT ���� ������





