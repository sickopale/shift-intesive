CREATE TABLE department (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(50) NOT NULL
);

CREATE TABLE employee (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(50) NOT NULL,
                          salary DOUBLE PRECISION NOT NULL,
                          department_id INT REFERENCES department(id),
                          is_manager BOOLEAN NOT NULL
);