CREATE TABLE user_profile (
  id SERIAL PRIMARY KEY,

  first_name VARCHAR(40) NOT NULL,
  last_name VARCHAR(40) NOT NULL,
  date_of_birth date NOT NULL
);

CREATE TABLE address (
  address_id SERIAL PRIMARY KEY,
  user_profile_id INT NOT NULL,

  city VARCHAR(60) NOT NULL,
  line1 VARCHAR(100) NOT NULL,
  line2 VARCHAR(100),
  state VARCHAR(2) NOT NULL,
  zip_code VARCHAR(10) NOT NULL,

  address_type VARCHAR(10) NOT NULL,

  FOREIGN key (user_profile_id) REFERENCES user_profile (id)
);