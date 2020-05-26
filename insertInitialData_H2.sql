
insert into user_account(user_name, user_password, user_roles, is_active, expiration_date)
 values('margot','Vacances2020','Assistant',true,TO_DATE('01.01.2022', 'dd.MM.yyyy'));
 
 insert into user_account(user_name, user_password, user_roles, is_active, expiration_date)
 values('alex','laVieEstBelle','Student',true,TO_DATE('01.01.2010', 'dd.MM.yyyy'));
 
 
 insert into user_account(user_name, user_password, user_roles, is_active, expiration_date)
 values('hugo','Lac@Geneve999','Student',true,TO_DATE('01.01.2021', 'dd.MM.yyyy'));
 
 
 insert into user_account(user_name, user_password, user_roles, is_active, expiration_date)
 values('celine','HEG@Carouge_111','Teacher',true,TO_DATE('01.01.2021', 'dd.MM.yyyy'));
 
 
 insert into user_account(user_name, user_password, user_roles, is_active, expiration_date)
 values('fred','HEG@Battelle','Teacher, Admin',false,TO_DATE('01.01.2021', '%d.%m.%Y'));
 
 commit;
