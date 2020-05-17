CREATE TABLE
    order_test
    (
        id BIGINT NOT NULL,
        order_id VARCHAR(50) NOT NULL,
        status INT(11) NOT NULL,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8