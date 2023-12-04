create table university_attendance.sys_menu
(
    id        bigint auto_increment
        primary key,
    menu_name varchar(64) default 'NULL' not null comment '菜单名',
    perms     varchar(100)               null comment '权限标识'
)
    comment '菜单表';

create table university_attendance.sys_role
(
    id       bigint auto_increment
        primary key,
    name     varchar(128) null,
    role_key varchar(100) null comment '角色权限字符串'
)
    comment '角色表';

create table university_attendance.sys_role_menu
(
    role_id bigint auto_increment comment '角色ID',
    menu_id bigint default 0 not null comment '菜单id',
    primary key (role_id, menu_id),
    constraint role_menu_menu_id_fk
        foreign key (menu_id) references university_attendance.sys_menu (id)
            on update cascade on delete cascade,
    constraint role_menu_role_id_fk
        foreign key (role_id) references university_attendance.sys_role (id)
            on update cascade on delete cascade
);

create table university_attendance.sys_user
(
    id        bigint auto_increment
        primary key,
    no        varchar(20) null,
    password  text        null,
    name      varchar(20) null,
    gender    varchar(2)  null,
    college   varchar(20) null,
    user_type int         null,
    constraint id
        unique (id)
);

create table university_attendance.course_detail
(
    id               bigint auto_increment
        primary key,
    course_name      varchar(30) null comment '课程名称',
    semester         int         null comment '0代表上学期，1代表下学期',
    week             smallint    null,
    weekday          smallint    null,
    section_start    tinyint     null,
    section_end      tinyint     null,
    begin_time       datetime    not null comment '这节课的开始时间',
    end_time         datetime    null,
    course_teacher   bigint      not null comment '课程对应的教师',
    school_open_time datetime    null,
    course_place     varchar(30) not null,
    longitude        varchar(40) null,
    latitude         varchar(40) null,
    constraint course_teacher__fk
        foreign key (course_teacher) references university_attendance.sys_user (id)
            on update cascade on delete cascade
)
    comment '课程表';

create table university_attendance.student
(
    id     int auto_increment comment '学生ID'
        primary key,
    no     varchar(10) not null comment '学号',
    name   varchar(50) not null comment '姓名',
    grade  varchar(20) not null comment '年级',
    clas_s varchar(20) not null comment '班级',
    major  varchar(50) not null comment '专业',
    userid bigint      null,
    constraint student_user_id_fk
        foreign key (userid) references university_attendance.sys_user (id)
            on update cascade on delete cascade
)
    comment '学生表';

create table university_attendance.attendanceappeal
(
    id                bigint auto_increment
        primary key,
    student_id        int          not null,
    course_id         bigint       not null,
    appeal_begin_time datetime     null,
    appeal_end_time   datetime     null,
    status            varchar(20)  not null,
    reason            varchar(400) not null,
    image             varchar(100) null,
    constraint attendanceappeal_course_detail_id_fk
        foreign key (course_id) references university_attendance.course_detail (id)
            on update cascade on delete cascade,
    constraint attendanceappeal_student_id_fk
        foreign key (student_id) references university_attendance.student (id)
            on update cascade on delete cascade
)
    comment '考勤申请';

create table university_attendance.course_attendance
(
    id         bigint auto_increment
        primary key,
    student_id int      not null,
    course_id  bigint   not null,
    status     bigint   not null comment '0为未考勤，1为考勤，2为缺勤，3为请假',
    time       datetime null,
    constraint attendance_result_course_id_fk
        foreign key (course_id) references university_attendance.course_detail (id)
            on update cascade on delete cascade,
    constraint attendance_result_students_id_fk
        foreign key (student_id) references university_attendance.student (id)
            on update cascade on delete cascade
)
    comment '考勤结果表';

create table university_attendance.leaveapplication
(
    id                bigint auto_increment comment '申诉ID，整数类型，作为主键'
        primary key,
    student_id        int          not null comment '学生ID',
    course_id         bigint       null,
    leave_place       varchar(10)  not null,
    appeal_begin_time datetime     not null,
    appeal_end_time   datetime     not null,
    status            varchar(20)  not null,
    reason            varchar(255) null,
    image             varchar(100) null,
    constraint leaveapplication_course_detail_id_fk
        foreign key (course_id) references university_attendance.course_detail (id)
            on update cascade on delete cascade,
    constraint leaveapplication_student_id_fk
        foreign key (student_id) references university_attendance.student (id)
            on update cascade on delete cascade
)
    comment '请假表';

create table university_attendance.supervision_task
(
    id        bigint auto_increment
        primary key,
    course_id bigint not null,
    userid    bigint null,
    constraint supervision_task_course_detail_id_fk
        foreign key (course_id) references university_attendance.course_detail (id)
            on update cascade on delete cascade,
    constraint supervision_task_user_id_fk
        foreign key (userid) references university_attendance.sys_user (id)
            on update cascade on delete cascade
)
    comment '督导_课程关系表';

create table university_attendance.sys_user_role
(
    user_id bigint auto_increment comment '用户id',
    role_id bigint default 0 not null comment '角色id',
    primary key (user_id, role_id),
    constraint sys_user_role_sys_role_id_fk
        foreign key (role_id) references university_attendance.sys_role (id),
    constraint sys_user_role_sys_user_id_fk
        foreign key (user_id) references university_attendance.sys_user (id)
);

