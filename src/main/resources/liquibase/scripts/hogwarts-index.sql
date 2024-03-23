-- liquibase formatted sql

--changeset AlexanderD:1
create index students_name_index ON students(name)

--changeset AlexanderD:2
create index faculties_nc_index ON faculties(name,color)