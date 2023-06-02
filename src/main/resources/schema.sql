-- executed by spring boot on H2 DB at test and application start time
CREATE TABLE IF NOT EXISTS p2p_table  (
  phone             VARCHAR(100) NOT NULL,
  device            VARCHAR(100)  NOT NULL,
  note           VARCHAR(100)  ,
    CONSTRAINT p2p_table primary key (phone,device)
);


CREATE TABLE IF NOT EXISTS p2p_user_table  (
  phone             VARCHAR(100) NOT NULL ,
  password            VARCHAR(100)  NOT NULL,
  primary key (phone)
);