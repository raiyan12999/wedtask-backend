-- Enable UUID generation (Supabase has this by default, but safe to include)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(100) NOT NULL UNIQUE,
    status     VARCHAR(10)  NOT NULL DEFAULT 'FREE'
                            CHECK (status IN ('FREE', 'BUSY')),
    is_admin   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Tasks table
CREATE TABLE IF NOT EXISTS tasks (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    assigned_to   VARCHAR(100),
    time_category VARCHAR(10)  NOT NULL
                               CHECK (time_category IN ('BEFORE', 'DURING', 'AFTER')),
    status        VARCHAR(10)  NOT NULL DEFAULT 'PENDING'
                               CHECK (status IN ('PENDING', 'DONE')),
    is_urgent     BOOLEAN      NOT NULL DEFAULT FALSE,
    scheduled_at  TIMESTAMP,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Indexes (make queries faster when filtering by these columns)
CREATE INDEX IF NOT EXISTS idx_tasks_assigned_to   ON tasks(assigned_to);
CREATE INDEX IF NOT EXISTS idx_tasks_time_category ON tasks(time_category);
CREATE INDEX IF NOT EXISTS idx_tasks_status        ON tasks(status);
CREATE INDEX IF NOT EXISTS idx_users_status        ON users(status);