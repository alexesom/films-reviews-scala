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
VALUES ('admin', 'admin','admin', 'ac9689e2272427085e35b9d3e3e8bed88cb3434828b43b86fc0596cad4c6e270', true),
('user', 'user', 'user', '01da2f16f6bd99f056d0c4ace5b9e0b7c6e4cc2e810d69ffc0e002083898873d', false);
