#margot;Vacances2020;Assistant;true;01.01.2022
#alex;laVieEstBelle;Student;true;01.01.2010
#hugo;Lac@Geneve999;Student, Assistant;true;01.01.2021
#celine;HEG@Carouge_111;Teacher;true;01.01.2021
#fred;HEG@Battelle;Teacher, Admin;false;01.01.2021


insert into user_account(user_name, user_password, user_roles, is_active, expiration_date)
 values('margot','Vacances2020','Assistant',true,STR_TO_DATE('01.01.2022', '%d.%m.%Y'));

 
 insert into user_account(user_name, user_password, user_roles, is_active, expiration_date)
 values('alex','laVieEstBelle','Student',true,STR_TO_DATE('01.01.2010', '%d.%m.%Y'));
 
 
 insert into user_account(user_name, user_password, user_roles, is_active, expiration_date)
 values('hugo','Lac@Geneve999','Student',true,STR_TO_DATE('01.01.2021', '%d.%m.%Y'));
 
 
 insert into user_account(user_name, user_password, user_roles, is_active, expiration_date)
 values('celine','HEG@Carouge_111','Teacher',true,STR_TO_DATE('01.01.2021', '%d.%m.%Y'));
 
 
 insert into user_account(user_name, user_password, user_roles, is_active, expiration_date)
 values('fred','HEG@Battelle','Teacher, Admin',false,STR_TO_DATE('01.01.2021', '%d.%m.%Y'));
 
 commit;
