DELETE from address;
DELETE from user_profile;

INSERT INTO
  user_profile (user_profile_id, first_name, last_name, date_of_birth)
VALUES
  (1, 'Richard', 'McDonnell', '1992-05-23'),
  (2, 'Alvaro', 'Gomez', '2002-06-23');

INSERT INTO
  address (address_id, user_profile_id, line1, city, state, zip_code, address_type)
VALUES
  (
    1,
    1,
    '123 Main St.',
    'San Jose',
    'CA',
    '94088',
    'HOME'
  );

INSERT INTO
   address (address_id, user_profile_id, line1, line2, city, state, zip_code, address_type)
VALUES
  (
    2,
    1,
    'Blue Cube',
    'Pod 143',
    'San Jose',
    'CA',
    '95008',
    'WORK'
  );