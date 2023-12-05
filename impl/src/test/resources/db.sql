CREATE SCHEMA skillbox;

CREATE TABLE country (
                         ID UUID PRIMARY KEY,
                         isDeleted BOOLEAN,
                         title VARCHAR(255));

CREATE TABLE city (
                      ID UUID PRIMARY KEY,
                      isDeleted BOOLEAN,
                      title VARCHAR(255),
                      country_id UUID,
                      FOREIGN KEY (country_id) REFERENCES country(ID));

INSERT INTO country (ID, isDeleted, title)
VALUES
    ('5ba60904-478f-423b-bacf-97969b6dacf3', false, 'Россия'),
    ('934e4688-b145-4a32-837e-bc3ae50236b4', false, 'Белорусь>');

INSERT INTO city (ID, isDeleted, title, country_id)
VALUES
    ('e2239f31-e533-4c7d-9a18-d9e9670c2d8a', false, 'Москва', '5ba60904-478f-423b-bacf-97969b6dacf3'),
    ('87e0a03a-347d-44af-b8d1-a312619b24d6', false, 'Санкт-Петербург', '5ba60904-478f-423b-bacf-97969b6dacf3'),
    ('d70c0920-31f1-41b2-b105-4283751f7578', false, 'Минск', '934e4688-b145-4a32-837e-bc3ae50236b4'),
    ('1c42bd63-7184-484d-9deb-dd75c9fc2756', false, 'Бобруйск', '934e4688-b145-4a32-837e-bc3ae50236b4'),
