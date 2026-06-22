CREATE TABLE users (
   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   role VARCHAR(50) NOT NULL,
   first_name VARCHAR(50) NOT NULL,
   middle_name VARCHAR(50),
   last_name VARCHAR(50) NOT NULL,
   username VARCHAR(25) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL UNIQUE,
   contact_number VARCHAR(20),
   status SMALLINT NOT NULL DEFAULT 1,
   created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

    CREATE INDEX idx_users_role
        ON users(role);

    CREATE INDEX idx_users_status
        ON users(status);

    CREATE INDEX idx_users_created_at
        ON users(created_at);

    CREATE INDEX idx_users_role_status
        ON users(role, status);