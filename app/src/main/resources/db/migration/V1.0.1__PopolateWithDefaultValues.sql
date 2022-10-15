COPY languages(lang)
FROM 'C:/Temp/languages.csv'
DELIMITER ','
CSV HEADER;

COPY countries(country)
FROM 'C:/Temp/countries.csv'
DELIMITER ','
CSV HEADER;

INSERT INTO public.users(
	first_name, second_name, login, password, is_admin)
VALUES ('admin', 'admin','admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', true),
('user', 'user', 'user', '01da2f16f6bd99f056d0c4ace5b9e0b7c6e4cc2e810d69ffc0e002083898873d', false);
