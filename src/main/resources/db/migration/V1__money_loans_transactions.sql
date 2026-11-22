-- Brings a database created by the pre-refactor app up to the schema the entities
-- now expect: money as DECIMAL(19,2), optimistic locking, loans that an admin
-- reviews, and an account transaction history.
--
-- The script works on both an existing database (the CREATE statements are no-ops
-- and the columns are migrated in place) and an empty one (the CREATE statements
-- build the schema). Every statement is safe to re-run.

-- ---------------------------------------------------------------------------
-- accountdto
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS accountdto
(
    id      BIGINT NOT NULL AUTO_INCREMENT,
    balance DECIMAL(19, 2) DEFAULT NULL,
    user_id BIGINT         NOT NULL,
    version BIGINT         NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

-- Converts the old `double` column; a no-op when it is already DECIMAL.
ALTER TABLE accountdto
    MODIFY COLUMN balance DECIMAL(19, 2) DEFAULT NULL;

SET @ddl = IF(
        (SELECT COUNT(*)
         FROM information_schema.columns
         WHERE table_schema = DATABASE()
           AND table_name = 'accountdto'
           AND column_name = 'version') = 0,
        'ALTER TABLE accountdto ADD COLUMN version BIGINT NOT NULL DEFAULT 0',
        'DO 0');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ---------------------------------------------------------------------------
-- loandto
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS loandto
(
    id              BIGINT NOT NULL AUTO_INCREMENT,
    user_id         BIGINT NOT NULL,
    sanction_amount DECIMAL(19, 2) DEFAULT NULL,
    account_id      BIGINT NOT NULL DEFAULT 0,
    status          VARCHAR(20)    DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

ALTER TABLE loandto
    MODIFY COLUMN sanction_amount DECIMAL(19, 2) DEFAULT NULL;

SET @ddl = IF(
        (SELECT COUNT(*)
         FROM information_schema.columns
         WHERE table_schema = DATABASE()
           AND table_name = 'loandto'
           AND column_name = 'account_id') = 0,
        'ALTER TABLE loandto ADD COLUMN account_id BIGINT NOT NULL DEFAULT 0',
        'DO 0');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @ddl = IF(
        (SELECT COUNT(*)
         FROM information_schema.columns
         WHERE table_schema = DATABASE()
           AND table_name = 'loandto'
           AND column_name = 'status') = 0,
        'ALTER TABLE loandto ADD COLUMN status VARCHAR(20) DEFAULT NULL',
        'DO 0');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Loans from before the review flow have no status and were never credited to an
-- account, so they are closed out instead of showing up as pending requests.
UPDATE loandto
SET status = 'REJECTED'
WHERE status IS NULL;

-- ---------------------------------------------------------------------------
-- userdto
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS userdto
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    user_id  BIGINT NOT NULL,
    name     VARCHAR(255) DEFAULT NULL,
    username VARCHAR(30)  DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL,
    role     VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_userdto_username (username)
) ENGINE = InnoDB;

-- The signup rules cap usernames at 30 characters.
UPDATE userdto
SET username = CONCAT('user', id)
WHERE username IS NULL;

ALTER TABLE userdto
    MODIFY COLUMN username VARCHAR(30) NOT NULL;

-- ---------------------------------------------------------------------------
-- transactions
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS transactions
(
    id                      BIGINT NOT NULL AUTO_INCREMENT,
    account_id              BIGINT NOT NULL,
    type                    VARCHAR(20)    DEFAULT NULL,
    amount                  DECIMAL(19, 2) DEFAULT NULL,
    balance_after           DECIMAL(19, 2) DEFAULT NULL,
    `timestamp`             DATETIME(6)    DEFAULT NULL,
    counterparty_account_id BIGINT         DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_transactions_account_id (account_id)
) ENGINE = InnoDB;
